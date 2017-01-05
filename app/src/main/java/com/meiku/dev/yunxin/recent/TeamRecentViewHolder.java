package com.meiku.dev.yunxin.recent;

import java.util.Map;

import android.text.TextUtils;

import com.meiku.dev.config.AppContext;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.yunxin.TeamDataCache;
import com.meiku.dev.yunxin.TipAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;

public class TeamRecentViewHolder extends CommonRecentViewHolder {

	@Override
	protected String getContent() {
		String content = descOfMsg();

		String fromId = recent.getFromAccount();
		if (!TextUtils.isEmpty(fromId)
				&& !fromId.equals(AppContext.getInstance().getUserInfo()
						.getLeanCloudUserName())
				&& !(recent.getAttachment() instanceof NotificationAttachment)) {
			String teamNick = "";
			Map<String, Object> ext = recent.getExtension();
			if (ext != null && ext.containsKey("groupId")) {
				int groupId = Integer.parseInt(ext.get("groupId").toString());
				Map<String, String> nickNameMaps = AppContext
						.getGroupMemberNickNameMap().get(groupId);
				if (nickNameMaps != null && nickNameMaps.containsKey(fromId)) {
					teamNick = nickNameMaps.get(fromId);
				}
			}
			if (Tool.isEmpty(teamNick)) {
				String tid = recent.getContactId();
				teamNick = getTeamUserDisplayName(tid, fromId);
				if (Tool.isEmpty(teamNick) && ext != null
						&& ext.containsKey("nickName")) {
					teamNick = ext.get("nickName").toString();
				}
			}
			if (teamNick != null
					&& !(recent.getAttachment() instanceof TipAttachment)) {
				content = teamNick + ": " + content;
			}
		}

		return content;
	}

	private String getTeamUserDisplayName(String tid, String account) {
		return TeamDataCache.getInstance().getTeamMemberDisplayName(tid,
				account);
	}

}
