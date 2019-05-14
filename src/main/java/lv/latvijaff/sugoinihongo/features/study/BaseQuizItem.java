package lv.latvijaff.sugoinihongo.features.study;

class BaseQuizItem {

	private final String mText;
	private final String mLongText;
	private boolean mIsLongTextShown = true;

	BaseQuizItem(String text, String longText) {
		mText = text;
		mLongText = longText;
	}

	final String getText() {
		return mIsLongTextShown
			? mLongText
			: mText;
	}

	final String getLongText() {
		return mLongText;
	}

	final void setLongTextShown(boolean isLongTextShown) {
		mIsLongTextShown = isLongTextShown;
	}
}
