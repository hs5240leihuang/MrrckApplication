package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;


/**
 * 版权所有：2016-美库网
 * 项目名称：mrrck-pc  
 *
 * 类描述：终端下发db数据表     购买选择月份数据表
 * 类名称：com.mrrck.db.entity.MkDataConfigReleaseMonths
 * 创建人：曙光
 * 创建时间：2016-8-31 下午06:29:24   
 * @version V1.0
 */
public class MkDataConfigReleaseMonths{
	/**自增编号*/
	private Integer id;
	/**发布月份名称*/
	private String monthsName;
	/**月份数值*/
	private String monthsValue;
	/**价格类型  0 找装修置顶置顶费用   1找产品置顶费用*/
	private Integer type;
	/**排序*/
	private Integer sortNo; 
	/**钱*/
	private String money;
	/**创建时间*/
	private String createDate;
	/**更新时间*/
	private String updateDate;
	/**删除标识 0:正常 1:删除*/
	private Integer delStatus;
	public MkDataConfigReleaseMonths(DbModel model) {
		super();
		this.setId(model.getInt("id"));
		this.setMonthsName(model.getString("monthsName"));
		this.setType(model.getInt("type"));
		this.setMonthsValue(model.getString("monthsValue"));
		this.setMoney(model.getString("money"));
		this.setSortNo(model.getInt("sortNo"));
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
	public String getMonthsName() {
		return monthsName;
	}
	public void setMonthsName(String monthsName) {
		this.monthsName = monthsName;
	}
	public String getMonthsValue() {
		return monthsValue;
	}
	public void setMonthsValue(String monthsValue) {
		this.monthsValue = monthsValue;
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
}