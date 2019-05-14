package lv.latvijaff.sugoinihongo.utils;

import java.util.Optional;

public final class IntUtils {

	public static final int DEFAULT_VALUE = 0;

	private IntUtils() {}

	public static int unbox(Integer integer) {
		return Optional.ofNullable(integer)
			.orElse(0);
	}
}
