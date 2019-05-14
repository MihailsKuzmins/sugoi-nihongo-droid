package lv.latvijaff.sugoinihongo.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lv.latvijaff.sugoinihongo.persistence.storage.BackupFileStorage;

@Module
class StorageModule {

	@Provides
	@Singleton
	BackupFileStorage provideBackupFileStorage() {
		return new BackupFileStorage();
	}
}
