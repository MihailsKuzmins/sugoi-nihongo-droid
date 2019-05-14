package lv.latvijaff.sugoinihongo.features.android.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.persistence.storage.BackupFileStorage;
import lv.latvijaff.sugoinihongo.utils.ContextUtils;
import lv.latvijaff.sugoinihongo.utils.FileUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;


public class SystemBackupSynchronisationService extends IntentService {

	@Inject
	BackupFileStorage storage;

	public SystemBackupSynchronisationService() {
		super(SystemBackupSynchronisationService.class.getName());
	}

	@Override
	public void onCreate() {
		super.onCreate();

		App app = (App) getApplication();
		app.getStorageComponent().inject(this);
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		boolean isInternetAvailable = isInternetAvailable();
		if (!isInternetAvailable) {
			return;
		}

		File[] backupFiles = ContextUtils.getBackupDirForSync(this)
			.listFiles((dir, name) -> name.endsWith(ContextUtils.BACKUP_EXTENSION));

		for (File file : backupFiles) {
			storage.uploadFile(file, () -> FileUtils.deleteFile(file));
		}
	}

	private boolean isInternetAvailable() {
		try {
			InetAddress address = InetAddress.getByName("google.com");
			return !StringUtils.isNullOrEmpty(address.getHostName());
		} catch (UnknownHostException e) {
			return false;
		}
	}
}
