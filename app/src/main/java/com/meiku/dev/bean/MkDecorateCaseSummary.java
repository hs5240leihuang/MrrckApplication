package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：找装修概述描述
 * 类名称：com.mrrck.db.entity.MkDecorateCaseSummary     
 * 创建人：陈卫
 * 创建时间：2016-8-30 下午05:32:00   
 * @version V1.0
 */
public class MkDecorateCaseSummary{
	/** 编号 */
	private Integer id;
	/** 概述名称 */
	private String title;
	/** 概述内容*/
	private String remark;
	/** 案例id */
	private Integer caseId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	
	
}
