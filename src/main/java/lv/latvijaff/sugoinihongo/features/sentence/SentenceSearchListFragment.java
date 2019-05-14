package lv.latvijaff.sugoinihongo.features.sentence;

import java.util.List;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.base.BaseSearchListFragment;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;

public class SentenceSearchListFragment extends BaseSearchListFragment<SentenceModel, SentenceSearchListViewModel> {

	public SentenceSearchListFragment() {
		super(SentenceSearchListViewModel.class);
	}

	@Override
	protected void processList(SentenceSearchListViewModel viewModel) {
		viewModel.processList(list -> {
			setAdapter(list);
			filterList();
		});
	}

	@NonNull
	@Override
	protected BaseListAdapter getListAdapter(List<SentenceModel> list) {
		return new SentenceSearchListAdapter(list);
	}
}
