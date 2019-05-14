package lv.latvijaff.sugoinihongo.features.study;


import android.content.Context;
import android.view.View;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.databinding.ListHeaderStudyResultBinding;
import lv.latvijaff.sugoinihongo.persistence.models.QuizResultModel;

public class StudyResultListFragment extends BaseListFragment<QuizResultModel, StudyResultListViewModel> {

	private QuizResults mQuizResults;

	public StudyResultListFragment() {
		super(StudyResultListViewModel.class);
	}

	@Override
	protected void processList(StudyResultListViewModel viewModel) {
		mQuizResults = Objects.requireNonNull(
			Objects.requireNonNull(getArguments())
				.getParcelable(AppConstants.Keys.Navigation.QUIZ_RESULTS));

		setAdapter(mQuizResults.getModels());
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<QuizResultModel> list) {
		return new StudyResultListAdapter(list);
	}

	@Nullable
	@Override
	protected View createHeaderView(StudyResultListViewModel viewModel) {
		ListHeaderStudyResultBinding binding = DataBindingUtil.inflate(
			getLayoutInflater(), R.layout.list_header_study_result, null, false);

		View view = binding.getRoot();
		Context context = view.getContext();

		viewModel.setScoreText(context, mQuizResults.getScore());
		viewModel.setMistakesText(context, mQuizResults.getMistakes());

		binding.setViewModel(viewModel);

		return view;
	}
}
