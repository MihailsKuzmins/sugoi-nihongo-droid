package lv.latvijaff.sugoinihongo.features.study;

class QuizOption extends BaseQuizItem {

	private boolean isCorrectAnswer;

	QuizOption(String text, String longText) {
		super(text, longText);
	}

	boolean isCorrectAnswer() {
		return isCorrectAnswer;
	}

	void setCorrectAnswer(boolean correctAnswer) {
		isCorrectAnswer = correctAnswer;
	}
}
