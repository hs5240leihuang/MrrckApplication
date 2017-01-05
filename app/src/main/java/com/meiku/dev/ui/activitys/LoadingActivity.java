package com.meiku.dev.ui.activitys;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.StartDiagramVersionEntity;
import com.meiku.dev.bean.UpdateBean;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.services.LoadImageServices;
import com.meiku.dev.services.LocationService;
import com.meiku.dev.ui.login.MkLoginActivity;
import com.meiku.dev.utils.DBManagerUtil;
import com.meiku.dev.utils.DBManagerUtil.ImportDBListener;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.UpdateAppManager;
import com.meiku.dev.views.CommonDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;

/**
 * 首页加载页
 */
public class LoadingActivity extends BaseActivity {

	/** 是否显示赛事广告页面 */
	private boolean showMatchTip = true;
	/** 赛事ID */
	private String matchId;
	/** 定位服务 */
	private LocationService locationService;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_splash;
	}

	@Override
	public void initView() {
		getLocationOnce();
		ScreenUtil.initScreenData(this);
	}

	@Override
	public void bindListener() {
	}

	/**
	 * 定位一次获取经纬度
	 */
	private void getLocationOnce() {
		locationService = MrrckApplication.getInstance().locationService;
		locationService.registerListener(mListener);
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		locationService.start();
	}

	/**
	 * 定位监听
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				StringBuffer sb = new StringBuffer(256);
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					sb.append("gps定位成功");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					sb.append("网络定位成功");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					sb.append("离线定位成功");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("服务端网络定位失败");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("网络不同导致定位失败，请检查网络是否通畅");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}
				LogUtil.d("hl", "baidu_ location--------->" + sb.toString());
				if (!Tool.isEmpty(location.getCity())) {// 城市名不为空，视为有效定位
					// 百度定位失败偶尔会出现4.9E-324情况
					if (!"4.9E-324".equals(String.valueOf(location
							.getLatitude()))) {
						MrrckApplication.laitude = location.getLatitude();
					}
					if (!"4.9E-324".equals(String.valueOf(location
							.getLatitude()))) {
						MrrckApplication.longitude = location.getLongitude();
					}
					MrrckApplication.cityName = location.getCity();
					locationService.unregisterListener(mListener); // 注销掉监听
					locationService.stop(); // 停止定位服务 break;
				}
			}
		}
	};

	@Override
	public void initValue() {
		if (!NetworkTools.isNetworkAvailable(LoadingActivity.this)) {// 判断当前网络是否连接
			showMatchTip = false;
			doImportAssetDB();// 没有网络导本地数据库
		} else {
			queryAppDbVersion();// 有网络check数据库版本及更新
		}
	}

	/**
	 * 查询程序和数据库版本信息
	 */
	public void queryAppDbVersion() {
		ReqBase req = new ReqBase();
		req.setHeader(new ReqHead(AppConfig.BUSINESS_APP_DB_VERSION));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("devType", ConstantKey.DEV_TYPE);
		if (!Tool.isEmpty(MrrckApplication.provinceCode)
				&& !"-1".equals(MrrckApplication.provinceCode)) {
			map.put("provinceCode", MrrckApplication.provinceCode);
		}
		if (!Tool.isEmpty(MrrckApplication.cityCode)
				&& !"-1".equals(MrrckApplication.cityCode)) {
			map.put("cityCode", MrrckApplication.cityCode);
		}
		LogUtil.d("hl", "queryAppDbVersion");
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLIC_REQUEST_MAPPING, req, false);
		// String userInfoTag = (String) PreferHelper.getSharedParam(
		// ConstantKey.SP_IS_PERFECTINFO, "0");
		// if ("0".equals(userInfoTag) &&
		// AppContext.getInstance().isHasLogined()) {
		// AppContext.getInstance().getUserInfo().setUserInfoFlag(1);
		// AppContext.getInstance().setLocalUser(
		// AppContext.getInstance().getUserInfo());
		// PreferHelper.setSharedParam(ConstantKey.SP_IS_PERFECTINFO, "1");
		// }
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "-->" + resp.getBody());
		switch (requestCode) {
		case reqCodeThree:
			// ------当前赛事及ID------
			if (!Tool.isEmpty(resp.getBody().get("match"))
					&& (resp.getBody().get("match") + "").length() > 2) {
				Map<String, String> matchMap = JsonUtil.jsonToMap(resp
						.getBody().get("match") + "");
				matchId = matchMap.get("id");
				if (Tool.isEmpty(matchId) || "null".equals(matchId)) {
					// showMatchTip = false;
				}
				if (!Tool.isEmpty(resp.getBody().get("matchCity"))
						&& (resp.getBody().get("matchCity") + "").length() > 2) {
					PreferHelper.setSharedParam("matchCity", resp.getBody()
							.get("matchCity").toString());
				}
			} else {
				// showMatchTip = false;
			}

			// ------应用版本更新------
			if ((resp.getBody().get("clientVersion") + "").length() > 2) {
				UpdateBean entity = (UpdateBean) JsonUtil.jsonToObj(
						UpdateBean.class, resp.getBody().get("clientVersion")
								.toString()); // APP版本信息
				Map<String, String> dbmap = JsonUtil.jsonToMap(resp.getBody()
						.get("appDbVersion").toString());// 数据库版本
				checkAppUpdate(entity, dbmap);
			}

			// 热补丁patch数据
			// if ((resp.getBody().get("hotPatch") + "").length() > 2) {
			// LogUtil.d("hl", "hotPatch-->" + resp.getBody().get("hotPatch"));
			// Map<String, String> hotPatchMap = JsonUtil.jsonToMap(resp
			// .getBody().get("hotPatch") + "");
			// if (hotPatchMap == null
			// || !hotPatchMap.containsKey("clientVersion")) {
			// return;
			// }
			// String clientVersion = hotPatchMap.get("clientVersion");
			// // 确认当前补丁与目前版本号是否一致
			// if (VersionUtils.getVersionName(LoadingActivity.this).equals(
			// clientVersion)) {
			// if (!hotPatchMap.containsKey("clientHotPatchUrl")
			// || !hotPatchMap.containsKey("clientName")) {
			// return;
			// }
			// String clientHotPatchUrl = hotPatchMap
			// .get("clientHotPatchUrl");
			// String clientHotPatchName = hotPatchMap.get("clientName");
			// String patchSDPath = SdcardUtil.getPath()
			// + FileConstant.FIXPATCH_PATH + clientHotPatchName;
			// // 如果补丁已下载则不再去下载
			// if (!Tool.isEmpty(clientHotPatchUrl)
			// && !Tool.isEmpty(clientHotPatchName)
			// && !FileHelper.isFileExist(new File(patchSDPath))) {
			// // 启动临时服务下载补丁
			// startService(new Intent(this,
			// LoadFixPatchServices.class).putExtra("url",
			// clientHotPatchUrl).putExtra("patchName",
			// clientHotPatchName));
			// }
			// }
			// }

			// -----赛事广告图更新-----
			if ((resp.getBody().get("startDiagramVersionMatch") + "").length() > 2) {
				StartDiagramVersionEntity sdv = (StartDiagramVersionEntity) JsonUtil
						.jsonToObj(StartDiagramVersionEntity.class, resp
								.getBody().get("startDiagramVersionMatch")
								.toString());
				if (null != sdv) {
					int currentScreenDPI = ScreenUtil.SCREEN_DPI;
					LogUtil.d("hl", "SCREEN_DPI=" + currentScreenDPI);
					int specType;// 根据当前设备只需要一种大小类型的图片
					// specType===>1:320*480 2:480*800 3:720*960 4:720*1280
					if (currentScreenDPI > 320) {// xxhdpi
						specType = 4;
					} else if (currentScreenDPI > 240
							&& currentScreenDPI <= 320) {// xhdpi--320
						specType = 3;
					} else if (currentScreenDPI > 160
							&& currentScreenDPI <= 240) {// hdpi--240
						specType = 2;
					} else {
						specType = 1;
					}
					if (sdv.getStartDiagramList() != null) {
						for (int i = 0, size = sdv.getStartDiagramList().size(); i < size; i++) {
							LogUtil.d("hl", specType
									+ "===="
									+ sdv.getStartDiagramList().get(i)
											.getSpecType());
							if (specType == sdv.getStartDiagramList().get(i)
									.getSpecType()) {
								// 获取之前保存的广告图url
								String tipADUrl_old = (String) PreferHelper
										.getSharedParam(
												ConstantKey.SP_TIPAD_URL, "");

								// url没变，无需下载新广告图
								if (tipADUrl_old.equals(sdv
										.getStartDiagramList().get(i)
										.getClientPicUrl())) {
									return;
								}

								String btnSetStr = resp.getBody()
										.get("startDiagramVersionMatch")
										.toString();
								// 广告图上按钮配置有变，保存为最新的
								if (!PreferHelper.getSharedParam(
										ConstantKey.SP_MATCHAD_INFO, "")
										.equals(btnSetStr)) {
									PreferHelper.setSharedParam(
											ConstantKey.SP_MATCHAD_INFO,
											btnSetStr);
									LogUtil.d("hl", "广告图按钮配置changed=》"
											+ btnSetStr);
								}

								startService(new Intent(this,
										LoadImageServices.class).putExtra(
										"url",
										sdv.getStartDiagramList().get(i)
												.getClientPicUrl()).putExtra(
										"matchAdVersion", sdv.getVersion()));
							}
						}
					}
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		String cacheDbVersion = AppContext.getInstance().getAppDBVersion();
		if (Tool.isEmpty(cacheDbVersion)) {
			doImportAssetDB();
		} else {
			judgeRedirectPage();
		}
	}

	/**
	 * 检查App版本更新
	 */
	public void checkAppUpdate(UpdateBean entity,
			final Map<String, String> dbmap) {
		UpdateAppManager manager = new UpdateAppManager(this);
		manager.setOnUpdateResultListener(new UpdateAppManager.OnUpdateResultListener() {
			@Override
			public void onNotUpdate() {// 当前是最新版本，无需更新
				doDBCheck(dbmap);
			}

			@Override
			public void onCancleClick() {// 提示更新点击取消（主要用于区别设置中检查版本，点击取消不可提示最新版本的toast）
				doDBCheck(dbmap);
			}
		});
		manager.checkUpdateInfo(entity);
	}

	/**
	 * 检查DB，是否需要更新下载
	 */
	protected void doDBCheck(Map<String, String> dbmap) {
		if (!NetworkTools.isNetworkAvailable(LoadingActivity.this)) {
			// 当前无网络,导入本地Asset数据库,返回结果
			doImportAssetDB();
		} else {// 有网络则对比数据库版本
			String url = dbmap.get("url");
			String version = dbmap.get("version");
			if (DBManagerUtil.isNeedDownloadDb(version)) {// 检查DB是否需要更新
				LogUtil.d("hl", "需要下载DB***url=" + url + "  version=" + version);
				DBManagerUtil.downLoadDBAndImport(url, version,
						new ImportDBListener() {

							@Override
							public void importResult(boolean isSuccess) {
								LogUtil.d("hl", "导入后台DB---result=" + isSuccess);
								if (isSuccess) {
									// 打开数据库
									judgeRedirectPage();// 无需更新直接走启动流程
								} else {
									// 失败,统一导入本地Asset数据库
									doImportAssetDB();
								}

							}
						});
			} else {// 无需更新直接走启动流程
				judgeRedirectPage();
			}
		}
	}

	/**
	 * 导入本地asset的DB,已经导入，则直接走启动流程
	 */
	protected void doImportAssetDB() {
		if (!Tool.isEmpty(AppContext.getInstance().getAppDBVersion())) { // 本地已加载过数据库
			judgeRedirectPage();
		} else {
			if (!DBManagerUtil.checkAssetDBHasImported()) {
				DBManagerUtil.asyncImportAssetDatabase(new ImportDBListener() {

					@Override
					public void importResult(boolean isSuccess) {
						LogUtil.d("hl", "导入asset本地DB---result=" + isSuccess);
						if (isSuccess) {
							judgeRedirectPage();// 导入成功，走启动流程
						} else {
							showErrorDialog("加载数据库失败，请重新打开APP");
						}
					}
				});
			} else {
				judgeRedirectPage();
			}

		}
	}

	/**
	 * 提示错误dialog
	 */
	private void showErrorDialog(String msg) {
		final CommonDialog commonDialog = new CommonDialog(this, "提示", msg,
				"确定");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						commonDialog.dismiss();
						LoadingActivity.this.finish();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
						LoadingActivity.this.finish();
					}
				});
	}

	/**
	 * 走页面流程
	 */
	private void judgeRedirectPage() {

		// MKDataBase.getInstance().initDataBase(this,
		// FileConstant.LOCALDB_PATH,
		// FileConstant.DB_NAME);

		MsgDataBase.getInstance().initMsgDb();
		//
		// String isAddColumn = (String) PreferHelper.getSharedParam(
		// ConstantKey.SP_IS_ADDMSGCOLUMN, "0");
		// if ("0".equals(isAddColumn)) {
		// MsgDataBase.getInstance().doAddColumn();
		// PreferHelper.setSharedParam(ConstantKey.SP_IS_ADDMSGCOLUMN, "1");
		// }

		// 微信QQ链接打开应用
		String guide = (String) PreferHelper.getSharedParam(
				ConstantKey.SP_IS_GUIDE, "0");
		Uri uri = getIntent().getData();
		String type = "";
		String data = "";
		if (!Tool.isEmpty(uri)) {
			if (uri.getScheme().equals("goto")
					&& uri.getHost().equals("mrrck.com")) {
				type = uri.getQueryParameter("type");
				if (!Tool.isEmpty(type)) {
					data = uri.getQueryParameter("data");
				}
			}
			showMatchTip = false;
		}

		// 点击推送通知打开应用
		String h5url = "";
		Bundle bundle = getIntent().getBundleExtra(
				ConstantKey.NOTIFICATION_BUNDLE);
		if (bundle != null) {
			type = bundle.getString("PushType");
			data = bundle.getString("PushKey");
			h5url = bundle.getString("PushUrl");
			showMatchTip = false;
		}

		// // 点击消息通知打开应用
		int arga = -1;
		String argb = "";
		String argc = "";
		Bundle bundle2 = getIntent().getBundleExtra(
				ConstantKey.NOTIFICATION_MESSAGE_BUNDLE);
		if (bundle2 != null) {
			int argType = bundle2.getInt("messagetype");
			type = argType + "";
			if (ConstantKey.TAG_MESSAGE_SINGLE == argType) {
				arga = bundle2.getInt("arga");
				argb = bundle2.getString("argb");
				argc = bundle2.getString("argc");
			} else if (ConstantKey.TAG_MESSAGE_GROUP == argType) {
				arga = bundle2.getInt("arga");
				argb = bundle2.getString("argb");
			}
			showMatchTip = false;
		}

		if ("1".equals(guide)) {// 是否首次
			Intent intent;
			if (showMatchTip) {// 需要显示赛事广告页面
				if (!AppContext.getInstance().isHasLogined()) {// 没登录则跳转登录（附带参数赛事ID）
					// intent = new Intent(LoadingActivity.this,
					// MkLoginActivity.class);
					intent = new Intent(LoadingActivity.this,
							HomeActivity.class);// 修改为没登录也能进主页
					intent.putExtra("Flag", 2);
					intent.putExtra("type", ConstantKey.INHOMEPAGE_SAISHIMAIN
							+ "");
					try {
						intent.putExtra("matchId", Integer.parseInt(matchId));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {// 已经登录，跳到主页，显示赛事广告

					if (AppContext.getInstance().getUserInfo() != null
							&& Tool.isEmpty(AppContext.getInstance()
									.getUserInfo().getLeanCloudUserName())) {// 新IM上线更新User账号（聊天账号为空则重新获取用户信息）
						intent = new Intent(LoadingActivity.this,
								MkLoginActivity.class);
						intent.putExtra("needGoToHome", true);
					} else {
						intent = new Intent(LoadingActivity.this,
								HomeActivity.class);
						intent.putExtra("type",
								ConstantKey.INHOMEPAGE_SAISHIMAIN + "");
						StatusCode status = NIMClient.getStatus();
						if (status == StatusCode.UNLOGIN) {
							MrrckApplication.getInstance().doYunXinLogin(
									AppContext.getInstance().getUserInfo()
											.getLeanCloudUserName(),
									AppContext.getInstance().getUserInfo()
											.getLeanCloudId());
						}
						try {
							intent.putExtra("matchId",
									Integer.parseInt(matchId));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				startActivity(intent);
				LoadingActivity.this.finish();
			} else {
				if (!AppContext.getInstance().isHasLogined()) {
					intent = new Intent(this, MkLoginActivity.class);
					intent.putExtra("Flag", 1);
				} else {
					intent = new Intent(this, HomeActivity.class);
				}
				if (!Tool.isEmpty(type)) {
					intent.putExtra("type", type);
					intent.putExtra("data", data);
					intent.putExtra("arga", arga);
					intent.putExtra("argb", argb);
					intent.putExtra("argc", argc);
					intent.putExtra("h5url", h5url);
				}
				startActivity(intent);
				LoadingActivity.this.finish();
			}
		} else {// 第一次安装
			PreferHelper.setSharedParam(ConstantKey.SP_IS_GUIDE, "1");
			Intent intent = new Intent(this, GuidePageActivity.class);
			if (!Tool.isEmpty(type)) {
				intent.putExtra("type", type);
				intent.putExtra("data", data);
				intent.putExtra("arga", arga);
				intent.putExtra("argb", argb == null ? "" : argb);
				intent.putExtra("argc", argc == null ? "" : argc);
				intent.putExtra("h5url", h5url);
			}
			intent.putExtra("START", "1");
			startActivity(intent);
			LoadingActivity.this.finish();
		}

	}

	@Override
	public void onDestroy() {
		if (null != locationService) {
			locationService.stop(); // 停止定位服务;
		}
		super.onDestroy();
	}
}
