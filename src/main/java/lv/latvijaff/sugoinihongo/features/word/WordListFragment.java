package lv.latvijaff.sugoinihongo.features.word;


import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseListFragment;
import lv.latvijaff.sugoinihongo.base.HasFabFunctionality;
import lv.latvijaff.sugoinihongo.features.android.activities.StudySelectTypeContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.WordEditContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.WordFavouritesListContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.WordSearchListContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import lv.latvijaff.sugoinihongo.utils.MenuItemUtils;
import timber.log.Timber;

public class WordListFragment extends BaseListFragment<WordModel, WordListViewModel>
	implements HasFabFunctionality {

	public WordListFragment() {
		super(WordListViewModel.class);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		FragmentUtils.inflateListFragmentMenu(menu, inflater);
		inflater.inflate(R.menu.menu_list_word, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);

		MenuItemUtils.setEnabled(menu, R.id.menu_item_list_word_start_study, listHasItems());
		FragmentUtils.setSearchButtonEnabled(menu, listHasItems());
	}

	@Override
	protected void processList(WordListViewModel viewModel) {
		viewModel.processList(this::setAdapter);
	}

	@Override
	protected void setupSubscriptions(WordListViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable listHasItemsDisp = viewModel.observeListHasItems()
			.subscribe(this::setListHasItems);

		cleanUp.add(listHasItemsDisp);
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<WordModel> list) {
		return new WordListAdapter(list);
	}

	@Override
	public void onFabClickedAction(@NonNull Context context) {
		Intent intent = new Intent(context, WordEditContentActivity.class);
		context.startActivity(intent);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		switch (id) {
			case R.id.menu_item_list_word_start_study:
				Timber.i("Menu item clicked: R.id.menu_item_list_word_start_study");
				return this::onStartStudyAction;
			case R.id.menu_item_list_base_search:
				Timber.i("Menu item clicked: R.id.menu_item_list_base_search");
				return this::onSearchAction;
			case R.id.menu_item_list_word_favourites:
				Timber.i("Menu item clicked: R.id.menu_item_list_word_favourites");
				return this::onFavourites;
		}

		return null;
	}

	private void onStartStudyAction() {
		Intent intent = new Intent(getContext(), StudySelectTypeContentActivity.class);
		startActivity(intent);
	}

	private void onSearchAction() {
		Intent intent = new Intent(getContext(), WordSearchListContentActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	private void onFavourites() {
		Intent intent = new Intent(getContext(), WordFavouritesListContentActivity.class);
		startActivity(intent);
	}
}
