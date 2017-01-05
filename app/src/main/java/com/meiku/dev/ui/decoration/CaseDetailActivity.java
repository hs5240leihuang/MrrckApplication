package com.meiku.dev.ui.decoration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 案例详情
 * 
 */
public class CaseDetailActivity extends BaseActivity implements OnClickListener {
	private FrameLayout ft_mengceng;
	private TextView center_txt_title;
	private ImageView right_res_title;
	private WebView webView;
	private List<PopupData> list = new ArrayList<PopupData>();
	private MyPopupwindow myPopupwindow;
	private WebSettings settings;
	private String loadUrl = "";
	private int userId;
	private int sourceId;
	private String shareTitle;
	private String shareContent;
	private String shareImg;
	private String shareUrl;
	private String shareJsonStr;
	private LinearLayout goback;

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
		return R.layout.activity_casedetail;
	}

	@Override
	public void initView() {
		shareTitle = getIntent().getStringExtra("shareTitle");
		shareContent = getIntent().getStringExtra("shareContent");
		shareImg = getIntent().getStringExtra("shareImg");
		shareUrl = getIntent().getStringExtra("shareUrl");
		loadUrl = getIntent().getStringExtra("loadUrl");
		userId = getIntent().getIntExtra("userId", -1);
		sourceId = getIntent().getIntExtra("sourceId", -1);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("loadUrl", loadUrl);
		map.put("shareUrl", shareUrl);
		map.put("title", shareTitle);
		map.put("content", shareContent);
		map.put("image", shareImg);
		map.put("sourceId", sourceId);
		shareJsonStr = JsonUtil.hashMapToJsonD(map);

		LogUtil.e("wangke", "shareJsonStr = " + shareJsonStr);

		ft_mengceng = (FrameLayout) findViewById(R.id.ft_mengceng);
		String guide = (String) PreferHelper.getSharedParam(
				ConstantKey.SP_IS_CASEDETAILGUIDE, "0");
		if (guide.equals("1")) {
			ft_mengceng.setVisibility(View.GONE);
		}
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_res_title = (ImageView) findViewById(R.id.right_res_title);
		goback = (LinearLayout) findViewById(R.id.goback);
		webView = (WebView) findViewById(R.id.webview);
		init();
	}

	@Override
	public void initValue() {
		webView.loadUrl(loadUrl);
		if (AppContext.getInstance().getUserInfo().getUserId() == userId) {

			list.add(new PopupData("分享", R.drawable.show_fengxiang));
			list.add(new PopupData("删除", R.drawable.show_shoucang));
			list.add(new PopupData("编辑", R.drawable.show_bianji));
			myPopupwindow = new MyPopupwindow(this, list, new popListener() {

				@Override
				public void doChoose(int position) {
					switch (position) {
					case 0:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {

							if (!Tool.isEmpty(shareUrl)
									&& !Tool.isEmpty(loadUrl)) {
								new InviteFriendDialog(CaseDetailActivity.this,
										shareUrl, shareTitle, shareContent,
										shareImg, shareJsonStr,
										ConstantKey.ShareStatus_DECCASE).show();
								myPopupwindow.dismiss();
							} else {
								ToastUtil.showShortToast("分享网址有误！");
							}
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(CaseDetailActivity.this);
						}
						break;
					case 1:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							delete();
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(CaseDetailActivity.this);
						}
						break;
					case 2:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							startActivityForResult(
									new Intent(CaseDetailActivity.this,
											PublishCaseActivity.class)
											.putExtra("flag", 1).putExtra(
													"sourceId", sourceId),
									reqCodeThree);
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(CaseDetailActivity.this);
						}
						break;
					default:
						break;
					}
				}
			});

		} else {
			list.add(new PopupData("分享", R.drawable.show_fengxiang));
			list.add(new PopupData("收藏", R.drawable.show_shoucang));
			myPopupwindow = new MyPopupwindow(this, list, new popListener() {

				@Override
				public void doChoose(int position) {
					switch (position) {
					case 0:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							if (!Tool.isEmpty(shareUrl)
									&& !Tool.isEmpty(loadUrl)) {
								new InviteFriendDialog(CaseDetailActivity.this,
										shareUrl, shareTitle, shareContent,
										shareImg, shareJsonStr,
										ConstantKey.ShareStatus_DECCASE).show();
								myPopupwindow.dismiss();
							} else {
								ToastUtil.showShortToast("分享网址有误！");
							}
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(CaseDetailActivity.this);
						}
						break;
					case 1:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							Collect();
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(CaseDetailActivity.this);
						}
						break;

					default:
						break;
					}
				}
			});
		}

	}

	@Override
	public void bindListener() {
		right_res_title.setOnClickListener(this);
		goback.setOnClickListener(this);
		ft_mengceng.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right_res_title:
			myPopupwindow.showAsDropDown(right_res_title,
					ScreenUtil.dip2px(CaseDetailActivity.this, -80),
					ScreenUtil.dip2px(CaseDetailActivity.this, 20));
			break;
		case R.id.ft_mengceng:
			ft_mengceng.setVisibility(View.GONE);
			PreferHelper.setSharedParam(ConstantKey.SP_IS_CASEDETAILGUIDE, "1");
			break;
		case R.id.goback:
			if (webView != null) {
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}
			} else {
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("收藏成功");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("删除成功");
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage()
						.toString());
			}
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

	private class MyWebViewClient extends WebViewClient {
		private Context mContext;

		public MyWebViewClient(Context context) {
			super();
			mContext = context;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
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
							.showTipToLoginDialog(CaseDetailActivity.this);
				}
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				webView.getContext().startActivity(intent);
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
				case 1003:// 跳个人主页
					startActivity(new Intent(CaseDetailActivity.this,
							PersonShowActivity.class).putExtra("toUserId",
							map.get("userId")));
					break;
				case 2008:// 申请设计
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CaseDetailActivity.this);
					} else {
						startActivity(new Intent(CaseDetailActivity.this,
								ApplicationDesignActivity.class).putExtra(
								"companyId", map.get("companyId")));
					}
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

	// 收藏接口
	public void Collect() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", 0);// 案例 0 公司 1
		map.put("sourceId", sourceId);
		map.put("collecteFlag", 1);// 1 表示收藏 2 表示取消收藏
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300022));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
		Log.d("wangke", map + "");
	}

	// 删除接口
	public void delete() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("deleteType", 2);
		map.put("sourceId", sourceId);
		req.setHeader(new ReqHead(AppConfig.ZX_300024));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				webView.loadUrl(loadUrl);
				setResult(RESULT_OK);
			}
		}
	}
}
