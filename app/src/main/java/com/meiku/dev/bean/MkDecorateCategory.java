package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修店铺分类 类名称：com.mrrck.db.entity.MkDecorateCategory 创建人：曙光 创建时间：2016-8-31
 * 下午06:29:24
 * 
 * @version V1.0
 */
public class MkDecorateCategory {

	/** 自增编号 */
	private Integer id;
	/** 类型名称 */
	private String name;
	/** 备注 */
	private String remark;
	/** code指标 */
	private Integer code;
	/** 排序号 */
	private Integer sortNo;
	/** 发布或编辑显示标记 0显示 1 不现实 */
	private Integer showFlag;
	/** 指标类型 0 店铺分类 1材料费类型 2 人工费类型 3 设计费类型 */
	private Integer type;
	/** 创建时间 */
	private String createDate;
	/** 更新时间 */
	private String updateDate;
	/** 删除标识 0:正常 1:删除 */
	private Integer delStatus;

	public MkDecorateCategory(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setName(model.getString("name"));
		this.setType(model.getInt("type"));
		this.setCode(model.getInt("code"));
		this.setRemark(model.getString("remark"));
		this.setSortNo(model.getInt("sortNo"));
		this.setShowFlag(model.getInt("showFlag"));
		this.setCreateDate(model.getString("createDate"));
		this.setUpdateDate(model.getString("updateDate"));
		this.setDelStatus(model.getInt("delStatus"));
	}

	public Integer getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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
