package lv.latvijaff.sugoinihongo.persistence.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class SettingModel implements Model {

	private String id;
	@DrawableRes private int image;
	@StringRes private int title;
	@StringRes private int description;
	private String descriptionString;

	public SettingModel(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@DrawableRes
	public int getImage() {
		return image;
	}

	public void setImage(@DrawableRes int image) {
		this.image = image;
	}

	@StringRes
	public int getTitle() {
		return title;
	}

	public void setTitle(@StringRes int title) {
		this.title = title;
	}

	@StringRes
	public int getDescription() {
		return description;
	}

	public void setDescription(@StringRes int description) {
		this.description = description;
	}

	public String getDescriptionString() {
		return descriptionString;
	}

	public void setDescriptionString(String descriptionString) {
		this.descriptionString = descriptionString;
	}
}
