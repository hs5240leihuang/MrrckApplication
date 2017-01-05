package com.meiku.dev.utils;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * 文件相关常量
 * 
 * @author sy
 * 
 */
public class FileConstant {
	/** 缓存目录 */
	public static String CacheFilePath;
	/** App使用的DB名称 */
	public static String APP_DB_NAME = "app.db";

	/** app文件夹 */
	public static String APP_PATH = "";

	/** Sqlite数据库路径 */
	public static String DB_PATH = "";

	/** 日志路径 */
	public static String LOG_PATH = "";
	/** 赛事广告路径 */
	public static String MATCHAD_PATH;
	public static boolean sdCardIsExist;

	/** SD卡路径 */
	public static String SD_PATH;

	/** 客户端是否是首次启动 */
	public static boolean isFirstStartUp = true;

	/** 当前接入点uri */
	public static final Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	/** 录音上传缓存路径(sd卡) */
	public static String UPLOAD_AUDIO_PATH = "";

	/** 图片上传缓存路径(sd卡) */
	public static String UPLOAD_PHOTO_PATH = "";

	/** 视频上传缓存路径(sd卡) */
	public static String UPLOAD_VIDEO_PATH = "";

	/** 拍照 */
	public static final int PHOTOCAPTURE = 1;

	/** 相册 */
	public static final int PHOTOALBUM = 2;

	/** 缩放 结果 */
	public static final int PHOTORESOULT = 3;

	/** 视频录制 */
	public static final int VIDEORECORD = 4;

	/** 视频播放 */
	public static final int VIDEOSHOW = 5;

	/** 保存的数据库文件名 */
	// public static final String DB_NAME = "xUtils.db";
	public static final String DB_NAME = "mrrckserver.db";
	/** 包名 */
	public static final String PACKAGE_NAME = "com.meiku.dev";

	/**
	 * 在手机里存放下载的服务端数据库的位置
	 */
	public static final String LOCALDB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases";

	/**
	 * 瞬时拍照时存图片的路径及名称全路径
	 */
	public static String TAKEPHOTO_PATH = "";

	/**
	 * @param Context
	 *            context
	 * @param app_name
	 */
	public FileConstant(Context context, String app_name) {
		// 判断SD卡存在
		sdCardIsExist = SdcardUtil.isExistSD();
		// SD卡根目录
		SD_PATH = SdcardUtil.getPath();
		// 应用目录 /apname/
		APP_PATH = File.separator + app_name + File.separator;
		// 缓存完整目录
		CacheFilePath = SD_PATH + APP_PATH + "imgsCache/";

		DB_PATH = APP_PATH + "DB/";
		MATCHAD_PATH = APP_PATH + "MatchAD/";
		UPLOAD_PHOTO_PATH = APP_PATH + "uploadPhoto/";
		UPLOAD_AUDIO_PATH = APP_PATH + "uploadAudio/";
		UPLOAD_VIDEO_PATH = APP_PATH + "uploadVideo/";
	}

	/**
	 * @param sdCardIsExist
	 * @param sd_path
	 */
	public void setSD_data(boolean sdCardIsExist, String sd_path) {
		this.sdCardIsExist = sdCardIsExist;
		SD_PATH = sd_path;
	}

}
