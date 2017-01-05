package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：赛区结果公布表
 * 类名称：com.mrrck.db.entity.MkMatchResult     
 * 创建人：仲崇生
 * 创建时间：2016-2-22 上午10:16:04   
 * @version V1.0
 */
public class MkMatchResult {

 	/** 编号 */
	private Integer id;

 	/** 用户编号(创建人) */
	private Integer userId;

 	/** 赛区编号 */
	private Integer matchCityId;

 	/** 赛区结果公布名称 */
	private String resultName;

 	/** 赛区结果公布内容 */
	private String resultRemark;

 	/** 创建时间(结果公布时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态0正常，1删除 */
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

	public void setMatchCityId(Integer matchCityId){
		this.matchCityId=matchCityId;
	}

	public Integer getMatchCityId(){
		return matchCityId;
	}

	public void setResultName(String resultName){
		this.resultName=resultName;
	}

	public String getResultName(){
		return resultName;
	}

	public void setResultRemark(String resultRemark){
		this.resultRemark=resultRemark;
	}

	public String getResultRemark(){
		return resultRemark;
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
