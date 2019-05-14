package lv.latvijaff.sugoinihongo.features.system;

import androidx.annotation.NonNull;

import com.jakewharton.processphoenix.ProcessPhoenix;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.constants.AppConstants.System;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditTextInteger;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemSwitch;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.GreaterThanOrEqualRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.GreaterThanRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.LessThanOrEqualRule;
import timber.log.Timber;

public class SettingsTechViewModel extends BaseViewModel {

	private final DetailItemSwitch mIsTestDbInUseItem;
	private final DetailItemSwitch mIsEntryIdShownItem;
	private final DetailItemEditTextInteger mLocalBackupFileCountItem;
	private final DetailItemEditTextInteger mBackupCreationFrequencyDaysItem;

	@Inject
	SharedPreferencesService preferencesService;

	public SettingsTechViewModel(@NonNull App application) {
		super(application);

		getAppComponent().inject(this);

		mIsTestDbInUseItem = new DetailItemSwitch(application, R.string.settings_tech_test_mode, R.string.settings_tech_sync_test_database);
		mIsEntryIdShownItem = new DetailItemSwitch(application, R.string.settings_tech_show_entry_ids, R.string.settings_tech_db_id_is_shown);
		mLocalBackupFileCountItem = new DetailItemEditTextInteger(application, R.string.settings_tech_local_backup_file_count)
			.addRuleEx(new GreaterThanOrEqualRule<>(System.MIN_BACKUP_CREATION_FREQUENCY))
			.addRuleEx(new LessThanOrEqualRule<>(System.MAX_LOCAL_BACKUP_FILES_COUNT));

		mBackupCreationFrequencyDaysItem = new DetailItemEditTextInteger(application, R.string.settings_tech_backup_creation_frequency_days)
			.addRuleEx(new GreaterThanRule<>(0))
			.addRuleEx(new LessThanOrEqualRule<>(System.MAX_BACKUP_CREATION_FREQUENCY));

		initDetailItemsValues();
		setBackupItemsVisibility();
	}

	public DetailItemSwitch getIsTestDbInUseItem() {
		return mIsTestDbInUseItem;
	}

	public DetailItemSwitch getIsEntryIdShownItem() {
		return mIsEntryIdShownItem;
	}

	public DetailItemEditTextInteger getLocalBackupFileCountItem() {
		return mLocalBackupFileCountItem;
	}

	public DetailItemEditTextInteger getBackupCreationFrequencyDaysItem() {
		return mBackupCreationFrequencyDaysItem;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable isEntryIdShownValueDisp = mIsEntryIdShownItem.observeValue()
			.doOnEach(x -> Timber.i("Setting IsEntryIdShown to: %s", x.getValue()))
			.subscribe(x -> preferencesService.putBoolean(Keys.IS_ENTRY_ID_SHOWN, x));

		Disposable backupFilesCountValueDisp = mLocalBackupFileCountItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting LocalBackupCount to: %d", x.getValue()))
			.subscribe(x -> preferencesService.putInt(Keys.LOCAL_BACKUP_FILES_COUNT, x));

		Disposable backupCreationFrequencyValueDisp = mBackupCreationFrequencyDaysItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting BackupCreationFrequency to: %d", x.getValue()))
			.subscribe(x -> preferencesService.putInt(Keys.BACKUP_CREATION_FREQUENCY, x));

		cleanUp.addAll(isEntryIdShownValueDisp, backupFilesCountValueDisp, backupCreationFrequencyValueDisp);
	}

	void restart() {
		boolean isTestDbInUse = mIsTestDbInUseItem.getValue();
		preferencesService.putBoolean(Keys.IS_TEST_DB_IN_USE, isTestDbInUse);
		Timber.i("IsTestDbInUse has been set to: %s", isTestDbInUse);

		ProcessPhoenix.triggerRebirth(getApplication());
	}

	private void initDetailItemsValues() {
		boolean isTestDbInUse = preferencesService.getBoolean(Keys.IS_TEST_DB_IN_USE);
		boolean isModelIdShown = preferencesService.getBoolean(Keys.IS_ENTRY_ID_SHOWN);
		int backupFileCount = preferencesService.getInt(Keys.LOCAL_BACKUP_FILES_COUNT);
		int backupCreationFrequency = preferencesService.getInt(Keys.BACKUP_CREATION_FREQUENCY);

		mIsTestDbInUseItem.setValue(isTestDbInUse);
		mIsEntryIdShownItem.setValue(isModelIdShown);
		mLocalBackupFileCountItem.setValue(backupFileCount);
		mBackupCreationFrequencyDaysItem.setValue(backupCreationFrequency);
	}

	private void setBackupItemsVisibility() {
		boolean isProdDbInUse = !preferencesService.getBoolean(Keys.IS_TEST_DB_IN_USE);

		mLocalBackupFileCountItem.setVisible(isProdDbInUse);
		mBackupCreationFrequencyDaysItem.setVisible(isProdDbInUse);
	}
}
