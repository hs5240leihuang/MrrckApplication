package com.meiku.dev.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck   
 *
 * 类描述：sdCard
 * 类名称：com.meiku.dev.utils.SdcardUtil     
 * 创建人：admin
 * 创建时间：2015-10-30 上午10:23:25   
 * @version V3.0
 */
public class SdcardUtil {
	public static String filepath = Environment.getExternalStorageDirectory()
			.getPath();

	/*
	 * Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
	 */
	/**
	 * 获取手机外置SD卡的根目录
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {

		//Map<String, String> evn = System.getenv();
		return Environment.getExternalStorageDirectory().getAbsolutePath();
		
	
	}

	/**
	 * 根据条件获取手机SD卡的根目录
	 * 
	 * @return
	 */
	public static String getPath() {
		if (getExternalSDRoot() != null) {
			if (new File(getExternalSDRoot()).length() != 0) {
				return getExternalSDRoot();
			}
		}
		return filepath;

	}
	
	
	/**
	 * 判断SD卡是否存在
	 * 
	 * @throws SDUnavailableException
	 */
	public static boolean isExistSD() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			FileConstant.SD_PATH = Environment.getExternalStorageDirectory()
					.toString();
			return true;
		}
		return false;
	}

	/**
	 * 获取SD卡的剩余空间
	 * 
	 * @return SD卡的剩余的字节数
	 */
	@SuppressWarnings("deprecation")
	public static long getFreeSD() {
		long nAvailableCount = 0l;
		try {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			nAvailableCount = (long) (stat.getBlockSize() * ((long) stat
					.getAvailableBlocks() - FileHelper.SDCARD_SYSTEM));
		} catch (Exception e) {
			// Log.i("getFreeSD() Exception", "in FileHelper.java");
		}
		return nAvailableCount;
	}
	
	
	/**
	 * 对sdcard的检查，主要是检查sd是否可用，并且sd卡的存储空间是否充足
	 * 
	 * @param io
	 *            写入sd卡的数据
	 * @throws SDUnavailableException
	 * @throws SDNotEnouchSpaceException
	 */
	public static void checkSD(byte[] io) throws SDUnavailableException,
			SDNotEnouchSpaceException {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// throw new SDUnavailableException("sd_Unavailable");
		} else {
			if (io.length >= SdcardUtil.getFreeSD()) {
				// 通知UI
				// DownloadTaskManager.getInstance().sdNotifyUI();
				// throw new SDNotEnouchSpaceException(/*
				// * LogicFacade.getText(
				// * "|:sd_NotEnoughSpace")
				// */"sd_NotEnoughSpace");

			}
		}
	}
	
	@SuppressWarnings("serial")
	public class SDUnavailableException extends Exception {
		private String msg = null;

		/**
		 * 构造方法
		 * 
		 * @param msg
		 *            异常信息
		 */
		public SDUnavailableException(String msg) {
			this.msg = msg;
		}

		/**
		 * 重写的方法，获取异常信息
		 * 
		 * @return 异常信息
		 */
		@Override
		public String getMessage() {
			return msg;
		}

	}

	@SuppressWarnings("serial")
	public class SDNotEnouchSpaceException extends Exception {
		private String msg = null;

		/**
		 * 构造方法
		 * 
		 * @param msg
		 *            异常信息
		 */
		public SDNotEnouchSpaceException(String msg) {
			this.msg = msg;
		}

		/**
		 * 重写的方法，获取异常信息
		 * 
		 * @return 异常信息
		 */
		@Override
		public String getMessage() {
			return msg;
		}

	}
}
