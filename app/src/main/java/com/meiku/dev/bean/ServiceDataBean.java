package com.meiku.dev.bean;

/**
 * 服务页面数据配置
 * 
 * @author Administrator
 * 
 */
public class ServiceDataBean {

	/**
	 * 类型
	 */
	private int type;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 说明
	 */
	private String remark;
	/**
	 * 点击更多条状的url
	 */
	private String moreUrl;

	public ServiceDataBean(int type, String name, String remark) {
		this.type = type;
		this.name = name;
		this.remark = remark;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoreUrl() {
		return moreUrl;
	}

	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
