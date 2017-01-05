package com.meiku.dev.bean;

/**
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：装修公司设计成员表 类名称：com.mrrck.db.entity.MkCompanyDesigners 创建人：陈卫
 * 创建时间：2016-10-13 上午10:36:31
 * 
 * @version V1.0
 */
public class MkCompanyDesigners {

	private Integer id;
	private Integer companyId;
	/** 设计师头像URL */
	private String headUrl;
	private String clientHeadUrl;
	/** 姓名 */
	private String name;
	/** 职称 */
	private String jobName;
	/** 工作年限 */
	private String workYears;
	/** 排序号 */
	private Integer sortNo;
	/** 个人说明 */
	private String personalNote;
	/** 用户id */
	private Integer userId;
	private String updateDate;
	private String createDate;
	private Integer delStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getWorkYears() {
		return workYears;
	}

	public void setWorkYears(String workYears) {
		this.workYears = workYears;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getPersonalNote() {
		return personalNote;
	}

	public void setPersonalNote(String personalNote) {
		this.personalNote = personalNote;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public String getClientHeadUrl() {
		return clientHeadUrl;
	}

	public void setClientHeadUrl(String clientHeadUrl) {
		this.clientHeadUrl = clientHeadUrl;
	}

}
