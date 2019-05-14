package lv.latvijaff.sugoinihongo.ui.detailitems;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.PropertyChangeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import kotlin.Unit;
import lv.latvijaff.sugoinihongo.BR;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.Rule;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public abstract class BaseDetailItem<T> implements androidx.databinding.Observable {

	private transient PropertyChangeRegistry mCallbacks;

	private T mValue;
	private T mDefaultValue;
	private boolean mIsVisible;

	private boolean mIsFirstLoad = true;

	private final ReplaySubject<T> mValueSubject = ReplaySubject.create();
	private final Observable<String> mFirstValidationMessageObservable;
	private final Observable<T> mValueObservable;
	private final Observable<T> mValidValueObservable;
	private final Observable<Boolean> mIsValidObservable;

	private final List<Rule<T>> mRules = new ArrayList<>();
	private final Subject<Unit> mValidationTriggerSubject = PublishSubject.create();

	BaseDetailItem(final Context context) {
		this(context, null);
	}

	BaseDetailItem(final Context context, final @Nullable T startWithValue) {
		mValueObservable = mValueSubject
			.observeOn(Schedulers.computation())
			.share();

		Observable<T> startWithValueObservable = startWithValue == null
			? mValueSubject
			: mValueSubject.startWith(startWithValue);

		Observable<Pair<String, T>> valueObservable  = Observable
			.combineLatest(
				mValidationTriggerSubject.startWith(Unit.INSTANCE), startWithValueObservable,
				(x, y) -> y)
			.map(x -> {
				String msg = mRules.stream()
					.filter(rule -> !rule.validate(x))
					.map(rule -> rule.getErrorMessage(context))
					.findFirst()
					.orElse(StringUtils.EMPTY);

				return new Pair<>(msg, x);
			});

		mFirstValidationMessageObservable = valueObservable
			.map(x -> x.first)
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread());

		mValidValueObservable = valueObservable
			.filter(x -> StringUtils.isNullOrEmpty(x.first))
			.map(x -> x.second)
			.debounce(AppConstants.System.DEFAULT_DEBOUNCE_MS, TimeUnit.MILLISECONDS);

		mIsValidObservable = valueObservable
			.map(x -> StringUtils.isNullOrEmpty(x.first))
			.distinctUntilChanged()
			.observeOn(AndroidSchedulers.mainThread());
	}

	@Bindable
	public final T getValue() {
		return mValue;
	}

	public final void setValue(T value) {
		if (canSetDefaultValue(value, mIsFirstLoad)) {
			mDefaultValue = value;
			mIsFirstLoad = false;
		}

		if (mValue == value) {
			return;
		}

		mValue = value;
		onNextValue(value);
		notifyValueChanged();
	}

	public T getDefaultValue() {
		return mDefaultValue;
	}

	@Bindable
	public final boolean isVisible() {
		return mIsVisible;
	}

	public final void setVisible(boolean visible) {
		if (mIsVisible != visible) {
			mIsVisible = visible;
			notifyPropertyChanged(BR.visible);
		}
	}

	public boolean isValueChanged() {
		return mValue != mDefaultValue;
	}

	public Disposable triggerValidation(Observable<?> observable) {
		return observable
			.map(x -> Unit.INSTANCE)
			.subscribe(mValidationTriggerSubject::onNext);
	}

	public final Observable<String> observeFirstErrorMessage() {
		return mFirstValidationMessageObservable;
	}

	public final Observable<T> observeValue() {
		return mValueObservable;
	}

	public final Observable<T> observeValidValue() {
		return mValidValueObservable;
	}

	public final Observable<Boolean> observeIsValid() {
		return mIsValidObservable;
	}

	final void addRule(Rule<T> rule) {
		mRules.add(rule);
	}

	boolean canSetDefaultValue(final T value, final boolean isFirstLoad) {
		return isFirstLoad;
	}

	void onNextValue(T value) {
		mValueSubject.onNext(value);
	}

	void notifyValueChanged() {
		notifyPropertyChanged(BR.value);
	}

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

	private void notifyPropertyChanged(int fieldId) {
		synchronized (this) {
			if (mCallbacks == null) {
				return;
			}
		}
		mCallbacks.notifyCallbacks(this, fieldId, null);
	}
}
