package lv.latvijaff.sugoinihongo.utils;

import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.constants.AppConstants;

public final class ObservableUtils {

	private ObservableUtils() {}

	public static Disposable setCursorOnce(Observable<String> observable, EditText editText) {
		return observable
			.debounce(AppConstants.System.FOCUSABLE_DEBOUNCE_MS, TimeUnit.MILLISECONDS)
			.take(1)
			.map(String::length)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(editText::setSelection);
	}
}
