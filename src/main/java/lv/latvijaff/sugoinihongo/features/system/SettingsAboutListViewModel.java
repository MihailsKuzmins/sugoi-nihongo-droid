package lv.latvijaff.sugoinihongo.features.system;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.ThirdPartyLibraryModel;
import lv.latvijaff.sugoinihongo.persistence.repos.SettingsRepo;

public class SettingsAboutListViewModel extends BaseListViewModel<ThirdPartyLibraryModel> {

	@Inject
	SettingsRepo repo;

	protected SettingsAboutListViewModel(@NonNull App application) {
		super(application, false);

		getRepoComponent().inject(this);
	}

	@Override
	protected void fetchList(Consumer<List<ThirdPartyLibraryModel>> consumer) {
		List<ThirdPartyLibraryModel> list = repo.createThirdPartyLibrariesList(getApplication());
		consumer.accept(list);
	}
}
