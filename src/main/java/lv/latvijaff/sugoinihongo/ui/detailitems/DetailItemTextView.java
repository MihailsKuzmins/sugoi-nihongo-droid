package lv.latvijaff.sugoinihongo.ui.detailitems;

import android.content.Context;

import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class DetailItemTextView extends BaseDetailItem<String> {

	@StringRes private final int mHeader;

	public DetailItemTextView(Context context, @StringRes int header) {
		super(context);

		mHeader = header;
		setVisible(true);
	}

	@StringRes
	public int getHeader() {
		return mHeader;
	}

	@Override
	void onNextValue(String value) {
		if (value == null) {
			value = StringUtils.EMPTY;
		}

		super.onNextValue(value);
	}
}
