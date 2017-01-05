package com.meiku.dev.bean;

 

public class ReqBaseStr {
	private ReqHead header;
	private String body;
	
	public ReqHead getHeader() {
		return header;
	}
	public void setHeader(ReqHead header) {
		this.header = header;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

}
