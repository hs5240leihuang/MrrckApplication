package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：收藏作品Entity 类名称：com.mrrck.ap.bd.entity.PostsSignupCollectEntity 创建人：吉陈明
 * 创建时间：2016-2-19 下午04:47:22
 * 
 * @version V1.0
 */
public class PostsSignupCollectEntity extends MkPostsSignupCollect {

	/** 收藏编号 重命名 */
	private Integer signupCollectId;

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 收藏编号,多个逗号分隔 */
	private String signupCollectIds;

	/** 帖子编号 */
	private Integer postsId;

	/** 作品名 */
	private String name;

	/** 描述 */
	private String remark;

	/** 比赛类别编号 */
	private Integer categoryId;

	/** 省份code */
	private Integer provinceCode;

	/** 城市code */
	private Integer cityCode;

	/** 附件数量 0:没有 其他数值 */
	private Integer attachmentNum;

	/** 票数 */
	private Integer voteNum;

	/** 评论数 */
	private Integer commentNum;

	/** 阅读数 */
	private Integer viewNum;

	/** 点赞数 */
	private Integer likeNum;

	/** 作品标识: 0:普通作品 1:比赛作品 */
	private Integer worksFlag;

	/** 作品附件类型: 0:图片 1:视频 默认0 */
	private Integer fileType;

	/** 封面图片路径(新增) */
	private String fileUrl;

	/** 用户头像 */
	private String headPicUrl;

	/** 用户昵称 */
	private String nickName;

	/** 用户头像(客户端使用) */
	private String clientHeadPicUrl;

	/** 附件路径(客户端使用,附件路径(封面图片OR视频缩略图)) */
	private String clientFileUrl;

	/** 附件路径(客户端使用,被压缩封面图片OR视频缩略图) */
	private String clientThumbFileUrl;

	/** 用户对应岗位名称 */
	private String positionName;

	private boolean isSelected;

	/** 人气票数(其他渠道投票) */
	private Integer otherVoteNum;
	/** 总投票数 */
	private Integer totalVoteNum;
	
	public Integer getSignupCollectId() {
		return signupCollectId;
	}

	public void setSignupCollectId(Integer signupCollectId) {
		this.signupCollectId = signupCollectId;
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

	public String getSignupCollectIds() {
		return signupCollectIds;
	}

	public void setSignupCollectIds(String signupCollectIds) {
		this.signupCollectIds = signupCollectIds;
	}

	public Integer getPostsId() {
		return postsId;
	}

	public void setPostsId(Integer postsId) {
		this.postsId = postsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getAttachmentNum() {
		return attachmentNum;
	}

	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public Integer getVoteNum() {
		return voteNum;
	}

	public void setVoteNum(Integer voteNum) {
		this.voteNum = voteNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getWorksFlag() {
		return worksFlag;
	}

	public void setWorksFlag(Integer worksFlag) {
		this.worksFlag = worksFlag;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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

	public String getClientFileUrl() {
		return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public String getClientThumbFileUrl() {
		return clientThumbFileUrl;
	}

	public void setClientThumbFileUrl(String clientThumbFileUrl) {
		this.clientThumbFileUrl = clientThumbFileUrl;
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
