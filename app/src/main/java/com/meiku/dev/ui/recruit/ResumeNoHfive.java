package com.meiku.dev.ui.recruit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.util.TextUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.meiku.dev.ui.decoration.ApplicationDesignActivity;
import com.meiku.dev.ui.decoration.CaseCommentActivity;
import com.meiku.dev.ui.decoration.CaseDetailActivity;
import com.meiku.dev.ui.encyclopaedia.CreatCommonEntryActivity;
import com.meiku.dev.ui.encyclopaedia.CreatCompanyEntryActivity;
import com.meiku.dev.ui.encyclopaedia.CreatePersonEntryActivity;
import com.meiku.dev.ui.encyclopaedia.EntriesDetailActivity;
import com.meiku.dev.ui.login.MkLoginActivity;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.mine.MyEntryActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.ui.morefun.MrrckResumeActivity;
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.GifView;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.HintDialogwk.ClickListenerInterface;
import com.meiku.dev.views.HintDialogwk.DoOneClickListenerInterface;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.meiku.dev.views.TopTitle;
import com.umeng.analytics.MobclickAgent;

public class ResumeNoHfive extends BaseActivity implements OnClickListener {
	public static final String TAG = "WebViewActivity";
	private WebView webView;
	private String webUrl;// 网页url
	private String color;// 页面颜色
	private TopTitle topTitle;// 标题栏
	private TextView titleView;// 操作栏标题
	private ProgressBar progressBar;// 进度条
	private String title = "";// 操作栏标题
	private WebSettings settings;// WebView设置

	private RelativeLayout loadFailRL;
	private GifView loadFailIMG;
	private Button reloadBTN;
	private int flag;
	private MyPopupwindow myPopupwindow;
	private List<PopupData> list = new ArrayList<PopupData>();

	private ValueCallback<Uri> mUploadMessage;
	private boolean notOnceBack = true;
	private int resumeId;
	private String shareImg, shareTitle, shareContent, shareUrl;

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
	public void initView() {
		flag = getIntent().getIntExtra("flag", 0);
		resumeId = getIntent().getIntExtra("resumeId", -1);
		getData();
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.right_res_title).setOnClickListener(this);
		loadFailRL = (RelativeLayout) findViewById(R.id.loadFailRL);
		reloadBTN = (Button) findViewById(R.id.reloadBTN);
		reloadBTN.setOnClickListener(this);
		loadFailIMG = (GifView) findViewById(R.id.loadFailIMG);
		loadFailIMG.setMovieResource(R.raw.load_fail);

		// topTitle = (TopTitle) findViewById(R.id.top_title);
		color = getIntent().getStringExtra("color");
		if ("".equals(color) || null == color) {
			color = "#FF5073";
		}
		// topTitle.setBackgroundColor(Color.parseColor(color));

