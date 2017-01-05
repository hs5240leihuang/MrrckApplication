package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：app启动图版本信息表Entity 类名称：com.mrrck.db.entity.MkStartDiagramVersion 创建人：仲崇生
 * 创建时间：2016-5-23 下午03:49:47
 * 
 * @version V1.0
 */
public class MkStartDiagramVersion {

	/** 编号 */
	private Integer id;

	/** 创建人编号 */
	private Integer userId;

	/** 设备类型1:android 2:Pad 3:ios 4 iPad */
	private Integer devType;

	/** 图片类型 1:普通启动页面 2:赛事启动页 */
	private Integer picType;

	/** 版本号（x.x.x) */
	private String version;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标记 0:正常1:删除 */
	private Integer delStatus;
	private Integer startSeconds;
	
	/** 终端button 是否显示  0 不显示  1是显示*/
	private Integer buttonFlag;
	
	/**button按钮名称*/
	private String buttonName;
	
	/**是否客户端应用 0:客户端应用打开 1:H5网页打开  默认:0*/
	private Integer isClientApp;
	
	/**功能URL 客户端应用填写客户端约定内容 H5打开填写URL*/
	private String functionUrl; //APP_MODEL_MATCH  比赛   APP_MODEL_POSTS_DETAIL?5 跳转帖子详情    APP_MODEL_BOARD?5  跳转板块
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDevType() {
		return this.devType;
	}

	public void setDevType(Integer devType) {
		this.devType = devType;
	}

	public Integer getPicType() {
		return this.picType;
	}

	public void setPicType(Integer picType) {
		this.picType = picType;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getStartSeconds() {
		return startSeconds;
	}

	public void setStartSeconds(Integer startSeconds) {
		this.startSeconds = startSeconds;
	}

	public Integer getButtonFlag() {
		return buttonFlag;
	}

	public void setButtonFlag(Integer buttonFlag) {
		this.buttonFlag = buttonFlag;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public Integer getIsClientApp() {
		return isClientApp;
	}

	public void setIsClientApp(Integer isClientApp) {
		this.isClientApp = isClientApp;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

}
