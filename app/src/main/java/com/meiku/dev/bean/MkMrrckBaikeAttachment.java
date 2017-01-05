package com.meiku.dev.bean;

/**
 *
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web
 *
 * 类描述：
 * 类名称：com.mrrck.db.entity.MkMrrckBaikeAttachment
 * 创建人：仲崇生
 * 创建时间：Fri Jun 03 20:05:47 CST 2016
 * @version V1.0
 */ 
public class MkMrrckBaikeAttachment{

 	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 原百科编号 */
	private Integer baiKeId;

 	/** 附件类型 0:图片 1:视频 2音频 */
	private Integer fileType;

 	/** 图片多媒体路径 */
	private String fileUrl;

 	/** 音视频长度（秒数）附件类型是1,2有值 */
	private Integer fileSeconds;

 	/** 标题 */
	private String title;

 	/** 文字描述 */
	private String remark;

 	/** 排序 */
	private Integer sortNo;

 	/** 图片宽度（文件为非图片时此字段为空） */
	private String width;

 	/** 图片高度（非图片时此字段为空） */
	private String height;

 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 是否公开 0:公开 1:不公开 默认0 */
	private Integer isPublic;

 	/** 删除标记0:正常1:删除 */
	private Integer delStatus;
	
	/**  */
	private String clientFileUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBaiKeId() {
		return baiKeId;
	}

	public void setBaiKeId(Integer baiKeId) {
		this.baiKeId = baiKeId;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getFileSeconds() {
		return fileSeconds;
	}

	public void setFileSeconds(Integer fileSeconds) {
		this.fileSeconds = fileSeconds;
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

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public String getClientFileUrl() {
		return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}
	

}
