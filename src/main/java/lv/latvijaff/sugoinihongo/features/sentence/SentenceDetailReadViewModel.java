package lv.latvijaff.sugoinihongo.features.sentence;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailReadViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.persistence.repos.SentenceRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemTextView;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class SentenceDetailReadViewModel extends BaseDetailReadViewModel<SentenceModel> {

	private final DetailItemTextView mIdItem;
	private final DetailItemTextView mEnglishItem;
	private final DetailItemTextView mTranslationItem;
	private final DetailItemTextView mTranscriptionItem;

	private Consumer<SentenceModel> mConsumer;

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	SentenceRepo repo;

	public SentenceDetailReadViewModel(@NonNull App application) {
		super(application);

		getAppRepoComponent().inject(this);

		mIdItem = new DetailItemTextView(application, R.string.general_identification);
		mEnglishItem = new DetailItemTextView(application, R.string.models_sentence_english);
		mTranslationItem = new DetailItemTextView(application, R.string.general_translation);
		mTranscriptionItem = new DetailItemTextView(application, R.string.general_transcription);
	}

	public DetailItemTextView getIdItem() {
		return mIdItem;
	}

	public DetailItemTextView getEnglishItem() {
		return mEnglishItem;
	}

	public DetailItemTextView getTranslationItem() {
		return mTranslationItem;
	}

	public DetailItemTextView getTranscriptionItem() {
		return mTranscriptionItem;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable isEntryIdShownDisp = preferencesService.observeBoolean(Keys.IS_ENTRY_ID_SHOWN)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(mIdItem::setVisible);

		cleanUp.addAll(isEntryIdShownDisp);
	}

	@NonNull
	@Override
	protected Consumer<SentenceModel> getModelConsumer() {
		if (mConsumer == null) {
			mConsumer = x -> {
				mIdItem.setValue(x.getId());
				mEnglishItem.setValue(x.getEnglish());
				mTranslationItem.setValue(x.getTranslation());
				mTranscriptionItem.setValue(x.getTranscription());

				boolean isTranscriptionVisible = !StringUtils.isNullOrEmpty(x.getTranscription());
				mTranscriptionItem.setVisible(isTranscriptionVisible);
			};
		}

		return mConsumer;
	}

	@NonNull
	@Override
	protected ListenerRegistration fetchDocument(String id, Consumer<SentenceModel> consumer) {
		return repo.processDocument(id, consumer);
	}
}
