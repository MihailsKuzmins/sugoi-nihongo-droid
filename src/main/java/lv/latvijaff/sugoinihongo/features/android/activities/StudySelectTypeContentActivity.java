package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.study.StudySelectTypeFragment;

public class StudySelectTypeContentActivity extends BaseContentActivity<StudySelectTypeFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.study_select_test);
	}

	@NonNull
	@Override
	protected StudySelectTypeFragment createFragment() {
		return new StudySelectTypeFragment();
	}
}
