package lv.latvijaff.sugoinihongo.base;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import io.reactivex.Observable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.persistence.models.Model;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import timber.log.Timber;

public abstract class BaseDetailEditViewModel<TModel extends Model> extends BaseViewModel {

	private TModel mModel;

	public BaseDetailEditViewModel(@NonNull App application) {
		super(application);
	}

	public void processDocument(String id) {
		// If model is not null, then screen has been rotated and the existing model should be retrieved
		if (mModel != null) {
			Timber.d("Model exists in the ViewModel: %s", mModel.getId());
			getModelConsumer().accept(mModel);
			return;
		}

		if (StringUtils.isNullOrEmpty(id)) {
			Timber.d("Id is empty. Creating a new model");
			getModelConsumer().accept(mModel = createEmptyModel());
			return;
		}

		Timber.d("Getting a model from repository. Id: %s", id);
		fetchDocument(id, getModelConsumer().andThen(x -> {
			this.mModel = x;
			Timber.d("Model is assigned");
		}));
	}

	@NonNull
	public abstract Observable<Boolean> observeIsSaved();

	protected final TModel getModel() {
		return mModel;
	}

	@NonNull
	protected abstract TModel createEmptyModel();

	@NonNull
	protected abstract Consumer<TModel> getModelConsumer();

	protected abstract void fetchDocument(String id, Consumer<TModel> consumer);
}
