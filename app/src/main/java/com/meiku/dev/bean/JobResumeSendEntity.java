package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：简历投递Entity
 * 类名称：com.mrrck.ap.rt.entity.JobResumeSendEntity     
 * 创建人：仲崇生
 * 创建时间：2015-7-7 下午03:39:48   
 * @version V1.0
 */
public class JobResumeSendEntity extends MkResumeSend{
    
    /**投递简历表 resumeSendIds*/
    private List<String> resumeSendIds;
    
    /** 真实姓名 */
    private String realName;
    
    /** 简历头像 */
    private String resumePhoto;
    
    /** 简历头像(客户端使用) */
    private String clientResumePhoto;
    
    /** 性别 1:男，2:女 */
    private String gender;
    
    /** 性别中文名称 */
    private String genderName;
    
    /** 出生日期 */
    private String birthDate;
    
    /** 美库简历开启状态 0:未开启 1:开启 */
    private String mrrckStatus;

    /** 文字简历开启状态 0:未开启 1: 开启 */
    private String wordStatus;

    /** 是否有美库简历 0:没有 1:有 */
    private String isMrrckResume;

    /** 是否有文字简历 0:没有 1:有 */
    private String isWordResume;
    
    /**是否有语音简历*/
    private String isVoiceResume;
    
    /**是否有视频简历*/
    private String isVideoResume;
    
    /**岗位编号*/
    private Integer positionId;
    
    /**专业技能codeId*/
    private String knowledgeId;
    
    /**专业技能*/
    private String knowledge;
    
    /**城市代码*/
    private Integer cityCode;
    
    /**分页页数*/
    private Integer offset;
    
    /**每页条数*/
    private Integer pageNum;
    
    /**简历状态对应中文名称*/
    private String resumeStatusName;
    
     /** 最高学历 */
    private Integer education;
    
    /**最高学历中文名称*/
    private String educationName;
    
    /** 美业职龄 */
    private Integer jobAge;
    
    /**美业工龄*/
    private String jobAgeValue;
    
    /** 婚姻状况 1：未婚，2：已婚，3：保密 */
    private String isMarry;
    
    /** 手机号码 */
    private String phone;
    
    /** 刷新时间 */
    private String refreshDate;
    
    /**真实年龄*/
    private String ageValue;
    
    /**简历投递状态*/
    private String sendStatusName;
    
    /**意向职位id,多个以逗号隔开*/
    private String likeJobsId;
    
    /**意向职位*/
    private String likeJobs;
    
    /**职位名称---用于简历显示求职名称  2015-12-04 19:32:06   version:3.0  曙光加入*/
    private String jobName;
    
    /**用于终端展示刷新时间*/
    private String clientRefreshDate;
    
    /**职位是否被删除delStatus*/
    private String jobDelStatus;
    
    /**是否选择，客户端添加使用*/
    private boolean isSelected;

	public List<String> getResumeSendIds() {
		return resumeSendIds;
	}

	public void setResumeSendIds(List<String> resumeSendIds) {
		this.resumeSendIds = resumeSendIds;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getResumePhoto() {
		return resumePhoto;
	}

	public void setResumePhoto(String resumePhoto) {
		this.resumePhoto = resumePhoto;
	}

	public String getClientResumePhoto() {
		return clientResumePhoto;
	}

	public void setClientResumePhoto(String clientResumePhoto) {
		this.clientResumePhoto = clientResumePhoto;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getMrrckStatus() {
		return mrrckStatus;
	}

	public void setMrrckStatus(String mrrckStatus) {
		this.mrrckStatus = mrrckStatus;
	}

	public String getWordStatus() {
		return wordStatus;
	}

	public void setWordStatus(String wordStatus) {
		this.wordStatus = wordStatus;
	}

	public String getIsMrrckResume() {
		return isMrrckResume;
	}

	public void setIsMrrckResume(String isMrrckResume) {
		this.isMrrckResume = isMrrckResume;
	}

	public String getIsWordResume() {
		return isWordResume;
	}

	public void setIsWordResume(String isWordResume) {
		this.isWordResume = isWordResume;
	}

	public String getIsVoiceResume() {
		return isVoiceResume;
	}

	public void setIsVoiceResume(String isVoiceResume) {
		this.isVoiceResume = isVoiceResume;
	}

	public String getIsVideoResume() {
		return isVideoResume;
	}

	public void setIsVideoResume(String isVideoResume) {
		this.isVideoResume = isVideoResume;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
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

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
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

	public String getResumeStatusName() {
		return resumeStatusName;
	}

	public void setResumeStatusName(String resumeStatusName) {
		this.resumeStatusName = resumeStatusName;
	}

	public Integer getEducation() {
		return education;
	}

	public void setEducation(Integer education) {
		this.education = education;
	}

	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}

	public Integer getJobAge() {
		return jobAge;
	}

	public void setJobAge(Integer jobAge) {
		this.jobAge = jobAge;
	}

	public String getJobAgeValue() {
		return jobAgeValue;
	}

	public void setJobAgeValue(String jobAgeValue) {
		this.jobAgeValue = jobAgeValue;
	}

	public String getIsMarry() {
		return isMarry;
	}

	public void setIsMarry(String isMarry) {
		this.isMarry = isMarry;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(String refreshDate) {
		this.refreshDate = refreshDate;
	}

	public String getAgeValue() {
		return ageValue;
	}

	public void setAgeValue(String ageValue) {
		this.ageValue = ageValue;
	}

	public String getSendStatusName() {
		return sendStatusName;
	}

	public void setSendStatusName(String sendStatusName) {
		this.sendStatusName = sendStatusName;
	}

	public String getLikeJobsId() {
		return likeJobsId;
	}

	public void setLikeJobsId(String likeJobsId) {
		this.likeJobsId = likeJobsId;
	}

	public String getLikeJobs() {
		return likeJobs;
	}

	public void setLikeJobs(String likeJobs) {
		this.likeJobs = likeJobs;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getClientRefreshDate() {
		return clientRefreshDate;
	}

	public void setClientRefreshDate(String clientRefreshDate) {
		this.clientRefreshDate = clientRefreshDate;
	}

	public String getJobDelStatus() {
		return jobDelStatus;
	}

	public void setJobDelStatus(String jobDelStatus) {
		this.jobDelStatus = jobDelStatus;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
