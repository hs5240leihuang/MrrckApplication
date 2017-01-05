package com.meiku.dev.yunxin;

import java.util.HashMap;
import java.util.Map;

import com.meiku.dev.utils.JsonUtil;

public class TipAttachment extends CustomAttachment {
	private String msg;

	TipAttachment() {
		super(CustomAttachmentType.SysTip);
	}

	public TipAttachment(String msg) {
		this();
		this.msg = msg;
	}

	@Override
	protected void parseData(String data) {
		try {
			Map<String, String> map = JsonUtil.jsonToMap(data);
			msg = map.get("msg");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected String packData() {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("msg", msg);
		return JsonUtil.hashMapToJson(map);
	}

	public String getMsg() {
		return msg;
	}

}
