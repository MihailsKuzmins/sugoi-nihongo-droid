package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleDetailReadFragment;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;

public class GrammarRuleReadContentActivity extends BaseContentActivity<GrammarRuleDetailReadFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.general_grammar_rule);
	}

	@NonNull
	@Override
	protected GrammarRuleDetailReadFragment createFragment() {
		GrammarRuleDetailReadFragment fragment = new GrammarRuleDetailReadFragment();

		String key = AppConstants.Keys.Params.DOCUMENT_ID;
		ActivityUtils.passStringToFragment(this, fragment, key);

		return fragment;
	}
}
