package lv.latvijaff.sugoinihongo.features.sentence;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.base.HasFabFunctionality;
import lv.latvijaff.sugoinihongo.features.android.activities.SentenceEditContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.SentenceSearchListContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;

public class SentenceListFragment extends BaseListFragment<SentenceModel, SentenceListViewModel>
	implements HasFabFunctionality {

	public SentenceListFragment() {
		super(SentenceListViewModel.class);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		FragmentUtils.inflateListFragmentMenu(menu, inflater);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);
		FragmentUtils.setSearchButtonEnabled(menu, listHasItems());
	}

	@Override
	protected void processList(SentenceListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@Override
	protected void setupSubscriptions(SentenceListViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable listHasItemsDisp = viewModel.observeListHasItems()
			.subscribe(this::setListHasItems);

		cleanUp.add(listHasItemsDisp);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<SentenceModel> list) {
		return new SentenceListAdapter(list);
	}

	@Override
	public void onFabClickedAction(@NonNull Context context) {
		Intent intent = new Intent(context, SentenceEditContentActivity.class);
		context.startActivity(intent);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		if (id == R.id.menu_item_list_base_search) {
			return this::onSearchAction;
		}

		return null;
	}

	private void onSearchAction() {
		Intent intent = new Intent(getContext(), SentenceSearchListContentActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
}
