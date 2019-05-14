package lv.latvijaff.sugoinihongo.ui.listitems;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;

public class SentenceListItem extends BaseListItem {

	public SentenceListItem(@NonNull SentenceModel model) {
		super(model.getId());

		String text = createText(model.getEnglish(), model.getTranslation());
		setText(text);
	}

	private String createText(String english, String translation) {
		return english
			+ System.lineSeparator()
			+ translation;
	}
}
