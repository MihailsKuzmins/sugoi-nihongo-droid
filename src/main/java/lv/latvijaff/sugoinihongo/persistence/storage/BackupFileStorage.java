package lv.latvijaff.sugoinihongo.persistence.storage;

import java.io.File;

import lv.latvijaff.sugoinihongo.constants.AppConstants;

public class BackupFileStorage extends BaseFileStorage {

	private final String REFERENCE = AppConstants.Firestore.Dirs.BACKUPS;

	public void uploadFile(File file, Runnable runnable) {
		uploadFile(REFERENCE, file, runnable);
	}
}
