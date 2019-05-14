package lv.latvijaff.sugoinihongo.features.system;


import java.util.List;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.SettingModel;

public class SettingsListFragment extends BaseListFragment<SettingModel, SettingsListViewModel> {

	public SettingsListFragment() {
		super(SettingsListViewModel.class);
	}

	@Override
	protected void processList(SettingsListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<SettingModel> list) {
		return new SettingsListAdapter(list);
	}
}
