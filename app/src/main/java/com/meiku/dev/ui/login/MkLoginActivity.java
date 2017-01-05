package com.meiku.dev.ui.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.ConstantLogin;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.HomeActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 新的登录页面
 * 
 * @author 库
 * 
 */
public class MkLoginActivity extends BaseActivity implements OnClickListener {
	public final static String KEY_PHONE_NUM = "KEY_PHONE_NUM";
	private ImageView backImg, img_clearphone, img_setpasstatus;

	private Button btn_login;
	private TextView txt_forget, txt_regis;

	private EditText edit_phone, edit_password;
	private ImageButton btn_qq_login, btn_wechat_login;
	private String loginOpenid, currLoginType;// 当前登录类型
	private boolean ispassword = false;
	private int flag;
	protected String thirdLoginInfo_Name;
	protected String thirdLoginInfo_Gender;
	protected String thirdLoginInfo_HeadImgUrl;
	private UMShareAPI mShareAPI;
	private boolean needGoToHome;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_mklogin;
	}

	@Override
	public void initView() {
		img_setpasstatus = (ImageView) findViewById(R.id.img_setpasstatus);
		img_clearphone = (ImageView) findViewById(R.id.img_clearphone);
		btn_qq_login = (ImageButton) findViewById(R.id.btn_qq_login);
		btn_wechat_login = (ImageButton) findViewById(R.id.btn_wechat_login);
		backImg = (ImageView) findViewById(R.id.btnBack);
		backImg.setOnClickListener(this);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_login.setAlpha(0.8f);
		txt_forget = (TextView) findViewById(R.id.txt_forget);
		txt_forget.setOnClickListener(this);
		txt_regis = (TextView) findViewById(R.id.txt_regis);
		txt_regis.setOnClickListener(this);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_phone.clearFocus();
		edit_phone.setCursorVisible(false);
		edit_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edit_phone.setCursorVisible(true);
			}
		});
		edit_phone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) {
					edit_phone.setCursorVisible(true);
				}
			}
		});
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_password.clearFocus();
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_password.clearFocus();
		edit_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (edit_password.getText().toString().length() > 5
						&& edit_phone.getText().toString().length() == 11) {
					btn_login.setAlpha(1);
				} else {
					btn_login.setAlpha(0.8f);
				}
			}
		});
		edit_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				if (s.length() == 11) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("phone", s.toString());
					// map.put("queryType", ConstantKey.PHONE_TYPE_LOGIN);
					req.setHeader(new ReqHead(AppConfig.BUSINESS_YANZHENG_EXIST));
					req.setBody(JsonUtil.Map2JsonObj(map));
					httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
							req, false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (edit_phone.getText().length() > 0) {
					img_clearphone.setVisibility(View.VISIBLE);
				} else {
					img_clearphone.setVisibility(View.INVISIBLE);
				}
				if (edit_password.getText().toString().length() > 5
						&& edit_phone.getText().toString().length() == 11) {
					btn_login.setAlpha(1);
				} else {
					btn_login.setAlpha(0.8f);
				}
			}
		});
	}

	@Override
	public void initValue() {
		regisBroadcast();
		mShareAPI = UMShareAPI.get(this);
		mShareAPI.isInstall(this, SHARE_MEDIA.QQ);
		mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN);
		flag = getIntent().getIntExtra("Flag", 0);// 1首次引导页进来登录，0普通登录
		needGoToHome = getIntent().getBooleanExtra("needGoToHome", false);// 登陆成功是否需要重新进主页面（设置里推出登陆需要再次进主界面，游客则直接发广播刷新数据）
	}

	@Override
	protected void onResume() {
		super.onResume();
		String initPhone = getIntent().getStringExtra(KEY_PHONE_NUM);
		if (!TextUtils.isEmpty(initPhone)) {
			edit_phone.setText(initPhone);
		}
	}

	@Override
	public void bindListener() {
		btn_wechat_login.setOnClickListener(this);
		btn_qq_login.setOnClickListener(this);
		findViewById(R.id.layout_clearphone).setOnClickListener(this);
		findViewById(R.id.layout_setpasstatus).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBean = (ReqBase) arg0;
		LogUtil.d("wangke", reqBean.getBody() + "");
		switch (requestCode) {
		case reqCodeOne:
			try {
				String longinCode = reqBean.getBody().get("loginCode")
						.getAsString();
				if (ConstantKey.PHONE_IS_NOEXIT.equals(longinCode)) {// 手机号不存在
					showPhoneNoExsitDialog();
				} else if (ConstantKey.PHONE_IS_RESET.equals(longinCode)) { // 需要设置密码
					Intent intent2 = new Intent(MkLoginActivity.this,
							ResetPassActivity.class);
					intent2.putExtra("phone", edit_phone.getText().toString());
					ToastUtil.showShortToast("当前手机号还未设置密码，请重置密码 ");
					startActivity(intent2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case reqCodeTwo:
			try {
				String loginCode = reqBean.getBody().get("loginCode")
						.getAsString();
				if (ConstantKey.PHONE_LOGIN_SUCCESS.equals(loginCode)) {
					// 登录成功后保存JsessionId，登陆后的数据请求都要用上
					AppContext.getInstance().setJsessionId(
							reqBean.getHeader().getJsessionId());
					MkUser userBean = (MkUser) JsonUtil.jsonToObj(MkUser.class,
							reqBean.getBody().get("user").toString());
					AppContext.getInstance().setLocalUser(userBean);
					MrrckApplication.getInstance().doYunXinLogin(userBean.getLeanCloudUserName(), userBean.getLeanCloudId());
					if (needGoToHome) {
						startActivity(new Intent(MkLoginActivity.this,
								HomeActivity.class));
					} else {
						// 登陆成功发广播刷新主页面
						sendBroadcast(new Intent(
								BroadCastAction.ACTION_LOGIN_SUCCESS));
						finish();
					}

				} else {
					ToastUtil.showShortToast(reqBean.getBody().get("loginMsg")
							.getAsString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case reqCodeThree:
		case reqCodeFour:
			if (ConstantLogin.LOGIN_OTHER_SUCCESS.equals(reqBean.getBody()
					.get("loginCode").getAsString())) { // 登录成功
				AppContext.getInstance().setJsessionId(
						reqBean.getHeader().getJsessionId());
				MkUser userBean = (MkUser) JsonUtil.jsonToObj(MkUser.class,
						reqBean.getBody().get("user").toString());
				AppContext.getInstance().setLocalUser(userBean);
				MrrckApplication.getInstance().doYunXinLogin(userBean.getLeanCloudUserName(), userBean.getLeanCloudId());
				if (needGoToHome) {
					startActivity(new Intent(MkLoginActivity.this,
							HomeActivity.class));
				} else {
					// 三方登陆成功发广播刷新主页面
					sendBroadcast(new Intent(
							BroadCastAction.ACTION_LOGIN_SUCCESS));
					finish();
				}
			} else {
				ToastUtil.showShortToast(reqBean.getBody().get("loginMsg")
						.getAsString());
			}
			break;
		default:
			break;
		}
	}

	// 显示手机号码不存在提示
	private void showPhoneNoExsitDialog() {
		final CommonDialog commonDialog = new CommonDialog(this, "提示",
				"该手机号尚未注册，是否使用该手机号进行注册？", "注册", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						Intent intent = new Intent(MkLoginActivity.this,
								NewRegisUserActivity.class);
						intent.putExtra("phoneNum", edit_phone.getText()
								.toString());
						startActivity(intent);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
						// edit_phone.setText("");
					}
				});
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
		case reqCodeThree:
		case reqCodeFour:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						MkLoginActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
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
			}
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btn_login:
			if (TextUtils.isEmpty(edit_phone.getText().toString())
					|| TextUtils.isEmpty(edit_password.getText().toString())) {
				ToastUtil.showShortToast("用户名或密码不能为空");
				return;
			}
			if (edit_phone.getText().toString().length() < 11) {
				ToastUtil.showShortToast("手机位数不正确");
				return;
			}
			if (edit_password.getText().toString().length() < 6) {
				ToastUtil.showShortToast("密码至少6位");
				return;
			}
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getDecorView()
					.getWindowToken(), 0);
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", edit_phone.getText().toString());
			map.put("pwd", edit_password.getText().toString());
			map.put("longitude",
					MrrckApplication.getInstance().longitude == 0 ? ""
							: MrrckApplication.getInstance().longitude);
			map.put("latitude",
					MrrckApplication.getInstance().laitude == 0 ? ""
							: MrrckApplication.getInstance().laitude);

			req.setHeader(new ReqHead(AppConfig.BUSINESS_NEW_LOGIN));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeTwo, AppConfig.PUBLIC_REQUEST_MAPPING_USER, req);

			break;
		case R.id.txt_forget:
			Intent intent2 = new Intent(MkLoginActivity.this,
					ResetPassActivity.class);
			if (!Tool.isEmpty(edit_phone.getText().toString())) {
				intent2.putExtra("phone", edit_phone.getText().toString());
			}
			startActivity(intent2);
			break;
		case R.id.txt_regis:
			Intent intent = new Intent(MkLoginActivity.this,
					NewRegisUserActivity.class);
			startActivityForResult(intent, reqCodeThree);
			break;
		case R.id.btn_wechat_login:
			mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);
			break;
		case R.id.btn_qq_login:
			mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
			break;
		case R.id.layout_clearphone:
			edit_phone.setText("");
			break;
		case R.id.layout_setpasstatus:
			if (ispassword) {
				edit_password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				img_setpasstatus.setBackgroundResource(R.drawable.guanbi_login);
				ispassword = false;
			} else {
				edit_password
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				img_setpasstatus
						.setBackgroundResource(R.drawable.zhengkai_login);
				ispassword = true;
			}
			edit_password.setSelection(edit_password.getText().toString()
					.length());
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == reqCodeThree) {
				String initPhone = data.getStringExtra(KEY_PHONE_NUM);
				if (!TextUtils.isEmpty(initPhone)) {
					edit_phone.setText(initPhone);
				}
			}
		}
		Log.d("hl", "on activity re 2");
		mShareAPI.onActivityResult(requestCode, resultCode, data);
		Log.d("hl", "on activity re 3");
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_PUBLIC_BANGDING_PHONE);
		registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_PUBLIC_BANGDING_PHONE.equals(intent
					.getAction())) {
				finish();
			}
		}
	};

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * auth callback interface
	 * 
	 * 微信验证返回log： WEIXIN,action=0,data = {unionid=osIcyuGCFPyZhq7TPRjvw86nR-Tw,
	 * scope=snsapi_userinfo, expires_in=7200, access_token=
	 * nmGaRubmN4TA8EDDR1PN0ySwjQNSpX6AbS02FvSj5BKAni5n5I8T4pYUrM9c8RxWi9UNxnvrhAr53wdqq_bNs2mtNrGymTv4R8DJslttPdY
	 * , openid=ouI9NtxkXSmteYv-bENbrjIuRMZM, refresh_token=
	 * MBfPIrDGcy9pQ90ZzldpkZz30EoEMCc3oj4OqbuD5nWOJuqiKR_IDJwvktCkHpKF2SM7CjhAznaZzgsv17W5pyFZVbPzvwqFFDA5m_sFBeU
	 * }
	 * 
	 * 微信用户信息返回log： WEIXIN,action=2,data =
	 * {unionid=osIcyuGCFPyZhq7TPRjvw86nR-Tw, country=中国, nickname=黄磊, city=南京,
	 * province=江苏, language=zh_CN, headimgurl=http://wx.qlogo.cn/mmopen/
	 * ajNVdqHZLLCHbzZ2TdgHbsk8MJvvXMQEWpLXMEnPQsw3OLCfBFo00xSByyL0NyVyO9oN6mLm2IpsPdrWSt8Ekg
	 * /0, sex=2, openid=ouI9NtxkXSmteYv-bENbrjIuRMZM}
	 * 
	 * QQ验证返回log： QQ,action=0,data =
	 * {access_token=72BD995CC159F45421D46A82E32E4C66, page_type=, appid=,
	 * pfkey=e40a36bce034f424fb29386bcd1c338a,
	 * uid=7B80D9E357720A67A6C58129E31A9EEB, auth_time=, sendinstall=,
	 * pf=desktop_m_qq-10000144-android-2002-, expires_in=7776000,
	 * pay_token=1BA12B1D95CAF3C8F8A1E46F6DF6F341, ret=0,
	 * openid=7B80D9E357720A67A6C58129E31A9EEB}
	 * 
	 * QQ用户信息返回log： QQ,action=2,data = {is_yellow_vip=0, yellow_vip_level=0,
	 * profile_image_url=http://q.qlogo.cn/
	 * qqapp/1104776283/7B80D9E357720A67A6C58129E31A9EEB/100, screen_name=小火,
	 * msg=, vip=0, city=, gender=男, province=, level=0, is_yellow_year_vip=0,
	 * openid=7B80D9E357720A67A6C58129E31A9EEB}
	 * 
	 * 
	 * 
	 * **/
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action,
				Map<String, String> data) {
			LogUtil.d("hlhl", action + "==>" + platform + ",action=" + action
					+ ",data = " + data);
			switch (action) {
			case 0:// 授权验证
				if (data == null || !data.containsKey("openid")
						|| Tool.isEmpty(data.get("openid"))) {
					ToastUtil.showShortToast("获取第三方登录授权信息失败，未能登录");
					return;
				}
				loginOpenid = data.get("openid").toString();
				ToastUtil.showShortToast("授权完成");
				// 获取登录用户信息
				mShareAPI.getPlatformInfo(MkLoginActivity.this, platform,
						umAuthListener);
				break;
			case 2:// 获取用户信息
				if (data != null) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					StringBuilder sb = new StringBuilder();
					Set<String> keys = data.keySet();
					for (String key : keys) {
						sb.append(key + "=" + data.get(key).toString() + "\r\n");
					}
					map.put("openId", loginOpenid);
					map.put("longitude", MrrckApplication.getInstance()
							.getLongitudeStr());
					map.put("latitude", MrrckApplication.getInstance()
							.getLaitudeStr());
					if (SHARE_MEDIA.QQ == platform) {
						LogUtil.d("hl", SHARE_MEDIA.QQ + sb.toString());
						if (data.containsKey("screen_name")) {
							thirdLoginInfo_Name = data.get("screen_name")
									.toString();
						}
						String sex = "";
						if (data.containsKey("gender")) {
							sex = data.get("gender").toString();// 腾讯男性别返回“男”
						}
						if ("男".equals(sex)) {
							thirdLoginInfo_Gender = "1";
						} else {
							thirdLoginInfo_Gender = "2";
						}
						thirdLoginInfo_HeadImgUrl = data.get(
								"profile_image_url").toString();
						currLoginType = ConstantLogin.LOGIN_QQ;
						map.put("flag", ConstantLogin.LOGIN_QQ);
						map.put("gender", thirdLoginInfo_Gender);
						map.put("nickName", thirdLoginInfo_Name);
						map.put("recommendCode", "");
						map.put("headPicUrl", thirdLoginInfo_HeadImgUrl);
						req.setHeader(new ReqHead(AppConfig.BUSINESS_US_20072));
						req.setBody(JsonUtil.Map2JsonObj(map));
						httpPost(reqCodeThree, AppConfig.USER_REQUEST_MAPPING,
								req);

					} else if (SHARE_MEDIA.WEIXIN == platform) {
						LogUtil.d("hl", SHARE_MEDIA.WEIXIN + sb.toString());
						if (data.containsKey("nickname")) {
							thirdLoginInfo_Name = data.get("nickname")
									.toString();
						}
						String sex = "";
						if (data.containsKey("sex")) {
							sex = data.get("sex").toString();// 微信男性别返回“2”
						}
						if ("2".equals(sex)) {
							thirdLoginInfo_Gender = "1";
						} else {
							thirdLoginInfo_Gender = "2";
						}
						if (data.containsKey("headimgurl")) {
							thirdLoginInfo_HeadImgUrl = data.get("headimgurl")
									.toString();
						}
						currLoginType = ConstantLogin.LOGIN_WECHAT;
						map.put("flag", ConstantLogin.LOGIN_WECHAT);
						map.put("gender", thirdLoginInfo_Gender);
						map.put("nickName", thirdLoginInfo_Name);
						map.put("recommendCode", "");
						map.put("headPicUrl", thirdLoginInfo_HeadImgUrl);
						req.setHeader(new ReqHead(AppConfig.BUSINESS_US_20072));
						req.setBody(JsonUtil.Map2JsonObj(map));
						httpPost(reqCodeFour, AppConfig.USER_REQUEST_MAPPING,
								req);
					}
					LogUtil.d("hlhl", map + "");
				} else {
					ToastUtil.showShortToast("获取第三方登录用户信息失败，未能登录");
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(getApplicationContext(), "获取第三方登录授权信息失败，未能登录",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(getApplicationContext(), "授权被取消", Toast.LENGTH_SHORT)
					.show();
		}
	};

}
