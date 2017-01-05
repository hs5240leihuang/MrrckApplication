package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：达人导师团表 类名称：com.mrrck.db.entity.MkPeopleAdvisor 创建人：仲崇生 创建时间：2015-12-22
 * 下午02:15:10
 * 
 * @version V1.0
 */
public class MkPeopleAdvisor {

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

	/** 打开URL */
	private String advertUrl;

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

	/** 是否客户端应用 0:客户端应用打开 1:H5网页打开 默认:0 */
	private Integer isClientApp;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSmallTitle(String smallTitle) {
		this.smallTitle = smallTitle;
	}

	public String getSmallTitle() {
		return smallTitle;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public String getAdvertLogo() {
		return this.advertLogo;
	}

	public void setAdvertLogo(String advertLogo) {
		this.advertLogo = advertLogo;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortRemark() {
		return this.shortRemark;
	}

	public void setShortRemark(String shortRemark) {
		this.shortRemark = shortRemark;
	}

	public String getAdvertImgUrl() {
		return this.advertImgUrl;
	}

	public void setAdvertImgUrl(String advertImgUrl) {
		this.advertImgUrl = advertImgUrl;
	}

	public String getAdvertUrl() {
		return this.advertUrl;
	}

	public void setAdvertUrl(String advertUrl) {
		this.advertUrl = advertUrl;
	}

	public Integer getAdvertFlag() {
		return this.advertFlag;
	}

	public void setAdvertFlag(Integer advertFlag) {
		this.advertFlag = advertFlag;
	}

	public Integer getAdvertSortNo() {
		return this.advertSortNo;
	}

	public void setAdvertSortNo(Integer advertSortNo) {
		this.advertSortNo = advertSortNo;
	}

	public Integer getAdvertStatus() {
		return this.advertStatus;
	}

	public void setAdvertStatus(Integer advertStatus) {
		this.advertStatus = advertStatus;
	}

	public String getAdvertStartDate() {
		return this.advertStartDate;
	}

	public void setAdvertStartDate(String advertStartDate) {
		this.advertStartDate = advertStartDate;
	}

	public String getAdvertEndDate() {
		return this.advertEndDate;
	}

	public void setAdvertEndDate(String advertEndDate) {
		this.advertEndDate = advertEndDate;
	}

	public Integer getIsClientApp() {
		return this.isClientApp;
	}

	public void setIsClientApp(Integer isClientApp) {
		this.isClientApp = isClientApp;
	}

}
