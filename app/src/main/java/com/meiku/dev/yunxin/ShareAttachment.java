package com.meiku.dev.yunxin;

public class ShareAttachment extends CustomAttachment {

	private String shareJson;

	public ShareAttachment() {
		super(CustomAttachmentType.Share);
	}

	public ShareAttachment(String shareJson) {
		this();
		this.shareJson = shareJson;
	}

	@Override
	protected void parseData(String data) {
		shareJson = data;
	}

	@Override
	protected String packData() {
		return shareJson;
	}

	public String getShareJson() {
		return shareJson;
	}

	public void setShareJson(String shareJson) {
		this.shareJson = shareJson;
	}

}
