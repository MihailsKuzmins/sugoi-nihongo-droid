package lv.latvijaff.sugoinihongo.features.study;

import android.content.Context;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.QuizResultModel;

public class StudyResultListViewModel extends BaseListViewModel<QuizResultModel> {

	private String scoreText;
	private String mistakesText;

	public StudyResultListViewModel(@NonNull App application) {
		super(application, false);
	}

	@Override
	protected void fetchList(Consumer<List<QuizResultModel>> consumer) {
		// List is passed as a Parcelable without interacting with the viewModel
	}

	public String getScoreText() {
		return scoreText;
	}

	void setScoreText(Context context, int score) {
		this.scoreText = context.getString(R.string.study_quiz_score)
			+ ": " + score;
	}

	public String getMistakesText() {
		return mistakesText;
	}

	void setMistakesText(Context context, int mistakes) {
		this.mistakesText = context.getString(R.string.study_quiz_mistakes)
			+ ": " + mistakes;
	}
}
