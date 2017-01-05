 package com.meiku.dev.bean;

import com.google.gson.JsonObject;

/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：群聊消息保存记录表
 * 类名称：com.mrrck.ap.jc.entity.OfChatGroupLogs     
 * 创建人：陈卫
 * 创建时间：2016-8-29 下午02:20:21   
 * @version V1.0
 */
public class OfChatGroupLogs  {
 
	 private String messageId ;	// '消息唯一性ID，终端生成回执用',
	 private String sessionJID; //'本次消息JID',
	 private String  sender; 	//'消息发送人',
	 private String  groupId;	// '群ID',
	 private String createDate; // '消息保存时间',
	 private JsonObject content; 	// '消息里的BODY',
	 
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSessionJID() {
		return sessionJID;
	}
	public void setSessionJID(String sessionJID) {
		this.sessionJID = sessionJID;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public JsonObject getContent() {
		return content;
	}
	public void setContent(JsonObject content) {
		this.content = content;
	}
 
 
 
	 
	
}
