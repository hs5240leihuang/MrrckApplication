package com.meiku.dev.ui.chat;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.TextUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;

public class GroupNotificationActivity extends BaseActivity {
	private EditText groupcontent;
	private String groupId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_group_notification;
	}

	@Override
	public void initView() {
		groupcontent = (EditText) findViewById(R.id.notification);
		TextView right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(groupcontent.getText().toString())) {
					ToastUtil.showShortToast("群介绍不能为空");
				} else {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("groupId", groupId);
					map.put("groupName", "");
					map.put("remark", groupcontent.getText().toString());
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_NEARBY_GROUPNAME));
					req.setBody(JsonUtil.Map2JsonObj(map));
					httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
				}
			}
		});
		groupId = getIntent().getStringExtra("groupId");
		String content = getIntent().getStringExtra("remark");
		groupcontent.setText(content);
		groupcontent.setSelection(content.length());
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeTwo:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("修改失败");
	}
}
