package lv.latvijaff.sugoinihongo.features.study;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.Map;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.BR;
import lv.latvijaff.sugoinihongo.base.BaseViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.models.WordStatus;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;
import lv.latvijaff.sugoinihongo.utils.IntUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class StudySelectTypeViewModel extends BaseViewModel {

	private ListenerRegistration mGroupingSubscription;

	private int excellentCount;
	private int goodCount;
	private int averageCount;
	private int badCount;
	private int excludedCount;
	private boolean isQuizEnabled;
	private boolean isListOfWordsEnabled;

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	WordSecondaryPropsRepo repo;

	public StudySelectTypeViewModel(@NonNull App application) {
		super(application);

		getAppRepoComponent().inject(this);
	}

	@Override
	protected void onCleared() {
		ObjectUtils.invokeIfNotNull(mGroupingSubscription, ListenerRegistration::remove);
		super.onCleared();
	}

	void processWordsGrouping() {
		final int maxOptionsCount = preferencesService.getInt(AppConstants.SharedPrefs.Keys.MAX_QUIZ_ITEMS_COUNT);

		mGroupingSubscription = repo.processListGroupingByStatus(map -> {
			// use setters to update UI
			setExcellentCount(getStatusCount(map, WordStatus.EXCELLENT));
			setGoodCount(getStatusCount(map, WordStatus.GOOD));
			setAverageCount(getStatusCount(map, WordStatus.AVERAGE));
			setBadCount(getStatusCount(map, WordStatus.BAD));
			setExcludedCount(getStatusCount(map, WordStatus.EXCLUDED));

			int includedCount = excellentCount + goodCount + averageCount + badCount;
			setQuizEnabled(includedCount >= maxOptionsCount);
			setListOfWordsEnabled(includedCount > 0);
		});
	}

	@Bindable
	public int getExcellentCount() {
		return excellentCount;
	}

	private void setExcellentCount(int excellentCount) {
		this.excellentCount = excellentCount;
		notifyPropertyChanged(BR.excellentCount);
	}

	@Bindable
	public int getGoodCount() {
		return goodCount;
	}

	private void setGoodCount(int goodCount) {
		this.goodCount = goodCount;
		notifyPropertyChanged(BR.goodCount);
	}

	@Bindable
	public int getAverageCount() {
		return averageCount;
	}

	private void setAverageCount(int averageCount) {
		this.averageCount = averageCount;
		notifyPropertyChanged(BR.averageCount);
	}

	@Bindable
	public int getBadCount() {
		return badCount;
	}

	private void setBadCount(int badCount) {
		this.badCount = badCount;
		notifyPropertyChanged(BR.badCount);
	}

	@Bindable
	public int getExcludedCount() {
		return excludedCount;
	}

	private void setExcludedCount(int excludedCount) {
		this.excludedCount = excludedCount;
		notifyPropertyChanged(BR.excludedCount);
	}

	private int getStatusCount(Map<WordStatus, Integer> map, WordStatus status) {
		return IntUtils.unbox(map.getOrDefault(status, 0));
	}

	@Bindable
	public boolean isQuizEnabled() {
		return isQuizEnabled;
	}

	private void setQuizEnabled(boolean isQuizEnabled) {
		this.isQuizEnabled = isQuizEnabled;
		notifyPropertyChanged(BR.quizEnabled);
	}

	@Bindable
	public boolean isListOfWordsEnabled() {
		return isListOfWordsEnabled;
	}

	private void setListOfWordsEnabled(boolean isListOfWordsEnabled) {
		this.isListOfWordsEnabled = isListOfWordsEnabled;
		notifyPropertyChanged(BR.listOfWordsEnabled);
	}
}
