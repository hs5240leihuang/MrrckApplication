package com.meiku.dev.yunxin.recent;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqBaseStr;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.utils.CompressUtil;
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.Des3Util;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EmotionTextView;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.volleyextend.StringJsonRequest;
import com.meiku.dev.yunxin.TViewHolder;
import com.meiku.dev.yunxin.TeamDataCache;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

public abstract class RecentViewHolder extends TViewHolder implements
		OnClickListener {
	protected FrameLayout portraitPanel;
	protected LinearLayout imgHeadLayout;
	protected TextView tvNickname;
	protected EmotionTextView tvMessage;
	protected TextView tvDatetime;
	// 消息发送错误状态标记，目前没有逻辑处理
	protected ImageView imgMsgStatus;
	protected RecentContact recent;
	protected View bottomLine;
	protected View topLine;
	// 未读红点（一个占坑，一个全屏动画）
	protected DropFake tvUnread;
	private ImageView imgUnreadExplosion;

	protected abstract String getContent();

	public void refresh(Object item) {
		boolean flag = recent != null && recent.getUnreadCount() > 0;
		recent = (RecentContact) item;
		flag = flag && recent.getUnreadCount() == 0; // 未读数从N->0执行爆裂动画
		updateBackground();
		loadPortrait();
		updateMsgLabel();
		updateNewIndicator();
		if (flag) {
			Object o = DropManager.getInstance().getCurrentId();
			if (o instanceof String && o.equals("0")) {
				imgUnreadExplosion.setImageResource(R.drawable.explosion);
				imgUnreadExplosion.setVisibility(View.VISIBLE);
				((AnimationDrawable) imgUnreadExplosion.getDrawable()).start();
			}
		}
	}

	public void refreshCurrentItem() {
		if (recent != null) {
			refresh(recent);
		}
	}

	private void updateBackground() {
		topLine.setVisibility(isFirstItem() ? View.GONE : View.VISIBLE);
		bottomLine.setVisibility(isLastItem() ? View.VISIBLE : View.GONE);
		if (Tool.isEmpty(recent.getTag()) || recent.getTag() == 0) {
			view.setBackgroundResource(R.drawable.nim_list_item_selector);
		} else {
			view.setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter);
		}
	}

	protected void loadPortrait() {
		if (recent.getSessionType() == SessionTypeEnum.P2P) {// 单聊
			updateNickLabel("美库用户");
			final MyRectDraweeView img_head = new MyRectDraweeView(context);
			imgHeadLayout.removeAllViews();
			imgHeadLayout.addView(img_head, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			IMYxUserInfo yxuser = MsgDataBase.getInstance().getYXUserById(
					recent.getContactId());
			Integer friendId = AppContext.getFriendTalkIdMap().get(
					recent.getContactId());
			if (AppContext.getFriendTalkIdMap().containsKey(
					recent.getContactId())
					&& friendId != null
					&& AppContext.getRemarkMap().containsKey(friendId)) {
				String friendName = AppContext.getRemarkMap().get(friendId);
				updateNickLabel(friendName);
				String friendImg = AppContext.getHeadImgMap().get(friendId);
				img_head.setUrlOfImage(friendImg);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", friendId);
				map.put("nickName", friendName);
				map.put("clientHeadPicUrl", friendImg);
				upDateRecentExtension(map);
			} else if (yxuser != null) {
				updateNickLabel(yxuser.getNickName());
				img_head.setUrlOfImage(yxuser.getUserHeadImg());
			} else if (null != recent.getExtension()) {
				Map<String, Object> ext = recent.getExtension();
				if (ext != null && ext.containsKey("nickName")) {
					updateNickLabel(ext.get("nickName").toString());
				}
				if (ext != null && ext.containsKey("clientHeadPicUrl")) {
					img_head.setUrlOfImage(ext.get("clientHeadPicUrl")
							.toString());
				}
			}
		} else if (recent.getSessionType() == SessionTypeEnum.Team) {// 群聊
			updateNickLabel("群消息");
			final MyRectDraweeView img_head = new MyRectDraweeView(context);
			imgHeadLayout.removeAllViews();
			imgHeadLayout.addView(img_head, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			Integer groupId = AppContext.getGroupTalkIDMap().get(
					recent.getContactId());
			if (groupId != null
					&& AppContext.getGroupMap().containsKey(groupId)) {
				String groupName = AppContext.getGroupNameMap().get(groupId);
				updateNickLabel(groupName);
				String groupImg = AppContext.getGroupMap().get(groupId);
				img_head.setUrlOfImage(groupImg);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("groupId", groupId);
				map.put("groupName", groupName);
				map.put("groupImg", groupImg);
				upDateRecentExtension(map);
			} else if (null != recent.getExtension()) {
				Map<String, Object> ext = recent.getExtension();
				if (ext != null && ext.containsKey("groupName")) {
					updateNickLabel(ext.get("groupName").toString());
				}
				if (ext != null && ext.containsKey("groupImg")) {
					img_head.setUrlOfImage(ext.get("groupImg").toString());
				}
			} else {
				String groupName = TeamDataCache.getInstance().getTeamName(
						recent.getContactId());
				updateNickLabel(groupName);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("groupName", groupName);
				upDateRecentExtension(map);
				checkGroupInfoByOtherId(recent.getContactId());
			}
		}
	}

	private void upDateRecentExtension(Map<String, Object> attMap) {
		Map<String, Object> mapRec = recent.getExtension();
		if (mapRec == null) {
			mapRec = new HashMap<String, Object>();
		}
		mapRec.putAll(attMap);
		recent.setExtension(mapRec);
	}

	public void checkGroupInfoByOtherId(String otherId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("otherId", otherId);
		req.setHeader(new ReqHead(AppConfig.GP_18052));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		if (!NetworkTools.isNetworkAvailable(MrrckApplication.getInstance())) {
			return;
		}
		Listener<String> successLis = new Listener<String>() {
			@Override
			public void onResponse(String resp) {
				try {
					ReqBase basejson = new ReqBase();
					if (AppConfig.IS_SECRET) {// APP端采用了加密
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						basejson.setHeader(reqStr.getHeader());
						basejson.setBody(JsonUtil.String2Object(Des3Util
								.decode(reqStr.getBody())));
					} else {
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						if (AppConfig.IS_COMPRESS
								&& !Tool.isEmpty(reqStr)
								&& !Tool.isEmpty(reqStr.getHeader())
								&& AppConfig.DATA_COMPRESS.equals(reqStr
										.getHeader().getZipFlag())) {
							basejson.setHeader(reqStr.getHeader());
							basejson.setBody(JsonUtil
									.String2Object(CompressUtil
											.DecompressBody(reqStr.getBody())));
						} else {
							basejson = (ReqBase) JsonUtil.jsonToObj(
									ReqBase.class, resp);
						}
					}
					ReqHead head = basejson.getHeader();
					if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
						MrrckApplication.getInstance().checkDoubleLoginStatus(
								basejson.getHeader().getJsessionId());
						if (basejson.getBody().get("data") != null
								&& basejson.getBody().get("data").toString()
										.length() > 4) {
							GroupEntity groupSimpleInfo = (GroupEntity) JsonUtil
									.jsonToObj(GroupEntity.class, basejson
											.getBody().get("data").toString());
							if (groupSimpleInfo != null
									&& !Tool.isEmpty(groupSimpleInfo.getId())) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("groupId", groupSimpleInfo.getId());
								map.put("groupName",
										groupSimpleInfo.getGroupName());
								map.put("groupImg", groupSimpleInfo
										.getClientThumbGroupPhoto());
								upDateRecentExtension(map);
								context.sendBroadcast(new Intent(
										BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE));// 发广播刷新消息页面
							}
						}
					} else {
					}
				} catch (Exception e) {
					ToastUtil.showShortToast(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ErrorListener errLis = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
			}
		};
		String mreqBody;
		// 是否压缩
		if (AppConfig.IS_COMPRESS
				&& !Tool.isEmpty(req.getBody())
				&& req.getBody().toString().length() > CompressUtil.BYTE_MIN_LENGTH) {
			mreqBody = CompressUtil.CompressBody(req);
		} else {// 压缩暂不使用加密
			if (AppConfig.IS_SECRET) {
				mreqBody = Des3Util.reqToSecret(req);
			} else {
				mreqBody = JsonUtil.objToJson(req);
			}
		}
		StringJsonRequest request = new StringJsonRequest(AppConfig.DOMAIN
				+ AppConfig.PUBLICK_NEARBY_GROUP, successLis, mreqBody, errLis);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantKey.ReqNetTimeOut,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MrrckApplication.getInstance().addToRequestQueue(request);
	}

	private void updateNewIndicator() {
		int unreadNum = recent.getUnreadCount();
		tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
		tvUnread.setText(unreadCountShowRule(unreadNum));
	}

	private void updateMsgLabel() {
		tvMessage.setText(EmotionHelper.getLocalEmotion(context, getContent()));
		MsgStatusEnum status = recent.getMsgStatus();
		switch (status) {
		case fail:
			imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small);
			imgMsgStatus.setVisibility(View.VISIBLE);
			break;
		case sending:
			imgMsgStatus
					.setImageResource(R.drawable.nim_recent_contact_ic_sending);
			imgMsgStatus.setVisibility(View.VISIBLE);
			break;
		default:
			imgMsgStatus.setVisibility(View.GONE);
			break;
		}
		tvDatetime.setText(DateTimeUtil.getTimeShowString(recent.getTime(),
				true));
	}

	protected void updateNickLabel(String nick) {
		int labelWidth = ScreenUtil.SCREEN_WIDTH;
		labelWidth -= ScreenUtil
				.dip2px(MrrckApplication.getInstance(), 50 + 70); // 减去固定的头像和时间宽度
		if (labelWidth > 0) {
			tvNickname.setMaxWidth(labelWidth);
		}
		tvNickname.setText(nick);
	}

	protected int getResId() {
		return R.layout.nim_recent_contact_list_item;
	}

	public void inflate() {
		this.portraitPanel = (FrameLayout) view
				.findViewById(R.id.portrait_panel);
		this.imgHeadLayout = (LinearLayout) view.findViewById(R.id.img_head);
		this.tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
		this.tvMessage = (EmotionTextView) view.findViewById(R.id.tv_message);
		this.tvUnread = (DropFake) view.findViewById(R.id.unread_number_tip);
		this.imgUnreadExplosion = (ImageView) view
				.findViewById(R.id.unread_number_explosion);
		this.tvDatetime = (TextView) view.findViewById(R.id.tv_date_time);
		this.imgMsgStatus = (ImageView) view.findViewById(R.id.img_msg_status);
		this.bottomLine = view.findViewById(R.id.bottom_line);
		this.topLine = view.findViewById(R.id.top_line);
		this.tvUnread.setClickListener(new DropFake.ITouchListener() {
			@Override
			public void onDown() {
				DropManager.getInstance().setCurrentId(recent);
				DropManager.getInstance().getDropCover()
						.down(tvUnread, tvUnread.getText());
			}

			@Override
			public void onMove(float curX, float curY) {
				DropManager.getInstance().getDropCover().move(curX, curY);
			}

			@Override
			public void onUp() {
				DropManager.getInstance().getDropCover().up();
			}
		});
	}

	protected String unreadCountShowRule(int unread) {
		unread = Math.min(unread, 99);
		return String.valueOf(unread);
	}

	protected RecentContactsCallback getCallback() {
		return ((RecentContactAdapter) getAdapter()).getCallback();
	}

	@Override
	public void onClick(View v) {
	}
}
