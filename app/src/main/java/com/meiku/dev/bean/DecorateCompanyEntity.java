package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：验证的装修企业拓展 类名称：com.mrrck.db.entity.MkDecorateCompany 创建人：陈卫 创建时间：2016-8-31
 * 下午02:33:54
 * 
 * @version V1.0
 */
public class DecorateCompanyEntity extends MkDecorateCompany {
	private Integer DCOCCId;
	/** 分页页数 */
	private Integer offset;
	/** 每页条数 */
	private Integer pageNum;
	/** 公司名称 */
	private String name;
	/** 公司log */
	private String advertLogo;
	/** 公司log缩略图 */
	private String clientAdvertLogo;
	/** 推荐企业图片URL 终端用 */
	private String clientRecommendImgFileUrl;
	/** 推荐企业图片URL 终端缩略图片用 */
	private String clientThumbRecommendImgFileUrl;
	/** 公司地址 */
	private String address;
	/** 优惠信息集合 */
	List<DecorateCompanyFavourEntity> dcfList;
	/** 优惠详情信息集合 */
	List<DecorateCompanyFavourContentEntity> dcfcList;
	/**公司log缩略图*/
	private String clientCompanyLogo;
	/** 客户端自己添加 是否选中 */
	private boolean isSelect;
	private String shareTitle;
	private String shareContent;
	private String shareImg;
	private String shareUrl;

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getClientThumbRecommendImgFileUrl() {
		return clientThumbRecommendImgFileUrl;
	}

	public void setClientThumbRecommendImgFileUrl(
			String clientThumbRecommendImgFileUrl) {
		this.clientThumbRecommendImgFileUrl = clientThumbRecommendImgFileUrl;
	}

	public String getClientRecommendImgFileUrl() {
		return clientRecommendImgFileUrl;
	}

	public void setClientRecommendImgFileUrl(String clientRecommendImgFileUrl) {
		this.clientRecommendImgFileUrl = clientRecommendImgFileUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdvertLogo() {
		return advertLogo;
	}

	public void setAdvertLogo(String advertLogo) {
		this.advertLogo = advertLogo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<DecorateCompanyFavourEntity> getDcfList() {
		return dcfList;
	}

	public void setDcfList(List<DecorateCompanyFavourEntity> dcfList) {
		this.dcfList = dcfList;
	}

	public String getClientAdvertLogo() {
		return clientAdvertLogo;
	}

	public void setClientAdvertLogo(String clientAdvertLogo) {
		this.clientAdvertLogo = clientAdvertLogo;
	}

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

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public Integer getDCOCCId() {
		return DCOCCId;
	}

	public void setDCOCCId(Integer dCOCCId) {
		DCOCCId = dCOCCId;
	}

	public List<DecorateCompanyFavourContentEntity> getDcfcList() {
		return dcfcList;
	}

	public void setDcfcList(List<DecorateCompanyFavourContentEntity> dcfcList) {
		this.dcfcList = dcfcList;
	}

	public String getClientCompanyLogo() {
		return clientCompanyLogo;
	}

	public void setClientCompanyLogo(String clientCompanyLogo) {
		this.clientCompanyLogo = clientCompanyLogo;
	}

}
