package com.meiku.dev.bean;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：支付系统订单管理(组订单   主表)
 * 类名称：com.mrrck.db.entity.MkPayOrderGroup     
 * 创建人：曙光
 * 创建时间：2016-9-15 上午09:41:40   
 * @version V1.0
 */
public class MkPayOrderGroup implements Serializable {
	/**自增编号*/
	private Integer id;
	/**用户编号*/
	private Integer userId;
	/**业务类型 跟子表有个字段不重复 这个是按照模块划分 
	 *0 充值模块 1找产品模块  2找装修模块
	 **/
	private Integer workType;
	/**通知的手机号码(冗余数据)*/
	private String phone;
	/**冗余子表数据*/
	private String orderName;
	/**冗余子表数据*/
	private String orderContent;
	/**千分位\r \r\n-5000：交易失败\r \r\n-4000：系统取消\r\r\n-3000：卖家取消\r \r\n-2000：买家取消\r \r\n1000：创建 初始化状态\r \r\n2000：交易完成\r \r\r\n百分位\r \r\n100：已支付\r\n200：已退款 \r \r\n十分位\r \r\n10：已发货\r \r \r\n个分位\r \r\n1：买家已评论\r \r\n2：卖家已评论\r \r\n3：双方已评论*/
	private Integer orderStatus;
	/**总订单价格*/
	private Float orderAllAmount;
	/**订单组编号*/
	private String orderGroupNumber;
	/**(三方订单编号,用于历史记录三方交互查询)支付平台交易号*/
	private String ortherOrderNumber;
	/**(支付方式 0美库余额 ,1支付宝,2微信 )*/
	private Integer payType;
	/**支付时间*/
	private String payTime;
	/**ip地址*/
	private String ipAddrss;
	/**业务冗余文件地址*/
	private String workFileUrl;
	/**(userId 自己 表示系统操作,如果是别人记录userId)*/
	private Integer createByUserId;
	/**创建时间*/
	private String createDate;
	/**更新时间*/
	private String updateDate;
	/**0 正常 1 删除*/
	private Integer delStatus;
	
	public Integer getWorkType() {
		return workType;
	}
	public void setWorkType(Integer workType) {
		this.workType = workType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOrderGroupNumber() {
		return orderGroupNumber;
	}
	public void setOrderGroupNumber(String orderGroupNumber) {
		this.orderGroupNumber = orderGroupNumber;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getOrderContent() {
		return orderContent;
	}
	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrtherOrderNumber() {
		return ortherOrderNumber;
	}
	public void setOrtherOrderNumber(String ortherOrderNumber) {
		this.ortherOrderNumber = ortherOrderNumber;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getIpAddrss() {
		return ipAddrss;
	}
	public void setIpAddrss(String ipAddrss) {
		this.ipAddrss = ipAddrss;
	}
	public Integer getCreateByUserId() {
		return createByUserId;
	}
	public void setCreateByUserId(Integer createByUserId) {
		this.createByUserId = createByUserId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getWorkFileUrl() {
		return workFileUrl;
	}
	public void setWorkFileUrl(String workFileUrl) {
		this.workFileUrl = workFileUrl;
	}
	public Float getOrderAllAmount() {
		return orderAllAmount;
	}
	public void setOrderAllAmount(Float orderAllAmount) {
		this.orderAllAmount = orderAllAmount;
	}
}
