package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：用户表 类名称：com.mrrck.db.entity.MkUser 创建人：仲崇生 创建时间：2015-5-12 下午03:23:54
 * 
 * @version V1.0
 */
public class MkUser {

	/** 推荐码 */
	private String recommendCode;

	/** 星级 */
	private float starLevel;

	/** vip等级 */
	private float vipLevel;

	/** 用户对外编码code */
	private String userCode;

	/** 用户编号 */
	private int id;

	private int userId;

	/** 用户身份：1应届生2从业者3企业主4投资商 */
	private Integer type;

	/** 手机号码，登陆用户名 */
	private String phone;

	/** QQ号码第三方登录 */
	private String qq;

	/** 微信第三方登录 */
	private String weiXin;

	/** 新浪微博第三方登录 */
	private String sinaWeibo;

	/** 第三方聊天接口用户编号 */
	private String leanCloudId;

	/** leanCloud用户名 */
	private String leanCloudUserName;

	/** 登录密码 */
	private String password;

	/** 真实姓名 */
	private String realName;

	/** 昵称，显示在社交圈的名称 */
	private String nickName;

	/** 昵称拼音首字母 */
	private String nameFirstChar;

	/** 性别1男2女 */
	private String gender;

	/** 出生日期 */
	private String birthDate;

	/** 现居地 */
	private Integer liveCity;

	/** 用户头像(120X120) */
	private String headPicUrl;

	/** 用户主页背景图片(640X320) */
	private String backgroundId;

	/** 岗位ID */
	private Integer position;
	/** 岗位名称 */
	private String positionName;

	/** 应届生专业ID */
	private Integer major;

	/** 婚姻状况 0未婚 1已婚 */
	private Integer marryFlag;

	/** 个人简介 */
	private String remark;

	/** 角色权限 1:普通用户 2:写手 3:运营人员 4管理员 默认:1 */
	private int roleType;

	/** 常出没地 */
	private String appearPlace;

	/** 兴趣爱好 */
	private String hobby;

	/** 我的求职URL */
	private String myJobUrl;
	/** 个性标签 */
	private List<PersonalHonorConfigEntity> personalHonorList;
	 /**好友对我备注的名称*/
    private String friend2MyNickName;
    
	public List<PersonalHonorConfigEntity> getPersonalHonorList() {
		return personalHonorList;
	}

	public void setPersonalHonorList(
			List<PersonalHonorConfigEntity> personalHonorList) {
		this.personalHonorList = personalHonorList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getMarryFlag() {
		return marryFlag;
	}

	public void setMarryFlag(Integer marryFlag) {
		this.marryFlag = marryFlag;
	}

	/** 个性签名 */
	private String introduce;

	/** 朋友圈是否被陌生人查看 0-不可以查看，1-可以查看 */
	private String circleStatus;

	/** 用户创建日期 */
	private String createDate;

	/** 是否是导入用户,0不是，1是 */
	private String isImport;

	/** 初始化标记，0：未完成初始化，1：已完成初始化 */
	private String initFlag;

	/** 用户状态：0正常，1禁言 */
	private String status;

	/** 当前登录session */
	private String session;

	/** 登录次数 */
	private Integer loginTimes;

	/** 上次登录时间 */
	private String lastLoginDate;

	/** 状态0:正常1:删除 */
	private String delStatus;

	/** 最后更改时间 */
	private String lastEditDate;

	/** 用户登录名(后台使用) */
	private String userName;

	/** 角色id(后台使用) */
	private Integer roleId;

	/** 创建人(后台使用) */
	private Integer createBy;

	/** 修改人(后台使用) */
	private Integer updateBy;

	/** 是否超级管理员 0:不是，1:是 */
	private String superAdmin;

	/** 点赞次数(收藏次数) */
	private Integer collectNum;

	/** 用户二维码 */
	private String qRCode;

	/** 位置精度 */
	private String longitude;

	/** 位置纬度 */
	private String latitude;

	/** 用户头像(120X120) (客户端使用) */
	private String clientHeadPicUrl;

	/** type为2(美业经营者返回企业信息) */
	private CompanyEntity companyEntity;

	/** 用户登录时返回的用户的简历ID (-1没有) */
	private Integer resumeId;

	private String cityCode;
	private String provinceCode;
	private String address;
	/** 用户头像缩略图 */
	private String clientThumbHeadPicUrl;

	/** 用户是否完善信息标识 0:未完善 1:已完善 */
	private int userInfoFlag;

	/** 个性标签(个性标签表id,多个逗号分隔) */
	private String personalTag;

	/** 用户秀场作品数据List */
	private List<PostsSignupEntity> postsSignupList;

	/** 用户个性标签数据List */
	private List<PersonalTagEntity> personalTagList;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl == null ? "" : clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeiXin() {
		return weiXin;
	}

	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}

