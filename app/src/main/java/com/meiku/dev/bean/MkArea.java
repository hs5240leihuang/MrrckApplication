package com.meiku.dev.bean;

public class MkArea {

 	/** 行政区划id */
	private Integer id;

 	/** 行政区划编码 */
	private Integer cityCode;

 	/** 行政区划名称 */
	private String cityName;

 	/** 上级编码 */
	private Integer parentCode;

 	/** 级别 1：省、直辖市，2：市，3：区县 */
	private String level;

 	/** 排序 */
	private Integer sortNo;

 	/** 创建时间 */
	private String createDate;

 	/** 删除标识 0:正常 1:删除*/
	private String delStatus;
	
	/**是否热门标识位0:普通 1:热门*/
	private String isHot;

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setCityCode(Integer cityCode){
		this.cityCode=cityCode;
	}

	public Integer getCityCode(){
		return cityCode;
	}

	public void setCityName(String cityName){
		this.cityName=cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setParentCode(Integer parentCode){
		this.parentCode=parentCode;
	}

	public Integer getParentCode(){
		return parentCode;
	}

	public void setLevel(String level){
		this.level=level;
	}

	public String getLevel(){
		return level;
	}

	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}

	public Integer getSortNo(){
		return sortNo;
	}

	public void setCreateDate(String createDate){
		this.createDate=createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setDelStatus(String delStatus){
		this.delStatus=delStatus;
	}

	public String getDelStatus(){
		return delStatus;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
}
