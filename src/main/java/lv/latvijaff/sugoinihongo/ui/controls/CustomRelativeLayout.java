package lv.latvijaff.sugoinihongo.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

@HasMultipleClickPrevention
public class CustomRelativeLayout extends RelativeLayout {

	public CustomRelativeLayout(Context context) {
		super(context);
	}

	public CustomRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
