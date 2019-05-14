package lv.latvijaff.sugoinihongo.features.grammarrule;

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
import lv.latvijaff.sugoinihongo.databinding.CellGrammarRuleBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.GrammarRuleReadContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.ui.listitems.GrammarRuleListItem;

public class GrammarRuleListAdapter extends BaseListAdapter<GrammarRuleModel, GrammarRuleListItem, CellGrammarRuleBinding, GrammarRuleListAdapter.ViewHolder> {

	GrammarRuleListAdapter(List<GrammarRuleModel> list) {
		super(list);
	}

	@NonNull
	@Override
	protected GrammarRuleListItem transform(@NonNull GrammarRuleModel model) {
		return new GrammarRuleListItem(model);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellGrammarRuleBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_grammar_rule, parent, false);

		return new ViewHolder(binding);
	}

	public static class ViewHolder extends BaseListAdapter.BaseViewHolder<GrammarRuleListItem, CellGrammarRuleBinding> {

		ViewHolder(@NonNull CellGrammarRuleBinding binding) {
			super(binding);
		}

		@Override
		public void itemClickedAction(@NonNull String id) {
			Context context = getContext();
			Intent intent = new Intent(context, GrammarRuleReadContentActivity.class);
			intent.putExtra(AppConstants.Keys.Params.DOCUMENT_ID, id);

			context.startActivity(intent);
		}

		@Override
		protected void bind(final GrammarRuleListItem listItem, final CellGrammarRuleBinding binding) {
			binding.setListItem(listItem);
			binding.setViewHolder(this);
		}
	}
}