	public String getSinaWeibo() {
		return sinaWeibo;
	}

	public void setSinaWeibo(String sinaWeibo) {
		this.sinaWeibo = sinaWeibo;
	}

	public String getLeanCloudId() {
		return leanCloudId;
	}

	public void setLeanCloudId(String leanCloudId) {
		this.leanCloudId = leanCloudId;
	}

	public String getLeanCloudUserName() {
		return leanCloudUserName;
	}

	public void setLeanCloudUserName(String leanCloudUserName) {
		this.leanCloudUserName = leanCloudUserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNameFirstChar() {
		return nameFirstChar;
	}

	public void setNameFirstChar(String nameFirstChar) {
		this.nameFirstChar = nameFirstChar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getLiveCity() {
		return liveCity;
	}

	public void setLiveCity(Integer liveCity) {
		this.liveCity = liveCity;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getBackgroundId() {
		return backgroundId;
	}

	public void setBackgroundId(String backgroundId) {
		this.backgroundId = backgroundId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getCircleStatus() {
		return circleStatus;
	}

	public void setCircleStatus(String circleStatus) {
		this.circleStatus = circleStatus;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getIsImport() {
		return isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}

	public String getInitFlag() {
		return initFlag;
	}

	public void setInitFlag(String initFlag) {
		this.initFlag = initFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getLastEditDate() {
		return lastEditDate;
	}

	public void setLastEditDate(String lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}

	public String getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(String superAdmin) {
		this.superAdmin = superAdmin;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public String getqRCode() {
		return qRCode;
	}

	public void setqRCode(String qRCode) {
		this.qRCode = qRCode;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public CompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public void setCompanyEntity(CompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
	}

	public Integer getResumeId() {
		return resumeId;
	}

	public void setResumeId(Integer resumeId) {
		this.resumeId = resumeId;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public String getAppearPlace() {
		return appearPlace;
	}

	public void setAppearPlace(String appearPlace) {
		this.appearPlace = appearPlace;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getMyJobUrl() {
		return myJobUrl;
	}

	public void setMyJobUrl(String myJobUrl) {
		this.myJobUrl = myJobUrl;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public float getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(float starLevel) {
		this.starLevel = starLevel;
	}

	public float getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(float vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getUserInfoFlag() {
		return userInfoFlag;
	}

	public void setUserInfoFlag(int userInfoFlag) {
		this.userInfoFlag = userInfoFlag;
	}

	public String getPersonalTag() {
		return personalTag;
	}

	public void setPersonalTag(String personalTag) {
		this.personalTag = personalTag;
	}

	public List<PostsSignupEntity> getPostsSignupList() {
		return postsSignupList;
	}

	public void setPostsSignupList(List<PostsSignupEntity> postsSignupList) {
		this.postsSignupList = postsSignupList;
	}

	public List<PersonalTagEntity> getPersonalTagList() {
		return personalTagList;
	}

	public void setPersonalTagList(List<PersonalTagEntity> personalTagList) {
		this.personalTagList = personalTagList;
	}

	public String getFriend2MyNickName() {
		return friend2MyNickName;
	}

	public void setFriend2MyNickName(String friend2MyNickName) {
		this.friend2MyNickName = friend2MyNickName;
	}

}
