package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：话题表 类名称：com.mrrck.db.entity.MkTopic 创建人：仲崇生 创建时间：2015-10-26 下午02:08:36
 * 
 * @version V1.0
 */
public class MkTopic {

	/** 编号 */
	private Integer id;

	/** 话题名称 */
	private String name;

	/** 描述 */
	private String remark;

	/** 用户编号(创建人编号) */
	private Integer userId;

	/** 兴趣分类编号 */
	private Integer menuId;

	/** 版块编号 */
	private Integer boardId;

	/** 排序号 */
	private Integer sortNo;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0正常 1删除 */
	private Integer delStatus;

	/** 话题用户查看的角色(多个逗号分隔) */
	private String roleTypes;

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

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public Integer getBoardId() {
		return boardId;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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

	public String getRoleTypes() {
		return this.roleTypes;
	}

	public void setRoleTypes(String roleTypes) {
		this.roleTypes = roleTypes;
	}

}
