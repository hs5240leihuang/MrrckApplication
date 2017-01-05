package com.meiku.dev.bean;

/**
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修首页配置数据实体 类名称：com.mrrck.db.entity.MkDecorateIndexConfig 创建人：曙光
 * 创建时间：2016-8-30 下午06:04:43
 * 
 * @version V1.0
 */
public class MkDecorateIndexConfig {
	/** 自增编号 */
	private Integer id;
	/** 标题 */
	private String title;
	/** 备注 */
	private String remark;
	/** 是否终端打开 0 打开 1 H5 */
	private Integer isClient;
	/** 跳转url */
	private String functionUrl;
	/** 配置图片url */
	private String imgUrl;
	/** 位置标记(存放在首页标记 从位置开始 分别 0 1 标记) */
	private Integer type;
	/*** 更新时间 */
	private String updateDate;
	/*** 创建时间 */
	private String createDate;
	/** 删除标记 删除标记 0 正常 1删除 */
	private Integer delStatus;
	/*** 菜单图片 */
	private String clientImgUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsClient() {
		return isClient;
	}

	public void setIsClient(Integer isClient) {
		this.isClient = isClient;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getClientImgUrl() {
		return clientImgUrl;
	}

	public void setClientImgUrl(String clientImgUrl) {
		this.clientImgUrl = clientImgUrl;
	}
}
