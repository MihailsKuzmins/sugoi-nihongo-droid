package lv.latvijaff.sugoinihongo.features.android.activities;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceDetailEditFragment;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class SentenceEditContentActivity extends BaseContentActivity<SentenceDetailEditFragment> {

	@NonNull
	@Override
	protected SentenceDetailEditFragment createFragment() {
		SentenceDetailEditFragment fragment = new SentenceDetailEditFragment();

		String key = AppConstants.Keys.Params.DOCUMENT_ID;
		String id = ActivityUtils.passStringToFragment(this, fragment, key);
		setToolbarTitle(id);

		return fragment;
	}

	private void setToolbarTitle(String id) {
		@StringRes int title = StringUtils.isNullOrEmpty(id)
			? R.string.models_add_sentence
			: R.string.models_edit_sentence;

		setTitle(title);
	}
}
