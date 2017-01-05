package com.meiku.dev.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.meiku.dev.R;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.HomeActivity;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.ui.myshow.WorkDetailNewActivity;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;

/**
 * 极光推送自定义消息接收广播
 */
public class JPushMessageReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			String pushStr = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + pushStr);
//			if (JsonUtil.getJSONType(pushStr) == JSON_TYPE.JSON_TYPE_OBJECT) {
//				JGMessage jgMsg = (JGMessage) JsonUtil.jsonToObj(
//						JGMessage.class, pushStr);
//				if (jgMsg.getNotifyType() == 1) {// 自定义消息
//
//					switch (jgMsg.getReciveType()) {
//					case ConstantKey.JGPUSH_MESSAGE_RES_ALL:
//						if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
//							return;
//						}
//						IMMessage newMessage = new IMMessage();
//						newMessage.setMsgType(XmppConstant.MSGTYPE_RCV);
//
//						newMessage.setFriendSubJid(jgMsg.getUserId());
//						newMessage.setMySubJid(AppContext.getInstance()
//								.getUserInfo().getId());
//						newMessage.setFriendnickname(jgMsg.getNickName());
//						newMessage.setMynickname(AppContext.getInstance()
//								.getUserInfo().getNickName());
//
//						newMessage.setFriendheadimg(jgMsg.getHeadImg());
//						newMessage.setMyheadimg(AppContext.getInstance()
//								.getUserInfo().getClientHeadPicUrl());
//						newMessage.setType(XmppConstant.MSG_CONTENT_SHARE);
//						newMessage.setContent(XmppConstant.CHAT_MSG_SHARE);
//						newMessage.setFilePath(jgMsg.getShareMessage());
//						LogUtil.d("hl", "保存推送的分享消息——" + jgMsg.getShareMessage());
//						boolean isSuccess = MsgDataBase.getInstance()
//								.saveIMMsgTable(newMessage);
//						// 生成通知
//						saveNotice(jgMsg.getUserId(), newMessage.getContent());
//						if (isSuccess) {
//							Intent broadCastIntent = new Intent(
//									BroadCastAction.ACTION_IM_NEW_MESSAGE);
//							intent.putExtra(XmppConstant.IMMESSAGE_KEY,
//									newMessage);
//							MrrckApplication.getInstance().sendBroadcast(
//									broadCastIntent);// 发送广播
//						}
//						if (Util.isAppInBackground(context)) {
//							if (JsonUtil.getJSONType(jgMsg.getShareMessage()) == JSON_TYPE.JSON_TYPE_OBJECT) {
//								ShareMessage sharemsg = (ShareMessage) JsonUtil
//										.jsonToObj(ShareMessage.class,
//												jgMsg.getShareMessage());
//								Intent gotoIntent = new Intent();
//								switch (sharemsg.getShareType()) {
//								case ConstantKey.ShareStatus_SAISHI:
//									gotoIntent
//											.putExtra("postsId",
//													Integer.parseInt(sharemsg
//															.getKey()));
//									ComponentName cn = new ComponentName(
//											context, ShowMainActivity.class);
//									gotoIntent.setComponent(cn);
//									NotificationUtil
//											.showShareMessageNotification(
//													context, jgMsg.getUserId(),
//													gotoIntent,
//													sharemsg.getShareTitle(),
//													sharemsg.getShareContent(),
//													null);
//									break;
//								case ConstantKey.ShareStatus_SHOWWORK:
//									gotoIntent
//											.putExtra("SignupId",
//													Integer.parseInt(sharemsg
//															.getKey()));
//									ComponentName cn2 = new ComponentName(
//											context,
//											NewWorkDetailActivity.class);
//									gotoIntent.setComponent(cn2);
//									NotificationUtil
//											.showShareMessageNotification(
//													context, jgMsg.getUserId(),
//													gotoIntent,
//													sharemsg.getShareTitle(),
//													sharemsg.getShareContent(),
//													null);
//									break;
//								case ConstantKey.ShareStatus_NOSHOWWORK:
//									gotoIntent
//											.putExtra("SignupId",
//													Integer.parseInt(sharemsg
//															.getKey()));
//									ComponentName cn3 = new ComponentName(
//											context,
//											WorkDetailNewActivity.class);
//									gotoIntent.setComponent(cn3);
//									NotificationUtil
//											.showShareMessageNotification(
//													context, jgMsg.getUserId(),
//													gotoIntent,
//													sharemsg.getShareTitle(),
//													sharemsg.getShareContent(),
//													null);
//									break;
//								default:
//									break;
//								}
//							}
//
//						}
//						break;
//
//					default:
//						break;
//					}
//
//				} else {
//
//				}
//			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			// int notifactionId = bundle
			// .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			// 有重复登录，来通知就退出登录（不是点击通知才退出）
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.d(TAG, "extras=" + extras);
			JSONObject extrasJson;
			try {
				extrasJson = new JSONObject(extras);
				String PushType = extrasJson.optString("PushType");// 11=重复登录
				String PushKey = extrasJson.optString("PushKey");
				if (Tool.isEmpty(PushType)) {
					ToastUtil.showShortToast("推送参数有误！");
					return;
				}
				if (Util.isAppRunning(context)
						&& !Tool.isEmpty(PushType)
						&& String.valueOf(ConstantKey.PUSHStatus_LOGOUT)
								.equals(PushType)) {
					if (AppContext.getInstance().isHasLogined()
							&& String.valueOf(
									AppContext.getInstance().getUserInfo()
											.getId()).equals(PushKey)) {
						LogUtil.d("hl",
								"_____重复登陆(由推送消息)________________________");
						context.sendBroadcast(new Intent(
								BroadCastAction.ACTION_RELOGINT_DOLOUOUT)
								.putExtra("RELOGIN", true));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			openNotification(context, bundle);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			// if (!connected) {
			// ToastUtil.showLongToast(context.getResources().getString(
			// R.string.needCheckNetwork));
			//
			// } else {
			// // MrrckApplication.stopMsgServices();
			// // new Handler().postDelayed(new Runnable() {
			// //
			// // @Override
			// // public void run() {
			// // MrrckApplication.getInstance().doMsgServerLogin();
			// // }
			// // }, 2000);
			// }
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	/**
	 * 保存未读的通知消息
	 */
//	public static void saveNotice(int friendid, String body) {
////		Notice notice = new Notice();
////		notice.setTitle("会话信息");
////		notice.setNoticeType(XmppConstant.NOTICE_CHAT_MSG);
////		notice.setContent(body);
////		notice.setFriendsubid(friendid);
////		notice.setStatus(XmppConstant.NOTICE_UNREAD);
////		notice.setMysubid(AppContext.getInstance().getUserInfo().getId());
////		notice.setNoticeTime(DateUtil.getCurFormate(""));
////		MsgDataBase.getInstance().saveIMNoticeTable(notice);
//	}

	private void openNotification(Context context, Bundle bundle) {
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Log.d(TAG, "extras=" + extras);
		String PushType = "";
		String PushKey = "";
		String PushUrl = "";
		try {
			JSONObject extrasJson = new JSONObject(extras);
			PushType = extrasJson.optString("PushType");// 1卡片（个人名片）,2资讯、帖子,3赛事活动
														// ,4参赛秀场作品,5=一般作
														// 品,6=直接弹出窗口展示内容，7=打开H5",8=重复登录
			PushKey = extrasJson.optString("PushKey");
			PushUrl = extrasJson.optString("PushUrl");
			if (Tool.isEmpty(PushType)) {
				ToastUtil.showShortToast("推送参数有误！");
				return;
			}
			if (Util.isAppRunning(context)) {
				switch (Integer.parseInt(PushType)) {
				case ConstantKey.ShareStatus_CARD_STATE:
					context.startActivity(new Intent(context,
							PersonShowActivity.class).putExtra("toUserId",
							String.valueOf(PushKey)).setFlags(
							Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				case ConstantKey.ShareStatus_NEWS_STATE:
					context.startActivity(new Intent(context,
							PostDetailNewActivity.class).putExtra(
							ConstantKey.KEY_POSTID, String.valueOf(PushKey))
							.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				case ConstantKey.ShareStatus_SAISHI:
					context.startActivity(new Intent(context,
							ShowMainActivity.class).putExtra("postsId",
							Integer.parseInt(PushKey)).setFlags(
							Intent.FLAG_ACTIVITY_NEW_TASK));

					break;
				case ConstantKey.ShareStatus_SHOWWORK:
					context.startActivity(new Intent(context,
							NewWorkDetailActivity.class).putExtra("SignupId",
							Integer.parseInt(PushKey)).setFlags(
							Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				case ConstantKey.ShareStatus_NOSHOWWORK:
					context.startActivity(new Intent(context,
							WorkDetailNewActivity.class).putExtra("SignupId",
							Integer.parseInt(PushKey)).setFlags(
							Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				case ConstantKey.PUSHStatus_OPENH5:
					if (!Tool.isEmpty(PushUrl)) {
						Intent intent = new Intent(context,
								WebViewActivity.class);
						intent.putExtra("webUrl", PushUrl);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					} else {
						ToastUtil.showShortToast(context.getResources()
								.getString(R.string.errorUrl));
					}
					break;
				case ConstantKey.TAG_MESSAGE_SINGLE:
				case ConstantKey.TAG_MESSAGE_GROUP:
					if (Util.isAppInBackground(context)) {
						Intent intent = new Intent(context, HomeActivity.class);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.setAction(Intent.ACTION_MAIN);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						context.startActivity(intent);
					}
					break;
				// case ConstantKey.PUSHStatus_LOGOUT:
				// if (AppContext.getInstance().isHasLogin()
				// && String.valueOf(
				// AppContext.getInstance().getUserInfo()
				// .getId()).equals(PushKey)) {
				// context.sendBroadcast(new Intent(
				// BroadCastAction.ACTION_DOLOUOUT));
				// }
				//
				// break;
				default:
					break;
				}
			} else {
				Intent launchIntent = context.getPackageManager()
						.getLaunchIntentForPackage(context.getPackageName());
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				Bundle args = new Bundle();
				args.putString("PushType", PushType);
				args.putString("PushKey", PushKey);
				args.putString("PushUrl", PushUrl);
				launchIntent.putExtra(ConstantKey.NOTIFICATION_BUNDLE, args);
				context.startActivity(launchIntent);
			}
		} catch (Exception e) {
			Log.d(TAG, "Unexpected: extras is not a valid json", e);
			return;
		}
	}
}
