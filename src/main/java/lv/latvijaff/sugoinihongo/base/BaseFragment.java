package lv.latvijaff.sugoinihongo.base;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.ViewModelFactory;
import lv.latvijaff.sugoinihongo.ui.events.CanNavigateBackEvent;
import lv.latvijaff.sugoinihongo.utils.EventBusUtils;
import lv.latvijaff.sugoinihongo.utils.RunnableUtils;

abstract class BaseFragment<TViewModel extends BaseViewModel> extends Fragment {

	private final Class<TViewModel> mViewModelClass;
	private final CompositeDisposable mCleanUp = new CompositeDisposable();
	private TViewModel mViewModel;

	BaseFragment(Class<TViewModel> viewModelClass) {
		mViewModelClass = viewModelClass;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App mApp = (App) Objects.requireNonNull(getActivity()).getApplication();
		mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(mApp)).get(mViewModelClass);
		setHasOptionsMenu(true);
	}

	@Override
	public final boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		Runnable runnable = getOnMenuItemSelectedAction(id);

		if (runnable != null) {
			RunnableUtils.runWithMultipleClickPrevention(runnable);
			return true;
		}

		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		setupSubscriptions(mViewModel, mCleanUp);
	}

	@Override
	public void onPause() {
		mCleanUp.clear();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		mCleanUp.dispose();
		super.onDestroy();
	}

	protected final TViewModel getViewModel() {
		return mViewModel;
	}

	public void beforeNavigatingFrom() {
		EventBusUtils.post(new CanNavigateBackEvent(true));
	}

	@Nullable
	protected Runnable getOnMenuItemSelectedAction(int id) {
		return null;
	}

	protected void setupSubscriptions(final TViewModel viewModel, final CompositeDisposable cleanUp) {
		mViewModel.setupSubscriptions(cleanUp);
	}
}
