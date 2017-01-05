package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

public class ReportType {
	
	private int id;
	private String name;
	private String remark;
	private String delStatus;
	
	public ReportType(DbModel model) {
        super();
        this.setId(model.getInt("id"));
        this.setName(model.getString("name"));
        this.setRemark(model.getString("remark"));
        this.setDelStatus(model.getString("delStatus"));
    }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}
	
}
