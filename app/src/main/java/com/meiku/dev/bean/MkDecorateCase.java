package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修(新版)案例 类名称：com.mrrck.db.entity.MkDecorateCase 创建人：陈卫 创建时间：2016-8-30
 * 下午05:32:00
 * 
 * @version V1.0
 */
public class MkDecorateCase {
	/** 编号 */
	private Integer id;
	/** 公司id */
	private Integer companyId;
	/** 用户编号 */
	private Integer userId;
	/** 购买城市关联id */
	private Integer orderCityId;
	/** 案例名称 */
	private String caseName;
	/** 所属省份code */
	private String provinceCode;
	/** 所属城市code */
	private String cityCode;
	/** 所属省份名称 */
	private String provinceName;
	/** 所属城市名称 */
	private String cityName;
	/** 主案设计名称 */
	private String caseDesignName;
	/** 店铺类型 code */
	private Integer shopCategory;
	/** 店铺面积大小 */
	private String shopAreaSize;
	/** 是否开启（0 开启 1 关闭） 默认关闭1 */
	private Integer isOpen;
	/** 浏览量 */
	private Integer viewNum;
	/** 案例主图文件路径 */
	private String caseImgFileUrl;
	/** 案例概述 */
	private String summary;
	/** 案例价格 */
	private String casePice;
	/** 店铺名称 */
	private String shopName;
	/** 店铺地址 */
	private String shopAddress;
	/** 更新时间 */
	private String updateDate;
	/** 创建时间 */
	private String createDate;
	/** 删除标记 0 正常 1 删除 */
	private String delStatus;
	/** 装修工期开始时间 */
	private String decorateProjectStartTime;
	/** 装修工期结束时间 */
	private String decorateProjectEndTime;
	private boolean isSelect;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getOrderCityId() {
		return orderCityId;
	}

	public void setOrderCityId(Integer orderCityId) {
		this.orderCityId = orderCityId;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public String getCaseImgFileUrl() {
		return caseImgFileUrl;
	}

	public void setCaseImgFileUrl(String caseImgFileUrl) {
		this.caseImgFileUrl = caseImgFileUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getShopAreaSize() {
		return shopAreaSize;
	}

	public void setShopAreaSize(String shopAreaSize) {
		this.shopAreaSize = shopAreaSize;
	}

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
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

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCaseDesignName() {
		return caseDesignName;
	}

	public void setCaseDesignName(String caseDesignName) {
		this.caseDesignName = caseDesignName;
	}

	public Integer getShopCategory() {
		return shopCategory;
	}

	public void setShopCategory(Integer shopCategory) {
		this.shopCategory = shopCategory;
	}

	public String getCasePice() {
		return casePice;
	}

	public void setCasePice(String casePice) {
		this.casePice = casePice;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
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

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getDecorateProjectStartTime() {
		return decorateProjectStartTime;
	}

	public void setDecorateProjectStartTime(String decorateProjectStartTime) {
		this.decorateProjectStartTime = decorateProjectStartTime;
	}

	public String getDecorateProjectEndTime() {
		return decorateProjectEndTime;
	}

	public void setDecorateProjectEndTime(String decorateProjectEndTime) {
		this.decorateProjectEndTime = decorateProjectEndTime;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
