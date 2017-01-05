package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：个人主页 类名称：com.mrrck.ap.uh.entity.UserHomeEntity 创建人：韩非 创建时间：2015-10-30
 * 上午11:30:38
 * 
 * @version V1.0
 */
public class UserHomeEntity extends MkUser {
	/** 备注名(是好友关系时使用) */
	private String aliasName;
	/** 用户主页背景图片 (客户端使用) */
	private String clientBackgroundId;
	/** 用户主页背景图片 缩略图(客户端使用) */
	private String clientThumbBackgroundId;
	private Integer flag;// 标识查看自己还是别人主页
	// 视频list
	private List<UserVedioEntity> vedioList;
	// 相册list
	private List<UserImageEntity> imageList;
	// 帖子list
	private List<PostsEntity> postsList;

	private List<GroupEntity> groupList;

	public List<GroupEntity> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupEntity> groupList) {
		this.groupList = groupList;
	}

	public String getClientBackgroundId() {
		return clientBackgroundId;
	}

	public void setClientBackgroundId(String clientBackgroundId) {
		this.clientBackgroundId = clientBackgroundId;
	}

	public String getClientThumbBackgroundId() {
		return clientThumbBackgroundId;
	}

	public void setClientThumbBackgroundId(String clientThumbBackgroundId) {
		this.clientThumbBackgroundId = clientThumbBackgroundId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	private Integer vedioNum;// 视频数
	private Integer postsNum;// 贴子数
	private Integer imagesNum;// 相册数

	private Integer isView;// 是否被关注

	// private String positionName;//岗位名
	private Integer toUserId;// 别人id
	private Integer boardNum;// 社区数
	private Integer likeNum;// 关注数
	private Integer toLikeNum;// 粉丝数

	private String cityName;// 城市名称
	// 客户端用用户头像
	// public String clientHeadPicUrl;

	private Integer age;// 年龄

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 是否允许陌生人关注我 0允许 1不允许 */
	private Integer toLikeMeFlag;

	/** 是否允许陌生人查看我的主页 0允许 1不允许 */
	private Integer viewFlag;

	/** 是否允许陌生人查看我的收藏 0允许 1不允许 */
	private Integer collectFlag;

	/** 是否允许陌生人查看我加入的同岗私聊群 0允许 1不允许 */
	private Integer chatFlag;

	/** 是否允许陌生人查看我的关注 0允许 1不允许 */
	private Integer likeFlag;

	/** 是否允许陌生人查看我的粉丝 0允许 1不允许 */
	private Integer toLikeFlag;

	/** 是否允许陌生人查看我的帖子 0允许 1不允许 */
	private Integer postsFlag;

	/** 是否允许陌生人查看我的相册 0允许 1不允许 */
	private Integer photoFlag;

	/** 是否允许陌生人查看我的视频 0允许 1不允许 */
	private Integer videoFlag;

	/** 用户附件List */
	private List<UserAttachmentEntity> userAttachmentList;

	/** 用户荣誉List */
	private List<UserHonorEntity> userHonorList;

	/** 获得中心经纬度与目标经纬度的距离 */
	private double distance;

	/** 发布时间与当前时间的间隔 */
	private String intervalTime;

	/** 是否是好友关系 0:不是 1:是 */
	private String friendFlag;

	/** 获得中心经纬度与目标经纬度的距离 */
	private String kmDistance;

	/** (用户个人相册数据List) */
	private List<PersonalPhotoEntity> personalPhotoList;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public List<PostsEntity> getPostsList() {
		return postsList;
	}

	public void setPostsList(List<PostsEntity> postsList) {
		this.postsList = postsList;
	}

	public Integer getVedioNum() {
		return vedioNum;
	}

	public void setVedioNum(Integer vedioNum) {
		this.vedioNum = vedioNum;
	}

	public Integer getPostsNum() {
		return postsNum;
	}

	public void setPostsNum(Integer postsNum) {
		this.postsNum = postsNum;
	}

	public Integer getImagesNum() {
		return imagesNum;
	}

	public void setImagesNum(Integer imagesNum) {
		this.imagesNum = imagesNum;
	}

	public Integer getIsView() {
		return isView;
	}

	public void setIsView(Integer isView) {
		this.isView = isView;
	}

	// public String getPositionName() {
	// return positionName;
	// }
	//
	// public void setPositionName(String positionName) {
	// this.positionName = positionName;
	// }

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getBoardNum() {
		return boardNum;
	}

	public void setBoardNum(Integer boardNum) {
		this.boardNum = boardNum;
	}

	public Integer getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getToLikeNum() {
		return toLikeNum;
	}

	public void setToLikeNum(Integer toLikeNum) {
		this.toLikeNum = toLikeNum;
	}

	// public String getClientHeadPicUrl() {
	//
	// return this.clientHeadPicUrl;
	//
	// }
	//
	// public void setClientHeadPicUrl(String clientHeadPicUrl) {
	// this.clientHeadPicUrl = clientHeadPicUrl;
	// }

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public Integer getAge() {

		return this.age;

	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getToLikeMeFlag() {
		return this.toLikeMeFlag;
	}

	public void setToLikeMeFlag(Integer toLikeMeFlag) {
		this.toLikeMeFlag = toLikeMeFlag;
	}

	public Integer getViewFlag() {
		return this.viewFlag;
	}

	public void setViewFlag(Integer viewFlag) {
		this.viewFlag = viewFlag;
	}

	public Integer getCollectFlag() {
		return this.collectFlag;
	}

	public void setCollectFlag(Integer collectFlag) {
		this.collectFlag = collectFlag;
	}

	public Integer getChatFlag() {
		return this.chatFlag;
	}

	public void setChatFlag(Integer chatFlag) {
		this.chatFlag = chatFlag;
	}

	public Integer getLikeFlag() {
		return this.likeFlag;
	}

	public void setLikeFlag(Integer likeFlag) {
		this.likeFlag = likeFlag;
	}

	public Integer getToLikeFlag() {
		return this.toLikeFlag;
	}

	public void setToLikeFlag(Integer toLikeFlag) {
		this.toLikeFlag = toLikeFlag;
	}

	public Integer getPostsFlag() {
		return this.postsFlag;
	}

	public void setPostsFlag(Integer postsFlag) {
		this.postsFlag = postsFlag;
	}

	public Integer getPhotoFlag() {
		return this.photoFlag;
	}

	public void setPhotoFlag(Integer photoFlag) {
		this.photoFlag = photoFlag;
	}

	public Integer getVideoFlag() {
		return this.videoFlag;
	}

	public void setVideoFlag(Integer videoFlag) {
		this.videoFlag = videoFlag;
	}

	public List<UserAttachmentEntity> getUserAttachmentList() {
		return this.userAttachmentList;
	}

	public void setUserAttachmentList(
			List<UserAttachmentEntity> userAttachmentList) {
		this.userAttachmentList = userAttachmentList;
	}

	public double getDistance() {
		return this.distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getIntervalTime() {

		return this.intervalTime;

	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getFriendFlag() {
		return this.friendFlag;
	}

	public void setFriendFlag(String friendFlag) {
		this.friendFlag = friendFlag;
	}

	public List<UserVedioEntity> getVedioList() {
		return vedioList;
	}

	public void setVedioList(List<UserVedioEntity> vedioList) {
		this.vedioList = vedioList;
	}

	public List<UserImageEntity> getImageList() {
		return imageList;
	}

	public void setImageList(List<UserImageEntity> imageList) {
		this.imageList = imageList;
	}

	public List<UserHonorEntity> getUserHonorList() {
		return userHonorList;
	}

	public void setUserHonorList(List<UserHonorEntity> userHonorList) {
		this.userHonorList = userHonorList;
	}

	public String getKmDistance() {
		return kmDistance;
	}

	public void setKmDistance(String kmDistance) {
		this.kmDistance = kmDistance;
	}

	public List<PersonalPhotoEntity> getPersonalPhotoList() {
		return personalPhotoList;
	}

	public void setPersonalPhotoList(List<PersonalPhotoEntity> personalPhotoList) {
		this.personalPhotoList = personalPhotoList;
	}

}
