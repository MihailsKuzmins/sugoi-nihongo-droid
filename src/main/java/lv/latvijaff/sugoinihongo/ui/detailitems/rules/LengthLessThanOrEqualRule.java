package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.R;

public class LengthLessThanOrEqualRule extends BaseRule<String> {

	private final int mMaxLength;

	public LengthLessThanOrEqualRule(final int maxLength) {
		super(R.string.validation_string_length_less_than_or_equal_message);

		mMaxLength = maxLength;
	}

	@Override
	public String getErrorMessage(@NonNull Context context) {
		@StringRes int errorMessage = getErrorMessageStringResource();
		return context.getString(errorMessage, mMaxLength);
	}

	@Override
	public boolean validate(String value) {
		int length = value == null ? 0 : value.length();
		return length <= mMaxLength;
	}
}
