package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 版权所有：2016-美库网 项目名称：mrrck-web 类描述：找策划配置数据
 * 类名称：com.mrrck.db.entity.MkDataConfigPlan 创建人：曙光 创建时间：2016-12-21 上午11:47:07
 * 
 * @version V1.0
 */
public class MkDataConfigPlan {

	/** 编号 */
	private Integer id;
	/** 名称 */
	private String functionName;
	/** 描述 */
	private String functionRemark;
	/** 图标URL */
	private String functionPhoto;
	/** 与终端协商布局方式 */
	private String functionLayout;
	/** 功能URL 客户端应用填写客户端约定内容 H5打开填写URL */
	private String functionUrl;
	/** code指标 */
	private Integer code;
	/** 类型 0首页配置 1类型配置 */
	private Integer type;
	/** 排序号(ASC正序排序) */
	private Integer sortNo;
	/** 是否客户端应用 0:客户端应用打开 1:H5网页打开 默认:0 */
	private Integer isClientApp;
	/** 创建时间 */
	private String createDate;
	/** 更新时间 */
	private String updateDate;
	/** 删除标识 0:正常,1:删除 */
	private Integer delStatus;

	public MkDataConfigPlan(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setFunctionName(model.getString("functionName"));
		this.setFunctionRemark(model.getString("functionRemark"));
		this.setFunctionPhoto(model.getString("functionPhoto"));
		this.setFunctionLayout(model.getString("functionLayout"));
		this.setFunctionUrl(model.getString("functionUrl"));
		this.setCode(model.getInt("code"));
		this.setType(model.getInt("type"));
		this.setSortNo(model.getInt("sortNo"));
		this.setIsClientApp(model.getInt("isClientApp"));
		this.setCreateDate(model.getString("createDate"));
		this.setUpdateDate(model.getString("updateDate"));
		this.setDelStatus(model.getInt("delStatus"));
	}

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

	public String getFunctionRemark() {
		return functionRemark;
	}

	public void setFunctionRemark(String functionRemark) {
		this.functionRemark = functionRemark;
	}

	public String getFunctionPhoto() {
		return functionPhoto;
	}

	public void setFunctionPhoto(String functionPhoto) {
		this.functionPhoto = functionPhoto;
	}

	public String getFunctionLayout() {
		return functionLayout;
	}

	public void setFunctionLayout(String functionLayout) {
		this.functionLayout = functionLayout;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getIsClientApp() {
		return isClientApp;
	}

	public void setIsClientApp(Integer isClientApp) {
		this.isClientApp = isClientApp;
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

}
