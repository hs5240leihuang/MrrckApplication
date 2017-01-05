package com.meiku.dev.bean;

public class PersonalHonorConfigEntity extends MkPersonalHonorConfig {

	/** 用户荣誉配置ids */
	private String personalHonorConfigIds;

	/** 荣誉图片(客户端使用) */
	private String clientImgUrl;

	/** 荣誉图片小图(客户端使用) */
	private String clientThumbImgUrl;

	/** 是否选中 */
	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getPersonalHonorConfigIds() {
		return this.personalHonorConfigIds;
	}

	public void setPersonalHonorConfigIds(String personalHonorConfigIds) {
		this.personalHonorConfigIds = personalHonorConfigIds;
	}

	public String getClientImgUrl() {

		return this.clientImgUrl;

	}

	public void setClientImgUrl(String clientImgUrl) {
		this.clientImgUrl = clientImgUrl;
	}

	public String getClientThumbImgUrl() {

		return this.clientThumbImgUrl;

	}

	public void setClientThumbImgUrl(String clientThumbImgUrl) {
		this.clientThumbImgUrl = clientThumbImgUrl;
	}
}
