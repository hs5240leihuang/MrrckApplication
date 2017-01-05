package com.meiku.dev.ui.login;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

/**
 * 修改密码（成功后重新登陆）
 *
 */
public class ChangePassActivity extends BaseActivity {
	private EditText edtoldpsd, edtnewpsd, edtrnewpsd;
	private Button btnok;
	private String regisPhone, validNumber;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_change_password;
	}

	@Override
	public void initView() {
		regisPhone = AppContext.getInstance().getUserInfo().getPhone();
		edtoldpsd = (EditText) findViewById(R.id.edtoldpsd);
		edtnewpsd = (EditText) findViewById(R.id.edtnewpsd);
		edtrnewpsd = (EditText) findViewById(R.id.edtrnewpsd);
		btnok = (Button) findViewById(R.id.btnok);
		btnok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(edtoldpsd.getText().toString())) {
					ToastUtil.showShortToast("旧密码不能为空");
					return;
				}
				if (TextUtils.isEmpty(edtnewpsd.getText().toString())) {
					ToastUtil.showShortToast("新密码不能为空");
					return;
				}
				if (edtnewpsd.getText().toString().length() < 6) {
					ToastUtil.showShortToast("密码必须6位以上");
					return;
				}
				if (TextUtils.isEmpty(edtrnewpsd.getText().toString())) {
					ToastUtil.showShortToast("确认新密码为空");
					return;
				}
				if (!edtnewpsd.getText().toString()
						.equals(edtrnewpsd.getText().toString())) {
					ToastUtil.showShortToast("新密码不一致");
					return;
				}
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("phone", regisPhone);
				map.put("pwd", edtnewpsd.getText().toString());
				map.put("oldPwd", edtoldpsd.getText().toString());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_USER_RESETPASS));
				req.setBody(JsonUtil.Map2JsonObj(map));
				httpPost(reqCodeOne, AppConfig.USER_REQUEST_MAPPING, req);

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
		ReqBase req = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(req.getBody())
					|| !Tool.isEmpty(req.getBody().get("loginCode"))) {
				String loginCode = req.getBody().get("loginCode").getAsString();
				if ("0".equals(loginCode)) {// 0=修改密码成功
					ToastUtil.showShortToast("修改密码成功,请重新登陆");
					doLogout();
				} else {
					ToastUtil.showShortToast(req.getBody().get("loginMsg")
							.getAsString());
				}
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
			ToastUtil.showShortToast("修改失败");
			break;

		default:
			break;
		}
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
		sendBroadcast(new Intent(BroadCastAction.ACTION_MAIN_LOGOUT));
		finish();
	}
}
