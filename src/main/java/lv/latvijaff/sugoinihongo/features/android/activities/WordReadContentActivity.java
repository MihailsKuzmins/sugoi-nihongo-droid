package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.word.WordDetailReadFragment;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;

public class WordReadContentActivity extends BaseContentActivity<WordDetailReadFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.general_word);
	}

	@NonNull
	@Override
	protected WordDetailReadFragment createFragment() {
		WordDetailReadFragment fragment = new WordDetailReadFragment();

		String key = AppConstants.Keys.Params.DOCUMENT_ID;
		ActivityUtils.passStringToFragment(this, fragment, key);

		return fragment;
	}
}
