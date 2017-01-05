package com.meiku.dev.ui.im;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 群组信息页面
 */
public class GroupInfoActivity extends BaseActivity {

	private TextView tv_name, tv_owner, tv_info, tv_num;
	private Button btn_join;
	private String roomId;
	private String grouImgPath;
	// private String jobType;
	private String groupname;
	private String msg;
	private MySimpleDraweeView iv_groupImg;
	private GroupEntity groupInfo;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_groupinfo;
	}

	@Override
	public void initView() {
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		iv_groupImg = new MySimpleDraweeView(GroupInfoActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(iv_groupImg, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_owner = (TextView) findViewById(R.id.tv_owner);
		tv_info = (TextView) findViewById(R.id.tv_info);
		// tv_type = findView(this, R.id.tv_type);
		tv_num = (TextView) findViewById(R.id.tv_num);
		btn_join = (Button) findViewById(R.id.btn_join);
	}

	@Override
	public void initValue() {
		roomId = getIntent()
				.getStringExtra(ConstantKey.KEY_IM_MULTI_CHATROOMID);
		if (Tool.isEmpty(roomId)) {
			ToastUtil.showShortToast("此群不存在！");
			finish();
		}
		if (AppContext.getGroupMap().containsKey(Integer.parseInt(roomId))) {
			btn_join.setText("进入群聊");
		}
		getGroupInfo();
	}

	@Override
	public void bindListener() {
		btn_join.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(GroupInfoActivity.this);
					return;
				}
				if (AppContext.getGroupMap().containsKey(
						Integer.parseInt(roomId))) {// 已经在群里则直接进入群聊
					if (groupInfo == null) {
						return;
					}
					Intent intent = new Intent(GroupInfoActivity.this,
							ChatActivity.class);
					intent.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
							groupInfo.getGroupName());
					intent.putExtra(ConstantKey.KEY_IM_TALKTO,
							groupInfo.getId());
					intent.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
							groupInfo.getOtherId());
					intent.putExtra(ConstantKey.KEY_IM_SESSIONTYPE,
							XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
					startActivity(intent);
				} else {
					if (groupInfo.getJoinmode() == 1) {
						startActivity(new Intent(GroupInfoActivity.this,
								VerifyGroupRequestActivity.class)
								.putExtra("groupId", groupInfo.getId())
								.putExtra("leanCloudUserName",
										groupInfo.getOtherId())
								.putExtra("picture",
										groupInfo.getClientThumbGroupPhoto()));
					} else {
						// JoinYunxinGroup(groupInfo.getOtherId());
						JoinTheGroup();
					}
				}
			}
		});
		iv_groupImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!Tool.isEmpty(grouImgPath)) {
					Intent intentImage = new Intent();
					intentImage.setClass(GroupInfoActivity.this,
							ImagePagerActivity.class);
					intentImage.putExtra("showOnePic", grouImgPath);
					startActivity(intentImage);
				} else {
					ToastUtil.showShortToast("图片路径有误!");
				}
			}
		});
	}

	// protected void JoinYunxinGroup(String teamtalkId) {
	// if (Tool.isEmpty(teamtalkId)) {
	// ToastUtil.showShortToast("获取群聊天号失败");
	// return;
	// }
	// NIMClient.getService(TeamService.class).applyJoinTeam(teamtalkId, null)
	// .setCallback(new RequestCallback<Team>() {
	// @Override
	// public void onSuccess(Team team) {
	// JoinTheGroup();
	// LogUtil.d("hl", "joined yunxin group");
	// }
	//
	// @Override
	// public void onFailed(int code) {
	// LogUtil.d("hl", "failed join yunxin group");
	// if (code == 808) {
	// ToastUtil
	// .showShortToast(getString(R.string.team_apply_to_join_send_success));
	// } else if (code == 809) {
	// ToastUtil
	// .showShortToast(getString(R.string.has_exist_in_team));
	// } else {
	// ToastUtil.showShortToast("加入群组失败, 错误码=" + code);
	// }
	// }
	//
	// @Override
	// public void onException(Throwable exception) {
	//
	// }
	// });
	// }

	/**
	 * 获取群组信息
	 */
	private void getGroupInfo() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", roomId);
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_NEARBY_GROUPINFORMATION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req, true);
	}

	/**
	 * 加入群组
	 */
	private void JoinTheGroup() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("groupId", roomId);
		map.put("leanCloudUserName", AppContext.getInstance().getUserInfo()
				.getLeanCloudUserName());
		map.put("nickName", AppContext.getInstance().getUserInfo()
				.getNickName());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_NEARBY_JOINGROUP));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req, true);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		Log.e("hl", resp.getBody() + "");
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("group") != null
					&& (resp.getBody().get("group") + "").length() > 2) {
				groupInfo = (GroupEntity) JsonUtil.jsonToObj(GroupEntity.class,
						resp.getBody().get("group").toString());
				if (groupInfo == null) {
					return;
				}
				// jobType = MKDataBase.getInstance().getJobNameById(
				// groupInfo.getLibGroupId());
				// tv_type.setText(jobType);
				groupname = groupInfo.getGroupName();
				tv_name.setText(groupname);
				tv_info.setText(groupInfo.getRemark());
				tv_num.setText(groupInfo.getMemberNum() + "");
				grouImgPath = groupInfo.getClientThumbGroupPhoto();
				iv_groupImg.setUrlOfImage(grouImgPath);
				tv_owner.setText("群主："
						+ groupInfo.getGroupUserList().get(0).getNickName());
			} else {
				ToastUtil.showShortToast("该群已删除");
				finish();
				return;
			}
			break;
		case reqCodeTwo:
			if ((resp.getBody().get("msg") + "").length() > 2) {
				msg = resp.getBody().get("msg").getAsString();
				ToastUtil.showShortToast(msg);
			}
			if (groupInfo == null) {
				return;
			}
			// 添加到缓存----
			AppContext.getInstance().getGroupTalkIDMap()
					.put(groupInfo.getOtherId(), groupInfo.getId());
			AppContext
					.getInstance()
					.getGroupMap()
					.put(groupInfo.getId(),
							groupInfo.getClientThumbGroupPhoto());
			AppContext.getInstance().getGroupNameMap()
					.put(groupInfo.getId(), groupInfo.getGroupName());

			// TipAttachment attch = new TipAttachment("["
			// + AppContext.getInstance().getUserInfo().getNickName()
			// + "]加入了此群");
			// com.netease.nimlib.sdk.msg.model.IMMessage msg = MessageBuilder
			// .createCustomMessage(groupInfo.getOtherId(), // 聊天对象的
			// // ID，如果是单聊，为用户帐号，如果是群聊，为群组
			// // ID
			// SessionTypeEnum.Team, // 聊天类型，单聊或群组
			// attch);
			// Map<String, Object> content = new HashMap<String, Object>(4);
			// content.put("nickName", AppContext.getInstance().getUserInfo()
			// .getNickName());
			// content.put("clientHeadPicUrl",
			// AppContext.getInstance().getUserInfo()
			// .getClientThumbHeadPicUrl());
			// content.put("userId",
			// AppContext.getInstance().getUserInfo().getId()
			// + "");
			// content.put("groupId", groupInfo.getOtherId());
			// msg.setRemoteExtension(content);
			// msg.setPushPayload(content);
			// CustomMessageConfig config = new CustomMessageConfig();
			// config.enablePushNick = false;
			// msg.setConfig(config);
			// NIMClient.getService(MsgService.class)
			// .sendMessage(msg, true);

			Intent intent = new Intent(GroupInfoActivity.this,
					ChatActivity.class);
			intent.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
					groupInfo.getGroupName());
			intent.putExtra(ConstantKey.KEY_IM_TALKTO, groupInfo.getId());
			intent.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
					groupInfo.getOtherId());
			intent.putExtra(ConstantKey.KEY_IM_SESSIONTYPE,
					XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取群信息失败！");
			finish();
			break;
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			Log.d("hl", resp.getBody() + "");
			if ((resp.getBody().get("msg") + "").length() > 2) {
				msg = resp.getBody().get("msg").getAsString();
				ToastUtil.showShortToast(msg);
			}
			break;
		}
	}

}
