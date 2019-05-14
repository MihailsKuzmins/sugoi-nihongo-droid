package lv.latvijaff.sugoinihongo.utils;

import androidx.annotation.Nullable;

import com.google.common.primitives.Ints;

import java.util.Optional;

public final class StringUtils {

	public static final String EMPTY = "";

	private StringUtils() {}

	public static boolean isNullOrEmpty(@Nullable String value) {
		return value == null || value.isEmpty();
	}

	public static boolean isNullOrWhiteSpace(@Nullable String value) {
		if (value == null) {
			return true;
		}

		for (int i = 0; i < value.length(); i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	public static int toInt(@Nullable String value) {
		return Optional.ofNullable(value)
			.map(Ints::tryParse)
			.orElse(0);
	}

	static String getFirstNonEmpty(final String string1, final String string2) {
		String text = isNullOrEmpty(string1)
			? string2
			: string1;

		if (isNullOrEmpty(text)) {
			throw new IllegalArgumentException("String2 is empty");
		}

		return text;
	}

	public static String removeWhiteSpaces(String value) {
		return value.replaceAll("\\s+", " ");
	}

	public static String emptyIfNull(String string) {
		return string == null
			? EMPTY
			: string;
	}

	@Nullable
	public static String trim(@Nullable String string) {
		return isNullOrWhiteSpace(string)
			? null
			: string.trim();
	}
}
