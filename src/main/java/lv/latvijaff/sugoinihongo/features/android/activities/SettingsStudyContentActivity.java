package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.system.SettingsStudyFragment;

public class SettingsStudyContentActivity extends BaseContentActivity<SettingsStudyFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.general_study);
	}

	@NonNull
	@Override
	protected SettingsStudyFragment createFragment() {
		return new SettingsStudyFragment();
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
