package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：产品收藏表(找产品)
 * 类名称：com.mrrck.db.entity.MkProductCollect     
 * 创建人：仲崇生
 * 创建时间：2016-2-29 上午11:43:34   
 * @version V1.0
 */
public class MkProductCollect {

 	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 收藏的产品编号 */
	private Integer productId;

 	/** 创建时间(收藏时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除标识 0:正常 1:删除 */
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

	public void setProductId(Integer productId){
		this.productId=productId;
	}

	public Integer getProductId(){
		return productId;
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

