package lv.latvijaff.sugoinihongo.features.word;

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
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditText;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditTextMultiline;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.KanaRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.KanjiOrKanaRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.LengthLessThanOrEqualRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.NotNullOrWhiteSpaceRule;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class WordDetailEditViewModel extends BaseDetailEditViewModel<WordModel> {

	private final Subject<Boolean> mIsSavedSubject = PublishSubject.create();

	private final DetailItemEditText mEnglishItem;
	private final DetailItemEditText mTranslationItem;
	private final DetailItemEditText mTranscriptionItem;
	private final DetailItemEditTextMultiline mNotesItem;

	private Consumer<WordModel> mConsumer;

	@Inject
	WordRepo wordRepo;

	public WordDetailEditViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);

		final int wordMaxLength = 32;

		mEnglishItem = new DetailItemEditText(application, R.string.models_word_english)
			.addRuleEx(NotNullOrWhiteSpaceRule.getInstance())
			.addRuleEx(new LengthLessThanOrEqualRule(wordMaxLength));

		mTranslationItem = new DetailItemEditText(application, R.string.general_translation)
			.addRuleEx(NotNullOrWhiteSpaceRule.getInstance())
			.addRuleEx(KanjiOrKanaRule.getInstance())
			.addRuleEx(new LengthLessThanOrEqualRule(wordMaxLength));

		mTranscriptionItem = new DetailItemEditText(application, R.string.general_transcription, true)
			.addRuleEx(KanaRule.getInstance())
			.addRuleEx(new LengthLessThanOrEqualRule(wordMaxLength));

		mNotesItem = new DetailItemEditTextMultiline(application, R.string.models_word_notes)
			.addRuleMultilineEx(new LengthLessThanOrEqualRule(128));
	}

	public DetailItemEditText getEnglishItem() {
		return mEnglishItem;
	}

	public DetailItemEditText getTranslationItem() {
		return mTranslationItem;
	}

	public DetailItemEditText getTranscriptionItem() {
		return mTranscriptionItem;
	}

	public DetailItemEditTextMultiline getNotesItem() {
		return mNotesItem;
	}

	@NonNull
	@Override
	public Observable<Boolean> observeIsSaved() {
		return mIsSavedSubject;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable englishValueDisp = mEnglishItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setEnglish(value)));

		Disposable translationValueDisp = mTranslationItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setTranslation(value)));

		Disposable transcriptionValueDisp = mTranscriptionItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setTranscription(value)));

		Disposable notesValueDisp = mNotesItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setNotes(value)));

		cleanUp.addAll(englishValueDisp, translationValueDisp, transcriptionValueDisp, notesValueDisp);
	}

	@NonNull
	@Override
	protected WordModel createEmptyModel() {
		WordModel model = new WordModel(AppConstants.Models.EMPTY_ID);
		model.setDateCreated(LocalDate.now());

		WordModel.SecondaryProps subModel = new WordModel.SecondaryProps(AppConstants.Models.EMPTY_ID);
		subModel.setMark(AppConstants.Models.MIN_MARK);
		subModel.setDateLastAccessed(AppConstants.Models.MIN_DATE_TIME);
		subModel.setStudiable(true);

		model.setSecondaryProps(subModel);

		return model;
	}

	@NonNull
	@Override
	protected Consumer<WordModel> getModelConsumer() {
		if (mConsumer == null) {
			mConsumer = x -> {
				mEnglishItem.setValue(x.getEnglish());
				mTranslationItem.setValue(x.getTranslation());
				mTranscriptionItem.setValue(x.getTranscription());
				mNotesItem.setValue(x.getNotes());
			};
		}

		return mConsumer;
	}

	@Override
	protected void fetchDocument(String id, Consumer<WordModel> consumer) {
		wordRepo.getDocument(id, consumer);
	}

	void saveDetail() {
		WordModel model = getModel();
		wordRepo.saveDocument(model, mIsSavedSubject);
	}
}
