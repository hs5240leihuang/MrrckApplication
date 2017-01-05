package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：群聊表(找同行) 类名称：com.mrrck.db.entity.MkGroup 创建人：仲崇生 创建时间：2015-12-10
 * 上午10:48:33
 * 
 * @version V1.0
 */
public class MkGroup {

	/** 编号 */
	private Integer id;

	/** 用户编号(创建人) */
	private Integer userId;

	/** 群聊头像 */
	private String groupPhoto;

	/** 群聊名称 */
	private String groupName;

	/** 群聊描述 */
	private String remark;

	/** 群主介绍 */
	private String introduction;

	/** 库群编号 */
	private Integer libGroupId;

	/** 审核状态:0审核中1:审核通过2:审核未通过 */
	private Integer approveStatus;

	/** 审核不通过原因 */
	private String refuseReson;

	/** 已有成员数目 */
	private Integer memberNum;

	/** 最大群成员数 */
	private Integer maxMemberNum;

	/** 库群排序（升序） */
	private Integer sortNo;

	/** 群二维码 */
	private String qRCode;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标记 0:正常 1:删除 */
	private Integer delStatus;
	/** 是否公开 0公开1私有 */
	private Integer publicFlag;
	/** 0:正常接收 1:设置免打扰 */
	private Integer msgFlag;
	/**群主建群:0不用验证，1需要验证 */
	private Integer joinmode;
	private String addGroupFlag;
	
	public Integer getJoinmode() {
		return joinmode;
	}

	public void setJoinmode(Integer joinmode) {
		this.joinmode = joinmode;
	}

	public Integer getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(Integer publicFlag) {
		this.publicFlag = publicFlag;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getGroupPhoto() {
		return this.groupPhoto;
	}

	public void setGroupPhoto(String groupPhoto) {
		this.groupPhoto = groupPhoto;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getLibGroupId() {
		return this.libGroupId;
	}

	public void setLibGroupId(Integer libGroupId) {
		this.libGroupId = libGroupId;
	}

	public Integer getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getRefuseReson() {
		return this.refuseReson;
	}

	public void setRefuseReson(String refuseReson) {
		this.refuseReson = refuseReson;
	}

	public Integer getMemberNum() {
		return this.memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public Integer getMaxMemberNum() {
		return this.maxMemberNum;
	}

	public void setMaxMemberNum(Integer maxMemberNum) {
		this.maxMemberNum = maxMemberNum;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getqRCode() {
		return this.qRCode;
	}

	public void setqRCode(String qRCode) {
		this.qRCode = qRCode;
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

	public Integer getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(Integer msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getAddGroupFlag() {
		return addGroupFlag;
	}

	public void setAddGroupFlag(String addGroupFlag) {
		this.addGroupFlag = addGroupFlag;
	}

}
