package com.meiku.dev.yunxin;

import com.meiku.dev.R;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.views.EmotionTextView;

public class MsgViewHolderSysTip extends MsgViewHolderBase {

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
		TipAttachment attachment = (TipAttachment) message.getAttachment();
		notificationTextView.setText(EmotionHelper.getLocalEmotion(context,
				attachment.getMsg()));
	}

	@Override
	protected boolean isMiddleItem() {
		return true;
	}

	@Override
	protected boolean onItemLongClick() {
		return true;
	}

}
