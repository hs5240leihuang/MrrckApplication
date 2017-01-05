package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：功能菜单 类名称：com.mrrck.db.entity.MkMenu 创建人：仲崇生 创建时间：2015-10-26 上午11:43:03
 * 
 * @version V1.0
 */
public class MkMenu {
	public MkMenu() {
		super();
	}

	public MkMenu(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setName(model.getString("name"));
		this.setRemark(model.getString("remark"));
		this.setSortNo(model.getInt("sortNo"));
		this.setShowFlag(model.getInt("showFlag"));
		this.setCustomFlag(model.getInt("customFlag"));
		this.setMoreFlag(model.getInt("moreFlag"));
		this.setCreateDate(model.getString("createDate"));
		this.setUpdateDate(model.getString("updateDate"));
		this.setDelStatus(model.getInt("delStatus"));
		this.setPostsFlag(model.getInt("postsFlag"));
	}

	public MkMenu(String name) {
		this.name = name;
	}

	/** 编号 */
	private Integer id;

	/** 名称 */
	private String name;

	/** 描述 */
	private String remark;

	/** 排序号 */
	private Integer sortNo;

	/** 是否展示 0展示 1不展示 */
	private Integer showFlag;

	/** 是否允许创建自己的版 0允许 1不允许 */
	private Integer customFlag;

	/** 是否允许添加更多社区版 0允许 1不允许 */
	private Integer moreFlag;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0正常 1删除 */
	private Integer delStatus;

	/** 0版块，1帖子,2热帖 */
	private Integer postsFlag;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public Integer getShowFlag() {
		return showFlag;
	}

	public void setCustomFlag(Integer customFlag) {
		this.customFlag = customFlag;
	}

	public Integer getCustomFlag() {
		return customFlag;
	}

	public void setMoreFlag(Integer moreFlag) {
		this.moreFlag = moreFlag;
	}

	public Integer getMoreFlag() {
		return moreFlag;
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

	public Integer getPostsFlag() {
		return postsFlag;
	}

	public void setPostsFlag(Integer postsFlag) {
		this.postsFlag = postsFlag;
	}

}
