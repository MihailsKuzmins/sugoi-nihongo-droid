package lv.latvijaff.sugoinihongo.features.system;


import java.util.List;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.ThirdPartyLibraryModel;

public class SettingsAboutListFragment extends BaseListFragment<ThirdPartyLibraryModel, SettingsAboutListViewModel> {

	public SettingsAboutListFragment() {
		super(SettingsAboutListViewModel.class);
	}

	@Override
	protected void processList(SettingsAboutListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<ThirdPartyLibraryModel> list) {
		return new SettingsAboutListAdapter(list);
	}
}
