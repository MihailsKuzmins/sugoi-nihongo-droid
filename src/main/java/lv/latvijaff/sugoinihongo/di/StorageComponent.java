package lv.latvijaff.sugoinihongo.di;

import javax.inject.Singleton;

import dagger.Component;
import lv.latvijaff.sugoinihongo.features.android.services.SystemBackupSynchronisationService;

@Singleton
@Component(modules = StorageModule.class)
public interface StorageComponent {

	void inject(SystemBackupSynchronisationService service);
}
