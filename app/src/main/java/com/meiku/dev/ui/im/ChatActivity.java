package com.meiku.dev.ui.im;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.IMDraft;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShareMessage;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.ui.activitys.BaseChatActivity;
import com.meiku.dev.ui.chat.GroupManageActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.yunxin.CardAttachment;
import com.meiku.dev.yunxin.FileBrowserActivity;
import com.meiku.dev.yunxin.ShareAttachment;
import com.meiku.dev.yunxin.TeamDataCache;
import com.meiku.dev.yunxin.TipAttachment;
import com.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 好友聊天
 * 
 */
public class ChatActivity extends BaseChatActivity {

	private String talkToName;
	private int talkToID;// 人Id或者群ID，用来查询人、群信息(不是聊天的ID，聊天的是talkToAccount)
	private View headview;
	private boolean isResume = false;
	private String myPushNickName;

	@Override
	protected void onResume() {
		super.onResume();
		String nickname = AppContext.getRemarkMap().get(talkToID);
		if (!Tool.isEmpty(nickname)) {
			center_txt_title.setText(nickname);
		}
		NIMClient.getService(MsgService.class).setChattingAccount(
				talkToAccount, sessionType);// 进入聊天界面，建议放在onResume中
		isResume = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		NIMClient.getService(MsgService.class).setChattingAccount(
				MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);// 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
	}

	@Override
	protected void onDestroy() {
		registerObservers(false);
		unregisterReceiver(receiver);
		NIMClient.getService(MsgServiceObserve.class)
				.observeCustomNotification(commandObserver, false);
		super.onDestroy();
	}

