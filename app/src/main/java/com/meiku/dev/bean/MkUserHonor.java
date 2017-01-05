package com.meiku.dev.bean;

 

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：用户荣誉表
 * 类名称：com.mrrck.db.entity.MkUserHonor     
 * 创建人：仲崇生
 * 创建时间：2015-11-18 上午10:09:49   
 * @version V1.0
 */
public class MkUserHonor  {

 	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 荣誉名称 */
	private String content;

 	/** 描述(备用) */
	private String remark;

 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态0正常1:逻辑删除 */
	private Integer delStatus;

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

	public void setContent(String content){
		this.content=content;
	}

	public String getContent(){
		return content;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return remark;
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

	public void setDelStatus(Integer delStatus){
		this.delStatus=delStatus;
	}

	public Integer getDelStatus(){
		return delStatus;
	}

}
