package lv.latvijaff.sugoinihongo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public abstract class BaseDetailFragment<TViewModel extends BaseViewModel, TBinding extends ViewDataBinding>
	extends BaseFragment<TViewModel> {

	@LayoutRes
	private final int mLayoutId;
	private Unbinder mUnbinder;

	protected BaseDetailFragment(Class<TViewModel> viewModelClass, @LayoutRes int layoutId) {
		super(viewModelClass);
		mLayoutId = layoutId;
	}

	@NonNull
	@Override
	public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		TBinding binding = DataBindingUtil.inflate(inflater, mLayoutId, container, false);
		View view = binding.getRoot();

		mUnbinder = ButterKnife.bind(this, view);

		TViewModel viewModel = getViewModel();
		applyBinding(binding, viewModel);

		return view;
	}

	@Override
	public void onDestroyView() {
		ObjectUtils.invokeIfNotNull(mUnbinder, Unbinder::unbind);
		super.onDestroyView();
	}

	protected abstract void applyBinding(final @NonNull TBinding binding, final @NonNull TViewModel viewModel);
}
