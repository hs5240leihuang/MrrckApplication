package com.meiku.dev.bean;

public class CompanyInfoCountEntity {

	/** 编号 */
	private Integer companyId;

	/** 用户id */
	private Integer userId;

	/** 是否是vip企业 0:不是 1:是 默认:0 */
	private String isVip;

	/** vip企业开始日期 */
	private String startVipDate;

	/** vip企业截止日期 */
	private String endVipDate;

	/** 公司信誉等级 默认3级 最大5级 */
	private String startLevel;

	/** 剩余邀请面试次数 默认 0 */
	private Integer inviteNum;

	/** 是否已被美库认证 0:未认证 1:已认证 */
	private Integer mrrckAuthFlag;

	/** 会员类型 0:未开通会员 1:月度会员 2:季度会员 3.半年会员 4.年度会员 5.会员已过期 */
	private Integer vipType;

	/** 正在发布职位数 */
	private Integer jobNum;

	/** 7天收到简历数 */
	private Integer sevenReceiveResumeNum;

	/** 共收到简历数 */
	private Integer receiveResumeNum;

	/** 会员类型对应中文 */
	private String vipTypeName;
	/** 开通城市 */
	private String openCityName = "";
	/** 开通月份 */
	private String monthName;

	/** 会员类型图片地址 */
	private String vipTypeImgUrl;

	/** 购买资格 0 可以购买 1 不可以购买 */
	private String orderFlag;

	/** 购买话语提示 */
	private String orderFlagMsg;

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getVipTypeImgUrl() {
		return vipTypeImgUrl;
	}

	public void setVipTypeImgUrl(String vipTypeImgUrl) {
		this.vipTypeImgUrl = vipTypeImgUrl;
	}

	public String getOpenCityName() {
		return openCityName;
	}

	public void setOpenCityName(String openCityName) {
		this.openCityName = openCityName;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getIsVip() {
		return this.isVip;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

	public String getStartVipDate() {
		return this.startVipDate;
	}

	public void setStartVipDate(String startVipDate) {
		this.startVipDate = startVipDate;
	}

	public String getEndVipDate() {
		return this.endVipDate;
	}

	public void setEndVipDate(String endVipDate) {
		this.endVipDate = endVipDate;
	}

	public String getStartLevel() {
		return this.startLevel;
	}

	public void setStartLevel(String startLevel) {
		this.startLevel = startLevel;
	}

	public Integer getInviteNum() {
		return this.inviteNum;
	}

	public void setInviteNum(Integer inviteNum) {
		this.inviteNum = inviteNum;
	}

	public Integer getMrrckAuthFlag() {
		return this.mrrckAuthFlag;
	}

	public void setMrrckAuthFlag(Integer mrrckAuthFlag) {
		this.mrrckAuthFlag = mrrckAuthFlag;
	}

	public Integer getVipType() {
		return this.vipType;
	}

	public void setVipType(Integer vipType) {
		this.vipType = vipType;
	}

	public Integer getJobNum() {
		return this.jobNum;
	}

	public void setJobNum(Integer jobNum) {
		this.jobNum = jobNum;
	}

	public Integer getSevenReceiveResumeNum() {
		return this.sevenReceiveResumeNum;
	}

	public void setSevenReceiveResumeNum(Integer sevenReceiveResumeNum) {
		this.sevenReceiveResumeNum = sevenReceiveResumeNum;
	}

	public Integer getReceiveResumeNum() {
		return this.receiveResumeNum;
	}

	public void setReceiveResumeNum(Integer receiveResumeNum) {
		this.receiveResumeNum = receiveResumeNum;
	}

	public String getVipTypeName() {
		return this.vipTypeName;
	}

	public void setVipTypeName(String vipTypeName) {
		this.vipTypeName = vipTypeName;
	}

	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}

	public String getOrderFlagMsg() {
		return orderFlagMsg;
	}

	public void setOrderFlagMsg(String orderFlagMsg) {
		this.orderFlagMsg = orderFlagMsg;
	}

}
