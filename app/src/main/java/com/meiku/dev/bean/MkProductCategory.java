package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：产品类型表(找产品) 类名称：com.mrrck.db.entity.MkProductCategory 创建人：仲崇生
 * 创建时间：2016-2-29 上午11:37:08
 * 
 * @version V1.0
 */
public class MkProductCategory {
	public MkProductCategory() {
		super();
	}
	public MkProductCategory(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setUserId(model.getInt("userId"));
		this.setName(model.getString("name"));
		this.setCode(model.getString("code"));
		this.setSortNo(model.getInt("sortNo"));
		this.setColorCode(model.getString("colorCode"));
		this.setCreateDate(model.getString("createDate"));
		this.setUpdateDate(model.getString("updateDate"));
		this.setDelStatus(model.getInt("delStatus"));
	}
	/** 编号 */
	private Integer id;

	/** 创建人(用户编号) */
	private Integer userId;

	/** 类型名称 */
	private String name;

	/** 类型编码 */
	private String code;

	/** 排序号 */
	private Integer sortNo;

	/** 类型颜色编码 */
	private String colorCode;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0:正常 1:删除 */
	private Integer delStatus;

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

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorCode() {
		return colorCode;
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

}
