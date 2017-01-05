package com.meiku.dev.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 未读消息监听器管理类
 * 
 */
public class MessageObs {

	private static MessageObs instense;

	// 静态工厂方法
	public static MessageObs getInstance() {
		if (null == instense) {
			instense = new MessageObs();
		}
		return instense;
	}

	public interface MessageSizeChageListener {

		public void onMsgSizeChange(int unreadMessageSize);
	}

	private List<MessageSizeChageListener> listerList = new ArrayList<MessageSizeChageListener>();

	public void registerListener(MessageSizeChageListener listener) {
		if (listerList != null && listener != null) {
			listerList.add(listener);
		}
	}

	public void notifyAllLis(int unreadMessageSize) {
		for (MessageSizeChageListener listener : listerList) {
			listener.onMsgSizeChange(unreadMessageSize);
		}
	}

	public void clearMessageListaner() {
		listerList.clear();
	}

}
