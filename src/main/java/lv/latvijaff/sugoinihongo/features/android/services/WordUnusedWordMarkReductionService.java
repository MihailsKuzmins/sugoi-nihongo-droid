package lv.latvijaff.sugoinihongo.features.android.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;
import timber.log.Timber;

public class WordUnusedWordMarkReductionService extends IntentService {

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	WordSecondaryPropsRepo repo;

	public WordUnusedWordMarkReductionService() {
		super(WordUnusedWordMarkReductionService.class.getName());
	}

	@Override
	public void onCreate() {
		super.onCreate();

		App app = (App) getApplication();
		app.getAppRepoComponent().inject(this);
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Timber.i("Reducing marks for unused words");

		int unusedWordDays = preferencesService.getInt(Keys.UNUSED_WORD_DAYS);
		repo.reduceMarkForUnusedWords(unusedWordDays);
	}
}
