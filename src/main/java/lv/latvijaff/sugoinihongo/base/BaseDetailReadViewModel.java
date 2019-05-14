package lv.latvijaff.sugoinihongo.base;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.function.Consumer;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.persistence.models.Model;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public abstract class BaseDetailReadViewModel<TModel extends Model> extends BaseViewModel {

	private TModel mModel;
	private ListenerRegistration mDocumentSubscription;

	public BaseDetailReadViewModel(@NonNull App application) {
		super(application);
	}

	@Override
	protected void onCleared() {
		ObjectUtils.invokeIfNotNull(mDocumentSubscription, ListenerRegistration::remove);
		super.onCleared();
	}

	public void processDocument(String id) {
		// If model is not null, then screen has been rotated and the existing model should be retrieved
		if (mModel != null) {
			getModelConsumer().accept(mModel);
			return;
		}

		mDocumentSubscription = fetchDocument(id, getModelConsumer().andThen(x -> this.mModel = x));
	}

	protected final TModel getModel() {
		return mModel;
	}

	@NonNull
	protected abstract Consumer<TModel> getModelConsumer();

	@NonNull
	protected abstract ListenerRegistration fetchDocument(String id, Consumer<TModel> consumer);
}
