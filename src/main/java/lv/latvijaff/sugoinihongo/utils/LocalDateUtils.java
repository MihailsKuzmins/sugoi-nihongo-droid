package lv.latvijaff.sugoinihongo.utils;

import android.content.Context;
import android.os.LocaleList;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.constants.AppConstants;

public final class LocalDateUtils {

	private LocalDateUtils() {}

	public static String toStringDefaultFormat(@NonNull LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("ddMMyyyy_HHmmss")
			.withZone(ZoneId.systemDefault());

		return dateTime.format(formatter);
	}

	public static String toStringWithNeverLabel(@NonNull Context context, @NonNull LocalDateTime dateTime) {
		if (dateTime.compareTo(AppConstants.Models.MIN_DATE_TIME) == 0) {
			return context.getString(R.string.general_never);
		}

		Locale locale = getCurrentOrDefaultLocale(context);

		DateTimeFormatter formatter = DateTimeFormatter
			.ofLocalizedDateTime(FormatStyle.LONG)
			.withLocale(locale)
			.withZone(ZoneId.systemDefault());

		return dateTime.format(formatter);
	}

	public static long toEpoch(LocalDateTime localDateTime) {
		return toInstant(localDateTime)
			.getEpochSecond();
	}

	public static Instant toInstant(LocalDateTime localDateTime) {
		return localDateTime
			.atZone(ZoneId.systemDefault())
			.toInstant();
	}

	private static Locale getCurrentOrDefaultLocale(@NonNull Context context) {
		LocaleList list = context.getResources().getConfiguration().getLocales();

		return list.isEmpty()
			? Locale.getDefault()
			: list.get(0);
	}
}
