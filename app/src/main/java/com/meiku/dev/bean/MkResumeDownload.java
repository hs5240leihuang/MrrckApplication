package com.meiku.dev.bean;

public class MkResumeDownload {
	/** 编号 */
	private Integer id;

 	/** 简历编号 */
	private Integer resumeId;

 	/** 简历用户编号 */
	private Integer userId;
	
	/** 公司编号 */
    private Integer companyId;
    
    /** 下载标识状态 0:下载简历 1:打电话联系 */
    private Integer downFlag;

 	/** 创建时间(下载时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态 0:正常 1:删除*/
	private Integer delStatus;
	
	/** 是否扣除点数 0未扣除点数 1扣除点数*/
    private Integer pointFlag;

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

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getDownFlag() {
        return this.downFlag;
    }

    public void setDownFlag(Integer downFlag) {
        this.downFlag = downFlag;
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

    public Integer getDelStatus() {
        return this.delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getPointFlag() {
        return this.pointFlag;
    }

    public void setPointFlag(Integer pointFlag) {
        this.pointFlag = pointFlag;
    }
}
