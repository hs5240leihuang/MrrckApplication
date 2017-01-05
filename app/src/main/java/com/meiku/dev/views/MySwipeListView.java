package com.meiku.dev.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

public class MySwipeListView extends SwipeListView{

	public MySwipeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySwipeListView(Context context) {
		super(context);
	}

	public MySwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
