package lv.latvijaff.sugoinihongo.persistence.dto;

public class QuizItemDto {

	private String english;
	private String translation;
	private String transcription;
	private boolean isAnswer;

	public QuizItemDto(String english, String translation, String transcription, boolean isAnswer) {
		this.english = english;
		this.translation = translation;
		this.transcription = transcription;
		this.isAnswer = isAnswer;
	}

	public String getEnglish() {
		return english;
	}

	public String getTranslation() {
		return translation;
	}

	public String getTranscription() {
		return transcription;
	}

	public boolean isAnswer() {
		return isAnswer;
	}
}
