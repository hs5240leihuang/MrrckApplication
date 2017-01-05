package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.MessageObs;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.MessageDialog;
import com.meiku.dev.views.MessageDialog.messageListener;
import com.meiku.dev.yunxin.ListViewUtil;
import com.meiku.dev.yunxin.TAdapterDelegate;
import com.meiku.dev.yunxin.TViewHolder;
import com.meiku.dev.yunxin.TeamDataCache;
import com.meiku.dev.yunxin.recent.CommonRecentViewHolder;
import com.meiku.dev.yunxin.recent.DropCover;
import com.meiku.dev.yunxin.recent.DropManager;
import com.meiku.dev.yunxin.recent.RecentContactAdapter;
import com.meiku.dev.yunxin.recent.RecentContactsCallback;
import com.meiku.dev.yunxin.recent.RecentViewHolder;
import com.meiku.dev.yunxin.recent.TeamRecentViewHolder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.umeng.analytics.MobclickAgent;

/**
 * 消息主页面
 * 
 */
public class MessageMainFragment extends BaseFragment {

	private View layout_view;
	private LinearLayout stateLayout;
	private TextView tv_tips;
	private PullToRefreshListView listview;
	private List<RecentContact> items;
	private RecentContactAdapter adapter;
	private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		// 消息提醒
		NIMClient.getService(MsgService.class).setChattingAccount(
				MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		// 消息提醒
		NIMClient.getService(MsgService.class).setChattingAccount(
				MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_message, null);
		regisBroadcast();
		initView();
		return layout_view;
	}

