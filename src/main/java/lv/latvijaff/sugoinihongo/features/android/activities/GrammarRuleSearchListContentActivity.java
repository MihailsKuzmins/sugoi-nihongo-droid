package lv.latvijaff.sugoinihongo.features.android.activities;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleSearchListFragment;

public class GrammarRuleSearchListContentActivity extends BaseContentActivity<GrammarRuleSearchListFragment> {

	@NonNull
	@Override
	protected GrammarRuleSearchListFragment createFragment() {
		return new GrammarRuleSearchListFragment();
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setMainMenuDisplayed(false);
	}
}
