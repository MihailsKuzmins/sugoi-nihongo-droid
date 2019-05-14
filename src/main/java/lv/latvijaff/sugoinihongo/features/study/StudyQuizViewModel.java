package lv.latvijaff.sugoinihongo.features.study;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.moji4j.MojiDetector;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import kotlin.Unit;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.BR;
import lv.latvijaff.sugoinihongo.base.BaseViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.dto.QuizItemDto;
import lv.latvijaff.sugoinihongo.persistence.models.QuizModel;
import lv.latvijaff.sugoinihongo.persistence.models.QuizResultModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import lv.latvijaff.sugoinihongo.utils.helpers.CustomCollectors;
import timber.log.Timber;

public class StudyQuizViewModel extends BaseViewModel {

	private final Subject<Stack<QuizModel>> mStackSubject = PublishSubject.create();
	private final Subject<QuizModel> mQuizModelSubject = PublishSubject.create();
	private final Subject<QuizOption> mQuestionItemSubject = PublishSubject.create();
	private final Subject<QuizResults> mResultSubject = PublishSubject.create();
	// For ShowTranscription
	private final Subject<Unit> mCanShowTranscriptionTriggerSubject = PublishSubject.create();
	private final Subject<Unit> mCanShowTranscriptionChangedSubject = PublishSubject.create();
	private final Subject<String> mQuestionSubject = ReplaySubject.create();
	private final Subject<List<QuizOption>> mOptionsSubject = ReplaySubject.create();

	private final Observable<List<QuizOption>> mQuestionItemsObservable;
	private final Observable<QuizResults> mQuizResultsObservable;
	private final Observable<Unit> mCanShowTranscriptionChangedObservable;

	private final MojiDetector mMojiDetector = new MojiDetector();
	private final String mTranslationTranscriptionSeparator = AppConstants.System.TRANSLATION_TRANSCRIPTION_SEPARATOR;
	private final QuizResults mResultItem = new QuizResults();

	private int mQuizCount;
	private int mCurrentQuizIndex;

	private boolean mCanShowTranscription;
	private boolean mEnglishToJapanese;
	private boolean mShowKana;

	private Stack<QuizModel> mStack;
	private BaseQuizItem mQuestion;
	private String mSecondaryPropsId;
	private List<QuizOption> mOptions;

	private int mScore;
	private int mMistakes;

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	WordRepo wordRepo;

	@Inject
	WordSecondaryPropsRepo secondaryPropsRepo;

	public StudyQuizViewModel(@NonNull App application) {
		super(application);

		getAppRepoComponent().inject(this);

		mShowKana = preferencesService.getBoolean(Keys.IS_TRANSCRIPTION_SHOWN);

		setScore(0);
		setMistakes(0);

		mQuestionItemsObservable = mQuizModelSubject
			.map(x -> mOptions = createOptions(x.getQuestion(), x.getOptions()))
			.share();

		mQuizResultsObservable = mResultSubject
			.map(x -> {
				x.setScore(mScore);
				x.setMistakes(mMistakes);
				Timber.i("Quiz results. Score: %d, mistakes: %d", mScore, mMistakes);

				return x;
			})
			.share();

		mCanShowTranscriptionChangedObservable = mCanShowTranscriptionChangedSubject
			.observeOn(AndroidSchedulers.mainThread())
			.share();
	}

	@Bindable
	public String getQuestion() {
		if (mQuestion != null) {
			return mQuestion.getText();
		}

		return null;
	}

	private void setQuestion(QuizItemDto dto, boolean showTranslation) {
		String text = createItemText(dto, mEnglishToJapanese);
		String longText = createItemLongText(dto, mEnglishToJapanese);

		mQuestion = new BaseQuizItem(text, longText);
		mQuestion.setLongTextShown(showTranslation);

		notifyQuestionChanged();
	}

	private void notifyQuestionChanged() {
		if (mQuestion != null) {
			notifyPropertyChanged(BR.question);
			mQuestionSubject.onNext(mQuestion.getText());
		}
	}

	@Bindable
	public int getScore() {
		return mScore;
	}

