package lv.latvijaff.sugoinihongo.features.word;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailReadFragment;
import lv.latvijaff.sugoinihongo.databinding.FragmentWordDetailReadBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.WordEditContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.WordSecondaryPropsReadContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import timber.log.Timber;

public class WordDetailReadFragment extends BaseDetailReadFragment<WordModel, WordDetailReadViewModel, FragmentWordDetailReadBinding> {

	public WordDetailReadFragment() {
		super(WordDetailReadViewModel.class, R.layout.fragment_word_detail_read);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		FragmentUtils.inflateDetailReadFragmentMenu(menu, inflater);
		inflater.inflate(R.menu.menu_word_detail_read, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	protected void applyBinding(final @NonNull FragmentWordDetailReadBinding binding, final @NonNull WordDetailReadViewModel viewModel) {
		String id = getModelId();

		binding.setViewModel(viewModel);
		viewModel.processDocument(id);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		switch (id) {
			case R.id.menu_item_detail_read_base_edit:
				Timber.i("Menu item clicked: R.id.menu_item_detail_read_base_edit");
				return this::onEditAction;
			case R.id.menu_item_word_detail_read_additional_info:
				Timber.i("Menu item clicked: R.id.menu_item_word_detail_read_additional_info");
				return this::onAdditionalInfoAction;
		}

		return null;
	}

	private void onEditAction() {
		Intent intent = createNavigationIntent(WordEditContentActivity.class);
		startActivity(intent);
	}

	private void onAdditionalInfoAction() {
		// SecondaryProps intent accepts wordId as a DOCUMENT_ID parameter
		Intent intent = createNavigationIntent(WordSecondaryPropsReadContentActivity.class);
		startActivity(intent);
	}
}
