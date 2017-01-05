package com.meiku.dev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.services.LocationService;
import com.meiku.dev.ui.activitys.HomeActivity;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.SystemUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.VersionUtils;
import com.meiku.dev.yunxin.CustomAttachParser;
import com.meiku.dev.yunxin.StorageUtil;
import com.meiku.dev.yunxin.TeamDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.PlatformConfig;

import dalvik.system.DexClassLoader;

//import com.alipay.euler.andfix.patch.PatchManager;

public class MrrckApplication extends Application {

	private static MrrckApplication mInstance = null;
	private Map<String, Activity> activityListMap = new HashMap<String, Activity>();
	public static String provinceCode = "320000"; // 默认省份江苏省
	public static String cityCode = "320100"; // 默认城市南京市
	public static double laitude = 0;// 32.053135默认经纬度设置在南京
	public static double longitude = 0; // 118.750479
	public static String cityName = "南京市";

	SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();

	public static String getLaitudeStr() {
		return laitude == 0 ? "" : String.valueOf(laitude);
	}

	public static String getLongitudeStr() {
		return longitude == 0 ? "" : String.valueOf(longitude);
	}

	/**
	 * Log or request TAG
	 */
	public static final String TAG = "VolleyPatterns";

	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;
	public LocationService locationService;

	// public PatchManager patchManager;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// dexTool();
		Fresco.initialize(this);// Fresco取图初始化
		initFileSys();
		initImageLoader(mInstance);
		SDKInitializer.initialize(mInstance);
		initApp();
		locationService = new LocationService(this);
		initSound();

		// SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
		NIMClient.init(this, loginInfo(), options());
		if (inMainProcess()) {
			StorageUtil.init(mInstance, null);
			buildDataCache();
			// 注册自定义消息附件解析器(分享，名片类自定义消息)
			NIMClient.getService(MsgService.class)
					.registerCustomAttachmentParser(new CustomAttachParser());
			NIMClient.toggleNotification(true);//是否托管消息提醒

		}
	}

	public boolean inMainProcess() {
		String packageName = getPackageName();
		String processName = SystemUtil.getProcessName(this);
		return packageName.equals(processName);
	}

	// 如果返回值为 null，则全部使用默认参数。
	private SDKOptions options() {
		SDKOptions options = new SDKOptions();

		// 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
		StatusBarNotificationConfig config = new StatusBarNotificationConfig();
		config.notificationEntrance = HomeActivity.class; // 点击通知栏跳转到该Activity
		config.notificationSmallIconId = R.drawable.mrrck_logo;
		config.titleOnlyShowAppName = true;
		// 呼吸灯配置
		config.ledARGB = Color.GREEN;
		config.ledOnMs = 1000;
		config.ledOffMs = 1500;
		// 通知铃声的uri字符串
		config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
		options.statusBarNotificationConfig = config;

		// 配置保存图片，文件，log 等数据的目录
		// 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
		// 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
		// 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
		String sdkPath = Environment.getExternalStorageDirectory() + "/"
				+ getPackageName() + "/nim";
		options.sdkStorageRootPath = sdkPath;

		// 配置是否需要预下载附件缩略图，默认为 true
		options.preloadAttach = true;

		// 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
		// 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
		options.thumbnailSize = ScreenUtil.SCREEN_WIDTH / 2;

		// 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
		options.userInfoProvider = new UserInfoProvider() {
			@Override
			public UserInfo getUserInfo(String account) {
				return null;
			}

			@Override
			public int getDefaultIconResId() {
				return R.drawable.sichat_placehold_icon;
			}

			@Override
			public Bitmap getTeamIcon(String tid) {
				return null;
			}

			@Override
			public Bitmap getAvatarForMessageNotifier(String account) {
				return null;
			}

			@Override
			public String getDisplayNameForMessageNotifier(String account,
					String sessionId, SessionTypeEnum sessionType) {
				return null;
			}
		};
		return options;
	}

	// 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
	private LoginInfo loginInfo() {
		String account = (String) PreferHelper.getSharedParam(
				ConstantKey.KEY_USER_ACCOUNT, "");
		String token = (String) PreferHelper.getSharedParam(
				ConstantKey.KEY_USER_TOKEN, "");
		if (!Tool.isEmpty(account) && !Tool.isEmpty(token)) {
			return new LoginInfo(account, token);
		} else {
			return null;
		}
	}

	/**
	 * 登陆云信服务器
	 */
	@SuppressWarnings("unchecked")
	public void doYunXinLogin(final String account, final String token) {
		LogUtil.e("hl","doYunXinLogin="+account+"__"+token);
		LoginInfo info = new LoginInfo(account, token);
		NIMClient.getService(AuthService.class).login(info)
				.setCallback(new RequestCallback<LoginInfo>() {

					@Override
					public void onException(Throwable arg0) {
					}

					@Override
					public void onFailed(int arg0) {
						LogUtil.e("hl","登录云信Failed=" + arg0);
					}

					@Override
					public void onSuccess(LoginInfo arg0) {
						// 登陆成功保存本地，方便自动登陆
						PreferHelper.setSharedParam(
								ConstantKey.KEY_USER_ACCOUNT, account);
						PreferHelper.setSharedParam(ConstantKey.KEY_USER_TOKEN,
								token);
						LogUtil.e("hl","登录云信成功");
//						ToastUtil.showShortToast("登录云信成功");
					}
				});
	}

	// 初始化IMAGELOADER配置
	public static void initImageLoader(Context context) {
		// File cacheDir = StorageUtils.getOwnCacheDirectory(mInstance ,
		// "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				.memoryCacheExtraOptions(480, 800)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize((int) (2 * 1024 * 1024))
				// .memoryCacheSizePercentage(13)

				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(400)
				.defaultDisplayImageOptions(PictureUtil.normalImageOptions)
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 解决65536问题 Copy the following code and call dexTool() after
	 * super.onCreate() in Application.onCreate()
	 * <p>
	 * This method hacks the default PathClassLoader and load the secondary dex
	 * file as it's parent.
	 */
	@SuppressLint("NewApi")
	private void dexTool() {
		LogUtil.d("hl", "dexTool————————start");
		File dexDir = new File(getFilesDir(), "dlibs");
		dexDir.mkdir();
		File dexFile = new File(dexDir, "libs.apk");
		File dexOpt = getCacheDir();
		try {
			InputStream ins = getAssets().open("libs.apk");
			if (dexFile.length() != ins.available()) {
				FileOutputStream fos = new FileOutputStream(dexFile);
				byte[] buf = new byte[4096];
				int l;
				while ((l = ins.read(buf)) != -1) {
					fos.write(buf, 0, l);
				}
				fos.close();
			}
			ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ClassLoader cl = getClassLoader();
		ApplicationInfo ai = getApplicationInfo();
		String nativeLibraryDir = null;
		if (Build.VERSION.SDK_INT > 8) {
			nativeLibraryDir = ai.nativeLibraryDir;
		} else {
			nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
		}
		DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(),
				dexOpt.getAbsolutePath(), nativeLibraryDir, cl.getParent());

		try {
			Field f = ClassLoader.class.getDeclaredField("parent");
			f.setAccessible(true);
			f.set(cl, dcl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		LogUtil.d("hl", "dexTool————————end");
	}

	// 异步初始化资源
	public static void initApp() {
		new AsyncTask<Integer, Integer, Boolean>() {
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
			}

			@Override
			protected Boolean doInBackground(Integer... params) {
				try {
					AppContext.getInstance().initUser();
					CrashHandler crashHandler = CrashHandler.getInstance();
					crashHandler.init(mInstance);
					JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
					JPushInterface.init(mInstance); // 初始化 JPush
					if (AppContext.getInstance().isHasLogined()) {
						setJPushTag();
					}
					PlatformConfig.setWeixin("wx3ef182c439eeea1c",
							"9be57d46788039a8153472bf8a2c55f7");
					PlatformConfig.setQQZone("1104776283", "HeTke7nvnfjDtuRb");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} catch (NoClassDefFoundError e) {
					e.printStackTrace();
					return false;
				}
			}
		}.execute();
	}

	/**
	 * 初始化用到的提示音
	 */
	private void initSound() {
		soundPoolMap.put(1, soundPool.load(this, R.raw.accept_reminder, 1));
		soundPoolMap.put(2, soundPool.load(this, R.raw.play_completed, 2));
		soundPoolMap.put(3, soundPool.load(this, R.raw.talkroom_begin, 3));
		soundPoolMap.put(4, soundPool.load(this, R.raw.short_time, 4));
		soundPoolMap.put(5, soundPool.load(this, R.raw.sent_message, 5));
	}

	/**
	 * 播放提示音
	 * 
	 * @param sound
	 *            <Li >1 来消息叮咚 <Li >2 语音播放结束音 <Li >3 木质咚一声 <Li >3 电子嘀一声 <Li >5
	 *            发送消息完毕音
	 * @param loop
	 *            是否循环
	 */
	public void playSound(int sound, int loop) {
		AudioManager mgr = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
		// 参数：1、Map中取值 2、当前音量 3、最大音量 4、优先级 5、重播次数 6、播放速度
	}

	/**
	 * 添加推送 设备标签(Tag) 设备别名(Alias) Registration ID
	 */
	public static void setJPushTag() {
		LogUtil.d("hl", "DeviceId = " + VersionUtils.getDeviceId());
		if (AppContext.getInstance().isHasLogined()) {
			Set<String> tag = new HashSet<String>();
			tag.add("android");
			if (mInstance != null) {
				JPushInterface.setAliasAndTags(mInstance,
						VersionUtils.getDeviceId(), tag,
						new TagAliasCallback() {
							@Override
							public void gotResult(int arg0, String arg1,
									Set<String> arg2) {
							}
						});
			}

		}
	}

	// /**
	// * 登录聊天服务器
	// */
	// public void doMsgServerLogin() {
	// if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
	// getoffLineMsg();
	// AsmarkMethod.login();
	// }
	// }

	/**
	 * 初始化文件系统
	 */
	private static void initFileSys() {
		FileConstant constant = new FileConstant(mInstance,
				FileConstant.PACKAGE_NAME);// "com.meiku.dev");
		FileHelper.makeDir();
	}

	public static MrrckApplication getInstance() {
		if (null == mInstance) {
			mInstance = new MrrckApplication();
		}
		return mInstance;
	}

	// 添加Activity 到容器中
	public void addActivity(String key, Activity activity) {
		activityListMap.put(key, activity);
		LogUtil.d("hl", "addActivity=>" + key);
	}

	// 容器中移除Activity
	public void removeActivity(String key) {
		if (activityListMap != null && activityListMap.containsKey(key)) {
			activityListMap.remove(key);
			LogUtil.d("hl", "removeActivity=>" + key);
		}
	}

	public void exit() {
		if (activityListMap != null) {
			Iterator<Entry<String, Activity>> iter = activityListMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, Activity> entry = iter.next();
				entry.getValue().finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 清除缓存 ACT列表
	 */
	public void clearActivityes() {
		if (activityListMap != null) {
			Iterator<Entry<String, Activity>> iter = activityListMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, Activity> entry = iter.next();
				entry.getValue().finish();
			}
		}
	}

	/**
	 * 打开数据库
	 */
	// public static void openDataBase() {
	// // MKDataBase.getInstance().initDataBase(mInstance,
	// // FileConstant.LOCALDB_PATH, FileConstant.DB_NAME);
	// MKDataBase.getInstance().initDataBase(MrrckApplication.getInstance());
	// }

	// public void startMsgServices() {
	// mInstance.startService(new Intent(mInstance, IMChatService.class));
	// }
	//
	// public void stopMsgServices() {
	// if (AsmarkMethod.chatManage != null) {
	// AsmarkMethod.chatManage.clear();
	// }
	//
	// mInstance.stopService(new Intent(mInstance, IMChatService.class));
	// // if (XmppUtils.getInstance().checkIsConnectToService()) {
	// XmppUtils.getInstance().closeConnection();
	// // }
	// }
	//
	// // 开启定时任务ping消息服务器
	// public void startAlarmPing() {
	// LogUtil.e("xmppcon", "====启动定时服务+ ");
	// AlarmUtils.startPollingService(mInstance, 10,// 秒钟PING一次
	// AlarmPingReceiver.class, BroadCastAction.ACTION_IM_PING_IQ);
	// }
	//
	// // 停止定时任务
	// public void stopAlarmPing() {
	// LogUtil.e("xmppcon", "====停止定时服务+ ");
	// AlarmUtils.stopPollingService(mInstance, AlarmPingReceiver.class,
	// BroadCastAction.ACTION_IM_PING_IQ);
	// }
	//
	// // PING IQ给消息服务器
	// public static void pingMsgServer() {
	// try {
	// if (!XmppUtils.getInstance().checkIsConnectToService()
	// || !PingManager.getInstanceFor(
	// XmppUtils.getInstance().getConnection())
	// .pingMyServer()) {
	// Intent intent = new Intent(BroadCastAction.ACTION_IM_RECON);
	// intent.putExtra(XmppConstant.MESSAGE_XMPP_RECONNING, true); // =XMPP已断开
	// MrrckApplication.getInstance().sendBroadcast(intent);
	// MrrckApplication.getInstance().stopMsgServices();
	// if (XmppUtils.getInstance().checkIsConnectToService()) {
	// XmppUtils.getInstance().closeConnection();
	// }
	// MrrckApplication.getInstance().stopAlarmPing();
	// MrrckApplication.getInstance().doMsgServerLogin();
	// }
	// } catch (Exception e) {
	// reConMsgServer();
	// }
	// }
	//
	// /**
	// * 发送过程中出现发送问题，直接停止消息服务，重新开始轮询
	// */
	// public static void reConMsgServer() {
	// Intent intent = new Intent(BroadCastAction.ACTION_IM_RECON);
	// intent.putExtra(XmppConstant.MESSAGE_XMPP_RECONNING, true); // =XMPP已断开
	// MrrckApplication.getInstance().sendBroadcast(intent);
	// MrrckApplication.getInstance().stopMsgServices();
	// MrrckApplication.getInstance().stopAlarmPing();
	// MrrckApplication.getInstance().doMsgServerLogin();
	// }

	// ========VOLLEY统一队列管理
	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue() {
		// lazy initialize the request queue, the queue instance will be
		// created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	/**
	 * Adds the specified request to the global queue, if tag is specified then
	 * it is used else Default TAG is used.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		LogUtil.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Adds the specified request to the global queue using the Default TAG.
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * Cancels all pending requests by the specified TAG, it is important to
	 * specify a TAG so that the pending/ongoing requests can be cancelled.
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * 根据当前城市名 从数据库获取当前省市code，必须数据库初始化后才可调用
	 */
	public void initLocationData() {
		String cityCode = MKDataBase.getInstance().getCityCode(cityName);
		String provinceCode = MKDataBase.getInstance()
				.getprovinceCodeBycityCode(cityCode);
		MrrckApplication.provinceCode = Tool.isEmpty(provinceCode) ? "-1"
				: provinceCode;
		MrrckApplication.cityCode = Tool.isEmpty(cityCode) ? "-1" : cityCode;
		MkUser userData = AppContext.getInstance().getUserInfo();
		userData.setCityCode(cityCode);
		userData.setProvinceCode(provinceCode);
		AppContext.getInstance().setLocalUser(userData);
		LogUtil.d("hl", cityName + "--" + provinceCode + "--" + cityCode);
	}

	/**
	 * 检测JsessionId，是否重复登陆
	 * 
	 * @param newJsessionId
	 */
	public synchronized void checkDoubleLoginStatus(String newJsessionId) {
		LogUtil.d("hl", AppContext.getInstance().getJsessionId()
				+ "<--old**newJsessionId-->" + newJsessionId);
		if (AppContext.getInstance().isHasLogined()
				&& !Tool.isEmpty(AppContext.getInstance().getJsessionId())
				&& !newJsessionId.equals(AppContext.getInstance()
						.getJsessionId())) {
			LogUtil.d("hl", "__________重复登陆(接口JsessionId判断)_________________");
			sendBroadcast(new Intent(BroadCastAction.ACTION_RELOGINT_DOLOUOUT)
					.putExtra("RELOGIN", true));
		}
	}

//	/**
//	 * 保存未读的通知消息
//	 */
//	public static void saveNotice(int friendid, String body, int isRoom) {
//		Notice notice = new Notice();
//		notice.setTitle("会话信息");
//		// notice.setNoticeType(XmppConstant.NOTICE_CHAT_MSG);
//		notice.setNoticeType(isRoom);
//		notice.setContent(body);
//		notice.setFriendsubid(friendid);
//		notice.setStatus(XmppConstant.NOTICE_UNREAD);
//		notice.setMysubid(AppContext.getInstance().getUserInfo().getId());
//		notice.setNoticeTime(DateUtil.getCurFormate(""));
//		MsgDataBase.getInstance().saveIMNoticeTable(notice);
//	}
//
//	/**
//	 * 保存接收离线 消息记录
//	 * 
//	 * @param string
//	 */
//	public boolean saveIMmessage(int friendid, String body, String packetID,
//			String sysMsgTime) {
//		IMMessage newMessage = new IMMessage();
//		if (JsonUtil.getJSONType(body) != JsonUtil.JSON_TYPE.JSON_TYPE_ERROR) {
//			newMessage = (IMMessage) JsonUtil.jsonToObj(IMMessage.class, body);
//			if (newMessage.getType() == XmppConstant.MSG_CONTENT_TEXT) {
//				if (XmppConstant.MSG_FILE_URL_IMG == newMessage.getIsFileUrl()) {
//					newMessage.setContent(XmppConstant.CHAT_MSG_IMAGE);
//				} else if (XmppConstant.MSG_FILE_URL_VIDEO == newMessage
//						.getIsFileUrl()) {
//					newMessage.setContent(XmppConstant.CHAT_MSG_VIDEO);
//				}
//			} else if (newMessage.getType() == XmppConstant.MSG_CONTENT_IMG) {// 图片
//
//				newMessage.setFilePath(FileHelper.GenerateVoic(
//						newMessage.getFilePath(),
//						FileHelper.getImgPathByCurrentTime()));
//				newMessage.setContent(XmppConstant.CHAT_MSG_IMAGE);
//				newMessage.setType(XmppConstant.MSG_CONTENT_IMG);
//
//			} else if (newMessage.getType() == XmppConstant.MSG_CONTENT_VOICE) {// 语音
//				// 转成语音
//				newMessage.setFilePath(FileHelper.GenerateVoic(
//						newMessage.getFilePath(),
//						FileHelper.getRecordPathByCurrentTime()));
//				newMessage.setContent(XmppConstant.CHAT_MSG_VOICE);
//				newMessage.setType(XmppConstant.MSG_CONTENT_VOICE);
//				newMessage.setPlayStatus(XmppConstant.VOICE_NOTPLAY);// 收到语音消息，设为未播放状态
//			}
//		} else {
//			newMessage.setContent(body);
//		}
//		if (newMessage.getMsgType() == XmppConstant.MSGTYPE_SYSTEM) {
//			String noticContentStr = newMessage.getContent();
//			newMessage.setSystemJson(noticContentStr);
//			if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
//					.getJSONType(noticContentStr)) {// 是json格式
//				SysMsgContentBean sysContent = (SysMsgContentBean) JsonUtil
//						.jsonToObj(SysMsgContentBean.class, noticContentStr);
//				switch (sysContent.getType()) {
//				case XmppConstant.SYSTEM_TYPE_KICK:// 踢人出群
//				case XmppConstant.SYSTEM_TYPE_DISMISS:// 解散群
//				case XmppConstant.SYSTEM_TYPE_ADD:// 拉人进群
//
//					break;
//				default:
//					break;
//				}
//				newMessage.setContent(sysContent.getMsg());
//
//			} else {
//				newMessage.setContent(noticContentStr);
//			}
//
//		}
//
//		newMessage.setPacketID(packetID);
//		newMessage.setResult(XmppConstant.MESSAGE_RESULT_RSV);
//		newMessage.setMsgtime(sysMsgTime);
//		// String friendNickname = newMessage.getMynickname();// 此时取的是好友的昵称，单聊用
//		if (newMessage.getIsRoom() == XmppConstant.IM_CHAT_NO_ROOM) {
//			newMessage.setFriendnickname(newMessage.getMynickname());
//			// String friendHeadimg = newMessage.getMyheadimg();
//			newMessage.setFriendheadimg(newMessage.getMyheadimg());
//			newMessage.setMyheadimg(AppContext.getInstance().getUserInfo()
//					.getClientThumbHeadPicUrl());
//		}
//
//		if (!Tool.isEmpty(newMessage.getMsgtime())) {
//			// newMessage.setMsgtime(DateUtil.getCurFormate(""));
//			newMessage.setTimestamp(DateUtil.StringToLong(DateUtil.FORMAT,
//					newMessage.getMsgtime()));
//		} else {
//			newMessage.setMsgtime(DateUtil.getNetTimeStr());
//			newMessage.setTimestamp(DateUtil.getNetTimeLong());
//			// newMessage.setMsgtime(DateUtil.getCurFormate(""));
//			// newMessage.setTimestamp(System.currentTimeMillis());
//		}
//		newMessage.setMsgType(XmppConstant.MSGTYPE_RCV);
//		newMessage.setFriendSubJid(friendid);
//		newMessage.setMySubJid(AppContext.getInstance().getUserInfo().getId());
//		newMessage.setMynickname(AppContext.getInstance().getUserInfo()// 然后把昵称设为自己
//				.getNickName());
//
//		if (newMessage.getIsRoom() == XmppConstant.IM_CHAT_IS_ROOM) {
//			return MsgDataBase.getInstance().saveOfIMMsgGroup(newMessage);
//		} else {
//			return MsgDataBase.getInstance().saveIMMsgTable(newMessage);
//		}
//
//	}
//
//	@SuppressWarnings("unchecked")
//	protected void getoffLineMsg() {
//		ReqBase reqBase = new ReqBase();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("userId", AppContext.getInstance().getUserInfo().getId());
//		String groupTime = MsgDataBase.getInstance().getLastGroupTime();
//		map.put("createDate", groupTime);
//		LogUtil.e("hl", "18040" + map);
//		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_IM_OFFLINE_MSG));
//		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
//		StringJsonRequest request = new StringJsonRequest(AppConfig.DOMAIN
//				+ AppConfig.PUBLICK_NEARBY_GROUP, new Listener<String>() {
//			@Override
//			public void onResponse(String resp) {
//				// ReqBase basejson = new ReqBase();
//				ReqBase basejson = (ReqBase) JsonUtil.jsonToObj(ReqBase.class,
//						resp);
//				ReqHead head = basejson.getHeader();
//				if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
//					try {
//						String jsonUserChtList = basejson.getBody()
//								.get("userChatList").toString();
//
//						List<OfChatLogs> userChtList = (List<OfChatLogs>) JsonUtil
//								.jsonToList(jsonUserChtList,
//										new TypeToken<List<OfChatLogs>>() {
//										}.getType());
//						String lastOffLineMsgTime = "";
//						if (userChtList != null && userChtList.size() > 0) {
//
//							for (OfChatLogs ocl : userChtList) {
//								try {
//									int friendid = Integer.parseInt(ocl
//											.getSessionJID().split("@")[0]);
//									saveIMmessage(friendid, JsonUtil
//											.objToJson(ocl.getContent()), ocl
//											.getMessageId(), ocl
//											.getCreateDate());
//									MrrckApplication
//											.saveNotice(friendid,
//													JsonUtil.objToJson(ocl
//															.getContent()), ocl
//															.getIsGroup());
//									lastOffLineMsgTime = ocl.getCreateDate();// 最后一条时间为最后消息时间
//								} catch (Exception e) {
//									LogUtil.e("xmppcon", e.getMessage());
//									e.printStackTrace();
//									continue;
//								}
//
//							}
//
//							sendBroadcast(new Intent(
//									BroadCastAction.ACTION_IM_OFFLINE_MESSAGE));// 发送广播
//						}
//						// 添加丢失消息（过滤已有消息）
//						String jsonStr = basejson.getBody()
//								.get("groupChatList").toString();
//						LogUtil.e("hl", "18041result==jsonGroupChatList=="
//								+ jsonStr);
//						List<OfChatGroupLogs> list = (List<OfChatGroupLogs>) JsonUtil
//								.jsonToList(jsonStr,
//										new TypeToken<List<OfChatGroupLogs>>() {
//										}.getType());
//						if (!Tool.isEmpty(list)) {
//							LogUtil.e("hl", "18040, groupChatList.size()="
//									+ list.size());
//							for (OfChatGroupLogs bean : list) {
//								IMMessage newMessage = (IMMessage) JsonUtil
//										.jsonToObj(IMMessage.class, bean
//												.getContent().toString());
//								if (AppContext.getInstance().getUserInfo()
//										.getId() == newMessage.getSpeakuserid()) {
//									continue;
//								}
//
//								newMessage.setPacketID(bean.getMessageId());
//								newMessage
//										.setResult(XmppConstant.MESSAGE_RESULT_RSV);
//								newMessage.setMsgtime(bean.getCreateDate());
//
//								if (!Tool.isEmpty(newMessage.getMsgtime())) {
//									// newMessage.setMsgtime(DateUtil.getCurFormate(""));
//									newMessage.setTimestamp(DateUtil
//											.StringToLong(DateUtil.FORMAT,
//													newMessage.getMsgtime()));
//								} else {
//									newMessage.setMsgtime(DateUtil
//											.getCurFormate(""));
//									newMessage.setTimestamp(System
//											.currentTimeMillis());
//								}
//								newMessage.setMsgType(XmppConstant.MSGTYPE_RCV);
//								newMessage.setFriendSubJid(Integer
//										.parseInt(bean.getGroupId()));
//								newMessage.setMySubJid(AppContext.getInstance()
//										.getUserInfo().getId());
//								newMessage.setMynickname(AppContext
//										.getInstance().getUserInfo()// 然后把昵称设为自己
//										.getNickName());
//
//								MsgDataBase.getInstance().saveOfIMMsgGroup(
//										newMessage);
//							}
//						}
//
//						// 发请求更改离线消息为已读
//						ReqBase reqBase2 = new ReqBase();
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("userId", AppContext.getInstance()
//								.getUserInfo().getId());
//						map.put("createDate", lastOffLineMsgTime);
//						LogUtil.e("hl", "18041==" + map);
//						reqBase2.setHeader(new ReqHead(
//								AppConfig.BUSSINESS_IM_OFFLINE_SETREAD));
//						reqBase2.setBody(JsonUtil.String2Object(JsonUtil
//								.hashMapToJson(map)));
//						StringJsonRequest request2 = new StringJsonRequest(
//								AppConfig.DOMAIN
//										+ AppConfig.PUBLICK_NEARBY_GROUP,
//								new Listener<String>() {
//
//									@Override
//									public void onResponse(String arg0) {
//										LogUtil.e("hl", "18041result==" + arg0);
//									}
//								}, JsonUtil.objToJson(reqBase2),
//								new ErrorListener() {
//
//									@Override
//									public void onErrorResponse(VolleyError arg0) {
//										// TODO Auto-generated method stub
//
//									}
//								});
//						request2.setRetryPolicy(new DefaultRetryPolicy(5000,
//								DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//								DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//						MrrckApplication.getInstance().addToRequestQueue(
//								request2);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				// else {
//				// AsmarkMethod.login();
//				// }
//			}
//		},
//		// CompressUtil.CompressBody(reqBase),
//				JsonUtil.objToJson(reqBase), new ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError arg0) {
//						// AsmarkMethod.login();
//					}
//				});
//		request.setRetryPolicy(new DefaultRetryPolicy(5000,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		MrrckApplication.getInstance().addToRequestQueue(request);
//
//	}

	/**
	 * 本地缓存构建（同步）
	 */
	public static void buildDataCache() {
		// clear
		clearDataCache();
		// NimUserInfoCache.getInstance().buildCache();
		TeamDataCache.getInstance().buildCache();
	}

	/**
	 * 清空缓存（同步）
	 */
	public static void clearDataCache() {
		// NimUserInfoCache.getInstance().clear();
		TeamDataCache.getInstance().clear();
	}

}
