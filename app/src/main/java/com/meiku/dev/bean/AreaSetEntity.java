package com.meiku.dev.bean;

import java.util.List;

public class AreaSetEntity extends MkArea{
	/** 是否有全国选项 */
	private Integer isNational;
	
	/**父级地区名称*/
	private String parentCityName;
	
	private String pCode;
	
	private List<AreaContainEntity> containAreaList;

	public Integer getIsNational() {
		return isNational;
	}

	public void setIsNational(Integer isNational) {
		this.isNational = isNational;
	}

	public String getParentCityName() {
		return parentCityName;
	}

	public void setParentCityName(String parentCityName) {
		this.parentCityName = parentCityName;
	}

	public List<AreaContainEntity> getContainAreaList() {
		return containAreaList;
	}

	public void setContainAreaList(List<AreaContainEntity> containAreaList) {
		this.containAreaList = containAreaList;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
}