		titleView = (TextView) findViewById(R.id.center_txt_title);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		title = getIntent().getStringExtra("title");
		if ("".equals(title) || null == title) {
			titleView.setText("");
		} else {
			titleView.setText(title);
		}
		webUrl = getIntent().getStringExtra("webUrl");
		// webUrl="http://192.168.1.128:3000/view/baike/baike_list.html";
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

				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				ResumeNoHfive.this.startActivityForResult(
						Intent.createChooser(i, "选择图片"), FILECHOOSER_RESULTCODE);
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
					progressBar.setVisibility(View.GONE);
					sendDataToJS();
				} else {
					if (View.GONE == progressBar.getVisibility()) {
						progressBar.setVisibility(View.VISIBLE);
					}
					progressBar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			// 设置当前activity的标题栏
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				titleView.setText(title);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {

				// ToastUtil.showShortToast("aaaaa  " + message);
				return super.onJsAlert(view, url, message, result);
			}
		});
		// webUrl
		webView.loadUrl(webUrl);

	}

	private void removeCookie() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieManager.getInstance().removeSessionCookie();
		CookieSyncManager.getInstance().sync();
		CookieSyncManager.getInstance().startSync();
	}

	private class MyWebViewClient extends WebViewClient {
		private Context mContext;

		public MyWebViewClient(Context context) {
			super();
			mContext = context;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "URL地址:" + url);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i(TAG, "onPageFinished");
			setWebTitle(view.getTitle());
			super.onPageFinished(view, url);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "shouldOverrideUrlLoadingURL地址:" + url);
			if (url.contains("http://login_href/?")) {
				if (AppContext.getInstance().isHasLogined()) {// 已登录
					// webView.loadUrl(url);
					loadFailRL.setVisibility(View.VISIBLE);
				} else {
					showTipToLoginDialog();
				}
			} else if (url.contains("image_href")) {
				toViewImage(url);
			} else if (url.contains("http://edit_person_resume/")) { // 修改个人简历
				// if (AppContext.getInstance().isHasLogined()) {
				// Intent intent2 = new Intent(WebViewActivity.this,
				// MrrckResumeActivity.class);
				// startActivity(intent2);
				// } else {
				// showTipToLoginDialog();
				// }

				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(ResumeNoHfive.this);
				} else {
					Intent intent2 = new Intent(ResumeNoHfive.this,
							MrrckResumeActivity.class);
					startActivity(intent2);
				}
			} else if (url.contains("http://edit_job_href/?")) {
				String jobId[] = url.split("=");
				// Intent intent1 = new Intent(WebViewActivity.this,
				// RecruitmentTreasureActivity.class);
				// intent1.putExtra("jobId", jobId[1]);
				// startActivity(intent1);
				startActivity(new Intent(ResumeNoHfive.this,
						PublishJobActivity.class).putExtra("isDoEdit", true)
						.putExtra("jobId", jobId[1]));
			} else if (url.contains("http://publish_job_href/")) {
				// Intent intent1 = new Intent(WebViewActivity.this,
				// RecruitmentTreasureActivity.class);
				// startActivity(intent1);
				startActivity(new Intent(ResumeNoHfive.this,
						PublishJobActivity.class));
			} else if (url.contains("http://complete_enterprise_info_href/?")) {
				String enterpriseId[] = url.split("=");
				String id = enterpriseId[1];
				if (!"".equals(id)) {
					if (Integer.valueOf(id) == AppContext.getInstance()
							.getUserInfo().getCompanyEntity().getId()) {
						Intent intent = new Intent(ResumeNoHfive.this,
								EditCompInfoActivity.class);
						startActivity(intent);
					}
				} else {
					ToastUtil.showShortToast("非法操作!");
				}

			} else if (url.contains("http://qy_submit_success_href/")) {
				// MkUser userDAO = new MkUser(WebViewActivity.this);
				// MkUser userD = AppContext.getInstance().getUserInfo();
				//
				// userD.setCompanyId(-5);
				// userDAO.updateLoginUser(userDAO.convertDataToEntity(userD));
				// ToastUtil.showShortToast("您的企业信息正在审核中");
				// Intent intent = new
				// Intent("com.redcos.mrrck.LOCAL_BROADCAST");
				// intent.putExtra("ACTION", "updateCompany");
				// LocalBroadcastManager.getInstance(WebViewActivity.this)
				// .sendBroadcast(intent);
				// finish();
			} else if (url.contains("http://enterprise_certification_page/")) {
				startActivity(new Intent(ResumeNoHfive.this,
						CompanyCertificationActivity.class));
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				webView.getContext().startActivity(intent);
			} else if (url.contains("goto://mrrck.com/?type=1000")) {// 编辑职位
				String jobId = url.substring(url.indexOf("jobId") + 6,
						url.lastIndexOf("&"));
				startActivity(new Intent(ResumeNoHfive.this,
						PublishJobActivity.class).putExtra("isDoEdit", true)
						.putExtra("jobId", Integer.parseInt(jobId)));
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
					Intent i = new Intent(ResumeNoHfive.this,
							PersonShowActivity.class);
					i.putExtra("toUserId", map.get("userId"));
					startActivity(i);
					break;
				case 2000:// 编辑
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						if (map.get("categoryId").equals("1")) {
							startActivityForResult(
									new Intent(ResumeNoHfive.this,
											CreatePersonEntryActivity.class)
											.putExtra("pageType", 1)
											.putExtra(
													"edit_baikeId",
													Tool.isEmpty(map
															.get("baikeId")) ? -1
															: Integer
																	.parseInt(map
																			.get("baikeId"))),
									reqCodeOne);
						} else if (map.get("categoryId").equals("2")) {
							startActivityForResult(
									new Intent(ResumeNoHfive.this,
											CreatCompanyEntryActivity.class)
											.putExtra("pageType", 1)
											.putExtra(
													"edit_baikeId",
													Tool.isEmpty(map
															.get("baikeId")) ? -1
															: Integer
																	.parseInt(map
																			.get("baikeId"))),
									reqCodeOne);
						} else if (map.get("categoryId").equals("3")) {
							startActivityForResult(
									new Intent(ResumeNoHfive.this,
											CreatCommonEntryActivity.class)
											.putExtra("pageType", 1)
											.putExtra(
													"edit_baikeId",
													Tool.isEmpty(map
															.get("baikeId")) ? -1
															: Integer
																	.parseInt(map
																			.get("baikeId"))),
									reqCodeOne);
						}
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(ResumeNoHfive.this);
					}

					break;
				case 2001:// 创建个人词条
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						startActivityForResult(new Intent(ResumeNoHfive.this,
								CreatePersonEntryActivity.class), reqCodeOne);

					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(ResumeNoHfive.this);
					}

					break;
				case 2002:// 创建名企词条
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						if ("0".equals(map.get("isExsitCompany"))) {
							startActivityForResult(new Intent(
									ResumeNoHfive.this,
									CompanyCertificationActivity.class),
									reqCodeOne);
						} else {
							startActivityForResult(new Intent(
									ResumeNoHfive.this,
									CreatCompanyEntryActivity.class),
									reqCodeOne);
						}
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(ResumeNoHfive.this);
					}

					// Log.e("wangke", map.get("isExsitCompany"));
					break;
				case 2003:// 创建公共词条
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						startActivityForResult(new Intent(ResumeNoHfive.this,
								CreatCommonEntryActivity.class), reqCodeOne);
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(ResumeNoHfive.this);
					}

					break;
				case 2004:// 我的
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						startActivityForResult(new Intent(ResumeNoHfive.this,
								MyEntryActivity.class), reqCodeOne);
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(ResumeNoHfive.this);
					}
					break;
				case 2005:// 进详情
					// if (map.get("categoryId").equals("2")) {
					// if (map.get("isExsitCompany").equals("0")) {
					// startActivityForResult(new Intent(
					// WebViewActivity.this,
					// CompanyCertificationActivity.class),
					// reqCodeTwo);
					// break;
					// }
					// }
					Log.e("wangke", url);
					Intent ii = new Intent(ResumeNoHfive.this,
							EntriesDetailActivity.class);
					ii.putExtra("userId", Tool.isEmpty(map.get("userId")) ? -1
							: Integer.parseInt(map.get("userId")));
					ii.putExtra(
							"baikeId",
							Tool.isEmpty(map.get("baikeId")) ? -1 : Integer
									.parseInt(map.get("baikeId")));
					ii.putExtra(
							"categoryId",
							Tool.isEmpty(map.get("categoryId")) ? -1 : Integer
									.parseInt(map.get("categoryId")));

					ii.putExtra("loadUrl", map.get("loadUrl"));
					ii.putExtra("shareUrl", map.get("shareUrl"));
					ii.putExtra("title", map.get("shareTitle"));
					ii.putExtra("content", map.get("shareContent"));
					ii.putExtra("image", map.get("shareImgUrl"));
					startActivityForResult(ii, reqCodeTwo);
					break;
				case 2006:// 案例详情
					startActivity(new Intent(ResumeNoHfive.this,
							CaseDetailActivity.class)
							.putExtra("shareTitle", map.get("shareTitle"))
							.putExtra("shareContent", map.get("shareContent"))
							.putExtra("shareImg", map.get("shareImg"))
							.putExtra("shareUrl", map.get("shareUrl"))
							.putExtra(
									"userId",
									Tool.isEmpty(map.get("userId")) ? -1
											: Integer.parseInt(map
													.get("userId")))
							.putExtra(
									"sourceId",
									Tool.isEmpty(map.get("id")) ? -1 : Integer
											.parseInt(map.get("id")))
							.putExtra("loadUrl", map.get("loadUrl")));
					break;
				case 2007:// 公司信息品论
					startActivity(new Intent(ResumeNoHfive.this,
							CaseCommentActivity.class).putExtra("companyId",
							map.get("companyId")));
					break;
				case 2008:// 申请设计
					startActivity(new Intent(ResumeNoHfive.this,
							ApplicationDesignActivity.class).putExtra(
							"companyId", map.get("companyId")));
					break;
				case 2009:// 新增城市会员
					Intent addIntent = new Intent(ResumeNoHfive.this,
							BuyAddRecruitCityActivity.class);
					startActivityForResult(addIntent, reqCodeOne);
					break;
				case 2010: // 开通会员
					Intent intentvip = new Intent(ResumeNoHfive.this,
							OpenZhaopinbaoPersonActivity.class);
					intentvip.putExtra("flag", 1);
					startActivityForResult(intentvip, reqCodeOne);
					break;
				case 2011:// 续费会员
					Intent intentvip1 = new Intent(ResumeNoHfive.this,
							OpenZhaopinbaoPersonActivity.class);
					intentvip1.putExtra("flag", 0);
					startActivityForResult(intentvip1, reqCodeOne);
					break;
				case 2012:// 一个按钮 breakType='0';//0 原生 1h5 2关闭
					final String breaktype = map.get("breakType");
					final String functionUrl = map.get("functionUrl");// 原生 1代表
																		// 去创建简历
																		// 2
																		// 代表续费招聘宝会员
																		// 3
																		// 开通招聘宝会员
																		// 4开通招聘宝新增城市权限
					final HintDialogwk dialogwk = new HintDialogwk(
							ResumeNoHfive.this, map.get("msg"),
							map.get("buttonName"));
					dialogwk.show();
					dialogwk.setOneClicklistener(new DoOneClickListenerInterface() {

						@Override
						public void doOne() {
							if ("0".equals(breaktype)) {
								if ("1".equals(functionUrl)) {
									startActivityForResult(new Intent(
											ResumeNoHfive.this,
											MrrckResumeActivity.class),
											reqCodeOne);
									dialogwk.dismiss();
								} else if ("2".equals(functionUrl)) {
									Intent kIntent = new Intent(
											ResumeNoHfive.this,
											OpenZhaopinbaoPersonActivity.class);
									kIntent.putExtra("flag", 0);
									startActivityForResult(kIntent, reqCodeOne);
									dialogwk.dismiss();
								} else if ("3".equals(functionUrl)) {
									Intent huiyuanIntent = new Intent(
											ResumeNoHfive.this,
											OpenZhaopinbaoPersonActivity.class);
									huiyuanIntent.putExtra("flag", 1);
									startActivityForResult(huiyuanIntent,
											reqCodeOne);
									dialogwk.dismiss();
								} else if ("4".equals(functionUrl)) {
									Intent addIntent = new Intent(
											ResumeNoHfive.this,
											BuyAddRecruitCityActivity.class);
									addIntent.putExtra("flag", 1);
									startActivityForResult(addIntent,
											reqCodeOne);
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
				case 2014:
					final String breaktype1 = map.get("breakType");// 0 原生 1h5//
																	// 2关闭 3//
																	// 执行js代码
					final String functionUrl1 = map.get("functionUrl");// 方法名称
					final HintDialogwk dialogwk2 = new HintDialogwk(
							ResumeNoHfive.this, map.get("msg"), "确认", "取消");
					dialogwk2.show();
					dialogwk2.setClicklistener(new ClickListenerInterface() {

						@Override
						public void doConfirm() {
							if ("3".equals(breaktype1)) {
								webView.loadUrl("javascript:" + functionUrl1);
								dialogwk2.dismiss();
							}

						}

						@Override
						public void doCancel() {
							dialogwk2.dismiss();
						}
					});
					break;
				}
			} else {
				webView.loadUrl(url);
			}
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			if (webView != null) {
				if (webView.canGoBack() && notOnceBack) {
					webView.goBack();
				} else {
					finish();
				}
			} else {
				finish();
			}

			break;
		case R.id.right_res_title:
			list.clear();
			list.add(new PopupData("分享", R.drawable.show_fengxiang));
			myPopupwindow = new MyPopupwindow(this, list, new popListener() {

				@Override
				public void doChoose(int position) {
					switch (position) {
					case 0:
						if (!Tool.isEmpty(shareUrl)) {
							new InviteFriendDialog(ResumeNoHfive.this,
									shareUrl, shareTitle, shareContent,
									shareImg, resumeId + "",
									ConstantKey.ShareStatus_CRESUMEDETAIL)
									.show();

							myPopupwindow.dismiss();

						} else {
							ToastUtil.showShortToast("分享网址有误！");
						}

						break;
					default:
						break;
					}
				}
			});
			myPopupwindow.show(v);
			break;
		case R.id.reloadBTN: // 重新加载
			loadFailRL.setVisibility(View.GONE);
			webView.loadUrl(webUrl);
			break;

		default:
			break;
		}
	}

	// 查看网页图片
	private void toViewImage(String url) {
		// TODO hlhlhl
		String images = url.substring(url.indexOf('=') + 1, url.indexOf('&'));
		String[] imageUrls = images.split(",");

		Intent intent = new Intent();
		intent.setClass(ResumeNoHfive.this, ImagePagerActivity.class);
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

	// 提示登录对话框
	private void showTipToLoginDialog() {
		final HintDialogwk commonDialog = new HintDialogwk(this,
				"您还没有登录，请登录后操作", "去登录", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new HintDialogwk.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						Intent intent = new Intent(ResumeNoHfive.this,
								MkLoginActivity.class);
						startActivity(intent);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
			ToastUtil.showShortToast(datas);
		}
	}

	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
	private static final int REQ_CHOOSE = REQ_CAMERA + 1;

	// String compressPath = "";

	String imagePaths;
	Uri cameraUri;

	/**
	 * 选择照片后结束
	 * 
	 * @param data
	 */
	private Uri afterChosePic(Intent data) {

		// 获取图片的路径：
		String[] proj = { MediaStore.Images.Media.DATA };
		if (data == null) {
			return null;
		}
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
		if (cursor == null) {
			ToastUtil.showShortToast("请选择相册png或jpg图片上传");
			return null;
		}
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		String fileName = UUID.randomUUID().toString() + ".png";
		String compressPath = FileConstant.CacheFilePath + fileName;
		File vFile = new File(compressPath);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		}
		if (path != null
				&& (path.endsWith(".png") || path.endsWith(".PNG")
						|| path.endsWith(".jpg") || path.endsWith(".JPG"))) {
			File newFile = FileHelper.compressFile(path, compressPath);
			return Uri.fromFile(newFile);
		} else {
			ToastUtil.showShortToast("上传的图片仅支持png或jpg格式");
		}
		return null;
	}

	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public final boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			ToastUtil.showShortToast("请插入手机存储卡再使用本功能");
		}
		return flag;
	}

	/**
	 * 返回文件选择
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Log.v(TAG, "onActivityResult");
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			// Uri result = intent == null || resultCode != RESULT_OK ? null
			// : intent.getData();
			Uri result = afterChosePic(intent);
			if (result != null) {
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
			}

		}
		if (resultCode == RESULT_OK) {
			if (requestCode == reqCodeOne) {
				webView.loadUrl(webUrl);
			}
			if (requestCode == reqCodeTwo) {
				webView.loadUrl(webUrl);
			}
		}

		// if (null == mUploadMessage)
		// return;
		// Uri uri = null;
		// if(requestCode == REQ_CAMERA ){
		// afterOpenCamera();
		// uri = cameraUri;
		// }else if(requestCode == REQ_CHOOSE){
		// uri = afterChosePic(intent);
		// }
		// mUploadMessage.onReceiveValue(uri);
		// mUploadMessage = null;
		// super.onActivityResult(requestCode, resultCode, intent);
	}

	// protected final void selectImage() {
	// if (!checkSDcard())
	// return;
	// String[] selectPicTypeStr = { "拍照获取照片","从相册中获取照片" };
	// new AlertDialog.Builder(this)
	// .setItems(selectPicTypeStr,
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// switch (which) {
	// // 相机拍摄
	// case 0:
	// openCarcme();
	// break;
	// // 手机相册
	// case 1:
	// chosePic();
	// break;
	// default:
	// break;
	// }
	// compressPath = Environment
	// .getExternalStorageDirectory()
	// .getPath()
	// + "/fuiou_wmp/temp";
	// new File(compressPath).mkdirs();
	// compressPath = compressPath + File.separator
	// + "compress.jpg";
	// }
	// }).show();
	// }

	/**
	 * 设置网页标题
	 */
	private void setWebTitle(String webtitle) {
		if (titleView != null) {
			if (TextUtils.isEmpty(webtitle)) {
				webtitle = "";
			}
			titleView.setText(webtitle);
		}

	}

	/**
	 * 打开照相机
	 */
	private void openCarcme() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/" + (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQ_CAMERA);
	}

	/**
	 * 拍照结束后
	 */
	// private void afterOpenCamera() {
	// File f = new File(imagePaths);
	// addImageGallery(f);
	// File newFile = FileUtils2.compressFile(f.getPath(), compressPath);
	// }

	/**
	 * 解决拍照后在相册中找不到的问题
	 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	private void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resumeId", resumeId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_80062));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.RESUME_REQUEST_MAPPING, req);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_resumenohfive;
	}

	@Override
	public void initValue() {
		notOnceBack = getIntent().getBooleanExtra("notOnceBack", true);
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("363636", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			try {
				shareImg = resp.getBody().get("shareImg").getAsString();
				shareTitle = resp.getBody().get("shareTitle").getAsString();
				shareContent = resp.getBody().get("shareContent").getAsString();
				shareUrl = resp.getBody().get("shareUrl").getAsString();
			} catch (Exception e) {
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
			ReqBase resp = (ReqBase) arg0;
			final HintDialogwk commonDialog = new HintDialogwk(
					ResumeNoHfive.this, resp.getHeader().getRetMessage(), "确定",
					"取消");
			commonDialog.setClicklistener(new ClickListenerInterface() {

				@Override
				public void doConfirm() {
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

	// /**
	// * 本地相册选择图片
	// */
	// private void chosePic() {
	// FileUtils2.delFile(compressPath);
	// Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //
	// "android.intent.action.GET_CONTENT"
	// String IMAGE_UNSPECIFIED = "image/*";
	// innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
	// Intent wrapperIntent = Intent.createChooser(innerIntent, null);
	// startActivityForResult(wrapperIntent, REQ_CHOOSE);
	// }
}
