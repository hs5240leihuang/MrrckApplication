package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：聊天室人员信息Entity 类名称：com.mrrck.ap.gp.entity.ChatRoomUserEntity 创建人：仲崇生
 * 创建时间：2015-12-28 下午02:33:03
 * 
 * @version V1.0
 */
public class ChatRoomUserEntity {

	/** 用户编号重命名 */
	private Integer userId;

	/** 昵称，显示在社交圈的名称 */
	private String nickName;

	/** 用户头像(120X120) */
	private String headPicUrl;

	/** 用户头像(120X120) (客户端使用) */
	private String clientHeadPicUrl;
	
	/** 用户头像缩略图 */
	private String clientThumbHeadPicUrl;

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPicUrl() {
		return this.headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getClientHeadPicUrl() {

		return this.clientHeadPicUrl;

	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

}
