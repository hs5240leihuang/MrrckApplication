package com.meiku.dev.bean;

import java.io.Serializable;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：产品信息表(找产品)
 * 类名称：com.mrrck.db.entity.MkProductInfo     
 * 创建人：仲崇生
 * 创建时间：2016-2-29 上午11:46:19   
 * @version V1.0
 */
public class MkProductInfo implements Serializable{

 	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 产品名称 */
	private String productName;

 	/** 产品分类编号 */
	private Integer categoryId;

 	/** 海报主图 */
	private String posterMain;

 	/** 海报缩略图 */
	private String posterThum;

 	/** 招商省份编码(多个逗号分隔) */
	private String provinceCodes;

 	/** 招商省份名称(多个逗号分隔) */
	private String provinceNames;

 	/** 公司编号(个人认证时为0) */
	private Integer companyId;

 	/** 产品信息备注 */
	private String remark;

 	/** 刷新时间 */
	private String refreshDate;

 	/** 是否上线标识 0:否 1:是 */
	private Integer publicFlag;

 	/** 置顶标识 0:正常 1:置顶 */
	private Integer topFlag;
	
	/** 置顶开始时间 */
    private String topStartTime;
    
    /** 置顶结束时间 */
    private String topEndTime;
	
 	/** 浏览次数 */
	private Integer viewNum;
	
	/** 被收藏次数 */
    private Integer collectNum;

 	/** 最新购买开始时间 */
	private String buyStartTime;

 	/** 购买时长 */
	private Integer buyMonths;

 	/** 最新购买结束时间 */
	private String buyEndTime;
	
	/** 产品联系人 */
	private String contactName;
	
	/** 产品联系人电话 */
    private String contactPhone;
	
 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除标识 0:正常 1:删除 */
	private Integer delStatus;
	
	/** 支付记录标识 0:无购买记录 1:有购买记录 */
    private Integer payRecordFlag;

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

	public void setProductName(String productName){
		this.productName=productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId=categoryId;
	}

	public Integer getCategoryId(){
		return categoryId;
	}

	public void setPosterMain(String posterMain){
		this.posterMain=posterMain;
	}

	public String getPosterMain(){
		return posterMain;
	}

	public void setPosterThum(String posterThum){
		this.posterThum=posterThum;
	}

	public String getPosterThum(){
		return posterThum;
	}

	public void setProvinceCodes(String provinceCodes){
		this.provinceCodes=provinceCodes;
	}

	public String getProvinceCodes(){
		return provinceCodes;
	}

	public void setProvinceNames(String provinceNames){
		this.provinceNames=provinceNames;
	}

	public String getProvinceNames(){
		return provinceNames;
	}

	public void setCompanyId(Integer companyId){
		this.companyId=companyId;
	}

	public Integer getCompanyId(){
		return companyId;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setRefreshDate(String refreshDate){
		this.refreshDate=refreshDate;
	}

	public String getRefreshDate(){
		return refreshDate;
	}

	public void setPublicFlag(Integer publicFlag){
		this.publicFlag=publicFlag;
	}

	public Integer getPublicFlag(){
		return publicFlag;
	}

	public void setTopFlag(Integer topFlag){
		this.topFlag=topFlag;
	}

	public Integer getTopFlag(){
		return topFlag;
	}

	public String getTopStartTime() {
        return this.topStartTime;
    }

    public void setTopStartTime(String topStartTime) {
        this.topStartTime = topStartTime;
    }

    public String getTopEndTime() {
        return this.topEndTime;
    }

    public void setTopEndTime(String topEndTime) {
        this.topEndTime = topEndTime;
    }

    public void setViewNum(Integer viewNum){
		this.viewNum=viewNum;
	}

	public Integer getViewNum(){
		return viewNum;
	}

	public Integer getCollectNum() {
        return this.collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public void setBuyStartTime(String buyStartTime){
		this.buyStartTime=buyStartTime;
	}

	public String getBuyStartTime(){
		return buyStartTime;
	}

	public void setBuyMonths(Integer buyMonths){
		this.buyMonths=buyMonths;
	}

	public Integer getBuyMonths(){
		return buyMonths;
	}

	public void setBuyEndTime(String buyEndTime){
		this.buyEndTime=buyEndTime;
	}

	public String getBuyEndTime(){
		return buyEndTime;
	}

	public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public Integer getPayRecordFlag() {
        return this.payRecordFlag;
    }

    public void setPayRecordFlag(Integer payRecordFlag) {
        this.payRecordFlag = payRecordFlag;
    }

}
