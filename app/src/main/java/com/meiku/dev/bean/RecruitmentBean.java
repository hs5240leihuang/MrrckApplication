package com.meiku.dev.bean;

/**
 * Created by Administrator on 2015/8/28.
 */
public class RecruitmentBean {
    private String  userId;//用户名称
    private String  companyId;//公司id
    private String positionId;///** 职位类别编号 */
    private String jobName;//职位名称
    private String jobType;///** 工作性质：1：全职，2：兼职，3：实习 */
    private String isMarry;/** 婚姻状况：0：不限，1：已婚，2：未婚 */
    private String cityCode;/** 工作城市city表 */
    /**
     * 月薪code编码
     */
    private String salaryId;
    /**
     * 月薪code值
     */
    private String salaryValue;
    /**
     * 月薪最低，如果=0，面议
     */
    private String salaryFrom;
    /**
     * 月薪最高
     */
    private String salaryTo;
    private String houseSupport;
    private String jobIntroduce;
    private String gender;
    private String age;
    private String education;
    private String jobAge;
    private String needNum;
    private String knowledgeId;
    private String knowledge;
    private String goodAtId;
    private String goodAt;
    private String workAddress;
    private String arriveTime;
    private String longitude;
    private String latitude;
    private String voiceSeconds;
    private String contactName;
    private String contactPhone;
    private String resumeEmail;
    private String endDate;
    private String delStatus;
    private String jobId;


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getGoodAtId() {
        return goodAtId;
    }

    public void setGoodAtId(String goodAtId) {
        this.goodAtId = goodAtId;
    }

    public String getGoodAt() {
        return goodAt;
    }

    public void setGoodAt(String goodAt) {
        this.goodAt = goodAt;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getIsMarry() {
        return isMarry;
    }

    public void setIsMarry(String isMarry) {
        this.isMarry = isMarry;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(String salaryId) {
        this.salaryId = salaryId;
    }

    public String getSalaryValue() {
        return salaryValue;
    }

    public void setSalaryValue(String salaryValue) {
        this.salaryValue = salaryValue;
    }

    public String getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(String salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public String getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(String salaryTo) {
        this.salaryTo = salaryTo;
    }

    public String getHouseSupport() {
        return houseSupport;
    }

    public void setHouseSupport(String houseSupport) {
        this.houseSupport = houseSupport;
    }

    public String getJobIntroduce() {
        return jobIntroduce;
    }

    public void setJobIntroduce(String jobIntroduce) {
        this.jobIntroduce = jobIntroduce;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJobAge() {
        return jobAge;
    }

    public void setJobAge(String jobAge) {
        this.jobAge = jobAge;
    }

    public String getVoiceSeconds() {
        return voiceSeconds;
    }

    public void setVoiceSeconds(String voiceSeconds) {
        this.voiceSeconds = voiceSeconds;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getResumeEmail() {
        return resumeEmail;
    }

    public void setResumeEmail(String resumeEmail) {
        this.resumeEmail = resumeEmail;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }
}
