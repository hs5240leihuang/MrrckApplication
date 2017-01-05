package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：帖子评论表 类名称：com.mrrck.db.entity.MkPostsComment 创建人：仲崇生 创建时间：2015-10-26
 * 下午01:59:29
 * 
 * @version V1.0
 */
public class MkPostsComment {

	/** 编号 */
	private Integer id;

	/** 评论用户编号 */
	private Integer userId;

	/** 帖子编号 */
	private Integer postsId;

	/** 被回复的用户id，默认为0 */
	private Integer toUserId;

	/** 回复的评论ID */
	private Integer toCommentId;

	/** 评论内容 */
	private String content;

	/** 附件类型 0:图片 1:视频 2音频 */
	private Integer fileType;

	/** 图片多媒体路径 */
	private String fileUrl;

	/** 创建时间(评论时间) */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 举报类型1:广告信息2:黄色信息3:辱骂他人4:非法言论,默认:0 */
	private Integer reportType;

	/** 是否被举报,0:未被举报1:被举报,默认0 */
	private Integer isReport;

	/** 删除状态0正常1:逻辑删除 */
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

	public void setPostsId(Integer postsId) {
		this.postsId = postsId;
	}

	public Integer getPostsId() {
		return postsId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToCommentId(Integer toCommentId) {
		this.toCommentId = toCommentId;
	}

	public Integer getToCommentId() {
		return toCommentId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileUrl() {
		return fileUrl;
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

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setIsReport(Integer isReport) {
		this.isReport = isReport;
	}

	public Integer getIsReport() {
		return isReport;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

}
