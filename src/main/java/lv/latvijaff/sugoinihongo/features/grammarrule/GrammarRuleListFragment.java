package lv.latvijaff.sugoinihongo.features.grammarrule;

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
import lv.latvijaff.sugoinihongo.features.android.activities.GrammarRuleEditContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.GrammarRuleSearchListContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;

public class GrammarRuleListFragment extends BaseListFragment<GrammarRuleModel, GrammarRuleListViewModel>
	implements HasFabFunctionality {

	public GrammarRuleListFragment() {
		super(GrammarRuleListViewModel.class);
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
	protected void processList(GrammarRuleListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@Override
	protected void setupSubscriptions(GrammarRuleListViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable listHasItemsDisp = viewModel.observeListHasItems()
			.subscribe(this::setListHasItems);

		cleanUp.add(listHasItemsDisp);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<GrammarRuleModel> list) {
		return new GrammarRuleListAdapter(list);
	}

	@Override
	public void onFabClickedAction(@NonNull Context context) {
		Intent intent = new Intent(context, GrammarRuleEditContentActivity.class);
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
		Intent intent = new Intent(getContext(), GrammarRuleSearchListContentActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
}
