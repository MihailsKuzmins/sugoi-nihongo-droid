package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.R;

public class GreaterThanOrEqualRule<T extends Comparable<T>> extends BaseRule<T> {

	private final T mMinValue;

	public GreaterThanOrEqualRule(@NonNull T minValue) {
		super(R.string.validation_comparable_greater_than_or_equal_message);

		mMinValue = minValue;
	}

	@Override
	public String getErrorMessage(@NonNull Context context) {
		@StringRes int errorMessage = getErrorMessageStringResource();
		return context.getString(errorMessage, mMinValue);
	}

	@Override
	public boolean validate(T value) {
		int result = mMinValue.compareTo(value);
		return result <= 0;
	}
}
