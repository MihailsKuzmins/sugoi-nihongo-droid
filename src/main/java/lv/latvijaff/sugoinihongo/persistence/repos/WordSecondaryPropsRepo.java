package lv.latvijaff.sugoinihongo.persistence.repos;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore.Keys.WordsSecondaryProps;
import lv.latvijaff.sugoinihongo.persistence.dto.WordToSecondaryProps;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.models.WordStatus;
import lv.latvijaff.sugoinihongo.utils.FirebaseUtils;
import lv.latvijaff.sugoinihongo.utils.WordSecondaryPropsUtils;

public class WordSecondaryPropsRepo extends BaseRepo<WordModel.SecondaryProps> {

	private final String COLLECTION;

	public WordSecondaryPropsRepo(boolean isTestDbInUse) {
		COLLECTION = isTestDbInUse
			? Firestore.CollectionsTest.WORDS_SECONDARY_PROPERTIES
			: Firestore.Collections.WORDS_SECONDARY_PROPERTIES;
	}

	public void getCollection(Consumer<List<WordModel.SecondaryProps>> consumer) {
		getCollection(COLLECTION, consumer);
	}

	public void reduceMarkForUnusedWords(int unusedWordDays) {
		LocalDate checkDate = LocalDate.now().minusDays(unusedWordDays);
		Timestamp timestamp = FirebaseUtils.toTimestamp(checkDate);

		Function<Query, Query> query = x -> x
			.whereLessThan(WordsSecondaryProps.DATE_LAST_ACCESSED, timestamp);

		addOnCompleteListener(COLLECTION, query, docs -> docs.getDocuments().stream()
			.map(this::mapToModel)
			.filter(x -> x.getMark() > AppConstants.Models.MIN_MARK)
			.forEach(x -> {
				int mark = x.getMark();
				x.setMark(mark - 1);

				saveDocumentInternal(x);
			}));
	}

	void getWordIdsForStudy(int limit, Consumer<List<WordToSecondaryProps>> consumer) {
		Function<Query, Query> query = x -> x
			.whereEqualTo(WordsSecondaryProps.IS_STUDIABLE, true)
			.orderBy(WordsSecondaryProps.MARK)
			.orderBy(WordsSecondaryProps.DATE_LAST_ACCESSED)
			.limit(limit);

		addOnCompleteListener(COLLECTION, query, docs -> {
			List<WordToSecondaryProps> ids = getIdsList(docs);
			consumer.accept(ids);
		});
	}

	public ListenerRegistration processDocument(@NonNull String wordId, Consumer<WordModel.SecondaryProps> consumer) {
		Function<Query, Query> query = x -> x
			.whereEqualTo(WordsSecondaryProps.WORD_ID, wordId);

		return addSnapshotListener(COLLECTION, query, docs -> {
			// WordId is a unique identified for the SubCollection
			QueryDocumentSnapshot doc = docs.iterator().next();
			WordModel.SecondaryProps model = mapToModel(doc);

			consumer.accept(model);
		});
	}

	public void saveDocument(WordModel.SecondaryProps model) {
		saveDocumentInternal(model);
	}

	public void updateDocument(String id, boolean isCorrect) {
		addOnCompleteListener(COLLECTION, id, doc -> {
			WordModel.SecondaryProps model = mapToModel(doc);
			model.setDateLastAccessed(LocalDateTime.now());

			int newMark = updateMark(model.getMark(), isCorrect);
			model.setMark(newMark);

			saveDocumentInternal(model);
		});
	}

	public ListenerRegistration processListGroupingByStatus(Consumer<Map<WordStatus, Integer>> consumer) {
		return addSnapshotListener(COLLECTION, docs -> {
			Map<WordStatus, Integer> grouping = WordStatus.initMap();

			for (QueryDocumentSnapshot doc : docs) {
				WordStatus status = WordSecondaryPropsUtils.getWordStatus(mapToModel(doc));
				grouping.computeIfPresent(status, (k, v) -> v + 1);
			}

			consumer.accept(grouping);
		});
	}

	public void resetWordProgress(final Runnable runnable) {
		final int minMark = AppConstants.Models.MIN_MARK;

		Function<Query, Query> query = x -> x
			.whereGreaterThan(WordsSecondaryProps.MARK, minMark)
			.whereEqualTo(WordsSecondaryProps.IS_STUDIABLE, true);

		addOnCompleteListener(COLLECTION, query, docs -> {
			docs.getDocuments().parallelStream()
				.map(this::mapToModel)
				.forEach(x -> {
					x.setMark(minMark);
					saveDocumentInternal(x);
				});

			runnable.run();
		});
	}

	ListenerRegistration processFavourites(Consumer<List<WordToSecondaryProps>> consumer) {
		Function<Query, Query> query = x -> x
			.whereEqualTo(WordsSecondaryProps.IS_FAVOURITE, true);

		return addSnapshotListener(COLLECTION, query, docs -> {
			List<WordToSecondaryProps> ids = getIdsList(docs);
			consumer.accept(ids);
		});
	}

	@NonNull
	@Override
	WordModel.SecondaryProps mapToModel(DocumentSnapshot doc) {
		String id = doc.getId();

		WordModel.SecondaryProps model = new WordModel.SecondaryProps(id);
		model.setWordId((String) doc.get(WordsSecondaryProps.WORD_ID));
		model.setStudiable((boolean) doc.get(WordsSecondaryProps.IS_STUDIABLE));
		model.setFavourite((boolean) doc.get(WordsSecondaryProps.IS_FAVOURITE));

		long mark = (long) doc.get(WordsSecondaryProps.MARK);
		model.setMark(Math.toIntExact(mark));

		Object dateLastAccessed = doc.get(WordsSecondaryProps.DATE_LAST_ACCESSED);
		model.setDateLastAccessed(FirebaseUtils.toLocalDateTime(dateLastAccessed));

		return model;
	}

	@NonNull
	@Override
	Map<String, Object> modelToMap(WordModel.SecondaryProps model) {
		Map<String, Object> map = new HashMap<>();
		map.put(WordsSecondaryProps.WORD_ID, model.getWordId());
		map.put(WordsSecondaryProps.MARK, model.getMark());
		map.put(WordsSecondaryProps.IS_FAVOURITE, model.isFavourite());
		map.put(WordsSecondaryProps.IS_STUDIABLE, model.isStudiable());

		Timestamp dateLastAccessed = FirebaseUtils.toTimestamp(model.getDateLastAccessed());
		map.put(WordsSecondaryProps.DATE_LAST_ACCESSED, dateLastAccessed);

		return map;
	}

	private void saveDocumentInternal(WordModel.SecondaryProps model) {
		String id = model.getId();
		Map<String, Object> map = modelToMap(model);

		saveDocument(COLLECTION, id, map);
	}

	private static int updateMark(int mark, boolean isCorrect) {
		int minMark = AppConstants.Models.MIN_MARK;
		int maxMark = AppConstants.Models.MAX_MARK;

		if (isCorrect && mark < maxMark) {
			return mark + 1;
		} else if (!isCorrect && mark > minMark) {
			return mark - 1;
		}

		return mark;
	}

	private List<WordToSecondaryProps> getIdsList(QuerySnapshot docs) {
		return docs.getDocuments().stream()
			.map(x -> {
				String secondaryPropsId = x.getId();
				String wordId = (String) x.get(WordsSecondaryProps.WORD_ID);

				return new WordToSecondaryProps(wordId, secondaryPropsId);
			}).collect(Collectors.toList());
	}
}
