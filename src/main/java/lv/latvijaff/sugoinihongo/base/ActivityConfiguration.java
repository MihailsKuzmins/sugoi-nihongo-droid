package lv.latvijaff.sugoinihongo.base;

public class ActivityConfiguration {

	private boolean backButtonDisplayed = true;
	private boolean mainMenuDisplayed = true;

	boolean isBackButtonDisplayed() {
		return backButtonDisplayed;
	}

	public void setBackButtonDisplayed(boolean backButtonDisplayed) {
		this.backButtonDisplayed = backButtonDisplayed;
	}

	boolean isMainMenuDisplayed() {
		return mainMenuDisplayed;
	}

	public void setMainMenuDisplayed(boolean mainMenuDisplayed) {
		this.mainMenuDisplayed = mainMenuDisplayed;
	}
}
