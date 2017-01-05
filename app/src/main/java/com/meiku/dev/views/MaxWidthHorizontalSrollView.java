package com.meiku.dev.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;

public class MaxWidthHorizontalSrollView extends HorizontalScrollView {

	public MaxWidthHorizontalSrollView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MaxWidthHorizontalSrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MaxWidthHorizontalSrollView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int maxWidth = ScreenUtil.SCREEN_WIDTH * 3 / 4;
		setMeasuredDimension(getMeasuredWidth() > maxWidth ? maxWidth
				: getMeasuredWidth(), getMeasuredHeight());
	}

}
