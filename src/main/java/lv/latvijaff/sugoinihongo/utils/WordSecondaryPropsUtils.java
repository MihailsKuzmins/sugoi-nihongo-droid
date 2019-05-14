package lv.latvijaff.sugoinihongo.utils;

import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.models.WordStatus;

public final class WordSecondaryPropsUtils {

	private WordSecondaryPropsUtils() {}

	public static WordStatus getWordStatus(WordModel.SecondaryProps secondaryProps) {
		if (!secondaryProps.isStudiable()) {
			return WordStatus.EXCLUDED;
		} else if (secondaryProps.getMark() >= AppConstants.Models.EXCELLENT_WORD_STATUS) {
			return WordStatus.EXCELLENT;
		} else if (secondaryProps.getMark() >= AppConstants.Models.GOOD_WORD_STATUS) {
			return WordStatus.GOOD;
		} else if (secondaryProps.getMark() >= AppConstants.Models.AVERAGE_WORD_STATUS) {
			return WordStatus.AVERAGE;
		}

		return WordStatus.BAD;
	}
}
