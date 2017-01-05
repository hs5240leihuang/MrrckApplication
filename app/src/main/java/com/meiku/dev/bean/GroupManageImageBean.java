package com.meiku.dev.bean;

public class GroupManageImageBean {
	private String nickName;
	private String clientPicUrl;
	private String userId;
	private int type;
	private String clientThumbHeadPicUrl;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientPicUrl() {
		return clientPicUrl;
	}

	public void setClientPicUrl(String clientPicUrl) {
		this.clientPicUrl = clientPicUrl;
	}
	
}
