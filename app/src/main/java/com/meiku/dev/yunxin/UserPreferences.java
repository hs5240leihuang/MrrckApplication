package com.meiku.dev.yunxin;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.config.AppContext;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hzxuwen on 2015/10/21.
 */
public class UserPreferences {

	private final static String KEY_EARPHONE_MODE = "KEY_EARPHONE_MODE";

	public static void setEarPhoneModeEnable(boolean on) {
		saveBoolean(KEY_EARPHONE_MODE, on);
	}

	public static boolean isEarPhoneModeEnable() {
		return getBoolean(KEY_EARPHONE_MODE, true);
	}

	private static boolean getBoolean(String key, boolean value) {
		return getSharedPreferences().getBoolean(key, value);
	}

	private static void saveBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	static SharedPreferences getSharedPreferences() {
		return MrrckApplication.getInstance().getSharedPreferences(
				"UIKit." + AppContext.getInstance().getUserInfo().getId(),
				Context.MODE_PRIVATE);
	}
}
