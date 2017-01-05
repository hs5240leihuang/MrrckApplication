package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：作品检索记录Entity 类名称：com.mrrck.ap.bd.entity.PostsSignupSearchRecordEntity
 * 创建人：仲崇生 创建时间：2016-1-15 下午07:59:06
 * 
 * @version V1.0
 */
public class PostsSignupSearchRecordEntity {
	private String createDate;
	private int delStatus;
	private int hotFlag;
	private int id;
	private int offset;
	private int pageNum;
	private String searchName;
	private int sortNo;
	private int sourceFlag;
	private String updateDate;
	private int userId;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public int getHotFlag() {
		return hotFlag;
	}

	public void setHotFlag(int hotFlag) {
		this.hotFlag = hotFlag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public int getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(int sourceFlag) {
		this.sourceFlag = sourceFlag;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
