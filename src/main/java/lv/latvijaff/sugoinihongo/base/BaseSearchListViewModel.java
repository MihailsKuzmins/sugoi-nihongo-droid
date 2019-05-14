package lv.latvijaff.sugoinihongo.base;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.persistence.models.Model;

public abstract class BaseSearchListViewModel<TModel extends Model> extends BaseListViewModel<TModel> {

	private String queryText;

	protected BaseSearchListViewModel(@NonNull App application) {
		super(application);
	}

	String getQueryText() {
		return queryText;
	}

	void setQueryText(String queryText) {
		this.queryText = queryText;
	}
}
