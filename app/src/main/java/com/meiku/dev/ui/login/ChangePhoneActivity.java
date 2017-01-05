package com.meiku.dev.ui.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.NewDialog;
import com.meiku.dev.views.NewDialog.ClickListenerInterface;

/**
 * 更换绑定手机号
 * 
 */
public class ChangePhoneActivity extends BaseActivity {

	private EditText et_newPhone, et_code;
	private Button btn_yanzhengma, btnOk;
	private String validNumber;
	private TextView et_phone;
	private String currentSendPhoneNum;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_changephone;
	}

	@Override
	public void initView() {
		et_phone = (TextView) findViewById(R.id.et_phone);
		et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
		et_newPhone = (EditText) findViewById(R.id.et_newPhone);
		et_code = (EditText) findViewById(R.id.et_code);
		btn_yanzhengma = (Button) findViewById(R.id.btn_yanzhengma);
		btn_yanzhengma.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String newPhone = et_newPhone.getText().toString();
				if (TextUtils.isEmpty(newPhone)) {
					ToastUtil.showShortToast("手机号不能为空！");
					return;
				}
				if (newPhone.length() < 11) {
					ToastUtil.showShortToast("手机号码不正确！");
					return;
				}
				setTimer();
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("phone", newPhone);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_USER_VETIFICATION));
				req.setBody(JsonUtil.Map2JsonObj(map));
				httpPost(reqCodeTwo, AppConfig.USER_REQUEST_MAPPING, req);
			}
		});
		et_newPhone.addTextChangedListener(new TextWatcher() {

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
				if (arg0.toString().toString().length() == 11) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("phone", arg0.toString());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_US_20075));
					req.setBody(JsonUtil.Map2JsonObj(map));
					httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
							req, false);
				}
			}
		});
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String newphone = et_newPhone.getText().toString();
				if (TextUtils.isEmpty(newphone)) {
					ToastUtil.showShortToast("新手机号不能为空！");
					return;
				}
				if (newphone.length() < 11) {
					ToastUtil.showShortToast("新手机号码不正确！");
					return;
				}
				if (newphone.equals(et_phone.getText().toString())) {
					ToastUtil.showShortToast("新手机号不能与原手机号一致！");
					return;
				}
				if (Tool.isEmpty(et_code.getText().toString())) {
					ToastUtil.showShortToast("请填写验证码");
					return;
				}
				if (!et_code.getText().toString().equals(validNumber)) {
					ToastUtil.showShortToast("验证码不匹配");
					return;
				}
				if (!newphone.equals(currentSendPhoneNum)) {
					ToastUtil.showShortToast("验证码不匹配");
					return;
				}
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("phone", et_phone.getText().toString());
				map.put("newPhone", newphone);
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				LogUtil.e("hl", "更换手机号=》"+map);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_US_20076));
				req.setBody(JsonUtil.Map2JsonObj(map));
				httpPost(reqCodeThree, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
						req, true);
			}
		});
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBean = (ReqBase) arg0;
		LogUtil.d("wangke", reqBean.getBody() + "");
		switch (requestCode) {
		case reqCodeOne:
			try {
				if (!Tool.isEmpty(reqBean.getBody())
						&& !Tool.isEmpty(reqBean.getBody().get("data"))
						&& reqBean.getBody().get("data").toString().length() > 2) {// 号码已存在
					ToastUtil.showShortToast("该手机号已存在！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case reqCodeTwo:
			validNumber = reqBean.getBody().get("validNumber").getAsString();
			currentSendPhoneNum = reqBean.getBody().get("phone").getAsString();
			break;
		case reqCodeThree:
			try {
				if (!Tool.isEmpty(reqBean.getBody())
						&& !Tool.isEmpty(reqBean.getBody().get("data"))
						&& reqBean.getBody().get("data").toString().length() > 2) {// 号码已存在
					MkUser userBean = (MkUser) JsonUtil.jsonToObj(MkUser.class,
							reqBean.getBody().get("data").toString());
					AppContext.getInstance().getUserInfo()
							.setPhone(userBean.getPhone());
					AppContext.getInstance().setLocalUser(
							AppContext.getInstance().getUserInfo());
					final NewDialog newDialog = new NewDialog(
							ChangePhoneActivity.this, "提示", reqBean.getHeader()
									.getRetMessage(), "我知道了");
					newDialog.setClicklistener(new ClickListenerInterface() {

						@Override
						public void doConfirm() {
							newDialog.dismiss();
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void doCancel() {
							newDialog.dismiss();
						}
					});
					newDialog.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
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
		case reqCodeTwo:
		case reqCodeThree:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final NewDialog newDialog = new NewDialog(
						ChangePhoneActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
				newDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						newDialog.dismiss();
					}

					@Override
					public void doCancel() {
						newDialog.dismiss();
					}
				});
				newDialog.show();
			}
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeTimer();
	}

	// /////////==定义验证码定时器
	private Timer timer = null;// 定时器
	private int wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;// 倒计时

	/**
	 * 开启定时器
	 */

	private void setTimer() {
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timeHandler.sendEmptyMessage(1);
			}
		}, 0, 1000);
	}

	/**
	 * 关闭定时器
	 */
	private void removeTimer() {
		wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 定时器任务
	 */
	Handler timeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (wait <= 0) {
				btn_yanzhengma.setEnabled(true);
				btn_yanzhengma.setText("获取验证码");
				wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;
				timer.cancel();
				timer = null;
				et_code.clearFocus();
			} else {
				wait -= 1;
				btn_yanzhengma.setEnabled(false);
				btn_yanzhengma.setText("(" + wait + "s)重新发送");
			}
		}
	};
}
