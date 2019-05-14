package lv.latvijaff.sugoinihongo.features.study;


import android.content.Intent;
import android.widget.Button;

import androidx.annotation.NonNull;

import butterknife.OnClick;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailFragment;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.databinding.FragmentStudySelectTypeBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.StudyListOfWordsListContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.StudyQuizContentActivity;

public class StudySelectTypeFragment extends BaseDetailFragment<StudySelectTypeViewModel, FragmentStudySelectTypeBinding> {

	public StudySelectTypeFragment() {
		super(StudySelectTypeViewModel.class, R.layout.fragment_study_select_type);
	}

	@Override
	protected void applyBinding(final @NonNull FragmentStudySelectTypeBinding binding, final @NonNull StudySelectTypeViewModel viewModel) {
		binding.setViewModel(viewModel);
		viewModel.processWordsGrouping();
	}

	@OnClick(R.id.study_select_type_find_word_button)
	void navigateToFindWordStudy(Button button) {
		Intent intent = new Intent(button.getContext(), StudyQuizContentActivity.class);
		intent.putExtra(AppConstants.Keys.Params.ENGLISH_TO_JAPANESE, true);

		startActivity(intent);
	}

	@OnClick(R.id.study_select_type_recognise_word_button)
	void navigateToRecogniseWordStudy(Button button) {
		Intent intent = new Intent(button.getContext(), StudyQuizContentActivity.class);
		intent.putExtra(AppConstants.Keys.Params.ENGLISH_TO_JAPANESE, false);

		startActivity(intent);
	}

	@OnClick(R.id.study_select_type_list_of_words_button)
	void navigateToListOfWordsStudy(Button button) {
		Intent intent = new Intent(button.getContext(), StudyListOfWordsListContentActivity.class);
		startActivity(intent);
	}
}
