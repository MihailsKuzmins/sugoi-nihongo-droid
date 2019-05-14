package lv.latvijaff.sugoinihongo.utils;

import android.os.SystemClock;

import androidx.annotation.NonNull;

public final class RunnableUtils {

	private static long mLastClickTime;

	private RunnableUtils() {}

	public static void runWithMultipleClickPrevention(@NonNull Runnable runnable) {
		if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
			return;
		}

		mLastClickTime = SystemClock.elapsedRealtime();
		runnable.run();
	}
}
