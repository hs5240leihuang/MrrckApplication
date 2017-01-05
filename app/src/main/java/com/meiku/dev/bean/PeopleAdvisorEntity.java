package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：达人导师团 Entity 类名称：com.mrrck.ap.sf.entity.PeopleAdvisorEntity 创建人：仲崇生
 * 创建时间：2015-12-22 下午02:18:53
 * 
 * @version V1.0
 */
public class PeopleAdvisorEntity extends MkPeopleAdvisor {

	/** 起始数 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 广告推广logo(客户端使用) */
	private String clientAdvertLogo;

	/** 广告推广整图(客户端使用) */
	private String clientAdvertImgUrl;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getClientAdvertLogo() {
		return clientAdvertLogo;
	}

	public void setClientAdvertLogo(String clientAdvertLogo) {
		this.clientAdvertLogo = clientAdvertLogo;
	}

	public String getClientAdvertImgUrl() {
		return clientAdvertImgUrl;
	}

	public void setClientAdvertImgUrl(String clientAdvertImgUrl) {
		this.clientAdvertImgUrl = clientAdvertImgUrl;
	}

}
