package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：活动贴报名作品表 类名称：com.mrrck.db.entity.MkPostsSignup 创建人：仲崇生 创建时间：2015-10-26
 * 下午02:01:27
 * 
 * @version V1.0
 */
public class ShowMkPostsSignup {

	/** 编号 */
	private Integer id;

	/** 用户编号 */
	private Integer userId;

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

	/** 评论数 */
	private Integer commentNum;

	/** 阅读数 */
	private Integer viewNum;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除状态0正常1:逻辑删除 */
	private Integer delStatus;

	/** 点赞数 */
	private Integer likeNum;

	/** 作品标识: 0:普通作品 1:比赛作品 */
	private Integer worksFlag;

	/** 作品附件类型: 0:图片 1:视频 默认0 */
	private Integer fileType;

	/** 封面图片路径(新增) */
	private String fileUrl;

	/** 姓名(基本信息) */
	private String userName;

	/** 性别 1:男 2:女(基本信息) */
	private String gender;

	/** 出生年月(基本信息) */
	private String birthDate;

	/** 手机号码(基本信息) */
	private String phone;

	/** 从业年限(基本信息) */
	private String workAge;

	/** 工作单位(基本信息) */
	private String workCompany;

	/** 作品编号 如:01088 */
	private String signupNo;

	/** hot 标识 0:不是 1:是 大于1000票是HOT */
	private Integer hotFlag;
	private String matchPostsName;

	/** 同行票数 */
	private Integer voteNum;
	/** 人气票数(其他渠道投票) */
	private Integer otherVoteNum;
	/** 总投票数 */
	private Integer totalVoteNum;
	/** 所在赛区 */
	private String signupArea;

	public String getMatchPostsName() {
		return matchPostsName;
	}

	public void setMatchPostsName(String matchPostsName) {
		this.matchPostsName = matchPostsName;
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

	public void setPostsId(Integer postsId) {
		this.postsId = postsId;
	}

	public Integer getPostsId() {
		return postsId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCategoryId() {
		return categoryId;
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

	public void setAttachmentNum(Integer attachmentNum) {
		this.attachmentNum = attachmentNum;
	}

	public Integer getAttachmentNum() {
		return attachmentNum;
	}

	public void setVoteNum(Integer voteNum) {
		this.voteNum = voteNum;
	}

	public Integer getVoteNum() {
		return voteNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getViewNum() {
		return viewNum;
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

	public Integer getLikeNum() {
		return this.likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Integer getWorksFlag() {
		return this.worksFlag;
	}

	public void setWorksFlag(Integer worksFlag) {
		this.worksFlag = worksFlag;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWorkAge() {
		return this.workAge;
	}

	public void setWorkAge(String workAge) {
		this.workAge = workAge;
	}

	public String getWorkCompany() {
		return this.workCompany;
	}

	public void setWorkCompany(String workCompany) {
		this.workCompany = workCompany;
	}

	public String getSignupNo() {
		return this.signupNo;
	}

	public void setSignupNo(String signupNo) {
		this.signupNo = signupNo;
	}

	public Integer getHotFlag() {
		return hotFlag;
	}

	public void setHotFlag(Integer hotFlag) {
		this.hotFlag = hotFlag;
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

	public String getSignupArea() {
		return signupArea;
	}

	public void setSignupArea(String signupArea) {
		this.signupArea = signupArea;
	}

}
