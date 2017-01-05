package com.meiku.dev.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *我的秀场使用，编辑及刷新使用 
 *
 */
public class DoEditObs {

	private static DoEditObs instense;

	// 静态工厂方法
	public static DoEditObs getInstance() {
		if (null == instense) {
			instense = new DoEditObs();
		}
		return instense;
	}

	public interface DoEditListener {
		public void doEdit(boolean isSelectModel);
		public void doRefresh();
	}

	private HashMap<String, DoEditListener> listerMap = new HashMap<String, DoEditListener>();

	public void registerListener(String key, DoEditListener listener) {
		listerMap.put(key, listener);
	}

	public void UnRegisterListener(String key) {
		if (listerMap.containsKey(key)) {
			listerMap.remove(key);
		}
	}
	
	public void clear() {
		if (listerMap!=null) {
			listerMap.clear();
		}
	}

	public void notifyDoEdit(boolean isSelectModel) {
		Iterator<Entry<String, DoEditListener>> iter = listerMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			((DoEditListener) entry.getValue()).doEdit(isSelectModel);
		}
	}
	
	public void notifyDoRefresh() {
		Iterator<Entry<String, DoEditListener>> iter = listerMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			((DoEditListener) entry.getValue()).doRefresh();
		}
	}

}
