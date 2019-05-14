package lv.latvijaff.sugoinihongo.features.sentence;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailEditViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.persistence.repos.SentenceRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditTextMultiline;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.KanaRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.KanjiOrKanaRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.LengthLessThanOrEqualRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.NotNullOrWhiteSpaceRule;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class SentenceDetailEditViewModel extends BaseDetailEditViewModel<SentenceModel> {

	private final Subject<Boolean> mIsSavedSubject = PublishSubject.create();

	private final DetailItemEditTextMultiline mEnglishItem;
	private final DetailItemEditTextMultiline mTranslationItem;
	private final DetailItemEditTextMultiline mTranscriptionItem;

	private Consumer<SentenceModel> mConsumer;

	@Inject
	SentenceRepo repo;

	public SentenceDetailEditViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);

		final int maxLength = 64;

		mEnglishItem = new DetailItemEditTextMultiline(application, R.string.models_sentence_english)
			.addRuleMultilineEx(NotNullOrWhiteSpaceRule.getInstance())
			.addRuleMultilineEx(new LengthLessThanOrEqualRule(maxLength));

		mTranslationItem = new DetailItemEditTextMultiline(application, R.string.general_translation)
			.addRuleMultilineEx(NotNullOrWhiteSpaceRule.getInstance())
			.addRuleMultilineEx(KanjiOrKanaRule.getInstance())
			.addRuleMultilineEx(new LengthLessThanOrEqualRule(maxLength));

		mTranscriptionItem = new DetailItemEditTextMultiline(application, R.string.general_transcription, true)
			.addRuleMultilineEx(KanaRule.getInstance())
			.addRuleMultilineEx(new LengthLessThanOrEqualRule(maxLength));
	}

	@NonNull
	@Override
	public Observable<Boolean> observeIsSaved() {
		return mIsSavedSubject;
	}

	public DetailItemEditTextMultiline getEnglishItem() {
		return mEnglishItem;
	}

	public DetailItemEditTextMultiline getTranslationItem() {
		return mTranslationItem;
	}

	public DetailItemEditTextMultiline getTranscriptionItem() {
		return mTranscriptionItem;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable englishValueDisp = mEnglishItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setEnglish(value)));

		Disposable translationValueDisp = mTranslationItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setTranslation(value)));

		Disposable transcriptionValueDisp = mTranscriptionItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setTranscription(value)));

		cleanUp.addAll(englishValueDisp, translationValueDisp, transcriptionValueDisp);
	}

	@NonNull
	@Override
	protected SentenceModel createEmptyModel() {
		SentenceModel model = new SentenceModel(AppConstants.Models.EMPTY_ID);
		model.setDateCreated(LocalDate.now());

		return model;
	}

	@NonNull
	@Override
	protected Consumer<SentenceModel> getModelConsumer() {
		if (mConsumer == null) {
			mConsumer = x -> {
				mEnglishItem.setValue(x.getEnglish());
				mTranslationItem.setValue(x.getTranslation());
				mTranscriptionItem.setValue(x.getTranscription());
			};
		}

		return mConsumer;
	}

	@Override
	protected void fetchDocument(String id, Consumer<SentenceModel> consumer) {
		repo.getDocument(id, consumer);
	}

	void saveDetail() {
		SentenceModel model = getModel();
		repo.saveDocument(model, mIsSavedSubject);
	}
}
