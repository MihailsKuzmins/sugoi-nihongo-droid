package lv.latvijaff.sugoinihongo.persistence.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizResultModel implements Model, Parcelable {

	private String id;
	private String questionText;
	private String submittedAnswerText;
	private String correctAnswerText;

	public QuizResultModel() {}

	private QuizResultModel(Parcel in) {
		id = in.readString();
		questionText = in.readString();
		submittedAnswerText = in.readString();
		correctAnswerText = in.readString();
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getSubmittedAnswerText() {
		return submittedAnswerText;
	}

	public void setSubmittedAnswerText(String submittedAnswerText) {
		this.submittedAnswerText = submittedAnswerText;
	}

	public String getCorrectAnswerText() {
		return correctAnswerText;
	}

	public boolean isCorrectAnswer() {
		return correctAnswerText.equals(submittedAnswerText);
	}

	public void setCorrectAnswerText(String correctAnswerText) {
		this.correctAnswerText = correctAnswerText;
	}

	public static final Creator<QuizResultModel> CREATOR = new Creator<QuizResultModel>() {
		@Override
		public QuizResultModel createFromParcel(Parcel in) {
			return new QuizResultModel(in);
		}

		@Override
		public QuizResultModel[] newArray(int size) {
			return new QuizResultModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(questionText);
		dest.writeString(submittedAnswerText);
		dest.writeString(correctAnswerText);
	}
}
