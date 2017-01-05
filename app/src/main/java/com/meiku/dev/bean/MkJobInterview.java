package com.meiku.dev.bean;

/**
 *
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web
 *
 * 类描述：简历邀请面试表
 * 类名称：com.mrrck.db.entity.MkJobInterview
 * 创建人：仲崇生
 * 创建时间：2015-5-12 下午03:23:54
 * @version V1.0 
 */ 
public class MkJobInterview {
	
 	/** 编号 */
	private Integer id;

 	/** 简历id */
	private Integer resumeId;
	
	/** 简历对应的用户编号(id) */
    private Integer userId;

 	/** 工作职位id */
	private Integer jobId;
	
	/** 公司编号(邀请面试或投递简历必填) */
	private Integer companyId;

	/** 简历邀请人用户编号(id) */
    private Integer interviewUserId;

 	/** 面试时间 */
	private String interviewDate;

 	/** 面试地点 */
	private String interviewAddr;

 	/** 联系人名称 */
	private String contactName;

 	/** 联系人电话 */
	private String contactPhone;

 	/** 面试结果: 0:待面试 1:录用 2:弃用 */
	private String interviewResult;

 	/** 弃用原因 */
	private String refuseReason;

 	/** 面试通过通知 */
	private String passNotice;
	
	/** 是否查看 0:未查看 1:已查看 默认:0*/
	private String isView;

 	/** 创建时间(邀请时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

	/** 用户删除标识 0:正常 1:删除(求职宝使用) */
	private String userDelStatus;
	
 	/** 删除标识 0:正常 1:删除(招聘宝使用) */
	private String delStatus;

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setResumeId(Integer resumeId){
		this.resumeId=resumeId;
	}

	public Integer getResumeId(){
		return resumeId;
	}

	public void setUserId(Integer userId){
        this.userId=userId;
    }

    public Integer getUserId(){
        return userId;
    }
	
	public void setJobId(Integer jobId){
		this.jobId=jobId;
	}

	public Integer getJobId(){
		return jobId;
	}

	public Integer getInterviewUserId() {
        return this.interviewUserId;
    }

    public void setInterviewUserId(Integer interviewUserId) {
        this.interviewUserId = interviewUserId;
    }

    public void setInterviewDate(String interviewDate){
		this.interviewDate=interviewDate;
	}

	public String getInterviewDate(){
		return interviewDate;
	}

	public void setInterviewAddr(String interviewAddr){
		this.interviewAddr=interviewAddr;
	}

	public String getInterviewAddr(){
		return interviewAddr;
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

	public String getInterviewResult() {
		return interviewResult;
	}

	public void setInterviewResult(String interviewResult) {
		this.interviewResult = interviewResult;
	}

	public void setRefuseReason(String refuseReason){
		this.refuseReason=refuseReason;
	}

	public String getRefuseReason(){
		return refuseReason;
	}

	public void setPassNotice(String passNotice){
		this.passNotice=passNotice;
	}

	public String getPassNotice(){
		return passNotice;
	}

	public String getIsView() {
        return this.isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
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

	public String getUserDelStatus() {
		return userDelStatus;
	}

	public void setUserDelStatus(String userDelStatus) {
		this.userDelStatus = userDelStatus;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
