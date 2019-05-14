package lv.latvijaff.sugoinihongo.features.word;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseSearchListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class WordSearchListViewModel extends BaseSearchListViewModel<WordModel> {

	private ListenerRegistration mListSubscription;

	@Inject
	WordRepo repo;

	public WordSearchListViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);
	}

	@Override
	protected void onCleared() {
		ObjectUtils.invokeIfNotNull(mListSubscription, ListenerRegistration::remove);
		super.onCleared();
	}

	@Override
	protected void fetchList(Consumer<List<WordModel>> consumer) {
		mListSubscription = repo.processCollection(consumer, false);
	}
}
