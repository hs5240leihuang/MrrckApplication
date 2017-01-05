package com.meiku.dev.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 才艺秀及赛事使用
 * @author Administrator
 *
 */
public class RefreshObs {

	private static RefreshObs instense;

	// 静态工厂方法
	public static RefreshObs getInstance() {
		if (null == instense) {
			instense = new RefreshObs();
		}
		return instense;
	}

	public interface NeedRefreshListener {

		public void onPageRefresh();

		public void getFirstPageData(int key);
	}

	private HashMap<String, NeedRefreshListener> listerMap = new HashMap<String, NeedRefreshListener>();

	public void registerListener(String key, NeedRefreshListener listener) {
		listerMap.put(key, listener);
	}

	public void UnRegisterListener(String key) {
		if (listerMap.containsKey(key)) {
			listerMap.remove(key);
		}
	}

	public void notifyAllLisWithTag(String tag) {
		Iterator<Entry<String, NeedRefreshListener>> iter = listerMap
				.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (((String) entry.getKey()).contains(tag)) {
				((NeedRefreshListener) entry.getValue()).onPageRefresh();
			}
		}
	}

	public void notifyOneGetFirstPageData(String tag, int key) {
		Iterator<Entry<String, NeedRefreshListener>> iter = listerMap
				.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (((String) entry.getKey()).contains(tag)) {
				((NeedRefreshListener) entry.getValue()).getFirstPageData(key);
			}
		}
	}
}
