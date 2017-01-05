package com.meiku.dev.bean;

import java.io.Serializable;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：帖子表 类名称：com.mrrck.db.entity.MkPosts 创建人：仲崇生 创建时间：2015-10-26 下午01:49:21
 * 
 * @version V1.0
 */
public class MkPosts implements Serializable {

	/** 编号 */
	private Integer id;

	/** 用户编号 */
	private Integer userId;

	/** 版块编号 */
	private Integer boardId;

	/** 话题编号 */
	private Integer topicId;

	/** 标题 */
	private String title;

	/** 内容 */
	private String content;

	/** 省份code */
	private Integer provinceCode;

	/** 城市code */
	private Integer cityCode;
	/** 城市name */
	private String activeCityName;
	/** 阅读数 */
	private Integer viewNum;

	/** 评论数 */
	private Integer commentNum;

	/** 点赞数 */
	private Integer likeNum;

	/** 收藏数 */
	private Integer collectNum;

	/** 置顶 : 0不置顶 1置顶 */
	private Integer topFlag;

	/** 推荐标识: 0普通 1精华 */
	private Integer goodFlag;

	/** 热门: 0普通 1热门 */
	private Integer hotFlag;

	/** 数据来源: 0:普通(用户自创建) 1:官方 默认:0 */
	private Integer officeFlag;

	/** 活动贴: 0普通 1活动贴 */
	private Integer activeFlag;

	/** 附件数量 0:没有 其他数值 */
	private Integer attachmentNum;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标识 0正常 1删除 */
	private Integer delStatus;
	/** 是否收藏状态 0:未收藏 1:已收藏 */
	private String collectFlag;

	/** 是否点赞状态 0:未点赞 1:已点赞 */
	private String likeFlag;
	/** 帖子来源 */
	private String menuName;
	/** 时间 */
	private String clientViewDate;
	/** 帖子详情URL */
	private String clientPostsDetailUrl;

//	/**menu 图片地址  2016-07-07 16:37:23  jsg  添加*/
//    private String iocUrl;
//    /**menu 客户端图片地址  2016-07-07 16:37:23  jsg  添加*/
//    private String clientIocUrl;
	
	/** 分享图标 */
	private String  shareImg;
	
	public String getClientPostsDetailUrl() {
		return clientPostsDetailUrl;
	}

	public void setClientPostsDetailUrl(String clientPostsDetailUrl) {
		this.clientPostsDetailUrl = clientPostsDetailUrl;
	}

	public String getClientViewDate() {
		return clientViewDate;
	}

	public void setClientViewDate(String clientViewDate) {
		this.clientViewDate = clientViewDate;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

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

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public Integer getBoardId() {
		return boardId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer getTopicId() {
		return topicId;
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

	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getProvinceCode() {
		return provinceCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public String getActiveCityName() {
		return activeCityName;
	}

	public void setActiveCityName(String activeCityName) {
		this.activeCityName = activeCityName;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getViewNum() {
		return viewNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setTopFlag(Integer topFlag) {
		this.topFlag = topFlag;
	}

	public Integer getTopFlag() {
		return topFlag;
	}

	public void setGoodFlag(Integer goodFlag) {
		this.goodFlag = goodFlag;
	}

	public Integer getGoodFlag() {
		return goodFlag;
	}

	public void setHotFlag(Integer hotFlag) {
		this.hotFlag = hotFlag;
	}

	public Integer getHotFlag() {
		return hotFlag;
	}

	public void setOfficeFlag(Integer officeFlag) {
		this.officeFlag = officeFlag;
	}

	public Integer getOfficeFlag() {
		return officeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public Integer getAttachmentNum() {
		return attachmentNum;
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

	public String getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(String collectFlag) {
		this.collectFlag = collectFlag;
	}

	public String getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(String likeFlag) {
		this.likeFlag = likeFlag;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

//	public String getIocUrl() {
//		return iocUrl;
//	}
//
//	public void setIocUrl(String iocUrl) {
//		this.iocUrl = iocUrl;
//	}
//
//	public String getClientIocUrl() {
//		return clientIocUrl;
//	}
//
//	public void setClientIocUrl(String clientIocUrl) {
//		this.clientIocUrl = clientIocUrl;
//	}

}
