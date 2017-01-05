package com.meiku.dev.bean;

import java.io.Serializable;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：赛事对应赛区表 类名称：com.mrrck.db.entity.MkMatchCity 创建人：仲崇生 创建时间：2016-2-22
 * 上午10:15:59
 * 
 * @version V1.0
 */
public class MkMatchCity implements Serializable{

	/** 编号 */
	private Integer id;

	/** 用户编号(创建人) */
	private Integer userId;

	/** 赛事编号 */
	private Integer matchId;

	/** 赛区名称 */
	private String matchCityName;

	/** 赛区code代码 */
	private Integer matchCityCode;

	/** 赛区比赛开始时间 */
	private String matchStartDate;

	/** 赛区比赛结束时间 */
	private String matchEndDate;

	/** 赛区比赛年 */
	private String matchYear;

	/** 比赛月份 */
	private String matchMonth;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除状态0正常，1删除 */
	private Integer delStatus;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchCityName(String matchCityName) {
		this.matchCityName = matchCityName;
	}

	public String getMatchCityName() {
		return matchCityName;
	}

	public void setMatchCityCode(Integer matchCityCode) {
		this.matchCityCode = matchCityCode;
	}

	public Integer getMatchCityCode() {
		return matchCityCode;
	}

	public void setMatchStartDate(String matchStartDate) {
		this.matchStartDate = matchStartDate;
	}

	public String getMatchStartDate() {
		return matchStartDate;
	}

	public void setMatchEndDate(String matchEndDate) {
		this.matchEndDate = matchEndDate;
	}

	public String getMatchEndDate() {
		return matchEndDate;
	}

	public void setMatchYear(String matchYear) {
		this.matchYear = matchYear;
	}

	public String getMatchYear() {
		return matchYear;
	}

	public void setMatchMonth(String matchMonth) {
		this.matchMonth = matchMonth;
	}

	public String getMatchMonth() {
		return matchMonth;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

}
