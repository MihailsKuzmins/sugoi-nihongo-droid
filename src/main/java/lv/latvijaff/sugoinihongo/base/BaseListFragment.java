package lv.latvijaff.sugoinihongo.base;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.persistence.models.Model;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public abstract class BaseListFragment<TModel extends Model, TViewModel extends BaseListViewModel<TModel>>
	extends BaseFragment<TViewModel> {

	private Unbinder mUnbinder;
	private boolean mListHasItems;

	@BindView(R.id.base_list_fragment_header_view)
	FrameLayout headerViewContainer;

	@BindView(R.id.base_list_fragment_recycler_view)
	RecyclerView recyclerView;

	protected BaseListFragment(Class<TViewModel> viewModelClass) {
		super(viewModelClass);
	}

	@Override
	public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_base_list, container, false);
		mUnbinder = ButterKnife.bind(this, view);

		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		TViewModel viewModel = getViewModel();
		processList(viewModel);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		View headerView = createHeaderView(viewModel);
		if (headerView != null) {
			headerViewContainer.addView(headerView);
		}

		return view;
	}

	@Override
	public void onDestroyView() {
		ObjectUtils.invokeIfNotNull(mUnbinder, Unbinder::unbind);
		super.onDestroyView();
	}

	protected final boolean listHasItems() {
		return mListHasItems;
	}

	protected abstract void processList(TViewModel viewModel);

	@NonNull
	protected abstract BaseListAdapter getListAdapter(List<TModel> list);

	protected final void setAdapter(List<TModel> list) {
		ObjectUtils.invokeIfNotNull(recyclerView, x -> {
			BaseListAdapter adapter = getListAdapter(list);
			x.setAdapter(adapter);
		});
	}

	protected final void setListHasItems(boolean listHasItems) {
		mListHasItems = listHasItems;
		ObjectUtils.invokeIfNotNull(getActivity(), Activity::invalidateOptionsMenu);
	}

	@Nullable
	protected View createHeaderView(TViewModel viewModel) {
		return null;
	}
}
