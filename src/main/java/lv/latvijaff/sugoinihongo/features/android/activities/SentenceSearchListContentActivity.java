package lv.latvijaff.sugoinihongo.features.android.activities;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceSearchListFragment;

public class SentenceSearchListContentActivity extends BaseContentActivity<SentenceSearchListFragment> {

	@NonNull
	@Override
	protected SentenceSearchListFragment createFragment() {
		return new SentenceSearchListFragment();
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
