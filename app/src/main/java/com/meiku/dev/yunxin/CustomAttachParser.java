package com.meiku.dev.yunxin;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.meiku.dev.utils.JsonUtil;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements MsgAttachmentParser {

	private static final String KEY_TYPE = "type";
	private static final String KEY_DATA = "data";

	@Override
	public MsgAttachment parse(String json) {
		CustomAttachment attachment = null;
		try {
			JsonObject object;
			object = JsonUtil.String2Object(json);
			int type = object.get(KEY_TYPE).getAsInt();
			switch (type) {
			case CustomAttachmentType.Share:
				attachment = new ShareAttachment();
				break;
			case CustomAttachmentType.Card:
				attachment = new CardAttachment();
				break;
			case CustomAttachmentType.System:
				attachment = new SystemAttachment();
				break;
			case CustomAttachmentType.SysTip:
				attachment = new TipAttachment();
				break;
			}
			String data = "";
			if (object.get(KEY_DATA).isJsonObject()) {
				data = object.get(KEY_DATA).toString();
			} else if (object.get(KEY_DATA).isJsonPrimitive()) {
				data = object.get(KEY_DATA).getAsString();
			}
			if (attachment != null) {
				attachment.fromJson(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}

	public static String packData(int type, String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_TYPE, type);
		if (data != null) {
			map.put(KEY_DATA, data);
		}
		return JsonUtil.hashMapToJson(map);
	}
}
