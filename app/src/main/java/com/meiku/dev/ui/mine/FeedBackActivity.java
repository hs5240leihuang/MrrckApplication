package com.meiku.dev.ui.mine;

import java.util.HashMap;
import java.util.Map;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;

/**
 * 意见反馈页面
 * 
 */
public class FeedBackActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText feedET;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_res_title:
			commitFeedback();
			break;
		default:
			break;
		}
	}

	private void commitFeedback() {
		String content = feedET.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtil.showShortToast("请填写内容");
			return;
		}
		PackageManager pm = getApplicationContext().getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(),
					PackageManager.GET_ACTIVITIES);

			ReqBase reqBase = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			map.put("platform", "2");
			map.put("platformVersion", "" + Build.VERSION.RELEASE);
			map.put("clientVersion", "" + pi.versionName);
			map.put("content", content);
			reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_USER_FEED_BACK));
			reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			httpPost(reqCodeOne, AppConfig.USER_REQUEST_MAPPING, reqBase, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_feed_back;
	}

	@Override
	public void initView() {
		findViewById(R.id.right_res_title).setOnClickListener(this);
		feedET = (EditText) findViewById(R.id.feedET);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ToastUtil.showShortToast("提交成功");
		finish();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("提交失败");
	}

}
