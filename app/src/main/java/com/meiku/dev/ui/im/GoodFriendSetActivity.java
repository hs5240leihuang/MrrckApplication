package com.meiku.dev.ui.im;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.meiku.dev.R;
import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.CommonEditDialog;
import com.meiku.dev.views.CommonEditDialog.EditClickOkListener;

public class GoodFriendSetActivity extends BaseActivity implements
		OnClickListener {
	private final int reqCodeFive = 500;
	private final int reqCodeSix = 600;
	private FriendEntity data;
	private LinearLayout lincancel, linremove, lindefriend, linset,
			jubaofriend;
	private ToggleButton kaiguanbtn;
	private String friendid, beizhu;
	/*** 好友关系0.陌生人 1.好友 2.关注 3.自己 4.黑名单 */
	private int relationTag;
	/*** 0:不隐身 1:隐身 */
	private int hideFlag;
	private static String newNickName;
	private String sourceType = "2";
	private TextView tv_beizhu;
	private String leanCloudUserName;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_goodfriend_set;
	}

	@Override
	public void initView() {
		tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);
		findViewById(R.id.goback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				reflash();
			}
		});
		lincancel = (LinearLayout) findViewById(R.id.lincancel);
		linremove = (LinearLayout) findViewById(R.id.linremove);
		lindefriend = (LinearLayout) findViewById(R.id.lindefriend);
		jubaofriend = (LinearLayout) findViewById(R.id.jubaofriend);
		linset = (LinearLayout) findViewById(R.id.linset);
		kaiguanbtn = (ToggleButton) findViewById(R.id.kaiguanbtn);
	}

	@Override
	public void initValue() {
		friendid = getIntent().getStringExtra("lahei");
		leanCloudUserName = getIntent().getStringExtra("leanCloudUserName");
		// beizhu = getIntent().getStringExtra("beizhu");
		getdate();
	}

	@Override
	public void bindListener() {
		lincancel.setOnClickListener(this);
		linremove.setOnClickListener(this);
		lindefriend.setOnClickListener(this);
		linset.setOnClickListener(this);
		jubaofriend.setOnClickListener(this);
		kaiguanbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// 隐身设置
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getUserId());
				map.put("friendId", friendid);
				map.put("aliasName", beizhu);
				map.put("nameFirstChar", PinyinUtil.getTerm(beizhu));
				map.put("hideFlag", hideFlag == 1 ? 0 : 1);
				reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_GOODFRIENDSET));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				httpPost(reqCodeSix, AppConfig.PUBLICK_NEARBY_GROUP, reqBase,
						false);
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("取消关注成功");
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU));// 取消关注发广播刷新通讯录
			reflash();
			// lincancel.setVisibility(View.GONE);
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("移除粉丝成功");
			linremove.setVisibility(View.GONE);
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU));// 移除粉丝发广播刷新通讯录
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("拉黑成功");
			Intent mIntent = new Intent(BroadCastAction.ACTION_IM_BLACKLIST);
			// 发送广播
			sendBroadcast(mIntent);
			reflash();
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("修改成功");
			AppContext.getFriendSet().add(Integer.parseInt(friendid));
			AppContext.getRemarkMap().put(Integer.parseInt(friendid),
					newNickName);
			beizhu = newNickName;
			tv_beizhu.setText(beizhu);
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU));
			break;
		case reqCodeFive:
			ReqBase resp = (ReqBase) arg0;
			String jsonstr = resp.getBody().get("friend").toString();
			relationTag = Integer.parseInt(resp.getBody().get("data")
					.getAsString());

			try {
				data = (FriendEntity) JsonUtil.jsonToObj(FriendEntity.class,
						jsonstr);
				hideFlag = data.getHideFlag();
				beizhu = data.getAliasName();
				setToogleButonBg(hideFlag);
				tv_beizhu.setText(beizhu);
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			break;
		case reqCodeSix:
			hideFlag = (hideFlag == 1 ? 0 : 1);
			setToogleButonBg(hideFlag);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置隐身状态背景
	 * 
	 * @param hideFlag
	 */
	private void setToogleButonBg(int hideFlag) {
		if (hideFlag == 1) {
			kaiguanbtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ios7_switch_on));
		} else {
			kaiguanbtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ios7_switch_off));
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("取消关注失败");
			break;

		case reqCodeTwo:
			ToastUtil.showShortToast("移除粉丝失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("拉黑失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("修改失败");
			break;
		case reqCodeFive:

			break;
		case reqCodeSix:
			ToastUtil.showShortToast("修改失败");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lincancel:
			// 取消关注
			ReqBase reqBase = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo()
					.getUserId());
			map.put("friendId", friendid);
			reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CANCELFOCUS));
			reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
			break;
		case R.id.linremove:
			// 移除粉丝
			ReqBase req = new ReqBase();
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("userId", AppContext.getInstance().getUserInfo()
					.getUserId());
			map1.put("friendId", friendid);
			req.setHeader(new ReqHead(AppConfig.BUSSINESS_REMOVEFANS));
			req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map1)));
			httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req, true);
			break;
		case R.id.lindefriend:

			final CommonDialog commonDialog = new CommonDialog(
					GoodFriendSetActivity.this, "提示",
					"拉黑后将不再接收到对方发来的消息，可在“系统设置〉黑名单”中解除。是否拉黑？", "确认", "取消");
			commonDialog.show();
			commonDialog.changetext(33, 38, R.color.mrrck_bg);
			commonDialog.setClicklistener(new ClickListenerInterface() {

				@Override
				public void doConfirm() {
					// 拉黑
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getUserId());
					map.put("deFriendId", friendid);
					map.put("toLeanCloudUserName", leanCloudUserName);
					map.put("leanCloudUserName", AppContext.getInstance()
							.getUserInfo().getLeanCloudUserName());
					reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_ADD_BLACK));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeThree, AppConfig.PUBLICK_NEARBY_GROUP,
							reqBase, false);
					commonDialog.dismiss();
				}

				@Override
				public void doCancel() {
					// TODO Auto-generated method stub
					commonDialog.dismiss();
				}
			});
			break;
		case R.id.linset:
			// final EditDialog editDialog = new EditDialog(context, "修改备注",
			// beizhu, new EditClickListener() {
			//
			// @Override
			// public void doConfirm(String inputString) {
			// // 修改备注
			// ReqBase reqBase = new ReqBase();
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("userId", AppContext.getInstance()
			// .getUserInfo().getUserId());
			// map.put("friendId", friendid);
			// map.put("aliasName", inputString);
			// map.put("nameFirstChar", PinyinUtil.getTerm(beizhu));
			// map.put("hideFlag", hideFlag);
			// reqBase.setHeader(new ReqHead(
			// AppConfig.BUSSINESS_GOODFRIENDSET));
			// reqBase.setBody(JsonUtil.String2Object(JsonUtil
			// .hashMapToJson(map)));
			// httpPost(reqCodeFour,
			// AppConfig.PUBLICK_NEARBY_GROUP, reqBase,
			// false);
			// newNickName = inputString;
			// }
			// });
			// editDialog.show();
			new CommonEditDialog(GoodFriendSetActivity.this, "修改备注", "请填写备注名",
					beizhu, 10, true, new EditClickOkListener() {

						@Override
						public void doConfirm(String inputString) {
							// 修改备注
							ReqBase reqBase = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getUserId());
							map.put("friendId", friendid);
							map.put("aliasName", inputString);
							map.put("nameFirstChar", PinyinUtil.getTerm(beizhu));
							map.put("hideFlag", hideFlag);
							reqBase.setHeader(new ReqHead(
									AppConfig.BUSSINESS_GOODFRIENDSET));
							reqBase.setBody(JsonUtil.String2Object(JsonUtil
									.hashMapToJson(map)));
							httpPost(reqCodeFour,
									AppConfig.PUBLICK_NEARBY_GROUP, reqBase,
									false);
							newNickName = inputString;
						}
					}).show();
			break;
		case R.id.jubaofriend:
			Intent intent = new Intent(GoodFriendSetActivity.this,
					JuBaoActivity.class);
			intent.putExtra("friendid", friendid);
			intent.putExtra("sourceType", sourceType);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	/**
	 * 查询当前登录用户与查看用户关系
	 */
	public void getdate() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("toUserId", friendid);
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CHECK_RELATIONSHIP));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeFive, AppConfig.PUBLICK_NEARBY_GROUP, reqBase);
	}

	public void reflash() {
		setResult(RESULT_OK);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			reflash();
		}
		return false;
	}
}
