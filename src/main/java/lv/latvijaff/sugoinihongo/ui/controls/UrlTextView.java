package lv.latvijaff.sugoinihongo.ui.controls;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class UrlTextView extends AppCompatTextView {

	public UrlTextView(Context context) {
		super(context);
	}

	public UrlTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		readAttr(context, attrs);
	}

	public UrlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		readAttr(context, attrs);
	}

	public void setUrlText(final String urlText) {
		setOnClickListener(v -> {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText.trim()));
			v.getContext().startActivity(intent);
		});
	}

	public void setUnderlinedText(String underlinedText) {
		if (underlinedText == null) {
			// SpannableString does not accept null
			underlinedText = StringUtils.EMPTY;
		}

		SpannableString spannableString = new SpannableString(underlinedText.trim());
		spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);

		setText(spannableString);
	}

	private void readAttr(final Context context, final AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.UrlTextView);

		final String urlText = values.getString(R.styleable.UrlTextView_urlText);
		setUrlText(urlText);

		final String underlinedText = values.getString(R.styleable.UrlTextView_underlinedText);
		setUnderlinedText(underlinedText);

		values.recycle();
	}
}
