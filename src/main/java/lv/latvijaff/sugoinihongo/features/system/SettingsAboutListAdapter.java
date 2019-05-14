package lv.latvijaff.sugoinihongo.features.system;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseListAdapter;
import lv.latvijaff.sugoinihongo.databinding.CellThirdPartyLibraryBinding;
import lv.latvijaff.sugoinihongo.persistence.models.ThirdPartyLibraryModel;
import lv.latvijaff.sugoinihongo.ui.listitems.ThirdPartyLibraryListItem;

public class SettingsAboutListAdapter extends BaseListAdapter<ThirdPartyLibraryModel, ThirdPartyLibraryListItem, CellThirdPartyLibraryBinding, SettingsAboutListAdapter.ViewHolder> {

	SettingsAboutListAdapter(List<ThirdPartyLibraryModel> list) {
		super(list);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CellThirdPartyLibraryBinding binding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.getContext()),
			R.layout.cell_third_party_library, parent, false);

		return new ViewHolder(binding);
	}

	@NonNull
	@Override
	protected ThirdPartyLibraryListItem transform(@NonNull ThirdPartyLibraryModel model) {
		return new ThirdPartyLibraryListItem(model);
	}

	public class ViewHolder extends BaseListAdapter.BaseViewHolder<ThirdPartyLibraryListItem, CellThirdPartyLibraryBinding> {

		@BindView(R.id.list_item_third_party_lib_view_stub)
		ViewStub viewStub;

		public ViewHolder(@NonNull CellThirdPartyLibraryBinding binding) {
			super(binding);

			ButterKnife.bind(this, binding.getRoot());
		}

		@Override
		protected void bind(ThirdPartyLibraryListItem listItem, CellThirdPartyLibraryBinding binding) {
			binding.setListItem(listItem);

			int layoutResource = viewStub.getLayoutResource();

			// when a list item is created by the RecyclerView, LayoutResource is not reset
			// so the LayoutResource must be set, when its value is not default, i.e. "0"
			if (layoutResource == 0) {
				viewStub.setLayoutResource(listItem.getLicenceLayout());
				viewStub.inflate();
			}
		}
	}
}
