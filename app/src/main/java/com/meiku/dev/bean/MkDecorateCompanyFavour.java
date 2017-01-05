package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修公司认证表实惠扩展表 类名称：com.mrrck.db.entity.MkDecorateComapanyFavour 创建人：陈卫
 * 创建时间：2016-8-31 下午02:36:23
 * 
 * @version V1.0
 */
public class MkDecorateCompanyFavour {
	/** 编号 */
	private Integer id;
	/** 用户编号 */
	private Integer userId;
	/** 公司编号 */
	private Integer compnayId;
	/** 认证装修公司编号 */
	private Integer decorateCompnayId;
	/** 优惠信息 */
	private String favourTitle;
	/** 更新时间 */
	private String updateDate;
	/** 创建时间 */
	private String createDate;
	/** 删除标记 0 正常 1 删除 */
	private Integer delStatus;
	/** 是否开启0开启1关闭 */
	private Integer isOpen;

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDecorateCompnayId() {
		return decorateCompnayId;
	}

	public void setDecorateCompnayId(Integer decorateCompnayId) {
		this.decorateCompnayId = decorateCompnayId;
	}

	public String getFavourTitle() {
		return favourTitle;
	}

	public void setFavourTitle(String favourTitle) {
		this.favourTitle = favourTitle;
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

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getCompnayId() {
		return compnayId;
	}

	public void setCompnayId(Integer compnayId) {
		this.compnayId = compnayId;
	}

}
