package com.meiku.dev.bean;

/***
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找策划案例文件表（包含评论文件,策划人介绍）bean 类名称：com.mrrck.db.entity.MkPlanCaseAttachment
 * 创建人：曙光 创建时间：2016-12-22 下午02:10:35
 * 
 * @version V1.0
 */
public class PlanCaseAttachmentEntity {
	/** 自增编号 */
	private Integer id;
	/** 案例主表id 关联 */
	private Integer planCaseId;
	/** 案例扩展id 关联 */
	private Integer planCaseExtendId;
	/** 评论id 关联表 */
	private Integer planCommnetId;
	/** 用户编号 */
	private Integer userId;
	/** 数据类型 0案例文件 1案例评论文件 2策划人介绍（只关联userId） */
	private Integer type;
	/** 附件类型 0:图片 1:视频 2音频 */
	private String fileType;
	/** 图片多媒体路径 */
	private String fileUrl;
	/** 音视频长度（秒数）附件类型是1,2有值 */
	private Integer fileSeconds;
	/** 标题 */
	private String title;
	/** 描述 */
	private String remark;
	/** 排序 */
	private Integer sortNo;
	/** 图片宽度（文件为非图片时此字段为空） */
	private String width;
	/** 图片高度（文件为非图片时此字段为空） */
	private String height;
	/** 创建时间 */
	private String createDate;
	/** 更新时间 */
	private String updateDate;
	/** 0 正常 1 删除 */
	private Integer delStatus;
	/** 客户端列表展示文件路径 */
	private String clientThumbFileUrl;
	/** 客户端详情大图展示文件路径 */
	private String clientFileUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlanCaseId() {
		return planCaseId;
	}

	public void setPlanCaseId(Integer planCaseId) {
		this.planCaseId = planCaseId;
	}

	public Integer getPlanCaseExtendId() {
		return planCaseExtendId;
	}

	public void setPlanCaseExtendId(Integer planCaseExtendId) {
		this.planCaseExtendId = planCaseExtendId;
	}

	public Integer getPlanCommnetId() {
		return planCommnetId;
	}

	public void setPlanCommnetId(Integer planCommnetId) {
		this.planCommnetId = planCommnetId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
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

	public String getClientThumbFileUrl() {
		return clientThumbFileUrl;
	}

	public void setClientThumbFileUrl(String clientThumbFileUrl) {
		this.clientThumbFileUrl = clientThumbFileUrl;
	}

	public String getClientFileUrl() {
		return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}
}
