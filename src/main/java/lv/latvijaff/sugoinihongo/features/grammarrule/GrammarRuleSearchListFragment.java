package lv.latvijaff.sugoinihongo.features.grammarrule;


import java.util.List;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseSearchListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;

public class GrammarRuleSearchListFragment extends BaseSearchListFragment<GrammarRuleModel, GrammarRuleSearchListViewModel> {

	public GrammarRuleSearchListFragment() {
		super(GrammarRuleSearchListViewModel.class);
	}

	@Override
	protected void processList(GrammarRuleSearchListViewModel viewModel) {
		viewModel.processList(list -> {
			setAdapter(list);
			filterList();
		});
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<GrammarRuleModel> list) {
		return new GrammarRuleSearchListAdapter(list);
	}
}
