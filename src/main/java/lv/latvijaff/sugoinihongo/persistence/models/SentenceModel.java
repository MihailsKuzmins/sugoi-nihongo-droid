package lv.latvijaff.sugoinihongo.persistence.models;

public class SentenceModel extends BaseModel {

	private String english;
	private String translation;
	private String transcription;

	public SentenceModel(String id) {
		super(id);
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}
}
