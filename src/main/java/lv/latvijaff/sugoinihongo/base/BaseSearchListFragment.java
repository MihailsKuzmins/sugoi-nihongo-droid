package lv.latvijaff.sugoinihongo.base;


import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.Filterable;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.persistence.models.Model;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public abstract class BaseSearchListFragment<TModel extends Model, TViewModel extends BaseSearchListViewModel<TModel>>
	extends BaseListFragment<TModel, TViewModel>
	implements SearchView.OnQueryTextListener {

	protected BaseSearchListFragment(Class<TViewModel> viewModelClass) {
		super(viewModelClass);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.menu_list_search_base, menu);

		Context context = Objects.requireNonNull(getContext());
		SearchView searchView = getSearchView(menu);

		setSearchViewAttrs(context, searchView);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);

		SearchView searchView = getSearchView(menu);
		searchView.requestFocus();
	}

	@Override
	public final boolean onQueryTextChange(String newText) {
		getViewModel().setQueryText(newText);
		filterList(newText);

		return true;
	}

	@Override
	public final boolean onQueryTextSubmit(String query) {
		return true;
	}

	protected final void filterList() {
		String queryText = getViewModel().getQueryText();
		if (!StringUtils.isNullOrEmpty(queryText)) {
			filterList(queryText);
		}
	}

	private void filterList(String queryText) {
		Filterable adapter = (Filterable)recyclerView.getAdapter();

		// onQueryTextChange may still be called when is not yet initialised
		// ignore filtering until the adapter is set
		if (adapter != null) {
			adapter.getFilter().filter(queryText);
		}
	}

	private SearchView getSearchView(@NonNull Menu menu) {
		return (SearchView)menu.findItem(R.id.menu_item_list_search_base_search).getActionView();
	}

	private void setSearchViewAttrs(@NonNull Context context, SearchView searchView) {
		searchView.setQueryHint(searchView.getContext().getString(R.string.general_search));
		// focusable on navigation
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);

		// take full width
		searchView.setMaxWidth(Integer.MAX_VALUE);

		// override text colors
		EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
		editText.setTextColor(ContextCompat.getColor(context, R.color.colorToolbarText));
		editText.setHintTextColor(ContextCompat.getColor(context, R.color.colorToolbarTextHint));

		// Do not submit, because the adapter may still be not initialised
		String queryText = getViewModel().getQueryText();
		searchView.setQuery(queryText, false);
	}
}
