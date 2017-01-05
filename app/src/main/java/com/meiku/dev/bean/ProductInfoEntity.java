package com.meiku.dev.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找产品Entity 类名称：com.mrrck.ap.pd.entity.ProductEntity 创建人：仲崇生 创建时间：2016-2-29
 * 下午03:44:07
 * 
 * @version V1.0
 */
public class ProductInfoEntity extends MkProductInfo implements Serializable{
	/** 产品编号,重命名 */
	private Integer productId;

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 公司名称 */
	private String companyName;

	/** 海报主图(客户端使用) */
	private String clientPosterMain;

	/** 海报缩略图(客户端使用) */
	private String clientPosterThum;

	/** 产品类型名称 */
	private String categoryName;

	/** 客户端显示时间 */
	private String clientRefreshDate;

	/** 产品自定义扩展内容list */
	private List<ProductDetailEntity> productDetailList;
	
	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOffset() {
		return this.offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return this.pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getClientPosterMain() {

		return this.clientPosterMain;

	}

	public void setClientPosterMain(String clientPosterMain) {
		this.clientPosterMain = clientPosterMain;
	}

	public String getClientPosterThum() {

		return this.clientPosterThum;

	}

	public void setClientPosterThum(String clientPosterThum) {
		this.clientPosterThum = clientPosterThum;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getClientRefreshDate() {

		return this.clientRefreshDate;

	}

	public void setClientRefreshDate(String clientRefreshDate) {
		this.clientRefreshDate = clientRefreshDate;
	}

	public List<ProductDetailEntity> getProductDetailList() {
		return this.productDetailList;
	}

	public void setProductDetailList(List<ProductDetailEntity> productDetailList) {
		this.productDetailList = productDetailList;
	}

	
	
}
