package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：个人相册表 类名称：com.mrrck.db.entity.MkPersonalPhoto 创建人：仲崇生 创建时间：2016-5-26
 * 下午02:48:03
 * 
 * @version V1.0
 */
public class MkPersonalPhoto {

	/** 编号 */
	private Integer id;

	/** 用户编号 */
	private Integer userId;

	/** 附件类型 0:图片 1:视频 2音频 */
	private Integer fileType;

	/** 图片多媒体路径 */
	private String fileUrl;

	/** 音视频长度（秒数）附件类型是1,2有值 */
	private Integer fileSeconds;

	/** 标题 */
	private String title;

	/** 描述 */
	private String remark;

	/** 排序 默认999 */
	private Integer sortNo;

	/** 图片宽度（文件为非图片时此字段为空） */
	private String width;

	/** 图片高度（文件为非图片时此字段为空） */
	private String height;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 是否公开 0:公开 1:不公开 默认0 */
	private Integer isPublic;

	/** 删除标记0:正常1:删除 */
	private Integer delStatus;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getFileSeconds() {
		return this.fileSeconds;
	}

	public void setFileSeconds(Integer fileSeconds) {
		this.fileSeconds = fileSeconds;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getWidth() {
		return this.width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return this.height;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public Integer getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

}
