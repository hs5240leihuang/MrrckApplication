package com.meiku.dev.bean;

import java.util.List;

public class ResumeEntity extends MkResume{
	/** 简历状态中文名称 */
	private String resumeStatusName;

	/** 性别中文名称 */
	private String genderName;

	/** 美业职龄中文名称 */
	private String jobAgeValue;

	/** 岗位编号List */
	private List<String> positionIds;

	/** 专业知识编号(多个以逗号分隔) */
	private String knowledgeId;

	/** 专业知识(多个以逗号分隔) */
	private String knowledge;

	/** 分页页数 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 岗位名称 */
	private String positionName;

	/** 岗位图标 */
	private String positionImgUrl;

	/** 岗位图标(客户端使用) */
	private String clientPositionImgUrl;

	/** 最高学历中文名称 */
	private String educationName;

	/** 婚姻状况中文名称 */
	private String isMarryName;

	/** 给终端的用的resumePhoto */
	private String clientResumePhoto;

	/** 屏蔽的公司编号 */
	private Integer companyId;

	/** 真实年龄 */
	private String ageValue;

	/** 检索年龄最小值 */
	private String ageIdStart;

	/** 检索年龄最大值 */
	private String ageIdEnd;

	/** 现居城市对应中文名称 */
	private String cityName;

	/** 意向城市名称 */
	private String likeCitys;

	/** 意向城市code */
	private String likeCitysCode;

	/** 用于终端展示刷新时间 */
	private String clientRefreshDate;

	public String getClientRefreshDate() {

		return clientRefreshDate;
	}

	public void setClientRefreshDate(String clientRefreshDate) {
		this.clientRefreshDate = clientRefreshDate;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public List<String> getPositionIds() {
		return positionIds;
	}

	public void setPositionIds(List<String> positionIds) {
		this.positionIds = positionIds;
	}

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getResumeStatusName() {
		return resumeStatusName;
	}

	public void setResumeStatusName(String resumeStatusName) {
		this.resumeStatusName = resumeStatusName;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getJobAgeValue() {
		return jobAgeValue;
	}

	public void setJobAgeValue(String jobAgeValue) {
		this.jobAgeValue = jobAgeValue;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionImgUrl() {
		return this.positionImgUrl;
	}

	public void setPositionImgUrl(String positionImgUrl) {
		this.positionImgUrl = positionImgUrl;
	}

	public String getClientPositionImgUrl() {

		return this.clientPositionImgUrl;

	}

	public void setClientPositionImgUrl(String clientPositionImgUrl) {
		this.clientPositionImgUrl = clientPositionImgUrl;
	}

	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}

	public String getIsMarryName() {
		return isMarryName;
	}

	public void setIsMarryName(String isMarryName) {
		this.isMarryName = isMarryName;
	}

	public String getClientResumePhoto() {

		return clientResumePhoto;

	}

	public void setClientResumePhoto(String clientResumePhoto) {
		this.clientResumePhoto = clientResumePhoto;

	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	public String getAgeValue() {

		return this.ageValue;

	}

	public void setAgeValue(String ageValue) {
		this.ageValue = ageValue;
	}

	public String getAgeIdStart() {
		return this.ageIdStart;
	}

	public void setAgeIdStart(String ageIdStart) {
		this.ageIdStart = ageIdStart;
	}

	public String getAgeIdEnd() {
		return this.ageIdEnd;
	}

	public void setAgeIdEnd(String ageIdEnd) {
		this.ageIdEnd = ageIdEnd;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getLikeCitys() {
		return this.likeCitys;
	}

	public void setLikeCitys(String likeCitys) {
		this.likeCitys = likeCitys;
	}

	public String getLikeCitysCode() {
		return this.likeCitysCode;
	}

	public void setLikeCitysCode(String likeCitysCode) {
		this.likeCitysCode = likeCitysCode;
	}

}
