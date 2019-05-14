package lv.latvijaff.sugoinihongo.persistence.repos;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.subjects.Subject;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore.Keys.Sentences;
import lv.latvijaff.sugoinihongo.persistence.models.BaseModel;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.utils.FirebaseUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import lv.latvijaff.sugoinihongo.utils.helpers.CustomSorters;

public class SentenceRepo extends BaseRepo<SentenceModel> {

	private final String COLLECTION;

	public SentenceRepo(boolean isTestDbInUse) {
		COLLECTION = isTestDbInUse
			? Firestore.CollectionsTest.SENTENCES
			: Firestore.Collections.SENTENCES;
	}

	public ListenerRegistration processCollection(Consumer<List<SentenceModel>> consumer) {
		return processCollection(consumer, true);
	}

	public ListenerRegistration processCollection(Consumer<List<SentenceModel>> consumer, boolean hasLimit) {
		Function<Query, Query> query = x -> x
			.orderBy(Sentences.DATE_CREATED, Query.Direction.DESCENDING)
			.orderBy(Sentences.ENGLISH);

		if (hasLimit) {
			query = query.andThen(x -> x.limit(50));
		}

		return addSnapshotListener(COLLECTION, query, docs -> {
			// Firestore does not provide case-insensitive orderBy
			Comparator<SentenceModel> comparator = CustomSorters.sort(
				BaseModel::getDateCreated, CustomSorters.SortingExpression.DESCENDING,
				x -> x.getEnglish().toLowerCase());

			List<SentenceModel> list = docs.getDocuments().stream()
				.map(this::mapToModel)
				.sorted(comparator)
				.collect(Collectors.toList());

			consumer.accept(list);
		});
	}

	public void getCollection(Consumer<List<SentenceModel>> consumer) {
		getCollection(COLLECTION, consumer);
	}

	public ListenerRegistration processDocument(@NonNull String id, Consumer<SentenceModel> consumer) {
		return addSnapshotListener(COLLECTION, id, doc -> consumeDocument(doc, consumer));
	}

	public void getDocument(@NonNull String id, Consumer<SentenceModel> consumer) {
		addOnCompleteListener(COLLECTION, id, doc -> consumeDocument(doc, consumer));
	}

	public void saveDocument(SentenceModel model, Subject<Boolean> isSavedSubject) {
		Function<Query, Query> query = x -> x
			.whereEqualTo(Sentences.ENGLISH, StringUtils.trim(model.getEnglish()));

		addOnCompleteListener(COLLECTION, query, docs -> {
			boolean canBeSaved = canBeSaved(docs, model.getId());
			if (canBeSaved) {
				Map<String, Object> data = modelToMap(model);
				String id = model.getId();

				saveDocument(COLLECTION, id, data);
			}

			isSavedSubject.onNext(canBeSaved);
		});
	}

	@NonNull
	@Override
	SentenceModel mapToModel(DocumentSnapshot doc) {
		String id = doc.getId();

		SentenceModel model = new SentenceModel(id);
		model.setEnglish((String) doc.get(Sentences.ENGLISH));
		model.setTranslation((String) doc.get(Sentences.TRANSLATION));
		model.setTranscription((String) doc.get(Sentences.TRANSCRIPTION));

		Object dateCreated = doc.get(Sentences.DATE_CREATED);
		model.setDateCreated(FirebaseUtils.toLocalDate(dateCreated));

		return model;
	}

	@NonNull
	@Override
	Map<String, Object> modelToMap(SentenceModel model) {
		Map<String, Object> map = new HashMap<>();
		map.put(Sentences.ENGLISH, model.getEnglish());
		map.put(Sentences.TRANSLATION, model.getTranslation());
		map.put(Sentences.TRANSCRIPTION, model.getTranscription());

		Timestamp dateCreated = FirebaseUtils.toTimestamp(model.getDateCreated());
		map.put(Sentences.DATE_CREATED, dateCreated);

		return map;
	}

	private void consumeDocument(final DocumentSnapshot doc, final Consumer<SentenceModel> consumer) {
		SentenceModel model = mapToModel(doc);
		consumer.accept(model);
	}
}
