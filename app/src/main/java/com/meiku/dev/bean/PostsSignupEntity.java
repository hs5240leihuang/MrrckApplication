package com.meiku.dev.bean;

import java.io.Serializable;
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
public class PostsSignupEntity extends MkPostsSignup implements Serializable {
	
	private String clientThumbHeadPicUrl;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	/** 主键编号 重命名 */
	private Integer signupId;

	/** 页码 */
	private Integer offset;

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

	/** 是否投票状态 0:为投过该作品 1:已投过该作品 */
	private String voteStatus;

	/** 活动贴报名作品评论列表 */
	private List<PostsSignupCommentEntity> postsSignupCommentList;

	/** 音视频长度（秒数）附件类型是1,2有值 */
	private Integer fileSeconds;

	/** 附件路径(客户端使用,附件路径(封面图片OR视频缩略图)) */
	private String clientFileUrl;

	/** 附件路径(客户端使用,被压缩封面图片OR视频缩略图) */
	private String clientPhotoFileUrl;

	/** 终端展示时间 */
	private String clientCreateDate;

	/** 比赛排名号 */
	private Integer rankNum;

	/** 用户对应岗位名称 */
	private String positionName;

	/** hot 标识 0:不是 1:是 大于1000票是HOT */
	private Integer hotFlag;

	/** 附件数据列表 */
	private List<UserAttachmentEntity> attachmentList;

	/** 是否点赞状态 0:未点赞该作品 1:已点赞该作品 */
	private String likeStatus;

	/** 作品分享详情网页URL */
	private String postsSignupShareUrl;

	/** 作品类别对应中文名称 */
	private String categoryName;

	/** 是否允许删除作品 0:不允许删除 1:允许删除 */
	private String isDelete;

	/** 赛事贴名称 */
	private String matchPostsName;

	/** 作品编号,多个逗号分隔 */
	private String signupIds;

	/** 年月日 时间显示 */
	private String clientDate;

	/** 人气票数(其他渠道投票) */
	private Integer otherVoteNum;
	/** 总投票数 */
	private Integer totalVoteNum;
	
	public Integer getSignupId() {
		return this.signupId;
	}

	public void setSignupId(Integer signupId) {
		this.signupId = signupId;
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

	public String getClientHeadPicUrl() {

		return this.clientHeadPicUrl;

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

	public String getVoteFlag() {
		return this.voteFlag;
	}

	public void setVoteFlag(String voteFlag) {
		this.voteFlag = voteFlag;
	}

	public String getVoteStatus() {
		return this.voteStatus;
	}

	public void setVoteStatus(String voteStatus) {
		this.voteStatus = voteStatus;
	}

	public List<PostsSignupCommentEntity> getPostsSignupCommentList() {
		return this.postsSignupCommentList;
	}

	public void setPostsSignupCommentList(
			List<PostsSignupCommentEntity> postsSignupCommentList) {
		this.postsSignupCommentList = postsSignupCommentList;
	}

	public Integer getFileSeconds() {
		return this.fileSeconds;
	}

	public void setFileSeconds(Integer fileSeconds) {
		this.fileSeconds = fileSeconds;
	}

	public String getClientFileUrl() {

		return this.clientFileUrl;

	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public String getClientPhotoFileUrl() {

		return this.clientPhotoFileUrl;

	}

	public void setClientPhotoFileUrl(String clientPhotoFileUrl) {
		this.clientPhotoFileUrl = clientPhotoFileUrl;
	}

	public String getClientCreateDate() {

		return this.clientCreateDate;

	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public Integer getRankNum() {
		return this.rankNum;
	}

	public void setRankNum(Integer rankNum) {
		this.rankNum = rankNum;
	}

	public String getPositionName() {
		return this.positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public Integer getHotFlag() {

		return this.hotFlag;

	}

	public void setHotFlag(Integer hotFlag) {
		this.hotFlag = hotFlag;
	}

	public List<UserAttachmentEntity> getAttachmentList() {
		return this.attachmentList;
	}

	public void setAttachmentList(List<UserAttachmentEntity> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getLikeStatus() {
		return this.likeStatus;
	}

	public void setLikeStatus(String likeStatus) {
		this.likeStatus = likeStatus;
	}

	public String getPostsSignupShareUrl() {

		return this.postsSignupShareUrl;

	}

	public void setPostsSignupShareUrl(String postsSignupShareUrl) {
		this.postsSignupShareUrl = postsSignupShareUrl;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getMatchPostsName() {
		return this.matchPostsName;
	}

	public void setMatchPostsName(String matchPostsName) {
		this.matchPostsName = matchPostsName;
	}

	public String getSignupIds() {
		return this.signupIds;
	}

	public void setSignupIds(String signupIds) {
		this.signupIds = signupIds;
	}

	public String getClientDate() {
		return this.clientDate;
	}

	public void setClientDate(String clientDate) {
		this.clientDate = clientDate;
	}

	public Integer getOtherVoteNum() {
		return otherVoteNum;
	}

	public void setOtherVoteNum(Integer otherVoteNum) {
		this.otherVoteNum = otherVoteNum;
	}

	public Integer getTotalVoteNum() {
		return totalVoteNum;
	}

	public void setTotalVoteNum(Integer totalVoteNum) {
		this.totalVoteNum = totalVoteNum;
	}

}
