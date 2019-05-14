package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class NotNullOrWhiteSpaceRule extends BaseRule<String> {

	private static NotNullOrWhiteSpaceRule sInstance;

	private NotNullOrWhiteSpaceRule() {
		super(R.string.validation_string_not_null_or_empty_message);
	}

	public static NotNullOrWhiteSpaceRule getInstance() {
		if (sInstance == null) {
			sInstance = new NotNullOrWhiteSpaceRule();
		}

		return sInstance;
	}

	@Override
	public boolean validate(String value) {
		return !StringUtils.isNullOrWhiteSpace(value);
	}
}
