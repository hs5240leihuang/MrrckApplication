package com.meiku.dev.yunxin;

import java.util.Map;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;

import com.meiku.dev.R;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.views.EmotionTextView;

/**
 * Created by huangjun on 2015/11/25. Tip类型消息ViewHolder
 */
public class MsgViewHolderTip extends MsgViewHolderBase {

	protected EmotionTextView notificationTextView;

	@Override
	protected int getContentResId() {
		return R.layout.nim_message_item_notification;
	}

	@Override
	protected void inflateContentView() {
		notificationTextView = (EmotionTextView) view
				.findViewById(R.id.message_item_notification_label);
	}

	@Override
	protected void bindContentView() {
		String text = "未知通知提醒";
		if (TextUtils.isEmpty(message.getContent())) {
			Map<String, Object> content = message.getRemoteExtension();
			if (content != null && !content.isEmpty()) {
				text = (String) content.get("content");
			}
		} else {
			text = message.getContent();
		}

		handleTextNotification(text);
	}

	private void handleTextNotification(String text) {
		notificationTextView.setText(EmotionHelper.getLocalEmotion(context,
				text));
		notificationTextView
				.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected boolean isMiddleItem() {
		return true;
	}
}
