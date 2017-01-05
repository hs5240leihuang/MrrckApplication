package com.meiku.dev.bean;

import java.util.List;

/**
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：话题实体 类名称：com.mrrck.ap.bd.entity.TopicEntity 创建人：曙光 创建时间：2015-10-27
 * 上午09:04:24
 * 
 * @version V1.0
 */
public class TopicEntity extends MkTopic {

	/** 话题list */
	private List<TopicEntity> topicList;

	public List<TopicEntity> getTopicList() {
		return this.topicList;
	}

	public void setTopicList(List<TopicEntity> topicList) {
		this.topicList = topicList;
	}

}
