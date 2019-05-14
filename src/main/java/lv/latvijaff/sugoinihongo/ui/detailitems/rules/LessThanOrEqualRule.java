package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.R;

public class LessThanOrEqualRule<T extends Comparable<T>> extends BaseRule<T> {

	private final T mMaxValue;

	public LessThanOrEqualRule(@NonNull T maxValue) {
		super(R.string.validation_comparable_less_than_or_equal_message);

		mMaxValue = maxValue;
	}

	@Override
	public String getErrorMessage(@NonNull Context context) {
		@StringRes int errorMessage = getErrorMessageStringResource();
		return context.getString(errorMessage, mMaxValue);
	}

	@Override
	public boolean validate(T value) {
		int result = mMaxValue.compareTo(value);
		return result >= 0;
	}
}
