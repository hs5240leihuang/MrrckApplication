package com.meiku.dev.ui.im;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 入申请群
 */
public class VerifyGroupRequestActivity extends BaseActivity {

	private LinearLayout layout_icon;
	private EditText edit_msg;
	private int groupId;
	private int leanCloudUserName;
	private String picture;
	private TextView right_txt_title;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_verifygrouprequest;
	}

	@Override
	public void initView() {
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GetDate();
			}
		});
		layout_icon = (LinearLayout) findViewById(R.id.layout_icon);
		edit_msg = (EditText) findViewById(R.id.edit_msg);

	}

	@Override
	public void initValue() {
		picture = getIntent().getStringExtra("picture");
		groupId = getIntent().getIntExtra("groupId", -1);
		leanCloudUserName = getIntent().getIntExtra("leanCloudUserName", -1);
		layout_icon.removeAllViews();
		MySimpleDraweeView iv_head = new MySimpleDraweeView(
				VerifyGroupRequestActivity.this);
		layout_icon.addView(iv_head, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv_head.setUrlOfImage(AppContext.getInstance().getUserInfo().getClientThumbHeadPicUrl());
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.e("555", requestCode + "##" + reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast(reqBase.getHeader().getRetMessage());
			break;

		default:
			break;
		}

	}

	public void GetDate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("groupId", groupId);
		map.put("leanCloudUserName", leanCloudUserName);
		map.put("remark", edit_msg.getText().toString());
		map.put("nickName", AppContext.getInstance().getUserInfo()
				.getNickName());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18048));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}
}
