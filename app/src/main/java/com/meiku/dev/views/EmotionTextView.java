package com.meiku.dev.views;

import com.meiku.dev.utils.EmotionHelper;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class EmotionTextView extends TextView {

	public EmotionTextView(Context context) {
		super(context);
	}

	public EmotionTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmotionTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!TextUtils.isEmpty(text)) {
			super.setText(EmotionHelper.replace(getContext(), text.toString()),
					type);
		} else {
			super.setText(text, type);
		}
	}

}
