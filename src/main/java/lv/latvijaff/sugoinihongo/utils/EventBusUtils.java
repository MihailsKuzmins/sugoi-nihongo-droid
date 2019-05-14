package lv.latvijaff.sugoinihongo.utils;

import org.greenrobot.eventbus.EventBus;

import lv.latvijaff.sugoinihongo.ui.events.Event;

public final class EventBusUtils {

	private EventBusUtils() {}

	public static void post(Event event) {
		EventBus.getDefault().post(event);
	}
}
