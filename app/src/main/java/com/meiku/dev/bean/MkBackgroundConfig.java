package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：背景图配置表 类名称：com.mrrck.db.entity.MkBackgroundConfig 创建人：仲崇生 创建时间：2016-3-22
 * 下午04:45:30
 * 
 * @version V1.0
 */
public class MkBackgroundConfig {

	/** 编号 */
	private Integer id;

	/** 名称 */
	private String name;

	/** 背景图URL */
	private String picUrl;

	/** 描述 */
	private String remark;

	/** 排序号 默认999 */
	private Integer sortNo;

	/** 是否是默认背景图 0:不是 1:是 */
	private String defaultFalg;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除状态 0:正常 1:删除 */
	private Integer delStatus;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getDefaultFalg() {
		return this.defaultFalg;
	}

	public void setDefaultFalg(String defaultFalg) {
		this.defaultFalg = defaultFalg;
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

}
