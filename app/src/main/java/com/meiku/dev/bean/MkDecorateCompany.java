package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：验证的装修企业 类名称：com.mrrck.db.entity.MkDecorateCompany 创建人：陈卫 创建时间：2016-8-31
 * 下午02:33:54
 * 
 * @version V1.0
 */
public class MkDecorateCompany {
	/** 编号 */
	private int id;
	/** 用户编号 */
	private int userId;
	/** 公司编号 */
	private int companyId;
	/** 浏览量 */
	private int viewNum;
	/** 已服务店家数量 */
	private int serviceShopNum;
	/** 装修保是否认证 默认 0 已经认证 1 未认证 */
	private int protecteFlag;
	/** 推荐图片url地址 */
	private String recommendImgFileUrl;
	/** 好评数量 */
	private int highCommentNum;
	/** 是否推荐店铺 0 推荐 1不推荐 */
	private int recommendFlag;
	/** 优秀设计师个数 */
	private int designFineNum;
	/** 更新时间 */
	private String updateDate;
	/** 创建时间 */
	private String createDate;
	/** 删除标记 0 正常 1 删除 */
	private int delStatus;
	/**载入H5公司url地址*/
	private String loadUrl;
	/**是否已被美库认证 0:未认证 1:已认证*/
	private int mrrckAuthFlag;
	/**置顶标记  0  置顶  1 未置顶*/
	private int topFlag;
	
	public int getProtecteFlag() {
		return protecteFlag;
	}

	public void setProtecteFlag(int protecteFlag) {
		this.protecteFlag = protecteFlag;
	}

	public String getRecommendImgFileUrl() {
		return recommendImgFileUrl;
	}

	public void setRecommendImgFileUrl(String recommendImgFileUrl) {
		this.recommendImgFileUrl = recommendImgFileUrl;
	}

	public int getHighCommentNum() {
		return highCommentNum;
	}

	public void setHighCommentNum(int highCommentNum) {
		this.highCommentNum = highCommentNum;
	}

	public int getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(int recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	public int getDesignFineNum() {
		return designFineNum;
	}

	public void setDesignFineNum(int designFineNum) {
		this.designFineNum = designFineNum;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public int getServiceShopNum() {
		return serviceShopNum;
	}

	public void setServiceShopNum(int serviceShopNum) {
		this.serviceShopNum = serviceShopNum;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	public int getMrrckAuthFlag() {
		return mrrckAuthFlag;
	}

	public void setMrrckAuthFlag(int mrrckAuthFlag) {
		this.mrrckAuthFlag = mrrckAuthFlag;
	}

	public int getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(int topFlag) {
		this.topFlag = topFlag;
	}

}
