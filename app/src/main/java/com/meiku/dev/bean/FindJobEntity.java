package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：找工作
 * 类名称：com.mrrck.ap.ht.entity.FindJobEntity     
 * 创建人：韩非
 * 创建时间：2015-11-27 下午03:43:14   
 * @version V1.0
 */
public class FindJobEntity {
	//招聘职位列表
	private List<MkPosition> positionList;
	//公司列表
	private List<FindJobEntity> companyList;
	private Integer companyId;
	private String companyName;
	private String jobName;
	private String needNum;
	private String salaryValue;
	private String companyLogo;
	
	public List<MkPosition> getPositionList() {
		return positionList;
	}
	public void setPositionList(List<MkPosition> positionList) {
		this.positionList = positionList;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getNeedNum() {
		return needNum;
	}
	public void setNeedNum(String needNum) {
		this.needNum = needNum;
	}
	public String getSalaryValue() {
		return salaryValue;
	}
	public void setSalaryValue(String salaryValue) {
		this.salaryValue = salaryValue;
	}
	public List<FindJobEntity> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<FindJobEntity> companyList) {
		this.companyList = companyList;
	}
	public String getCompanyLogo() {
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}
	

}
