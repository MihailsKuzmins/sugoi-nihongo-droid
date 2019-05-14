package lv.latvijaff.sugoinihongo.ui.listitems;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class WordListItem extends BaseListItem {

	public WordListItem(@NonNull WordModel model) {
		super(model.getId());

		String text = createText(model.getTranslation(), model.getTranscription(), model.getEnglish());
		setText(text);
	}

	private String createText(String translation, String transcription, String english) {
		String text = translation;
		String transcriptionText = createTranscriptionText(transcription);

		if (transcriptionText != null) {
			text += "\t" + transcriptionText;
		}

		return text
			+ System.lineSeparator()
			+ english;
	}

	private static String createTranscriptionText(String transcription) {
		return !StringUtils.isNullOrEmpty(transcription)
			? String.format("[%s]", transcription)
			: null;
	}
}
