package com.meiku.dev.bean;

/**
 *
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web
 *
 * 类描述：
 * 类名称：com.mrrck.db.entity.MkJob
 * 创建人：仲崇生
 * 创建时间：Wed May 20 15:22:25 CST 2015
 * @version V1.0
 */ 
public class MkJob {

 	/** 编号 */
	private Integer id;

 	/** 公司ID */
	private Integer companyId;
	
	/** 职位类别编号 */
	private Integer positionId;

 	/** 职位名称 */
	private String jobName;

 	/** 工作性质：1：全职，2：兼职，3：实习 */
	private String jobType;
	
	/** 婚姻状况：0：不限，1：已婚，2：未婚 */
	private String isMarry;

 	/** 工作城市city表 */
	private Integer cityCode;
	
	/** 月薪code编码 */
	private Integer salaryId;
	
	/** 月薪code值 */
	private String salaryValue;

 	/** 月薪最低，如果=0，面议 */
	private Integer salaryFrom;

 	/** 月薪最高 */
	private Integer salaryTo;

 	/** 是否提供住宿0：不，1：提供 */
	private String houseSupport;

 	/** 职位描述 */
	private String jobIntroduce;

 	/** 性别要求，0：不限，1：限男性，2：限女性 */
	private String gender;

 	/** 年龄要求 */
	private Integer age;

 	/** 学历要求 */
	private Integer education;

 	/** 美业年龄要求 */
	private Integer jobAge;

 	/** 需要人数 */
	private String needNum;
	
	/** 专业知识编号(多个以逗号分隔) */
	private String knowledgeId;
	
	/** 专业知识 */
	private String knowledge;

 	/** 生活特长编号(多个以逗号分隔) */
	private String goodAtId;

 	/** 生活特长 */
	private String goodAt;

 	/** 工作地址 */
	private String workAddress;
	
	/** 到岗时间 存入mk_data_config codeId */
	private Integer arriveTime;

 	/** 位置精度 */
	private String longitude;

 	/** 位置纬度 */
	private String latitude;

 	/** 语音描述 */
	private String voice;

 	/** 语音长度（秒数） */
	private Integer voiceSeconds;

 	/** 联系人名称 */
	private String contactName;

 	/** 联系人电话 */
	private String contactPhone;

 	/** 接受简历邮箱 */
	private String resumeEmail;

 	/** 职位有效期 */
	private String endDate;

 	/** 发布时间（刷新时间） */
	private String refreshDate;

 	/** 职位创建人(用户编号) */
	private Integer userId;

 	/** 职位创建日期 */
	private String createDate;

 	/** 职位最后更新时间 */
	private String updateDate;

 	/** 0:正常 1:删除 2:下架 */
	private String delStatus;
	
	/** 福利待遇编号(多个逗号分隔) */
    private String fringeBenefitsId;
    
    /** 福利待遇名称(多个逗号分隔) */
    private String fringeBenefitsName;
    
    /** 职位置顶 0:普通 1:置顶 */
    private Integer topFlag;
    
    /** 置顶结束时间 */
    private String topEndDate;
    
    /** 有效天数 默认0天 */
    private Integer validDay;
    
    /** 职位浏览量 默认0次 */
    private Integer viewNum;
    
    /** 全国code(固定值100000) */
    private Integer wholeCode;
    
    /** 行政区划编码(省份code,多个逗号分隔) */
    private String provinceCode;
    
    /** 行政区划编码(城市code,多个逗号分隔) */
    private String jobCityCode;
    
    /** 行政区划城市名称(城市code对应名称,多个逗号分隔) */
    private String jobCityName;
    private String ageValue;
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

	public String getAgeValue() {
		return ageValue;
	}

	public void setAgeValue(String ageValue) {
		this.ageValue = ageValue;
	}

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setCompanyId(Integer companyId){
		this.companyId=companyId;
	}

	public Integer getCompanyId(){
		return companyId;
	}

	public void setJobName(String jobName){
		this.jobName=jobName;
	}

	public String getJobName(){
		return jobName;
	}

	public void setPositionId(Integer positionId){
		this.positionId=positionId;
	}

	public Integer getPositionId(){
		return positionId;
	}

	public void setJobType(String jobType){
		this.jobType=jobType;
	}

	public String getJobType(){
		return jobType;
	}

	public String getIsMarry() {
		return isMarry;
	}

	public void setIsMarry(String isMarry) {
		this.isMarry = isMarry;
	}

	public void setCityCode(Integer cityCode){
		this.cityCode=cityCode;
	}

	public Integer getCityCode(){
		return cityCode;
	}

	public Integer getSalaryId() {
		return salaryId;
	}

