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

public class GroupNameActivity extends BaseActivity implements OnClickListener {
	
	private String groupName;
	private String groupId;
	private String maxMemberNum;
	private EditText tv_groupname; 
	private TextView right_txt_title;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_group_name;
	}

	@Override
	public void initView() {
		tv_groupname = (EditText) findViewById(R.id.tv_groupname);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
	}

	@Override
	public void initValue() {
		groupId = getIntent().getStringExtra("groupId");
		groupName = getIntent().getStringExtra("groupName");
		maxMemberNum = getIntent().getStringExtra("maxMemberNum");
		tv_groupname.setText(groupName);
		tv_groupname.setSelection(groupName.length());
	}

	@Override
	public void bindListener() {
		right_txt_title.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("修改成功");
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_txt_title:
			//修改群昵称
			if(TextUtils.isEmpty(tv_groupname.getText().toString())){
				ToastUtil.showShortToast("群名称不能为空");
			}else{
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
				map.put("groupName", tv_groupname.getText().toString());
				map.put("groupId", groupId);
				map.put("remark", "");
				reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_NEARBY_GROUPNAME));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
				httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
			}
			break;
		default:
			break;
		}
	}
}
