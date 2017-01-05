package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 版权所有：2016-美库网
 * 项目名称：mrrck-pc  
 *
 * 类描述：终端下发db数据表     置顶购买参数
 * 类名称：com.mrrck.db.entity.MkDecorateCategory     
 * 创建人：曙光
 * 创建时间：2016-8-31 下午06:29:24   
 * @version V1.0
 */
public class MkDataConfigTopPrice {
	/**自增编号*/
	private Integer id;
	/**置顶天数*/
	private String day;
	/**打折数*/
	private String loseNum;
	/**原价*/
	private String costPrice;
	/**现价*/
	private String nowPrice;
	/**最低每天多少钱*/
	private String lowestPriceEveryDay;
	/**价格类型  0 找装修置顶置顶费用   1找产品置顶费用*/
	private Integer type;
	/**排序*/
	private Integer sortNo; 
	/**创建时间*/
	private String createDate;
	/**更新时间*/
	private String updateDate;
	/**删除标识 0:正常 1:删除*/
	private Integer delStatus;
	public MkDataConfigTopPrice(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setDay(model.getString("day"));
		this.setLoseNum(model.getString("loseNum"));
		this.setCostPrice(model.getString("costPrice"));
		this.setNowPrice(model.getString("nowPrice"));
		this.setLowestPriceEveryDay(model.getString("lowestPriceEveryDay"));
		this.setType(model.getInt("type"));
		this.setSortNo(model.getInt("type"));
		this.setCreateDate(model.getString("createDate"));
		this.setUpdateDate(model.getString("updateDate"));
		this.setDelStatus(model.getInt("delStatus"));
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getLoseNum() {
		return loseNum;
	}
	public void setLoseNum(String loseNum) {
		this.loseNum = loseNum;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getLowestPriceEveryDay() {
		return lowestPriceEveryDay;
	}
	public void setLowestPriceEveryDay(String lowestPriceEveryDay) {
		this.lowestPriceEveryDay = lowestPriceEveryDay;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSortNo() {
		return sortNo;
	}
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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
}