package com.meiku.dev.ui.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.im.ChatActivity;
import com.meiku.dev.ui.im.ImFragment;
import com.meiku.dev.ui.login.MkLoginActivity;
import com.meiku.dev.ui.mine.PersonFragment;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.AllShowFragment;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.ui.myshow.WorkDetailNewActivity;
import com.meiku.dev.ui.service.ServiceFragment;
import com.meiku.dev.utils.BadgeUtil;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.MessageObs;
import com.meiku.dev.utils.MessageObs.MessageSizeChageListener;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.SystemBarTintManager;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.MyViewpager;
import com.meiku.dev.yunxin.TeamDataCache;
import com.meiku.dev.yunxin.recent.DropCover;
import com.meiku.dev.yunxin.recent.DropFake;
import com.meiku.dev.yunxin.recent.DropManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.umeng.analytics.MobclickAgent;

/**
 * 应用主页面
 */
public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener {

	/** tab图片未选中 */
	private int[] imgUnSelected = new int[] {
			R.drawable.iocn_newzhaotonghangok, R.drawable.iocn_touxueyiok,
			R.drawable.iocn_kaidianpuok, R.drawable.iocn_newwodeok };
	/** tab图片选中 */
	private int[] imgSelected = new int[] { R.drawable.icon_newzhaotonghang,
			R.drawable.icon_touxueyi, R.drawable.icon_kaidianpu,
			R.drawable.icon_newwode };
	/** tab文字textview 的id */
	private int[] tvIDs = new int[] { R.id.txt_main_bottom_comm,
			R.id.txt_main_bottom_show, R.id.txt_main_bottom_morefun,
			R.id.txt_main_bottom_mine };
	/** tab图片imageview 的id */
	private int[] ivIDs = new int[] { R.id.id_img_tab_comm,
			R.id.id_img_tab_show, R.id.id_img_tab_morefun, R.id.id_img_tab_mine };
	/** tab布局的id */
	private int[] layoutIDs = new int[] { R.id.id_tab_comm, R.id.id_tab_show,
			R.id.id_tab_morefun, R.id.id_tab_mine };
	/** 所有tab文字的TextView */
	private TextView[] tab_tvs = new TextView[4];
	/** 所有tab图标的ImageView */
	private ImageView[] tab_ivs = new ImageView[4];
	/** 所有tab图标的外出RelativeLayout布局 */
	private RelativeLayout[] tab_layouts = new RelativeLayout[4];
	/** 所有tab数量 */
	private int tabsize;
	/** 未读消息数量 */
	// private TextView msgCount;
	/** 未读消息数图标是否拖出范围的标记 */
	protected boolean isDragOutside;
	private MyViewpager viewpager;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private HomeViewPagerAdapter adapter;
	/** 所有 Fragment */
	// private CircleFragment fragmentIM;//动态
	// private MoreFunFragment fragmentService;//功能
	private ImFragment fragmentIM = new ImFragment();// 即时聊天
	// private CommunityFragment fragmentCom = new CommunityFragment();// 社区
	private AllShowFragment fragmentAllShow = new AllShowFragment();// 秀场
	private ServiceFragment fragmentService = new ServiceFragment();// 功能
	private PersonFragment fragmentMine = new PersonFragment();// 我的
	private LocalReceiver localReceiver;
	/** 定位服务 */
	// private LocationService locationService;
	/** 完善资料提示栏布局 */
	private LinearLayout layout_perfectInfo;
	private SystemBarTintManager tintManager;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_home;
	}

	public void initView() {
		registerReceiver();
		ScreenUtil.initScreenData(this);
		setTransparentStatusBar();
		if (AppContext.getInstance().getUserInfo().getId() != -1) {
			getGroupList();
		}
		View titleEmptyTop = (View) findViewById(R.id.statuslayout);
		int statusBarHeight = ScreenUtil.getStatusBarHeight(this);
		titleEmptyTop.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, statusBarHeight));
		initTabs();
		initRedPoint();
		initViewpager();
		registerUnreadMessageChageListener();
		layout_perfectInfo = (LinearLayout) findViewById(R.id.layout_perfectInfo);
		isShowPerfectInfoBar();
		initUnreadCover();
		registerCacheObsver(true);
	}

	/**
	 * 查询群信息缓存群名称群头像
	 */
	public void getGroupList() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("loginUserId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_SEARCH_GROUP));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, false);
	}

	private void registerCacheObsver(boolean regist) {
		TeamDataCache.getInstance().registerObservers(regist);
		// NimUserInfoCache.getInstance().registerObservers(regist);
		NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(
				incomingMessageObserver, regist);
	}

	/**
	 * 初始化未读红点动画
	 */
	private void initUnreadCover() {
		DropManager.getInstance().init(HomeActivity.this,
				(DropCover) findViewById(R.id.unread_cover),
				new DropCover.IDropCompletedListener() {
					@Override
					public void onCompleted(Object id, boolean explosive) {
						if (id == null || !explosive) {
							return;
						}

						if (id instanceof RecentContact) {
							RecentContact r = (RecentContact) id;
							NIMClient.getService(MsgService.class)
									.clearUnreadCount(r.getContactId(),
											r.getSessionType());
							LogUtil.e(
									"hl",
									"clearUnreadCount, sessionId="
											+ r.getContactId());
						} else if (id instanceof String) {
							if (((String) id).contentEquals("0")) {
								List<RecentContact> recentContacts = NIMClient
										.getService(MsgService.class)
										.queryRecentContactsBlock();
								for (RecentContact r : recentContacts) {
									if (r.getUnreadCount() > 0) {
										NIMClient.getService(MsgService.class)
												.clearUnreadCount(
														r.getContactId(),
														r.getSessionType());
									}
								}
								LogUtil.e("hl", "clearAllUnreadCount");
							} else if (((String) id).contentEquals("1")) {
								NIMClient
										.getService(SystemMessageService.class)
										.resetSystemMessageUnreadCount();
								LogUtil.e("hl", "clearAllSystemUnreadCount");
							}
						}
						MessageObs.getInstance().notifyAllLis(0);
						sendBroadcast(new Intent(
								BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE));// 发广播刷新消息页面
					}
				});
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
		tintManager.setTintColor(Color.BLACK);
	}

	// private BDLocationListener mListener = new BDLocationListener() {
	//
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// if (null != location
	// && location.getLocType() != BDLocation.TypeServerError) {
	// StringBuffer sb = new StringBuffer(256);
	// if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
	// sb.append("gps定位成功");
	// } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//
	// 网络定位结果
	// sb.append("网络定位成功");
	// } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {//
	// 离线定位结果
	// sb.append("离线定位成功");
	// } else if (location.getLocType() == BDLocation.TypeServerError) {
	// sb.append("服务端网络定位失败");
	// } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
	// sb.append("网络不同导致定位失败，请检查网络是否通畅");
	// } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
	// sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
	// }
	// LogUtil.d("hl", "baidu_location--------->" + sb.toString());
	//
	// if (!Tool.isEmpty(location.getCity())) {// 城市名不为空，视为有效定位
	// // 百度定位失败偶尔会出现4.9E-324情况
	// if (!"4.9E-324".equals(String.valueOf(location
	// .getLatitude()))) {
	// MrrckApplication.laitude = location.getLatitude();
	// }
	// if (!"4.9E-324".equals(String.valueOf(location
	// .getLatitude()))) {
	// MrrckApplication.longitude = location.getLongitude();
	// }
	// MrrckApplication.cityName = location.getCity();
	// MrrckApplication.getInstance().initLocationData();//
	// 通过城市名称，查询获取数据库对应省市code
	// updateLocation();
	// locationService.unregisterListener(mListener); // 注销掉监听
	// locationService.stop(); // 停止定位服务 break;
	// // 刷新附近人
	// sendBroadcast(new Intent(
	// BroadCastAction.ACTION_NEARBY_PEOPLE_BYLOCATION));
	// }
	// }
	// }
	//
	// };
	private int currenPageIndex;
	private DropFake unreadTV;

	private void showCurrentPageByIndex(int index) {
		for (int i = 0; i < tabsize; i++) {
			tab_tvs[i].setTextColor(index == i ? getResources().getColor(
					R.color.home_buttom_txt_red) : getResources().getColor(
					R.color.home_buttom_txt_gray));
			tab_ivs[i].setBackgroundResource(index == i ? imgSelected[i]
					: imgUnSelected[i]);
		}
		viewpager.setCurrentItem(index, false);
	}

	private void initViewpager() {
		listFragment.add(fragmentIM);
		// listFragment.add(fragmentCom);
		listFragment.add(fragmentAllShow);
		listFragment.add(fragmentService);
		fragmentService.setTintManager(tintManager);
		listFragment.add(fragmentMine);
		viewpager = (MyViewpager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(4);

		currenPageIndex = 0;
		showCurrentPageByIndex(0);
	}

	/**
	 * 未读消息显示初始化
	 */
	private void initRedPoint() {
		// msgCount = (TextView) findViewById(R.id.message_dian);
		// msgCount.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent arg1) {
		// ClipData.Item item = new ClipData.Item((CharSequence) v
		// .getTag());
		// String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
		// ClipData dragData = new ClipData(v.getTag().toString(),
		// mimeTypes, item);
		// View.DragShadowBuilder myShadow = new DragShadowBuilder(
		// msgCount);
		// v.startDrag(dragData, // the data to be dragged
		// myShadow, // the drag shadow builder
		// null, // no need to use local data
		// 0 // flags (not currently used, set to 0)
		// );
		// msgCount.setVisibility(View.GONE);
		// return false;
		// }
		// });

		unreadTV = ((DropFake) findViewById(R.id.tab_new_msg_label));
		if (unreadTV != null) {
			unreadTV.setClickListener(new DropFake.ITouchListener() {
				@Override
				public void onDown() {
					DropManager.getInstance().setCurrentId("0");
					DropManager.getInstance().getDropCover()
							.down(unreadTV, unreadTV.getText());
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
		// if (tab_layouts[0] != null) {
		// tab_layouts[0].setOnDragListener(new OnDragListener() {
		// @Override
		// public boolean onDrag(View v, DragEvent event) {
		// switch (event.getAction()) {
		// case DragEvent.ACTION_DRAG_ENTERED:
		// isDragOutside = false;
		// break;
		// case DragEvent.ACTION_DRAG_EXITED:
		// isDragOutside = true;
		// break;
		// case DragEvent.ACTION_DRAG_ENDED:
		// msgCount.setVisibility(isDragOutside ? View.GONE
		// : View.VISIBLE);
		// if (isDragOutside) {
		// MsgDataBase.getInstance().delNoReadNotice();
		// sendBroadcast(new Intent(
		// BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE));// 发广播刷新消息页面
		// }
		// break;
		// default:
		// break;
		// }
		// return true;
		// }
		// });
		// }
	}

	/**
	 * 注册消息数变化监听器
	 */
	private void registerUnreadMessageChageListener() {
		MessageObs.getInstance().registerListener(
				new MessageSizeChageListener() {

					@Override
					public void onMsgSizeChange(int unreadMessageSize) {
						// msgCount.setText(unreadMessageSize > 99 ? "99+"
						// : unreadMessageSize + "");
						// msgCount.setVisibility(unreadMessageSize > 0 ?
						// View.VISIBLE
						// : View.GONE);
						// msgCount.setTag(unreadMessageSize + "");
						// 更新应用图标的右上角标数字
						if (unreadTV != null) {
							unreadTV.setText(unreadMessageSize > 99 ? "99+"
									: unreadMessageSize + "");
							unreadTV.setVisibility(unreadMessageSize > 0 ? View.VISIBLE
									: View.GONE);
						}

						BadgeUtil.setBadgeCount(HomeActivity.this,
								unreadMessageSize, R.drawable.icon);
					}
				});
	}

	/**
	 * 初始化TABS
	 */
	private void initTabs() {
		tabsize = tvIDs.length;
		for (int i = 0; i < tabsize; i++) {
			tab_tvs[i] = (TextView) findViewById(tvIDs[i]);
			tab_ivs[i] = (ImageView) findViewById(ivIDs[i]);
			tab_layouts[i] = (RelativeLayout) findViewById(layoutIDs[i]);
			tab_layouts[i].setOnClickListener(this);
		}

	}

	// Handler reconHandler = new Handler(); // 定时检测是否连上网络
	// Runnable reconRunnable = new Runnable() {
	// @Override
	// public void run() {
	// LogUtil.d("xmppcon", "定时检测");
	// if (Util.isNetworkAvailable(getApplicationContext())) {
	// LogUtil.d("xmppcon", "联网成功");
	// MrrckApplication.getInstance().doMsgServerLogin();
	// reconHandler.removeCallbacks(reconRunnable);
	// } else {
	// LogUtil.d("xmppcon", "联网失败，继续定时");
	// reconHandler.postDelayed(reconRunnable, 15000);
	// }
	// }
	// };

	@Override
	public void initValue() {
		// 点击消息通知进来，跳转到聊天页面
		if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
			LogUtil.d("hl", "onResume" + getIntent());
			if (getIntent().hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
				ArrayList<IMMessage> messagesList = (ArrayList<IMMessage>) getIntent()
						.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);

				LogUtil.d("hl", "messagesList=" + messagesList);
				if (null != messagesList && messagesList.size() >= 1) {
					IMMessage message = messagesList.get(0);
					if (null == message) {
						return;
					}
					LogUtil.d("hl", "message=" + message);
					LogUtil.d("hl", "ext=" + message.getRemoteExtension());
					switch (message.getSessionType()) {
					case P2P:
						Intent i = new Intent(HomeActivity.this,
								ChatActivity.class);
						Map<String, Object> ext = message.getRemoteExtension();
						int userId = -1;
						String nickName = "";
						if (ext != null && ext.containsKey("userId")) {
							userId = Integer.parseInt(ext.get("userId")
									.toString());
						}
						if (ext != null && ext.containsKey("nickName")) {
							nickName = ext.get("nickName").toString();
						}
						i.putExtra(ConstantKey.KEY_IM_TALKTO, userId);
						i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
								message.getFromAccount());
						i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME, nickName);
						startActivity(i);
						break;
					case Team:
						Intent intent = new Intent(HomeActivity.this,
								ChatActivity.class);
						Map<String, Object> extG = message.getRemoteExtension();
						int groupId = -1;
						String groupname = "";
						if (extG != null && extG.containsKey("groupId")) {
							groupId = Integer.parseInt(extG.get("groupId")
									.toString());
						}
						if (extG != null && extG.containsKey("groupName")) {
							groupname = extG.get("groupName").toString();
						}
						intent.putExtra(ConstantKey.KEY_IM_TALKTO, groupId);
						intent.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
								message.getSessionId());
						intent.putExtra(ConstantKey.KEY_IM_SESSIONTYPE,
								XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
						intent.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
								groupname);
						startActivity(intent);
						break;
					default:
						break;
					}
				}
			}
		}
		String type = getIntent().getStringExtra("type");
		if (!Tool.isEmpty(type)) {
			switch (Integer.parseInt(type)) {
			case ConstantKey.ShareStatus_NOSHOWWORK:// 转跳未参赛作品详情页
				String data = getIntent().getStringExtra("data");
				if (!Tool.isEmpty(data)) {
					Intent intent = new Intent(HomeActivity.this,
							WorkDetailNewActivity.class);
					intent.putExtra("SignupId", Integer.parseInt(data));
					startActivity(intent);
				}
				break;
			case ConstantKey.ShareStatus_SHOWWORK:// 转跳参赛作品详情页
				String data2 = getIntent().getStringExtra("data");
				if (!Tool.isEmpty(data2)) {
					Intent intent = new Intent(HomeActivity.this,
							NewWorkDetailActivity.class);
					intent.putExtra("SignupId", Integer.parseInt(data2));
					startActivity(intent);
				}
				break;
			case ConstantKey.ShareStatus_SAISHI:// 转跳才艺秀页面
				String data3 = getIntent().getStringExtra("data");
				if (!Tool.isEmpty(data3)) {
					Intent intent = new Intent(HomeActivity.this,
							ShowMainActivity.class);
					intent.putExtra("postsId", Integer.parseInt(data3));
					startActivity(intent);
				}
				break;
			case ConstantKey.PUSHStatus_OPENH5:// 转跳网页
				String h5url = getIntent().getStringExtra("h5url");
				if (!Tool.isEmpty(h5url)) {
					Intent intent = new Intent(HomeActivity.this,
							WebViewActivity.class);
					intent.putExtra("webUrl", h5url);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} else {
					ToastUtil.showShortToast(getResources().getString(
							R.string.errorUrl));
				}
				break;
			case ConstantKey.INHOMEPAGE_SAISHIMAIN:// 转跳赛事广告提示页面
				startActivity(new Intent(HomeActivity.this,
						MatchTipActivity.class).putExtra("matchId", getIntent()
						.getIntExtra("matchId", -1)));
				break;
			default:
				break;
			}
		}
		updateLocation();
	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				getWindow().getDecorView().getWindowToken(), 0);
		switch (v.getId()) {
		case R.id.id_tab_comm:
			currenPageIndex = 0;
			showCurrentPageByIndex(0);
			tintManager.setTintColor(Color.BLACK);
			break;
		// case R.id.id_tab_IM:
		// currenPageIndex = 1;
		// showCurrentPageByIndex(1);
		// break;
		case R.id.id_tab_show:
			currenPageIndex = 1;
			showCurrentPageByIndex(1);
			tintManager.setTintColor(Color.BLACK);
			break;
		case R.id.id_tab_morefun:
			currenPageIndex = 2;
			showCurrentPageByIndex(2);
			if (fragmentService != null) {
				tintManager.setTintColor(fragmentService
						.getCurrentStatusbarColor());
				fragmentService.scrollToTop();
			} else {
				tintManager.setTintColor(Color.TRANSPARENT);
			}
			break;
		case R.id.id_tab_mine:
			currenPageIndex = 3;
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				showCurrentPageByIndex(3);
				tintManager.setTintColor(Color.TRANSPARENT);
			} else {
				startActivity(new Intent(HomeActivity.this,
						MkLoginActivity.class));
			}
			break;
		case R.id.layout_perfectInfo:
			// startActivity(new Intent(HomeActivity.this,
			// PerfectMyInfoActivity.class));
			startActivity(new Intent(HomeActivity.this, MkLoginActivity.class));
			break;
		}
		isShowPerfectInfoBar();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(localReceiver);
		// MrrckApplication.getInstance().stopMsgServices();
		// MrrckApplication.getInstance().stopAlarmPing();

		MessageObs.getInstance().clearMessageListaner();// 注销消息数量监听
		if (null != MrrckApplication.getInstance().locationService) {
			MrrckApplication.getInstance().locationService.stop(); // 停止定位服务;
		}
		JPushInterface.clearAllNotifications(this);// 清除所有极光推送通知
		MrrckApplication.getInstance().clearActivityes();
		registerCacheObsver(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			ToastUtil.showLongToast("再按一次退出程序");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			moveTaskToBack(true);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		initValue();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计
		MobclickAgent.onResume(this);

		// if ((Boolean) PreferHelper.getSharedParam("isAppInBackground", false)
		// && AppContext.getInstance().isLoginedAndInfoPerfect()) {
		// new AsyncTask<String, Void, Boolean>() {
		// @Override
		// protected Boolean doInBackground(String... params) {
		// // 从后台进来时需要重新定位一次
		// locationService = ((MrrckApplication)
		// getApplication()).locationService;
		// // 获取locationservice实例，建议应用中只初始化1个location实例，然后使用,需要定位的页面就注册
		// locationService.registerListener(mListener);
		// locationService.setLocationOption(locationService
		// .getDefaultLocationClientOption());
		// locationService.start();
		// return true;
		// }
		//
		// @Override
		// protected void onPostExecute(Boolean aBoolean) {
		// super.onPostExecute(aBoolean);
		// }
		// }.execute();
		// }
		StatusCode status = NIMClient.getStatus();
		if (AppContext.getInstance().isHasLogined()
				&& status != StatusCode.LOGINED) {
			if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
					.getLeanCloudUserName())
					&& !Tool.isEmpty(AppContext.getInstance().getUserInfo()
							.getLeanCloudId())) {
				MrrckApplication.getInstance()
						.doYunXinLogin(
								AppContext.getInstance().getUserInfo()
										.getLeanCloudUserName(),
								AppContext.getInstance().getUserInfo()
										.getLeanCloudId());
			}
		}
	}

	private void updateLocation() {
		if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			map.put("longitude", MrrckApplication.getLongitudeStr());
			map.put("latitude", MrrckApplication.getLaitudeStr());
			LogUtil.d("hl", "updateLocation==" + map);
			req.setHeader(new ReqHead(AppConfig.UPLOAD_COORDINATES));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeOne, AppConfig.USER_REQUEST_MAPPING, req, false);
		}
	}

	@Override
	protected void onPause() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				getWindow().getDecorView().getWindowToken(), 0);
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		PreferHelper.setSharedParam("isAppInBackground",
				Util.isAppInBackground(HomeActivity.this));
		LogUtil.d(
				"hl",
				"isAppInBackground=="
						+ Util.isAppInBackground(HomeActivity.this));
	}

	@Override
	public void bindListener() {
		layout_perfectInfo.setOnClickListener(this);
	}

	/**
	 * 是否显示完善资料提示栏(我的页面底部不需要)
	 */
	private void isShowPerfectInfoBar() {
		// if (currenPageIndex == 4) {
		layout_perfectInfo.setVisibility(View.GONE);
		// } else {
		// layout_perfectInfo.setVisibility(AppContext.getInstance()
		// .isLoginedAndInfoPerfect() ? View.GONE : View.VISIBLE);
		// }
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeTwo:
			if (resp.getBody().get("group") != null
					&& resp.getBody().get("group").toString().length() > 4) {
				List<GroupEntity> groupList = new ArrayList<GroupEntity>();
				groupList = (List<GroupEntity>) JsonUtil.jsonToList(resp
						.getBody().get("group").toString(),
						new TypeToken<List<GroupEntity>>() {
						}.getType());
				if (!Tool.isEmpty(groupList)) {
					AppContext.setGroupList(groupList);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	class LocalReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_PERFECT_INFO.equals(intent.getAction())) {
				isShowPerfectInfoBar();
			} else if (BroadCastAction.ACTION_LOGIN_SUCCESS.equals(intent
					.getAction())) {
				isShowPerfectInfoBar();
				// 登陆成功，链接消息服务器
				if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
					// if (Util.isNetworkAvailable(getApplicationContext())) {//
					// 网络正常
					// MrrckApplication.getInstance().doMsgServerLogin();
					// } else {// 初始化时网络有问题，则做定时任务检测到有网开始连接消息服务器
					// LogUtil.d("xmppcon", "初始化时网络有问题，则做定时任务检测");
					// MrrckApplication.getInstance().startAlarmPing();
					// // reconHandler.postDelayed(reconRunnable, 15000);//
					// // 每15秒执行一次runnable.
					// }
				}
			}
		}
	}

	private void registerReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadCastAction.ACTION_PERFECT_INFO);
		intentFilter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		localReceiver = new LocalReceiver();
		registerReceiver(localReceiver, intentFilter);
	}

	Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
		@Override
		public void onEvent(List<IMMessage> messages) {
			// 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
			LogUtil.e("hl", "receive custom incomingMessageObserver");
		}
	};

}
