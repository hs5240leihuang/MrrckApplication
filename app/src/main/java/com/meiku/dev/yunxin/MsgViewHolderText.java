package com.meiku.dev.yunxin;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.views.EmotionTextView;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderText extends MsgViewHolderBase {

	@Override
	protected int getContentResId() {
		return R.layout.nim_message_item_text;
	}

	@Override
	protected void inflateContentView() {
	}

	@Override
	protected void bindContentView() {
		layoutDirection();
		EmotionTextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
		bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK
				: Color.WHITE);
		bodyTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClick();
			}
		});
		bodyTextView.setText(getDisplayText());
		bodyTextView.setOnLongClickListener(longClickListener);
	}

	private void layoutDirection() {
		TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
		if (isReceivedMessage()) {
			bodyTextView
					.setBackgroundResource(R.drawable.nim_message_item_left_selector);
			bodyTextView.setPadding(
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 15),
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 8),
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 10),
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 8));
		} else {
			bodyTextView
					.setBackgroundResource(R.drawable.nim_message_item_right_selector);
			bodyTextView.setPadding(
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 10),
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 8),
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 15),
					ScreenUtil.dip2px(MrrckApplication.getInstance(), 8));
		}
	}

	@Override
	protected int leftBackground() {
		return 0;
	}

	@Override
	protected int rightBackground() {
		return 0;
	}

	protected String getDisplayText() {
		return EmotionHelper.getLocalEmotion(context, message.getContent());
	}
}
