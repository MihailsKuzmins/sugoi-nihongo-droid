package lv.latvijaff.sugoinihongo.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

import lv.latvijaff.sugoinihongo.ui.controls.HasMultipleClickPrevention;
import lv.latvijaff.sugoinihongo.utils.RunnableUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public final class BindingAdapters {

	@BindingAdapter("android:layout_weight")
	public static void setLayoutWeight(View view, int weight) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.weight = weight;

		view.setLayoutParams(params);
	}

	@BindingAdapter("android:numericText")
	public static void setNumericText(View view, int number) {
		if (view instanceof TextView) {
			String text = Integer.toString(number);
			((TextView)view).setText(text);
		} else {
			throw new IllegalArgumentException("Cannot set Text property on " + view.getClass().getName());
		}
	}

	@BindingAdapter("android:hint")
	public static void setHint(View view, @StringRes int hint) {
		if (view instanceof TextInputLayout) {
			String stringHint = view.getContext().getString(hint);
			((TextInputLayout)view).setHint(stringHint);
		} else {
			throw new IllegalArgumentException("Cannot set Hint property on " + view.getClass().getName());
		}
	}

	@BindingAdapter("android:contentDescription")
	public static void setContentDescription(View view, @StringRes int stringRes) {
		String text = view.getContext().getString(stringRes);
		view.setContentDescription(text);
	}

	@BindingAdapter("android:onClick")
	public static void setOnClick(View view, Runnable runnable) {
		boolean supportsPrevention = view.getClass().isAnnotationPresent(HasMultipleClickPrevention.class);

		if (!supportsPrevention) {
			view.setOnClickListener(v -> runnable.run());
			return;
		}

		view.setOnClickListener(v -> RunnableUtils.runWithMultipleClickPrevention(runnable));
	}

	@BindingAdapter("android:text")
	public static void setText(View view, Integer intValue) {
		if (view instanceof EditText) {
			((EditText)view).setText(String.format(Locale.getDefault(), "%d", intValue));
		} else {
			throw new IllegalArgumentException("Cannot set Text property on " + view.getClass().getName());
		}
	}

	@InverseBindingAdapter(attribute = "android:text")
	public static Integer getText(View view) {
		if (view instanceof EditText) {
			String stringValue = ((EditText) view).getText().toString();
			return StringUtils.toInt(stringValue);
		} else {
			throw new IllegalArgumentException("Cannot set Text property on " + view.getClass().getName());
		}
	}

	@BindingAdapter("android:checked")
	public static void setChecked(Switch view, Boolean isChecked) {
		boolean blnValue = isChecked == null ? false : isChecked;
		view.setChecked(blnValue);
	}

	@InverseBindingAdapter(attribute = "android:checked")
	public static Boolean isChecked(Switch view) {
		return view.isChecked();
	}

	@BindingAdapter("android:visibility")
	public static void setVisibility(View view, boolean isVisible) {
		int visibility = isVisible ? View.VISIBLE : View.GONE;
		view.setVisibility(visibility);
	}
}
