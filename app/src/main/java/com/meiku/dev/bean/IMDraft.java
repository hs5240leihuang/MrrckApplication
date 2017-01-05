package com.meiku.dev.bean;

import com.lidroid.xutils.db.annotation.Table;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.XmppConstant;

/**
 * 聊天草稿记录表
 * 
 * @author 库
 * 
 */
@Table(name = XmppConstant.IM_DRAFT_TABLE_NAME)
public class IMDraft {
	private int id;
	private int isRoom;// 是否是群聊;0=否，1=是
	private int friendSubJid; // 好友的ID,为聊天群时是该群的ID
	private int mySubJid;// 本人的ID
	private String content;// 草稿内容

	public IMDraft() {

	}

	public IMDraft(int isRoom, int friendSubJid, String content) {
		this.isRoom = isRoom;
		this.friendSubJid = friendSubJid;
		this.mySubJid = AppContext.getInstance().getUserInfo().getId();
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsRoom() {
		return isRoom;
	}

	public void setIsRoom(int isRoom) {
		this.isRoom = isRoom;
	}

	public int getFriendSubJid() {
		return friendSubJid;
	}

	public void setFriendSubJid(int friendSubJid) {
		this.friendSubJid = friendSubJid;
	}

	public int getMySubJid() {
		return mySubJid;
	}

	public void setMySubJid(int mySubJid) {
		this.mySubJid = mySubJid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
