package com.meiku.dev.ui.mine;

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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.MkDataConfigReleaseMonths;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UserAttachmentEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.community.EditPostActivity;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.community.ReplyActivity;
import com.meiku.dev.ui.community.ReportActivity;
import com.meiku.dev.ui.decoration.ApplicationDesignActivity;
import com.meiku.dev.ui.decoration.CaseCommentActivity;
import com.meiku.dev.ui.decoration.CaseDetailActivity;
import com.meiku.dev.ui.decoration.DecCompanyDetailActivity;
import com.meiku.dev.ui.encyclopaedia.CreatCommonEntryActivity;
import com.meiku.dev.ui.encyclopaedia.CreatCompanyEntryActivity;
import com.meiku.dev.ui.encyclopaedia.CreatePersonEntryActivity;
import com.meiku.dev.ui.encyclopaedia.EntriesDetailActivity;
import com.meiku.dev.ui.morefun.MrrckResumeActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.ui.morefun.TestWebChromeClient;
import com.meiku.dev.ui.recruit.BuyAddRecruitCityActivity;
import com.meiku.dev.ui.recruit.OpenZhaopinbaoPersonActivity;
import com.meiku.dev.ui.recruit.PublishJobActivity;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

public class CompanyInformationActivity extends BaseActivity implements
		OnClickListener {
	private TextView right_txt_title;
	private CommonAdapter<UserAttachmentEntity> commonAdapterphoto;
	private CompanyEntity companyEntity;
	private TextView tv_companyname, tv_guimo, tv_address, tv_bossType,
			tv_bossType1;
	private ImageView img_yanzheng, img_shiping;
	private String authPass;
	private RelativeLayout morelayout;
	private ImageView arrow;
	private MyGridView GridView1;
	private List<UserAttachmentEntity> list = new ArrayList<UserAttachmentEntity>();
	private LinearLayout lin_companyphoto, lin_biaoqian;
	private FrameLayout fra_shiping;
	private List<AttachmentListDTO> imageDates = new ArrayList<AttachmentListDTO>();
	private String companyIntroduceStr;
	private boolean showAllIntro = false;
	private LinearLayout layout_companyintroduce;
	private ImageView img_logo;
	private ScrollView scroll;
	private String loadCompanyUrl;
	private static final int reqcompany = 10;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_company_information;
	}

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
		init();
		scroll = (ScrollView) findViewById(R.id.scroll);
		tv_bossType1 = (TextView) findViewById(R.id.tv_bossType1);
		lin_biaoqian = (LinearLayout) findViewById(R.id.lin_biaoqian);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(null);
		tv_companyname = (TextView) findViewById(R.id.tv_companyname);
		img_yanzheng = (ImageView) findViewById(R.id.img_yanzheng);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		tv_guimo = (TextView) findViewById(R.id.tv_guimo);
		tv_address = (TextView) findViewById(R.id.tv_address);
		morelayout = (RelativeLayout) findViewById(R.id.morelayout);
		arrow = (ImageView) findViewById(R.id.arrow);
		morelayout.setVisibility(View.VISIBLE);
		morelayout.setOnClickListener(this);
		img_shiping = (ImageView) findViewById(R.id.img_shiping);
		layout_companyintroduce = (LinearLayout) findViewById(R.id.layout_companyintroduce);
		GridView1 = (MyGridView) findViewById(R.id.GridView1);
		lin_companyphoto = (LinearLayout) findViewById(R.id.lin_companyphoto);
		tv_bossType = (TextView) findViewById(R.id.tv_bossType);
		fra_shiping = (FrameLayout) findViewById(R.id.fra_shiping);
	}

	@Override
	public void initValue() {
		List<MkDataConfigReleaseMonths> data = MKDataBase.getInstance()
				.getReleaseMonthsList(3);
		if (null != data && data.size() > 0) {
			loadCompanyUrl = data.get(0).getMonthsName();
		}
		if (Tool.isEmpty(loadCompanyUrl)) {
			scroll.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);
			GetData();
		} else {
			scroll.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
			webView.loadUrl(loadCompanyUrl
					+ AppContext.getInstance().getUserInfo().getCompanyEntity()
							.getId());
		}
	}

	@Override
	public void bindListener() {
		right_txt_title.setOnClickListener(this);
		img_shiping.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("121212", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("company").toString().length() > 2) {
				companyEntity = (CompanyEntity) JsonUtil.jsonToObj(
						CompanyEntity.class, resp.getBody().get("company")
								.toString());
				xuanran();
			} else {
				ToastUtil.showShortToast("无公司信息数据");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right_txt_title:
			if (!NetworkTools
					.isNetworkAvailable(CompanyInformationActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			startActivityForResult(new Intent(CompanyInformationActivity.this,
					EditCompInfoActivity.class), reqcompany);
			break;
		case R.id.img_shiping:
			startActivity(new Intent(CompanyInformationActivity.this,
					TestVideoActivity.class).putExtra("mrrck_videoPath",
					companyEntity.getClientVideo()));
			break;
		// case R.id.img_companyphoto:
		// Intent intentImage = new Intent();
		// intentImage.setClass(CompanyInformationActivity.this,
		// ImagePagerActivity.class);
		// intentImage.putExtra("showOnePic",
		// companyEntity.getClientCompanyLogo());
		// startActivity(intentImage);
		// break;
		case R.id.morelayout:
			showAllIntro = !showAllIntro;
			showAllInfo(showAllIntro);
			break;
		case R.id.goback:
			if (webView != null && !Tool.isEmpty(loadCompanyUrl)) {
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqcompany:
			case reqCodeOne:
			case reqCodeTwo:
				if (Tool.isEmpty(loadCompanyUrl)) {
					GetData();
				} else {
					webView.loadUrl(loadCompanyUrl
							+ AppContext.getInstance().getUserInfo()
									.getCompanyEntity().getId());
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 是否显示公司介绍
	 * 
	 * @param showall
	 */
	public void showAllInfo(boolean showall) {
		arrow.setBackgroundResource(showall ? R.drawable.shangla
				: R.drawable.xiala);

		if (Tool.isEmpty(companyIntroduceStr)) {
			return;
		}
		layout_companyintroduce.removeAllViews();
		TextView tv = new TextView(CompanyInformationActivity.this);
		tv.setText(companyIntroduceStr);
		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		tv.measure(spec, spec);
		int height = tv.getMeasuredHeight();
		if (height > 300) {
			if (showall) {
				layout_companyintroduce.addView(tv);
			} else {
				layout_companyintroduce.addView(tv,
						new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, 300));
			}
		} else {
			layout_companyintroduce.addView(tv);
		}
	}

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_COMPLANYINFO));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
	}

	public void xuanran() {
		tv_companyname.setText(companyEntity.getName());
		authPass = companyEntity.getAuthPass();
		if ("0".equals(authPass)) {
			img_yanzheng.setBackgroundResource(R.drawable.company_shenhezhon);
		} else if ("1".equals(authPass)) {
			img_yanzheng.setBackgroundResource(R.drawable.company_yiyanzheng);
		} else {
			img_yanzheng.setBackgroundResource(R.drawable.company_weiyanzheng);
		}
		tv_guimo.setText(companyEntity.getScaleName());
		tv_address.setText(companyEntity.getAddress());
		companyIntroduceStr = companyEntity.getIntroduce() + " ";
		showAllInfo(showAllIntro);
		BitmapUtils bitmapUtils = new BitmapUtils(
				CompanyInformationActivity.this);
		bitmapUtils
				.display(img_logo, companyEntity.getClientThumbCompanyLogo());
		if (companyEntity.getClientVideoPhoto().length() > 2) {
			fra_shiping.setVisibility(View.VISIBLE);
			BitmapUtils bitmapUtils1 = new BitmapUtils(
					CompanyInformationActivity.this);
			bitmapUtils1.display(img_shiping,
					companyEntity.getClientVideoPhoto());
		} else {
			fra_shiping.setVisibility(View.GONE);
		}
		list = companyEntity.getAttachmentList();
		if (list.size() > 0) {
			imageDates.clear();
			for (int i = 0, size = list.size(); i < size; i++) {
				AttachmentListDTO attachmentListDTO = new AttachmentListDTO();
				attachmentListDTO.setClientFileUrl(list.get(i)
						.getClientFileUrl());
				imageDates.add(attachmentListDTO);
			}
			lin_companyphoto.setVisibility(View.VISIBLE);
			commonAdapterphoto = new CommonAdapter<UserAttachmentEntity>(
					CompanyInformationActivity.this, R.layout.item_personshow,
					list) {

				@Override
				public void convert(final ViewHolder viewHolder,
						UserAttachmentEntity t) {
					viewHolder.setImage(R.id.image, t.getClientFileUrl()
							+ "_thumb.png");
					viewHolder.getConvertView().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent intentImage1 = new Intent();
									intentImage1.setClass(
											CompanyInformationActivity.this,
											ImagePagerActivity.class);
									intentImage1
											.putParcelableArrayListExtra(
													"imageDates",
													(ArrayList<? extends Parcelable>) imageDates);
									intentImage1.putExtra("index",
											viewHolder.getPosition());
									startActivity(intentImage1);
								}
							});
				}
			};

			GridView1.setAdapter(commonAdapterphoto);

		} else {
			lin_companyphoto.setVisibility(View.GONE);
		}
		if (companyEntity.getBossTypeName().toString().length() > 2) {
			lin_biaoqian.setVisibility(View.VISIBLE);
			if (companyEntity.getBossTypeName().toString().contains(",")) {
				tv_bossType.setVisibility(View.VISIBLE);
				tv_bossType1.setVisibility(View.VISIBLE);
				tv_bossType.setText(companyEntity.getBossTypeName().toString()
						.split(",")[0]);
				tv_bossType1.setText(companyEntity.getBossTypeName().toString()
						.split(",")[1]);
			} else {
				tv_bossType1.setVisibility(View.INVISIBLE);
				tv_bossType.setText(companyEntity.getBossTypeName());
			}
		} else {
			lin_biaoqian.setVisibility(View.GONE);
		}

	}

	private WebView webView;
	private WebSettings settings;

	private void init() {
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
		webView.setWebViewClient(new MyWebViewClient(
				CompanyInformationActivity.this));
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
		CookieSyncManager.createInstance(CompanyInformationActivity.this);
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

		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("hl", "shouldOverrideUrlLoadingURL地址:" + url);
			if (url.contains("image_href")) {
				toViewImage(url);
			} else if (url.contains("http://edit_person_resume/")) { // 修改个人简历
				// if (AppContext.getInstance().isHasLogined()) {
				// Intent intent2 = new Intent(CompanyInformationActivity.this,
				// MrrckResumeActivity.class);
				// startActivity(intent2);
				// } else {
				// showTipToLoginDialog();
				// }

				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(CompanyInformationActivity.this);
				} else {
					Intent intent2 = new Intent(
							CompanyInformationActivity.this,
							MrrckResumeActivity.class);
					startActivity(intent2);
				}
			} else if (url.contains("http://edit_job_href/?")) {
				String jobId[] = url.split("=");
				// Intent intent1 = new Intent(CompanyInformationActivity.this,
				// RecruitmentTreasureActivity.class);
				// intent1.putExtra("jobId", jobId[1]);
				// startActivity(intent1);
				startActivity(new Intent(CompanyInformationActivity.this,
						PublishJobActivity.class).putExtra("isDoEdit", true)
						.putExtra("jobId", jobId[1]));
			} else if (url.contains("http://publish_job_href/")) {
				// Intent intent1 = new Intent(CompanyInformationActivity.this,
				// RecruitmentTreasureActivity.class);
				// startActivity(intent1);
				startActivity(new Intent(CompanyInformationActivity.this,
						PublishJobActivity.class));
			} else if (url.contains("http://complete_enterprise_info_href/?")) {
				String enterpriseId[] = url.split("=");
				String id = enterpriseId[1];
				if (!"".equals(id)) {
					if (Integer.valueOf(id) == AppContext.getInstance()
							.getUserInfo().getCompanyEntity().getId()) {
						Intent intent = new Intent(
								CompanyInformationActivity.this,
								EditCompInfoActivity.class);
						startActivity(intent);
					}
				} else {
					ToastUtil.showShortToast("非法操作!");
				}

			} else if (url.contains("http://qy_submit_success_href/")) {
				// MkUser userDAO = new MkUser(CompanyInformationActivity.this);
				// MkUser userD = AppContext.getInstance().getUserInfo();
				//
				// userD.setCompanyId(-5);
				// userDAO.updateLoginUser(userDAO.convertDataToEntity(userD));
				// ToastUtil.showShortToast("您的企业信息正在审核中");
				// Intent intent = new
				// Intent("com.redcos.mrrck.LOCAL_BROADCAST");
				// intent.putExtra("ACTION", "updateCompany");
				// LocalBroadcastManager.getInstance(CompanyInformationActivity.this)
				// .sendBroadcast(intent);
				// finish();
			} else if (url.contains("http://enterprise_certification_page/")) {
				startActivity(new Intent(CompanyInformationActivity.this,
						CompanyCertificationActivity.class));
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				webView.getContext().startActivity(intent);
			} else if (url.contains("goto://mrrck.com/?type=1000")) {// 编辑职位
				String jobId = url.substring(url.indexOf("jobId") + 6,
						url.lastIndexOf("&"));
				startActivity(new Intent(CompanyInformationActivity.this,
						PublishJobActivity.class).putExtra("isDoEdit", true)
						.putExtra("jobId", Integer.parseInt(jobId)));
			} else if (url.contains("goto://mrrck.com/?type=1001")) {// 企业编辑（自己企业）
				Intent intent = new Intent(CompanyInformationActivity.this,
						EditCompInfoActivity.class);
				startActivity(intent);
			} else if (url.contains("goto://mrrck.com/?type=1002")) { // 企业发布职位
				startActivity(new Intent(CompanyInformationActivity.this,
						PublishJobActivity.class));
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
					Intent i = new Intent(CompanyInformationActivity.this,
							PersonShowActivity.class);
					i.putExtra("toUserId", map.get("userId"));
					startActivity(i);
					break;
				case 1004:
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						String nickname = map.get("nickName");
						String content = map.get("content");
						String sourceId = map.get("sourceId");
						String sourceType = map.get("sourceType");
						Intent intent = new Intent(CompanyInformationActivity.this,
								ReportActivity.class);
						intent.putExtra("content", content);
						intent.putExtra("nickname", nickname);
						intent.putExtra("sourceType", sourceType);// sourceType帖子是1,评论是2
						intent.putExtra("sourceId", sourceId);
						startActivity(intent);
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInformationActivity.this);
					}
					break;
				case 2000:// 编辑
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						if (map.get("categoryId").equals("1")) {
							startActivityForResult(
									new Intent(CompanyInformationActivity.this,
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
									new Intent(CompanyInformationActivity.this,
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
									new Intent(CompanyInformationActivity.this,
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
								.showTipToLoginDialog(CompanyInformationActivity.this);
					}

					break;
				case 2001:// 创建个人词条
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						startActivityForResult(new Intent(
								CompanyInformationActivity.this,
								CreatePersonEntryActivity.class), reqCodeOne);

					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInformationActivity.this);
					}

					break;
				case 2002:// 创建名企词条
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						if ("0".equals(map.get("isExsitCompany"))) {
							startActivityForResult(new Intent(
									CompanyInformationActivity.this,
									CompanyCertificationActivity.class),
									reqCodeOne);
						} else {
							startActivityForResult(new Intent(
									CompanyInformationActivity.this,
									CreatCompanyEntryActivity.class),
									reqCodeOne);
						}
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInformationActivity.this);
					}

					// Log.e("wangke", map.get("isExsitCompany"));
					break;
				case 2003:// 创建公共词条
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						startActivityForResult(new Intent(
								CompanyInformationActivity.this,
								CreatCommonEntryActivity.class), reqCodeOne);
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInformationActivity.this);
					}

					break;
				case 2004:// 我的
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						startActivityForResult(new Intent(
								CompanyInformationActivity.this,
								MyEntryActivity.class), reqCodeOne);
					} else {
						ShowLoginDialogUtil
								.showTipToLoginDialog(CompanyInformationActivity.this);
					}
					break;
				case 2005:// 进详情
					// if (map.get("categoryId").equals("2")) {
					// if (map.get("isExsitCompany").equals("0")) {
					// startActivityForResult(new Intent(
					// CompanyInformationActivity.this,
					// CompanyCertificationActivity.class),
					// reqCodeTwo);
					// break;
					// }
					// }
					Log.e("wangke", url);
					Intent ii = new Intent(CompanyInformationActivity.this,
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
					startActivity(new Intent(CompanyInformationActivity.this,
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
					startActivity(new Intent(CompanyInformationActivity.this,
							CaseCommentActivity.class).putExtra("companyId",
							map.get("companyId")));
					break;
				case 2008:// 申请设计
					startActivity(new Intent(CompanyInformationActivity.this,
							ApplicationDesignActivity.class).putExtra(
							"companyId", map.get("companyId")));
					break;
				case 2009:// 新增城市会员
					Intent addIntent = new Intent(
							CompanyInformationActivity.this,
							BuyAddRecruitCityActivity.class);
					startActivityForResult(addIntent, reqCodeOne);
					break;
				case 2010: // 开通会员
					Intent intentvip = new Intent(
							CompanyInformationActivity.this,
							OpenZhaopinbaoPersonActivity.class);
					intentvip.putExtra("flag", 1);
					startActivityForResult(intentvip, reqCodeOne);
					break;
				case 2011:// 续费会员
					Intent intentvip1 = new Intent(
							CompanyInformationActivity.this,
							OpenZhaopinbaoPersonActivity.class);
					intentvip1.putExtra("flag", 0);
					startActivityForResult(intentvip1, reqCodeOne);
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
		intent.setClass(CompanyInformationActivity.this,
				ImagePagerActivity.class);
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
