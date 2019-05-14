package lv.latvijaff.sugoinihongo.ui.detailitems;

import android.content.Context;

import androidx.annotation.StringRes;

public class DetailItemSwitch extends BaseDetailItem<Boolean> {

	@StringRes private final int mHeader;
	@StringRes private final int mBody;

	public DetailItemSwitch(Context context, @StringRes int header, @StringRes int body) {
		super(context);

		mHeader = header;
		mBody = body;
	}

	public void toggle() {
		boolean value = getValue();
		setValue(!value);
	}

	@Override
	boolean canSetDefaultValue(final Boolean value, final boolean isFirstLoad) {
		return isFirstLoad && value != null;
	}

	@StringRes
	public int getHeader() {
		return mHeader;
	}

	@StringRes
	public int getBody() {
		return  mBody;
	}
}
