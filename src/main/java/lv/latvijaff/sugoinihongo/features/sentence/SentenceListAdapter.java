package lv.latvijaff.sugoinihongo.features.sentence;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.databinding.CellSentenceBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.SentenceReadContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.ui.listitems.SentenceListItem;

public class SentenceListAdapter extends BaseListAdapter<SentenceModel, SentenceListItem, CellSentenceBinding, SentenceListAdapter.ViewHolder> {

	SentenceListAdapter(List<SentenceModel> list) {
		super(list);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellSentenceBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_sentence, parent, false);

		return new ViewHolder(binding);
	}

	@NonNull
	@Override
	protected SentenceListItem transform(@NonNull SentenceModel model) {
		return new SentenceListItem(model);
	}

	public static class ViewHolder extends BaseListAdapter.BaseViewHolder<SentenceListItem, CellSentenceBinding> {

		ViewHolder(@NonNull CellSentenceBinding binding) {
			super(binding);
		}

		@Override
		public void itemClickedAction(@NonNull String id) {
			Context context = getContext();
			Intent intent = new Intent(context, SentenceReadContentActivity.class);
			intent.putExtra(AppConstants.Keys.Params.DOCUMENT_ID, id);

			context.startActivity(intent);
		}

		@Override
		protected void bind(SentenceListItem listItem, CellSentenceBinding binding) {
			binding.setListItem(listItem);
			binding.setViewHolder(this);
		}
	}
}
