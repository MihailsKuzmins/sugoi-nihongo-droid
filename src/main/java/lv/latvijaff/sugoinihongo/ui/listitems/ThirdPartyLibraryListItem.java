package lv.latvijaff.sugoinihongo.ui.listitems;

import androidx.annotation.LayoutRes;
import lv.latvijaff.sugoinihongo.persistence.models.LicenceType;
import lv.latvijaff.sugoinihongo.persistence.models.ThirdPartyLibraryModel;

public class ThirdPartyLibraryListItem extends BaseListItem {

	@LayoutRes private final int licenceLayout;
	private final String name;
	private final String projectUrl;

	public ThirdPartyLibraryListItem(ThirdPartyLibraryModel model) {
		super(model.getId());

		licenceLayout = LicenceType.toLayoutId(model.getLicenceType());
		name = model.getName();
		projectUrl = model.getProjectUrl();

		String text = model.getCopyright();
		setText(text);
	}

	@LayoutRes
	public int getLicenceLayout() {
		return licenceLayout;
	}

	public String getName() {
		return name;
	}

	public String getProjectUrl() {
		return projectUrl;
	}
}
