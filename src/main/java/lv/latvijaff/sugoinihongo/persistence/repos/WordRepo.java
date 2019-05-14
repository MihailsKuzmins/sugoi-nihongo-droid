package lv.latvijaff.sugoinihongo.persistence.repos;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.subjects.Subject;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore.Keys.Words;
import lv.latvijaff.sugoinihongo.persistence.dto.QuizItemDto;
import lv.latvijaff.sugoinihongo.persistence.dto.WordToSecondaryProps;
import lv.latvijaff.sugoinihongo.persistence.models.BaseModel;
import lv.latvijaff.sugoinihongo.persistence.models.QuizModel;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.utils.FirebaseUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import lv.latvijaff.sugoinihongo.utils.helpers.CustomCollectors;
import lv.latvijaff.sugoinihongo.utils.helpers.CustomSorters;
import lv.latvijaff.sugoinihongo.utils.helpers.WordAndSecondaryPropsListenerRegistration;

public class WordRepo extends BaseRepo<WordModel> {

	private final String COLLECTION;

	@Inject
	WordSecondaryPropsRepo secondaryPropsRepo;

	public WordRepo(boolean isTestDbInUse, App app) {
		app.getRepoComponent().inject(this);

		COLLECTION = isTestDbInUse
			? Firestore.CollectionsTest.WORDS
			: Firestore.Collections.WORDS;
	}

	public ListenerRegistration processCollection(Consumer<List<WordModel>> consumer) {
		return processCollection(consumer, true);
	}

	public ListenerRegistration processCollection(Consumer<List<WordModel>> consumer, boolean hasLimit) {
		Function<Query, Query> query = x -> x
			.orderBy(Words.DATE_CREATED, Query.Direction.DESCENDING)
			.orderBy(Words.ENGLISH);

		if (hasLimit) {
			query = query.andThen(x -> x.limit(25));
		}

		return addSnapshotListener(COLLECTION, query, docs -> {
			// Firestore does not provide case-insensitive orderBy
			Comparator<WordModel> comparator = CustomSorters.sort(
				BaseModel::getDateCreated, CustomSorters.SortingExpression.DESCENDING,
				x -> x.getEnglish().toLowerCase());

			List<WordModel> list = docs.getDocuments().stream()
				.map(this::mapToModel)
				.sorted(comparator)
				.collect(Collectors.toList());

			consumer.accept(list);
		});
	}

	public void getCollection(Consumer<List<WordModel>> consumer) {
		getCollection(COLLECTION, consumer);
	}

	public ListenerRegistration processDocument(@NonNull String id, Consumer<WordModel> consumer) {
		return addSnapshotListener(COLLECTION, id, doc -> consumeDocument(doc, consumer));
	}

	public void getDocument(@NonNull String id, Consumer<WordModel> consumer) {
		addOnCompleteListener(COLLECTION, id, doc -> consumeDocument(doc, consumer));
	}

	public void saveDocument(WordModel model, Subject<Boolean> isSavedSubject) {
		Function<Query, Query> query = x -> x
			.whereEqualTo(Words.ENGLISH, StringUtils.trim(model.getEnglish()));

		addOnCompleteListener(COLLECTION, query, docs -> {
			boolean canSave = canBeSaved(docs, model.getId());
			if (canSave) {
				Map<String, Object> data = modelToMap(model);
				String id = model.getId();

				final String firestoreId = saveDocument(COLLECTION, id, data);

				// For existing words SecondaryProps are null (not fetched from firestore)
				ObjectUtils.invokeIfNotNull(model.getSecondaryProps(), x -> {
					x.setWordId(firestoreId);
					secondaryPropsRepo.saveDocument(x);
				});
			}

			isSavedSubject.onNext(canSave);
		});
	}

	public void getStudyWordList(int limit, Consumer<List<WordModel>> consumer) {
		secondaryPropsRepo.getWordIdsForStudy(limit, ids -> {
			final List<String> wordIds = getWordIds(ids);
			Function<Query, Query> query = getWordQueryByWordIds(wordIds);

			addOnCompleteListener(COLLECTION, query, docs -> {
				List<WordModel> list = filterQuerySnapshotByWordIds(docs, wordIds);

				Collections.shuffle(list);
				consumer.accept(list);
			});
		});
	}

	public void getStudyQuizList(int limit, int minOptionsCount, int maxOptionsCount, Consumer<Stack<QuizModel>> consumer) {
		secondaryPropsRepo.getWordIdsForStudy(limit, ids -> addOnCompleteListener(COLLECTION, docs -> {
				List<WordModel> words = docs.getDocuments().parallelStream()
					.map(this::mapToModel)
					.collect(Collectors.toList());

				List<QuizModel> list = ids.stream()
					.map(x -> {
						String wordId = x.getWordId();
						String secondaryPropsId = x.getSecondaryPropsId();

						WordModel word = words.parallelStream()
							.filter(y -> y.getId().equals(wordId))
							.collect(CustomCollectors.toSingleton());

						int i = words.indexOf(word);
						QuizItemDto question = wordModelToQuizDto(word, true);

						QuizModel quizModel = new QuizModel(secondaryPropsId, question);

						List<QuizItemDto> options = createOptionsList(i, maxOptionsCount, minOptionsCount, words);
						quizModel.setOptions(options);

						return quizModel;
					}).collect(Collectors.toList());

				Collections.shuffle(list);

				Stack<QuizModel> stack = new Stack<>();
				stack.addAll(list);

				consumer.accept(stack);
			}));
	}

