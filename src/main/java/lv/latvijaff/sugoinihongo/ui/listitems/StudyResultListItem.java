package lv.latvijaff.sugoinihongo.ui.listitems;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.QuizResultModel;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class StudyResultListItem extends BaseListItem {

	private boolean isCorrect;
	private String questionText;
	private String submittedAnswerText;
	private String correctAnswerText;

	@DrawableRes private int resultImage;
	@StringRes private int resultImageContentDescription;

	public StudyResultListItem(QuizResultModel model) {
		super(model.getId());

		isCorrect = model.isCorrectAnswer();
		resultImage = isCorrect
			? R.drawable.ic_check_green_32dp
			: R.drawable.ic_close_red_32dp;

		resultImageContentDescription = isCorrect
			? R.string.general_correct
			: R.string.general_wrong;

		questionText = createItemText(model.getQuestionText());
		submittedAnswerText = createItemText(model.getSubmittedAnswerText());
		correctAnswerText = createItemText(model.getCorrectAnswerText());
	}

	@DrawableRes
	public int getResultImage() {
		return resultImage;
	}

	@StringRes
	public int getResultImageContentDescription() {
		return resultImageContentDescription;
	}

	public void setText(Context context) {
		String text = createText(context);
		super.setText(text);
	}

	private String createItemText(String item) {
		String[] lines = item.split(AppConstants.System.TRANSLATION_TRANSCRIPTION_SEPARATOR);
		String text = lines[0];

		if (lines.length == 2 && !StringUtils.isNullOrWhiteSpace(lines[1])) {
			text += " [" + lines[1] + "]";
		}

		return text;
	}

	private String createText(Context context) {
		String text = questionText;

		if (!isCorrect) {
			text += ", " + context.getString(R.string.study_correct_answer) + ": " + correctAnswerText;
		}

		return text + System.lineSeparator() +
			context.getString(R.string.study_submitted_answer) + ": " + submittedAnswerText;
	}
}
