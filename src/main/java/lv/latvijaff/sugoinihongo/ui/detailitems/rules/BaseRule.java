package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public abstract class BaseRule<T> implements Rule<T> {

	@StringRes
	private final int errorMessage;

	BaseRule(@StringRes int errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getErrorMessage(@NonNull Context context) {
		return context.getString(errorMessage);
	}

	@StringRes
	final int getErrorMessageStringResource() {
		return errorMessage;
	}
}
