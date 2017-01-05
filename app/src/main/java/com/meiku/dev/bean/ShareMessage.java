package com.meiku.dev.bean;

public class ShareMessage {

	private String shareTitle;
	private String shareContent;
	private String shareImage;
	private String shareUrl;
	/** 本地打开需要的参数 */
	private String key;
	/** 0本地打开，1 网页打开 */
	private int openType;
	/** 1卡片（个人名片）,2资讯、帖子,3赛事活动 ,4参赛秀场作品,5=一般作品 */
	private int shareType;

	public ShareMessage(String shareTitle, String shareContent,
			String shareImage, String shareUrl, String key, int openType,int shareType) {
		this.shareTitle = shareTitle;
		this.shareContent = shareContent;
		this.shareImage = shareImage;
		this.shareUrl = shareUrl;
		this.key = key;
		this.openType = openType;
		this.shareType = shareType;
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

	public String getShareImage() {
		return shareImage;
	}

	public void setShareImage(String shareImage) {
		this.shareImage = shareImage;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getOpenType() {
		return openType;
	}

	public void setOpenType(int openType) {
		this.openType = openType;
	}
	

}