	@Override
	public void init() {
		regisBroadcast();
		headview = LayoutInflater.from(this).inflate(
				R.layout.view_chat_head_guanzhu, null);
		headLayout.addView(headview);
		headview.findViewById(R.id.btn_guanzhu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						guanzhuFriend();
					}
				});
	}

	@Override
	public void doTitleLeftBtnClick() {
		hideSoftInputView();
		finish();
	}

	@Override
	public void doTitleRightBtnClick(int id) {
		if (isTeamTalk()) {
			Intent intent = new Intent(ChatActivity.this,
					GroupManageActivity.class);
			intent.putExtra(ConstantKey.KEY_IM_MULTI_CHATROOM, talkToName);
			intent.putExtra(ConstantKey.KEY_IM_MULTI_CHATROOMID, talkToID);
			startActivityForResult(intent, MANAGEGROUP);
		} else {
			Intent intent = new Intent(ChatActivity.this,
					PersonShowActivity.class);
			intent.putExtra("nickName", talkToName);
			intent.putExtra(PersonShowActivity.TO_USERID_KEY, talkToID + "");
			startActivity(intent);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		initValue();
	}

	@Override
	public void initValue() {
		if (items != null) {
			items.clear();
		}
		myPushNickName = AppContext.getInstance().getUserInfo().getNickName();
		talkToID = getIntent().getIntExtra(ConstantKey.KEY_IM_TALKTO, -1);
		talkToAccount = getIntent().getStringExtra(
				ConstantKey.KEY_IM_TALKTOACCOUNT);
		talkToName = getIntent().getStringExtra(ConstantKey.KEY_IM_TALKTO_NAME);
		if (getIntent().getIntExtra(ConstantKey.KEY_IM_SESSIONTYPE, -1) == XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK) {
			sessionType = SessionTypeEnum.Team;
		} else {
			sessionType = SessionTypeEnum.P2P;
		}
		LogUtil.d("hl", "talkToID==" + talkToID + "\n talkToAccount=="
				+ talkToAccount + "\n talkToName==" + talkToName + "\n 聊天类型=="
				+ (isTeamTalk() ? "群聊" : "单聊"));
		registerObservers(true);
		setMessageLoader();
		right_res_title.setVisibility(View.GONE);
		right_txt_title.setText("资料");
		// if (talkToID == -1) {
		// ToastUtil.showShortToast("无效用户,进入聊天页失败");
		// finish();
		// return;
		// }
		if (Tool.isEmpty(talkToAccount)) {
			ToastUtil.showShortToast("聊天ID无效，进入聊天页失败");
			finish();
			return;
		} else {
			center_txt_title.setText(talkToName);
			if (isTeamTalk()) {// 群聊天不显示 顶部的好友关注栏
				showRightTxtOrImg(false);
				isShowHeadView(false);
				if (talkToID == -1) {// 通过聊天号查
					checkGroupInfoByOtherId(talkToAccount);
				} else {
					checkGroupInfo(talkToID);
				}
			} else {
				showRightTxtOrImg(true);
				getRelationship();
				// // 单聊时注册好友输入状态监听
				// NIMClient.getService(MsgServiceObserve.class)
				// .observeCustomNotification(commandObserver, true);
			}
			String shareMessage = getIntent().getStringExtra(
					ConstantKey.KEY_SHARE_KEY);
			if (!Tool.isEmpty(shareMessage)) {
				sendShareMessage(shareMessage);
			}
		}
		// 获取草稿消息
		IMDraft draft = MsgDataBase.getInstance().queryDraft(
				XmppConstant.IM_CHAT_TALKTYPE_SINGLE, talkToID);
		if (null != draft) {
			String draftContent = draft.getContent();
			if (!Tool.isEmpty(draftContent)) {
				contentEdit.setText(draftContent);
				contentEdit.setSelection(draftContent.length());
				sendBtn.setEnabled(true);
				showSendBtn();
			}
		}
	}

	/**
	 * 命令消息接收观察者
	 */
	Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
		@Override
		public void onEvent(CustomNotification message) {
			if (!talkToAccount.equals(message.getSessionId())
					|| message.getSessionType() != SessionTypeEnum.P2P) {
				return;
			}
			showCommandMessage(message);
		}
	};

	protected void showCommandMessage(CustomNotification message) {
		if (!isResume) {
			return;
		}
		String content = message.getContent();
		try {
			Map<String, String> map = JsonUtil.jsonToMap(content);
			if (map.containsKey("id")) {
				if ("1".equals(map.get("id").toString())) {
					// 正在输入
					ToastUtil.showShortToast("对方正在输入...");
				} else {
					ToastUtil.showShortToast("command: " + content);
				}
			}
		} catch (Exception e) {
		}
	}

	public void checkGroupInfo(int talkToID) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", talkToID);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_GP_18046));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, false);
	}

	public void checkGroupInfoByOtherId(String otherId) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("otherId", otherId);
		reqBase.setHeader(new ReqHead(AppConfig.GP_18052));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeFour, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, false);
	}

	/**
	 * 查询当前登录用户与查看用户关系
	 */
	public void getRelationship() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("toUserId", (talkToID == -1) ? "" : (talkToID + ""));
		map.put("toLeanCloudUserName", talkToAccount);
		map.put("loadFlag", 0);
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CHECK_RELATIONSHIP));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, false);
	}

	/**
	 * 关注此好友
	 */
	public void guanzhuFriend() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("friendId", talkToID);
		map.put("aliasName", talkToName);
		map.put("nameFirstChar", PinyinUtil.getTerm(talkToName));
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_ADDFOCUS));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==chat==>" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			/*** 好友关系0.陌生人 1.好友 2.关注 3.自己 4.黑名单 */
			int relationTag = Integer.parseInt(resp.getBody().get("data")
					.getAsString());
			if (relationTag == 0) {
				isShowHeadView(true);
			}
			if (resp.getBody().get("dataInfo") != null
					&& resp.getBody().get("dataInfo").toString().length() > 4) {
				MkUser friendInfo = (MkUser) JsonUtil.jsonToObj(MkUser.class,
						resp.getBody().get("dataInfo").toString());
				if (friendInfo != null) {
					String alineName = friendInfo.getNickName();
					if (AppContext.getFriendTalkIdMap().containsKey(
							talkToAccount)) {// 是好友
						if (Tool.isEmpty(AppContext.getRemarkMap().get(
								friendInfo.getId()))) {
							AppContext.getRemarkMap().put(friendInfo.getId(),
									friendInfo.getNickName());
						}
						alineName = AppContext.getRemarkMap().get(
								friendInfo.getId());
						AppContext.getHeadImgMap().put(friendInfo.getId(),
								friendInfo.getClientThumbHeadPicUrl());
						AppContext.getFriendSet().add(friendInfo.getId());
					}
					IMYxUserInfo userInfo = new IMYxUserInfo();
					userInfo.setNickName(alineName);
					userInfo.setUserHeadImg(friendInfo
							.getClientThumbHeadPicUrl());
					userInfo.setYxAccount(friendInfo.getLeanCloudUserName());
					userInfo.setUserId(friendInfo.getUserId());
					MsgDataBase.getInstance().saveOrUpdateYxUser(userInfo);
					String nickname = AppContext.getRemarkMap().get(talkToID);
					if (!Tool.isEmpty(nickname)) {
						center_txt_title.setText(nickname);
					} else {
						center_txt_title.setText(friendInfo.getNickName());
					}
					if (!Tool.isEmpty(friendInfo.getFriend2MyNickName())) {
						myPushNickName = friendInfo.getFriend2MyNickName();
					}
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							adapter.notifyDataSetChanged();
						}
					}, 1000);
				}
			}
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("关注成功");
			isShowHeadView(false);
			com.netease.nimlib.sdk.msg.model.IMMessage msg = MessageBuilder
					.createTipMessage(talkToAccount, sessionType);
			msg.setContent(AppContext.getInstance().getUserInfo().getNickName()
					+ "关注了你，互相关注成为好友，可随时了解对方动态。");
			sendMessage(msg);
			break;
		case reqCodeThree:
			if (resp.getBody().get("data") != null
					&& resp.getBody().get("data").toString().length() > 4) {
				final GroupEntity groupSimpleInfo = (GroupEntity) JsonUtil
						.jsonToObj(GroupEntity.class, resp.getBody()
								.get("data").toString());
				if (groupSimpleInfo != null) {// 群信息放缓存
					center_txt_title.setText(groupSimpleInfo.getGroupName()
							+ "(" + groupSimpleInfo.getMemberNum() + ")");
					// 获取我在该群的昵称
					if (!Tool.isEmpty(groupSimpleInfo.getNickName())) {
						myPushNickName = groupSimpleInfo.getNickName();
					} else if (AppContext.getMyGroupNickNameMap().containsKey(
							talkToID)
							&& !Tool.isEmpty(AppContext.getMyGroupNickNameMap()
									.get(talkToID))) {
						myPushNickName = AppContext.getMyGroupNickNameMap()
								.get(talkToID);
					}
					if (groupSimpleInfo.getGroupUserList() != null) {
						new AsyncTask<Integer, Integer, Boolean>() {
							@Override
							protected void onPostExecute(Boolean result) {
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										adapter.notifyDataSetChanged();
									}
								}, 1000);
								super.onPostExecute(result);
							}

							@Override
							protected Boolean doInBackground(Integer... params) {
								List<GroupUserEntity> userList = groupSimpleInfo
										.getGroupUserList();
								boolean hasMeInGroup = false;
								Map<String, String> nickMap = new HashMap<String, String>();
								for (GroupUserEntity user : userList) {// 群成员存DB
									IMYxUserInfo userInfo = new IMYxUserInfo();
									userInfo.setNickName(user.getNickName());
									userInfo.setUserHeadImg(user
											.getClientThumbHeadPicUrl());
									userInfo.setYxAccount(user
											.getLeanCloudUserName());
									userInfo.setUserId(user.getUserId());
									MsgDataBase.getInstance()
											.saveOrUpdateYxUser(userInfo);
									nickMap.put(user.getLeanCloudUserName(),
											user.getNickName());
									if (user.getUserId() == AppContext
											.getInstance().getUserInfo()
											.getId()) {
										hasMeInGroup = true;
									}
								}
								AppContext.getGroupMemberNickNameMap().put(
										talkToID, nickMap);
								if (!hasMeInGroup) {
									// ToastUtil.showShortToast("您已退出该群");
								} else {
									AppContext
											.getInstance()
											.getGroupTalkIDMap()
											.put(groupSimpleInfo.getOtherId(),
													groupSimpleInfo.getId());
									AppContext
											.getInstance()
											.getGroupMap()
											.put(groupSimpleInfo.getId(),
													groupSimpleInfo
															.getClientThumbGroupPhoto());
									AppContext
											.getInstance()
											.getGroupNameMap()
											.put(groupSimpleInfo.getId(),
													groupSimpleInfo
															.getGroupName());
								}
								return null;
							}
						}.execute();
					}
				}
			}
			break;
		case reqCodeFour:
			if (resp.getBody().get("data") != null
					&& resp.getBody().get("data").toString().length() > 4) {
				GroupEntity groupSimpleInfo = (GroupEntity) JsonUtil.jsonToObj(
						GroupEntity.class, resp.getBody().get("data")
								.toString());
				if (groupSimpleInfo != null
						&& !Tool.isEmpty(groupSimpleInfo.getId())) {
					talkToID = groupSimpleInfo.getId();
					center_txt_title.setText(groupSimpleInfo.getGroupName());
					checkGroupInfo(talkToID);
				}
			}
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKE_PHOTO_IMG:
				List<String> pictrue = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(pictrue.get(i));
					}
				}
				// 单张相册选择图片
				// String photoPath = data
				// .getStringExtra(ConstantKey.KEY_PHOTO_PATH);
				// CompressPic(photoPath);
				break;
			case TO_SELECT_VIDEO:
				String videoPath = data
						.getStringExtra(ConstantKey.KEY_VIDEO_PATH);
				int videoHeight = data.getIntExtra(ConstantKey.KEY_VIDEO_WITH,
						100);
				int videoWith = data.getIntExtra(ConstantKey.KEY_VIDEO_HEIGHT,
						100);
				if (!Tool.isEmpty(videoPath)) {
					com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
							.createVideoMessage(talkToAccount, // 聊天对象的
																// ID，如果是单聊，为用户帐号，如果是群聊，为群组
																// ID
									sessionType, // 聊天类型，单聊或群组
									new File(videoPath), // 视频文件
									60, // 视频持续时间
									videoWith, // 视频宽度
									videoHeight, // 视频高度
									"" // 视频显示名，可为空
							);
					message.setPushContent(myPushNickName + ":[发了一段视频]");
					sendMessage(message);
				} else {
					ToastUtil.showShortToast("获取视频路径失败！");
				}
				break;
			case GET_ADDRESS:
				float longitude = data.getFloatExtra("lng",
						(float) MrrckApplication.getInstance().longitude);
				float latitude = data.getFloatExtra("lat",
						(float) MrrckApplication.getInstance().laitude);
				String address = data.getStringExtra("address");
				if (Tool.isEmpty(address)) {
					address = "地址";
				}
				sendLocation(latitude, longitude, address);
				break;
			case REQCODE_CHOISECARD:
				sendPersonCardMessage(data);
				break;
			case CHOISEVIDEO:
				// List<String> videos = data
				// .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// if (!Tool.isEmpty(videos)) {// 多选图片返回
				// for (int i = 0; i < videos.size(); i++) {
				// SendVideoMessage(videos.get(i));
				// }
				// }
				break;
			case CHOISEFILE:// 发送文件
				String path = data
						.getStringExtra(FileBrowserActivity.EXTRA_DATA_PATH);
				File file = new File(path);
				com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
						.createFileMessage(talkToAccount, sessionType, file,
								file.getName());
				sendMessage(message);
				break;
			case MANAGEGROUP:
				finish();
				break;
			case FORWARDMESSAGE:
				if (data != null
						&& talkToAccount
								.equals(data.getStringExtra("selectId"))) {
					if (forwardMessage != null) {
						onMsgSend(forwardMessage);
					}
				}
				break;
			default:
				break;
			}
		} else {
			if (resultCode == ConstantKey.RESULT_REFRESH_GROUP) {
				if (requestCode == MANAGEGROUP) {
					checkGroupInfo(talkToID);
				}
			}
		}
	}

	/**
	 * 发送文字消息
	 */
	public void sendTxtMessage() {
		final String msg = EmotionHelper.getSendEmotion(ChatActivity.this,
				contentEdit.getText().toString());
		if (Tool.isEmpty(msg.trim())) {
			ToastUtil.showShortToast("不能发送空的内容！");
			return;
		}

		com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
				.createTextMessage(talkToAccount, // 聊天对象的
													// ID，如果是单聊，为用户帐号，如果是群聊，为群组
													// ID
						sessionType, // 聊天类型，单聊或群组
						msg // 文本内容
				);
		message.setPushContent(myPushNickName + ":" + msg);
		sendMessage(message);
		contentEdit.setText("");
	}

	/**
	 * 发送名片
	 * 
	 * @param data
	 */
	private void sendPersonCardMessage(final Intent data) {
		String card_id = data.getStringExtra(ConstantKey.KEY_IM_TALKTO);
		String card_name = data.getStringExtra(ConstantKey.KEY_IM_TALKTO_NAME);
		String card_headImg = data
				.getStringExtra(ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH);
		CardAttachment cardAtt = new CardAttachment(card_id, card_name,
				card_headImg);
		com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
				.createCustomMessage(talkToAccount, // 聊天对象的
													// ID，如果是单聊，为用户帐号，如果是群聊，为群组
													// ID
						sessionType, // 聊天类型，单聊或群组
						cardAtt);
		message.setPushContent(myPushNickName + ":[发了一张名片]");
		sendMessage(message);
	}

	/**
	 * 发送地理位置
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void sendLocation(final float latitude, final float longitude,
			String address) {
		com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
				.createLocationMessage(talkToAccount, // 聊天对象的
														// ID，如果是单聊，为用户帐号，如果是群聊，为群组
														// ID
						sessionType, // 聊天类型，单聊或群组
						latitude, // 纬度
						longitude, // 经度
						address // 地址信息描述
				);
		message.setPushContent(myPushNickName + ":[发了一个位置]");
		sendMessage(message);
	}

	/**
	 * 发送语音消息
	 */
	@Override
	public void sendAudioMessage(final String audioPath, final int secs) {
		com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
				.createAudioMessage(talkToAccount, // 聊天对象的
													// ID，如果是单聊，为用户帐号，如果是群聊，为群组
													// ID
						sessionType, // 聊天类型，单聊或群组
						new File(audioPath), // 音频文件
						secs * 1000 // 音频持续时间，单位是ms
				);
		message.setPushContent(myPushNickName + ":[发了一段语音]");
		sendMessage(message);
		// MrrckApplication.getInstance().playSound(5, 0);
	}

	/**
	 * 发送分享消息
	 */
	public void sendShareMessage(final String shareJson) {

		ShareMessage shareInfo = (ShareMessage) JsonUtil.jsonToObj(
				ShareMessage.class, shareJson);
		if (shareInfo != null
				&& shareInfo.getShareType() == ConstantKey.ShareStatus_CARD_STATE) {// 是分享卡片，发送卡片消息
			String card_id = shareInfo.getKey();
			String card_name = shareInfo.getShareTitle();
			String card_headImg = shareInfo.getShareImage();
			CardAttachment cardAtt = new CardAttachment(card_id, card_name,
					card_headImg);
			com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
					.createCustomMessage(talkToAccount, // 聊天对象的
														// ID，如果是单聊，为用户帐号，如果是群聊，为群组
														// ID
							sessionType, // 聊天类型，单聊或群组
							cardAtt);
			message.setPushContent(myPushNickName + ":[发了一张名片]");
			sendMessage(message);
		} else {
			ShareAttachment shareAtt = new ShareAttachment(shareJson);
			com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
					.createCustomMessage(talkToAccount, // 聊天对象的
														// ID，如果是单聊，为用户帐号，如果是群聊，为群组
														// ID
							sessionType, // 聊天类型，单聊或群组
							shareAtt);
			message.setPushContent(myPushNickName + ":[分享]");
			sendMessage(message);
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param photoPath
	 */
	private void CompressPic(String photoPath) {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				// 创建图片消息
				com.netease.nimlib.sdk.msg.model.IMMessage message = MessageBuilder
						.createImageMessage(talkToAccount, // 聊天对象的
															// ID，如果是单聊，为用户帐号，如果是群聊，为群组
															// ID
								sessionType, // 聊天类型，单聊或群组
								new File(result), // 图片文件对象
								"" // 文件显示名字，如果第三方 APP 不关注，可以为 null
						);
				message.setPushContent(myPushNickName + ":[发了一张图片]");
				sendMessage(message);
				super.onPostExecute(result);
			}

			@Override
			protected String doInBackground(String... arg0) {
				String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
				return photoPath;
			}

		}.execute(photoPath);
	}

	@Override
	protected void onStop() {
		super.onStop();
		String draftStr = EmotionHelper.getSendEmotion(ChatActivity.this,
				contentEdit.getText().toString());
		if (!Tool.isEmpty(draftStr)) {
			IMDraft draft = new IMDraft(XmppConstant.IM_CHAT_TALKTYPE_SINGLE,
					talkToID, draftStr);
			LogUtil.d("hl", "保存草稿" + draftStr);
			MsgDataBase.getInstance().saveIMDraft(draft);
		} else {
			MsgDataBase.getInstance().delDraftRecord(
					XmppConstant.IM_CHAT_TALKTYPE_SINGLE, talkToID);
		}
		sendBroadcast(new Intent(BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE));// 发广播刷新消息页面
		isResume = false;
	}

	@Override
	public void doPullDownToRefresh() {
	}

	public void sendMessage(com.netease.nimlib.sdk.msg.model.IMMessage message) {
		if (!isAllowSendMessage(message)) {
			return;
		}
		// 额外添加自己的信息
		Map<String, Object> content = new HashMap<String, Object>(4);
		content.put("nickName", myPushNickName);
		content.put("clientHeadPicUrl", AppContext.getInstance().getUserInfo()
				.getClientThumbHeadPicUrl());
		content.put("userId", AppContext.getInstance().getUserInfo().getId()
				+ "");
		content.put("groupId", talkToID);
		message.setRemoteExtension(content);
		message.setPushPayload(content);
		CustomMessageConfig config = new CustomMessageConfig();
		config.enablePushNick = false;
		message.setConfig(config);
		NIMClient.getService(MsgService.class).sendMessage(message, false);
		onMsgSend(message);
	}

	private boolean isAllowSendMessage(
			com.netease.nimlib.sdk.msg.model.IMMessage message) {
		return true;
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_CLEAR_GROUPMESSAGE);
		registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_IM_CLEAR_GROUPMESSAGE.equals(intent
					.getAction())) {
				if (items != null) {
					items.clear();
					setMessageLoader();
				}
			}
		}
	};

	@Override
	public void DoOnRevokeMessage(IMMessage item) {
		if (item == null) {
			return;
		}

		String nick = "";
		if (item.getSessionType() == SessionTypeEnum.Team) {
			// 先从成员DB取，
			IMYxUserInfo user = MsgDataBase.getInstance().getYXUserById(
					item.getFromAccount());
			Map<String, Object> ext = item.getRemoteExtension();
			if (user != null) {
				nick = user.getNickName();
			} else if (ext != null && ext.containsKey("nickName")) {
				nick = ext.get("nickName").toString();
			} else {
				nick = TeamDataCache.getInstance().getTeamMemberDisplayNameYou(
						item.getSessionId(), item.getFromAccount());
			}
		} else if (item.getSessionType() == SessionTypeEnum.P2P) {
			nick = item.getFromAccount().equals(
					AppContext.getInstance().getUserInfo()
							.getLeanCloudUserName()) ? AppContext.getInstance()
					.getUserInfo().getNickName() : "对方";
		}

		TipAttachment attch = new TipAttachment(nick + "撤回了一条消息");
		com.netease.nimlib.sdk.msg.model.IMMessage msg = MessageBuilder
				.createCustomMessage(item.getSessionId(), // 聊天对象的
						// ID，如果是单聊，为用户帐号，如果是群聊，为群组
						// ID
						item.getSessionType(), // 聊天类型，单聊或群组
						attch);

		// com.netease.nimlib.sdk.msg.model.IMMessage msg = MessageBuilder
		// .createTipMessage(item.getSessionId(), item.getSessionType());
		msg.setContent(nick + "撤回了一条消息");
		msg.setPushContent(nick + "撤回了一条消息");
		Map<String, Object> content = new HashMap<String, Object>(4);
		content.put("nickName", myPushNickName);
		content.put("clientHeadPicUrl", AppContext.getInstance().getUserInfo()
				.getClientThumbHeadPicUrl());
		content.put("userId", AppContext.getInstance().getUserInfo().getId()
				+ "");
		content.put("groupId", talkToID);
		msg.setRemoteExtension(content);
		msg.setPushPayload(item.getPushPayload());
		CustomMessageConfig config = new CustomMessageConfig();
		config.enablePushNick = false;
		config.enableUnreadCount = false;
		msg.setConfig(config);
		msg.setStatus(MsgStatusEnum.success);
		NIMClient.getService(MsgService.class).sendMessage(msg, true);
		onMsgSend(msg);
	}
}
