package com.meiku.dev.bean;

/**
 *
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web
 *
 * 类描述：收藏的简历表
 * 类名称：com.mrrck.db.entity.MkResumeCollect
 * 创建人：仲崇生
 * 创建时间：2015-5-12 下午03:23:54
 * @version V1.0 
 */ 
public class MkResumeCollect {
	
 	/** 编号 */
	private Integer id;

 	/** 简历编号 */
	private Integer resumeId;

 	/** 简历用户编号 */
	private Integer userId;
	
	/** 公司编号 */
    private Integer companyId;

 	/** 创建时间(收藏时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态 0:正常 1:删除*/
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

    public Integer getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
}

