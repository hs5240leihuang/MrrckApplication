package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：附近人Entity
 * 类名称：com.mrrck.ap.gp.entity.GroupEntity     
 * 创建人：仲崇生
 * 创建时间：2015-12-10 上午10:24:19   
 * @version V1.0
 */
public class NearUserEntity {
    

 	/** 用户编号 */
	private int id;

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

 	/** 应届生专业ID */
	private Integer major;

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
    
    /** 婚姻状况：0:未婚 1:已婚 默认0 */
    private Integer marryFlag;
    
    /** 推荐码 */
    private String recommendCode;
    
    /** 个人简介 */
    private String remark;
    
    /** 角色权限 1:普通用户 2:写手 3:运营人员 4管理员 默认:1  */
    private Integer roleType;
    
    /** 常出没地 */
    private String appearPlace;
    
    /** 兴趣爱好 */
    private String hobby;
    
    /** 星级 */
    private float starLevel;
    
    /** vip等级 */
    private float vipLevel;
    
    /** 用户对外编码code */
    private String userCode;
	
    /** 用户编号重命名*/
    private int userId;
	
    /** 用户头像(120X120) (客户端使用)*/
    private String clientHeadPicUrl;
    
    /**岗位名称*/
    private String positionName;
    
    /**岗位图标*/
    private String positionImgUrl;
    
    /**岗位图标(客户端使用)*/
    private String clientPositionImgUrl;
    
    /**真实年龄*/
    private String ageValue;
    
    /**起始数*/
    private Integer offset;
    
    /**每页条数*/
    private Integer pageNum;
    
    /** 最小纬度 */
    private double minLatitude;
    
    /** 最大纬度 */
    private double maxLatitude;
    
    /** 最小经度 */
    private double minLongitude;
    
    /** 最大经度 */
    private double maxLongitude;
    
    /** 获得中心经纬度与目标经纬度的距离 */
    private double distance;
    
    /** 目标距离转换成KM */
    private String kmDistance;
    
    /**用户更新时间与当前时间的间隔*/
    private String intervalTime;
    /**用户头像缩略图*/
    private String clientThumbHeadPicUrl;
    /** 个性标签分类名称  */
	private String tagName;
	
	/** 颜色 */
	private String color;
    
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionImgUrl() {
		return positionImgUrl;
	}

	public void setPositionImgUrl(String positionImgUrl) {
		this.positionImgUrl = positionImgUrl;
	}

	public String getClientPositionImgUrl() {
		return clientPositionImgUrl;
	}

	public void setClientPositionImgUrl(String clientPositionImgUrl) {
		this.clientPositionImgUrl = clientPositionImgUrl;
	}

	public String getAgeValue() {
		return ageValue;
	}

	public void setAgeValue(String ageValue) {
		this.ageValue = ageValue;
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

	public double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getKmDistance() {
		return kmDistance;
	}

	public void setKmDistance(String kmDistance) {
		this.kmDistance = kmDistance;
	}

	public String getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
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

	public Integer getMarryFlag() {
		return marryFlag;
	}

	public void setMarryFlag(Integer marryFlag) {
		this.marryFlag = marryFlag;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
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
    
    

}