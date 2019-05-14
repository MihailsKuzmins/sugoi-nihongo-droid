package lv.latvijaff.sugoinihongo;

import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.constants.AppConstants.System;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.utils.IntUtils;
import timber.log.Timber;

class DefaultPreferencesInitialiser {

	private final SharedPreferencesService mPreferencesService;

	DefaultPreferencesInitialiser(SharedPreferencesService preferencesService) {
		mPreferencesService = preferencesService;
	}

	void Run() {
		Timber.i("Initialising default preferences");

		initIntValue(Keys.STUDY_ITEMS_COUNT, System.MAX_STUDY_ITEMS_COUNT);
		initIntValue(Keys.MIN_QUIZ_ITEMS_COUNT, System.MIN_QUIZ_ITEMS_COUNT);
		initIntValue(Keys.MAX_QUIZ_ITEMS_COUNT, System.MAX_QUIZ_ITEMS_COUNT);
		initIntValue(Keys.UNUSED_WORD_DAYS, System.MIN_UNUSED_WORD_DAYS);
		initIntValue(Keys.LOCAL_BACKUP_FILES_COUNT, System.MAX_LOCAL_BACKUP_FILES_COUNT);
		initIntValue(Keys.BACKUP_CREATION_FREQUENCY, System.MIN_BACKUP_CREATION_FREQUENCY);
		initBooleanValue(Keys.IS_TRANSCRIPTION_SHOWN, true);
		initBooleanValue(Keys.IS_TEST_DB_IN_USE, false);
		initBooleanValue(Keys.IS_ENTRY_ID_SHOWN, false);
	}

	private void initBooleanValue(String key, boolean defaultValue) {
		boolean value = mPreferencesService.getBoolean(key, defaultValue);

		if (value == defaultValue) {
			mPreferencesService.putBoolean(key, defaultValue);
		}
	}

	private void initIntValue(String key, int defaultValue) {
		int value = mPreferencesService.getInt(key);

		if (value == IntUtils.DEFAULT_VALUE) {
			mPreferencesService.putInt(key, defaultValue);
		}
	}
}
