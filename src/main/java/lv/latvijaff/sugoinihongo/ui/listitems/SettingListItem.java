package lv.latvijaff.sugoinihongo.ui.listitems;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import lv.latvijaff.sugoinihongo.persistence.models.SettingModel;

public class SettingListItem extends BaseListItem {

	@DrawableRes private int image;
	@StringRes private int titleResource;
	@StringRes private int descriptionResource;
	private String descriptionParam;

	private String title;
	private String description;

	public SettingListItem(SettingModel model) {
		super(model.getId());

		image = model.getImage();
		titleResource = model.getTitle();
		descriptionResource = model.getDescription();
		descriptionParam = model.getDescriptionString();
	}

	@DrawableRes
	public int getImage() {
		return image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(Context context) {
		this.title = context.getString(titleResource);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(Context context) {
		this.description = context.getString(descriptionResource, descriptionParam);
	}
}
