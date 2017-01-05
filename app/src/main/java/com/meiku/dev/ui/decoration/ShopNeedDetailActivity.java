package com.meiku.dev.ui.decoration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.community.EditPostActivity;
import com.meiku.dev.ui.community.ReleaseTopicActivity;
import com.meiku.dev.ui.community.ReportActivity;
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.FanyeView;
import com.meiku.dev.views.FanyeView.FanyeClickListener;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.ObservableWebView;
import com.meiku.dev.views.ObservableWebView.OnScrollChangedCallback;

public class ShopNeedDetailActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout layoutFanye;
	private TextView btnShoucang;
	private String postsId;
	private String boardId;
	private String flag;
	private int currentPage = 1;
	private FanyeView fanyeView;
	private PostsEntity postBean;
	private String clientPostsDetailUrl;
	private String loadUrl;
	private ObservableWebView webView;
	private WebSettings settings;
	private final int REPLAYBACK = 111;// 回复后返回的TAG
	private static final int REQCODE_EDITPOST = 8002;
	private static final int REQCODE_DELETECOMMENT = 8001;
	private static final int REQCODE_DELETEPOST = 8000;
	private String shareUrl;
	private boolean hasNewReplay;
	private boolean hasEdited;
	private boolean hasDoCollect;
	private boolean hasDoCreat;
	protected int currentY;
	private int lastY;
	private boolean isflag;
	private TextView right_txt_title;
	private int needId;
	private TextView center_txt_title;

	// @Override
	// public void onResume() {
	// super.onResume();
	// MobclickAgent.onPageStart(getClass().getName());
	// MobclickAgent.onResume(this);
	// }
	//
	// @Override
	// public void onPause() {
	// super.onPause();
	// MobclickAgent.onPageEnd(getClass().getName());
	// MobclickAgent.onPause(this);
	// }

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_shopneeddetail;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setTextColor(Color.parseColor("#FF3499"));
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity((new Intent(ShopNeedDetailActivity.this,
						MyNeedDecPubActivity.class)));
			}
		});
		layoutFanye = (LinearLayout) findViewById(R.id.layoutFanye);
		initWebView();
	}

	private void initWebView() {
		webView = (ObservableWebView) findViewById(R.id.webview);
		webView.setOnScrollChangedCallback(new OnScrollChangedCallback() {

			@Override
			public void onScroll(int dx, int dy) {
				currentY = dy;
			}
		});
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

	@Override
	public void initValue() {
		needId = getIntent().getIntExtra("needId", -1);
		loadUrl = getIntent().getStringExtra("loadUrl");
		postsId = getIntent().getIntExtra(ConstantKey.KEY_POSTID, -1) + "";
		boardId = getIntent().getStringExtra(ConstantKey.KEY_BOARDID);
		flag = getIntent().getStringExtra("FLAG");
		isflag = getIntent().getBooleanExtra("isflag", true);
		if (!isflag) {
			findViewById(R.id.btnCreat).setVisibility(View.GONE);
		}
		getPostDetail();
		getPostDetailH5ByPage(currentPage);
	}

	@Override
	public void bindListener() {
		findViewById(R.id.left_res_title).setOnClickListener(this);
		btnShoucang = (TextView) findViewById(R.id.btnShoucang);
		btnShoucang.setOnClickListener(this);
		findViewById(R.id.btnShare).setOnClickListener(this);
		findViewById(R.id.btnCreat).setOnClickListener(this);
		findViewById(R.id.btnGoto).setOnClickListener(this);
		findViewById(R.id.btnReply).setOnClickListener(this);
	}

	// 下拉重新刷新页面
	private void getPostDetail() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("postsId", postsId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		LogUtil.d("hl", "请求--帖子详情" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BOARD_POSTDETAIL_NEW));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, false);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "帖子详情" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil.getJSONType(resp
					.getBody().get("postEntity") + "")) {
				if ((resp.getBody().get("postEntity") + "").length() > 2) {
					postBean = (PostsEntity) JsonUtil.jsonToObj(
							PostsEntity.class, resp.getBody().get("postEntity")
									.toString());
					// if ((resp.getBody().get("clientPostsDetailUrl") + "")
					// .length() > 2) {
					// clientPostsDetailUrl = resp.getBody()
					// .get("clientPostsDetailUrl").getAsString();
					// LogUtil.d("hl", "clientPostsDetailUrl="
					// + clientPostsDetailUrl);
					// getPostDetailH5ByPage(currentPage);
					// }
					if (null != postBean) {
						// 回复总数
						setShoucangState("1".equals(postBean.getCollectFlag()));// 0:未收藏,1:已收藏
						Integer postsTotal = postBean.getPostsTotal();
						if (postsTotal > 0) {
							int page = postsTotal / ConstantKey.PageNum;
							setFanyeView(postsTotal % ConstantKey.PageNum == 0 ? page
									: page + 1);
						}
						/** 分享url路径拼接 */
						shareUrl = postBean.getPostsShareUrl();
					}
				}
			} else {
				ToastUtil.showShortToast("该帖子已删除");
				finish();
				return;

			}
			break;
		case reqCodeThree://
			postBean.setCollectFlag("1");// 0:未收藏 1:已收藏 */
			setShoucangState(true);
			ToastUtil.showShortToast("收藏成功");
			hasDoCollect = true;
			break;
		case reqCodeFour:
			postBean.setCollectFlag("0");// 0:未收藏 1:已收藏 */
			setShoucangState(false);
			ToastUtil.showShortToast("取消收藏");
			hasDoCollect = true;
			break;
		case REQCODE_DELETECOMMENT:
			lastY = currentY;
			ToastUtil.showShortToast("删除成功");
			getPostDetail();
			getPostDetailH5ByPage(currentPage);
			break;
		case REQCODE_DELETEPOST:
			hasEdited = true;
			ToastUtil.showShortToast("删除成功");
			setResult(RESULT_OK);
			if ("FROMHOME".equals(flag)) {// 从社区首页进来
				sendBroadcast(new Intent(
						BroadCastAction.ACTION_BROAD_REFRESH_HOMEPOST));
			}
			finish();
			break;
		}
	}

	private void getPostDetailH5ByPage(int currentPage) {
		StringBuilder sb = new StringBuilder();
		sb.append(loadUrl);
		// sb.append(postsId);
		sb.append("&userId=");
		sb.append(AppContext.getInstance().getUserInfo().getId());
		sb.append("&page=");
		sb.append(currentPage);
		sb.append("&pageNum=");
		sb.append(ConstantKey.PageNum);
		webView.loadUrl(sb.toString());
		LogUtil.d("hl", "帖子详情url==" + sb.toString());
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		case reqCodeOne:
			ToastUtil.showShortToast("获取数据失败！");
			// finish();
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("收藏失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("取消收藏失败");
			break;
		case REQCODE_DELETECOMMENT:
			ToastUtil.showShortToast("删除失败");
			break;
		default:
			break;
		}
	}

	/**
	 * 设置是否收藏状态
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void setShoucangState(boolean colleacted) {
		Drawable topDrawable;
		if (colleacted) {
			topDrawable = ContextCompat.getDrawable(
					ShopNeedDetailActivity.this, R.drawable.shoucang1);
		} else {
			topDrawable = ContextCompat.getDrawable(
					ShopNeedDetailActivity.this, R.drawable.shoucang);
		}
		topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
				topDrawable.getMinimumHeight());
		btnShoucang.setCompoundDrawables(null, topDrawable, null, null);
	}

	/**
	 * 初始化翻页view
	 * 
	 * @param allpageSize
	 */
	private void setFanyeView(int allpageSize) {
		fanyeView = new FanyeView(this, allpageSize, currentPage, layoutFanye,
				new FanyeClickListener() {

					@Override
					public void onPageClick(int page) {
						currentPage = page;
						lastY = 0;
						getPostDetailH5ByPage(currentPage);
					}
				});
	}

	// 收藏(取消 )帖子
	private void ShoucangPost(int requestTag, String reqId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("postsId", postsId);
		req.setHeader(new ReqHead(reqId));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(requestTag, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REPLAYBACK) {
				hasNewReplay = true;
				getPostDetail();
				getPostDetailH5ByPage(currentPage);
			} else if (requestCode == REQCODE_EDITPOST) {
				hasEdited = true;
				getPostDetail();
				getPostDetailH5ByPage(currentPage);
				if ("FROMHOME".equals(flag)) {// 从社区首页进来,刷新
					sendBroadcast(new Intent(
							BroadCastAction.ACTION_BROAD_REFRESH_HOMEPOST));
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_res_title:
			finishWithResult();
			break;
		case R.id.btnReply:
			if (postBean != null) {
				Intent intent = new Intent(ShopNeedDetailActivity.this,
						ReflexActivity.class);
				intent.putExtra("postsId", postsId + "");
				intent.putExtra("toUserId", "0");
				intent.putExtra("toCommentId", "0");
				intent.putExtra("boardId", postBean.getBoardId() + "");
				intent.putExtra("toUserName", postBean.getNickName() + "");
				intent.putExtra("floorNum", "0");
				startActivityForResult(intent, REPLAYBACK);
			}
			break;
		case R.id.btnShare:
			// 点击分享页面
			if (null != shareUrl && postBean != null) {
				String postImg = "";
				if (!Tool.isEmpty(postBean.getAttachmentList())) {
					postImg = postBean.getAttachmentList().get(0)
							.getClientFileUrl();
				}
				new InviteFriendDialog(
						ShopNeedDetailActivity.this,
						shareUrl,
						postBean.getTitle(),
						postBean.getContent(),
						Tool.isEmpty(postImg) ? "http://h5.mrrck.com/images/meikuShareIcon.png"
								: postImg, postsId,
						ConstantKey.ShareStatus_NEWS_STATE).show();
			} else {
				ToastUtil.showLongToast("分享失败");
			}
			break;
		case R.id.btnGoto:
			if (fanyeView != null) {
				if (fanyeView.isShowing()) {
					fanyeView.showFanye(false);
				} else {
					fanyeView.showFanye(true);
				}
			} else {
				ToastUtil.showShortToast("当前帖子还没有任何回复!");
			}
			break;
		case R.id.btnShoucang:
			if (postBean != null) {
				if ("0".equals(postBean.getCollectFlag())) {// 0:未收藏 1:已收藏
					ShoucangPost(reqCodeThree,
							AppConfig.BUSINESS_BOARD_SHOUCANG);
				} else {
					ShoucangPost(reqCodeFour,
							AppConfig.BUSINESS_BOARD_NOTSHOUCANG);
				}
			}
			break;
		case R.id.btnCreat:
			if (postBean != null) {
				Intent intent = new Intent(ShopNeedDetailActivity.this,
						ReleaseTopicActivity.class);
				intent.putExtra(ReleaseTopicActivity.KEY_BOARDID,
						String.valueOf(boardId));
				intent.putExtra("menuId", postBean.getMenuId() + "");
				startActivity(intent);
				hasDoCreat = true;
			}
			break;
		default:
			break;
		}
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

	// JS调用本地方法
	final class JavaScriptInterface {
		@JavascriptInterface
		public void getDatasFromJS(String datas) {
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
			return;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			handler.sendEmptyMessageDelayed(0, 500);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("hl", "shouldOverrideUrlLoadingURL地址:" + url);
			if (url.contains("image_href")) {
				toViewImage(url);
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				webView.getContext().startActivity(intent);
			} else if (url.contains("http://login_href/?")) {
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
				case 1003:
					break;
				case 1004:
					String nickname = map.get("nickName");
					String content = map.get("content");
					String sourceId = map.get("sourceId");
					String sourceType = map.get("sourceType");
					Intent intent = new Intent(ShopNeedDetailActivity.this,
							ReportActivity.class);
					intent.putExtra("content", content);
					intent.putExtra("nickname", nickname);
					intent.putExtra("sourceType", sourceType);// sourceType帖子是1,评论是2
					intent.putExtra("sourceId", sourceId);
					startActivity(intent);
					break;
				case 1005:
					String postsCommentId = map.get("postsCommentId");
					String deleteType = map.get("deleteType");
					if ("1".equals(deleteType)) {// 1删除帖子，2删除评论
						showTipDialogDeletePost(postBean);
					} else {
						showTipDialogDeleteReply(postsCommentId);
					}
					break;
				case 1006:
				case 1009:
					String toUserId = map.get("toUserId");
					String toCommentId = map.get("toCommentId");
					String boardId = map.get("boardId");
					String toUserName = map.get("toUserName");
					String floorNum = map.get("floorNum");
					String flag = map.get("flag");// 1回复楼主，2回复评论
					Intent intent2 = new Intent(ShopNeedDetailActivity.this,
							ReflexActivity.class);
					intent2.putExtra("postsId", postsId + "");
					intent2.putExtra("toUserId", toUserId);
					intent2.putExtra("toCommentId", toCommentId);
					intent2.putExtra("boardId", boardId);
					intent2.putExtra("toUserName", toUserName);
					intent2.putExtra("floorNum", floorNum);
					startActivityForResult(intent2, REPLAYBACK);
					break;
				case 1007:
					Bundle bundle = new Bundle();
					bundle.putSerializable("postBean", postBean);
					startActivityForResult(
							new Intent(ShopNeedDetailActivity.this,
									EditPostActivity.class).putExtras(bundle),
							REQCODE_EDITPOST);
					break;
				case 1008:
					startActivityForResult(new Intent(
							ShopNeedDetailActivity.this,
							MyNeedDecPubActivity.class).putExtra("flag", 1)
							.putExtra("id", needId), REQCODE_EDITPOST);
					break;
				default:
					break;
				}
			} else {
				webView.loadUrl(url);
			}
			return true;
		}
	}

	// 提示删除帖子
	private void showTipDialogDeletePost(final PostsEntity postBean) {
		final CommonDialog commonDialog = new CommonDialog(
				ShopNeedDetailActivity.this, "提示", "删除该帖子?", "确定", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("postsId", postBean.getPostsId());
						map.put("userId", AppContext.getInstance()
								.getUserInfo().getId());
						map.put("boardId", postBean.getBoardId());
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_DELETE_POST));
						req.setBody(JsonUtil.Map2JsonObj(map));
						httpPost(REQCODE_DELETEPOST, AppConfig.PUBLICK_BOARD,
								req, true);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

	// 提示删除回复
	private void showTipDialogDeleteReply(final String postsCommentId) {
		final CommonDialog commonDialog = new CommonDialog(
				ShopNeedDetailActivity.this, "提示", "删除该回复?", "确定", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {

					@Override
					public void doConfirm() {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("postsCommentId", postsCommentId);
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_DELETE_COMMENT));
						req.setBody(JsonUtil.Map2JsonObj(map));
						httpPost(REQCODE_DELETECOMMENT,
								AppConfig.PUBLICK_BOARD, req, true);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWithResult();
		}
		return false;
	}

	/**
	 * 关闭页面返回是否有新的回复
	 */
	private void finishWithResult() {
		if ("FROMMINECOM".equals(flag)) {// 我的社区进来，收藏，编辑，回复，都需要返回后刷新
			if (hasNewReplay || hasEdited || hasDoCollect || hasDoCreat) {
				setResult(RESULT_OK, new Intent());
			}
		} else {
			if (hasNewReplay || hasEdited) {
				setResult(RESULT_OK, new Intent());
			}
		}
		finish();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onStop() {
		lastY = currentY;
		LogUtil.d("hl", "lastY====" + lastY);
		super.onStop();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			webView.scrollTo(0, lastY);
			LogUtil.d("hl", "lastY++++++++=" + lastY);
			super.handleMessage(msg);
		}
	};
}
