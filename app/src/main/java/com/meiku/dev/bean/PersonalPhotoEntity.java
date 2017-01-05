package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：个人相册表Entity 类名称：com.mrrck.ap.us.entity.PersonalPhotoEntity 创建人：仲崇生
 * 创建时间：2016-5-26 下午02:50:22
 * 
 * @version V1.0
 */
public class PersonalPhotoEntity extends MkPersonalPhoto {

	/** 主键编号 重命名 */
	private Integer personalPhotoId;

	/** 用户个人相册ids */
	private String personalPhotoIds;

	/** 附件路径(客户端使用) */
	private String clientFileUrl;

	/** 附件路径缩略图(客户端使用) */
	private String clientThumbFileUrl;

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 客户端显示时间(客户端使用) */
	private String clientViewDate;
	private boolean isSelected;
	private boolean isTimeSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isTimeSelected() {
		return isTimeSelected;
	}

	public void setTimeSelected(boolean isTimeSelected) {
		this.isTimeSelected = isTimeSelected;
	}

	public Integer getPersonalPhotoId() {
		return this.personalPhotoId;
	}

	public void setPersonalPhotoId(Integer personalPhotoId) {
		this.personalPhotoId = personalPhotoId;
	}

	public String getPersonalPhotoIds() {
		return this.personalPhotoIds;
	}

	public void setPersonalPhotoIds(String personalPhotoIds) {
		this.personalPhotoIds = personalPhotoIds;
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

	public String getClientViewDate() {
		return this.clientViewDate;
	}

	public void setClientViewDate(String clientViewDate) {
		this.clientViewDate = clientViewDate;
	}

	public String getClientFileUrl() {
		return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public String getClientThumbFileUrl() {
		return clientThumbFileUrl;
	}

	public void setClientThumbFileUrl(String clientThumbFileUrl) {
		this.clientThumbFileUrl = clientThumbFileUrl;
	}

}