	private void setScore(int score) {
		if (mScore != score) {
			mScore = score;
			notifyPropertyChanged(BR.score);
			Timber.i("Setting score to %d", score);
		}
	}

	@Bindable
	public int getMistakes() {
		return mMistakes;
	}

	private void setMistakes(int mistakes) {
		if (mMistakes != mistakes) {
			mMistakes = mistakes;
			notifyPropertyChanged(BR.mistakes);
			Timber.i("Setting mistakes to %d", mistakes);
		}
	}

	@Bindable
	public String getCurrentQuizIndex() {
		return Integer.toString(mCurrentQuizIndex);
	}

	private void setCurrentQuizIndex(int currentQuizIndex) {
		if (mCurrentQuizIndex != currentQuizIndex) {
			mCurrentQuizIndex = currentQuizIndex;
			notifyPropertyChanged(BR.currentQuizIndex);
			Timber.i("Setting current index to %d", currentQuizIndex);
		}
	}

	@Bindable
	public int getQuizCount() {
		return mQuizCount;
	}

	private void setQuizCount(int quizCount) {
		if (mQuizCount != quizCount) {
			mQuizCount = quizCount;
			notifyPropertyChanged(BR.quizCount);
			Timber.i("Setting the quiz count to %d", quizCount);
		}
	}

	boolean canShowTranscription() {
		return mCanShowTranscription;
	}

	Observable<List<QuizOption>> observeQuestionItems() {
		return mQuestionItemsObservable;
	}

	Observable<Unit> observeCanShowTranscriptionChanged() {
		return mCanShowTranscriptionChangedObservable;
	}

	Observable<QuizResults> observeQuizResults() {
		return mQuizResultsObservable;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Observable<Stack<QuizModel>> stackObservable = mStackSubject
			.observeOn(Schedulers.single())
			.filter(x -> !x.empty());

		Observable<QuizOption> questionObservable = mQuestionItemSubject
			.observeOn(Schedulers.computation());

		Disposable initialStackCountDisp = stackObservable
			// take(1) is reset when screen is rotated even when stackObservable is created in the constructor of the viewModel
			.filter(x -> mQuizCount == 0)
			.take(1)
			.subscribe(x -> setQuizCount(x.size()));

		Disposable stackItemDisp = stackObservable
			.doOnEach(x -> Timber.i("Popping the stack. Size: %d, CurrentIndex: %d", x.getValue().size(), mCurrentQuizIndex))
			.subscribe(x -> {
				setCurrentQuizIndex(mCurrentQuizIndex + 1);

				QuizModel model = x.pop();
				mQuizModelSubject.onNext(model);
			});

		Disposable quizModelDisp = mQuizModelSubject
			.subscribe(x -> {
				mSecondaryPropsId = x.getSecondaryPropsId();
				setQuestion(x.getQuestion(), mShowKana);
			});

		Disposable scoreDisp = questionObservable
			.map(x -> x.isCorrectAnswer() ? 1 : -2)
			.subscribe(x -> setScore(mScore + x));

		Disposable mistakesDisp = questionObservable
			.filter(x -> !x.isCorrectAnswer())
			.subscribe(x -> setMistakes(mMistakes + 1));

		// ShowTranscription
		Disposable optionsDisp = mQuestionItemsObservable
			.subscribe(mOptionsSubject::onNext);

		Disposable canShowTranscriptionObservable = Observable
			.combineLatest(
				mQuestionSubject, mOptionsSubject, mCanShowTranscriptionTriggerSubject.startWith(Unit.INSTANCE),
				(x, y, z) -> new Pair<>(x, y))
			.observeOn(Schedulers.newThread())
			.map(x -> canShowTranscription(x.first, x.second))
			// distinctUntilChanged is reset when the screen is rotated
			.filter(x -> mCanShowTranscription != x)
			.subscribe(x -> {
				mCanShowTranscription = x;
				mCanShowTranscriptionChangedSubject.onNext(Unit.INSTANCE);
			});

		cleanUp.addAll(initialStackCountDisp, stackItemDisp, quizModelDisp, scoreDisp, mistakesDisp, canShowTranscriptionObservable,
			optionsDisp);
	}

