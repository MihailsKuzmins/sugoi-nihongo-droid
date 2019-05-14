package lv.latvijaff.sugoinihongo.features.word;


import java.util.List;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseSearchListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;

public class WordSearchListFragment extends BaseSearchListFragment<WordModel, WordSearchListViewModel> {

	public WordSearchListFragment() {
		super(WordSearchListViewModel.class);
	}

	@Override
	protected void processList(WordSearchListViewModel viewModel) {
		viewModel.processList(list -> {
			setAdapter(list);
			filterList();
		});
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<WordModel> list) {
		return new WordSearchListAdapter(list);
	}
}
