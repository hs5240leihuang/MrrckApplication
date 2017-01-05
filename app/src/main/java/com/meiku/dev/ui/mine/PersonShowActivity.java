package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.bean.PersonalPhotoEntity;
import com.meiku.dev.bean.PersonalTagEntity;
import com.meiku.dev.bean.PostsSignupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UserHomeEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.chat.TaGroupActivity;
import com.meiku.dev.ui.im.ChatActivity;
import com.meiku.dev.ui.im.GoodFriendSetActivity;
import com.meiku.dev.ui.im.JuBaoActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.SystemBarTintManager;
import com.meiku.dev.utils.TextLongClickListener;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonEditDialog;
import com.meiku.dev.views.CommonEditDialog.EditClickOkListener;
import com.meiku.dev.views.FlowLayout;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MessageDialog;
import com.meiku.dev.views.MessageDialog.messageListener;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MyPullToRefreshScrollView;
import com.meiku.dev.views.MyPullToRefreshScrollView.ScrollListener;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.yunxin.TipAttachment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 查看个人的主页信息
 * 
 * @author 库
 * 
 */
public class PersonShowActivity extends BaseActivity implements OnClickListener {

	private MyPullToRefreshScrollView mypulltorefresh;
	private TextView txt_distance, txt_name, txt_logintime, txt_age, txt_job,
			txt_sign, txt_post1, txt_post1_con, txt_post2, txt_post2_con,
			txt_citys, txt_loves, txt_remark, txt_relation, txt_regisdate,
			btnTalk, btnAddfriend, btnBlack, txt_userCode, txt_beizhu;
	private LinearLayout lin_head;// 头像
	private LinearLayout lin_background;// 背景墙
	public static String TO_USERID_KEY = "toUserId";// 其他用户ID的KEY
	private List<String> messageList = new ArrayList<String>();
	private UserHomeEntity userBean;
	private TextView more;
	private Animation mFadeInScale, mFadeOutScale;
	private String clientHeadBigPicUrl;
	private LinearLayout lin_beizhu;
	private String nickName;// 1.好友自己的昵称2.用户备注过的
	private String clientHeadPicUrl;
	private final int reqCode_bianji = 10;
	protected int reqCode_more = 12;
	private final int reqCode_selectBg = 13;
	private int WATCHPHOTO = 14;
	private final int reqCodeFive = 500;
	private final int reqCodeSix = 600;
	private final int reqCodeSeven = 700;
	private String sourceType = "1";
	private String toUserId;
	private View titleEmptyTop;
	private int statusBarHeight;
	private FlowLayout layout_tags;
	private SystemBarTintManager tintManager;
	private TextView tv_vip;
	private String clientBackgroundId;

