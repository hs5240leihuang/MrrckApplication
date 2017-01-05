package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ProductInfoEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;

/**
 * 产品详情
 * 
 * @author zhunaixin
 * 
 */
public class ProductDetailActivity extends BaseActivity implements
		View.OnClickListener {

	private WebView webView;
	private WebSettings settings;
	private List<PopupData> list = new ArrayList<PopupData>();
	private MyPopupwindow myPopupwindow;
	private ImageView right_res_title;
	private CommonDialog commonDialog;
	private String productId;
	private String url;
	private ProductInfoEntity productInfoEntity;
	private TextView btnAddfriend, intent_apply, tv_name, tv_phone;
	private LinearLayout layout_contact;
	private String productShareDetailUrl;

	// @Override
	// protected void onResume() {
	// super.onResume();
	// // 友盟统计activity
	// MobclickAgent.onPageStart(this.getClass().getName());
	// MobclickAgent.onResume(this);
	// }
	//
	// @Override
	// protected void onPause() {
	// super.onPause();
	// // 友盟统计activity
	// MobclickAgent.onPageEnd(this.getClass().getName());
	// MobclickAgent.onPause(this);
	// }

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_product_detail;
	}

	// JS调用本地方法
	final class JavaScriptInterface {
		@JavascriptInterface
		public void getDatasFromJS(String datas) {
		}
	}

	@Override
	public void initView() {
		layout_contact = (LinearLayout) findViewById(R.id.layout_contact);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_name = (TextView) findViewById(R.id.tv_name);
		btnAddfriend = (TextView) findViewById(R.id.btnAddfriend);
		intent_apply = (TextView) findViewById(R.id.intent_apply);
		commonDialog = new CommonDialog(ProductDetailActivity.this, "提示",
				"是否删除产品", "确定", "取消");
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {

					@Override
					public void doConfirm() {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userId", AppContext.getInstance()
								.getUserInfo().getUserId());
						map.put("productId", productId);
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_PRODUCT_DELECT));
						req.setBody(JsonUtil.Map2JsonObj(map));
						httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING,
								req);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});

		right_res_title = (ImageView) findViewById(R.id.right_res_title);

		webView = (WebView) findViewById(R.id.webview);
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
		productId = getIntent().getStringExtra("productId");
		getData();
	}

	private void initRightPopupwindow(String publishUserId) {
		if ((AppContext.getInstance().getUserInfo().getUserId() + "")
				.equals(publishUserId)) {
			list.add(new PopupData("分享", R.drawable.show_fengxiang));
			list.add(new PopupData("编辑", R.drawable.show_bianji));
			list.add(new PopupData("删除", R.drawable.show_shanchu));
			myPopupwindow = new MyPopupwindow(this, list, new popListener() {

				@Override
				public void doChoose(int position) {
					switch (position) {
					case 0:
						if (productInfoEntity != null
								&& !Tool.isEmpty(productShareDetailUrl)) {
							new InviteFriendDialog(ProductDetailActivity.this,
									productShareDetailUrl,
									"[美库找产品]"
											+ productInfoEntity
													.getProductName(),
									"好产品一览无余~",
									productInfoEntity.getClientPosterMain(),
									productInfoEntity.getId() + "",
									ConstantKey.ShareStatus_PRODUCT).show();
							myPopupwindow.dismiss();
						} else {
							ToastUtil.showShortToast("分享的产品信息有误！");
						}
						break;
					case 1:
						Intent i = new Intent(ProductDetailActivity.this,
								EditProductActivity.class);
						i.putExtra("productId", Integer.parseInt(productId));
						startActivity(i);
						myPopupwindow.dismiss();
						break;
					case 2:
						commonDialog.show();
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
						if (productInfoEntity != null
								&& !Tool.isEmpty(productShareDetailUrl)) {
							new InviteFriendDialog(ProductDetailActivity.this,
									productShareDetailUrl, "[美库找产品]"
											+ productInfoEntity
													.getProductName(),
									"好产品一览无余~", productInfoEntity
											.getClientPosterMain(),
									productInfoEntity.getId() + "",
									ConstantKey.ShareStatus_PRODUCT).show();
							myPopupwindow.dismiss();
						} else {
							ToastUtil.showShortToast("分享的产品信息有误！");
						}
						break;
					case 1:
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getUserId());
							map.put("productId", productId);
							LogUtil.e(map + "");
							req.setHeader(new ReqHead(
									AppConfig.BUSINESS_PRODUCT_COLLECTION));
							req.setBody(JsonUtil.Map2JsonObj(map));
							httpPost(reqCodeThree,
									AppConfig.PRODUCT_REQUEST_MAPPING, req);
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(ProductDetailActivity.this);
						}
						break;
					default:
						break;
					}
				}
			});
		}
	}

	public void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", productId);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_DETAIL_URL));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PRODUCT_REQUEST_MAPPING, req);
	}

	@Override
	public void bindListener() {
		right_res_title.setOnClickListener(this);
		btnAddfriend.setOnClickListener(this);
		intent_apply.setOnClickListener(this);
		layout_contact.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e(resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("删除成功");
			Intent intent = new Intent();
			intent.setAction(BroadCastAction.ACTION_MYPRODUCT);
			LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
					.getInstance(ProductDetailActivity.this);
			localBroadcastManager.sendBroadcast(intent);
			finish();
			break;
		case reqCodeTwo:
			if (resp.getBody() != null) {
				if (resp.getBody().get("productDetailUrl") != null) {
					url = resp.getBody().get("productDetailUrl").getAsString();
				}
				if (resp.getBody().get("productShareDetailUrl") != null) {
					productShareDetailUrl = resp.getBody()
							.get("productShareDetailUrl").getAsString();
				}
				if (!Tool.isEmpty(url) && url.contains("productId")) {
					webView.loadUrl(url);
				} else {
					ToastUtil.showShortToast("产品详情网址有误！");
				}
				String currentUserId = "";
				if (resp.getBody().get("productInfo").toString().length() > 2) {
					productInfoEntity = (ProductInfoEntity) JsonUtil.jsonToObj(
							ProductInfoEntity.class,
							resp.getBody().get("productInfo").toString());
					currentUserId = productInfoEntity.getUserId() + "";
					tv_name.setText(productInfoEntity.getContactName());
					tv_phone.setText(productInfoEntity.getContactPhone());
				} else {
					ToastUtil.showShortToast("该产品已删除");
				}
				initRightPopupwindow(currentUserId);
			} else {
				ToastUtil.showShortToast("该产品已删除");
			}
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("收藏成功");
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeThree:
			ToastUtil.showShortToast("您已收藏该产品");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("网络获取数据失败！");
			break;
		case reqCodeOne:
			ToastUtil.showShortToast("删除失败");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_res_title:
			if (myPopupwindow != null) {
				myPopupwindow.showAsDropDown(right_res_title,
						ScreenUtil.dip2px(ProductDetailActivity.this, -80),
						ScreenUtil.dip2px(ProductDetailActivity.this, 20));
				// commonPopMenu.showWindow(more);// show popWindow
			}
			break;
		case R.id.btnAddfriend:
			if (!NetworkTools.isNetworkAvailable(ProductDetailActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			Intent i = new Intent(ProductDetailActivity.this,
					PersonShowActivity.class);
			i.putExtra(PersonShowActivity.TO_USERID_KEY,
					productInfoEntity.getUserId() + "");
			startActivity(i);
			break;
		case R.id.intent_apply:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(ProductDetailActivity.this);
				return;
			}
			if (!NetworkTools.isNetworkAvailable(ProductDetailActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			Intent intent = new Intent(ProductDetailActivity.this,
					IntentApplyActivity.class);
			intent.putExtra("product", productId + "");
			intent.putExtra("productUserId", productInfoEntity.getUserId() + "");
			intent.putExtra("productName", productInfoEntity.getProductName());
			intent.putExtra("category", productInfoEntity.getCategoryName());
			startActivity(intent);
			break;
		case R.id.layout_contact:
			final CommonDialog commonDialog = new CommonDialog(
					ProductDetailActivity.this, "提示", "拨打电话"
							+ productInfoEntity.getContactPhone(), "确定", "取消");
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							Uri smsToUri = Uri.parse("tel:"
									+ productInfoEntity.getContactPhone());
							Intent mIntent = new Intent(
									android.content.Intent.ACTION_DIAL,
									smsToUri);
							startActivity(mIntent);
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
			commonDialog.show();
			break;
		default:
			break;
		}

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
			if (url.contains("image_href")) {
				toViewImage(url);
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
		intent.setClass(ProductDetailActivity.this, ImagePagerActivity.class);
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
}
