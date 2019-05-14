package lv.latvijaff.sugoinihongo.features.study;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.databinding.CellStudyResultBinding;
import lv.latvijaff.sugoinihongo.persistence.models.QuizResultModel;
import lv.latvijaff.sugoinihongo.ui.listitems.StudyResultListItem;

public class StudyResultListAdapter extends BaseListAdapter<QuizResultModel, StudyResultListItem, CellStudyResultBinding, StudyResultListAdapter.ViewHolder> {

	StudyResultListAdapter(List<QuizResultModel> list) {
		super(list);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellStudyResultBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_study_result, parent, false);

		return new ViewHolder(binding);
	}

	@NonNull
	@Override
	protected StudyResultListItem transform(@NonNull QuizResultModel model) {
		return new StudyResultListItem(model);
	}

	public static class ViewHolder extends BaseListAdapter.BaseViewHolder<StudyResultListItem, CellStudyResultBinding> {

		public ViewHolder(@NonNull CellStudyResultBinding binding) {
			super(binding);
		}

		@Override
		protected void bind(StudyResultListItem listItem, CellStudyResultBinding binding) {
			listItem.setText(getContext());
			binding.setListItem(listItem);
		}
	}
}
