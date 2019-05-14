package lv.latvijaff.sugoinihongo.features.study;


import androidx.annotation.NonNull;

import java.util.List;

import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;

public class StudyListOfWordsListFragment extends BaseListFragment<WordModel, StudyListOfWordsListViewModel> {

	public StudyListOfWordsListFragment() {
		super(StudyListOfWordsListViewModel.class);
	}

	@Override
	protected void processList(StudyListOfWordsListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<WordModel> list) {
		return new StudyListOfWordsListAdapter(list);
	}
}
