package com.meiku.dev.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.ConstantLogin;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		// 将该app注册到微信
		api.registerApp(ConstantLogin.WECHAT_APPId);//APP_ID
	}
}
