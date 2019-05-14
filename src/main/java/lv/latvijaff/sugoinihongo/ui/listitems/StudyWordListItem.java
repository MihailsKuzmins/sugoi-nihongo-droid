package lv.latvijaff.sugoinihongo.ui.listitems;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;

public class StudyWordListItem extends BaseListItem {

	private String translation;
	private String transcription;

	public StudyWordListItem(@NonNull WordModel model) {
		super(model.getId());

		String text = model.getEnglish();
		setText(text);

		translation = model.getTranslation();
		transcription = model.getTranscription();
	}

	public String getTranslation() {
		return translation;
	}

	public String getTranscription() {
		return transcription;
	}
}
