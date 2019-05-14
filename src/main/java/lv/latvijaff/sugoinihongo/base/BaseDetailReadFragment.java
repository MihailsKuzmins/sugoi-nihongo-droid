package lv.latvijaff.sugoinihongo.base;


import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import java.util.Objects;

import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.Model;

public abstract class BaseDetailReadFragment<TModel extends Model, TViewModel extends BaseDetailReadViewModel<TModel>, TBinding extends ViewDataBinding>
	extends BaseDetailFragment<TViewModel, TBinding> {

	protected BaseDetailReadFragment(Class<TViewModel> viewModelClass, int layoutId) {
		super(viewModelClass, layoutId);
	}

	protected Intent createNavigationIntent(Class<?> activityClass) {
		String id = getModelId();

		Intent intent = new Intent(getContext(), activityClass);
		intent.putExtra(AppConstants.Keys.Params.DOCUMENT_ID, id);

		return intent;
	}

	protected final String getModelId() {
		Bundle bundle = Objects.requireNonNull(getArguments());
		return bundle.getString(AppConstants.Keys.Params.DOCUMENT_ID);
	}
}
