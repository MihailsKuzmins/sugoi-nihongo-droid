package lv.latvijaff.sugoinihongo.ui.listitems;

public abstract class BaseListItem {

	private String id;
	private String text;

	BaseListItem(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	protected void setText(String text) {
		this.text = text;
	}
}
