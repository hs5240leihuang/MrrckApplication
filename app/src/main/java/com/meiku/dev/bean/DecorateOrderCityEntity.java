package com.meiku.dev.bean;

import java.util.List;

public class DecorateOrderCityEntity extends MkDecorateOrderCity {
	/** 分页页数 */
	private Integer offset;
	/** 每页条数 */
	private Integer pageNum;
	/** 到期标记 0 未到期 1 到期 */
	private Integer expireFlag;
	/** 发布城市具体对象 */
	private List<DecorateOrderCityContentEntity> decorateOrderCityContentList;
	/*** 购买置顶标记 0 可以购买 1不可以购买 提示字段用orderTopFlagMsg */
	private Integer orderTopFlag;
	/** 是否可以置顶购买提示 **/
	private String orderTopFlagMsg;

	public Integer getOrderTopFlag() {
		return orderTopFlag;
	}

	public void setOrderTopFlag(Integer orderTopFlag) {
		this.orderTopFlag = orderTopFlag;
	}

	public String getOrderTopFlagMsg() {
		return orderTopFlagMsg;
	}

	public void setOrderTopFlagMsg(String orderTopFlagMsg) {
		this.orderTopFlagMsg = orderTopFlagMsg;
	}

	public List<DecorateOrderCityContentEntity> getDecorateOrderCityContentList() {
		return decorateOrderCityContentList;
	}

	public void setDecorateOrderCityContentList(
			List<DecorateOrderCityContentEntity> decorateOrderCityContentList) {
		this.decorateOrderCityContentList = decorateOrderCityContentList;
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

	public Integer getExpireFlag() {
		return expireFlag;
	}

	public void setExpireFlag(Integer expireFlag) {
		this.expireFlag = expireFlag;
	}

}
