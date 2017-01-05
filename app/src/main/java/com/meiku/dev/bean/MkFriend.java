package com.meiku.dev.bean;

import com.meiku.dev.utils.Tool;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：好友表(找同行) 类名称：com.mrrck.db.entity.MkFriend 创建人：仲崇生 创建时间：2015-12-10
 * 下午05:43:04
 * 
 * @version V1.0
 */
public class MkFriend {

	/** 编号 */
	private int id;

	/** 本人id */
	private int userId;

	/** 好友id */
	private int friendId;

	/** 好友备注名称 */
	private String aliasName;

	/** 好友备注名称拼音首字母 */
	private String nameFirstChar;

	/** 好友id */
	private int hideFlag;

	/** 好友id */
	private int friendFlag;

	/** 创建时间(成为好友时间) */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 好友状态 0:正常 1:解除 */
	private int delStatus;

	/** 个性签名 */
	private String introduce;
	/** 第三方聊天接口用户编号 */
	private String leanCloudId;

	/** leanCloud用户名 */
	private String leanCloudUserName;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFriendId() {
		return this.friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public String getAliasName() {
		return this.aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getNameFirstChar() {
		if (Tool.isEmpty(nameFirstChar)) {
			return "#";
		}
		return this.nameFirstChar;
	}

	public void setNameFirstChar(String nameFirstChar) {
		this.nameFirstChar = nameFirstChar;
	}

	public int getHideFlag() {
		return this.hideFlag;
	}

	public void setHideFlag(int hideFlag) {
		this.hideFlag = hideFlag;
	}

	public int getFriendFlag() {
		return this.friendFlag;
	}

	public void setFriendFlag(int friendFlag) {
		this.friendFlag = friendFlag;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getLeanCloudId() {
		return leanCloudId;
	}

	public void setLeanCloudId(String leanCloudId) {
		this.leanCloudId = leanCloudId;
	}

	public String getLeanCloudUserName() {
		return leanCloudUserName;
	}

	public void setLeanCloudUserName(String leanCloudUserName) {
		this.leanCloudUserName = leanCloudUserName;
	}

}
