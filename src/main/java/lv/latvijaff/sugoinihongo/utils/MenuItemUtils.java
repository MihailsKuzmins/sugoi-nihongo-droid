package lv.latvijaff.sugoinihongo.utils;

import android.view.Menu;
import android.view.MenuItem;

public final class MenuItemUtils {

	public static void setEnabled(Menu menu, int id, final boolean isEnabled) {
		MenuItem menuItem = menu.findItem(id);
		setEnabled(menuItem, isEnabled);
	}

	private static void setEnabled(MenuItem menuItem, final boolean isEnabled) {
		menuItem.setEnabled(isEnabled);
		ObjectUtils.invokeIfNotNull(menuItem.getIcon(), x -> {
			int alpha = isEnabled ? 255 : 130;
			x.setAlpha(alpha);
		});
	}
}
