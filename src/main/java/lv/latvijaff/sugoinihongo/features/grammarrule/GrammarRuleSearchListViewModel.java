package lv.latvijaff.sugoinihongo.features.grammarrule;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseSearchListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.persistence.repos.GrammarRuleRepo;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class GrammarRuleSearchListViewModel extends BaseSearchListViewModel<GrammarRuleModel> {

	private ListenerRegistration mListSubscription;

	@Inject
	GrammarRuleRepo repo;

	public GrammarRuleSearchListViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);
	}

	@Override
	protected void onCleared() {
		ObjectUtils.invokeIfNotNull(mListSubscription, ListenerRegistration::remove);
		super.onCleared();
	}

	@Override
	protected void fetchList(Consumer<List<GrammarRuleModel>> consumer) {
		mListSubscription = repo.processCollection(false, consumer);
	}
}
