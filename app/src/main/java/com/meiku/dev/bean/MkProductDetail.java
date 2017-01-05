package com.meiku.dev.bean;

import java.io.Serializable;

/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：产品自定义描述表
 * 类名称：com.mrrck.db.entity.MkProductDetail     
 * 创建人：仲崇生
 * 创建时间：2016-2-29 上午11:45:18   
 * @version V1.0
 */
public class MkProductDetail implements Serializable{
	/** 编号 */
	private Integer id;

	/** 用户编号 */
	private Integer userId;

	/** 产品编号 */
	private Integer productId;

	/** 产品扩展标题 */
	private String title;

	/** 产品扩展内容 */
	private String content;

	/** 附件数量 0:没有 其他数值 */
	private Integer attachmentNum;

	/** 排序号 */
	private Integer sortNo;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0:正常 1:删除 */
	private Integer delStatus;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public Integer getAttachmentNum() {
		return attachmentNum;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getSortNo() {
		return sortNo;
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

}
