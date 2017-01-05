package com.meiku.dev.bean;
/**
 * 粉丝列表页
 *
 */
public class BoardUserDTO {
	private String id;
	private String nickName;
	private String ageValue;
	private String positionName;
	private String gender;
	private String clientHeadPicUrl;
	private String clientThumbHeadPicUrl;
	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}
	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}
	private String introduce;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAgeValue() {
		return ageValue;
	}
	public void setAgeValue(String ageValue) {
		this.ageValue = ageValue;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}
	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	
}
