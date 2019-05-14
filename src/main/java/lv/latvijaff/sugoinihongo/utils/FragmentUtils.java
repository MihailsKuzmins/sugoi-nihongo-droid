package lv.latvijaff.sugoinihongo.utils;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import lv.latvijaff.sugoinihongo.R;

public final class FragmentUtils {

	private FragmentUtils() {}

	public static void finishActivity(Fragment fragment) {
		ObjectUtils.invokeIfNotNull(fragment.getActivity(), Activity::finish);
	}

	public static void inflateDetailEditFragmentMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.menu_detail_edit_base, menu);
	}

	public static void inflateDetailReadFragmentMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.menu_detail_read_base, menu);
	}

	public static void inflateListFragmentMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.menu_list_base, menu);
	}

	public static void setSaveButtonEnabled(@NonNull Menu menu, final boolean isValid) {
		MenuItemUtils.setEnabled(menu, R.id.menu_item_detail_edit_base_save, isValid);
	}

	public static void setSearchButtonEnabled(@NonNull Menu menu, final boolean listHasItems) {
		MenuItemUtils.setEnabled(menu, R.id.menu_item_list_base_search, listHasItems);
	}

	public static void invalidateOptionsMenu(@NonNull Fragment fragment) {
		ObjectUtils.invokeIfNotNull(fragment.getActivity(), Activity::invalidateOptionsMenu);
	}
}
