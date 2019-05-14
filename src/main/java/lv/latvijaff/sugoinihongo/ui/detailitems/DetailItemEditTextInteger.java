package lv.latvijaff.sugoinihongo.ui.detailitems;

import android.content.Context;

import androidx.annotation.StringRes;

import lv.latvijaff.sugoinihongo.ui.detailitems.rules.Rule;

public class DetailItemEditTextInteger extends BaseDetailItem<Integer> {

	@StringRes private final int mHint;
	private boolean mIsFirstLoad = true;

	public DetailItemEditTextInteger(Context context, @StringRes int hint) {
		super(context);

		mHint = hint;
	}

	@StringRes
	public int getHint() {
		return mHint;
	}

	public final DetailItemEditTextInteger addRuleEx(Rule<Integer> rule) {
		addRule(rule);
		return this;
	}

	@Override
	void notifyValueChanged() {
		if (mIsFirstLoad) {
			super.notifyValueChanged();
			mIsFirstLoad = false;
		}
	}
}
