package lv.latvijaff.sugoinihongo.utils;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.constants.AppConstants;

public final class ActivityUtils {

	private ActivityUtils() {}

	public static void setAppTheme(Activity activity) {
		boolean isTestDbInUse = ContextUtils.getSharedPreferences(activity)
			.getBoolean(AppConstants.SharedPrefs.Keys.IS_TEST_DB_IN_USE, false);

		final @StyleRes int theme = isTestDbInUse
			? R.style.AppTheme_Test
			: R.style.AppTheme;

		activity.setTheme(theme);
	}

	public static String passStringToFragment(Activity activity, Fragment fragment, String key) {
		String id = activity.getIntent().getStringExtra(key);

		Bundle bundle = new Bundle();
		bundle.putString(key, id);

		fragment.setArguments(bundle);
		return id;
	}
}