	public WordAndSecondaryPropsListenerRegistration processFavourites(Consumer<List<WordModel>> consumer) {
		WordAndSecondaryPropsListenerRegistration registration = new WordAndSecondaryPropsListenerRegistration();

		ListenerRegistration secondaryPropsRegistration = secondaryPropsRepo.processFavourites(ids -> {
			final List<String> wordIds = getWordIds(ids);
			Function<Query, Query> query = getWordQueryByWordIds(wordIds);

			ListenerRegistration wordRegistration = addSnapshotListener(COLLECTION, query, docs -> {
				List<WordModel> list = filterQuerySnapshotByWordIds(docs, wordIds);
				list.sort(CustomSorters.sort(x -> x.getEnglish().toLowerCase()));

				consumer.accept(list);
			});

			registration.setWordListenerRegistration(wordRegistration);
		});

		registration.setSecondaryPropsListenerRegistration(secondaryPropsRegistration);

		return registration;
	}

	@NonNull
	@Override
	WordModel mapToModel(DocumentSnapshot doc) {
		String id = doc.getId();

		WordModel model = new WordModel(id);
		model.setEnglish((String) doc.get(Words.ENGLISH));
		model.setTranslation((String) doc.get(Words.TRANSLATION));
		model.setTranscription((String) doc.get(Words.TRANSCRIPTION));
		model.setNotes((String) doc.get(Words.NOTES));

		Object dateCreated = doc.get(Words.DATE_CREATED);
		model.setDateCreated(FirebaseUtils.toLocalDate(dateCreated));

		return model;
	}

	@NonNull
	@Override
	Map<String, Object> modelToMap(WordModel model) {
		Map<String, Object> map = new HashMap<>();
		map.put(Words.ENGLISH, model.getEnglish());
		map.put(Words.TRANSLATION, model.getTranslation());
		map.put(Words.TRANSCRIPTION, model.getTranscription());
		map.put(Words.NOTES, model.getNotes());

		Timestamp dateCreated = FirebaseUtils.toTimestamp(model.getDateCreated());
		map.put(Words.DATE_CREATED, dateCreated);

		return map;
	}

	private void consumeDocument(final DocumentSnapshot doc, final Consumer<WordModel> consumer) {
		WordModel model = mapToModel(doc);
		consumer.accept(model);
	}

	private List<QuizItemDto> createOptionsList(int i, int maxOptionsCount, int minOptionsCount, List<WordModel> words) {
		Random random = new Random();
		// "+1" includes the lower bound; "-1" preserve one place for the correct answer
		int listCapacity = random.nextInt(maxOptionsCount - minOptionsCount + 1) + minOptionsCount - 1;

		List<Integer> optionsIds = new ArrayList<>(listCapacity);
		List<QuizItemDto> optionsList = new ArrayList<>(listCapacity);

		int size = words.size();
		while (optionsIds.size() != listCapacity) {
			int randomNum = random.nextInt(size);

			// Options must NOT contain the question and duplicate WordModels
			if (randomNum != i && !optionsIds.contains(randomNum)) {
				optionsIds.add(randomNum);

				WordModel optionsWordModel = words.get(randomNum);
				optionsList.add(wordModelToQuizDto(optionsWordModel, false));
			}
		}

		return optionsList;
	}

	private QuizItemDto wordModelToQuizDto(WordModel model, boolean isAnswer) {
		return new QuizItemDto(model.getEnglish(), model.getTranslation(), model.getTranscription(), isAnswer);
	}

	private List<String> getWordIds(List<WordToSecondaryProps> ids) {
		return ids.stream()
			.map(WordToSecondaryProps::getWordId)
			.collect(Collectors.toList());
	}

	private Function<Query, Query> getWordQueryByWordIds(List<String> wordIds) {
		String min = Collections.min(wordIds);
		String max = Collections.max(wordIds);

		return x -> x
			.whereGreaterThanOrEqualTo(FieldPath.documentId(), min)
			.whereLessThanOrEqualTo(FieldPath.documentId(), max);
	}

	private List<WordModel> filterQuerySnapshotByWordIds(QuerySnapshot docs, List<String> wordIds) {
		return docs.getDocuments().stream()
			.filter(x -> wordIds.contains(x.getId()))
			.map(this::mapToModel)
			.collect(Collectors.toList());
	}
}
