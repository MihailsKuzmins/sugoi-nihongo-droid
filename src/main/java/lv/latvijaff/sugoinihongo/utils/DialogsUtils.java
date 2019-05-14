package lv.latvijaff.sugoinihongo.utils;

import android.content.Context;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import lv.latvijaff.sugoinihongo.R;

public class DialogsUtils {

	private DialogsUtils() {}

	public static AlertDialog createYesNoConfirmDialog(Context context, @StringRes int title, @StringRes int message, Runnable yesRunnable) {
		return createYesNoConfirmDialog(context, title, message, yesRunnable, () -> {});
	}

	public static AlertDialog createYesNoConfirmDialog(Context context, @StringRes int title, @StringRes int message, Runnable yesRunnable, Runnable noRunnable) {
		return new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton(R.string.general_yes, (dialog, id) -> {
				yesRunnable.run();
				dialog.dismiss();
			})
			.setNegativeButton(R.string.general_no, ((dialog, id) -> {
				noRunnable.run();
				dialog.dismiss();
			}))
			.create();
	}
}
