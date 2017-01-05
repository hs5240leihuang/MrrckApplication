package com.meiku.dev.ui.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;

/**
 * 重设密码
 */
public class ResetPassActivity extends BaseActivity implements OnClickListener {
	private Button getvalidate_btn, btn_regis;
	private EditText security_code_edit, edit_phone, edit_password,
			edit_password2;
	private String validNumber;
	private String regisPhone = "";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_reset_password;
	}

	@Override
	public void initView() {
		getvalidate_btn = (Button) findViewById(R.id.getvalidate_btn);
		getvalidate_btn.setOnClickListener(this);
		security_code_edit = (EditText) findViewById(R.id.security_code_edit);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
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
		btn_regis = (Button) findViewById(R.id.btn_regis);
		btn_regis.setOnClickListener(this);
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_password2 = (EditText) findViewById(R.id.edit_password2);

	}

	@SuppressLint("NewApi")
	@Override
	public void initValue() {
		regisPhone = getIntent().getStringExtra("phone");
		if (!TextUtils.isEmpty(regisPhone)) {
			//ToastUtil.showShortToast("当前手机号还未设置密码，请重置密码 ");
			edit_phone.setText(regisPhone);
		}
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase req = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			try {
				validNumber = req.getBody().get("validNumber").getAsString();
				// validNumber="123456";

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case reqCodeTwo:
			try {
				String loginCode = req.getBody().get("loginCode").getAsString();
				// 0=重置密码成功
				if ("0".equals(loginCode)) {
					ToastUtil.showShortToast("重置密码成功");

					finish();
				} else {
					ToastUtil.showShortToast(req.getBody().get("loginMsg")
							.getAsString());
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getvalidate_btn:
			getSecurityCode();
			break;

		case R.id.btn_regis:
			// security_code_edit,edit_phone,
			// edit_password,edit_password2,edit_nickname
			if (TextUtils.isEmpty(edit_phone.getText().toString())) {
				ToastUtil.showShortToast("手机号不能为空");
				return;
			}
			if (TextUtils.isEmpty(security_code_edit.getText().toString())) {
				ToastUtil.showShortToast("验证码不能为空");
				return;
			}

			if (!security_code_edit.getText().toString().equals(validNumber)) {
				ToastUtil.showShortToast("验证码不匹配");
				return;
			}

			if (TextUtils.isEmpty(edit_password.getText().toString())) {
				ToastUtil.showShortToast("密码不能为空");
				return;
			}
			if (!edit_password.getText().toString()
					.equals(edit_password2.getText().toString())) {
				ToastUtil.showShortToast("两次密码输入不一致");
				return;
			}

			regisPhone = edit_phone.getText().toString();
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", regisPhone);
			map.put("pwd", edit_password.getText().toString());
			map.put("oldPwd", "");
			req.setHeader(new ReqHead(AppConfig.BUSINESS_USER_RESETPASS));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeTwo, AppConfig.USER_REQUEST_MAPPING, req);

			break;

		default:
			break;
		}
	}

	// 获取验证码
	private void getSecurityCode() {
		String phone = edit_phone.getText().toString();

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.showShortToast("手机号不能为空");
			return;
		}
		if (phone.length() < 11) {
			ToastUtil.showShortToast("手机号码不正确！");
			return;
		}
		setTimer();
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_USER_VETIFICATION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.USER_REQUEST_MAPPING, req);
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
				getvalidate_btn.setEnabled(true);

				getvalidate_btn.setTextColor(getResources().getColor(
						R.color.mrrck_bg));
				getvalidate_btn.setBackground(ContextCompat.getDrawable(ResetPassActivity.this,
						R.drawable.dialog_commit));
				getvalidate_btn.setText("获取验证码");
				wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;
				timer.cancel();
				timer = null;

				security_code_edit.clearFocus();
			} else {
				wait -= 1;
				getvalidate_btn.setEnabled(false);
				getvalidate_btn.setTextColor(getResources().getColor(
						R.color.gray));
				getvalidate_btn.setBackground(getResources().getDrawable(
						R.drawable.yanzhengma));
				getvalidate_btn.setText("(" + wait + "s)重新获取");
			}
		}
	};
	// ////////////////////定时器end
}