	private void initView() {
		items = new ArrayList<RecentContact>();
		cached = new HashMap<String, RecentContact>(3);
		initStatusView();
		initPullListView();
		registerObservers(true);
		ShowNetWorkStatus();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		listview = (PullToRefreshListView) layout_view
				.findViewById(R.id.pull_refresh_list);
		listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				downRefreshData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
			}
		});
		adapter = new RecentContactAdapter(getActivity(), items,
				new TAdapterDelegate() {

					@Override
					public Class<? extends TViewHolder> viewHolderAtPosition(
							int position) {
						SessionTypeEnum type = items.get(position)
								.getSessionType();
						if (type == SessionTypeEnum.Team) {
							return TeamRecentViewHolder.class;
						} else {
							return CommonRecentViewHolder.class;
						}
					}

					@Override
					public int getViewTypeCount() {
						return 2;
					}

					@Override
					public boolean enabled(int position) {
						return true;
					}
				});
		adapter.setCallback(callback);
		listview.setAdapter(adapter);
		listview.getRefreshableView().setItemsCanFocus(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (callback != null) {
					RecentContact recent = (RecentContact) parent.getAdapter()
							.getItem(position);
					callback.onItemClick(recent);
				}
			}
		});
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				adapter.onMutable(scrollState == SCROLL_STATE_FLING);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		listview.getRefreshableView().setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						if (position < listview.getRefreshableView()
								.getHeaderViewsCount()) {
							return false;
						}
						showLongClickMenu((RecentContact) parent.getAdapter()
								.getItem(position));
						return true;
					}
				});
	}

	private RecentContactsCallback callback = new RecentContactsCallback() {

		@Override
		public void onUnreadCountChange(int unreadCount) {
			MessageObs.getInstance().notifyAllLis(unreadCount);
		}

		@Override
		public void onRecentContactsLoaded() {
		}

		@Override
		public void onItemClick(RecentContact recent) {
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			switch (recent.getSessionType()) {
			case P2P:
				Intent i = new Intent(getActivity(), ChatActivity.class);
				Map<String, Object> ext = recent.getExtension();
				int userId = -1;
				String nickName = "";
				if (ext != null && ext.containsKey("userId")) {
					userId = Integer.parseInt(ext.get("userId").toString());
				} else if (recent.getContactId() != null) {
					IMYxUserInfo Yxuser = MsgDataBase.getInstance()
							.getYXUserById(recent.getContactId());
					if (Yxuser != null) {
						userId = Yxuser.getUserId();
					}
				}
				if (ext != null && ext.containsKey("nickName")) {
					nickName = ext.get("nickName").toString();
				}
				i.putExtra(ConstantKey.KEY_IM_TALKTO, userId);
				i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
						recent.getContactId());
				i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME, nickName);
				startActivity(i);
				break;
			case Team:
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				Map<String, Object> extG = recent.getExtension();
				int groupId = -1;
				String groupname = "";
				if (extG != null && extG.containsKey("groupId")) {
					groupId = Integer.parseInt(extG.get("groupId").toString());
				}
				if (extG != null && extG.containsKey("groupName")) {
					groupname = extG.get("groupName").toString();
				}
				intent.putExtra(ConstantKey.KEY_IM_TALKTO, groupId);
				intent.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
						recent.getContactId());
				intent.putExtra(ConstantKey.KEY_IM_SESSIONTYPE,
						XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
				intent.putExtra(ConstantKey.KEY_IM_TALKTO_NAME, groupname);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

		@Override
		public String getDigestOfTipMsg(RecentContact recent) {
			return null;
		}

		@Override
		public String getDigestOfAttachment(MsgAttachment attachment) {
			return null;
		}
	};

	private boolean isTagSet(RecentContact recent) {
		return !Tool.isEmpty(recent.getTag()) && (recent.getTag() == 1);
	}

	private void addTag(RecentContact recent) {
		recent.setTag(1);
		PreferHelper.setSharedParam(recent.getContactId(), 1);
	}

	private void removeTag(RecentContact recent) {
		recent.setTag(0);
		PreferHelper.setSharedParam(recent.getContactId(), 0);
	}

	protected void showLongClickMenu(final RecentContact recent) {
		if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
			ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
			return;
		}
		ArrayList<String> messageList = new ArrayList<String>();
		messageList.add(getString(R.string.main_msg_list_delete_chatting));
		if (isTagSet(recent)) {
			messageList
					.add(getString(R.string.main_msg_list_clear_sticky_on_top));
		} else {
			messageList.add(getString(R.string.main_msg_list_sticky_on_top));
		}
		// messageList.add("删除该聊天（仅服务器）");
		MessageDialog messageDialog = new MessageDialog(getActivity(),
				messageList, new messageListener() {

					@Override
					public void positionchoose(int position) {
						switch (position) {
						case 0:
							// 删除会话，
							NIMClient.getService(MsgService.class)
									.deleteRecentContact(recent);
							// 消息历史被一起删除
							// NIMClient.getService(MsgService.class)
							// .clearChattingHistory(
							// recent.getContactId(),
							// recent.getSessionType());
							items.remove(recent);

							if (recent.getUnreadCount() > 0) {
								refreshMessages(true);
							} else {
								adapter.notifyDataSetChanged();
							}
							break;
						case 1:
							if (isTagSet(recent)) {
								removeTag(recent);
							} else {
								addTag(recent);
							}
							NIMClient.getService(MsgService.class)
									.updateRecent(recent);
							refreshMessages(false);
							break;
						case 2:
							NIMClient
									.getService(MsgService.class)
									.deleteRoamingRecentContact(
											recent.getContactId(),
											recent.getSessionType())
									.setCallback(new RequestCallback<Void>() {
										@Override
										public void onSuccess(Void param) {
											ToastUtil.showShortToast("删除成功");
										}

										@Override
										public void onFailed(int code) {
											ToastUtil.showShortToast("删除失败");
										}

										@Override
										public void onException(
												Throwable exception) {
										}
									});
							break;
						default:
							break;
						}
					}
				});
		messageDialog.show();
	}

	protected void downRefreshData() {
		refreshMessages(true);
		if (listview != null) {
			listview.postDelayed(new Runnable() {

				@Override
				public void run() {
					listview.onRefreshComplete();
					StatusCode status = NIMClient.getStatus();
					LogUtil.e("hl", "云信status==" + status);
					if (AppContext.getInstance().isHasLogined()
							&& status != StatusCode.LOGINED) {
						if (!Tool.isEmpty(AppContext.getInstance()
								.getUserInfo().getLeanCloudUserName())
								&& !Tool.isEmpty(AppContext.getInstance()
										.getUserInfo().getLeanCloudId())) {
							MrrckApplication.getInstance().doYunXinLogin(
									AppContext.getInstance().getUserInfo()
											.getLeanCloudUserName(),
									AppContext.getInstance().getUserInfo()
											.getLeanCloudId());
						}
					}
				}
			}, 2 * 1000);
		}
	}

	private void initStatusView() {
		stateLayout = (LinearLayout) layout_view
				.findViewById(R.id.im_client_state_view);
		stateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!NetworkTools.isNetworkAvailable(getActivity())) {
					Intent intent = new Intent("android.settings.WIFI_SETTINGS");
					startActivity(intent);
					return;
				}
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
					return;
				}

			}
		});
		tv_tips = (TextView) layout_view.findViewById(R.id.tv_tips);
	}

	@Override
	public void initValue() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE);
		filter.addAction(BroadCastAction.ACTION_IM_REFRESHRECENT_LEAVEGROUP);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU);
		filter.addAction(BroadCastAction.ACTION_IM_CLEAR_GROUPMESSAGE);
		filter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE.equals(intent
					.getAction())) {
				String otherId = intent.getStringExtra("otherId");
				if (!Tool.isEmpty(otherId)) {
					for (int i = 0; i < items.size(); i++) {
						if (items.get(i).getContactId().equals(otherId)) {
							items.get(i).setTag(
									intent.getIntExtra("topFlag", 0));
							NIMClient.getService(MsgService.class)
									.updateRecent(items.get(i));
							break;
						}
					}
				}
				downRefreshData();
			} else if (BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU// 修改备注后刷新
					.equals(intent.getAction())) {
				downRefreshData();
			} else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {// 网络变化刷新
				ShowNetWorkStatus();
			} else if (BroadCastAction.ACTION_IM_CLEAR_GROUPMESSAGE
					.equals(intent.getAction())) {// 清空群消息刷新
				downRefreshData();
			} else if (BroadCastAction.ACTION_LOGIN_SUCCESS.equals(intent
					.getAction())) {
				ShowNetWorkStatus();
			} else if (BroadCastAction.ACTION_IM_REFRESHRECENT_LEAVEGROUP
					.equals(intent.getAction())) {
				final String leaveGroupTalkId = intent
						.getStringExtra("leaveGroupTalkId");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (items != null) {
							for (int i = items.size() - 1; i >= 0; i--) {
								if (items.get(i).getContactId().toString()
										.equals(leaveGroupTalkId)) {
									NIMClient.getService(MsgService.class)
											.deleteRecentContact(items.get(i));
									items.remove(i);
									break;
								}
							}
							refreshMessages(true);
						}

					}
				}, 1000);

			}
		}
	};

	@Override
	public void onDestroy() {
		removeUpdateCallback();
		getActivity().unregisterReceiver(receiver);
		registerObservers(false);
		registerDropCompletedListener(false);
		super.onDestroy();
	}

	private void registerDropCompletedListener(boolean register) {
		if (DropManager.getInstance().getDropCover() == null) {
			return;
		}

		if (register) {
			DropManager.getInstance().getDropCover()
					.addDropCompletedListener(dropCompletedListener);
		} else {
			DropManager.getInstance().getDropCover()
					.removeDropCompletedListener(dropCompletedListener);
		}
	}

	DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
		@Override
		public void onCompleted(Object id, boolean explosive) {
			if (cached != null && !cached.isEmpty()) {
				// 红点爆裂，已经要清除未读，不需要再刷cached
				if (explosive) {
					if (id instanceof RecentContact) {
						RecentContact r = (RecentContact) id;
						cached.remove(r.getContactId());
					} else if (id instanceof String
							&& ((String) id).contentEquals("0")) {
						cached.clear();
					}
				}

				// 刷cached
				if (!cached.isEmpty()) {
					List<RecentContact> recentContacts = new ArrayList<RecentContact>(
							cached.size());
					recentContacts.addAll(cached.values());
					cached.clear();

					onRecentContactChanged(recentContacts);
				}
			}
		}
	};

	protected void ShowNetWorkStatus() {
		if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
			tv_tips.setText("请先登录！");
			if (items != null) {
				items.clear();
				downRefreshData();
			}
			showConnectTips(true);
		} else {
			if (!NetworkTools.isNetworkAvailable(getActivity())) {
				tv_tips.setText(R.string.netNoUse);
				showConnectTips(true);
			} else {
				showConnectTips(false);
				if (items != null) {
					items.clear();
				}
				msgLoaded = false;
				requestMessages(true);
			}
		}
	}

	/**
	 * 是否显示断开连接的提示
	 * 
	 * @param show
	 */
	private void showConnectTips(boolean show) {
		stateLayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	private void registerObservers(boolean register) {
		MsgServiceObserve service = NIMClient
				.getService(MsgServiceObserve.class);
		service.observeRecentContact(messageObserver, register);
		service.observeMsgStatus(statusObserver, register);
		service.observeRecentContactDeleted(deleteObserver, register);
		service.observeRevokeMessage(revokeMessageObserver, register);
		registerTeamUpdateObserver(register);
		registerTeamMemberUpdateObserver(register);
		registerDropCompletedListener(true);
	}

	Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
		@Override
		public void onEvent(List<RecentContact> recentContacts) {
			if (!DropManager.getInstance().isTouchable()) {
				// 正在拖拽红点，缓存数据
				for (RecentContact r : recentContacts) {
					cached.put(r.getContactId(), r);
				}

				return;
			}
			onRecentContactChanged(recentContacts);
		}
	};

	private void onRecentContactChanged(List<RecentContact> recentContacts) {
		int index;
		for (RecentContact r : recentContacts) {
			index = -1;
			for (int i = 0; i < items.size(); i++) {
				if (r.getContactId().equals(items.get(i).getContactId())
						&& r.getSessionType() == (items.get(i).getSessionType())) {
					index = i;
					break;
				}
			}

			if (index >= 0) {
				items.remove(index);
			}
			items.add(r);
			getLastMsg(r);
		}

		refreshMessages(true);
	}

	private Runnable updateRunnable = new Runnable() {

		@Override
		public void run() {
			refreshMessages(true);
			LogUtil.d("hl", "updateRunnable==========update");
		}
	};

	private Handler updateHandler = new Handler();

	private void getLastMsg(final RecentContact r) {
		if (r.getExtension() != null) {
			return;
		}
		new AsyncTask<Integer, Integer, Boolean>() {
			@Override
			protected void onPostExecute(Boolean result) {
				updateHandler.removeCallbacks(updateRunnable);
				updateHandler.post(updateRunnable);
				super.onPostExecute(result);
			}

			@Override
			protected Boolean doInBackground(Integer... params) {
				int topTag = (Integer) PreferHelper.getSharedParam(
						r.getContactId(), 0);
				if (topTag != 0) {
					PreferHelper.setSharedParam(r.getContactId(), 1);
					addTag(r);
					NIMClient.getService(MsgService.class).updateRecent(r);
				}
				if (r.getSessionType() == SessionTypeEnum.P2P) {// 单聊\
					NIMClient
							.getService(MsgService.class)
							.queryMessageListEx(
									MessageBuilder.createEmptyMessage(
											r.getContactId(),
											r.getSessionType(), 0),
									QueryDirectionEnum.QUERY_OLD, 20, true)
							.setCallback(
									new RequestCallback<List<IMMessage>>() {
										@Override
										public void onSuccess(
												List<IMMessage> arg0) {
											if (null == arg0) {
												return;
											}
											String fromId = r.getFromAccount();
											for (IMMessage imMessage : arg0) {
												if (!TextUtils.isEmpty(fromId)
														&& !fromId
																.equals(AppContext
																		.getInstance()
																		.getUserInfo()
																		.getLeanCloudUserName())
														&& !(r.getAttachment() instanceof NotificationAttachment)
														&& Util.AllowedMessage(imMessage)) {// 非通知类消息
													Map<String, Object> ext = imMessage
															.getRemoteExtension();
													if (ext != null
															&& imMessage
																	.getDirect() == MsgDirectionEnum.In) {
														Map<String, Object> recentExt = r
																.getExtension();
														if (recentExt == null) {
															recentExt = new HashMap<String, Object>();
														}
														recentExt.put(
																"lastMsgStr",
																r.getContent());
														recentExt.putAll(ext);
														r.setExtension(recentExt);
													}
												}
											}
											Map<String, Object> recentExt = r
													.getExtension();
											if (recentExt != null
													&& recentExt
															.containsKey("userId")
													&& recentExt
															.containsKey("nickName")
													&& recentExt
															.containsKey("clientHeadPicUrl")) {// 存本地DB
												int persionId = Integer
														.parseInt(recentExt
																.get("userId")
																.toString());
												String persionNickName = recentExt
														.get("nickName")
														.toString();
												String persionImg = recentExt
														.get("clientHeadPicUrl")
														.toString();
												if (AppContext
														.getFriendTalkIdMap()
														.containsKey(fromId)) {// 是好友存备注名
													persionNickName = AppContext
															.getRemarkMap()
															.get(persionId);
												}
												AppContext.getFriendTalkIdMap()
														.put(fromId, persionId);
												AppContext.getRemarkMap().put(
														persionId,
														persionNickName);
												AppContext.getHeadImgMap().put(
														persionId, persionImg);
												IMYxUserInfo userInfo = new IMYxUserInfo();
												userInfo.setNickName(persionNickName);
												userInfo.setUserHeadImg(persionImg);
												userInfo.setYxAccount(fromId);
												userInfo.setUserId(persionId);
												MsgDataBase.getInstance()
														.saveOrUpdateYxUser(
																userInfo);
											}
										}

										@Override
										public void onFailed(int arg0) {
											items.remove(r);
										}

										@Override
										public void onException(Throwable arg0) {
										}
									});
				} else {// 群聊
					NIMClient
							.getService(MsgService.class)
							.queryMessageListEx(
									MessageBuilder.createEmptyMessage(
											r.getContactId(),
											r.getSessionType(), 0),
									QueryDirectionEnum.QUERY_OLD, 20, true)
							.setCallback(
									new RequestCallback<List<IMMessage>>() {

										@Override
										public void onSuccess(
												List<IMMessage> arg0) {
											String fromId = r.getFromAccount();
											for (IMMessage imMessage : arg0) {
												if (!TextUtils.isEmpty(fromId)
														&& !fromId
																.equals(AppContext
																		.getInstance()
																		.getUserInfo()
																		.getLeanCloudUserName())) {// 非通知类消息
													Map<String, Object> ext = imMessage
															.getRemoteExtension();
													if (ext != null) {
														if (ext.containsKey("userId")
																&& ext.containsKey("nickName")
																&& ext.containsKey("clientHeadPicUrl")
																&& imMessage
																		.getDirect() == MsgDirectionEnum.In) {// user存本地DB
															IMYxUserInfo userInfo = new IMYxUserInfo();
															userInfo.setNickName(ext
																	.get("nickName")
																	.toString());
															userInfo.setUserHeadImg(ext
																	.get("clientHeadPicUrl")
																	.toString());
															userInfo.setYxAccount(fromId);
															userInfo.setUserId(Integer
																	.parseInt(ext
																			.get("userId")
																			.toString()));
															MsgDataBase
																	.getInstance()
																	.saveOrUpdateYxUser(
																			userInfo);
														}
														Map<String, Object> recentExt = r
																.getExtension();
														if (recentExt == null) {
															recentExt = new HashMap<String, Object>();
														}
														recentExt.put(
																"lastMsgStr",
																r.getContent());
														recentExt.putAll(ext);
														r.setExtension(recentExt);
													}
												}

											}
										}

										@Override
										public void onFailed(int arg0) {
											items.remove(r);
										}

										@Override
										public void onException(Throwable arg0) {
										}
									});
				}
				return null;
			}
		}.execute();

	}

	private void refreshMessages(boolean unreadChanged) {
		sortRecentContacts(items);
		adapter.notifyDataSetChanged();
		if (unreadChanged) {

			// 方式一：累加每个最近联系人的未读（快）
			/*
			 * int unreadNum = 0; for (RecentContact r : items) { unreadNum +=
			 * r.getUnreadCount(); }
			 */

			// 方式二：直接从SDK读取（相对慢）
			int unreadNum = NIMClient.getService(MsgService.class)
					.getTotalUnreadCount();

			if (callback != null) {
				callback.onUnreadCountChange(unreadNum);
			}
		}
	}

	/**
	 * **************************** 排序 ***********************************
	 */
	private void sortRecentContacts(List<RecentContact> list) {
		if (list.size() == 0) {
			return;
		}
		Collections.sort(list, comp);
	}

	private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

		@Override
		public int compare(RecentContact o1, RecentContact o2) {
			// 先比较置顶tag
			long tag1 = Tool.isEmpty(o1.getTag()) ? 0 : o1.getTag();
			long tag2 = Tool.isEmpty(o2.getTag()) ? 0 : o2.getTag();
			long sticky = tag1 - tag2;
			if (sticky != 0) {
				return sticky > 0 ? -1 : 1;
			} else {
				long time = o1.getTime() - o2.getTime();
				return time == 0 ? 0 : (time > 0 ? -1 : 1);
			}
		}
	};

	Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
		@Override
		public void onEvent(IMMessage message) {
			int index = getItemIndex(message.getUuid());
			if (index >= 0 && index < items.size()) {
				RecentContact item = items.get(index);
				item.setMsgStatus(message.getStatus());
				refreshViewHolderByIndex(index);
			}
		}
	};

	private int getItemIndex(String uuid) {
		for (int i = 0; i < items.size(); i++) {
			RecentContact item = items.get(i);
			if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
				return i;
			}
		}

		return -1;
	}

	protected void refreshViewHolderByIndex(final int index) {
		Object tag = ListViewUtil.getViewHolderByIndex(
				listview.getRefreshableView(), index);
		if (tag instanceof RecentViewHolder) {
			RecentViewHolder viewHolder = (RecentViewHolder) tag;
			viewHolder.refreshCurrentItem();
		}
	}

	Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
		@Override
		public void onEvent(RecentContact recentContact) {
			if (recentContact != null) {
				for (RecentContact item : items) {
					if (TextUtils.equals(item.getContactId(),
							recentContact.getContactId())
							&& item.getSessionType() == recentContact
									.getSessionType()) {
						items.remove(item);
						refreshMessages(true);
						break;
					}
				}
			} else {
				items.clear();
				refreshMessages(true);
			}
		}
	};

	Observer<IMMessage> revokeMessageObserver = new Observer<IMMessage>() {
		@Override
		public void onEvent(IMMessage message) {
			if (message == null) {
				return;
			}

			// MessageHelper.getInstance().onRevokeMessage(message);
		}
	};

	private boolean msgLoaded = false;
	private List<RecentContact> loadedRecents;

	private void requestMessages(boolean delay) {
		if (msgLoaded) {
			return;
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (msgLoaded) {
					return;
				}
				// 查询最近联系人列表数据
				NIMClient
						.getService(MsgService.class)
						.queryRecentContacts()
						.setCallback(
								new RequestCallbackWrapper<List<RecentContact>>() {

									@Override
									public void onResult(int code,
											List<RecentContact> recents,
											Throwable exception) {
										if (code != ResponseCode.RES_SUCCESS
												|| recents == null) {
											return;
										}
										loadedRecents = recents;

										// 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
										//
										msgLoaded = true;
										if (isAdded()) {
											onRecentContactsLoaded();
										}
									}
								});
			}
		}, delay ? 250 : 0);
	}

	private void onRecentContactsLoaded() {
		items.clear();
		if (loadedRecents != null) {
			items.addAll(loadedRecents);
			loadedRecents = null;
		}
		refreshMessages(true);

		if (callback != null) {
			callback.onRecentContactsLoaded();
		}
	}

	/**
	 * 注册群信息&群成员更新监听
	 */
	private void registerTeamUpdateObserver(boolean register) {
		if (register) {
			TeamDataCache.getInstance().registerTeamDataChangedObserver(
					teamDataChangedObserver);
		} else {
			TeamDataCache.getInstance().unregisterTeamDataChangedObserver(
					teamDataChangedObserver);
		}
	}

	private void registerTeamMemberUpdateObserver(boolean register) {
		if (register) {
			TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(
					teamMemberDataChangedObserver);
		} else {
			TeamDataCache.getInstance()
					.unregisterTeamMemberDataChangedObserver(
							teamMemberDataChangedObserver);
		}
	}

	TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {

		@Override
		public void onUpdateTeams(List<Team> teams) {
			doTeamDataChange();
		}

		@Override
		public void onRemoveTeam(Team team) {
			doTeamDataChange();
		}
	};

	TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {
		@Override
		public void onUpdateTeamMember(List<TeamMember> members) {
			doTeamDataChange();
		}

		@Override
		public void onRemoveTeamMember(TeamMember member) {
			doTeamDataChange();
		}
	};

	protected void doTeamDataChange() {
		// if (items != null) {
		// for (int i = items.size() - 1; i >= 0; i--) {
		// if (items.get(i).getExtension() == null) {
		// items.remove(i);
		// }
		// }
		// }
		uiHandler.removeCallbacks(updateRecentRunnable);
		uiHandler.post(updateRecentRunnable);
	}

	private Handler uiHandler = new Handler();
	private Runnable updateRecentRunnable = new Runnable() {

		@Override
		public void run() {
			LogUtil.d("hl", "updateRecentRunnable==========sendBroadcast");
			refreshMessages(true);
			getActivity().sendBroadcast(
					new Intent(BroadCastAction.ACTION_IM_GROUP_UPDATE));
		}
	};

	private void removeUpdateCallback() {
		if (updateRecentRunnable != null) {
			uiHandler.removeCallbacks(updateRecentRunnable);
		}
	}
}
