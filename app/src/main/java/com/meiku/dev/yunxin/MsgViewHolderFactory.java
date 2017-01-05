package com.meiku.dev.yunxin;

import java.util.HashMap;

import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 消息项展示ViewHolder工厂类。
 */
public class MsgViewHolderFactory {

	private static HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>>();

	private static Class<? extends MsgViewHolderBase> tipMsgViewHolder;

	static {
		// built in
		register(ImageAttachment.class, MsgViewHolderPicture.class);
		register(AudioAttachment.class, MsgViewHolderAudio.class);
		register(VideoAttachment.class, MsgViewHolderVideo.class);
		register(LocationAttachment.class, MsgViewHolderLocation.class);
		register(NotificationAttachment.class, MsgViewHolderNotification.class);
		registerTipMsgViewHolder(MsgViewHolderTip.class);
		register(FileAttachment.class, MsgViewHolderFile.class);
		register(ShareAttachment.class, MsgViewHolderShare.class);
		register(CardAttachment.class, MsgViewHolderCard.class);
		register(SystemAttachment.class, MsgViewHolderSystem.class);
		register(TipAttachment.class, MsgViewHolderSysTip.class);
	}

	public static void register(Class<? extends MsgAttachment> attach,
			Class<? extends MsgViewHolderBase> viewHolder) {
		viewHolders.put(attach, viewHolder);
	}

	public static void registerTipMsgViewHolder(
			Class<? extends MsgViewHolderBase> viewHolder) {
		tipMsgViewHolder = viewHolder;
	}

	public static Class<? extends MsgViewHolderBase> getViewHolderByType(
			IMMessage message) {
		if (message.getMsgType() == MsgTypeEnum.text) {
			return MsgViewHolderText.class;
		} else if (message.getMsgType() == MsgTypeEnum.tip) {
			return tipMsgViewHolder == null ? MsgViewHolderUnknown.class
					: tipMsgViewHolder;
		} else {
			Class<? extends MsgViewHolderBase> viewHolder = null;
			if (message.getAttachment() != null) {
				Class<? extends MsgAttachment> clazz = message.getAttachment()
						.getClass();
				while (viewHolder == null && clazz != null) {
					viewHolder = viewHolders.get(clazz);
					if (viewHolder == null) {
						clazz = getSuperClass(clazz);
					}
				}
			}
			return viewHolder == null ? MsgViewHolderUnknown.class : viewHolder;
		}
	}

	public static int getViewTypeCount() {
		// plus text and unknown
		return viewHolders.size() + 2;
	}

	public static Class<? extends MsgAttachment> getSuperClass(
			Class<? extends MsgAttachment> derived) {
		Class sup = derived.getSuperclass();
		if (sup != null && MsgAttachment.class.isAssignableFrom(sup)) {
			return sup;
		} else {
			for (Class itf : derived.getInterfaces()) {
				if (MsgAttachment.class.isAssignableFrom(itf)) {
					return itf;
				}
			}
		}
		return null;
	}
}