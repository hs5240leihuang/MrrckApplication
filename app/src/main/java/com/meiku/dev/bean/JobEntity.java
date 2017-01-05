package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：职位实体类 类名称：com.mrrck.ap.ht.entity.JobEntity 创建人：刘茂林 创建时间：2015-5-30
 * 下午02:22:42
 * 
 * @version V1.0
 */
public class JobEntity extends MkJob {

	/** 老板类型 */
	private String bossType;

	/** 分页数量 */
	private Integer pageNum;

	/** 分页偏移量 */
	private Integer offset;

	/** 最小纬度 */
	private String minLatitude;

	/** 最大纬度 */
	private String maxLatitude;

	/** 最小经度 */
	private String minLongitude;

	/** 最大经度 */
	private String maxLongitude;
	private String bossTypeName;// 老板类型
	private String companyName;// 公司名称
	private String jobAgeName;// 美业职龄
	private String educationName;// 学历
	private List<JobEntity> jobList;// 招聘职位
	private String address;
	private Integer resumeId;// 简历id
	/** 福利待遇编号(多个逗号分隔) */
	private List<JobEntity> fringeBenefitsIdList;

	private String benefitsId;

	/** 客户端展示的时间 */
	private String clientRefreshDate;
	/** 公司logo */
	private String clientThumbCompanyLogo;
	private String collectFlag;

	public String getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(String collectFlag) {
		this.collectFlag = collectFlag;
	}

	public String getClientThumbCompanyLogo() {
		return clientThumbCompanyLogo;
	}

	public void setClientThumbCompanyLogo(String clientThumbCompanyLogo) {
		this.clientThumbCompanyLogo = clientThumbCompanyLogo;
	}

	public String getClientRefreshDate() {
		return clientRefreshDate;
	}

	public void setClientRefreshDate(String clientRefreshDate) {
		this.clientRefreshDate = clientRefreshDate;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public List<JobEntity> getJobList() {
		return jobList;
	}

	public void setJobList(List<JobEntity> jobList) {
		this.jobList = jobList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getBossType() {
		return bossType;
	}

	public void setBossType(String bossType) {
		this.bossType = bossType;
	}

	public String getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(String minLatitude) {
		this.minLatitude = minLatitude;
	}

	public String getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(String maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public String getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(String minLongitude) {
		this.minLongitude = minLongitude;
	}

	public String getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(String maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public String getBossTypeName() {
		return bossTypeName;
	}

	public void setBossTypeName(String bossTypeName) {
		this.bossTypeName = bossTypeName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobAgeName() {
		return jobAgeName;
	}

	public void setJobAgeName(String jobAgeName) {
		this.jobAgeName = jobAgeName;
	}

	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}

	public Integer getResumeId() {
		return resumeId;
	}

	public void setResumeId(Integer resumeId) {
		this.resumeId = resumeId;
	}

	public List<JobEntity> getFringeBenefitsIdList() {
		return fringeBenefitsIdList;
	}

	public void setFringeBenefitsIdList(List<JobEntity> fringeBenefitsIdList) {
		this.fringeBenefitsIdList = fringeBenefitsIdList;
	}

	public String getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(String benefitsId) {
		this.benefitsId = benefitsId;
	}

}
