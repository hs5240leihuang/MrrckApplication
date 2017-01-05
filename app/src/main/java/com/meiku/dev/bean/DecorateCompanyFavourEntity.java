package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修公司认证表实惠扩展表拓展 类名称：com.mrrck.db.entity.MkDecorateComapanyFavour 创建人：陈卫
 * 创建时间：2016-8-31 下午02:36:23
 * 
 * @version V1.0
 */
public class DecorateCompanyFavourEntity extends MkDecorateCompanyFavour {
	/** 分页页数 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 优惠详情list */
	List<DecorateCompanyFavourContentEntity> favourContentList;

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

	public List<DecorateCompanyFavourContentEntity> getFavourContentList() {
		return favourContentList;
	}

	public void setFavourContentList(
			List<DecorateCompanyFavourContentEntity> favourContentList) {
		this.favourContentList = favourContentList;
	}
}
