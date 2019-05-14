package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.utils.helpers.MojiDetectorSingleton;

public class KanaRule extends BaseRule<String> {

	private static KanaRule sInstance;

	private KanaRule() {
		super(R.string.validation_japanese_kana);
	}

	public static KanaRule getInstance() {
		if (sInstance == null) {
			sInstance = new KanaRule();
		}

		return sInstance;
	}

	@Override
	public boolean validate(String value) {
		char[] chars = value.toCharArray();

		for (int i = chars.length - 1; i >= 0; i--) {
			if (!MojiDetectorSingleton.getInstance().isKana(chars[i])) {
				return false;
			}
		}

		return true;
	}
}
