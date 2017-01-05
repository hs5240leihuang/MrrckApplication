package com.meiku.dev.ui.im;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.decoration.IneedDecActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 处理入群申请
 */
public class HandleGroupRequestActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout lin_personImg;
	private TextView tv_personName;
	private TextView tv_personInfo;
	private TextView tv_groupName;
	private TextView tv_msg;
	private TextView cancel, confirm;
	private int groupUserId;
	private GroupUserEntity groupUserEntity;
	private LinearLayout lin_zhuye;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_handlegrouprequest;
	}

	@Override
	public void initView() {
		lin_zhuye = (LinearLayout) findViewById(R.id.lin_zhuye);
		cancel = (TextView) findViewById(R.id.cancel);
		confirm = (TextView) findViewById(R.id.confirm);
		lin_personImg = (LinearLayout) findViewById(R.id.lin_personImg);
		tv_personName = (TextView) findViewById(R.id.tv_personName);
		tv_personInfo = (TextView) findViewById(R.id.tv_personInfo);
		tv_groupName = (TextView) findViewById(R.id.tv_groupName);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
	}

	@Override
	public void initValue() {
		groupUserId = getIntent().getIntExtra("groupUserId", -1);
		GetDate();
	}

	@Override
	public void bindListener() {
		findViewById(R.id.tv_msg).setOnClickListener(this);
		findViewById(R.id.right_txt_title).setOnClickListener(this);
		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);
		lin_zhuye.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.e("336", requestCode + "##"
				+ reqBase.getBody().get("data").toString());
		switch (requestCode) {
		case reqCodeOne:
			if (reqBase.getBody().get("data").toString().length() > 2) {
				groupUserEntity = (GroupUserEntity) JsonUtil.jsonToObj(
						GroupUserEntity.class, reqBase.getBody().get("data")
								.toString());
				tv_msg.setText(groupUserEntity.getRemark());
				tv_personName.setText(groupUserEntity.getNickName());
				tv_groupName.setText(groupUserEntity.getGroupName());
				if (groupUserEntity.getGender() == 1) {
					tv_personInfo.setText(groupUserEntity.getAgeValue()
							+ " 男  " + groupUserEntity.getPositionName());
				} else {
					tv_personInfo.setText(groupUserEntity.getAgeValue()
							+ " 女  " + groupUserEntity.getPositionName());
				}

				lin_personImg.removeAllViews();
				MySimpleDraweeView iv_head = new MySimpleDraweeView(
						HandleGroupRequestActivity.this);
				lin_personImg.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(groupUserEntity
						.getClientThumbHeadPicUrl());
			}

			break;
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
		ReqBase reqBase = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ToastUtil.showShortToast(reqBase.getHeader().getRetMessage());
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.cancel:
			Caozuo(2);
			break;
		case R.id.confirm:
			Caozuo(1);
			break;
		case R.id.lin_zhuye:
			startActivity(new Intent(HandleGroupRequestActivity.this,
					PersonShowActivity.class).putExtra(
					PersonShowActivity.TO_USERID_KEY,
					groupUserEntity.getUserId() + ""));
			break;
		default:
			break;
		}
	}

	public void GetDate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupUserId", groupUserId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18051));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	public void Caozuo(int type) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupUserId", groupUserEntity.getId());
		map.put("type", type);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18050));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}
}
