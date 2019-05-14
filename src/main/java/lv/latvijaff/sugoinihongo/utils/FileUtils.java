package lv.latvijaff.sugoinihongo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

public final class FileUtils {

	private static final int BUF_SIZE = 16 * 1024;

	public static void copyFile(File originalFile, File destinationFile) throws IOException {
		try (InputStream originalStream = new BufferedInputStream(new FileInputStream(originalFile));
			 OutputStream destinationStream = new BufferedOutputStream(new FileOutputStream(destinationFile))) {

			writeFile(originalStream, destinationStream);
		}
	}

	public static void writeFile(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		int size;

		while ((size = inputStream.read(buf)) != -1) {
			outputStream.write(buf, 0, size);
		}
	}

	public static void deleteFile(File file) {
		if (file.delete()) {
			Timber.d("Deleted file: %s", file.getName());
		} else {
			Timber.e("Could not delete file: %s", file.getName());
		}
	}
}
