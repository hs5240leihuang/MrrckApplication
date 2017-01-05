package com.meiku.dev.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修(新版)案例拓展实体类 类名称：com.mrrck.db.entity.MkDecorateCase 创建人：陈卫
 * 创建时间：2016-8-30 下午05:32:00
 * 
 * @version V1.0
 */
public class DecorateCaseEntity extends MkDecorateCase {
	/** 分页页数 */
	private Integer offset;
	/** 每页条数 */
	private Integer pageNum;
	/** 用户昵称 */
	private String nickName;
	/** 用户头像 */
	private String headPicUrl;
	/** 用户头像缩略图 */
	private String clientHeadPicUrl;
	/** 商户分类名称 */
	private String shopCategoryName;
	/** 公司名称 */
	private String companyName;
	/** 用于终端展示的时间显示 */
	private String clientUpdateDate;
	/** 用于终端面积和价格显示 */
	private String showAreaSizeAndPice;
	/** 描述集合 */
	private List<DecorateCaseSummaryEntity> dcsList;
	/** 公司用户联系方式称 */
	private String phone;
	/** 收藏的id */
	private Integer DCOCCId;
	/** 案例分享参数 **/
	private String shareTitle;
	private String shareContent;
	private String shareImg;
	private String shareUrl;
	private String loadUrl;
	/** 案例分享参数 **/
	/** 案例主图图片 */
	private String clientCaseImgFileUrl;
	/** 案例主图图片缩略图 */
	private String clientCaseImgFileUrlThumb;
	/** 发布案例的城市集合 */
	private List<DecorateOrderCityContentEntity> decorateOrderCityContentEntityList = new ArrayList<DecorateOrderCityContentEntity>();
	/** 用于终端展示名称 */
	private String showCityName;

	public String getShowCityName() {
		return showCityName;
	}

	public void setShowCityName(String showCityName) {
		this.showCityName = showCityName;
	}

	public List<DecorateOrderCityContentEntity> getDecorateOrderCityContentEntityList() {
		return decorateOrderCityContentEntityList;
	}

	public void setDecorateOrderCityContentEntityList(
			List<DecorateOrderCityContentEntity> decorateOrderCityContentEntityList) {
		this.decorateOrderCityContentEntityList = decorateOrderCityContentEntityList;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public String getShopCategoryName() {
		return shopCategoryName;
	}

	public void setShopCategoryName(String shopCategoryName) {
		this.shopCategoryName = shopCategoryName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getClientUpdateDate() {
		return clientUpdateDate;
	}

	public void setClientUpdateDate(String clientUpdateDate) {
		this.clientUpdateDate = clientUpdateDate;
	}

	public String getShowAreaSizeAndPice() {
		return showAreaSizeAndPice;
	}

	public void setShowAreaSizeAndPice(String showAreaSizeAndPice) {
		this.showAreaSizeAndPice = showAreaSizeAndPice;
	}

	public List<DecorateCaseSummaryEntity> getDcsList() {
		return dcsList;
	}

	public void setDcsList(List<DecorateCaseSummaryEntity> dcsList) {
		this.dcsList = dcsList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getDCOCCId() {
		return DCOCCId;
	}

	public void setDCOCCId(Integer dCOCCId) {
		DCOCCId = dCOCCId;
	}

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

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	public String getClientCaseImgFileUrl() {
		return clientCaseImgFileUrl;
	}

	public void setClientCaseImgFileUrl(String clientCaseImgFileUrl) {
		this.clientCaseImgFileUrl = clientCaseImgFileUrl;
	}

	public String getClientCaseImgFileUrlThumb() {
		return clientCaseImgFileUrlThumb;
	}

	public void setClientCaseImgFileUrlThumb(String clientCaseImgFileUrlThumb) {
		this.clientCaseImgFileUrlThumb = clientCaseImgFileUrlThumb;
	}
}
