package com.meiku.dev.ui.encyclopaedia;

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
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;

/**
 * 词条详情
 * 
 */
public class EntriesDetailActivity extends BaseActivity implements
		OnClickListener {

	private TextView center_txt_title;
	private ImageView right_res_title;
	private WebView webView;
	private List<PopupData> list = new ArrayList<PopupData>();
	private MyPopupwindow myPopupwindow;
	private WebSettings settings;
	private int baikeId;
	private int categoryId;
	private int userId;
	private String loadUrl;
	private String shareUrl;
	private String title;
	private String content;
	private String image;
	private String jsonStr;
	private int flag;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_companyinfo;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_res_title = (ImageView) findViewById(R.id.right_res_title);
		webView = (WebView) findViewById(R.id.webview);
		init();
	}

	@Override
	public void initValue() {
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {
			try {

				jsonStr = getIntent().getStringExtra("jsonStr");
				Log.e("wangke", jsonStr);
				if (Tool.isEmpty("json")) {
					ToastUtil.showShortToast("参数有误！");
					finish();
					return;
				}
				Map<String, String> map = JsonUtil.jsonToMap(jsonStr);
				if (map.containsKey(baikeId)) {
					baikeId = Integer.parseInt(map.get("baikeId"));
				}
				if (map.containsKey(categoryId)) {
					categoryId = Integer.parseInt(map.get("categoryId"));
				}
				if (map.containsKey(userId)) {
					userId = Integer.parseInt(map.get("userId"));
				}
				loadUrl = map.get("loadUrl");
				shareUrl = map.get("shareUrl");
				title = map.get("title");
				content = map.get("content");
				image = map.get("image");
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			baikeId = getIntent().getIntExtra("baikeId", -1);
			categoryId = getIntent().getIntExtra("categoryId", -1);
			userId = getIntent().getIntExtra("userId", -1);
			loadUrl = getIntent().getStringExtra("loadUrl");
			shareUrl = getIntent().getStringExtra("shareUrl");
			title = getIntent().getStringExtra("title");
			content = getIntent().getStringExtra("content");
			image = getIntent().getStringExtra("image");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("baikeId", baikeId);
		map.put("categoryId", categoryId);
		map.put("userId", userId);
		map.put("loadUrl", loadUrl);
		map.put("shareUrl", shareUrl);
		map.put("title", title);
		map.put("content", content);
		map.put("image", image);
		jsonStr = JsonUtil.hashMapToJsonD(map);
		webView.loadUrl(loadUrl);
		if (AppContext.getInstance().getUserInfo().getUserId() == userId) {

			list.add(new PopupData("分享", R.drawable.show_fengxiang));
			list.add(new PopupData("删除", R.drawable.show_shoucang));
			list.add(new PopupData("编辑", R.drawable.show_bianji));
			if (categoryId == 1) {
				center_txt_title.setText("达人词条详情");
			} else if (categoryId == 2) {
				center_txt_title.setText("名企词条详情");
			} else {
				center_txt_title.setText("公共词条详情");
			}
			myPopupwindow = new MyPopupwindow(this, list, new popListener() {

				@Override
				public void doChoose(int position) {
					switch (position) {
					case 0:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {

							if (!Tool.isEmpty(shareUrl)
									&& !Tool.isEmpty(loadUrl)) {
								new InviteFriendDialog(
										EntriesDetailActivity.this, shareUrl,
										title, content, image, jsonStr,
										ConstantKey.ShareStatus_CITIAO).show();
								myPopupwindow.dismiss();
							} else {
								ToastUtil.showShortToast("分享网址有误！");
							}
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(EntriesDetailActivity.this);
						}
						break;
					case 1:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							delete();
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(EntriesDetailActivity.this);
						}
						break;
					case 2:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							if (categoryId == 1) {
								startActivityForResult(
										new Intent(EntriesDetailActivity.this,
												CreatePersonEntryActivity.class)
												.putExtra("pageType", 1)
												.putExtra("edit_baikeId",
														baikeId), reqCodeOne);
							} else if (categoryId == 2) {
								startActivityForResult(
										new Intent(EntriesDetailActivity.this,
												CreatCompanyEntryActivity.class)
												.putExtra("pageType", 1)
												.putExtra("edit_baikeId",
														baikeId), reqCodeOne);
							} else {
								startActivityForResult(
										new Intent(EntriesDetailActivity.this,
												CreatCommonEntryActivity.class)
												.putExtra("pageType", 1)
												.putExtra("edit_baikeId",
														baikeId), reqCodeOne);
							}
						}

						else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(EntriesDetailActivity.this);
						}
						break;
					default:
						break;
					}
				}
			});

		} else {
			if (categoryId == 3) {
				list.add(new PopupData("分享", R.drawable.show_fengxiang));
				list.add(new PopupData("收藏", R.drawable.show_shoucang));
				list.add(new PopupData("编辑", R.drawable.show_bianji));
				center_txt_title.setText("公共词条详情");
				myPopupwindow = new MyPopupwindow(this, list,
						new popListener() {

							@Override
							public void doChoose(int position) {
								switch (position) {
								case 0:
									if (AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {

										if (!Tool.isEmpty(shareUrl)
												&& !Tool.isEmpty(loadUrl)) {
											new InviteFriendDialog(
													EntriesDetailActivity.this,
													shareUrl,
													title,
													content,
													image,
													jsonStr,
													ConstantKey.ShareStatus_CITIAO)
													.show();
											myPopupwindow.dismiss();
										} else {
											ToastUtil.showShortToast("分享网址有误！");
										}
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(EntriesDetailActivity.this);
									}
									break;
								case 1:
									if (AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {
										Collect();
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(EntriesDetailActivity.this);
									}
									break;
								case 2:
									if (AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {
										startActivityForResult(
												new Intent(
														EntriesDetailActivity.this,
														CreatCommonEntryActivity.class)
														.putExtra("pageType", 1)
														.putExtra(
																"edit_baikeId",
																baikeId),
												reqCodeOne);
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(EntriesDetailActivity.this);
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
				if (categoryId == 1) {
					center_txt_title.setText("达人词条详情");
				} else if (categoryId == 2) {
					center_txt_title.setText("名企词条详情");
				} else {
					center_txt_title.setText("公共词条详情");
				}
				myPopupwindow = new MyPopupwindow(this, list,
						new popListener() {

							@Override
							public void doChoose(int position) {
								switch (position) {
								case 0:
									if (AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {

										if (!Tool.isEmpty(shareUrl)
												&& !Tool.isEmpty(loadUrl)) {
											new InviteFriendDialog(
													EntriesDetailActivity.this,
													shareUrl,
													title,
													content,
													image,
													jsonStr,
													ConstantKey.ShareStatus_CITIAO)
													.show();
											myPopupwindow.dismiss();
										} else {
											ToastUtil.showShortToast("分享网址有误！");
										}
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(EntriesDetailActivity.this);
									}
									break;
								case 1:
									if (AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {
										Collect();
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(EntriesDetailActivity.this);
									}
									break;

								default:
									break;
								}
							}
						});
			}

		}
	}

	@Override
	public void bindListener() {
		right_res_title.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right_res_title:
			myPopupwindow.showAsDropDown(right_res_title,
					ScreenUtil.dip2px(EntriesDetailActivity.this, -80),
					ScreenUtil.dip2px(EntriesDetailActivity.this, 20));
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
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######"
				+ resp.getHeader().getRetMessage().toString());
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast(resp.getHeader().getRetMessage()
					.toString());
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast(resp.getHeader().getRetMessage()
					.toString());
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
							.showTipToLoginDialog(EntriesDetailActivity.this);
				}
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				webView.getContext().startActivity(intent);
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
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("baikeId", baikeId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_COLLECT));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BAIKE, req);
		Log.d("wangke", map + "");
	}

	// 删除接口
	public void delete() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("baikeId", baikeId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_DELETEBAIKE));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BAIKE, req);
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
