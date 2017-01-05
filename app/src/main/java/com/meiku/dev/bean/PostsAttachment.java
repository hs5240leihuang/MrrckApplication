package com.meiku.dev.bean;

import java.io.Serializable;


/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：帖子附件(2016-07-07 15:35:23 jsg 后添加,用于列表扩展)
 * 类名称：com.mrrck.db.entity.MkPostsAttachment 创建人：曙光 创建时间：2016-7-7 下午03:35:09
 * 
 * @version V1.0
 */
public class PostsAttachment extends MkPostsAttachment implements Serializable{

	/** 用于客户端文件url地址 */
	private String clientFileUrl;

	public String getClientPicUrl() {
		return this.clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

}
