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
import lv.latvijaff.sugoinihongo.constants.AppConstants.Firestore.Keys.GrammarRules;
import lv.latvijaff.sugoinihongo.persistence.models.BaseModel;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.utils.FirebaseUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import lv.latvijaff.sugoinihongo.utils.helpers.CustomSorters;

public class GrammarRuleRepo extends BaseRepo<GrammarRuleModel> {

	private final String COLLECTION;

	public GrammarRuleRepo(boolean isTestDbInUse) {
		COLLECTION = isTestDbInUse
			? Firestore.CollectionsTest.GRAMMAR_RULES
			: Firestore.Collections.GRAMMAR_RULES;
	}

	public ListenerRegistration processCollection(Consumer<List<GrammarRuleModel>> consumer) {
		return processCollection(true, consumer);
	}

	public ListenerRegistration processCollection(boolean hasLimit, Consumer<List<GrammarRuleModel>> consumer) {
		Function<Query, Query> query = x -> x
			.orderBy(GrammarRules.DATE_CREATED, Query.Direction.DESCENDING)
			.orderBy(GrammarRules.HEADER);

		if (hasLimit) {
			query = query.andThen(x -> x.limit(50));
		}

		return addSnapshotListener(COLLECTION, query, docs -> {
			// Firestore does not provide case-insensitive orderBy
			Comparator<GrammarRuleModel> comparator = CustomSorters.sort(
				BaseModel::getDateCreated, CustomSorters.SortingExpression.DESCENDING,
				x -> x.getHeader().toLowerCase());

			List<GrammarRuleModel> list = docs.getDocuments().stream()
				.map(this::mapToModel)
				.sorted(comparator)
				.collect(Collectors.toList());

			consumer.accept(list);
		});
	}

	public void getCollection(Consumer<List<GrammarRuleModel>> consumer) {
		getCollection(COLLECTION, consumer);
	}

	public ListenerRegistration processDocument(@NonNull final String id, Consumer<GrammarRuleModel> consumer) {
		return addSnapshotListener(COLLECTION, id, doc -> consumeDocument(doc, consumer));
	}

	public void getDocument(@NonNull final String id, Consumer<GrammarRuleModel> consumer) {
		addOnCompleteListener(COLLECTION, id, doc -> consumeDocument(doc, consumer));
	}

	public void saveDocument(GrammarRuleModel model, Subject<Boolean> isSavedSubject) {
		Function<Query, Query> query = x -> x
			.whereEqualTo(GrammarRules.HEADER, StringUtils.trim(model.getHeader()));

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
	GrammarRuleModel mapToModel(DocumentSnapshot doc) {
		String id = doc.getId();

		GrammarRuleModel model = new GrammarRuleModel(id);
		model.setHeader((String) doc.get(GrammarRules.HEADER));
		model.setBody((String) doc.get(GrammarRules.BODY));

		Object dateCreated = doc.get(GrammarRules.DATE_CREATED);
		model.setDateCreated(FirebaseUtils.toLocalDate(dateCreated));

		return model;
	}

	@NonNull
	@Override
	Map<String, Object> modelToMap(GrammarRuleModel model) {
		Map<String, Object> map = new HashMap<>();
		map.put(GrammarRules.HEADER, model.getHeader());
		map.put(GrammarRules.BODY, model.getBody());

		Timestamp dateCreated = FirebaseUtils.toTimestamp(model.getDateCreated());
		map.put(GrammarRules.DATE_CREATED, dateCreated);

		return map;
	}

	private void consumeDocument(final DocumentSnapshot doc, final Consumer<GrammarRuleModel> consumer) {
		GrammarRuleModel model = mapToModel(doc);
		consumer.accept(model);
	}
}
