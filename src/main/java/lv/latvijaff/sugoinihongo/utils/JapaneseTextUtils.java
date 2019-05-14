package lv.latvijaff.sugoinihongo.utils;

import com.moji4j.MojiConverter;

public final class JapaneseTextUtils {

	private JapaneseTextUtils() {}

	public static String getRomanisedText(MojiConverter converter, String string1, String string2) {
		String text = StringUtils.getFirstNonEmpty(string1, string2);
		return converter.convertKanaToRomaji(text);
	}
}
