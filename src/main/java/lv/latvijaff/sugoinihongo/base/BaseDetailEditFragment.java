package lv.latvijaff.sugoinihongo.base;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.BaseModel;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public abstract class BaseDetailEditFragment<TModel extends BaseModel, TViewModel extends BaseDetailEditViewModel<TModel>, TBinding extends ViewDataBinding>
	extends BaseDetailFragment<TViewModel, TBinding> {

	private boolean mIsValid;
	private AlertDialog mCannotSaveAlertDialog;

	protected BaseDetailEditFragment(Class<TViewModel> viewModelClass, int layoutId) {
		super(viewModelClass, layoutId);
	}

	protected final boolean isValid() {
		return mIsValid;
	}

	protected final String getModelId() {
		Bundle bundle = Objects.requireNonNull(getArguments());
		return bundle.getString(AppConstants.Keys.Params.DOCUMENT_ID);
	}

	protected final void setIsValid(boolean isValid) {
		mIsValid = isValid;
		ObjectUtils.invokeIfNotNull(getActivity(), Activity::invalidateOptionsMenu);
	}

	@Override
	protected void setupSubscriptions(TViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable isSavedDisp = viewModel.observeIsSaved()
			.filter(x -> x)
			.subscribe(x -> FragmentUtils.finishActivity(this));

		Disposable isNotSavedDisp = viewModel.observeIsSaved()
			.filter(x -> !x)
			.subscribe(x -> showNotSavedAlertDialog());

		cleanUp.addAll(isSavedDisp, isNotSavedDisp);
	}

	private void showNotSavedAlertDialog() {
		if (mCannotSaveAlertDialog == null) {
			mCannotSaveAlertDialog = new AlertDialog.Builder(getContext())
				.setMessage(R.string.validation_constraint_error)
				.setNeutralButton(android.R.string.ok, (dialog, id) -> dialog.dismiss())
				.create();
		}

		String errorMessage = createErrorMessage();
		mCannotSaveAlertDialog.setMessage(errorMessage);

		mCannotSaveAlertDialog.show();
	}

	protected abstract String createErrorMessage();
}
