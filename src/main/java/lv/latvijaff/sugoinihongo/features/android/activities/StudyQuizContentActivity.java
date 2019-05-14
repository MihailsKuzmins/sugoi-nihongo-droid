package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.study.StudyQuizFragment;

public class StudyQuizContentActivity extends BaseContentActivity<StudyQuizFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.study_quiz);
	}

	@NonNull
	@Override
	protected StudyQuizFragment createFragment() {
		StudyQuizFragment fragment = new StudyQuizFragment();

		String key = AppConstants.Keys.Params.ENGLISH_TO_JAPANESE;
		boolean englishToJapanese = getIntent().getBooleanExtra(key, false);

		Bundle bundle = new Bundle();
		bundle.putBoolean(key, englishToJapanese);

		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
