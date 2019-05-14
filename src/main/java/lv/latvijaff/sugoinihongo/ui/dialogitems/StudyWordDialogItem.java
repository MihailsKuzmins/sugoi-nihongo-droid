package lv.latvijaff.sugoinihongo.ui.dialogitems;

import android.content.Context;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class StudyWordDialogItem extends BaseDialogItem {

	public StudyWordDialogItem(Context context, String translation, String transcription) {
		String text = createText(context, translation, transcription);
		setText(text);
	}

	private static String createText(Context context, String translation, String transcription) {
		String text = context.getString(R.string.general_translation) + ": " + translation;

		if (!StringUtils.isNullOrEmpty(transcription)) {
			text += System.lineSeparator()
				+ context.getString(R.string.general_transcription) + ": " + transcription;
		}

		return text;
	}
}
