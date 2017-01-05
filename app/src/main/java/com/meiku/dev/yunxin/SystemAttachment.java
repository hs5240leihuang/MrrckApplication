package com.meiku.dev.yunxin;

import java.util.Map;

import com.meiku.dev.utils.JsonUtil;

public class SystemAttachment extends CustomAttachment {
	private String msgType;// =200跳转H5
	private String groupid;
	private String msg;
	private String h5Url;
	private String sourceId;// 对应业务的资源ID

	SystemAttachment() {
		super(CustomAttachmentType.System);
	}

	@Override
	protected void parseData(String data) {
		try {
			Map<String, String> map = JsonUtil.jsonToMap(data);
			msgType = map.get("msgType");
			groupid = map.get("groupid");
			msg = map.get("msg");
			h5Url = map.get("h5Url");
			sourceId = map.get("sourceId");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected String packData() {
		return null;
	}

	public String getMsgType() {
		return msgType;
	}

	public String getGroupid() {
		return groupid;
	}

	public String getMsg() {
		return msg;
	}

	public String getH5Url() {
		return h5Url;
	}

	public String getSourceId() {
		return sourceId;
	}

}
