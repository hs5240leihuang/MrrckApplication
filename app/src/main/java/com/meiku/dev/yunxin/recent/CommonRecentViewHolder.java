package com.meiku.dev.yunxin.recent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.meiku.dev.yunxin.CardAttachment;
import com.meiku.dev.yunxin.ShareAttachment;
import com.meiku.dev.yunxin.SystemAttachment;
import com.meiku.dev.yunxin.TeamNotificationHelper;
import com.meiku.dev.yunxin.TipAttachment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class CommonRecentViewHolder extends RecentViewHolder {

	@Override
	protected String getContent() {
		return descOfMsg();
	}

	protected String descOfMsg() {
		if (recent.getMsgType() == MsgTypeEnum.text) {
			return recent.getContent();
		} else if (recent.getMsgType() == MsgTypeEnum.tip) {
			String digest = null;
			// if (getCallback() != null) {
			// digest = getCallback().getDigestOfTipMsg(recent);
			// }
			//
			// if (digest == null) {
			digest = getDefaultDigest(null);
			// }

			return digest;
		} else if (recent.getAttachment() != null) {
			String digest = null;
			// if (getCallback() != null) {
			// digest =
			// getCallback().getDigestOfAttachment(recent.getAttachment());
			// }
			//
			// if (digest == null) {
			digest = getDefaultDigest(recent.getAttachment());
			// }

			return digest;
		}
		return "";
	}

	// SDK本身只记录原始数据，第三方APP可根据自己实际需求，在最近联系人列表上显示缩略消息
	// 以下为一些常见消息类型的示例。
	private String getDefaultDigest(MsgAttachment attachment) {
		switch (recent.getMsgType()) {
		case text:
			return recent.getContent();
		case image:
			return "[图片]";
		case video:
			return "[视频]";
		case audio:
			return "[语音消息]";
		case location:
			return "[位置]";
		case file:
			return "[文件]";
		case tip:
			// List<String> uuids = new ArrayList<String>();
			// uuids.add(recent.getRecentMessageId());
			// List<IMMessage> messages =
			// NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
			// if (messages != null && messages.size() > 0) {
			// return messages.get(0).getContent();
			// }
			return "[通知提醒]";
		case notification:
			// return
			// TeamNotificationHelper.getTeamNotificationText(recent.getContactId(),
			// recent.getFromAccount(),
			// (NotificationAttachment) recent.getAttachment());
			Map<String, Object> recentExt = recent.getExtension();
			if (recentExt != null && recentExt.containsKey("lastMsgStr")) {
				return recentExt.get("lastMsgStr").toString();
			} else {
				return "";
			}
		default:
			if (attachment instanceof ShareAttachment) {
				return "[分享]";
			}
			if (attachment instanceof CardAttachment) {
				return "[名片]";
			}
			if (attachment instanceof SystemAttachment) {
				return ((SystemAttachment) attachment).getMsg();
			}
			if (attachment instanceof TipAttachment) {
				return ((TipAttachment) attachment).getMsg();
			}
			return "[自定义消息]";
		}
	}
}
