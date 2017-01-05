package com.meiku.dev.bean;

import java.io.Serializable;


/**
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：支付系统订单管理
 * 类名称：com.mrrck.ap.pay.entity.PayOrderEntity     
 * 创建人：曙光
 * 创建时间：2016-8-17 下午03:53:16   
 * @version V1.0
 */
public class PayOrderGroupEntity extends MkPayOrderGroup implements Serializable{
	/**订单id 用,隔开*/
	private String ids;
	
	/**客户端下单时间*/
	private String clientCreateDate;
	
	 /** 页码 */
    private Integer offset;
    
    /** 每页条数 */
    private Integer pageNum;
	
    /**服务器图片地址*/
	private String clientWorkFileUrl;
	
	/**交易状态名称*/
	private String orderStatusName;
	
	/**终端tab订单类型  0全部 1已完成 2已取消*/
	private Integer orderType;
	
	/**终端订单状态   1完成 2未支付 3已取消 */
	private Integer orderPayType;
	
	
	public String getOrderStatusName() {
//		千分位
//		-5000：交易失败
//		-4000：系统取消
//		-3000：卖家取消
//		-2000：买家取消
//		1000：创建 初始化状态
//		2000：交易完成
//		百分位
//		100：已支付
//		200：已退款 
//		十分位
//		10：已发货
//		个分位
//		1：买家已评论
		if(getOrderStatus() >= 2100){
			setOrderPayType(1);
			setOrderStatusName("已完成");
		}else if(getOrderStatus() == 1000){
			setOrderPayType(2);
			setOrderStatusName("未支付");
		}else if(getOrderStatus() == -2000){
			setOrderPayType(3);
			setOrderStatusName("已取消");
		}
		return orderStatusName;
	}

	
	public Integer getOrderPayType() {
		return orderPayType;
	}


	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}


	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getClientCreateDate() {
		return clientCreateDate;
	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getClientWorkFileUrl() {
		return clientWorkFileUrl;
	}

	public void setClientWorkFileUrl(String clientWorkFileUrl) {
		this.clientWorkFileUrl = clientWorkFileUrl;
	}
	
	
	

}
