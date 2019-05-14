package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceDetailReadFragment;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;

public class SentenceReadContentActivity extends BaseContentActivity<SentenceDetailReadFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.general_sentence);
	}

	@NonNull
	@Override
	protected SentenceDetailReadFragment createFragment() {
		SentenceDetailReadFragment fragment = new SentenceDetailReadFragment();

		String key = AppConstants.Keys.Params.DOCUMENT_ID;
		ActivityUtils.passStringToFragment(this, fragment, key);

		return fragment;
	}
}
