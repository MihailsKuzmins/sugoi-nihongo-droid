package lv.latvijaff.sugoinihongo.features.sentence;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseSearchListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.persistence.repos.SentenceRepo;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class SentenceSearchListViewModel extends BaseSearchListViewModel<SentenceModel> {

	private ListenerRegistration mListSubscription;

	@Inject
	SentenceRepo repo;

	public SentenceSearchListViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);
	}

	@Override
	protected void onCleared() {
		ObjectUtils.invokeIfNotNull(mListSubscription, ListenerRegistration::remove);
		super.onCleared();
	}

	@Override
	protected void fetchList(Consumer<List<SentenceModel>> consumer) {
		mListSubscription = repo.processCollection(consumer, false);
	}
}
