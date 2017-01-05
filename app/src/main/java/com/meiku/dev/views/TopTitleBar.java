package com.meiku.dev.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meiku.dev.R;

public class TopTitleBar extends RelativeLayout {
	private LayoutInflater inflater;
	private TextView centertxt;
	private TextView rightTxt;
	private View view;

	public TopTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public TopTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TopTitleBar(Context context) {
		super(context);
		init(context, null);
	}

	public void setTitle(String title) {
		if (null != centertxt) {
			centertxt.setText(title);
		}
	}

	public void setRightText(String text) {
		rightTxt = (TextView) findViewById(R.id.right_txt_title);
		rightTxt.setText(text);
		rightTxt.setPadding(10, 8, 10, 8);
//		rightTxt.setBackgroundResource(R.drawable.radius_border_white_solid);
	}

	/**
	 * 设置背景
	 */
	public void setTitleBg(Drawable drawable) {
		view.setBackgroundDrawable(drawable);
	}

	private void init(final Context context, AttributeSet attrs) {
		inflater = LayoutInflater.from(context);
		if (null != attrs) {
			view = inflater.inflate(R.layout.top_titlebar, this, true);

			ImageView rightimg = (ImageView) view
					.findViewById(R.id.right_res_title);

			final ImageView leftimg = (ImageView) view
					.findViewById(R.id.left_res_title);
			findViewById(R.id.goback).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (leftimg.getDrawable() != null) {
						((Activity) context).finish();
					}
				}
			});
			rightTxt = (TextView) findViewById(R.id.right_txt_title);
			centertxt = (TextView) findViewById(R.id.center_txt_title);
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.TopTitle);

			TextView linear_txt = (TextView) findViewById(R.id.linear_txt_title);
			ImageView linear_res = (ImageView) findViewById(R.id.linear_res_title);

			int indexCount = typedArray.getIndexCount();
			for (int k = 0; k < indexCount; k++) {
				int attr = typedArray.getIndex(k);
				switch (attr) {
				case R.styleable.TopTitle_center_txt_title:
					String titleCenter = typedArray.getString(attr);
					if (!TextUtils.isEmpty(titleCenter)) {
						centertxt.setText(titleCenter);
					}
					break;
				case R.styleable.TopTitle_left_res_title:
					Drawable drawLeft = typedArray.getDrawable(attr);
					if (null != drawLeft) {
						leftimg.setImageBitmap(((BitmapDrawable) drawLeft)
								.getBitmap());
					}
					break;
				case R.styleable.TopTitle_right_res_title:
					Drawable drawRight = typedArray.getDrawable(attr);
					if (null != drawRight) {
						rightimg.setImageBitmap(((BitmapDrawable) drawRight)
								.getBitmap());
					}
					break;
				case R.styleable.TopTitle_center_txt_title_color:
					ColorStateList colorStateList = typedArray
							.getColorStateList(attr);
					if (colorStateList != null) {
						centertxt.setTextColor(colorStateList);
					}

					break;
				case R.styleable.TopTitle_right_txt_title:
					String titleRight = typedArray.getString(attr);
					if (!TextUtils.isEmpty(titleRight)) {
						rightTxt.setText(titleRight);
						rightTxt.setPadding(10, 8, 10, 8);
					}
					break;
				case R.styleable.TopTitle_linear_txt_title:
					String titleLinear = typedArray.getString(attr);
					if (!TextUtils.isEmpty(titleLinear)) {
						linear_txt.setText(titleLinear);
					}
					break;
				case R.styleable.TopTitle_linear_res_title:
					Drawable drawLinear = typedArray.getDrawable(attr);
					if (null != drawLinear) {
						linear_res.setImageBitmap(((BitmapDrawable) drawLinear)
								.getBitmap());
					}
					break;
				case R.styleable.TopTitle_right_txt_title_color:
					colorStateList = typedArray.getColorStateList(attr);
					if (colorStateList != null) {
						rightTxt.setTextColor(colorStateList);
					}

					break;
				}
			}
		}
	}
}
