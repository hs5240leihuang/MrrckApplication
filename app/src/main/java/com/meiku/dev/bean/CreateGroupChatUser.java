package com.meiku.dev.bean;

public class CreateGroupChatUser {
	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	private int friendId;
	private String mkNickname;
	private String mkHeadurl;
	private String sortLetters;
	private Boolean isCheck = false;
	private Boolean isAddGroup = false;

	public Boolean getIsAddGroup() {
		return isAddGroup;
	}

	public void setIsAddGroup(Boolean isAddGroup) {
		this.isAddGroup = isAddGroup;
	}

	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public CreateGroupChatUser(String sortLetters, String mkNickname) {
		this.sortLetters = sortLetters;
		this.mkNickname = mkNickname;
	}

	public CreateGroupChatUser() {
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getMkNickname() {
		return mkNickname;
	}

	public void setMkNickname(String mkNickname) {
		this.mkNickname = mkNickname;
	}

	public String getMkHeadurl() {
		return mkHeadurl;
	}

	public void setMkHeadurl(String mkHeadurl) {
		this.mkHeadurl = mkHeadurl;
	}
}
