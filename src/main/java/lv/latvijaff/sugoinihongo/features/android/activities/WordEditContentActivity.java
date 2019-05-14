package lv.latvijaff.sugoinihongo.features.android.activities;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.word.WordDetailEditFragment;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class WordEditContentActivity extends BaseContentActivity<WordDetailEditFragment> {

	@NonNull
	@Override
	protected WordDetailEditFragment createFragment() {
		WordDetailEditFragment fragment = new WordDetailEditFragment();

		String key = AppConstants.Keys.Params.DOCUMENT_ID;
		String id = ActivityUtils.passStringToFragment(this, fragment, key);
		setToolbarTitle(id);

		return fragment;
	}

	private void setToolbarTitle(String id) {
		@StringRes int title = StringUtils.isNullOrEmpty(id)
			? R.string.models_add_word
			: R.string.models_edit_word;

		setTitle(title);
	}
}
