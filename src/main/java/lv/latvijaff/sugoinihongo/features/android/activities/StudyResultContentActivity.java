package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.study.QuizResults;
import lv.latvijaff.sugoinihongo.features.study.StudyResultListFragment;

public class StudyResultContentActivity extends BaseContentActivity<StudyResultListFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.general_result);
	}

	@NonNull
	@Override
	protected StudyResultListFragment createFragment() {
		StudyResultListFragment fragment = new StudyResultListFragment();

		String key = AppConstants.Keys.Navigation.QUIZ_RESULTS;
		QuizResults quizResults = getIntent().getParcelableExtra(key);

		Bundle bundle = new Bundle();
		bundle.putParcelable(key, quizResults);

		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
