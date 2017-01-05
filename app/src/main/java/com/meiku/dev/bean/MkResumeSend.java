package com.meiku.dev.bean;

/**
 *
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web
 *
 * 类描述：简历投递表
 * 类名称：com.mrrck.db.entity.MkResumeSend
 * 创建人：仲崇生
 * 创建时间：2015-5-12 下午03:23:54
 * @version V1.0 
 */ 
public class MkResumeSend {
	
 	/** 编号 */
	private Integer id;

 	/** 简历id */
	private Integer resumeId;

 	/** 工作职位id */
	private Integer jobId;
	
	/** 公司编号 */
    private Integer companyId;

 	/** 简历投递人用户编号(id) */
	private Integer userId;

 	/** 简历投递状态 0:未查看 1:安排面试 2:已查看 */
	private String sendStatus;

 	/** 创建时间(投递时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;
	
	/** 用户删除标识 0:正常 1:删除(求职宝使用) */
    private String userDelStatus;

 	/** 删除标识 0:正常 1:删除(招聘宝使用) */
	private String delStatus;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResumeId() {
        return this.resumeId;
    }

    public void setResumeId(Integer resumeId) {
        this.resumeId = resumeId;
    }

    public Integer getJobId() {
        return this.jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserDelStatus() {
        return this.userDelStatus;
    }

    public void setUserDelStatus(String userDelStatus) {
        this.userDelStatus = userDelStatus;
    }

    public String getDelStatus() {
        return this.delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

}

