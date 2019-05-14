package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import android.content.Context;

import androidx.annotation.NonNull;

public interface Rule<T> {

	String getErrorMessage(@NonNull Context context);
	boolean validate(T value);
}