	void processQuizStack(boolean englishToJapanese, Consumer<List<QuizOption>> consumer) {
		if (mStack != null) {
			consumer.accept(mOptions);
			mQuestionSubject.onNext(mQuestion.getText());
			mOptionsSubject.onNext(mOptions);
			return;
		}

		mEnglishToJapanese = englishToJapanese;

		int limit = preferencesService.getInt(Keys.STUDY_ITEMS_COUNT);
		int minOptionsCount = preferencesService.getInt(Keys.MIN_QUIZ_ITEMS_COUNT);
		int maxOptionsCount = preferencesService.getInt(Keys.MAX_QUIZ_ITEMS_COUNT);

		wordRepo.getStudyQuizList(limit, minOptionsCount, maxOptionsCount,
			stack -> mStackSubject.onNext(mStack = stack));
	}

	void submitAnswer(QuizOption questionItem) {
		mQuestionItemSubject.onNext(questionItem);

		appendSubmittedAnswerModel(questionItem);
		secondaryPropsRepo.updateDocument(mSecondaryPropsId, questionItem.isCorrectAnswer());

		if (mCurrentQuizIndex == mQuizCount) {
			Timber.i("Submitting the quiz results");
			mResultSubject.onNext(mResultItem);
			return;
		}

		Timber.i("Going to the next question");
		mStackSubject.onNext(mStack);
	}

	String[] getTextsWithTranscriptions() {
		return mOptions.stream()
			.map(x -> {
				x.setLongTextShown(true);
				return x.getText();
			})
			.toArray(String[]::new);
	}

	void setQuestionWithTranscription() {
		mQuestion.setLongTextShown(true);
		notifyQuestionChanged();
	}

	void triggerCanShowTranscription() {
		mCanShowTranscriptionTriggerSubject.onNext(Unit.INSTANCE);
	}

	private List<QuizOption> createOptions(QuizItemDto question, List<QuizItemDto> models) {
		List<QuizOption> options = Stream.concat(models.stream(), Stream.of(question))
			.map(this::createQuizQuestionItem)
			.collect(Collectors.toList());

		Collections.shuffle(options);
		return options;
	}

	private QuizOption createQuizQuestionItem(QuizItemDto dto) {
		boolean isOptionInEnglish = !mEnglishToJapanese;

		String text = createItemText(dto, isOptionInEnglish);
		String longText = createItemLongText(dto, isOptionInEnglish);

		QuizOption item = new QuizOption(text, longText);
		item.setCorrectAnswer(dto.isAnswer());
		item.setLongTextShown(mShowKana);

		return item;
	}

	private static String createItemText(QuizItemDto dto, boolean showEnglish) {
		return showEnglish
			? dto.getEnglish()
			: dto.getTranslation();
	}

	private String createItemLongText(QuizItemDto dto, boolean showEnglish) {
		if (showEnglish) {
			return dto.getEnglish();
		}

		String text = dto.getTranslation();
		String transcription = dto.getTranscription();

		if (!StringUtils.isNullOrWhiteSpace(transcription)) {
			text += mTranslationTranscriptionSeparator + transcription;
		}

		return text;
	}

	private boolean canShowTranscription(String question, List<QuizOption> questionItems) {
		if (mEnglishToJapanese) {
			return questionItems.stream()
				.map(QuizOption::getText)
				.anyMatch(this::isTranscriptionNotShown);
		}

		return isTranscriptionNotShown(question);
	}

	private boolean isTranscriptionNotShown(String value) {
		return !value.contains(mTranslationTranscriptionSeparator) &&
			mMojiDetector.hasKanji(value);
	}

	private void appendSubmittedAnswerModel(QuizOption questionItem) {
		QuizResultModel model = new QuizResultModel();
		model.setQuestionText(mQuestion.getLongText());
		model.setSubmittedAnswerText(questionItem.getLongText());

		String correctAnswer = mOptions.stream()
			.filter(QuizOption::isCorrectAnswer)
			.collect(CustomCollectors.toSingleton())
			.getLongText();

		model.setCorrectAnswerText(correctAnswer);

		mResultItem.addQuizResultModel(model);
	}
}
