package lv.latvijaff.sugoinihongo.di.services;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;
import lv.latvijaff.sugoinihongo.utils.IntUtils;

public class SharedPreferencesService {

	private final SharedPreferences mPreferences;
	private final RxSharedPreferences mRxPreferences;

	public SharedPreferencesService(SharedPreferences preferences) {
		mPreferences = preferences;
		mRxPreferences = RxSharedPreferences.create(preferences);
	}

	// int
	public void putInt(String key, int value) {
		mPreferences.edit().putInt(key, value).apply();
	}

	public int getInt(String key) {
		return getInt(key, IntUtils.DEFAULT_VALUE);
	}

	public int getInt(String key, int defaultValue) {
		return mPreferences.getInt(key, defaultValue);
	}

	// boolean
	public void putBoolean(String key, boolean value) {
		mPreferences.edit().putBoolean(key, value).apply();
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return mPreferences.getBoolean(key, defaultValue);
	}

	public Observable<Boolean> observeBoolean(String key) {
		return mRxPreferences.getBoolean(key).asObservable();
	}

	// long
	public void putLong(String key, long value) {
		mPreferences.edit().putLong(key, value).apply();
	}

	public long getLong(String key) {
		return getLong(key, 0);
	}

	private long getLong(String key, long defaultValue) {
		return mPreferences.getLong(key, defaultValue);
	}
}
