package com.meiku.dev.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meiku.dev.R;
import com.meiku.dev.utils.ScreenUtil;

/**
 * 点阵
 * 
 * @author hl
 * 
 */
public class IndicatorView extends LinearLayout {

	private int pointcount;

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IndicatorView(Context context) {
		super(context);
	}

	public void setPointCount(Context context, int pointcount) {
		this.pointcount = pointcount;
		setGravity(Gravity.CENTER);
		removeAllViews();
		for (int i = 0; i < pointcount; i++) {
			ImageView img = new ImageView(context);
			LayoutParams params = new LayoutParams(ScreenUtil.dpToPx(
					getResources(), 5), ScreenUtil.dpToPx(getResources(), 5));
			params.leftMargin = ScreenUtil.dpToPx(getResources(), 10);
			params.rightMargin = ScreenUtil.dpToPx(getResources(), 10);
			img.setLayoutParams(params);
			img.setBackgroundResource(R.drawable.lou_white);
			addView(img);
		}
		setSelectedPosition(0);
	}

	public void setSelectedPosition(int currentIndex) {
		for (int j = 0; j < pointcount; j++) {
			if (j == currentIndex) {
				getChildAt(j).setBackgroundResource(R.drawable.whiteround);
				LayoutParams params = new LayoutParams(ScreenUtil.dpToPx(
						getResources(), 15), ScreenUtil.dpToPx(getResources(),
						5));
				params.leftMargin = ScreenUtil.dpToPx(getResources(), 5);
				params.rightMargin = ScreenUtil.dpToPx(getResources(), 5);
				getChildAt(j).setLayoutParams(params);
			} else {
				getChildAt(j).setBackgroundResource(R.drawable.lou_white);
				LayoutParams params = new LayoutParams(ScreenUtil.dpToPx(
						getResources(), 5),
						ScreenUtil.dpToPx(getResources(), 5));
				params.leftMargin = ScreenUtil.dpToPx(getResources(), 10);
				params.rightMargin = ScreenUtil.dpToPx(getResources(), 10);
				getChildAt(j).setLayoutParams(params);
			}
		}

	}

}