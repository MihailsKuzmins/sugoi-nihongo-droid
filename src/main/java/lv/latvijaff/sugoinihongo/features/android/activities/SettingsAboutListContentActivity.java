package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.system.SettingsAboutListFragment;

public class SettingsAboutListContentActivity extends BaseContentActivity<SettingsAboutListFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.general_licenses);
	}

	@NonNull
	@Override
	protected SettingsAboutListFragment createFragment() {
		return new SettingsAboutListFragment();
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
