package lv.latvijaff.sugoinihongo.ui.detailitems;

import android.content.Context;
import android.text.InputType;

import lv.latvijaff.sugoinihongo.ui.detailitems.rules.Rule;

public class DetailItemEditTextMultiline extends DetailItemEditText {

	public DetailItemEditTextMultiline(Context context, int hint) {
		this(context, hint, false);
	}

	public DetailItemEditTextMultiline(Context context, int hint, boolean triggerValue) {
		super(context, hint, InputType.TYPE_TEXT_FLAG_MULTI_LINE & InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, triggerValue);
	}

	public final DetailItemEditTextMultiline addRuleMultilineEx(Rule<String> rule) {
		addRule(rule);
		return this;
	}
}
