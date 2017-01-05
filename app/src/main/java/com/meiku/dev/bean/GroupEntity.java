package com.meiku.dev.bean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：群聊Entity 类名称：com.mrrck.ap.gp.entity.GroupEntity 创建人：仲崇生 创建时间：2015-12-10
 * 上午10:24:19
 * 
 * @version V1.0
 */
public class GroupEntity extends MkGroup {
	/** 当前登录人昵称(OR 群主昵称) */
	private String nickName;
	private String otherId;
	/** 群聊头像(客户端使用) */
	private String clientGroupPhoto;

	/** 群聊头像(客户端使用) */
	private String clientThumbGroupPhoto;

	 /**终端显示的群tags*/
    private Map<String,String> tagsName;
    
	public String getClientThumbGroupPhoto() {
		return clientThumbGroupPhoto;
	}

	public void setClientThumbGroupPhoto(String clientThumbGroupPhoto) {
		this.clientThumbGroupPhoto = clientThumbGroupPhoto;
	}

	private String groupNo;

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	/** 群成员列表List */
	List<GroupUserEntity> groupUserList;

	public String getClientGroupPhoto() {
		return this.clientGroupPhoto;
	}

	public void setClientGroupPhoto(String clientGroupPhoto) {
		this.clientGroupPhoto = clientGroupPhoto;
	}

	public List<GroupUserEntity> getGroupUserList() {
		return this.groupUserList;
	}

	public void setGroupUserList(List<GroupUserEntity> groupUserList) {
		this.groupUserList = groupUserList;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Map<String, String> getTagsName() {
		return tagsName;
	}

	public void setTagsName(Map<String, String> tagsName) {
		this.tagsName = tagsName;
	}

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

}
