package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：背景图配置Entity 类名称：com.mrrck.ap.common.entity.BackgroundConfigEntity 创建人：仲崇生
 * 创建时间：2016-3-22 下午04:48:36
 * 
 * @version V1.0
 */
public class BackgroundConfigEntity extends MkBackgroundConfig {

	/** 背景图URL(客户端使用) */
	private String clientPicUrl;

	/** 背景图URL缩略图(客户端使用) */
	private String clientThumbPicUrl;

	public String getClientPicUrl() {

		return this.clientPicUrl;

	}

	public void setClientPicUrl(String clientPicUrl) {
		this.clientPicUrl = clientPicUrl;
	}

	public String getClientThumbPicUrl() {

		return this.clientThumbPicUrl;
	}

	public void setClientThumbPicUrl(String clientThumbPicUrl) {
		this.clientThumbPicUrl = clientThumbPicUrl;
	}

}