	private LinearLayout layout_post, layout_post1, layout_post2, bottom_bar;
	private MyGridView gridview_myshow, gridview_mygroup, gridview_myPhotos;
	private LinearLayout layout_myshow, layout_group, layout_myPhotos;
	// 秀场数据
	private CommonAdapter<PostsSignupEntity> adapter_myshow;
	private List<PostsSignupEntity> data_showList = new ArrayList<PostsSignupEntity>();
	// 群组数据
	private List<GroupEntity> data_groupList = new ArrayList<GroupEntity>();
	private CommonAdapter<GroupEntity> adapter_mygroup;
	// 相册数据
	private List<PersonalPhotoEntity> data_myPhotos = new ArrayList<PersonalPhotoEntity>();
	private CommonAdapter<PersonalPhotoEntity> adapter_myPhotos;
	private MySimpleDraweeView iv_background;
	private LinearLayout layout_goshare;
	private String shareUrl;
	private LinearLayout layout_goEdit;
	private String shareTitle;
	private String shareContent;
	private String shareImgUrl;
	private String leanCloudUserName;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_person_show;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		setTransparentStatusBar();
		mypulltorefresh = (MyPullToRefreshScrollView) findViewById(R.id.mypulltorefresh);
		lin_background = (LinearLayout) findViewById(R.id.lin_background);
		titleEmptyTop = (View) findViewById(R.id.titleEmptyTop);
		statusBarHeight = ScreenUtil
				.getStatusBarHeight(PersonShowActivity.this);
		titleEmptyTop.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, statusBarHeight));
		lin_head = (LinearLayout) findViewById(R.id.lin_head);
		layout_tags = (FlowLayout) findViewById(R.id.layout_tags);
		lin_beizhu = (LinearLayout) findViewById(R.id.lin_beizhu);
		txt_distance = (TextView) findViewById(R.id.txt_distance);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_userCode = (TextView) findViewById(R.id.txt_userCode);
		txt_logintime = (TextView) findViewById(R.id.txt_logintime);
		txt_age = (TextView) findViewById(R.id.txt_age);
		txt_job = (TextView) findViewById(R.id.txt_job);
		txt_sign = (TextView) findViewById(R.id.txt_sign);
		txt_beizhu = (TextView) findViewById(R.id.txt_beizhu);
		layout_goshare = (LinearLayout) findViewById(R.id.layout_goshare);
		layout_goEdit = (LinearLayout) findViewById(R.id.layout_goEdit);
		txt_sign.setOnLongClickListener(new TextLongClickListener(
				PersonShowActivity.this, txt_sign));
		// 相册
		layout_myPhotos = (LinearLayout) findViewById(R.id.layout_myPhotos);
		gridview_myPhotos = (MyGridView) findViewById(R.id.gridview_myPhotos);
		adapter_myPhotos = new CommonAdapter<PersonalPhotoEntity>(
				PersonShowActivity.this, R.layout.item_persionshow_myshow,
				data_myPhotos) {
			@Override
			public void convert(ViewHolder viewHolder, PersonalPhotoEntity t) {
				LinearLayout lin_img = viewHolder.getView(R.id.lin_img);
				MySimpleDraweeView img = new MySimpleDraweeView(PersonShowActivity.this);
				lin_img.removeAllViews();
				lin_img.addView(img, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img.setUrlOfImage(t.getClientThumbFileUrl());
				viewHolder.getView(R.id.tv).setVisibility(View.GONE);
			}
		};
		gridview_myPhotos.setAdapter(adapter_myPhotos);
		gridview_myPhotos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onClick(layout_myPhotos);
			}
		});
		// 秀场
		layout_myshow = (LinearLayout) findViewById(R.id.layout_myshow);
		gridview_myshow = (MyGridView) findViewById(R.id.gridview_myshow);
		adapter_myshow = new CommonAdapter<PostsSignupEntity>(
				PersonShowActivity.this, R.layout.item_persionshow_myshow,
				data_showList) {
			@Override
			public void convert(ViewHolder viewHolder, PostsSignupEntity t) {
				LinearLayout lin_img = viewHolder.getView(R.id.lin_img);
				MySimpleDraweeView img = new MySimpleDraweeView(PersonShowActivity.this);
				lin_img.removeAllViews();
				lin_img.addView(img, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img.setUrlOfImage(t.getClientPhotoFileUrl());
				viewHolder.setText(R.id.tv, t.getName());
			}
		};
		gridview_myshow.setAdapter(adapter_myshow);
		gridview_myshow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onClick(layout_myshow);
			}
		});
		// 群组
		layout_group = (LinearLayout) findViewById(R.id.layout_group);
		gridview_mygroup = (MyGridView) findViewById(R.id.gridview_mygroup);
		adapter_mygroup = new CommonAdapter<GroupEntity>(
				PersonShowActivity.this, R.layout.item_persionshow_group,
				data_groupList) {

			@Override
			public void convert(ViewHolder viewHolder, GroupEntity t) {
				LinearLayout lin_img = viewHolder.getView(R.id.lin_img);
				MyRectDraweeView img = new MyRectDraweeView(PersonShowActivity.this);
				lin_img.removeAllViews();
				lin_img.addView(img, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img.setUrlOfImage(t.getClientThumbGroupPhoto());
				viewHolder.setText(R.id.tv, t.getGroupName());
			}
		};
		gridview_mygroup.setAdapter(adapter_mygroup);
		gridview_mygroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onClick(layout_group);
			}
		});
		// 帖子
		layout_post = (LinearLayout) findViewById(R.id.layout_post);
		layout_post1 = (LinearLayout) findViewById(R.id.layout_post1);
		layout_post2 = (LinearLayout) findViewById(R.id.layout_post2);
		txt_post1 = (TextView) findViewById(R.id.txt_post1);
		txt_post1_con = (TextView) findViewById(R.id.txt_post1_con);
		txt_post2 = (TextView) findViewById(R.id.txt_post2);
		txt_post2_con = (TextView) findViewById(R.id.txt_post2_con);

		txt_citys = (TextView) findViewById(R.id.txt_citys);
		txt_citys.setOnLongClickListener(new TextLongClickListener(
				PersonShowActivity.this, txt_citys));
		txt_loves = (TextView) findViewById(R.id.txt_loves);
		txt_loves.setOnLongClickListener(new TextLongClickListener(
				PersonShowActivity.this, txt_loves));
		txt_remark = (TextView) findViewById(R.id.txt_remark);
		txt_remark.setOnLongClickListener(new TextLongClickListener(
				PersonShowActivity.this, txt_remark));
		txt_relation = (TextView) findViewById(R.id.txt_relation);
		txt_relation.setOnLongClickListener(new TextLongClickListener(
				PersonShowActivity.this, txt_relation));
		txt_regisdate = (TextView) findViewById(R.id.txt_regisdate);
		txt_regisdate.setOnLongClickListener(new TextLongClickListener(
				PersonShowActivity.this, txt_regisdate));
		bottom_bar = (LinearLayout) findViewById(R.id.bottomBar);
		btnTalk = (TextView) findViewById(R.id.btnTalk);
		btnAddfriend = (TextView) findViewById(R.id.btnAddfriend);
		btnBlack = (TextView) findViewById(R.id.btnBlack);
		more = (TextView) findViewById(R.id.goEdit);
		tv_vip = (TextView) findViewById(R.id.tv_vip);
		// initPicAnim();
	}

	/**
	 * 透明状态栏
	 */
	private void setTransparentStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(Color.TRANSPARENT);
	}

	/**
	 * 背景墙动画
	 */
	private void initPicAnim() {
		mFadeInScale = AnimationUtils.loadAnimation(this,
				R.anim.welcome_fade_in_scale);
		mFadeInScale.setRepeatCount(Animation.INFINITE);
		mFadeInScale.setRepeatMode(Animation.REVERSE);
		mFadeInScale.setDuration(8000);
		mFadeInScale.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				iv_background.startAnimation(mFadeOutScale);
			}
		});
		mFadeOutScale = AnimationUtils.loadAnimation(this,
				R.anim.welcome_fade_out_scale);
		mFadeOutScale.setRepeatCount(Animation.INFINITE);
		mFadeOutScale.setRepeatMode(Animation.REVERSE);
		mFadeOutScale.setDuration(8000);
		mFadeOutScale.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				iv_background.startAnimation(mFadeInScale);
			}
		});
		iv_background.startAnimation(mFadeInScale);
	}

	@Override
	public void initValue() {
		getShareurl();
		messageList.add("拉黑");
		messageList.add("举报");
		messageList.add("取消");
		toUserId = getIntent().getStringExtra(TO_USERID_KEY);
		if (Tool.isEmpty(toUserId)) {
			toUserId = AppContext.getInstance().getUserInfo().getId() + "";
			bottom_bar.setVisibility(View.GONE);
		}
		if ((AppContext.getInstance().getUserInfo().getId() + "")
				.equals(toUserId)) {
			bottom_bar.setVisibility(View.GONE);
		}
		getUserInfo(toUserId);
	}

	// 获取用户信息
	public void getUserInfo(String toUserid) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("toUserId", toUserId);
		map.put("longitude", MrrckApplication.getLongitudeStr());
		map.put("latitude", MrrckApplication.getLaitudeStr());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_USER_SHOW_NEW));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PERSONAL_REQUEST_MAPPING, reqBase, true);
	}

	private void showBottom() {
		findViewById(R.id.view_a).setVisibility(View.GONE);
		findViewById(R.id.view_b).setVisibility(View.GONE);
		btnAddfriend.setVisibility(View.GONE);
		btnBlack.setVisibility(View.GONE);
		txt_relation.setText("关注");
	}

	@Override
	public void bindListener() {
		findViewById(R.id.layout_goback).setOnClickListener(this);
		lin_beizhu.setOnClickListener(this);
		lin_background.setOnClickListener(this);
		lin_head.setOnClickListener(this);
		layout_post.setOnClickListener(this);
		findViewById(R.id.btnTalk).setOnClickListener(this);
		findViewById(R.id.btnAddfriend).setOnClickListener(this);
		findViewById(R.id.btnBlack).setOnClickListener(this);
		layout_myshow.setOnClickListener(this);
		layout_group.setOnClickListener(this);
		layout_myPhotos.setOnClickListener(this);
		mypulltorefresh.addScrollChangeLiatener(new ScrollListener() {

			@Override
			public void scrollChange(int l, int t, int oldl, int oldt) {
				if (t > ScreenUtil.dip2px(PersonShowActivity.this, 150)) {
					tintManager.setTintColor(Color.parseColor("#66000000"));
				} else {
					tintManager.setTintColor(Color.TRANSPARENT);
				}
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.e("hl", reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = reqBase.getBody().get("userHomeInfo").toString();
			userBean = (UserHomeEntity) JsonUtil.jsonToObj(
					UserHomeEntity.class, jsonstr);
			if (!Tool.isEmpty(userBean) && userBean.getId() > 0) {
				setUserInfo();
			}
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("关注成功");
			getUserInfo(toUserId);
			if (userBean != null) {
				sendBroadcast(new Intent(
						BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU));// 关注后发广播刷新通讯录
				// 发送关注消息
				String context = AppContext.getInstance().getUserInfo()
						.getNickName()
						+ "关注了你，互相关注成为好友，可随时了解对方动态。";
				TipAttachment tipatt = new TipAttachment(context);
				com.netease.nimlib.sdk.msg.model.IMMessage msg = MessageBuilder
						.createCustomMessage(userBean.getLeanCloudUserName(),
								SessionTypeEnum.P2P,tipatt);
				msg.setContent(context);
				msg.setPushContent(context);
				Map<String, Object> content = new HashMap<String, Object>(4);
				content.put("nickName", AppContext.getInstance().getUserInfo()
						.getNickName());
				content.put("clientHeadPicUrl", AppContext.getInstance()
						.getUserInfo().getClientThumbHeadPicUrl());
				content.put("userId", AppContext.getInstance().getUserInfo()
						.getId()
						+ "");
				content.put("groupId", "");
				msg.setRemoteExtension(content);
				msg.setPushPayload(content);
				CustomMessageConfig config = new CustomMessageConfig();
				config.enablePushNick = false;
				msg.setConfig(config);
				NIMClient.getService(MsgService.class).sendMessage(msg, false);
			}
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("拉黑成功");
			sendBroadcast(new Intent(BroadCastAction.ACTION_IM_BLACKLIST));
			getUserInfo(toUserId);
			break;
		case reqCodeFour:
			sendBroadcast(new Intent(BroadCastAction.ACTION_IM_BLACKLIST));
			getUserInfo(toUserId);
			break;
		case reqCodeFive:
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU));// 取消关注发广播刷新通讯录
			getUserInfo(toUserId);
			break;
		case reqCodeSix:
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU));
			getUserInfo(toUserId);
			break;
		case reqCodeSeven:
			if (!Tool.isEmpty(reqBase.getBody().get("homePageShareUrl"))
					&& (reqBase.getBody().get("homePageShareUrl") + "")
							.length() > 4) {
				shareUrl = reqBase.getBody().get("homePageShareUrl")
						.getAsString();
				shareTitle = reqBase.getBody().get("shareTitle").getAsString();
				shareContent = reqBase.getBody().get("shareContent")
						.getAsString();
				shareImgUrl = reqBase.getBody().get("shareImgUrl")
						.getAsString();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		default:
			ToastUtil.showShortToast("获取数据失败！");
			finish();
			break;
		}
	}

	// 给界面赋值用户信息
	public void setUserInfo() {
		leanCloudUserName = userBean.getLeanCloudUserName();
		clientBackgroundId = userBean.getClientBackgroundId();
		iv_background = new MySimpleDraweeView(PersonShowActivity.this);
		lin_background.removeAllViews();
		lin_background.addView(iv_background, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		GenericDraweeHierarchy hierarchy = iv_background.getHierarchy();
		hierarchy.setFailureImage(ContextCompat.getDrawable(
				PersonShowActivity.this, R.drawable.beijing),
				ScalingUtils.ScaleType.CENTER_CROP);
		iv_background.setUrlOfImage(clientBackgroundId);
		if ("1".equals(userBean.getFriendFlag())) {
			lin_beizhu.setVisibility(View.VISIBLE);
		} else {
			lin_beizhu.setVisibility(View.GONE);
		}
		txt_distance.setText(userBean.getKmDistance());
		if (!Tool.isEmpty(userBean.getIntervalTime())) {
			txt_logintime.setText("  |  " + userBean.getIntervalTime());
		}
		nickName = userBean.getNickName();
		txt_name.setText(nickName);
		txt_userCode.setText(userBean.getUserCode());
		if (Tool.isEmpty(userBean.getAge()) || userBean.getAge() == 0) {
			txt_age.setText("未知");
		} else {
			txt_age.setText(userBean.getAge() + "");
		}
		tv_vip.setVisibility(userBean.getVipLevel() > 0 ? View.VISIBLE
				: View.GONE);
		txt_job.setText(userBean.getPositionName());
		if (Tool.isEmpty(userBean.getIntroduce())) {
			txt_sign.setText("我期望通过手艺交同行朋友");
		} else {
			txt_sign.setText(userBean.getIntroduce());
		}
		txt_citys.setText(userBean.getAppearPlace());
		txt_beizhu.setText(userBean.getAliasName());
		txt_loves.setText(userBean.getHobby());

		txt_remark.setText(userBean.getRemark());
		if (userBean.getGender().equals("1")) {
			Drawable drawable = ContextCompat.getDrawable(PersonShowActivity.this,
					R.drawable.nan_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			txt_age.setCompoundDrawables(drawable, null, null, null);
			txt_age.setBackgroundResource(R.drawable.nanxing);
		} else {
			Drawable drawable = ContextCompat.getDrawable(PersonShowActivity.this,
					R.drawable.nv_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			txt_age.setCompoundDrawables(drawable, null, null, null);
			txt_age.setBackgroundResource(R.drawable.nvxing);

		}
		// 设置个性标签
		layout_tags.removeAllViews();
		for (int i = 0, size = userBean.getPersonalTagList().size(); i < size; i++) {
			creatOneTags(userBean.getPersonalTagList().get(i));
		}

		clientHeadPicUrl = userBean.getClientThumbHeadPicUrl();
		clientHeadBigPicUrl = userBean.getClientHeadPicUrl();
		MyRoundDraweeView img_head = new MyRoundDraweeView(
				PersonShowActivity.this);
		lin_head.removeAllViews();
		lin_head.addView(img_head, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		img_head.setUrlOfImage(clientHeadPicUrl);
		if ("1".equals(userBean.getFriendFlag())) { // 是好友
			findViewById(R.id.view_a).setVisibility(View.GONE);
			findViewById(R.id.view_b).setVisibility(View.GONE);
			btnAddfriend.setVisibility(View.GONE);
			btnBlack.setVisibility(View.GONE);
			txt_relation.setText("好友");
			btnTalk.setText("对话");
			btnTalk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil
								.showTipToLoginDialog(PersonShowActivity.this);
						return;
					}
					Intent i = new Intent(PersonShowActivity.this,
							ChatActivity.class);
					i.putExtra(ConstantKey.KEY_IM_TALKTO, userBean.getUserId());
					i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
							userBean.getLeanCloudUserName());
					i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
							userBean.getNickName());
					i.putExtra(ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
							userBean.getClientHeadPicUrl());
					startActivity(i);
				}
			});
			layout_goshare.setVisibility(View.GONE);
			more.setVisibility(View.VISIBLE);
			// more.setBackgroundResource(R.drawable.setting_round);
			more.setText("设置");
			layout_goEdit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil
								.showTipToLoginDialog(PersonShowActivity.this);
						return;
					}
					Intent intent = new Intent(PersonShowActivity.this,
							GoodFriendSetActivity.class);
					intent.putExtra("lahei", toUserId);
					intent.putExtra("leanCloudUserName", leanCloudUserName);
					startActivityForResult(intent, reqCode_more);
				}
			});
			if (!TextUtils.isEmpty(userBean.getAliasName())) {
				txt_name.setText(userBean.getAliasName());
			}
		} else if ("2".equals(userBean.getFriendFlag())) {
			btnAddfriend.setVisibility(View.VISIBLE);
			btnAddfriend.setText("取消关注");
			btnBlack.setVisibility(View.VISIBLE);
			btnTalk.setText("对话");
			btnTalk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil
								.showTipToLoginDialog(PersonShowActivity.this);
						return;
					}
					Intent i = new Intent(PersonShowActivity.this,
							ChatActivity.class);
					i.putExtra(ConstantKey.KEY_IM_TALKTO, userBean.getUserId());
					i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
							userBean.getLeanCloudUserName());
					i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
							userBean.getNickName());
					i.putExtra(ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
							userBean.getClientHeadPicUrl());
					startActivity(i);
				}
			});
			findViewById(R.id.view_a).setVisibility(View.VISIBLE);
			findViewById(R.id.view_b).setVisibility(View.VISIBLE);
			layout_goshare.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			txt_relation.setText("关注");
		} else if ("3".equals(userBean.getFriendFlag())) {// 自己
			txt_relation.setText("自己");
			layout_goshare.setVisibility(View.VISIBLE);
			layout_goshare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (Tool.isEmpty(shareUrl)) {
						ToastUtil.showShortToast("获取分享路径失败！");
						return;
					}
					new InviteFriendDialog(PersonShowActivity.this, shareUrl,
							shareTitle, shareContent, shareImgUrl, AppContext
									.getInstance().getUserInfo().getUserId()
									+ "", ConstantKey.ShareStatus_CARD_STATE)
							.show();
				}
			});
			more.setVisibility(View.VISIBLE);
			// more.setBackgroundResource(R.drawable.bianji_round);
			more.setText("编辑");
			layout_goEdit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivityForResult(new Intent(PersonShowActivity.this,
							EditPersonalInfoActivity.class), reqCode_bianji);
				}
			});
		} else if ("4".equals(userBean.getFriendFlag())) {// 黑名单
			findViewById(R.id.view_a).setVisibility(View.GONE);
			findViewById(R.id.view_b).setVisibility(View.GONE);
			btnAddfriend.setVisibility(View.GONE);
			btnBlack.setVisibility(View.GONE);
			txt_relation.setText("黑名单");
			layout_goshare.setVisibility(View.GONE);
			more.setVisibility(View.INVISIBLE);
			btnTalk.setText("取消黑名单");
			btnTalk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil
								.showTipToLoginDialog(PersonShowActivity.this);
						return;
					}
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getUserId());
					map.put("deFriendId", toUserId);
					map.put("toLeanCloudUserName", leanCloudUserName);
					map.put("leanCloudUserName", AppContext.getInstance()
							.getUserInfo().getLeanCloudUserName());
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSSINESS_CANCEL_BLACK));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeFour, AppConfig.PUBLICK_NEARBY_GROUP,
							reqBase, true);
				}
			});
			saveToDB(userBean);
		} else { // 陌生人
			btnAddfriend.setVisibility(View.VISIBLE);
			btnAddfriend.setText("加关注");
			btnBlack.setVisibility(View.VISIBLE);
			btnTalk.setText("对话");
			btnTalk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil
								.showTipToLoginDialog(PersonShowActivity.this);
						return;
					}
					Intent i = new Intent(PersonShowActivity.this,
							ChatActivity.class);
					i.putExtra(ConstantKey.KEY_IM_TALKTO, userBean.getUserId());
					i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
							userBean.getLeanCloudUserName());
					i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
							userBean.getNickName());
					i.putExtra(ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
							userBean.getClientHeadPicUrl());
					startActivity(i);
				}
			});
			findViewById(R.id.view_a).setVisibility(View.VISIBLE);
			findViewById(R.id.view_b).setVisibility(View.VISIBLE);
			layout_goshare.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			txt_relation.setText("陌生人");
			saveToDB(userBean);
		}

		if (!Tool.isEmpty(userBean.getPostsList())) {
			if (userBean.getPostsList().size() == 1) {
				if (TextUtils
						.isEmpty(userBean.getPostsList().get(0).getTitle())) {
					txt_post1.setText(userBean.getPostsList().get(0)
							.getContent());
					txt_post1_con.setVisibility(View.GONE);
				} else {
					txt_post1
							.setText(userBean.getPostsList().get(0).getTitle());
					txt_post1_con.setText(userBean.getPostsList().get(0)
							.getContent());
				}
				layout_post2.setVisibility(View.GONE);
			} else {
				if (TextUtils
						.isEmpty(userBean.getPostsList().get(0).getTitle())) {
					txt_post1.setText(userBean.getPostsList().get(0)
							.getContent());
					txt_post1_con.setVisibility(View.GONE);
				} else {
					txt_post1
							.setText(userBean.getPostsList().get(0).getTitle());
					txt_post1_con.setText(userBean.getPostsList().get(0)
							.getContent());
				}
				if (TextUtils
						.isEmpty(userBean.getPostsList().get(1).getTitle())) {
					txt_post2.setText(userBean.getPostsList().get(1)
							.getContent());
					txt_post2_con.setVisibility(View.GONE);
				} else {
					txt_post2
							.setText(userBean.getPostsList().get(1).getTitle());
					txt_post2_con.setText(userBean.getPostsList().get(1)
							.getContent());
				}
			}
		} else {
			layout_post.setVisibility(View.GONE);
			layout_post1.setVisibility(View.GONE);
			layout_post2.setVisibility(View.GONE);
		}
		// 相册
		if (!Tool.isEmpty(userBean.getPersonalPhotoList())) {
			layout_myPhotos.setVisibility(View.VISIBLE);
			data_myPhotos.clear();
			data_myPhotos.addAll(userBean.getPersonalPhotoList());
			adapter_myPhotos.notifyDataSetChanged();
		} else {
			layout_myPhotos.setVisibility(View.GONE);
		}
		// 秀场
		if (!Tool.isEmpty(userBean.getPostsSignupList())) {
			layout_myshow.setVisibility(View.VISIBLE);
			data_showList.clear();
			data_showList.addAll(userBean.getPostsSignupList());
			adapter_myshow.notifyDataSetChanged();
		} else {
			layout_myshow.setVisibility(View.GONE);
		}
		// 群组
		if (!Tool.isEmpty(userBean.getGroupList())) {
			layout_group.setVisibility(View.VISIBLE);
			data_groupList.clear();
			data_groupList.addAll(userBean.getGroupList());
			adapter_mygroup.notifyDataSetChanged();
		} else {
			layout_group.setVisibility(View.GONE);
		}
		txt_regisdate.setText(userBean.getCreateDate());
		mypulltorefresh.smoothScrollTo(0, 0);
	}

	private void saveToDB(UserHomeEntity user) {
		IMYxUserInfo userInfo = new IMYxUserInfo();
		userInfo.setNickName(user.getNickName());
		userInfo.setUserHeadImg(user
				.getClientThumbHeadPicUrl());
		userInfo.setYxAccount(user.getLeanCloudUserName());
		userInfo.setUserId(user.getUserId());
		MsgDataBase.getInstance().saveOrUpdateYxUser(
				userInfo);
	}

	/**
	 * 添加一个标签
	 * 
	 * @param oneTypeData
	 */
	private void creatOneTags(PersonalTagEntity tagData) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, ScreenUtil.dip2px(this, 3),
				ScreenUtil.dip2px(this, 12), ScreenUtil.dip2px(this, 3));
		int padding = ScreenUtil.dip2px(this, 10);
		TextView mTextView = new TextView(this);
		mTextView.setPadding(padding, padding / 3, padding, padding / 3);
		mTextView.setEllipsize(TruncateAt.END);
		mTextView.setSingleLine();
		mTextView.setGravity(Gravity.NO_GRAVITY);
		mTextView.setLayoutParams(params);
		if (!Tool.isEmpty(tagData.getColor())) {
			mTextView.setBackgroundResource(R.drawable.whiteround);
		}
		Drawable drawable = mTextView.getBackground();
		int color = Color.parseColor(tagData.getColor());
		drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		mTextView.setTextColor(getResources().getColor(R.color.white));
		mTextView.setText(tagData.getTagName());
		layout_tags.addView(mTextView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_post:
			Intent intent = new Intent(PersonShowActivity.this,
					MyCommunityActivity.class);
			intent.putExtra("userId", Integer.parseInt(toUserId));
			startActivityForResult(intent, reqCodeFour);// 查看帖子
			break;
		case R.id.btnAddfriend:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(PersonShowActivity.this);
				return;
			}
			if (userBean == null) {
				return;
			}
			if ("0".equals(userBean.getFriendFlag())) {
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getUserId());
				map.put("friendId", toUserId);
				String nickname = userBean.getNickName();
				String sortString = PinyinUtil
						.getTerm(Tool.isEmpty(nickname) ? "" : nickname
								.substring(0, 1));
				map.put("nameFirstChar",
						sortString.matches("[A-Z]") ? sortString : "#");
				map.put("aliasName", nickname);
				reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_ADDFOCUS));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, reqBase,
						true);
			} else {
				// 取消关注
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getUserId());
				map.put("friendId", toUserId);
				reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CANCELFOCUS));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				httpPost(reqCodeFive, AppConfig.PUBLICK_NEARBY_GROUP, reqBase,
						true);
				// ToastUtil.showShortToast("已经关注");
			}
			break;
		case R.id.btnTalk:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(PersonShowActivity.this);
				return;
			}
			if (userBean == null) {
				return;
			}
			Intent i = new Intent(PersonShowActivity.this, ChatActivity.class);
			i.putExtra(ConstantKey.KEY_IM_TALKTO, userBean.getUserId());
			i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME, userBean.getNickName());
			i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
					userBean.getLeanCloudUserName());
			i.putExtra(ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
					userBean.getClientHeadPicUrl());
			startActivity(i);
			break;
		case R.id.btnBlack:
			// 拉黑
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(PersonShowActivity.this);
				return;
			}
			final MessageDialog messageDialog = new MessageDialog(
					PersonShowActivity.this, messageList,
					new messageListener() {

						@Override
						public void positionchoose(int position) {
							switch (position) {
							case 0:
								ReqBase reqBase1 = new ReqBase();
								Map<String, Object> map1 = new HashMap<String, Object>();
								map1.put("userId", AppContext.getInstance()
										.getUserInfo().getUserId());
								map1.put("deFriendId", toUserId);
								map1.put("toLeanCloudUserName",
										leanCloudUserName);
								map1.put("leanCloudUserName", AppContext
										.getInstance().getUserInfo()
										.getLeanCloudUserName());
								reqBase1.setHeader(new ReqHead(
										AppConfig.BUSSINESS_ADD_BLACK));
								reqBase1.setBody(JsonUtil
										.String2Object(JsonUtil
												.hashMapToJson(map1)));
								httpPost(reqCodeThree,
										AppConfig.PUBLICK_NEARBY_GROUP,
										reqBase1, true);
								break;
							case 1:
								Intent intent = new Intent(
										PersonShowActivity.this,
										JuBaoActivity.class);
								intent.putExtra("friendid", toUserId);
								intent.putExtra("sourceType", sourceType);
								startActivity(intent);
								break;
							case 2:

								break;
							default:
								break;
							}
						}
					});
			messageDialog.show();
			break;
		case R.id.lin_head:
			if (!Tool.isEmpty(clientHeadPicUrl)) {
				if (!Tool.isEmpty(userBean)
						&& userBean.getId() == AppContext.getInstance()
								.getUserInfo().getId()) {
					startActivityForResult(new Intent(PersonShowActivity.this,
							MyAvatarActivity.class).putExtra("picUrl",
							clientHeadBigPicUrl), reqCodeOne);

				} else {
					Intent intentImage = new Intent();
					intentImage.setClass(PersonShowActivity.this,
							ImagePagerActivity.class);
					intentImage.putExtra("showOnePic", clientHeadBigPicUrl);
					startActivity(intentImage);
				}
			}
			break;

		case R.id.layout_group:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(PersonShowActivity.this);
				return;
			}
			if (userBean == null) {
				return;
			}
			startActivity(new Intent(PersonShowActivity.this,
					TaGroupActivity.class).putExtra("userId",
					userBean.getUserId() + ""));
			break;
		case R.id.lin_background:
			if (userBean == null) {
				return;
			}
			if ("3".equals(userBean.getFriendFlag())) {
				ArrayList<String> cardStrList = new ArrayList<String>();
				cardStrList.add("更换背景图片");
				if (!Tool.isEmpty(userBean.getClientBackgroundId())) {
					cardStrList.add("查看");
				}
				MessageDialog changeCardDialog = new MessageDialog(
						PersonShowActivity.this, cardStrList,
						new messageListener() {

							@Override
							public void positionchoose(int position) {
								switch (position) {
								case 0:
									Intent singleIntent = new Intent(
											PersonShowActivity.this,
											ChangeCardActivity.class).putExtra(
											"clientBackgroundId",
											clientBackgroundId);
									startActivityForResult(singleIntent,
											reqCode_selectBg);
									break;
								case 1:
									Intent intentImage = new Intent();
									intentImage.setClass(
											PersonShowActivity.this,
											ImagePagerActivity.class);
									intentImage.putExtra("showOnePic",
											userBean.getClientBackgroundId());
									startActivity(intentImage);
									break;
								default:
									break;
								}
							}
						});
				changeCardDialog.show();
			} else {
				if (!Tool.isEmpty(userBean.getClientBackgroundId())) {
					Intent intentImage = new Intent();
					intentImage.setClass(PersonShowActivity.this,
							ImagePagerActivity.class);
					intentImage.putExtra("showOnePic",
							userBean.getClientBackgroundId());
					startActivity(intentImage);
				}
			}
			break;
		case R.id.layout_goback:
			finish();
			break;
		case R.id.lin_beizhu:
			if (userBean == null) {
				return;
			}
			new CommonEditDialog(PersonShowActivity.this, "修改备注", "请填写备注名",
					userBean.getAliasName(), 10, true,
					new EditClickOkListener() {

						@Override
						public void doConfirm(String inputString) {
							// 修改备注
							ReqBase reqBase = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getUserId());
							map.put("friendId", toUserId);
							map.put("aliasName", inputString);
							map.put("nameFirstChar",
									PinyinUtil.getTerm(userBean.getAliasName()));
							map.put("hideFlag", -1);
							reqBase.setHeader(new ReqHead(
									AppConfig.BUSSINESS_GOODFRIENDSET));
							reqBase.setBody(JsonUtil.String2Object(JsonUtil
									.hashMapToJson(map)));
							httpPost(reqCodeSix,
									AppConfig.PUBLICK_NEARBY_GROUP, reqBase,
									false);
						}
					}).show();
			break;
		case R.id.layout_myshow:// 进我的秀场
			Intent intentImage = new Intent();
			intentImage.setClass(PersonShowActivity.this,
					MyShowWorksActivity.class);
			intentImage.putExtra("userId", Integer.parseInt(toUserId));
			startActivity(intentImage);
			break;
		case R.id.layout_myPhotos:
			startActivityForResult(new Intent(PersonShowActivity.this,
					PersonPhotoActivity.class).putExtra("userId", toUserId),
					WATCHPHOTO);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCode_more) {
				getUserInfo(toUserId);
			} else if (requestCode == reqCode_bianji) {
				getUserInfo(toUserId);
			} else if (requestCode == reqCodeOne) {
				getUserInfo(toUserId);
			} else if (requestCode == reqCode_selectBg) {
				getUserInfo(toUserId);
			} else if (requestCode == reqCodeFour) {// 查看帖子
				getUserInfo(toUserId);
			} else if (requestCode == WATCHPHOTO) {// 查看相册，若有过编辑则需要刷新
				getUserInfo(toUserId);
			}
		}
	}

	public void getShareurl() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PEESON_SHARE));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeSeven, AppConfig.PERSONAL_REQUEST_MAPPING, req, true);
	}
}