	public void setSalaryId(Integer salaryId) {
		this.salaryId = salaryId;
	}

	public String getSalaryValue() {
		return salaryValue;
	}

	public void setSalaryValue(String salaryValue) {
		this.salaryValue = salaryValue;
	}

	public void setSalaryFrom(Integer salaryFrom){
		this.salaryFrom=salaryFrom;
	}

	public Integer getSalaryFrom(){
		return salaryFrom;
	}

	public void setSalaryTo(Integer salaryTo){
		this.salaryTo=salaryTo;
	}

	public Integer getSalaryTo(){
		return salaryTo;
	}

	public void setHouseSupport(String houseSupport){
		this.houseSupport=houseSupport;
	}

	public String getHouseSupport(){
		return houseSupport;
	}

	public void setJobIntroduce(String jobIntroduce){
		this.jobIntroduce=jobIntroduce;
	}

	public String getJobIntroduce(){
		return jobIntroduce;
	}

	public void setGender(String gender){
		this.gender=gender;
	}

	public String getGender(){
		return gender;
	}

	public void setAge(Integer age){
		this.age=age;
	}

	public Integer getAge(){
		return age;
	}

	public void setEducation(Integer education){
		this.education=education;
	}

	public Integer getEducation(){
		return education;
	}

	public void setJobAge(Integer jobAge){
		this.jobAge=jobAge;
	}

	public Integer getJobAge(){
		return jobAge;
	}

	public String getNeedNum() {
		return needNum;
	}

	public void setNeedNum(String needNum) {
		this.needNum = needNum;
	}

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public void setKnowledge(String knowledge){
		this.knowledge=knowledge;
	}

	public String getKnowledge(){
		return knowledge;
	}

	public String getGoodAtId() {
		return goodAtId;
	}

	public void setGoodAtId(String goodAtId) {
		this.goodAtId = goodAtId;
	}

	public void setGoodAt(String goodAt){
		this.goodAt=goodAt;
	}

	public String getGoodAt(){
		return goodAt;
	}

	public void setWorkAddress(String workAddress){
		this.workAddress=workAddress;
	}

	public String getWorkAddress(){
		return workAddress;
	}

	public Integer getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Integer arriveTime) {
		this.arriveTime = arriveTime;
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

	public void setVoice(String voice){
		this.voice=voice;
	}

	public String getVoice(){
		return voice;
	}

	public void setVoiceSeconds(Integer voiceSeconds){
		this.voiceSeconds=voiceSeconds;
	}

	public Integer getVoiceSeconds(){
		return voiceSeconds;
	}

	public void setContactName(String contactName){
		this.contactName=contactName;
	}

	public String getContactName(){
		return contactName;
	}

	public void setContactPhone(String contactPhone){
		this.contactPhone=contactPhone;
	}

	public String getContactPhone(){
		return contactPhone;
	}

	public void setResumeEmail(String resumeEmail){
		this.resumeEmail=resumeEmail;
	}

	public String getResumeEmail(){
		return resumeEmail;
	}

	public void setEndDate(String endDate){
		this.endDate=endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setRefreshDate(String refreshDate){
		this.refreshDate=refreshDate;
	}

	public String getRefreshDate(){
		return refreshDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

    public String getFringeBenefitsId() {
        return this.fringeBenefitsId;
    }

    public void setFringeBenefitsId(String fringeBenefitsId) {
        this.fringeBenefitsId = fringeBenefitsId;
    }

    public String getFringeBenefitsName() {
        return this.fringeBenefitsName;
    }

    public void setFringeBenefitsName(String fringeBenefitsName) {
        this.fringeBenefitsName = fringeBenefitsName;
    }

    public Integer getTopFlag() {
        return this.topFlag;
    }

    public void setTopFlag(Integer topFlag) {
        this.topFlag = topFlag;
    }

    public String getTopEndDate() {
        return this.topEndDate;
    }

    public void setTopEndDate(String topEndDate) {
        this.topEndDate = topEndDate;
    }

    public Integer getValidDay() {
        return this.validDay;
    }

    public void setValidDay(Integer validDay) {
        this.validDay = validDay;
    }

    public Integer getViewNum() {
        return this.viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getWholeCode() {
        return this.wholeCode;
    }

    public void setWholeCode(Integer wholeCode) {
        this.wholeCode = wholeCode;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getJobCityCode() {
        return this.jobCityCode;
    }

    public void setJobCityCode(String jobCityCode) {
        this.jobCityCode = jobCityCode;
    }

    public String getJobCityName() {
        return this.jobCityName;
    }

    public void setJobCityName(String jobCityName) {
        this.jobCityName = jobCityName;
    }

}
