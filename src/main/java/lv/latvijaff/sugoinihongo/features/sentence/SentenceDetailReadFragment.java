package lv.latvijaff.sugoinihongo.features.sentence;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailReadFragment;
import lv.latvijaff.sugoinihongo.databinding.FragmentSentenceDetailReadBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.SentenceEditContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import timber.log.Timber;

public class SentenceDetailReadFragment extends BaseDetailReadFragment<SentenceModel, SentenceDetailReadViewModel, FragmentSentenceDetailReadBinding> {

	public SentenceDetailReadFragment() {
		super(SentenceDetailReadViewModel.class, R.layout.fragment_sentence_detail_read);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		FragmentUtils.inflateDetailReadFragmentMenu(menu, inflater);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	protected void applyBinding(final @NonNull FragmentSentenceDetailReadBinding binding, final @NonNull SentenceDetailReadViewModel viewModel) {
		String id = getModelId();

		binding.setViewModel(viewModel);
		viewModel.processDocument(id);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		if (id == R.id.menu_item_detail_read_base_edit) {
			Timber.i("Menu item clicked: R.id.menu_item_detail_read_base_edit");
			return this::onEditAction;
		}

		return null;
	}

	private void onEditAction() {
		Intent intent = createNavigationIntent(SentenceEditContentActivity.class);
		startActivity(intent);
	}
}
