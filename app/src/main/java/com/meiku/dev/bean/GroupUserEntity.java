package com.meiku.dev.bean;

public class GroupUserEntity {
	private int approveStatus;// 0:审核中 1:通过 2:拒绝',

	private String createDate;

	private int delStatus;

	private int groupId;

	private int id;

	private String nickName;

	private String updateDate;

	private int userId;

	private String userIds;

	private String clientHeadPicUrl;
	private String clientThumbHeadPicUrl;
	/** leanCloud用户名 */
	private String leanCloudUserName;
	/** 三方群唯一id */
	private String otherId;
	/** 群名称 */
	private String groupName;
	private Integer gender;
	private String positionName;
	private String ageValue;

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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String remark;

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}

	public int getApproveStatus() {
		return this.approveStatus;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public int getDelStatus() {
		return this.delStatus;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return this.updateDate;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserIds() {
		return this.userIds;
	}

	public String getLeanCloudUserName() {
		return leanCloudUserName;
	}

	public void setLeanCloudUserName(String leanCloudUserName) {
		this.leanCloudUserName = leanCloudUserName;
	}

}
