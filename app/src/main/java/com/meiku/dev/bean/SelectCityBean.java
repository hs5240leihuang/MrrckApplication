package com.meiku.dev.bean;

import com.google.gson.JsonArray;

public class SelectCityBean {
	private String cityName;
	private int cityCode;
	private int parentCode;
	private JsonArray cityJsonArray;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getCityCode() {
		return cityCode;
	}
	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}
	public int getParentCode() {
		return parentCode;
	}
	public void setParentCode(int parentCode) {
		this.parentCode = parentCode;
	}
	public JsonArray getCityJsonArray() {
		return cityJsonArray;
	}
	public void setCityJsonArray(JsonArray cityJsonArray) {
		this.cityJsonArray = cityJsonArray;
	}
}
