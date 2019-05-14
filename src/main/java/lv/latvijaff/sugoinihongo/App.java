package lv.latvijaff.sugoinihongo;

import android.app.Application;
import android.content.Intent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.AppComponent;
import lv.latvijaff.sugoinihongo.di.AppRepoComponent;
import lv.latvijaff.sugoinihongo.di.DaggerAppComponent;
import lv.latvijaff.sugoinihongo.di.DaggerAppRepoComponent;
import lv.latvijaff.sugoinihongo.di.DaggerRepoComponent;
import lv.latvijaff.sugoinihongo.di.DaggerStorageComponent;
import lv.latvijaff.sugoinihongo.di.RepoComponent;
import lv.latvijaff.sugoinihongo.di.RepoModule;
import lv.latvijaff.sugoinihongo.di.ServicesModule;
import lv.latvijaff.sugoinihongo.di.StorageComponent;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.features.android.services.SystemBackupCreationService;
import lv.latvijaff.sugoinihongo.features.android.services.SystemBackupSynchronisationService;
import lv.latvijaff.sugoinihongo.features.android.services.WordUnusedWordMarkReductionService;
import timber.log.Timber;

public class App extends Application {

	@Inject
	SharedPreferencesService preferencesService;

	private AppComponent mAppComponent;
	private RepoComponent mRepoComponent;
	private AppRepoComponent mAppRepoComponent;
	private StorageComponent mStorageComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		initLogging();

		ServicesModule servicesModule = new ServicesModule(getApplicationContext());

		mAppComponent = DaggerAppComponent
			.builder().servicesModule(servicesModule)
			.build();

		mAppComponent.inject(this);

		boolean isTestDbInUse = preferencesService.getBoolean(Keys.IS_TEST_DB_IN_USE);
		Timber.i("IsTestDbInUse: %s", isTestDbInUse);

		RepoModule repoModule = new RepoModule(isTestDbInUse, this);

		mRepoComponent = DaggerRepoComponent
			.builder().repoModule(repoModule)
			.build();

		mAppRepoComponent = DaggerAppRepoComponent
			.builder().servicesModule(servicesModule).repoModule(repoModule)
			.build();

		mStorageComponent = DaggerStorageComponent.create();

		checkFirstRun();
		initStorage();
		reduceWordMarkForUnusedWords(isTestDbInUse);
		synchroniseBackups();

		if (!isTestDbInUse) {
			createBackup();
		}
	}

	public RepoComponent getRepoComponent() {
		return mRepoComponent;
	}

	public AppComponent getAppComponent() {
		return mAppComponent;
	}

	public AppRepoComponent getAppRepoComponent() {
		return mAppRepoComponent;
	}

	public StorageComponent getStorageComponent() {
		return mStorageComponent;
	}

	private void initLogging() {
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}
	}

	private void checkFirstRun() {
		final String key = Keys.IS_FIRST_RUN;
		boolean isFirstRun = preferencesService.getBoolean(key, true);

		if (isFirstRun) {
			new DefaultPreferencesInitialiser(preferencesService)
				.Run();

			preferencesService.putBoolean(key, false);
		}
	}

	private void initStorage() {
		new StorageInitialiser()
			.run(this);
	}

	private void reduceWordMarkForUnusedWords(boolean isTestDbInUse) {
		final String key = isTestDbInUse ? Keys.LAST_RUN_TEST_EPOCH : Keys.LAST_RUN_PROD_EPOCH;

		long lastRunEpoch = preferencesService.getLong(key);
		long todayEpoch = LocalDate.now().toEpochDay();

		if (lastRunEpoch != todayEpoch) {
			Intent intent = new Intent(this, WordUnusedWordMarkReductionService.class);
			startService(intent);

			preferencesService.putLong(key, todayEpoch);
		}
	}

	private void createBackup() {
		final String key = Keys.LAST_BACKUP_EPOCH;

		long lastBackupEpoch = preferencesService.getLong(key);
		int backupCreationFrequency = preferencesService.getInt(Keys.BACKUP_CREATION_FREQUENCY);

		LocalDate today = LocalDate.now();
		long backupCreationEpoch = today.minus(backupCreationFrequency, ChronoUnit.DAYS).toEpochDay();

		if (backupCreationEpoch >= lastBackupEpoch) {
			Intent intent = new Intent(this, SystemBackupCreationService.class);
			startService(intent);

			preferencesService.putLong(key, today.toEpochDay());
		}
	}

	private void synchroniseBackups() {
		Intent syncIntent = new Intent(this, SystemBackupSynchronisationService.class);
		startService(syncIntent);
	}
}
