package lv.latvijaff.sugoinihongo.features.system;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.constants.AppConstants.Keys.Navigation;
import lv.latvijaff.sugoinihongo.databinding.CellSettingBinding;
import lv.latvijaff.sugoinihongo.features.android.activities.SettingsAboutListContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.SettingsStudyContentActivity;
import lv.latvijaff.sugoinihongo.features.android.activities.SettingsTechContentActivity;
import lv.latvijaff.sugoinihongo.persistence.models.SettingModel;
import lv.latvijaff.sugoinihongo.ui.listitems.SettingListItem;

public class SettingsListAdapter extends BaseListAdapter<SettingModel, SettingListItem, CellSettingBinding, SettingsListAdapter.ViewHolder> {

	SettingsListAdapter(List<SettingModel> list) {
		super(list);
	}

	@NonNull
	@Override
	protected SettingListItem transform(@NonNull SettingModel model) {
		return new SettingListItem(model);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellSettingBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_setting, parent, false);

		return new ViewHolder(binding);
	}

	public static class ViewHolder extends BaseListAdapter.BaseViewHolder<SettingListItem, CellSettingBinding> {

		public ViewHolder(@NonNull CellSettingBinding binding) {
			super(binding);
		}

		@Override
		public void itemClickedAction(@NonNull String id) {
			Context context = getContext();
			Class<?> activityClass = getSettingsActivityClass(id);

			Intent intent = new Intent(context, activityClass);
			context.startActivity(intent);
		}

		@Override
		protected void bind(SettingListItem listItem, CellSettingBinding binding) {
			Context context = getContext();
			listItem.setTitle(context);
			listItem.setDescription(context);

			binding.setListItem(listItem);
			binding.setViewHolder(this);
		}

		private Class<?> getSettingsActivityClass(String id) {
			switch (id) {
				case Navigation.SETTINGS_STUDY:
					return SettingsStudyContentActivity.class;
				case Navigation.SETTINGS_TECH:
					return SettingsTechContentActivity.class;
				case Navigation.SETTINGS_ABOUT:
					return SettingsAboutListContentActivity.class;

				default:
					throw new IllegalArgumentException("Cannot resolve a SettingsActivity for " + id);
			}
		}
	}
}
