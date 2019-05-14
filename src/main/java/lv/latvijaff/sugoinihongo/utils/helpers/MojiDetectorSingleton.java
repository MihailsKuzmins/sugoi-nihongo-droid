package lv.latvijaff.sugoinihongo.utils.helpers;

import com.moji4j.MojiDetector;

public final class MojiDetectorSingleton {

	private static MojiDetector sMojiDetector;

	private MojiDetectorSingleton() {}

	public static MojiDetector getInstance() {
		if (sMojiDetector == null) {
			sMojiDetector = new MojiDetector();
		}

		return sMojiDetector;
	}
}
