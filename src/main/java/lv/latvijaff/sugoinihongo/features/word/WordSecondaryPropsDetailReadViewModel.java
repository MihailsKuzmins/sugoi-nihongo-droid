package lv.latvijaff.sugoinihongo.features.word;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import kotlin.Unit;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailReadViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Models;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemSwitch;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemTextView;
import lv.latvijaff.sugoinihongo.utils.LocalDateUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class WordSecondaryPropsDetailReadViewModel extends BaseDetailReadViewModel<WordModel.SecondaryProps> {

	private final DetailItemSwitch mIsIncludedInStudyItem;
	private final DetailItemSwitch mIsFavouriteItem;
	private final DetailItemTextView mIdItem;
	private final DetailItemTextView mMarkItem;
	private final DetailItemTextView mDateLastAccessedItem;

	private final Subject<Unit> mMarkResetSubject = PublishSubject.create();
	private final Subject<Unit> mMarkResetUndoneSubject = PublishSubject.create();

	@Nullable
	private Consumer<WordModel.SecondaryProps> mConsumer;

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	WordSecondaryPropsRepo repo;

	public WordSecondaryPropsDetailReadViewModel(@NonNull App application) {
		super(application);

		getAppRepoComponent().inject(this);

		mIsIncludedInStudyItem = new DetailItemSwitch(application, R.string.models_word_include_in_study, R.string.models_word_include_in_study_subtext);
		mIsFavouriteItem = new DetailItemSwitch(application, R.string.models_added_to_favourites, R.string.models_favourites_for_fast_access);
		mIdItem = new DetailItemTextView(application, R.string.general_identification);
		mMarkItem = new DetailItemTextView(application, R.string.models_mark);
		mDateLastAccessedItem = new DetailItemTextView(application, R.string.models_date_last_accessed);
	}

	public DetailItemSwitch getIsIncludedInStudyItem() {
		return mIsIncludedInStudyItem;
	}

	public DetailItemSwitch getIsFavouriteItem() {
		return mIsFavouriteItem;
	}

	public DetailItemTextView getIdItem() {
		return mIdItem;
	}

	public DetailItemTextView getMarkItem() {
		return mMarkItem;
	}

	public DetailItemTextView getDateLastAccessedItem() {
		return mDateLastAccessedItem;
	}

	Observable<Unit> observeMarkReset() {
		return mMarkResetSubject;
	}

	Observable<Unit> observeMarkResetUndone() {
		return mMarkResetUndoneSubject;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable isEntryIdShownDisp = preferencesService.observeBoolean(Keys.IS_ENTRY_ID_SHOWN)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(mIdItem::setVisible);

		Disposable isIncludedInStudyValueDisp = mIsIncludedInStudyItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setStudiable(value)));

		Disposable isFavouriteValueDisp = mIsFavouriteItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setFavourite(value)));

		Disposable markValueDisp = mMarkItem.observeValue()
			.map(StringUtils::toInt)
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setMark(value)));

		cleanUp.addAll(isEntryIdShownDisp, isIncludedInStudyValueDisp, isFavouriteValueDisp, markValueDisp);
	}

	@NonNull
	@Override
	protected Consumer<WordModel.SecondaryProps> getModelConsumer() {
		if (mConsumer == null) {
			mConsumer = x -> {
				mIsIncludedInStudyItem.setValue(x.isStudiable());
				mIsFavouriteItem.setValue(x.isFavourite());
				mIdItem.setValue(x.getId());
				mMarkItem.setValue(Integer.toString(x.getMark()));

				String dateTime = LocalDateUtils.toStringWithNeverLabel(getApplication(), x.getDateLastAccessed());
				mDateLastAccessedItem.setValue(dateTime);
			};
		}
		return mConsumer;
	}

	@NonNull
	@Override
	protected ListenerRegistration fetchDocument(String wordId, Consumer<WordModel.SecondaryProps> consumer) {
		return repo.processDocument(wordId, consumer);
	}

	void resetMark() {
		if (getModel().getMark() == Models.MIN_MARK)
			return;

		mMarkItem.setValue(Integer.toString(Models.MIN_MARK));
		mMarkResetSubject.onNext(Unit.INSTANCE);
	}

	void undoResetMark() {
		String defaultValue = mMarkItem.getDefaultValue();
		mMarkItem.setValue(defaultValue);

		mMarkResetUndoneSubject.onNext(Unit.INSTANCE);
	}

	void saveModel() {
		repo.saveDocument(getModel());
	}
}
