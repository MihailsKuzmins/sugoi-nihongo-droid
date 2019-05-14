package lv.latvijaff.sugoinihongo.di;

import javax.inject.Singleton;

import dagger.Component;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.features.system.SettingsTechViewModel;

@Singleton
@Component(modules = ServicesModule.class)
public interface AppComponent {

	void inject(App app);
	void inject(SettingsTechViewModel viewModel);
}
