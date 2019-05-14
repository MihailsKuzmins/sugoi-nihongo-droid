package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.function.Supplier;

import lv.latvijaff.sugoinihongo.R;

public class LessThanOrEqualConsumerRule<T extends Comparable<T>> extends BaseRule<T> {

	private final Supplier<T> mMaxValueSupplier;

	public LessThanOrEqualConsumerRule(@NonNull Supplier<T> maxValueSupplier) {
		super(R.string.validation_comparable_less_than_or_equal_message);

		mMaxValueSupplier = maxValueSupplier;
	}

	@Override
	public String getErrorMessage(@NonNull Context context) {
		@StringRes int errorMessage = getErrorMessageStringResource();
		T maxValue = mMaxValueSupplier.get();

		return context.getString(errorMessage, maxValue);
	}

	@Override
	public boolean validate(T value) {
		T maxValue = mMaxValueSupplier.get();
		int result = maxValue.compareTo(value);

		return result >= 0;
	}
}
