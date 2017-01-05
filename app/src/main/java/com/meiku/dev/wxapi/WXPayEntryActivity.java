package com.meiku.dev.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.ConstantLogin;
import com.meiku.dev.utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付返回、交互
 * 
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, ConstantLogin.WECHAT_APPId);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		switch (resp.errCode) {
		case 0:// 支付成功后,更新支付页面，与后台验证
			sendBroadcast(new Intent(BroadCastAction.ACTION_WX_PAYRESULT));
			break;
		case -1:
			ToastUtil
					.showShortToast("签名错误、APPID不正确、您的微信账号异常等。");
			break;
		case -2:// 用户取消支付后的界面
			ToastUtil.showShortToast("支付被取消");
			break;
		}
		finish();
	}
}