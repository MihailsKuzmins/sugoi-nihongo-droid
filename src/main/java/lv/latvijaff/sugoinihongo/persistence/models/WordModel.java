package lv.latvijaff.sugoinihongo.persistence.models;

import java.time.LocalDateTime;

public class WordModel extends BaseModel {

	private String english;
	private String translation;
	private String transcription;
	private String notes;
	private SecondaryProps secondaryProps;

	public WordModel(String id) {
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public SecondaryProps getSecondaryProps() {
		return secondaryProps;
	}

	public void setSecondaryProps(SecondaryProps secondaryProps) {
		this.secondaryProps = secondaryProps;
	}

	public static class SecondaryProps implements Model {

		private String id;
		private String wordId;
		private boolean isStudiable;
		private int mark;
		private LocalDateTime dateLastAccessed;
		private boolean isFavourite;

		public SecondaryProps(String id) {
			this.id = id;
		}

		@Override
		public String getId() {
			return id;
		}

		public String getWordId() {
			return wordId;
		}

		public void setWordId(String wordId) {
			this.wordId = wordId;
		}

		public int getMark() {
			return mark;
		}

		public void setMark(int mark) {
			this.mark = mark;
		}

		public LocalDateTime getDateLastAccessed() {
			return dateLastAccessed;
		}

		public void setDateLastAccessed(LocalDateTime dateLastAccessed) {
			this.dateLastAccessed = dateLastAccessed;
		}

		public boolean isStudiable() {
			return isStudiable;
		}

		public void setStudiable(boolean studiable) {
			isStudiable = studiable;
		}

		public boolean isFavourite() {
			return isFavourite;
		}

		public void setFavourite(boolean favourite) {
			isFavourite = favourite;
		}
	}
}
