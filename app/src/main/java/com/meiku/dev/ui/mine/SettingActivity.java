package com.meiku.dev.ui.mine;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.meiku.dev.AboutMrrckActivity;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.GuidePageActivity;
import com.meiku.dev.ui.im.DefriendListActivity;
import com.meiku.dev.ui.login.BindPhoneActivity;
import com.meiku.dev.ui.login.ChangePassActivity;
import com.meiku.dev.ui.login.ChangePhoneActivity;
import com.meiku.dev.ui.plan.PlotDetailActivity;
import com.meiku.dev.ui.plan.PlotterDetailActivity;
import com.meiku.dev.utils.DataCleanManager;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.UpdateAppManager;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonEditDialog;
import com.meiku.dev.views.CommonEditDialog.EditClickOkListener;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.LoadingDialog;
import com.meiku.dev.yunxin.recent.DropManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private ImageView back;
	private View contactService;
	private View feedBack;
	private View cleanCacheLay;
	private View checkUpdateLL;
	private View splash;
	private View defriend;

	private LoadingDialog loadingDialog;// 加载框
	private ToggleButton tb_opened;
	private LinearLayout lin_updatepassword, lin_aboutmrrck, lin_backlogin,
			lin_sharemrrck;
	private final String meiku_alter = "个人名片";
	private TextView tv_recommendCode;
	private TextView tv_phone;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_setting;
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
		lin_sharemrrck = (LinearLayout) findViewById(R.id.lin_sharemrrck);
		lin_updatepassword = (LinearLayout) findViewById(R.id.lin_updatepassword);
		lin_aboutmrrck = (LinearLayout) findViewById(R.id.lin_aboutmrrck);
		lin_backlogin = (LinearLayout) findViewById(R.id.lin_backlogin);
		back = (ImageView) findViewById(R.id.left_res_title);
		back.setOnClickListener(this);

		contactService = findViewById(R.id.contactService_lay);
		contactService.setOnClickListener(this);

		feedBack = findViewById(R.id.feedback_lay);
		feedBack.setOnClickListener(this);

		cleanCacheLay = findViewById(R.id.clean_cache_lay);
		cleanCacheLay.setOnClickListener(this);

		checkUpdateLL = findViewById(R.id.checkUpdateLL);
		checkUpdateLL.setOnClickListener(this);

		splash = findViewById(R.id.splash_lay);
		splash.setOnClickListener(this);

		defriend = findViewById(R.id.defriend);
		defriend.setOnClickListener(this);

		tb_opened = (ToggleButton) findViewById(R.id.tb_opened);
		Boolean isOpenVibrate = (Boolean) PreferHelper.getSharedParam(
				ConstantKey.SETTING_VIBRATE, true);
		tb_opened.setChecked(!isOpenVibrate);// 接收消息，既取消 消息免打扰
		NIMClient.toggleNotification(isOpenVibrate);
		tb_opened.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				PreferHelper.setSharedParam(ConstantKey.SETTING_VIBRATE, !arg1);
				LogUtil.d("hl", "istoggleNotification=" + !arg1);
				NIMClient.toggleNotification(!arg1);
			}
		});
		tv_recommendCode = (TextView) findViewById(R.id.tv_recommendCode);
		tv_recommendCode.setText(AppContext.getInstance().getUserInfo()
				.getRecommendCode());
		findViewById(R.id.lin_passwordGroup)
				.setVisibility(
						Tool.isEmpty(AppContext.getInstance().getUserInfo()
								.getPhone()) ? View.GONE : View.VISIBLE);

		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
		findViewById(R.id.layout_verifPhone).setOnClickListener(this);
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		lin_updatepassword.setOnClickListener(this);
		lin_aboutmrrck.setOnClickListener(this);
		lin_backlogin.setOnClickListener(this);
		lin_sharemrrck.setOnClickListener(this);
		findViewById(R.id.lin_recommendCode).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().toString().length() > 2) {
				Map<String, String> map = JsonUtil.jsonToMap(resp.getBody()
						.toString());
				new InviteFriendDialog(
						SettingActivity.this,
						map.get("meikuUrl"),
						map.get("shareTitle"),
						map.get("shareContent"),
						map.get("shareImgUrl"),
						AppContext.getInstance().getUserInfo().getUserId() + "",
						ConstantKey.ShareStatus_MRRCK).show();
			} else {
				ToastUtil.showShortToast("获取参数失败");
			}
			break;
		case reqCodeFour:
			if (!Tool.isEmpty(resp.getBody().get("data"))) {
				AppContext
						.getInstance()
						.getUserInfo()
						.setRecommendCode(
								resp.getBody().get("data").getAsString());
				AppContext.getInstance().setLocalUser(
						AppContext.getInstance().getUserInfo());
				tv_recommendCode.setText(resp.getBody().get("data")
						.getAsString());
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

	}

	private void checkAppUpdate() {

		UpdateAppManager manager = new UpdateAppManager(this);

		manager.setOnUpdateResultListener(new UpdateAppManager.OnUpdateResultListener() {
			@Override
			public void onNotUpdate() {
				ToastUtil.showShortToast("您已经是最新版本");
			}

			@Override
			public void onCancleClick() {
				// TODO Auto-generated method stub

			}
		});

		manager.checkUpdateInfo();

	}

	/**
	 * 清除缓存
	 */
	private void cleanCache() {
		try {
			DataCleanManager.cleanCustomCache(FileConstant.CacheFilePath);
			DataCleanManager.cleanCustomCache("/sdcard"
					+ FileConstant.MATCHAD_PATH);
			DataCleanManager.cleanCustomCache("/sdcard"
					+ FileConstant.UPLOAD_VIDEO_PATH);
			DataCleanManager.cleanCustomCache("/sdcard"
					+ FileConstant.UPLOAD_PHOTO_PATH);
			DataCleanManager.cleanCustomCache("/sdcard"
					+ FileConstant.UPLOAD_AUDIO_PATH);
			ImagePipeline imagePipeline = Fresco.getImagePipeline();
			imagePipeline.clearCaches();
			if (loadingDialog != null) {
				loadingDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_res_title:
			finish();
			break;
		case R.id.contactService_lay:
			final CommonDialog commonDialog = new CommonDialog(this, "拨打电话",
					"400-688-6800", "拨打", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:4006886800"));
							startActivity(intent);
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
			break;
		case R.id.feedback_lay:
			Intent intent = new Intent(this, FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.checkUpdateLL: // 检查更新
			if (!NetworkTools.isNetworkAvailable(SettingActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			checkAppUpdate();
			break;
		case R.id.clean_cache_lay:
			loadingDialog = new LoadingDialog(this, "正在清除");
			loadingDialog.show();
			cleanCache();
			Toast toast = Toast.makeText(this, "清理完成", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			LinearLayout toastView = (LinearLayout) toast.getView();
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setImageResource(R.drawable.checkbos_bg_on);
			toastView.addView(imageView, 0);
			toast.show();
			break;
		case R.id.splash_lay:
			startActivity(new Intent(this, GuidePageActivity.class));
			break;
		case R.id.defriend:
			 Intent defriendIntent = new Intent(this,
			 DefriendListActivity.class);
			 startActivity(defriendIntent);
			break;
		case R.id.lin_updatepassword:
			if (!Tool
					.isEmpty(AppContext.getInstance().getUserInfo().getPhone())) {
				startActivity(new Intent(SettingActivity.this,
						ChangePassActivity.class));
			} else {
				// ToastUtil.showShortToast("请先设置手机号！");
			}
			break;
		case R.id.lin_aboutmrrck:
			startActivity(new Intent(SettingActivity.this,
					AboutMrrckActivity.class));
			break;
		case R.id.lin_backlogin:
			dlgLogout();
			break;
		case R.id.lin_sharemrrck:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(SettingActivity.this);
				return;
			}
			// 数据请求
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			req.setHeader(new ReqHead(AppConfig.BUSINESS_SHARE_MEIKU));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeOne, AppConfig.PERSONAL_REQUEST_MAPPING, req);
			break;
		case R.id.lin_recommendCode:
			if (Tool.isEmpty(AppContext.getInstance().getUserInfo()
					.getRecommendCode())) {
				new CommonEditDialog(SettingActivity.this, "推荐码", "请填写推荐码", "",
						100, true, new EditClickOkListener() {

							@Override
							public void doConfirm(String inputString) {
								ReqBase reqBase = new ReqBase();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("userId", AppContext.getInstance()
										.getUserInfo().getId());
								map.put("recommendCode", inputString);
								reqBase.setHeader(new ReqHead(
										AppConfig.BUSINESS_US_20073));
								reqBase.setBody(JsonUtil.String2Object(JsonUtil
										.hashMapToJson(map)));
								httpPost(reqCodeFour,
										AppConfig.USER_REQUEST_MAPPING,
										reqBase, false);
							}
						}).show();
			} else {
				ToastUtil.showShortToast("推荐码不可更改");
			}
			break;
		case R.id.layout_verifPhone:
			if (Tool.isEmpty(AppContext.getInstance().getUserInfo().getPhone())) {
				startActivityForResult(new Intent(SettingActivity.this,
						BindPhoneActivity.class), reqCodeOne);// 绑定手机
			} else { // 更换手机
				startActivityForResult(new Intent(SettingActivity.this,
						ChangePhoneActivity.class), reqCodeOne);
			}
			break;
		default:
			break;
		}
	}

	// 退出确认
	private void dlgLogout() {
		final CommonDialog commonDialog = new CommonDialog(
				SettingActivity.this, "提示", "您确定要退出登录吗？", "确定", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						doLogout();
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

	/**
	 * 调服务器退出接口---清空MKuser---发登出广播（关主页面）---启动登陆页面---关闭本页面
	 */
	protected void doLogout() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_LOGOUT));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.USER_REQUEST_MAPPING, reqBase, false);

		NIMClient.getService(AuthService.class).logout();
		AppContext.getInstance().setLocalUserEmpty();
		DropManager.getInstance().destroy();
		sendBroadcast(new Intent(BroadCastAction.ACTION_MAIN_LOGOUT));
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == reqCodeOne) {
				tv_phone.setText(AppContext.getInstance().getUserInfo()
						.getPhone());
			}
		}
	}
}
