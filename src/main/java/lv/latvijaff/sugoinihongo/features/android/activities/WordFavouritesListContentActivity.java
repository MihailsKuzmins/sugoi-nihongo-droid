package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseContentActivity;
import lv.latvijaff.sugoinihongo.features.word.WordFavouritesListFragment;

public class WordFavouritesListContentActivity extends BaseContentActivity<WordFavouritesListFragment> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.models_word_favourites);
	}

	@NonNull
	@Override
	protected WordFavouritesListFragment createFragment() {
		return new WordFavouritesListFragment();
	}
}
