package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 消息记录和最近一条消息以及未读消息数目
 * @author 库
 *
 */
public class IMUserAndMsg  {
	private int id;
	private String content;
	private String msgtime;
	private int  friendSubJid;
	private String friendnickname;
	private String friendheadimg;
	private int type;
	private int noticeSum;
	private int speakuserid;
	private String speaknickname;
	private int isRoom ;//是否是群聊;0=否，1=是
	private boolean isToped;
 

	public IMUserAndMsg   (DbModel model){
        super();
        this.setId(model.getInt("id"));
        this.setContent(model.getString("content"));
        this.setMsgtime(model.getString("msgtime"));
        this.setFriendSubJid(model.getInt("friendSubJid"));
        this.setFriendnickname(model.getString("friendnickname"));
        this.setFriendheadimg(model.getString("friendheadimg"));
        this.setType(model.getInt("type"));
        this.setSpeakuserid(model.getInt("speakuserid"));
        this.setIsRoom(model.getInt("isRoom")) ;
       // this.setSortno(model.getInt("sortno")) ;
        this.setSpeaknickname(model.getString("speaknickname"));
    } 

	public int getNoticeSum() {
		return noticeSum;
	}

	public void setNoticeSum(int noticeSum) {
		this.noticeSum = noticeSum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgtime() {
		return msgtime;
	}

	public void setMsgtime(String msgtime) {
		this.msgtime = msgtime;
	}

	public int getFriendSubJid() {
		return friendSubJid;
	}

	public void setFriendSubJid(int friendSubJid) {
		this.friendSubJid = friendSubJid;
	}

	public String getFriendnickname() {
		return friendnickname;
	}

	public void setFriendnickname(String friendnickname) {
		this.friendnickname = friendnickname;
	}

	public String getFriendheadimg() {
		return friendheadimg;
	}

	public void setFriendheadimg(String friendheadimg) {
		this.friendheadimg = friendheadimg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSpeakuserid() {
		return speakuserid;
	}

	public void setSpeakuserid(int speakuserid) {
		this.speakuserid = speakuserid;
	}

	public int getIsRoom() {
		return isRoom;
	}

	public void setIsRoom(int isRoom) {
		this.isRoom = isRoom;
	}

	public String getSpeaknickname() {
		return speaknickname;
	}

	public void setSpeaknickname(String speaknickname) {
		this.speaknickname = speaknickname;
	}

	public boolean isToped() {
		return isToped;
	}

	public void setToped(boolean isToped) {
		this.isToped = isToped;
	}

//	public int getSortno() {
//		return sortno;
//	}
//
//	public void setSortno(int sortno) {
//		this.sortno = sortno;
//	}

}
