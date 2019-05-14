package lv.latvijaff.sugoinihongo.features.study;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lv.latvijaff.sugoinihongo.persistence.models.QuizResultModel;

public class QuizResults implements Parcelable {

	private int score;
	private int mistakes;
	private List<QuizResultModel> models = new ArrayList<>();

	QuizResults() {}

	private QuizResults(Parcel in) {
		score = in.readInt();
		mistakes = in.readInt();
		models = in.createTypedArrayList(QuizResultModel.CREATOR);
	}

	public static final Creator<QuizResults> CREATOR = new Creator<QuizResults>() {
		@Override
		public QuizResults createFromParcel(Parcel in) {
			return new QuizResults(in);
		}

		@Override
		public QuizResults[] newArray(int size) {
			return new QuizResults[size];
		}
	};

	int getScore() {
		return score;
	}

	void setScore(int score) {
		this.score = score;
	}

	int getMistakes() {
		return mistakes;
	}

	void setMistakes(int mistakes) {
		this.mistakes = mistakes;
	}

	public List<QuizResultModel> getModels() {
		return models;
	}

	void addQuizResultModel(QuizResultModel model) {
		models.add(model);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(score);
		dest.writeInt(mistakes);
		dest.writeTypedList(models);
	}
}
