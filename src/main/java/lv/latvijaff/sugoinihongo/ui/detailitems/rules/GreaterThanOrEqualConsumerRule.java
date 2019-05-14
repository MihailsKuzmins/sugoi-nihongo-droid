package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.function.Supplier;

import lv.latvijaff.sugoinihongo.R;

public class GreaterThanOrEqualConsumerRule<T extends Comparable<T>> extends BaseRule<T> {

	private final Supplier<T> mMinValueSupplier;

	public GreaterThanOrEqualConsumerRule(@NonNull Supplier<T> minValueSupplier) {
		super(R.string.validation_comparable_greater_than_or_equal_message);

		mMinValueSupplier = minValueSupplier;
	}

	@Override
	public String getErrorMessage(@NonNull Context context) {
		@StringRes int errorMessage = getErrorMessageStringResource();
		T minValue = mMinValueSupplier.get();

		return context.getString(errorMessage, minValue);
	}

	@Override
	public boolean validate(T value) {
		T minValue = mMinValueSupplier.get();
		int result = minValue.compareTo(value);

		return result <= 0;
	}
}
