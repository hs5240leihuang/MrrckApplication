package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述： 类名称：com.mrrck.db.entity.MkMrrckBaike 创建人：仲崇生 创建时间：Fri Jun 03 20:05:47
 * CST 2016
 * 
 * @version V1.0
 */
public class MkMrrckBaike {

	/** '编号 */
	private Integer id;

	/** 用户编号(百科内容发布者编号) */
	private Integer userId;

	/** 一级词条编号(1.个人词条、2.公司词条、3.公共词条) */
	private Integer categoryId;

	/** 个人词条(词条名) \r\n公司词条(词条名) \r\n公共词条(词条名) */
	private String name;

	/** 概述 */
	private String summary;

	/** 概述图:个人词条(头像) \r\n企业词条(企业LOGO) 公共词条(概述图) */
	private String photo;

	/** 省份编号(所在地) */
	private Integer provinceCode;

	/** 城市code(所在地) */
	private Integer cityCode;

	/** 中文名(基本信息栏)(个人) */
	private String chnName;

	/** 外文名(基本信息栏)(个人) */
	private String enName;

	/** 别名(基本信息栏)(个人) */
	private String aliasName;

	/** 国籍(基本信息栏)(个人) */
	private String country;

	/** 民族(基本信息栏)(个人) */
	private String national;

	/** 出生地(基本信息栏)(个人) */
	private String birthPlace;

	/** 出生日期(基本信息栏)(个人) */
	private String birthDate;

	/** 职业(基本信息栏)(个人) */
	private String positionName;

	/** 毕业院校(基本信息栏)(个人) */
	private String graduateSchool;

	/** 信仰(基本信息栏)(个人) */
	private String belief;

	/** 主要成就,多个%%分隔(基本信息栏)(个人) */
	private String achievement;

	/** 代表作品(基本信息栏)(个人) */
	private String majorWorks;

	/** 描述(个人:个人简介 企业:企业简介 公共:名词解释) */
	private String remark;

	/** 兴趣爱好(个人词条) */
	private String hobby;

	/** 个人说明(个人词条)\r\n企业优势说明(企业词条)\r\n详细说明(公共词条) */
	private String detailRemark;

	/** 企业名称(基本信息栏)(企业) */
	private String companyName;

	/** 成立时间(基本信息栏)(企业) */
	private String registTime;

	/** 公司位置(基本信息栏)(企业) */
	private String address;

	/** 注册资本(基本信息栏)(企业) */
	private String registCapital;

	/** 法定代表人(基本信息栏)(企业) */
	private String legalPerson;

	/** 企业类型(基本信息栏)(企业) */
	private String enterpriseType;

	/** 组织机构代码(基本信息栏)(企业) */
	private String organizationCode;

	/** 经营范围(企业词条使用) */
	private String scopeBusiness;

	/** 联系人(基本信息栏)(企业) */
	private String contactName;

	/** 联系电话(基本信息栏)(企业) */
	private String contactPhone;

	/** 附件数量(个人,企业,公共) */
	private Integer attachmentNum;

	/** 刷新时间 */
	private String refreshDate;

	/** 置顶 : 0不置顶 1置顶 */
	private Integer topFlag;

	/** 推荐标识: 0普通 1精华 */
	private Integer goodFlag;

	/** 审核状态:0待审核1:审核通过2:审核未通过 */
	private Integer approveStatus;

	/** 审核时间 */
	private String approveDate;

	/** 审核未通过理由 */
	private String refuseReason;

	/** 审核人编号 */
	private Integer approveUserId;

	/** 数据来源地址 */
	private String resourceAddress;

	/** 浏览数 */
	private Integer viewNum;

	/** 收藏数 */
	private Integer collectNum;

	/** 点赞数 */
	private Integer likeNum;

	/** 更新次数 */
	private Integer updateNum;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0:正常,1:删除 */
	private Integer delStatus;
	/** 图片缩略图 */
	private String mainPhotoThumb;

	/**分享的URL*/
	private String shareUrl;
	/**分享的标题*/
	private String shareTitle;
	/**分享的内容*/
	private String shareContent;
	/**载入的url地址*/
	private String loadUrl;
	/**是否存在企业标记  0 存在  1 不存在*/
	private String isExsitCompany;
	
	public String getMainPhotoThumb() {
		return mainPhotoThumb;
	}

	public void setMainPhotoThumb(String mainPhotoThumb) {
		this.mainPhotoThumb = mainPhotoThumb;
	}

	public String getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return summary;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhoto() {
		return photo;
	}

	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getProvinceCode() {
		return provinceCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}

	public String getChnName() {
		return chnName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEnName() {
		return enName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getNational() {
		return national;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setBelief(String belief) {
		this.belief = belief;
	}

	public String getBelief() {
		return belief;
	}

	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	public String getAchievement() {
		return achievement;
	}

	public void setMajorWorks(String majorWorks) {
		this.majorWorks = majorWorks;
	}

	public String getMajorWorks() {
		return majorWorks;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getHobby() {
		return hobby;
	}

	public void setDetailRemark(String detailRemark) {
		this.detailRemark = detailRemark;
	}

	public String getDetailRemark() {
		return detailRemark;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setRegistCapital(String registCapital) {
		this.registCapital = registCapital;
	}

	public String getRegistCapital() {
		return registCapital;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setScopeBusiness(String scopeBusiness) {
		this.scopeBusiness = scopeBusiness;
	}

	public String getScopeBusiness() {
		return scopeBusiness;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public Integer getAttachmentNum() {
		return attachmentNum;
	}

	public void setRefreshDate(String refreshDate) {
		this.refreshDate = refreshDate;
	}

	public String getRefreshDate() {
		return refreshDate;
	}

	public void setTopFlag(Integer topFlag) {
		this.topFlag = topFlag;
	}

	public Integer getTopFlag() {
		return topFlag;
	}

	public void setGoodFlag(Integer goodFlag) {
		this.goodFlag = goodFlag;
	}

	public Integer getGoodFlag() {
		return goodFlag;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setApproveUserId(Integer approveUserId) {
		this.approveUserId = approveUserId;
	}

	public Integer getApproveUserId() {
		return approveUserId;
	}

	public void setResourceAddress(String resourceAddress) {
		this.resourceAddress = resourceAddress;
	}

	public String getResourceAddress() {
		return resourceAddress;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getViewNum() {
		return viewNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setUpdateNum(Integer updateNum) {
		this.updateNum = updateNum;
	}

	public Integer getUpdateNum() {
		return updateNum;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	public String getIsExsitCompany() {
		return isExsitCompany;
	}

	public void setIsExsitCompany(String isExsitCompany) {
		this.isExsitCompany = isExsitCompany;
	}

}
