package com.meiku.dev.bean;

public class ServiceZhaoPingBean {
	/** 公司编号 */
	private Integer id;
	/** 用户id */
	private Integer userId;

	/** 企业名称 */
	private String name;
	/** 是否是vip企业 0:不是 1:是 默认:0 */
	private String isVip;

	/** vip企业开始日期 */
	private String startVipDate;

	/** vip企业截止日期 */
	private String endVipDate;
	/** 广告推广logo(后台管理员设置) */
	private String advertLogo;

	/** 公司简写(后台管理员使用) */
	private String shortName;

	/** 公司简短描述(后台管理员使用) */
	private String shortRemark;

	/** 广告推广整图(后台管理员使用) */
	private String advertImgUrl;

	/** 广告推广显示方式 0:logo加文字形式 1:整图广告 默认:0 */
	private Integer advertFlag;

	/** 广告排序号 ASC 默认:999 */
	private Integer advertSortNo;

	/** 是否是推荐广告 0:不是 1:是 默认:0 */
	private Integer advertStatus;
	/** 广告推广logo(客户端使用) */
	private String clientAdvertLogo;

	/** 广告推广整图(客户端使用) */
	private String clientAdvertImgUrl;

	/** H5打开URL(客户端使用) */
	private String clientHFiveUrl;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsVip() {
		return isVip;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

	public String getStartVipDate() {
		return startVipDate;
	}

	public void setStartVipDate(String startVipDate) {
		this.startVipDate = startVipDate;
	}

	public String getEndVipDate() {
		return endVipDate;
	}

	public void setEndVipDate(String endVipDate) {
		this.endVipDate = endVipDate;
	}

	public String getAdvertLogo() {
		return advertLogo;
	}

	public void setAdvertLogo(String advertLogo) {
		this.advertLogo = advertLogo;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortRemark() {
		return shortRemark;
	}

	public void setShortRemark(String shortRemark) {
		this.shortRemark = shortRemark;
	}

	public String getAdvertImgUrl() {
		return advertImgUrl;
	}

	public void setAdvertImgUrl(String advertImgUrl) {
		this.advertImgUrl = advertImgUrl;
	}

	public Integer getAdvertFlag() {
		return advertFlag;
	}

	public void setAdvertFlag(Integer advertFlag) {
		this.advertFlag = advertFlag;
	}

	public Integer getAdvertSortNo() {
		return advertSortNo;
	}

	public void setAdvertSortNo(Integer advertSortNo) {
		this.advertSortNo = advertSortNo;
	}

	public Integer getAdvertStatus() {
		return advertStatus;
	}

	public void setAdvertStatus(Integer advertStatus) {
		this.advertStatus = advertStatus;
	}

	public String getClientAdvertLogo() {
		return clientAdvertLogo;
	}

	public void setClientAdvertLogo(String clientAdvertLogo) {
		this.clientAdvertLogo = clientAdvertLogo;
	}

	public String getClientAdvertImgUrl() {
		return clientAdvertImgUrl;
	}

	public void setClientAdvertImgUrl(String clientAdvertImgUrl) {
		this.clientAdvertImgUrl = clientAdvertImgUrl;
	}

	public String getClientHFiveUrl() {
		return clientHFiveUrl;
	}

	public void setClientHFiveUrl(String clientHFiveUrl) {
		this.clientHFiveUrl = clientHFiveUrl;
	}

}
