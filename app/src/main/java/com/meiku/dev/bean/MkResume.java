package com.meiku.dev.bean;



/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述： 简历总览表
 * 类名称：com.mrrck.db.entity.MkResume     
 * 创建人：仲崇生
 * 创建时间：2015-5-23 下午01:41:10   
 * @version V1.0
 */
public class MkResume{

 	/** 编号 */
	private Integer id;

 	/** 用户id */
	private Integer userId;

 	/** 简历头像 */
	private String resumePhoto;

 	/** 简历状态 0:我不需要找工作，关闭简历 1:我要找工作 */
	private String resumeStatus;

 	/** 美库简历开启状态 0:未开启 1:开启 */
	private String mrrckStatus;

 	/** 文字简历开启状态 0:未开启 1: 开启 */
	private String wordStatus;
	
	/** 公开状态 0:未公开 1: 公开  默认:1 */
    private String isPublic;

 	/** 是否有美库简历 0:没有 1:有 */
	private String isMrrckResume;

 	/** 是否有文字简历 0:没有 1:有 */
	private String isWordResume;

 	/** 真实姓名 */
	private String realName;

 	/** 性别 1:男，2:女 */
	private String gender;

 	/** 出生日期 */
	private String birthDate;

 	/** 现在工作岗位 */
	private Integer positionId;

 	/** 现居住城市 */
	private Integer cityCode;

 	/** 最高学历 */
	private Integer education;

 	/** 美业职龄 */
	private Integer jobAge;

 	/** 婚姻状况 1：未婚，2：已婚，3：保密 */
	private String isMarry;

 	/** 手机号码 */
	private String phone;

 	/** 电子邮箱 */
	private String email;

 	/** qq号码 */
	private String qq;

 	/** 简历完整度格式为：85,20,20,15,15,15 */
	private String progress;

 	/** 刷新时间 */
	private String refreshDate;

 	/** 简历创建时间 */
	private String createDate;

 	/** 简历更新时间 */
	private String updateDate;

 	/** 删除标识 0:正常 1:删除 */
	private String delStatus;
	
	/**是否有语音简历*/
	private String isVoiceResume;
	
	/**是否有视频简历*/
	private String isVideoResume;

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

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setUserId(Integer userId){
		this.userId=userId;
	}

	public Integer getUserId(){
		return userId;
	}

	public void setResumePhoto(String resumePhoto){
		this.resumePhoto=resumePhoto;
	}

	public String getResumePhoto(){
		return resumePhoto;
	}

	public void setResumeStatus(String resumeStatus){
		this.resumeStatus=resumeStatus;
	}

	public String getResumeStatus(){
		return resumeStatus;
	}

	public void setMrrckStatus(String mrrckStatus){
		this.mrrckStatus=mrrckStatus;
	}

	public String getMrrckStatus(){
		return mrrckStatus;
	}

	public void setWordStatus(String wordStatus){
		this.wordStatus=wordStatus;
	}

	public String getWordStatus(){
		return wordStatus;
	}

	public String getIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public void setIsMrrckResume(String isMrrckResume){
		this.isMrrckResume=isMrrckResume;
	}

	public String getIsMrrckResume(){
		return isMrrckResume;
	}

	public void setIsWordResume(String isWordResume){
		this.isWordResume=isWordResume;
	}

	public String getIsWordResume(){
		return isWordResume;
	}

	public void setRealName(String realName){
		this.realName=realName;
	}

	public String getRealName(){
		return realName;
	}

	public void setGender(String gender){
		this.gender=gender;
	}

	public String getGender(){
		return gender;
	}

	public void setBirthDate(String birthDate){
		this.birthDate=birthDate;
	}

	public String getBirthDate(){
		return birthDate;
	}

	public void setPositionId(Integer positionId){
		this.positionId=positionId;
	}

	public Integer getPositionId(){
		return positionId;
	}

	public void setCityCode(Integer cityCode){
		this.cityCode=cityCode;
	}

	public Integer getCityCode(){
		return cityCode;
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

	public void setIsMarry(String isMarry){
		this.isMarry=isMarry;
	}

	public String getIsMarry(){
		return isMarry;
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

	public void setQq(String qq){
		this.qq=qq;
	}

	public String getQq(){
		return qq;
	}

	public void setProgress(String progress){
		this.progress=progress;
	}

	public String getProgress(){
		return progress;
	}

	public void setRefreshDate(String refreshDate){
		this.refreshDate=refreshDate;
	}

	public String getRefreshDate(){
		return refreshDate;
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

}

