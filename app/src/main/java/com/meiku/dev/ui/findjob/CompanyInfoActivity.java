package com.meiku.dev.ui.findjob;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.JobEntity;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.morefun.MrrckResumeActivity;
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.BossTypeDialogWk;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.HintDialogwk.DoOneClickListenerInterface;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 公司详情
 * 
 */
public class CompanyInfoActivity extends BaseActivity implements
		OnClickListener {
	private ImageView right_res_title;
	private MyPopupwindow myPopupwindow;
	private List<PopupData> list = new ArrayList<PopupData>();
	private WebView webView;
	private WebSettings settings;
	private TextView center_txt_title;
	private String companyId;
	private String collectFlag;
	private CompanyEntity comEntity;
	private ImageView left_res_title;
	private Boolean onclick = true;
	private String shareImg, shareTitle, shareContent, shareUrl;
	private JobEntity jobData;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_companyinfo;
	}

	@Override
	public void initView() {
		left_res_title = (ImageView) findViewById(R.id.left_res_title);
		left_res_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (onclick) {
					if (AppContext.getInstance().getUserInfo().getResumeId() == -1) {

						onclick = false;
						final HintDialogwk dialogwk3 = new HintDialogwk(
								CompanyInfoActivity.this,
								"请完善你的简历，让同频的老板第一时间找到你！", "去完善");
						dialogwk3.show();
						dialogwk3
								.setOneClicklistener(new DoOneClickListenerInterface() {

									@Override
									public void doOne() {
										startActivity(new Intent(
												CompanyInfoActivity.this,
												MrrckResumeActivity.class));
										dialogwk3.dismiss();
									}
								});

					} else {
						finish();
					}
				} else {
					finish();
				}
			}
		});
		BossTypeDialogWk bossTypeDialogWk = new BossTypeDialogWk(
				CompanyInfoActivity.this);
		bossTypeDialogWk.show();
		String guide = (String) PreferHelper.getSharedParam(
				ConstantKey.SP_IS_CASEDETAILGUIDE, "0");
		if (guide.equals("1")) {
			bossTypeDialogWk.dismiss();
		}
		PreferHelper.setSharedParam(ConstantKey.SP_IS_CASEDETAILGUIDE, "1");
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_res_title = (ImageView) findViewById(R.id.right_res_title);
		webView = (WebView) findViewById(R.id.webview);
		list.add(new PopupData("分享", R.drawable.show_fengxiang));
		list.add(new PopupData("收藏", R.drawable.show_shoucang));
		myPopupwindow = new MyPopupwindow(this, list, new popListener() {

			@Override
			public void doChoose(int position) {
				switch (position) {
				case 0:
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {

						if (!Tool.isEmpty(shareUrl)) {
							new InviteFriendDialog(CompanyInfoActivity.this,
									shareUrl, shareTitle, shareContent,
									shareImg, companyId,
									ConstantKey.ShareStatus_COMPANY).show();
							myPopupwindow.dismiss();
						} else {
							ToastUtil.showShortToast("分享网址有误！");
						}
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInfoActivity.this);
					}
					break;
				case 1:
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						if ("1".equals(collectFlag)) {
							ToastUtil.showShortToast("您已收藏");
						} else {
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getUserId());
							map.put("companyId", companyId);
							LogUtil.e(map + "");
							req.setHeader(new ReqHead(
									AppConfig.BUSINESS_FINDJOB_COLLECT));
							req.setBody(JsonUtil.Map2JsonObj(map));
							httpPost(reqCodeTwo,
									AppConfig.RESUME_REQUEST_MAPPING, req);
						}

					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInfoActivity.this);
					}
					break;
				default:
					break;
				}
			}
		});

	}

	@Override
	public void bindListener() {
		right_res_title.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "sssssssssssss=" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("data").toString().length() > 2)) {
				try {
					String json = resp.getBody().get("data").toString();
					jobData = (JobEntity) JsonUtil.jsonToObj(JobEntity.class,
							json);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				companyId = jobData.getCompanyId() + "";
				shareImg = jobData.getShareImg();
				shareTitle = jobData.getShareTitle();
				shareContent = jobData.getShareContent();
				shareUrl = jobData.getShareUrl();
				collectFlag = jobData.getCollectFlag();
			} else {
				ToastUtil.showShortToast("无更多数据");
			}

			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("收藏成功");
			collectFlag = "1";
			break;
		case reqCodeThree:
			if ((resp.getBody().get("position").toString().length() > 2)) {
				try {
					String json = resp.getBody().get("position").toString();
					comEntity = (CompanyEntity) JsonUtil.jsonToObj(
							CompanyEntity.class, json);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				companyId = comEntity.getId() + "";
				shareImg = comEntity.getShareImg();
				shareTitle = comEntity.getShareTitle();
				shareContent = comEntity.getShareContent();
				shareUrl = comEntity.getShareUrl();
				collectFlag = comEntity.getCollectFlag();
			} else {
				ToastUtil.showShortToast("无更多数据");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("数据获取失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("收藏失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("数据获取失败");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right_res_title:
			myPopupwindow.showAsDropDown(right_res_title,
					ScreenUtil.dip2px(CompanyInfoActivity.this, -80),
					ScreenUtil.dip2px(CompanyInfoActivity.this, 20));
			break;

		default:
			break;
		}
	}

	// JS调用本地方法
	final class JavaScriptInterface {
		@JavascriptInterface
		public void getDatasFromJS(String datas) {
		}
	}

	public void init() {
		removeCookie();
		settings = webView.getSettings();
		settings.setDomStorageEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setPluginState(WebSettings.PluginState.ON);
		settings.setJavaScriptEnabled(true);
		// settings.setAppCacheMaxSize(1024*1024*8);
		// String appCachePath =
		// getApplicationContext().getCacheDir().getAbsolutePath();
		// settings .setAppCachePath(appCachePath);
		// settings.setAppCacheEnabled(true);
		// settings.setSupportZoom(true);//支持缩放
		// settings.setBuiltInZoomControls(true);//缩放控件
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setTextSize(WebSettings.TextSize.NORMAL);
		webView.addJavascriptInterface(new JavaScriptInterface(), "mrrck");// 供JS调用本地方法的接口
		webView.setWebViewClient(new MyWebViewClient(this));
		webView.setWebChromeClient(new TestWebChromeClient(
				new WebChromeClient()) {
			private View myView = null;
			private CustomViewCallback myCallback = null;

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				if (myCallback != null) {
					myCallback.onCustomViewHidden();
					myCallback = null;
					return;
				}

				ViewGroup parent = (ViewGroup) webView.getParent();
				parent.removeView(webView);
				parent.addView(view);
				myView = view;
				myCallback = callback;
				// mChromeClient = this;
			}

			@Override
			public void onHideCustomView() {
				if (myView != null) {
					if (myCallback != null) {
						myCallback.onCustomViewHidden();
						myCallback = null;
					}

					ViewGroup parent = (ViewGroup) myView.getParent();
					parent.removeView(myView);
					parent.addView(webView);
					myView = null;
				}
			}

			// 配置权限（同样在WebChromeClient中实现）
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					android.webkit.GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			// The undocumented magic method override
			// Eclipse will swear at you if you try to put @Override here
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				openFileChooser(uploadMsg);
			}

			// For Android > 4.1.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				openFileChooser(uploadMsg, acceptType);
			}

			// 更新进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					sendDataToJS();
				} else {
				}
				super.onProgressChanged(view, newProgress);
			}

			// 设置当前activity的标题栏
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				center_txt_title.setText(title);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {

				// ToastUtil.showShortToast("aaaaa  " + message);
				return super.onJsAlert(view, url, message, result);
			}
		});

	}

	private void removeCookie() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieManager.getInstance().removeSessionCookie();
		CookieSyncManager.getInstance().sync();
		CookieSyncManager.getInstance().startSync();
	}

	// 传信息到JS
	private void sendDataToJS() {
		MkUser userDTO = AppContext.getInstance().getUserInfo();
		// UserTempCompanyData tmpCompany = new UserTempCompanyData();
		// tmpCompany.index = userDTO. getCompanyEntity().getId();
		// userDTO.companyDto = tmpCompany;
		String userInfo = JsonUtil.objToJson(userDTO);
		webView.loadUrl("javascript:jQuery.sysGlobal.setDataBySource('"
				+ JsonUtil.ToZhuanyiJson(userInfo) + "')");
		webView.loadUrl("javascript:jQuery.sysGlobal.setDevType(2)");
		// webView.loadUrl("javascript:getFromAndroid('" + userInfo + "')");
	}

	@Override
	public void initValue() {
		init();
		// 点击职位进入页面
		String comeUrl = getIntent().getStringExtra("comeUrl");
		int key = getIntent().getIntExtra("key", -1);
		String type = getIntent().getStringExtra("type");
		// 分享使用
		String companyId_fromShare = getIntent().getStringExtra(
				"companyId_fromShare");// 分享使用
		String sharUrl_fromShare = getIntent().getStringExtra(
				"sharUrl_fromShare");// 分享使用

		if (!Tool.isEmpty(companyId_fromShare)
				&& !Tool.isEmpty(sharUrl_fromShare)) {
			LogUtil.d("hl", "companyId_fromShare=====" + companyId_fromShare);
			LogUtil.d("hl", "sharUrl_fromShare=====" + sharUrl_fromShare);
			webView.loadUrl(sharUrl_fromShare);
			getCompanyData(companyId_fromShare);
			return;
		}
		if (!Tool.isEmpty(comeUrl) && key != -1) {
			LogUtil.d("hl", "****comeUrl=====" + comeUrl);
			LogUtil.d("hl", "****key=====" + key);
			webView.loadUrl(comeUrl + key);
			if ("fromJob".equals(type)) {
				getJobData(key);
			} else {
				getCompanyData(key + "");
			}
			return;
		}
		finish();
		ToastUtil.showShortToast("参数有误，进入页面失败！");
	}

	// http://192.168.1.188:8080/meiku/view/service/job/job_detail.html?id=174&userId=399&shareDevFlag=1
	private class MyWebViewClient extends WebViewClient {
		private Context mContext;

		public MyWebViewClient(Context context) {
			super();
			mContext = context;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			LogUtil.d("" + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtil.d("hl", "url=" + url);
			if (url.contains("image_href")) {
				toViewImage(url);
			} else if (url.contains("http://login_href/?")) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(CompanyInfoActivity.this);
				}
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				webView.getContext().startActivity(intent);
			} else if (url.contains("http://edit_person_resume/")) { // 修改个人简历
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(CompanyInfoActivity.this);
				} else {
					Intent intent2 = new Intent(CompanyInfoActivity.this,
							MrrckResumeActivity.class);
					startActivity(intent2);
				}
			} else if (url.contains("goto://mrrck.com/?")) {

				String type = url.substring(url.indexOf("=") + 1,
						url.indexOf("=") + 5);
				Map<String, String> map = new HashMap<String, String>();
				try {
					if (url.contains("&params=")) {
						map = JsonUtil.jsonToMap(URLDecoder.decode(
								url.substring(url.indexOf("&params=") + 8,
										url.length()), "UTF-8"));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				switch (Integer.parseInt(type)) {

				case 2012:// 一个按钮 breakType='0';//0 原生 1h5 2关闭
					final String breaktype = map.get("breakType");
					final String functionUrl = map.get("functionUrl");// 1代表
																		// 去创建简历
					final HintDialogwk dialogwk = new HintDialogwk(
							CompanyInfoActivity.this, map.get("msg"),
							map.get("buttonName"));
					dialogwk.show();
					dialogwk.setOneClicklistener(new DoOneClickListenerInterface() {

						@Override
						public void doOne() {
							if ("0".equals(breaktype)) {
								if ("1".equals(functionUrl)) {
									startActivity(new Intent(
											CompanyInfoActivity.this,
											MrrckResumeActivity.class));
									dialogwk.dismiss();
								}

							} else if ("1".equals(breaktype)) {
								webView.loadUrl(functionUrl);

							} else if ("2".equals(breaktype)) {
								dialogwk.dismiss();
							}
						}
					});
					break;
				case 2013:
					ToastUtil.showShortToast(map.get("msg"));
					break;
				}

			} else {
				webView.loadUrl(url);
			}
			return true;
		}
	}

	// 查看网页图片
	private void toViewImage(String url) {
		LogUtil.d("hl", "web——all-pic=" + url);
		String images = url.substring(url.indexOf('=') + 1, url.indexOf('&'));
		String[] imageUrls = images.split(",");
		Intent intent = new Intent();
		intent.setClass(this, ImagePagerActivity.class);
		List<AttachmentListDTO> image = new ArrayList<AttachmentListDTO>();
		for (int i = 0; i < imageUrls.length; i++) {
			AttachmentListDTO attachmentListDTO = new AttachmentListDTO();
			attachmentListDTO.setClientFileUrl(imageUrls[i]);
			image.add(attachmentListDTO);
		}
		intent.putParcelableArrayListExtra("imageDates",
				(ArrayList<? extends Parcelable>) image);
		String indexStr = url.substring(url.lastIndexOf("&") + 1, url.length());
		int index = 0;
		if (!Tool.isEmpty(indexStr)) {
			try {
				index = Integer.parseInt(indexStr);
			} catch (Exception e) {
				index = 0;
			}
		}
		intent.putExtra("index", index);
		startActivity(intent);
	}

	public void getJobData(int jobId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("jobId", jobId);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_FINDJOB_SELECTINFORMATION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.RESUME_REQUEST_MAPPING, req, false);
	}

	public void getCompanyData(String companyId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("companyId", companyId);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_FINDJOB_GONGSI));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.RESUME_REQUEST_MAPPING, req, false);
	}

}
