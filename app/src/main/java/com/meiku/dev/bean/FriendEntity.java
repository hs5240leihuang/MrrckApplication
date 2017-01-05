package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：好友表(找同行) 类名称：com.mrrck.ap.gp.entity.FriendEntity 创建人：仲崇生 创建时间：2015-12-10
 * 下午06:41:36
 * 
 * @version V1.0
 */
public class FriendEntity extends MkFriend {

	/** 用户头像(120X120) */
	private String headPicUrl;

	/** 用户头像(120X120) (客户端使用) */
	private String clientHeadPicUrl;

	/** 性别1男2女 */
	private String gender;

	/** 出生日期 */
	private String birthDate;

	/** 岗位名称 */
	private String positionName;

	/** 岗位图标 */
	private String positionImgUrl;

	/** 岗位图标(客户端使用) */
	private String clientPositionImgUrl;

	/** 真实年龄 */
	private String ageValue;
	/** 用户头像缩略图 */
    private String clientThumbHeadPicUrl;
	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionImgUrl() {
		return positionImgUrl;
	}

	public void setPositionImgUrl(String positionImgUrl) {
		this.positionImgUrl = positionImgUrl;
	}

	public String getClientPositionImgUrl() {
		return clientPositionImgUrl;
	}

	public void setClientPositionImgUrl(String clientPositionImgUrl) {
		this.clientPositionImgUrl = clientPositionImgUrl;
	}

	public String getAgeValue() {
		return ageValue;
	}

	public void setAgeValue(String ageValue) {
		this.ageValue = ageValue;
	}

}
