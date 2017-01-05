package com.meiku.dev.bean;

/**
 * 广告数据
 */
public class AdvertiseBannerEntity {
	 /** 广告标题 */
    private String title;

    /** 广告内容(备用) */
    private String content;

    /** 外部url */
    private String url;

    /** 图片url路径 */
    private String imgUrl;

    /** 省份code */
    private Integer provinceCode;

    /** 城市code */
    private Integer cityCode;

    /** 数据来源 1:来自后台管理员 2:来自企业者中心 */
    private Integer sourceType;
	
    /** 客户端使用 广告图片 */
	private String clientImgUrl;
	/** 判断是客户端打开，还是h5打开 */
	private int isClientApp;

	public int getIsClientApp() {
		return isClientApp;
	}

	public void setIsClientApp(int isClientApp) {
		this.isClientApp = isClientApp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getClientImgUrl() {
		return clientImgUrl;
	}

	public void setClientImgUrl(String clientImgUrl) {
		this.clientImgUrl = clientImgUrl;
	}
	
}
