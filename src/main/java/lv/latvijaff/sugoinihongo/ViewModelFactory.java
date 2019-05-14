package lv.latvijaff.sugoinihongo;

import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

	private final static Object sLock = new Object();
	private static ViewModelFactory sInstance;
	private final App mApp;

	private ViewModelFactory(App app) {
		mApp = app;
	}

	@NonNull
	public static ViewModelFactory getInstance(@NonNull App app) {
		if (sInstance == null) {
			synchronized (sLock) {
				if (sInstance == null) {
					sInstance = new ViewModelFactory(app);
				}
			}
		}

		return sInstance;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
			//noinspection TryWithIdenticalCatches
			try {
				return modelClass.getConstructor(App.class).newInstance(mApp);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Cannot create an instance of " + modelClass, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot create an instance of " + modelClass, e);
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot create an instance of " + modelClass, e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Cannot create an instance of " + modelClass, e);
			}
		}
		return super.create(modelClass);
	}
}
