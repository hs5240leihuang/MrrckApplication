package com.meiku.dev.bean;

import java.util.List;

/**
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找策划 案例实体 类名称：com.mrrck.ap.plan.entity.PlanCaseEntity 创建人：曙光
 * 创建时间：2016-12-21 下午06:12:08
 * 
 * @version V1.0
 */
public class PlanCaseEntity {

	/** 编号 */
	private Integer id;
	/** 案例标题 */
	private String title;
	/** 案例类型 另外配置数据code指标 */
	private Integer caseType;
	/** 店铺类型 */
	private String shopType;
	/** 浏览数量 */
	private Integer viewNum;
	/** 收藏数量 */
	private Integer collectNum;
	/** 评论数量 */
	private Integer commentNum;
	/** 点赞个数 默认0 */
	private Integer likeNum;
	/** 企业介绍 */
	private Integer userId;
	/** 企业创建时间 */
	private String createDate;
	/** 最后更新时间 */
	private String updateDate;
	/** 0正常1删除 */
	private String delStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCaseType() {
		return caseType;
	}

	public void setCaseType(Integer caseType) {
		this.caseType = caseType;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
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

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	/** 搜索关键词 */
	private String searchName;
	/** 附件集合 */
	private List<PlanCaseAttachmentEntity> planCaseAttachmentEntityList;
	/** 头像后缀 */
	private String headPicUrl;
	/** 客户端头像 */
	private String clientHeadPicUrl;
	/** 客户端店铺类型 */
	private String shopTypeName;
	/** 客户端展示时间 */
	private String clientUpdateDate;
	/** 用户昵称 */
	private String nickName;
	/**载入h5网页*/
	private String loadUrl;
	/**分享h5网页*/
	private String shareUrl;
	/**分享标题*/
	private String shareTitle;
	/**分享内容*/
	private String shareContent;
	/**分享图片*/
	private String shareImg;
	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public List<PlanCaseAttachmentEntity> getPlanCaseAttachmentEntityList() {
		return planCaseAttachmentEntityList;
	}

	public void setPlanCaseAttachmentEntityList(
			List<PlanCaseAttachmentEntity> planCaseAttachmentEntityList) {
		this.planCaseAttachmentEntityList = planCaseAttachmentEntityList;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public String getShopTypeName() {
		return shopTypeName;
	}

	public void setShopTypeName(String shopTypeName) {
		this.shopTypeName = shopTypeName;
	}

	public String getClientUpdateDate() {
		return clientUpdateDate;
	}

	public void setClientUpdateDate(String clientUpdateDate) {
		this.clientUpdateDate = clientUpdateDate;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

}
