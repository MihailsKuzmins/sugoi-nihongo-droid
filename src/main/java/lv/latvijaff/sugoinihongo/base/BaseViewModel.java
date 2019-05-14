package lv.latvijaff.sugoinihongo.base;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.disposables.CompositeDisposable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.di.AppComponent;
import lv.latvijaff.sugoinihongo.di.AppRepoComponent;
import lv.latvijaff.sugoinihongo.di.RepoComponent;

public abstract class BaseViewModel extends AndroidViewModel implements Observable {

	private transient PropertyChangeRegistry mCallbacks;

	public BaseViewModel(@NonNull App application) {
		super(application);
	}

	protected final AppComponent getAppComponent() {
		return this.<App>getApplication().getAppComponent();
	}

	protected final RepoComponent getRepoComponent() {
		return this.<App>getApplication().getRepoComponent();
	}

	protected final AppRepoComponent getAppRepoComponent() {
		return this.<App>getApplication().getAppRepoComponent();
	}

	public void setupSubscriptions(CompositeDisposable cleanUp) {}

	@Override
	public final void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
		synchronized (this) {
			if (mCallbacks == null) {
				mCallbacks = new PropertyChangeRegistry();
			}
		}
		mCallbacks.add(callback);
	}

	@Override
	public final void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
		synchronized (this) {
			if (mCallbacks == null) {
				return;
			}
		}
		mCallbacks.remove(callback);
	}

	public final void notifyPropertyChanged(int fieldId) {
		synchronized (this) {
			if (mCallbacks == null) {
				return;
			}
		}
		mCallbacks.notifyCallbacks(this, fieldId, null);
	}
}
