package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：产品意向表(找产品)
 * 类名称：com.mrrck.db.entity.MkProductWill     
 * 创建人：仲崇生
 * 创建时间：2016-2-29 下午01:52:40   
 * @version V1.0
 */
public class MkProductWill{

 	/** 编号 */
	private Integer id;

 	/** 产品发布用户编号 */
	private Integer pubUserId;

 	/** 意向人编号 */
	private Integer willUserId;

 	/** 产品编号 */
	private Integer productId;

 	/** 意向内容 */
	private String willContent;

 	/** 联系人 */
	private String contactName;

 	/** 联系电话 */
	private String contactPhone;

 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除标识 0:正常 1:删除 */
	private Integer delStatus;
	
	private String clientHeadPicUrl;

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setPubUserId(Integer pubUserId){
		this.pubUserId=pubUserId;
	}

	public Integer getPubUserId(){
		return pubUserId;
	}

	public void setWillUserId(Integer willUserId){
		this.willUserId=willUserId;
	}

	public Integer getWillUserId(){
		return willUserId;
	}

	public void setProductId(Integer productId){
		this.productId=productId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setWillContent(String willContent){
		this.willContent=willContent;
	}

	public String getWillContent(){
		return willContent;
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
