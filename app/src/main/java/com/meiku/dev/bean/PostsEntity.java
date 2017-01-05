package com.meiku.dev.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：帖子实体 类名称：com.mrrck.ap.bd.entity.BoardEntity 创建人：曙光 创建时间：2015-10-26
 * 下午03:06:19
 * 
 * @version V1.0
 */
public class PostsEntity extends MkPosts implements Serializable {

	/** 帖子附件集合 用于列表展示 2016-07-07 16:37:23 jsg 添加 */
	private List<PostsAttachment> postsAttachmentList;

	public List<PostsAttachment> getPostsAttachmentList() {
		return postsAttachmentList;
	}

	public void setPostsAttachmentList(List<PostsAttachment> postsAttachmentList) {
		this.postsAttachmentList = postsAttachmentList;
	}

	private String clientThumbHeadPicUrl;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	// 发布作品顶部广告
	private String clientRecommendImgUrl;

	public String getClientRecommendImgUrl() {
		return clientRecommendImgUrl;
	}

	public void setClientRecommendImgUrl(String clientRecommendImgUrl) {
		this.clientRecommendImgUrl = clientRecommendImgUrl;
	}

	// '全国code(固定值100000)'
	private int wholeCode;
	// '行政区划编码(省份code,多个逗号分隔)',
	private String activeProvinceCode;
	// 城市code 多个逗号分隔
	private String activeCityCode;

	// 比赛分类id 逗号分隔
	private String categoryId;

	private String menuId;
	/** 赛事帖子分享详情网页URL */
	private String postsGuideShareUrl;

	public int getWholeCode() {
		return wholeCode;
	}

	public void setWholeCode(int wholeCode) {
		this.wholeCode = wholeCode;
	}

	public String getActiveProvinceCode() {
		return activeProvinceCode;
	}

	public void setActiveProvinceCode(String activeProvinceCode) {
		this.activeProvinceCode = activeProvinceCode;
	}

	public String getActiveCityCode() {
		return activeCityCode;
	}

	public void setActiveCityCode(String activeCityCode) {
		this.activeCityCode = activeCityCode;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/** 主键编号 重命名 */
	private Integer postsId;

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 评论数据列表 */
	private List<PostsCommentEntity> commentList;

	/** 用户头像 */
	private String headPicUrl;

	/** 用户昵称 */
	private String nickName;

	/** 用户性别 */
	private String gender;

	/** 用户头像(客户端使用) */
	private String clientHeadPicUrl;

	/** 被关注数(粉丝数) */
	private Integer toLikeNum;

	/** 活动投票开始时间 */
	private String startDate;

	/** 活动投票结束时间 */
	private String endDate;

	/** 报名开始时间 */
	private String applyStartDate;

	/** 报名截止时间 */
	private String applyEndDate;

	/** 已报名人数 */
	private Integer applyNum;

	/** 活动最大上限人数 */
	private Integer applyMaxNum;

	/** 终端展示的时间 */
	private String clientCreateDate;

	/** 发布时间与当前时间的间隔 */
	private String intervalTime;

	/** 活动贴参赛作品列表 */
	private List<PostsSignupEntity> postsSignupList;

	/** 附件数据列表 */
	private List<UserAttachmentEntity> attachmentList;

	private Integer postsTotal;// 帖子回复总数

	/** 报名状态 0:报名尚未开始 1:报名进行中 2:报名已结束 */
	private String signupFlag;

	/** 投票状态 0:投票尚未开始 1:投票进行中 2:投票已结束 */
	private String voteFlag;

	/** 活动贴图片 */
	private String imgUrl;

	/** 活动贴图片(客户端使用) */
	private String clientImgUrl;

	/** 帖子分享详情网页URL */
	private String postsShareUrl;

	/** 帖子投票是0图片还是视频1 */
	private String activeType;
	private Integer matchCityId;
	private Integer matchId;
	private String matchYear;
	private String matchMonth;

	public Integer getMatchCityId() {
		return matchCityId;
	}

	public void setMatchCityId(Integer matchCityId) {
		this.matchCityId = matchCityId;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public String getMatchYear() {
		return matchYear;
	}

	public void setMatchYear(String matchYear) {
		this.matchYear = matchYear;
	}

	public String getMatchMonth() {
		return matchMonth;
	}

	public void setMatchMonth(String matchMonth) {
		this.matchMonth = matchMonth;
	}

	public Integer getPostsId() {
		return this.postsId;
	}

	public void setPostsId(Integer postsId) {
		this.postsId = postsId;
	}

	public List<PostsCommentEntity> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<PostsCommentEntity> commentList) {
		this.commentList = commentList;
	}

	public Integer getOffset() {
		return this.offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return this.pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getHeadPicUrl() {
		return this.headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public Integer getToLikeNum() {
		return this.toLikeNum;
	}

	public void setToLikeNum(Integer toLikeNum) {
		this.toLikeNum = toLikeNum;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getApplyStartDate() {
		return this.applyStartDate;
	}

	public void setApplyStartDate(String applyStartDate) {
		this.applyStartDate = applyStartDate;
	}

	public String getApplyEndDate() {
		return this.applyEndDate;
	}

	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	public Integer getApplyNum() {
		return this.applyNum;
	}

	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}

	public Integer getApplyMaxNum() {
		return this.applyMaxNum;
	}

	public void setApplyMaxNum(Integer applyMaxNum) {
		this.applyMaxNum = applyMaxNum;
	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public List<PostsSignupEntity> getPostsSignupList() {
		return this.postsSignupList;
	}

	public void setPostsSignupList(List<PostsSignupEntity> postsSignupList) {
		this.postsSignupList = postsSignupList;
	}

	public List<UserAttachmentEntity> getAttachmentList() {
		return this.attachmentList;
	}

	public void setAttachmentList(List<UserAttachmentEntity> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public Integer getPostsTotal() {
		return postsTotal;
	}

	public void setPostsTotal(Integer postsTotal) {
		this.postsTotal = postsTotal;
	}

	public String getSignupFlag() {
		return this.signupFlag;
	}

	public void setSignupFlag(String signupFlag) {
		this.signupFlag = signupFlag;
	}

	public String getVoteFlag() {
		return this.voteFlag;
	}

	public void setVoteFlag(String voteFlag) {
		this.voteFlag = voteFlag;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setClientImgUrl(String clientImgUrl) {
		this.clientImgUrl = clientImgUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public String getClientCreateDate() {
		return clientCreateDate;
	}

	public String getIntervalTime() {
		return intervalTime;
	}

	public String getClientImgUrl() {
		return clientImgUrl;
	}

	public String getPostsShareUrl() {
		return postsShareUrl;
	}

	public void setPostsShareUrl(String postsShareUrl) {
		this.postsShareUrl = postsShareUrl;
	}

	public String getActiveType() {
		return activeType;
	}

	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}

	public String getPostsGuideShareUrl() {
		return postsGuideShareUrl;
	}

	public void setPostsGuideShareUrl(String postsGuideShareUrl) {
		this.postsGuideShareUrl = postsGuideShareUrl;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}
