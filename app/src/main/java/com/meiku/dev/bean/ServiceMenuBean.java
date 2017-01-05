package com.meiku.dev.bean;

public class ServiceMenuBean {
	/** 编号 */
	private Integer id;

	/** 功能名称 */
	private String functionName;

	/** 图标URL 必填 */
	private String functionPhoto;

	/** 功能URL 客户端应用填写客户端约定内容 H5打开填写URL */
	private String functionUrl;

	/** 背景色 */
	private String bgColor;

	/** 排序号(ASC正序排序) */
	private Integer sortNo;

	/** 快捷功能排序号(ASC正序排序) */
	private Integer fastSortNo;

	/** 是否开启 0:开启 1:不开启 默认:0 */
	private Integer isOpen;

	/** 是否需要登录 0:需要登录 1:无需登录 默认:0 */
	private Integer isLogin;

	/** 是否客户端应用 0:客户端应用打开 1:H5网页打开 默认:0 */
	private Integer isClientApp;

	/** 是否快捷功能 0:是 1:不是 默认:1 */
	private Integer isFastFunction;

	/**
	 * 功能权限设定显示 有且仅有 0:全公开 1.从业者 2.经营者3.爱好者 4.(未登录,从业者,爱好者)5.(从业者,经营者)6.(从业者,爱好者
	 * )7.(经营者,爱好者 ) 默认:0
	 */
	private String authorType;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0:正常,1:删除 */
	private Integer delStatus;
	/** 图标URL(客户端使用) */
	private String clientFunctionPhoto;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFunctionPhoto() {
		return functionPhoto;
	}
	public void setFunctionPhoto(String functionPhoto) {
		this.functionPhoto = functionPhoto;
	}
	public String getFunctionUrl() {
		return functionUrl;
	}
	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public Integer getSortNo() {
		return sortNo;
	}
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	public Integer getFastSortNo() {
		return fastSortNo;
	}
	public void setFastSortNo(Integer fastSortNo) {
		this.fastSortNo = fastSortNo;
	}
	public Integer getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	public Integer getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(Integer isLogin) {
		this.isLogin = isLogin;
	}
	public Integer getIsClientApp() {
		return isClientApp;
	}
	public void setIsClientApp(Integer isClientApp) {
		this.isClientApp = isClientApp;
	}
	public Integer getIsFastFunction() {
		return isFastFunction;
	}
	public void setIsFastFunction(Integer isFastFunction) {
		this.isFastFunction = isFastFunction;
	}
	public String getAuthorType() {
		return authorType;
	}
	public void setAuthorType(String authorType) {
		this.authorType = authorType;
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
	public String getClientFunctionPhoto() {
		return clientFunctionPhoto;
	}
	public void setClientFunctionPhoto(String clientFunctionPhoto) {
		this.clientFunctionPhoto = clientFunctionPhoto;
	}
	
	

}
