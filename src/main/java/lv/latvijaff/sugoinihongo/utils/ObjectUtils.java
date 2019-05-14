package lv.latvijaff.sugoinihongo.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Consumer;

public final class ObjectUtils {

	protected ObjectUtils() {}

	public static <T> void invokeIfNotNull(@Nullable T obj, @NonNull Consumer<T> consumer) {
		if (obj != null) {
			consumer.accept(obj);
		}
	}
}
