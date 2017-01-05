package com.meiku.dev.bean;

import java.io.Serializable;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：帖子评论entity 类名称：com.mrrck.ap.bd.entity.PostsCommentEntity 创建人：韩非
 * 创建时间：2015-11-2 下午02:25:09
 * 
 * @version V1.0
 */
public class PostsCommentEntity extends MkPostsComment implements Serializable{
	
	private String clientThumbHeadPicUrl;
	
	private String clientThumbFHeadPicUrl;
	
	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public String getClientThumbFHeadPicUrl() {
		return clientThumbFHeadPicUrl;
	}

	public void setClientThumbFHeadPicUrl(String clientThumbFHeadPicUrl) {
		this.clientThumbFHeadPicUrl = clientThumbFHeadPicUrl;
	}

	/** 社区版块编号 */
	private Integer boardId;

	/** 每页条数 */
	private Integer pageNum;

	/** 分页页数 */
	private Integer offset;

	//private PostsCommentEntity toCommentEntity;

	/** 评论用户头像 */
	private String headPicUrl;

	/** 评论用户头像(供客户端使用) */
	private String clientHeadPicUrl;

	/** 评论用户别名 */
	private String nickName;

	/** 父级评论编号 */
	private Integer fId;

	/** 父级评论用户编号 */
	private Integer fUserId;

	/** 父级评论用户头像 */
	private String fHeadPicUrl;

	/** 父级评论用户头像(供客户端使用) */
	private String clientFHeadPicUrl;

	/** 父级评论用户别名 */
	private String fNickName;

	/** 父级评论类容 */
	private String fContent;
	
	/** 父级评论楼层号 */
    private String fFloorNum;

	private String positionName;

	private String fPositionName;
	/** 终端展示时间 */
	private String clientCreateDate;

	/** 图片多媒体路径(客户端使用) */
	private String clientFileUrl;
	
	/** 被关注数(粉丝数) */
    private Integer toLikeNum;
    
    /** 父级评论被关注数(粉丝数) */
    private Integer fToLikeNum;
    
    /**作品比赛排名号*/
    private Integer rankNum;
    
    private Integer floorNum;
    
	public Integer getBoardId() {
		return boardId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

//	public PostsCommentEntity getToCommentEntity() {
//		return toCommentEntity;
//	}
//
//	public void setToCommentEntity(PostsCommentEntity toCommentEntity) {
//		this.toCommentEntity = toCommentEntity;
//	}

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getfId() {
		return fId;
	}

	public void setfId(Integer fId) {
		this.fId = fId;
	}

	public Integer getfUserId() {
		return fUserId;
	}

	public void setfUserId(Integer fUserId) {
		this.fUserId = fUserId;
	}

	public String getfHeadPicUrl() {
		return fHeadPicUrl;
	}

	public void setfHeadPicUrl(String fHeadPicUrl) {
		this.fHeadPicUrl = fHeadPicUrl;
	}

	public String getClientFHeadPicUrl() {
		return clientFHeadPicUrl;
	}

	public void setClientFHeadPicUrl(String clientFHeadPicUrl) {
		this.clientFHeadPicUrl = clientFHeadPicUrl;
	}

	public String getfNickName() {
		return fNickName;
	}

	public void setfNickName(String fNickName) {
		this.fNickName = fNickName;
	}

	public String getfContent() {
		return fContent;
	}

	public void setfContent(String fContent) {
		this.fContent = fContent;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getfPositionName() {
		return fPositionName;
	}

	public void setfPositionName(String fPositionName) {
		this.fPositionName = fPositionName;
	}

	public String getClientCreateDate() {
		return clientCreateDate;
	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public String getClientFileUrl() {
		return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public Integer getToLikeNum() {
		return toLikeNum;
	}

	public void setToLikeNum(Integer toLikeNum) {
		this.toLikeNum = toLikeNum;
	}

	public Integer getfToLikeNum() {
		return fToLikeNum;
	}

	public void setfToLikeNum(Integer fToLikeNum) {
		this.fToLikeNum = fToLikeNum;
	}

	public Integer getRankNum() {
		return rankNum;
	}

	public void setRankNum(Integer rankNum) {
		this.rankNum = rankNum;
	}

	public String getfFloorNum() {
		return fFloorNum;
	}

	public void setfFloorNum(String fFloorNum) {
		this.fFloorNum = fFloorNum;
	}

	public Integer getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(Integer floorNum) {
		this.floorNum = floorNum;
	}

}
