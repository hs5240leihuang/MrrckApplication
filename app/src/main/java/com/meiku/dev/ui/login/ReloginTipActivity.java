package com.meiku.dev.ui.login;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;

/**
 * 重复登陆提示框
 * 
 */
public class ReloginTipActivity extends Activity {

	private TextView tvMessage;
	private TextView tvConfirm;
	private TextView tvCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_newdialog);
		initView();
		setFinishOnTouchOutside(false);
	}

	public void initView() {
		TextView tvTitle = (TextView) findViewById(R.id.title);
		tvMessage = (TextView) findViewById(R.id.message);
		tvConfirm = (TextView) findViewById(R.id.confirm);
		tvCancel = (TextView) findViewById(R.id.cancel);
		tvTitle.setText("登录异常");
		tvMessage.setText("您的账号已在另一台设备登陆,是否重新登陆？");
		tvConfirm.setText("重新登录");
		tvCancel.setText("取消");
		tvConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MrrckApplication.getInstance().clearActivityes();
				startActivity(new Intent(ReloginTipActivity.this,
						MkLoginActivity.class).putExtra("needGoToHome", true));
				finish();
			}
		});
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MrrckApplication.getInstance().clearActivityes();
				startActivity(new Intent(ReloginTipActivity.this,
						MkLoginActivity.class).putExtra("needGoToHome", true));
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

}
