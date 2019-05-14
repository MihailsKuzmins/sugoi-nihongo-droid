package lv.latvijaff.sugoinihongo.features.system;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.SettingModel;
import lv.latvijaff.sugoinihongo.persistence.repos.SettingsRepo;

public class SettingsListViewModel extends BaseListViewModel<SettingModel> {

	@Inject
	SettingsRepo repo;

	protected SettingsListViewModel(@NonNull App application) {
		super(application, false);

		getRepoComponent().inject(this);
	}

	@Override
	protected void fetchList(Consumer<List<SettingModel>> consumer) {
		List<SettingModel> list = repo.createSettingsList();
		consumer.accept(list);
	}
}
