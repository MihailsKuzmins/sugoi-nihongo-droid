package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Keys.Params;
import lv.latvijaff.sugoinihongo.features.word.WordSecondaryPropsDetailReadFragment;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;

public class WordSecondaryPropsReadContentActivity extends BaseContentActivity<WordSecondaryPropsDetailReadFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.models_word_additional_info);
	}

	@NonNull
	@Override
	protected WordSecondaryPropsDetailReadFragment createFragment() {
		WordSecondaryPropsDetailReadFragment fragment = new WordSecondaryPropsDetailReadFragment();
		ActivityUtils.passStringToFragment(this, fragment, Params.DOCUMENT_ID);

		return fragment;
	}
}
