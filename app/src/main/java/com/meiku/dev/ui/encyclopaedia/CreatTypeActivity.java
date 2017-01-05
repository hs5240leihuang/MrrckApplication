package com.meiku.dev.ui.encyclopaedia;

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
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;

/**
 * 选择新创建词条类型
 * 
 */
public class CreatTypeActivity extends BaseActivity {

	protected String shareUrl;
	protected String keyUrl;
	private TextView center_txt_title;
	private WebView webView;
	private WebSettings settings;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_baike_creattype;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		webView = (WebView) findViewById(R.id.webview);
		init();
	}

	@Override
	public void initValue() {
		webView.loadUrl("http://h5.mrrck.com/view/service/encyclopedias/entry_create.html");

	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

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
							.showTipToLoginDialog(CreatTypeActivity.this);
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
				case 2001:
					startActivity(new Intent(CreatTypeActivity.this,
							CreatePersonEntryActivity.class));
					break;
				case 2002:
					startActivity(new Intent(CreatTypeActivity.this,
							CreatCompanyEntryActivity.class));
					break;
				case 2003:
					startActivity(new Intent(CreatTypeActivity.this,
							CreatCommonEntryActivity.class));
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

	private class TestWebChromeClient extends WebChromeClient {

		private WebChromeClient mWrappedClient;

		protected TestWebChromeClient(WebChromeClient wrappedClient) {
			mWrappedClient = wrappedClient;
		}

		/**
		 * }
		 */
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			mWrappedClient.onProgressChanged(view, newProgress);
		}

		/**
		 * }
		 */
		@Override
		public void onReceivedTitle(WebView view, String title) {
			mWrappedClient.onReceivedTitle(view, title);
		}

		/**
		 * }
		 */
		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			mWrappedClient.onReceivedIcon(view, icon);
		}

		/**
		 * }
		 */
		@Override
		public void onReceivedTouchIconUrl(WebView view, String url,
				boolean precomposed) {
			mWrappedClient.onReceivedTouchIconUrl(view, url, precomposed);
		}

		/**
		 * }
		 */
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			mWrappedClient.onShowCustomView(view, callback);
		}

		/**
		 * }
		 */
		@Override
		public void onHideCustomView() {
			mWrappedClient.onHideCustomView();
		}

		/**
		 * }
		 */
		@Override
		public boolean onCreateWindow(WebView view, boolean dialog,
				boolean userGesture, Message resultMsg) {
			return mWrappedClient.onCreateWindow(view, dialog, userGesture,
					resultMsg);
		}

		/**
		 * }
		 */
		@Override
		public void onRequestFocus(WebView view) {
			mWrappedClient.onRequestFocus(view);
		}

		/**
		 * }
		 */
		@Override
		public void onCloseWindow(WebView window) {
			mWrappedClient.onCloseWindow(window);
		}

		/**
		 * }
		 */
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			return mWrappedClient.onJsAlert(view, url, message, result);
		}

		/**
		 * }
		 */
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			return mWrappedClient.onJsConfirm(view, url, message, result);
		}

		/**
		 * }
		 */
		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {
			return mWrappedClient.onJsPrompt(view, url, message, defaultValue,
					result);
		}

		/**
		 * }
		 */
		@Override
		public boolean onJsBeforeUnload(WebView view, String url,
				String message, JsResult result) {
			return mWrappedClient.onJsBeforeUnload(view, url, message, result);
		}

		/**
		 * }
		 */
		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long currentQuota,
				long estimatedSize, long totalUsedQuota,
				WebStorage.QuotaUpdater quotaUpdater) {
			mWrappedClient.onExceededDatabaseQuota(url, databaseIdentifier,
					currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
		}

		/**
		 * }
		 */
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded,
				long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
			mWrappedClient.onReachedMaxAppCacheSize(spaceNeeded,
					totalUsedQuota, quotaUpdater);
		}

		/**
		 * }
		 */
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			mWrappedClient.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		/**
		 * }
		 */
		@Override
		public void onGeolocationPermissionsHidePrompt() {
			mWrappedClient.onGeolocationPermissionsHidePrompt();
		}

		/**
		 * }
		 */
		@Override
		public boolean onJsTimeout() {
			return mWrappedClient.onJsTimeout();
		}

		/**
		 * }
		 */
		@Override
		@Deprecated
		public void onConsoleMessage(String message, int lineNumber,
				String sourceID) {
			mWrappedClient.onConsoleMessage(message, lineNumber, sourceID);
		}

		/**
		 * }
		 */
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return mWrappedClient.onConsoleMessage(consoleMessage);
		}

		/**
		 * }
		 */
		@Override
		public Bitmap getDefaultVideoPoster() {
			return mWrappedClient.getDefaultVideoPoster();
		}

		/**
		 * }
		 */
		@Override
		public View getVideoLoadingProgressView() {
			return mWrappedClient.getVideoLoadingProgressView();
		}

		/**
		 * }
		 */
		@Override
		public void getVisitedHistory(ValueCallback<String[]> callback) {
			mWrappedClient.getVisitedHistory(callback);
		}

		/**
		 * }
		 */

		public void openFileChooser(ValueCallback<Uri> uploadFile) {
			((TestWebChromeClient) mWrappedClient).openFileChooser(uploadFile);
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
	}

}
