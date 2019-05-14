package lv.latvijaff.sugoinihongo.features.word;


import androidx.annotation.NonNull;

import java.util.List;

import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;

public class WordFavouritesListFragment extends BaseListFragment<WordModel, WordFavouritesListViewModel> {

	public WordFavouritesListFragment() {
		super(WordFavouritesListViewModel.class);
	}

	@Override
	protected void processList(WordFavouritesListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<WordModel> list) {
		return new WordListAdapter(list);
	}
}
