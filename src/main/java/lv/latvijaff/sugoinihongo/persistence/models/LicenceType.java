package lv.latvijaff.sugoinihongo.persistence.models;

import androidx.annotation.LayoutRes;
import lv.latvijaff.sugoinihongo.R;

public enum LicenceType {

	APACHE_2;

	@LayoutRes
	public static int toLayoutId(LicenceType licenceType) {
		switch (licenceType) {
			case APACHE_2:
				return R.layout.licence_apache_2;
				default:
					throw new IllegalArgumentException("Cannot resolve a layout id for " + licenceType);
		}
	}
}
