package lv.latvijaff.sugoinihongo.di;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.utils.ContextUtils;

@Module
public class ServicesModule {

	private final Context mContext;

	public ServicesModule(Context context) {
		mContext = context;
	}

	@Provides
	@Singleton
	SharedPreferencesService provideSharedPreferencesService() {
		SharedPreferences preferences = ContextUtils.getSharedPreferences(mContext);
		return new SharedPreferencesService(preferences);
	}
}
