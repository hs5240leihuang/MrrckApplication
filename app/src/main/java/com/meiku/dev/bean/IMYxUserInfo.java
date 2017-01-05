package com.meiku.dev.bean;

import com.lidroid.xutils.db.annotation.Table;
import com.meiku.dev.config.XmppConstant;

/**
 * 保存云信用户产生的用户信息
 * @author 库
 *
 */
@Table(name = XmppConstant.IM_YX_USER_INFO)
public class IMYxUserInfo {
	private int id;
	private String yxAccount;//用户云信对应账号
	private int userId;//用户系统ID
	private String nickName;
	private String userHeadImg;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserHeadImg() {
		return userHeadImg;
	}
	public void setUserHeadImg(String userHeadImg) {
		this.userHeadImg = userHeadImg;
	}
	public String getYxAccount() {
		return yxAccount;
	}
	public void setYxAccount(String yxAccount) {
		this.yxAccount = yxAccount;
	}

}
