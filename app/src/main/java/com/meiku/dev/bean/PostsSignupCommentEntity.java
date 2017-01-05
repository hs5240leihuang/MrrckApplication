package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：活动贴报名作品评论entity 类名称：com.mrrck.ap.bd.entity.PostsSignupCommentEntity
 * 创建人：仲崇生 创建时间：2015-11-6 上午11:59:02
 * 
 * @version V1.0
 */
public class PostsSignupCommentEntity extends MkPostsSignupComment {
	
	private String clientThumbHeadPicUrl;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	/** 社区版块编号 */
	private Integer boardId;

	/** 每页条数 */
	private Integer pageNum;

	/** 分页页数 */
	private Integer offset;

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

	private String positionName;

	private String fPositionName;

	/** 终端展示时间 */
	private String clientCreateDate;

	/** 图片多媒体路径(客户端使用) */
	private String clientFileUrl;

	public Integer getBoardId() {
		return this.boardId;
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

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
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

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public String getClientFHeadPicUrl() {
		return clientFHeadPicUrl;
	}

	public String getClientCreateDate() {
		return clientCreateDate;
	}

	public String getClientFileUrl() {
		return clientFileUrl;
	}

}
