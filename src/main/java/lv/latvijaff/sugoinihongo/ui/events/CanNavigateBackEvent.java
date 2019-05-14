package lv.latvijaff.sugoinihongo.ui.events;

public class CanNavigateBackEvent implements Event {

	private final boolean mCanNavigateBack;

	public CanNavigateBackEvent(boolean canNavigateBack) {
		mCanNavigateBack = canNavigateBack;
	}

	public boolean canNavigateBack() {
		return mCanNavigateBack;
	}
}
