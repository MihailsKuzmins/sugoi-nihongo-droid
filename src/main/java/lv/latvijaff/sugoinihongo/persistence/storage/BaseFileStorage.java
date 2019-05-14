package lv.latvijaff.sugoinihongo.persistence.storage;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

import timber.log.Timber;

abstract class BaseFileStorage {

	private final FirebaseStorage mStorage = FirebaseStorage.getInstance();

	void uploadFile(@NonNull final String reference, @NonNull final File file, Runnable runnable) {
		final Uri uri = Uri.fromFile(file);
		final String fileName = file.getName();

		mStorage.getReference(reference).child(fileName).putFile(uri)
			.addOnCompleteListener(task -> {
				if (task.isSuccessful()) {
					Timber.i("Uploaded file: %s", fileName);
					runnable.run();
				} else {
					Timber.e(task.getException());
				}
			});
	}
}
