package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：找装修概述描述附件
 * 类名称：com.mrrck.db.entity.MkDecorateAttachment
 * 创建人：陈卫
 * 创建时间：2016-8-30 下午05:32:00   
 * @version V1.0
 */
public class MkDecorateAttachment {
	/** 编号*/
	private Integer id;
	/** 用户编号*/
	private Integer userId;
	/** 案例概述id*/
	private Integer caseSummaryId;
	/** 找装修案例id 关联表*/
	private Integer caseId;
	/** 附件类型 0:图片 1:视频 2音频*/
	private Integer fileType;
	/** 图片多媒体路径*/
	private String fileUrl;
	private String clientFileUrl ;
	/** 音视频长度（秒数）附件类型是1,2有值 */
	private Integer fileSeconds;
	/** 标题 */
	private String title;;
	/** 描述 */
	private String remark;
	/** 排序*/
	private Integer sortNo;
	/** 图片宽度（文件为非图片时此字段为空）*/
	private String width;
	/** 图片高度（文件为非图片时此字段为空）*/
	private String height;
	/** 创建时间*/
	private String createDate;
	/** 更新时间*/
	private String updateDate;
	/** 删除标记0:正常1:删除*/
	private Integer delStatus;
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
	public Integer getCaseSummaryId() {
		return caseSummaryId;
	}
	public void setCaseSummaryId(Integer caseSummaryId) {
		this.caseSummaryId = caseSummaryId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
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
