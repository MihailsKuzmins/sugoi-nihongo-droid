package lv.latvijaff.sugoinihongo;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;

import lv.latvijaff.sugoinihongo.utils.ContextUtils;
import timber.log.Timber;

class StorageInitialiser {

	void run(@NonNull Context context) {
		initDir(ContextUtils.getBackupDir(context));
		initDir(ContextUtils.getBackupDirForSync(context));
	}

	private static void initDir(File dir) {
		if (dir.mkdir()) {
			Timber.i("Creating a directory: %s", dir.getAbsolutePath());
		} else {
			Timber.d("Directory already exists: %s", dir.getAbsolutePath());
		}
	}
}
