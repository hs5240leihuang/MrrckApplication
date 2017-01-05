package com.meiku.dev.bean;

public class MkDecorateOrderCityContent {
	/** 自增编号 */
	private Integer id;
	/** 用户编号 */
	private Integer userId;
	/** 订购地区(城市)主表id 关联 */
	private Integer orderCityId;
	/** 公司id */
	private Integer companyId;
	/** 所属省份名称 */
	private String provinceName;
	/** 所属省份code */
	private String provinceCode;
	/** 所属城市code */
	private String cityCode;
	/** 所属城市名称 */
	private String cityName;
	/** 有效开始时间 */
	private String validStartTime;
	/** 有效结束时间 */
	private String validEndTime;
	/** 有效月 */
	private Integer validMonth;
	/** 置顶标记 0 置顶 1 未置顶 */
	private Integer topFlag;
	/** 置顶开始时间 */
	private String topStartTime;
	/** 置顶结束时间 */
	private String topEndTime;
	/** 更新时间 */
	private String updateDate;
	/** 开始时间 */
	private String createDate;
	/** 删除标记 0 正常 1删除 */
	private Integer delStatus;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(Integer topFlag) {
		this.topFlag = topFlag;
	}

	public String getTopStartTime() {
		return topStartTime;
	}

	public void setTopStartTime(String topStartTime) {
		this.topStartTime = topStartTime;
	}

	public String getTopEndTime() {
		return topEndTime;
	}

	public void setTopEndTime(String topEndTime) {
		this.topEndTime = topEndTime;
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

	public Integer getOrderCityId() {
		return orderCityId;
	}

	public void setOrderCityId(Integer orderCityId) {
		this.orderCityId = orderCityId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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
}
