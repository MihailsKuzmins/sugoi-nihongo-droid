package lv.latvijaff.sugoinihongo.features.word;

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
import lv.latvijaff.sugoinihongo.databinding.CellWordBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.WordReadContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.ui.listitems.WordListItem;

public class WordListAdapter extends BaseListAdapter<WordModel, WordListItem, CellWordBinding, WordListAdapter.ViewHolder> {

	WordListAdapter(List<WordModel> list) {
		super(list);
	}

	@NonNull
	@Override
	protected WordListItem transform(@NonNull WordModel model) {
		return new WordListItem(model);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellWordBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_word, parent, false);

		return new ViewHolder(binding);
	}


	public static class ViewHolder extends BaseListAdapter.BaseViewHolder<WordListItem, CellWordBinding> {

		ViewHolder(@NonNull CellWordBinding binding) {
			super(binding);
		}

		@Override
		public void itemClickedAction(@NonNull String id) {
			Context context = getContext();
			Intent intent = new Intent(context, WordReadContentActivity.class);
			intent.putExtra(AppConstants.Keys.Params.DOCUMENT_ID, id);

			context.startActivity(intent);
		}

		@Override
		protected void bind(WordListItem listItem, CellWordBinding binding) {
			binding.setListItem(listItem);
			binding.setViewHolder(this);
		}
	}
}
