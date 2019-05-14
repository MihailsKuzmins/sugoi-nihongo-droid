package lv.latvijaff.sugoinihongo.persistence.repos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import lv.latvijaff.sugoinihongo.persistence.models.Model;
import lv.latvijaff.sugoinihongo.utils.FirebaseUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import timber.log.Timber;

abstract class BaseRepo<TModel extends Model> {

	private final FirebaseFirestore db = FirebaseFirestore.getInstance();

	@NonNull
	abstract TModel mapToModel(DocumentSnapshot doc);

	@NonNull
	abstract Map<String, Object> modelToMap(TModel model);

	void getCollection(@NonNull final String collection, Consumer<List<TModel>> consumer) {
		addOnCompleteListener(collection, docs -> {
			List<TModel> list = docs.getDocuments().stream()
				.map(this::mapToModel)
				.collect(Collectors.toList());

			consumer.accept(list);
		});
	}

	ListenerRegistration addSnapshotListener(@NonNull final String collection, Consumer<QuerySnapshot> consumer) {
		return addSnapshotListener(collection, query -> null, consumer);
	}

	ListenerRegistration addSnapshotListener(@NonNull final String collection, @NonNull Function<Query, Query> func, Consumer<QuerySnapshot> consumer) {
		CollectionReference ref = db.collection(collection);
		Query query = MoreObjects.firstNonNull(func.apply(ref), ref);

		return query.addSnapshotListener((doc, ex) -> {
			if (ex != null) {
				Timber.e(ex);
				return;
			}

			if (doc != null) {
				Timber.i("Collection elements: %s", doc.getDocuments().size());
				consumer.accept(doc);
			} else {
				Timber.d("Collection is empty");
			}
		});
	}

	ListenerRegistration addSnapshotListener(@NonNull final String collection, @NonNull final String id, Consumer<DocumentSnapshot> consumer) {
		return db.collection(collection).document(id)
			.addSnapshotListener((doc, ex) -> {
				if (ex != null) {
					Timber.e(ex, "Failed to listen to data");
					return;
				}

				if (doc != null && doc.exists()) {
					Timber.i("Document id: %s", doc.getId());
					consumer.accept(doc);
				} else {
					Timber.d("No such document");
				}
			});
	}

	void addOnCompleteListener(@NonNull final String collection, Consumer<QuerySnapshot> consumer) {
		addOnCompleteListener(collection, query -> null, consumer);
	}

	void addOnCompleteListener(@NonNull final String collection, @NonNull Function<Query, Query> func, Consumer<QuerySnapshot> consumer) {
		CollectionReference ref = db.collection(collection);
		Query query = MoreObjects.firstNonNull(func.apply(ref), ref);

		query.get().addOnCompleteListener(task -> {
			if (!task.isSuccessful()) {
				Timber.e(task.getException());
				return;
			}

			QuerySnapshot doc = task.getResult();
			if (doc != null) {
				Timber.i("Collection elements: %s", doc.getDocuments().size());
				consumer.accept(doc);
			} else {
				Timber.d("No such document");
			}
		});
	}

	void addOnCompleteListener(@NonNull final String collection, @NonNull final String id, Consumer<DocumentSnapshot> consumer) {
		db.collection(collection).document(id).get().addOnCompleteListener(task -> {
			if (!task.isSuccessful()) {
				return;
			}

			DocumentSnapshot doc = task.getResult();
			if (doc != null && doc.exists()) {
				Timber.i("Document id: %s", doc.getId());
				consumer.accept(doc);
			} else {
				Timber.d("No such document");
			}
		});
	}

	String saveDocument(final String collection, final @Nullable String id, Map<String, Object> data) {
		String firestoreId = FirebaseUtils.getFireStoreId(id);

		trimAttributes(data);

		Timber.i("Saving the document: %s", firestoreId);
		db.collection(collection).document(firestoreId)
			.set(data);

		return firestoreId;
	}

	boolean canBeSaved(QuerySnapshot docs, String id) {
		return docs.size() == 0 || docs.getDocuments().get(0).getId().equals(id);
	}

	private void trimAttributes(final Map<String, Object> data) {
		data.entrySet().stream()
			.filter(x -> x.getValue() instanceof String)
			.forEach(x -> {
				String newValue = StringUtils.trim((String) x.getValue());
				data.put(x.getKey(), newValue);
			});
	}
}
