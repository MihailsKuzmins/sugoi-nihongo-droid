package lv.latvijaff.sugoinihongo.features.study;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.databinding.CellStudyWordBinding;
import lv.latvijaff.sugoinihongo.databinding.DialogStudyWordBinding;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.ui.dialogitems.StudyWordDialogItem;
import lv.latvijaff.sugoinihongo.ui.listitems.StudyWordListItem;

public class StudyListOfWordsListAdapter extends BaseListAdapter<WordModel, StudyWordListItem, CellStudyWordBinding, StudyListOfWordsListAdapter.ViewHolder> {

	StudyListOfWordsListAdapter(List<WordModel> list) {
		super(list);
	}

	@NonNull
	@Override
	protected StudyWordListItem transform(@NonNull WordModel model) {
		return new StudyWordListItem(model);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellStudyWordBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_study_word, parent, false);

		return new ViewHolder(binding);
	}

	public static class ViewHolder extends BaseListAdapter.BaseViewHolder<StudyWordListItem, CellStudyWordBinding> {

		public ViewHolder(@NonNull CellStudyWordBinding binding) {
			super(binding);
		}

		public void itemClickedAction(StudyWordListItem listItem) {
			Context context = getContext();

			DialogStudyWordBinding binding = DataBindingUtil.inflate(
				LayoutInflater.from(context), R.layout.dialog_study_word, null, false);

			AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setView(binding.getRoot())
				.setNegativeButton(R.string.general_cancel, (dialog, id) -> dialog.dismiss())
				.create();

			String translation = listItem.getTranslation();
			String transcription = listItem.getTranscription();

			StudyWordDialogItem dialogItem = new StudyWordDialogItem(context, translation, transcription);
			binding.setDialogItem(dialogItem);

			alertDialog.show();
		}

		@Override
		protected void bind(StudyWordListItem listItem, CellStudyWordBinding binding) {
			binding.setViewHolder(this);
			binding.setListItem(listItem);
		}
	}
}
