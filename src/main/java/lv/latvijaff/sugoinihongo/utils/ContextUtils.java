package lv.latvijaff.sugoinihongo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import lv.latvijaff.sugoinihongo.BuildConfig;

public final class ContextUtils {

	public static final String BACKUP_EXTENSION = ".bkp.zip";

	private ContextUtils() {}

	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
	}

	public static File getBackupDir(Context context) {
		return new File(context.getExternalFilesDir(null), "backup");
	}

	public static File getBackupDirForSync(Context context) {
		return new File(context.getFilesDir(), "backupSync");
	}
}
