package com.meiku.dev.bean;

import java.util.List;

public class BoardBean {
	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 功能菜单编号 */
	private Integer menuId;

 	/** 版块编号 */
	private Integer boardId;

 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除标识 0:正常 1:删除 */
	private Integer delStatus;
	
	 /**分页页数*/
    private Integer offset;
    
    /**每页条数*/
    private Integer pageNum;
    
    /**版块图片(客户端使用)*/
    private String clientImgUrl;
    
    /** 版块创建者用户编号 */
    private Integer boardUserId;
    
    /** 版名 */
    private String name;

    /** 描述 */
    private String remark;

    /** 岗位分类 */
    private Integer positionId;

    /** 省份code */
    private Integer provinceCode;

    /** 城市code */
    private Integer cityCode;

    /** 图片URL */
    private String imgUrl;

    /** 帖子数 */
    private Integer postsNum;

    /** 评论数 */
    private Integer commentNum;

    /** 刷新时间 */
    private String refreshDate;

    /** 置顶 : 0不置顶 1置顶 */
    private Integer topFlag;

    /** 推荐标识: 0普通 1精华 */
    private Integer goodFlag;

    /** 热门: 0普通 1热门 */
    private Integer hotFlag;

    /** 数据来源: 0:普通(用户自创建) 1:官方  默认:0 */
    private Integer officeFlag;
    
    /** 是否默认显示数据 0不是 1是 默认0 */
    private Integer defaultFlag;
    
    /** 审核状态:0待审核1:审核通过2:审核未通过 */
    private Integer approveStatus;

    /** 审核未通过理由 */
    private String refuseReason;
    
    private String clientThumbHeadPicUrl;
    
    

    public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	/** 审核人编号 */
    private Integer approveUserId;
    
    private List<UserDto> userList;
 
    
    public List<UserDto> getUserList() {
		return userList;
	}

	public void setUserList(List<UserDto> userList) {
		this.userList = userList;
	}

	/**  帖子列表 */
    private List<PostsEntity> postsList;
    
 

 
    
	/** 是否已添加 0未添加 1已添加 */
	private Integer addFlag;
	
 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getBoardId() {
		return boardId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
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

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
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

	public String getClientImgUrl() {
		return clientImgUrl;
	}

	public void setClientImgUrl(String clientImgUrl) {
		this.clientImgUrl = clientImgUrl;
	}

	public Integer getBoardUserId() {
		return boardUserId;
	}

	public void setBoardUserId(Integer boardUserId) {
		this.boardUserId = boardUserId;
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

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getPostsNum() {
		return postsNum;
	}

	public void setPostsNum(Integer postsNum) {
		this.postsNum = postsNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public String getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(String refreshDate) {
		this.refreshDate = refreshDate;
	}

	public Integer getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(Integer topFlag) {
		this.topFlag = topFlag;
	}

	public Integer getGoodFlag() {
		return goodFlag;
	}

	public void setGoodFlag(Integer goodFlag) {
		this.goodFlag = goodFlag;
	}

	public Integer getHotFlag() {
		return hotFlag;
	}

	public void setHotFlag(Integer hotFlag) {
		this.hotFlag = hotFlag;
	}

	public Integer getOfficeFlag() {
		return officeFlag;
	}

	public void setOfficeFlag(Integer officeFlag) {
		this.officeFlag = officeFlag;
	}

	public Integer getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Integer defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Integer getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(Integer approveUserId) {
		this.approveUserId = approveUserId;
	}
 
 
 

	public Integer getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(Integer addFlag) {
		this.addFlag = addFlag;
	}

	public List<PostsEntity> getPostsList() {
		return postsList;
	}

	public void setPostsList(List<PostsEntity> postsList) {
		this.postsList = postsList;
	}
 
    
    
}
