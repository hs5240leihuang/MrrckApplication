package com.meiku.dev.bean;

 

 

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：公司企业表Entity
 * 类名称：com.mrrck.db.entity.MkCompany     
 * 创建人：仲崇生
 * 创建时间：2015-5-20 下午03:27:13   
 * @version V1.0
 */
public class MkCompany  {

 	/** 编号 */
	 private int id;

 	/** 用户id */
	private Integer userId;

 	/** 企业名称 */
	private String name;

 	/** 企业名称拼音首字母 */
	private String nameFirstChar;

 	/** 公司所在城市 */
	private Integer cityCode;

 	/** 企业类型 */
	private Integer type;

 	/** 联系电话 */
	private String phone;

 	/** 企业邮箱 */
	private String email;

 	/** 企业介绍 */
	private String introduce;

 	/** 企业规模 */
	private Integer scale;

 	/** 企业经营品牌 */
	private String brands;

 	/** 企业所在地 */
	private String address;

 	/** 老板类型，多个以逗号分隔 */
	private String bossType;
	
	/** 老板类型名称，多个以逗号分隔 */
	private String bossTypeName;

 	/** 视频缩略图 */
	private String videoPhoto;

 	/** 视频 */
	private String video;

 	/** 视频长度（秒数） */
	private Integer videoSeconds;

 	/** 企业照片logo */
	private String companyLogo;

 	/** 企业营业执照 */
	private String license;

 	/** 企业身份认证不通过原因 */
	private String authResult;

 	/** 是否完成身份认证，0：审核中 1：已完成  2:不通过*/
	private String authPass;

 	/** 位置精度 */
	private String longitude;

 	/** 位置纬度 */
	private String latitude;
	
	/** 置顶 : 0不置顶 1置顶 */
    private String topFlag;
    
    /** 推荐标识: 0普通 1精华 */
    private String goodFlag;

 	/** 企业创建时间 */
	private String createDate;

 	/** 最后更新时间 */
	private String updateDate;

 	/** 0正常1删除 */
	private String delStatus;
	
	/** 是否是vip企业 0:不是 1:是 默认:0 */
    private String isVip;
    
    /** vip企业开始日期  */
    private String startVipDate;
    
    /** vip企业截止日期 */
    private String endVipDate;
    
    /** 公司网址 */
    private String website;
    
    /** 公司QQ号 */
    private String qq;
    
    /** 公司微信号 */
    private String weiXin;
    
    /** 附件数量 0:没有 其他数值 */
    private String isAttachment;
    /**分享图（公司logo）*/
	private String shareImg;
	
	/**分享头部 */
	private String shareTitle;
	
	/**分享内容*/
	private String shareContent;
	
	/**分享路径*/
	private String shareUrl;

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public void setUserId(Integer userId){
		this.userId=userId;
	}

	public Integer getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return name;
	}

	public void setNameFirstChar(String nameFirstChar){
		this.nameFirstChar=nameFirstChar;
	}

	public String getNameFirstChar(){
		return nameFirstChar;
	}

	public void setCityCode(Integer cityCode){
		this.cityCode=cityCode;
	}

	public Integer getCityCode(){
		return cityCode;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return type;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getEmail(){
		return email;
	}

	public void setIntroduce(String introduce){
		this.introduce=introduce;
	}

	public String getIntroduce(){
		return introduce;
	}

	public void setScale(Integer scale){
		this.scale=scale;
	}

	public Integer getScale(){
		return scale;
	}

	public void setBrands(String brands){
		this.brands=brands;
	}

	public String getBrands(){
		return brands;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getAddress(){
		return address;
	}

	public void setBossType(String bossType){
		this.bossType=bossType;
	}

	public String getBossType(){
		return bossType;
	}

	public String getBossTypeName() {
		return bossTypeName;
	}

	public void setBossTypeName(String bossTypeName) {
		this.bossTypeName = bossTypeName;
	}

	public void setVideoPhoto(String videoPhoto){
		this.videoPhoto=videoPhoto;
	}

	public String getVideoPhoto(){
		return videoPhoto;
	}

	public void setVideo(String video){
		this.video=video;
	}

	public String getVideo(){
		return video;
	}

	public void setVideoSeconds(Integer videoSeconds){
		this.videoSeconds=videoSeconds;
	}

	public Integer getVideoSeconds(){
		return videoSeconds;
	}

	public void setCompanyLogo(String companyLogo){
		this.companyLogo=companyLogo;
	}

	public String getCompanyLogo(){
		return companyLogo;
	}

	public void setLicense(String license){
		this.license=license;
	}

	public String getLicense(){
		return license;
	}

	public void setAuthResult(String authResult){
		this.authResult=authResult;
	}

	public String getAuthResult(){
		return authResult;
	}

	public void setAuthPass(String authPass){
		this.authPass=authPass;
	}

	public String getAuthPass(){
		return authPass;
	}

	public void setLongitude(String longitude){
		this.longitude=longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setLatitude(String latitude){
		this.latitude=latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getTopFlag() {
        return this.topFlag;
    }

    public void setTopFlag(String topFlag) {
        this.topFlag = topFlag;
    }

    public String getGoodFlag() {
        return this.goodFlag;
    }

    public void setGoodFlag(String goodFlag) {
        this.goodFlag = goodFlag;
    }

    public void setCreateDate(String createDate){
		this.createDate=createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setUpdateDate(String updateDate){
		this.updateDate=updateDate;
	}

	public String getUpdateDate(){
		return updateDate;
	}

	public void setDelStatus(String delStatus){
		this.delStatus=delStatus;
	}

	public String getDelStatus(){
		return delStatus;
	}

    public String getIsVip() {
        return this.isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getStartVipDate() {
        return this.startVipDate;
    }

    public void setStartVipDate(String startVipDate) {
        this.startVipDate = startVipDate;
    }

    public String getEndVipDate() {
        return this.endVipDate;
    }

    public void setEndVipDate(String endVipDate) {
        this.endVipDate = endVipDate;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeiXin() {
        return this.weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin;
    }

    public String getIsAttachment() {
        return this.isAttachment;
    }

    public void setIsAttachment(String isAttachment) {
        this.isAttachment = isAttachment;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
