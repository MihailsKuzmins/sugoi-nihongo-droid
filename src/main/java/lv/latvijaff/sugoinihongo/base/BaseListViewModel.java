package lv.latvijaff.sugoinihongo.base;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.persistence.models.Model;
import timber.log.Timber;

public abstract class BaseListViewModel<TModel extends Model> extends BaseViewModel {

	private final Subject<Integer> mListSizeSubject = PublishSubject.create();
	private final Observable<Boolean> mListHasItemsObservable;

	private final boolean mRefreshList;
	private List<TModel> mList;

	protected BaseListViewModel(@NonNull App application) {
		this(application, true);
	}

	protected BaseListViewModel(@NonNull App application, boolean refreshList) {
		super(application);

		mRefreshList = refreshList;

		mListHasItemsObservable = mListSizeSubject
			.map(x -> x > 0)
			.observeOn(AndroidSchedulers.mainThread());
	}

	private void setList(List<TModel> list) {
		this.mList = list;
		mListSizeSubject.onNext(list.size());
	}

	public Observable<Boolean> observeListHasItems() {
		return mListHasItemsObservable;
	}

	public void processList(Consumer<List<TModel>> consumer) {
		if (!mRefreshList && mList != null) {
			Timber.d("List exists in teh ViewModel");
			consumer.accept(mList);
			return;
		}

		Timber.d("Getting a list from repository");
		fetchList(consumer.andThen(this::setList));
	}

	protected abstract void fetchList(Consumer<List<TModel>> consumer);
}
