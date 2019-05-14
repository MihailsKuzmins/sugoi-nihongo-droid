package lv.latvijaff.sugoinihongo.features.word;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseListViewModel;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;
import lv.latvijaff.sugoinihongo.utils.helpers.WordAndSecondaryPropsListenerRegistration;

public class WordFavouritesListViewModel extends BaseListViewModel<WordModel> {

	private WordAndSecondaryPropsListenerRegistration mListSubscription;

	@Inject
	WordRepo repo;

	public WordFavouritesListViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);
	}

	@Override
	protected void onCleared() {
		ObjectUtils.invokeIfNotNull(mListSubscription, WordAndSecondaryPropsListenerRegistration::remove);
		super.onCleared();
	}

	@Override
	protected void fetchList(Consumer<List<WordModel>> consumer) {
		mListSubscription = repo.processFavourites(consumer);
	}
}
