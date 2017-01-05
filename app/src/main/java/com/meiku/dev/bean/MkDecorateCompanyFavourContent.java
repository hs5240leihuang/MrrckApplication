package com.meiku.dev.bean;

import java.io.Serializable;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修实惠内容扩展表 类名称：com.mrrck.db.entity.MkDecorateCompanyFavourContent 创建人：陈卫
 * 创建时间：2016-9-2 上午11:42:13
 * 
 * @version V1.0
 */
public class MkDecorateCompanyFavourContent implements Serializable{
	/** 编号 */
	private Integer id;
	/** 实惠名称 */
	private String name="";
	/** 备注 */
	private String remark;
	/** 装修实惠id */
	private String decorateComapanyFavourId;
	/** 更新时间 */
	private String updateDate;
	/** 创建时间 */
	private String createDate;
	/** 删除标记 0 正常 1 删除 */
	private Integer delStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDecorateComapanyFavourId() {
		return decorateComapanyFavourId;
	}

	public void setDecorateComapanyFavourId(String decorateComapanyFavourId) {
		this.decorateComapanyFavourId = decorateComapanyFavourId;
	}

}
