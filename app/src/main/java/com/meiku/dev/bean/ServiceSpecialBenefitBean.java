package com.meiku.dev.bean;

public class ServiceSpecialBenefitBean {
	/** 编号 */
	private Integer id;

	/** 用户编号 */
	private Integer userId;

	/** 正标题 */
	private String title;

	/** 副标题 */
	private String smallTitle;

	/** 正文内容 */
	private String content;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除状态0正常1:逻辑删除 */
	private Integer delStatus;

	/** 广告推广logo(后台管理员设置) */
	private String advertLogo;

	/** 名称简写(后台管理员使用)(后台管理员使用) */
	private String shortName;

	/** 简短描述(后台管理员使用) */
	private String shortRemark;

	/** 广告推广整图(后台管理员使用) */
	private String advertImgUrl;

	/** 广告推广显示方式 0:logo加文字形式 1:整图广告 默认:0 */
	private Integer advertFlag;

	/** 广告排序号 ASC 默认:999 */
	private Integer advertSortNo;

	/** 是否是推荐广告 0:不是 1:是 默认:0 */
	private Integer advertStatus;

	/** 广告开始时间 */
	private String advertStartDate;

	/** 广告截止时间 */
	private String advertEndDate;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSmallTitle() {
		return smallTitle;
	}

	public void setSmallTitle(String smallTitle) {
		this.smallTitle = smallTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
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

	public String getAdvertStartDate() {
		return advertStartDate;
	}

	public void setAdvertStartDate(String advertStartDate) {
		this.advertStartDate = advertStartDate;
	}

	public String getAdvertEndDate() {
		return advertEndDate;
	}

	public void setAdvertEndDate(String advertEndDate) {
		this.advertEndDate = advertEndDate;
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
