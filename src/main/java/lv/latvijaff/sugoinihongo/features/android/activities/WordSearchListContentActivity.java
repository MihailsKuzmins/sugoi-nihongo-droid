package lv.latvijaff.sugoinihongo.features.android.activities;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.word.WordSearchListFragment;

public class WordSearchListContentActivity extends BaseContentActivity<WordSearchListFragment> {

	@NonNull
	@Override
	protected WordSearchListFragment createFragment() {
		return new WordSearchListFragment();
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
