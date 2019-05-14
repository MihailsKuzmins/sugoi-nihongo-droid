package lv.latvijaff.sugoinihongo.utils;

import com.google.android.material.textfield.TextInputLayout;

public final class TextInputLayoutUtils {

	private TextInputLayoutUtils() {}

	public static void setErrorMessage(TextInputLayout textInputLayout, String errorMessage) {
		boolean hasError = !StringUtils.isNullOrEmpty(errorMessage);
		textInputLayout.setErrorEnabled(hasError);

		if (hasError) {
			textInputLayout.setError(errorMessage);
		}
	}
}
