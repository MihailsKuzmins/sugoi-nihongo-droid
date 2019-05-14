package lv.latvijaff.sugoinihongo.features.system;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.constants.AppConstants.System;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditTextInteger;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemSwitch;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.GreaterThanOrEqualConsumerRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.GreaterThanOrEqualRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.GreaterThanRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.LessThanOrEqualConsumerRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.LessThanOrEqualRule;
import timber.log.Timber;

public class SettingsStudyViewModel extends BaseViewModel {

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	WordSecondaryPropsRepo repo;

	private final DetailItemEditTextInteger mStudyItemsItem;
	private final DetailItemEditTextInteger mMinQuizOptionsItem;
	private final DetailItemEditTextInteger mMaxQuizOptionsItem;
	private final DetailItemSwitch mIsTranscriptionShownItem;
	private final DetailItemEditTextInteger mUnusedWordDaysItem;

	public SettingsStudyViewModel(@NonNull App application) {
		super(application);

		getAppRepoComponent().inject(this);

		mStudyItemsItem = new DetailItemEditTextInteger(application, R.string.settings_study_items)
			.addRuleEx(new GreaterThanRule<>(0))
			.addRuleEx(new LessThanOrEqualRule<>(System.MAX_STUDY_ITEMS_COUNT));

		mMinQuizOptionsItem = new DetailItemEditTextInteger(application, R.string.settings_study_min_options_count)
			.addRuleEx(new GreaterThanOrEqualRule<>(System.MIN_QUIZ_ITEMS_COUNT))
			.addRuleEx(new LessThanOrEqualRule<>(System.MAX_QUIZ_ITEMS_COUNT));

		mMaxQuizOptionsItem = new DetailItemEditTextInteger(application, R.string.settings_study_max_options_count)
			.addRuleEx(new LessThanOrEqualRule<>(System.MAX_QUIZ_ITEMS_COUNT))
			.addRuleEx(new GreaterThanOrEqualConsumerRule<>(mMinQuizOptionsItem::getValue))
			.addRuleEx(new GreaterThanOrEqualRule<>(System.MIN_QUIZ_ITEMS_COUNT));

		mMinQuizOptionsItem
			.addRuleEx(new LessThanOrEqualConsumerRule<>(mMaxQuizOptionsItem::getValue));

		mIsTranscriptionShownItem = new DetailItemSwitch(application, R.string.general_show_transcription, R.string.settings_study_show_kana_subtext);

		mUnusedWordDaysItem = new DetailItemEditTextInteger(application, R.string.settings_study_decrement_mark)
			.addRuleEx(new GreaterThanOrEqualRule<>(System.MIN_UNUSED_WORD_DAYS))
			.addRuleEx(new LessThanOrEqualRule<>(System.MAX_UNUSED_WORD_DAYS));

		initDetailItemsValues();
	}

	public DetailItemEditTextInteger getStudyItemsItem() {
		return mStudyItemsItem;
	}

	public DetailItemEditTextInteger getMinQuizOptionsItem() {
		return mMinQuizOptionsItem;
	}

	public DetailItemEditTextInteger getMaxQuizOptionsItem() {
		return mMaxQuizOptionsItem;
	}

	public DetailItemSwitch getIsTranscriptionShownItem() {
		return mIsTranscriptionShownItem;
	}

	public DetailItemEditTextInteger getUnusedWordDaysItem() {
		return mUnusedWordDaysItem;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable minOptionValidationTriggerDisp = mMinQuizOptionsItem.triggerValidation(
			mMaxQuizOptionsItem.observeValue());

		Disposable maxOptionValidationTriggerDisp = mMaxQuizOptionsItem.triggerValidation(
			mMinQuizOptionsItem.observeValue());

		Disposable studyItemsValueDisp = mStudyItemsItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting study items to: %d", x.getValue()))
			.subscribe(value -> preferencesService.putInt(Keys.STUDY_ITEMS_COUNT, value));

		Disposable minQuizOptionsValueDisp = mMinQuizOptionsItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting min quiz items count to: %d", x.getValue()))
			.subscribe(value -> preferencesService.putInt(Keys.MIN_QUIZ_ITEMS_COUNT, value));

		Disposable maxQuizOptionsValueDisp = mMaxQuizOptionsItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting max quiz items count to: %d", x.getValue()))
			.subscribe(value -> preferencesService.putInt(Keys.MAX_QUIZ_ITEMS_COUNT, value));

		Disposable isTranscriptionShownValueDisp = mIsTranscriptionShownItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting isTranscriptionShown to: %s", x.getValue()))
			.subscribe(value -> preferencesService.putBoolean(Keys.IS_TRANSCRIPTION_SHOWN, value));

		Disposable unusedWordDaysValueDisp = mUnusedWordDaysItem.observeValidValue()
			.doOnEach(x -> Timber.i("Setting unused word days to: %d", x.getValue()))
			.subscribe(value -> preferencesService.putInt(Keys.UNUSED_WORD_DAYS, value));

		cleanUp.addAll(minOptionValidationTriggerDisp, maxOptionValidationTriggerDisp,
			studyItemsValueDisp, minQuizOptionsValueDisp, maxQuizOptionsValueDisp, isTranscriptionShownValueDisp,
			unusedWordDaysValueDisp);
	}

	void resetWordProgress(Runnable runnable) {
		repo.resetWordProgress(runnable);
	}

	private void initDetailItemsValues() {
		int studyItemsCount = preferencesService.getInt(Keys.STUDY_ITEMS_COUNT);
		int minOptionsCount = preferencesService.getInt(Keys.MIN_QUIZ_ITEMS_COUNT);
		int maxOptionsCount = preferencesService.getInt(Keys.MAX_QUIZ_ITEMS_COUNT);
		int unusedWordDays = preferencesService.getInt(Keys.UNUSED_WORD_DAYS);
		boolean showKana = preferencesService.getBoolean(Keys.IS_TRANSCRIPTION_SHOWN);

		mStudyItemsItem.setValue(studyItemsCount);
		mMinQuizOptionsItem.setValue(minOptionsCount);
		mMaxQuizOptionsItem.setValue(maxOptionsCount);
		mUnusedWordDaysItem.setValue(unusedWordDays);
		mIsTranscriptionShownItem.setValue(showKana);
	}
}
