package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：个性标签配置表 类名称：com.mrrck.db.entity.MkPersonalTag 创建人：陈卫 创建时间：2016-5-13
 * 下午07:37:18
 * 
 * @version V1.0
 */
public class MkPersonalTag {
	/** 编号 */
	private Integer id;
	/** 个性标签分类id */
	private Integer categoryId;
	/** 颜色色值 */
	private String color;
	/** 标签名称 */
	private String tagName;
	/** 排序号 */
	private Integer sortNo;
	/** 创建时间 */
	private String createDate;
	/** 更新时间 */
	private String updateDate;
	/** 删除状态0正常1:逻辑删除 */
	private Integer delStatus;
	private Integer parentId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
