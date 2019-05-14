package lv.latvijaff.sugoinihongo.ui.detailitems.rules;

import com.moji4j.MojiDetector;

import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.utils.helpers.MojiDetectorSingleton;

public class KanjiOrKanaRule extends BaseRule<String> {

	private static KanjiOrKanaRule sInstance;
	private final MojiDetector mMojiDetector;

	private KanjiOrKanaRule() {
		super(R.string.validation_japanese_kanji_or_kana);

		mMojiDetector = MojiDetectorSingleton.getInstance();
	}

	public static KanjiOrKanaRule getInstance() {
		if (sInstance == null) {
			sInstance = new KanjiOrKanaRule();
		}

		return sInstance;
	}

	@Override
	public boolean validate(String value) {
		char[] chars = value.toCharArray();

		for (int i = chars.length - 1; i >= 0; i--) {
			if (!mMojiDetector.isKanji(chars[i]) && !mMojiDetector.isKana(chars[i])) {
				return false;
			}
		}

		return true;
	}
}
