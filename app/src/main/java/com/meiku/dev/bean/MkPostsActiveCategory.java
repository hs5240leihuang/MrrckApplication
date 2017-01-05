package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：活动贴分类表 类名称：com.mrrck.db.entity.MkPostsActiveCategory 创建人：仲崇生
 * 创建时间：2015-10-26 下午01:52:37
 * 
 * @version V1.0
 */
public class MkPostsActiveCategory {

	/** 类目编号 */
	private Integer id;

	/** 类目名称 */
	private String name;

	/** 类目描述 */
	private String remark;

	/** 排序号 */
	private Integer sortNo;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0:正常,1:删除 */
	private Integer delStatus;

	/** 文件类型 0:图片 1:视频 */
	private Integer fileType;
	
	private Integer matchFlag;

	public MkPostsActiveCategory() {
		super();
	}

	public MkPostsActiveCategory(String name,int id,int fileType) {
		this.id = id;
		this.name = name;
		this.fileType = fileType;
	}

	public MkPostsActiveCategory(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setName(model.getString("name"));
		this.setRemark(model.getString("remark"));
		this.setSortNo(model.getInt("sortNo"));
		this.setCreateDate(model.getString("createDate"));
		this.setUpdateDate(model.getString(updateDate));
		this.setDelStatus(model.getInt("delStatus"));
		this.setFileType(model.getInt("fileType"));
		this.setMatchFlag(model.getInt("matchFlag"));
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

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getMatchFlag() {
		return matchFlag;
	}

	public void setMatchFlag(Integer matchFlag) {
		this.matchFlag = matchFlag;
	}

}
