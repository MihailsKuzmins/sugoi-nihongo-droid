package lv.latvijaff.sugoinihongo.base;

import android.content.Context;
import android.widget.Filter;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import lv.latvijaff.sugoinihongo.persistence.models.Model;
import lv.latvijaff.sugoinihongo.ui.listitems.BaseListItem;

public abstract class BaseListAdapter<TModel extends Model, TListItem extends BaseListItem, TBinding extends ViewDataBinding,
		TViewHolder extends BaseListAdapter.BaseViewHolder<TListItem, TBinding>>
	extends RecyclerView.Adapter<TViewHolder> {

	private final List<TModel> originalList;
	private List<TModel> filteredList;

	protected BaseListAdapter(List<TModel> list) {
		this.originalList = list;
		this.filteredList = list;
	}

	@Override
	public final void onBindViewHolder(@NonNull TViewHolder holder, int position) {
		TModel model = filteredList.get(position);
		TListItem listItem = transform(model);
		holder.bind(listItem);
	}

	@Override
	public final int getItemCount() {
		return filteredList.size();
	}

	@NonNull
	protected abstract TListItem transform(@NonNull TModel model);

	protected final Filter createFilter(BiFunction<TModel, String, Boolean> predicate) {
		return new android.widget.Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final String searchText = constraint.toString().toLowerCase();

				List<TModel> list = originalList.stream()
					.filter(x -> predicate.apply(x, searchText))
					.collect(Collectors.toList());

				FilterResults results = new FilterResults();
				results.values = list;

				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				@SuppressWarnings("unchecked")
				List<TModel> list = (List<TModel>)results.values;
				filteredList = list;
				notifyDataSetChanged();
			}
		};
	}

	public static abstract class BaseViewHolder<TListItem extends BaseListItem, TBinding extends ViewDataBinding>
		extends RecyclerView.ViewHolder {

		private final TBinding binding;

		public BaseViewHolder(@NonNull TBinding binding) {
			super(binding.getRoot());

			this.binding = binding;
		}

		protected final Context getContext() {
			return binding.getRoot().getContext();
		}

		public final void bind(TListItem listItem) {
			bind(listItem, binding);
		}

		public void itemClickedAction(@NonNull String id) {}

		protected abstract void bind(final TListItem listItem, final TBinding binding);
	}
}
