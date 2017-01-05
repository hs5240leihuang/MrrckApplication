package com.meiku.dev.yunxin;

import java.util.HashMap;
import java.util.Map;

import com.meiku.dev.utils.JsonUtil;

public class CardAttachment extends CustomAttachment {

	private String card_id;
	private String card_name;
	private String card_headImg;
	private String CARDID = "card_id";
	private String CARDNAME = "card_name";
	private String CARDHEADIMG = "card_headImg";
	
	CardAttachment() {
		super(CustomAttachmentType.Card);
	}

	public CardAttachment(String card_id, String card_name, String card_headImg) {
		this();
		this.card_id = card_id;
		this.card_name = card_name;
		this.card_headImg = card_headImg;
	}

	@Override
	protected void parseData(String data) {
		try {
			Map<String, String> map = JsonUtil.jsonToMap(data);
			card_id = map.get(CARDID);
			card_name = map.get(CARDNAME);
			card_headImg = map.get(CARDHEADIMG);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected String packData() {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put(CARDID, card_id);
    	map.put(CARDNAME, card_name);
    	map.put(CARDHEADIMG, card_headImg);
		return JsonUtil.hashMapToJson(map);
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_headImg() {
		return card_headImg;
	}

	public void setCard_headImg(String card_headImg) {
		this.card_headImg = card_headImg;
	}

}
