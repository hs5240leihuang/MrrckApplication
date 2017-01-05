package com.meiku.dev.bean;

import com.google.gson.JsonObject;

 
/**
 * 请求和返回数据统一格式
 * @author 库
 *
 */
public class ReqBase {
	private ReqHead header;
	private JsonObject body;
	
	public ReqHead getHeader() {
		return header;
	}
	public void setHeader(ReqHead header) {
		this.header = header;
	}
	public JsonObject getBody() {
		return body;
	}
	public void setBody(JsonObject body) {
		this.body = body;
	}
	
}
