package com.meiku.dev.utils;

import com.meiku.dev.MrrckApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * 当前设备的基本信息
 * @author 库
 *
 */
public class VersionUtils {
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * 获取当前应用的名称
	 * @param mContext
	 * @return
	 */
	public static String getVersionName(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获取设备ID
	 */
	public static String getDeviceId( ) {
		TelephonyManager tm = (TelephonyManager)MrrckApplication.getInstance().
				getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId(); 
		return DEVICE_ID;
	}
}
