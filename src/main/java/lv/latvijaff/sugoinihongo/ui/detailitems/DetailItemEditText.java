package lv.latvijaff.sugoinihongo.ui.detailitems;

import android.content.Context;
import android.text.InputType;

import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.ui.detailitems.rules.Rule;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class DetailItemEditText extends BaseDetailItem<String> {

	@StringRes private final int mHint;
	private final int mInputType;

	private boolean mIsFirstLoad = true;

	public DetailItemEditText(Context context, @StringRes int hint) {
		this(context, hint, InputType.TYPE_CLASS_TEXT);
	}

	public DetailItemEditText(Context context, @StringRes int hint, int inputType) {
		this(context, hint, inputType, false);
	}

	public DetailItemEditText(Context context, @StringRes int hint, boolean triggerValue) {
		this(context, hint, InputType.TYPE_CLASS_TEXT, triggerValue);
	}

	public DetailItemEditText(Context context, @StringRes int hint, int inputType, boolean triggerValue) {
		super(context, triggerValue ? StringUtils.EMPTY : null);

		mHint = hint;
		mInputType = inputType;
	}

	@StringRes
	public int getHint() {
		return mHint;
	}

	public int getInputType() {
		return mInputType;
	}

	public final DetailItemEditText addRuleEx(Rule<String> rule) {
		addRule(rule);
		return this;
	}

	@Override
	void onNextValue(String value) {
		if (value == null) {
			value = StringUtils.EMPTY;
		}

		super.onNextValue(value);
	}

	@Override
	void notifyValueChanged() {
		if (mIsFirstLoad) {
			super.notifyValueChanged();
			mIsFirstLoad = false;
		}
	}
}
