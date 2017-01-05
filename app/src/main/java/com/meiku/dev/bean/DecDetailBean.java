package com.meiku.dev.bean;

import com.google.gson.JsonArray;

public class DecDetailBean {

	private String title;
	private String remark;
	private JsonArray fileUrlJSONArray;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public JsonArray getFileUrlJSONArray() {
		return fileUrlJSONArray;
	}
	public void setFileUrlJSONArray(JsonArray jsonArray) {
		this.fileUrlJSONArray = jsonArray;
	}
	
}
