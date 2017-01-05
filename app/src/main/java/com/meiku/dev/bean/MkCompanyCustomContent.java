package com.meiku.dev.bean;

/**
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：公司自定义内容表 类名称：com.mrrck.db.entity.MkCompanyCsutomContent 创建人：陈卫
 * 创建时间：2016-10-13 上午11:05:18
 * 
 * @version V1.0
 */
public class MkCompanyCustomContent {
	/***/
	private Integer id;
	/** 公司ID */
	private Integer companyId;
	/** 标题 */
	private String title;
	/** 内容 */
	private String content;
	/** 附件数量 */
	private Integer attachmentNum;
	/** 排序号 */
	private Integer sortNo;
	/** 创建人 */
	private Integer userId;
	/** 公司ID */
	private String createDate;
	/** 公司ID */
	private String updateDate;
	/** 公司ID */
	private Integer delStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getAttachmentNum() {
		return attachmentNum;
	}

	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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
