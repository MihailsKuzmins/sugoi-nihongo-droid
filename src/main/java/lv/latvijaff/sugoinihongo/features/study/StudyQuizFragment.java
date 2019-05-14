package lv.latvijaff.sugoinihongo.features.study;


import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailFragment;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.databinding.FragmentStudyQuizBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.StudyResultContentActivity;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import lv.latvijaff.sugoinihongo.utils.MenuItemUtils;

public class StudyQuizFragment extends BaseDetailFragment<StudyQuizViewModel, FragmentStudyQuizBinding> {

	private final LinearLayout.LayoutParams mLayoutParams;
	private boolean mIsQuestionInEnglish;

	@BindView(R.id.fragment_study_quiz_buttons_linear_layout)
	LinearLayout optionsLinearLayout;

	public StudyQuizFragment() {
		super(StudyQuizViewModel.class, R.layout.fragment_study_quiz);

		mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_study_quiz, menu);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);

		boolean isEnabled = getViewModel().canShowTranscription();
		MenuItemUtils.setEnabled(menu, R.id.menu_item_study_quiz_show_transcription, isEnabled);
	}

	@Override
	protected void setupSubscriptions(StudyQuizViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable quizItemsDisp = viewModel.observeQuestionItems()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::processQuizItems);

		Disposable quizResultsDisp = viewModel.observeQuizResults()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(x -> {
				Context context = getContext();

				Intent intent = new Intent(context, StudyResultContentActivity.class);
				intent.putExtra(AppConstants.Keys.Navigation.QUIZ_RESULTS, x);
				startActivity(intent);

				FragmentUtils.finishActivity(this);
			});

		Disposable canShowKanaDisp = viewModel.observeCanShowTranscriptionChanged()
			.subscribe(x -> FragmentUtils.invalidateOptionsMenu(this));

		cleanUp.addAll(quizItemsDisp, quizResultsDisp, canShowKanaDisp);
	}

	@Override
	protected void applyBinding(final @NonNull FragmentStudyQuizBinding binding, final @NonNull StudyQuizViewModel viewModel) {
		mIsQuestionInEnglish = Objects.requireNonNull(getArguments())
			.getBoolean(AppConstants.Keys.Params.ENGLISH_TO_JAPANESE);

		viewModel.processQuizStack(mIsQuestionInEnglish, this::processQuizItems);
		binding.setViewModel(viewModel);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		if (id == R.id.menu_item_study_quiz_show_transcription) {
			return this::onShowTranscriptionAction;
		}

		return null;
	}

	private void onShowTranscriptionAction() {
		if (mIsQuestionInEnglish) {
			String[] texts = getViewModel().getTextsWithTranscriptions();
			getViewModel().triggerCanShowTranscription();

			for (int i = 0; i < texts.length; i++) {
				TextView tv = (TextView) optionsLinearLayout.getChildAt(i);
				tv.setText(texts[i]);
			}
		} else {
			getViewModel().setQuestionWithTranscription();
		}
	}

	private void processQuizItems(List<QuizOption> items) {
		optionsLinearLayout.removeAllViews();

		for (QuizOption item : items) {
			Button button = createButton(item);
			optionsLinearLayout.addView(button);
		}
	}

	private Button createButton(final QuizOption item) {
		Button button = new Button(getContext());
		button.setLayoutParams(mLayoutParams);
		button.setText(item.getText());

		button.setOnClickListener(v -> getViewModel().submitAnswer(item));

		return button;
	}
}
