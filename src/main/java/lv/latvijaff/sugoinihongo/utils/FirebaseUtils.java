package lv.latvijaff.sugoinihongo.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class FirebaseUtils {

	private FirebaseUtils() {}

	public static LocalDate toLocalDate(Object timestamp) {
		return getZonedDateTime(timestamp).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Object timestamp) {
		return getZonedDateTime(timestamp).toLocalDateTime();
	}

	public static Timestamp toTimestamp(LocalDate localDate) {
		return toTimestamp(localDate.atStartOfDay());
	}

	public static Timestamp toTimestamp(LocalDateTime localDateTime) {
		Date date = Date.from(LocalDateUtils.toInstant(localDateTime));
		return new Timestamp(date);
	}

	public static String getFireStoreId(String id) {
		return StringUtils.isNullOrEmpty(id)
			? Util.autoId()
			: id;
	}

	private static ZonedDateTime getZonedDateTime(Object timestamp) {
		Date date = ((Timestamp) timestamp).toDate();
		return date.toInstant().atZone(ZoneId.systemDefault());
	}
}
