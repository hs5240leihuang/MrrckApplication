package com.meiku.dev.bean;

import java.security.Timestamp;

public class AreaContainEntity {
	private Integer id;

	private Integer cityCode;

	private String cityName;

	private Integer parentCode;

	private String level;

	private Integer sortNo;

	private Timestamp createDate;

	private String delStatus;
	private int delStatus1;


	public int getDelStatus1() {
		return delStatus1;
	}

	public void setDelStatus1(int delStatus1) {
		this.delStatus1 = delStatus1;
	}

	private String isHot;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getParentCode() {
		return this.parentCode;
	}

	public void setParentCode(Integer parentCode) {
		this.parentCode = parentCode;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getIsHot() {
		return this.isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
}
