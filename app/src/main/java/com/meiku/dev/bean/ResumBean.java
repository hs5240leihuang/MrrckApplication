package com.meiku.dev.bean;

/**
 * Created by Administrator on 2015/8/24.
 */
public class ResumBean {
    private String userId;//用户id
    private String resumeStatus;//简历状态0:暂不考虑新工作 1:正在找工作 2:观望好的机会
    private String positionId;//现在工作岗位
    private String cityCode;//现居住城市code
    private String jobAge;//美业职龄
    private String birthDate; // 生日
    private String gender; // 性别
    private String realName; // 真实姓名
    private String knowledgeId; // 专业知识
    private String knowledge; // 专业知识
    private String isMarry;//婚姻状况 1：未婚，2：已婚，3：保密
    private String phone;//手机号码
    private String likeJobsId;//意向职位id
    private String likeJobs;//意向职位
    private String likeCitysCode;//意向城市code，多个，隔开
    private String likeCitys;//意向城市
    private String expectSalary;//意向工资
    private String houseSupport;//是否希望职位提供住宿
    private String videoSeconds;//视频长度秒数（必填）
    private String videoTitle;//视频标题（可选）
    private String videoRemark;//视频备注（可选）
    public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getFringeBenefits() {
		return fringeBenefits;
	}

	public void setFringeBenefits(String fringeBenefits) {
		this.fringeBenefits = fringeBenefits;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getPersonalResume() {
		return personalResume;
	}

	public void setPersonalResume(String personalResume) {
		this.personalResume = personalResume;
	}

	private  String voiceSeconds;//音频长度秒数（必填）
    private String voiceTitle;//音频标题（可选）
    private String voiceRemark;//音频备注（可选）
    private String bossTypeId;
    private String bossType;
    
    private String education;//学历
    public String getFringeBenefitsId() {
		return fringeBenefitsId;
	}

	public void setFringeBenefitsId(String fringeBenefitsId) {
		this.fringeBenefitsId = fringeBenefitsId;
	}

	private String fringeBenefits;//福利待遇
    private String fringeBenefitsId;//福利待遇code
    private String jobType;//工作性质
    private String honor;//荣耀
    private String isRecommend;//推荐信
    private String personalResume;//个人履历
    
    public String getBossTypeId() {
        return bossTypeId;
    }

    public void setBossTypeId(String bossTypeId) {
        this.bossTypeId = bossTypeId;
    }

    public String getBossType() {
        return bossType;
    }

    public void setBossType(String bossType) {
        this.bossType = bossType;
    }

    private String isPublic;

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getResumeStatus() {
        return resumeStatus;
    }

    public void setResumeStatus(String resumeStatus) {
        this.resumeStatus = resumeStatus;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getJobAge() {
        return jobAge;
    }

    public void setJobAge(String jobAge) {
        this.jobAge = jobAge;
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

    public String getLikeCitysCode() {
        return likeCitysCode;
    }

    public void setLikeCitysCode(String likeCitysCode) {
        this.likeCitysCode = likeCitysCode;
    }

    public String getLikeCitys() {
        return likeCitys;
    }

    public void setLikeCitys(String likeCitys) {
        this.likeCitys = likeCitys;
    }

    public String getExpectSalary() {
        return expectSalary;
    }

    public void setExpectSalary(String expectSalary) {
        this.expectSalary = expectSalary;
    }

    public String getHouseSupport() {
        return houseSupport;
    }

    public void setHouseSupport(String houseSupport) {
        this.houseSupport = houseSupport;
    }

    public String getVideoSeconds() {
        return videoSeconds;
    }

    public void setVideoSeconds(String videoSeconds) {
        this.videoSeconds = videoSeconds;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVoiceSeconds() {
        return voiceSeconds;
    }

    public void setVoiceSeconds(String voiceSeconds) {
        this.voiceSeconds = voiceSeconds;
    }

    public String getVoiceTitle() {
        return voiceTitle;
    }

    public void setVoiceTitle(String voiceTitle) {
        this.voiceTitle = voiceTitle;
    }

    public String getVoiceRemark() {
        return voiceRemark;
    }

    public void setVoiceRemark(String voiceRemark) {
        this.voiceRemark = voiceRemark;
    }

    public String getVideoRemark() {
        return videoRemark;
    }

    public void setVideoRemark(String videoRemark) {
        this.videoRemark = videoRemark;
    }
}
