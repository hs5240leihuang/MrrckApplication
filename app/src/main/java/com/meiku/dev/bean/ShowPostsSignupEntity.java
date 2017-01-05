package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：活动贴报名作品 Entity 类名称：com.mrrck.ap.bd.entity.PostsSignupEntity 创建人：仲崇生
 * 创建时间：2015-11-2 下午04:11:20
 * 
 * @version V1.0
 */
public class ShowPostsSignupEntity extends ShowMkPostsSignup {
	
	private String clientThumbHeadPicUrl;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	/** 主键编号 重命名 */
	private Integer signupId;

	/** 作品类别名称 */
	private String categoryName;

	/** 页码 */
	private Integer offset;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/** 每页条数 */
	private Integer pageNum;

	/** 用户头像 */
	private String headPicUrl;

	/** 用户昵称 */
	private String nickName;

	/** 用户头像(客户端使用) */
	private String clientHeadPicUrl;

	/** 被关注数(粉丝数) */
	private Integer toLikeNum;

	/** 投票状态 0:投票尚未开始 1:投票进行中 2:投票已结束 */
	private String voteFlag;

	/** 是否投票状态 0:未投过该作品 1:已投过该作品 */
	private String voteStatus;
	/** 是否点赞 0:未点赞 1:已点赞 */
	private String likeStatus;

	/** 活动贴报名作品评论列表 */
	private List<PostsSignupCommentEntity> postsSignupCommentList;

	/** 音视频长度（秒数）附件类型是1,2有值 */
	private Integer fileSeconds;

	/** 附件路径(客户端使用,video路径) */
	private String clientFileUrl;

	/** 附件路径(客户端使用,video缩略图路径) */
	private String clientPhotoFileUrl;

	/** 终端展示时间 */
	private String clientCreateDate;

	/** 比赛排名号 */
	private Integer rankNum;

	/** 用户对应岗位名称 */
	private String positionName;

	/** 附件数据列表 */
	private List<UserAttachmentEntity> attachmentList;
	/** 分享路径 */
	private String postsSignupShareUrl;

	private boolean isSelected;
	
	

	public String getPostsSignupShareUrl() {
		return postsSignupShareUrl;
	}

	public void setPostsSignupShareUrl(String postsSignupShareUrl) {
		this.postsSignupShareUrl = postsSignupShareUrl;
	}

	public List<UserAttachmentEntity> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<UserAttachmentEntity> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public Integer getSignupId() {
		return signupId;
	}

	public void setSignupId(Integer signupId) {
		this.signupId = signupId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public Integer getToLikeNum() {
		return toLikeNum;
	}

	public void setToLikeNum(Integer toLikeNum) {
		this.toLikeNum = toLikeNum;
	}

	public String getVoteFlag() {
		return voteFlag;
	}

	public void setVoteFlag(String voteFlag) {
		this.voteFlag = voteFlag;
	}

	public String getVoteStatus() {
		return voteStatus;
	}

	public void setVoteStatus(String voteStatus) {
		this.voteStatus = voteStatus;
	}

	public String getLikeStatus() {
		return likeStatus;
	}

	public void setLikeStatus(String likeStatus) {
		this.likeStatus = likeStatus;
	}

	public List<PostsSignupCommentEntity> getPostsSignupCommentList() {
		return postsSignupCommentList;
	}

	public void setPostsSignupCommentList(
			List<PostsSignupCommentEntity> postsSignupCommentList) {
		this.postsSignupCommentList = postsSignupCommentList;
	}

	public Integer getFileSeconds() {
		return fileSeconds;
	}

	public void setFileSeconds(Integer fileSeconds) {
		this.fileSeconds = fileSeconds;
	}

	public String getClientFileUrl() {
		return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public String getClientPhotoFileUrl() {
		return clientPhotoFileUrl;
	}

	public void setClientPhotoFileUrl(String clientPhotoFileUrl) {
		this.clientPhotoFileUrl = clientPhotoFileUrl;
	}

	public String getClientCreateDate() {
		return clientCreateDate;
	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public Integer getRankNum() {
		return rankNum;
	}

	public void setRankNum(Integer rankNum) {
		this.rankNum = rankNum;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
