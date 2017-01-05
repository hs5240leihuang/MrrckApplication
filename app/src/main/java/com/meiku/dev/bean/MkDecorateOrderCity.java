package com.meiku.dev.bean;

public class MkDecorateOrderCity {
	/**自增编号*/
	private Integer id;
	/**用户编号*/
	private Integer userId;
	/**公司id*/
	private Integer companyId;
	/**案例个数*/
	private Integer caseNum;
	/**有效开始时间*/
	private String validStartTime;
	/**有效结束时间*/
	private String validEndTime;
	/**有效月数*/
	private Integer validMonth;
	/**是否开启   0 开启  1关闭*/
	private Integer isOpen;
	/**列表显示购买地区 (数据库冗余字段 方便终端展示)*/
	private String showCityName;
	/**购买地区数据-------终端请求参数  用于计算金钱*/
//	private String orderCityData;
	/**更新时间*/
	private String updateDate;
	/**创建时间*/
	private String createDate;
	/**删除标记(0正常  1删除)*/
	private Integer delStatus;
	/**续费有效开始时间*/
	private String renewValidStartTime;
	/**续费有效结束时间*/
	private String renewValidEndTime;
	/**公司log缩略图*/
	private String clientCompanyLogo;

	
	public String getClientCompanyLogo() {
		return clientCompanyLogo;
	}
	public void setClientCompanyLogo(String clientCompanyLogo) {
		this.clientCompanyLogo = clientCompanyLogo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}
	public String getValidStartTime() {
		return validStartTime;
	}
	public void setValidStartTime(String validStartTime) {
		this.validStartTime = validStartTime;
	}
	public String getValidEndTime() {
		return validEndTime;
	}
	public void setValidEndTime(String validEndTime) {
		this.validEndTime = validEndTime;
	}
	public Integer getValidMonth() {
		return validMonth;
	}
	public void setValidMonth(Integer validMonth) {
		this.validMonth = validMonth;
	}
	public Integer getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	public String getShowCityName() {
		return showCityName;
	}
	public void setShowCityName(String showCityName) {
		this.showCityName = showCityName;
	}
//	public String getOrderCityData() {
//		return orderCityData;
//	}
//	public void setOrderCityData(String orderCityData) {
//		this.orderCityData = orderCityData;
//	}
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
	public String getRenewValidStartTime() {
		return renewValidStartTime;
	}
	public void setRenewValidStartTime(String renewValidStartTime) {
		this.renewValidStartTime = renewValidStartTime;
	}
	public String getRenewValidEndTime() {
		return renewValidEndTime;
	}
	public void setRenewValidEndTime(String renewValidEndTime) {
		this.renewValidEndTime = renewValidEndTime;
	}
	
}
