package com.meiku.dev.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.meiku.dev.MrrckApplication;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck
 * 
 * 类描述： 类名称：com.meiku.dev.utils.PreferHelper 创建人：admin 创建时间：2015-10-30
 * 上午10:22:12
 * 
 * @version V3.0
 */
public class PreferHelper {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "MRRCK_DATA";

	/**
	 * 设置缓存数据
	 */
	public static void setSharedParam(String key, Object object) {
		String type = object.getClass().getSimpleName();
		SharedPreferences sp = MrrckApplication.getInstance()
				.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if ("String".equals(type)) {
			editor.putString(key, (String) object);
		} else if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) object);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) object);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) object);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, (String) object);
		}
		editor.commit();
	}

	/**
	 * 获取缓存数据
	 */
	public static Object getSharedParam(String key, Object defaultObject) {
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = MrrckApplication.getInstance()
				.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		if ("String".equals(type)) {
			return sp.getString(key, (String) defaultObject);
		} else if ("Integer".equals(type)) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if ("Boolean".equals(type)) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if ("Float".equals(type)) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if ("Long".equals(type)) {
			return sp.getLong(key, (Long) defaultObject);
		} else {
			return sp.getString(key, (String) defaultObject);
		}
	}

}
