package com.meiku.dev.bean;

import java.io.Serializable;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修公司认证表实惠扩展表拓展 类名称：com.mrrck.db.entity.MkDecorateComapanyFavour 创建人：陈卫
 * 创建时间：2016-8-31 下午02:36:23
 * 
 * @version V1.0
 */
public class DecorateCompanyFavourContentEntity extends MkDecorateCompanyFavourContent implements Serializable{
	/** 分页页数 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

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

}
