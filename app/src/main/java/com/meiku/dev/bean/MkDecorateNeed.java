package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修 发布需求(正对C端) 类名称：com.mrrck.db.entity.MkDecorateNeed 创建人：陈卫
 * 创建时间：2016-9-15 上午09:49:15
 * 
 * @version V1.0
 */
public class MkDecorateNeed {

	/** 编号 */
	private Integer id;

	/** 用户id */
	private Integer userId;

	/** 需求主题 */
	private String needName;
	/** 店铺类型 */
	private String shopCategory;
	private String shopCategoryName;
	/** 面积大小 */
	private Integer areaSize;
	/** 装修时间 */
	private String decorateTime;
	/** 费用预算（存code） */
	private String costBudget;
	private String clientCostBudgetName;
	/** 省份code */
	private String provinceCode;
	/** 省份名字 */
	private String provinceName;
	/** 城市code */
	private String cityCode;
	/** 城市 */
	private String cityName;
	/** 联系人 */
	private String contactName;
	/** 联系手机 */
	private String contactPhone;
	/** 是否开启（0 开启 1 关闭） 默认开启 */
	private Integer isOpen;
	/** 更新时间 */
	private String updateDate;
	/** 创建时间 */
	private String createDate;
	/** 删除标记 0 正常 1删除 */
	private Integer delStatus;
	/**扩展表 mk_posts 中id字段  改造 jsg 2016年10月26日 18:39:11*/
	private Integer postsId;

	public Integer getPostsId() {
		return postsId;
	}

	public void setPostsId(Integer postsId) {
		this.postsId = postsId;
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

	public String getNeedName() {
		return needName;
	}

	public void setNeedName(String needName) {
		this.needName = needName;
	}

	public String getShopCategory() {
		return shopCategory;
	}

	public void setShopCategory(String shopCategory) {
		this.shopCategory = shopCategory;
	}

	public Integer getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Integer areaSize) {
		this.areaSize = areaSize;
	}

	public String getDecorateTime() {
		return decorateTime;
	}

	public void setDecorateTime(String decorateTime) {
		this.decorateTime = decorateTime;
	}

	public String getCostBudget() {
		return costBudget;
	}

	public void setCostBudget(String costBudget) {
		this.costBudget = costBudget;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
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

	public String getShopCategoryName() {
		return shopCategoryName;
	}

	public void setShopCategoryName(String shopCategoryName) {
		this.shopCategoryName = shopCategoryName;
	}

	public String getClientCostBudgetName() {
		return clientCostBudgetName;
	}

	public void setClientCostBudgetName(String clientCostBudgetName) {
		this.clientCostBudgetName = clientCostBudgetName;
	}

}
