package lv.latvijaff.sugoinihongo.features.grammarrule;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;

public class GrammarRuleSearchListAdapter extends GrammarRuleListAdapter implements Filterable {

	private final Filter filter;

	GrammarRuleSearchListAdapter(List<GrammarRuleModel> list) {
		super(list);

		filter = createFilter((model, searchText) -> model
			.getHeader().toLowerCase().contains(searchText));
	}

	@Override
	public Filter getFilter() {
		return filter;
	}
}
