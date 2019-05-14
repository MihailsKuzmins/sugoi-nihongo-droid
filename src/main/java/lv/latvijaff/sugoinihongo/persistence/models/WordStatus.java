package lv.latvijaff.sugoinihongo.persistence.models;

import java.util.HashMap;
import java.util.Map;

public enum WordStatus {

	EXCELLENT,
	GOOD,
	AVERAGE,
	BAD,
	EXCLUDED;

	public static Map<WordStatus, Integer> initMap() {
		Map<WordStatus, Integer> grouping = new HashMap<>();
		for (WordStatus status : WordStatus.class.getEnumConstants()) {
			grouping.put(status, 0);
		}

		return grouping;
	}
}
