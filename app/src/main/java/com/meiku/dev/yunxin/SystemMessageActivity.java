package com.meiku.dev.yunxin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.yunxin.AutoRefreshListView.OnRefreshListener;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.team.TeamService;

public class SystemMessageActivity extends BaseActivity implements
		OnRefreshListener, SystemMessageViewHolder.SystemMessageListener,
		TAdapterDelegate {

	private MessageListView listView;
	private static final int LOAD_MESSAGE_COUNT = 10;
	private static final boolean MERGE_ADD_FRIEND_VERIFY = false; // 是否要合并好友申请，同一个用户仅保留最近一条申请内容（默认不合并）

	// adapter
	private SystemMessageAdapter adapter;
	private List<SystemMessage> items = new ArrayList<SystemMessage>();
	private Set<Long> itemIds = new HashSet<Long>();

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.system_notification_message_activity;
	}

	@Override
	protected void onResume() {
		super.onResume();

		NIMClient.getService(SystemMessageService.class)
				.resetSystemMessageUnreadCount();
	}

	@Override
	protected void onStop() {
		super.onStop();

		NIMClient.getService(SystemMessageService.class)
				.resetSystemMessageUnreadCount();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		registerSystemObserver(false);
	}

	@Override
	public void initView() {
		listView = (MessageListView) findViewById(R.id.messageListView);
		listView.setMode(AutoRefreshListView.Mode.END);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		// adapter
		listView.setAdapter(adapter);
		// listener
		listView.setOnRefreshListener(this);
		adapter = new SystemMessageAdapter(this, items, this, this);
		loadMessages(); // load old data
		registerSystemObserver(true);
	}

	private void registerSystemObserver(boolean register) {
		NIMClient.getService(SystemMessageObserver.class)
				.observeReceiveSystemMsg(systemMessageObserver, register);
	}

	Observer<SystemMessage> systemMessageObserver = new Observer<SystemMessage>() {
		@Override
		public void onEvent(SystemMessage systemMessage) {
			onIncomingMessage(systemMessage);
		}
	};

	

	private boolean firstLoad = true;

	public static void start(Context context) {
		start(context, null, true);
	}

	public static void start(Context context, Intent extras, boolean clearTop) {
		Intent intent = new Intent();
		intent.setClass(context, SystemMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		if (clearTop) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
		return SystemMessageViewHolder.class;
	}

	@Override
	public boolean enabled(int position) {
		return false;
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefreshFromStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefreshFromEnd() {
		loadMessages(); // load old data
	}

	private int loadOffset = 0;
	private Set<String> addFriendVerifyRequestAccounts = new HashSet<String>(); // 发送过好友申请的账号（好友申请合并用）

	/**
	 * 加载历史消息
	 */
	public void loadMessages() {
		listView.onRefreshStart(AutoRefreshListView.Mode.END);
		boolean loadCompleted; // 是否已经加载完成，后续没有数据了or已经满足本次请求数量
		int validMessageCount = 0; // 实际加载的数量（排除被过滤被合并的条目）
		List<String> messageFromAccounts = new ArrayList<String>(
				LOAD_MESSAGE_COUNT);

		while (true) {
			List<SystemMessage> temps = NIMClient.getService(
					SystemMessageService.class).querySystemMessagesBlock(
					loadOffset, LOAD_MESSAGE_COUNT);

			loadOffset += temps.size();
			loadCompleted = temps.size() < LOAD_MESSAGE_COUNT;

			int tempValidCount = 0;

			for (SystemMessage m : temps) {
				// 去重
				if (duplicateFilter(m)) {
					continue;
				}

				// 同一个账号的好友申请仅保留最近一条
				if (addFriendVerifyFilter(m)) {
					continue;
				}

				// 保存有效消息
				items.add(m);
				tempValidCount++;
				if (!messageFromAccounts.contains(m.getFromAccount())) {
					messageFromAccounts.add(m.getFromAccount());
				}

				// 判断是否达到请求数
				if (++validMessageCount >= LOAD_MESSAGE_COUNT) {
					loadCompleted = true;
					// 已经满足要求，此时需要修正loadOffset
					loadOffset -= temps.size();
					loadOffset += tempValidCount;

					break;
				}
			}

			if (loadCompleted) {
				break;
			}
		}

		// 更新数据源，刷新界面
		refresh();

		boolean first = firstLoad;
		firstLoad = false;
		if (first) {
			ListViewUtil.scrollToPosition(listView, 0, 0); // 第一次加载后显示顶部
		}

		listView.onRefreshComplete(validMessageCount, LOAD_MESSAGE_COUNT, true);

		// 收集未知用户资料的账号集合并从远程获取
		// collectAndRequestUnknownUserInfo(messageFromAccounts);
	}

	private void refresh() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

	// 去重
	private boolean duplicateFilter(final SystemMessage msg) {
		if (itemIds.contains(msg.getMessageId())) {
			return true;
		}

		itemIds.add(msg.getMessageId());
		return false;
	}

	// 同一个账号的好友申请仅保留最近一条
	private boolean addFriendVerifyFilter(final SystemMessage msg) {
		if (!MERGE_ADD_FRIEND_VERIFY) {
			return false; // 不需要MERGE，不过滤
		}

		if (msg.getType() != SystemMessageType.AddFriend) {
			return false; // 不过滤
		}

		AddFriendNotify attachData = (AddFriendNotify) msg.getAttachObject();
		if (attachData == null) {
			return true; // 过滤
		}

		if (attachData.getEvent() != AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
			return false; // 不过滤
		}

		if (addFriendVerifyRequestAccounts.contains(msg.getFromAccount())) {
			return true; // 过滤
		}

		addFriendVerifyRequestAccounts.add(msg.getFromAccount());
		return false; // 不过滤
	}

	 /**
     * 新消息到来
     */
    private void onIncomingMessage(final SystemMessage message) {
        // 同一个账号的好友申请仅保留最近一条
        if (addFriendVerifyFilter(message)) {
            SystemMessage del = null;
            for (SystemMessage m : items) {
                if (m.getFromAccount().equals(message.getFromAccount()) && m.getType() == SystemMessageType.AddFriend) {
                    AddFriendNotify attachData = (AddFriendNotify) m.getAttachObject();
                    if (attachData != null && attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                        del = m;
                        break;
                    }
                }
            }

            if (del != null) {
                items.remove(del);
            }
        }

        loadOffset++;
        items.add(0, message);

        refresh();

//        // 收集未知用户资料的账号集合并从远程获取
//        List<String> messageFromAccounts = new ArrayList<String>(1);
//        messageFromAccounts.add(message.getFromAccount());
//        collectAndRequestUnknownUserInfo(messageFromAccounts);
    }
    
    @Override
    public void onAgree(SystemMessage message) {
        onSystemNotificationDeal(message, true);
    }

    @Override
    public void onReject(SystemMessage message) {
        onSystemNotificationDeal(message, false);
    }

    @Override
    public void onLongPressed(SystemMessage message) {
        showLongClickMenu(message);
    }

    private void onSystemNotificationDeal(final SystemMessage message, final boolean pass) {
        RequestCallback callback = new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                onProcessSuccess(pass, message);
            }

            @Override
            public void onFailed(int code) {
                onProcessFailed(code, message);
            }

            @Override
            public void onException(Throwable exception) {

            }
        };
        if (message.getType() == SystemMessageType.TeamInvite) {
            if (pass) {
                NIMClient.getService(TeamService.class).acceptInvite(message.getTargetId(), message.getFromAccount()).setCallback(callback);
            } else {
                NIMClient.getService(TeamService.class).declineInvite(message.getTargetId(), message.getFromAccount(), "").setCallback(callback);
            }

        } else if (message.getType() == SystemMessageType.ApplyJoinTeam) {
            if (pass) {
                NIMClient.getService(TeamService.class).passApply(message.getTargetId(), message.getFromAccount()).setCallback(callback);
            } else {
                NIMClient.getService(TeamService.class).rejectApply(message.getTargetId(), message.getFromAccount(), "").setCallback(callback);
            }
        } else if (message.getType() == SystemMessageType.AddFriend) {
            NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(), pass).setCallback(callback);
        }
    }

    private void onProcessSuccess(final boolean pass, SystemMessage message) {
        SystemMessageStatus status = pass ? SystemMessageStatus.passed : SystemMessageStatus.declined;
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(),
                status);
        message.setStatus(status);
        refreshViewHolder(message);
    }

    private void onProcessFailed(final int code, SystemMessage message) {
        Toast.makeText(SystemMessageActivity.this, "failed, error code=" + code,
                Toast.LENGTH_LONG).show();
        if (code == 408) {
            return;
        }

        SystemMessageStatus status = SystemMessageStatus.expired;
        NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(message.getMessageId(),
                status);
        message.setStatus(status);
        refreshViewHolder(message);
    }
    
    private void refreshViewHolder(final SystemMessage message) {
        final long messageId = message.getMessageId();

        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            SystemMessage item = items.get(i);
            if (messageId == item.getMessageId()) {
                index = i;
                break;
            }
        }

        if (index < 0) {
            return;
        }

        final int m = index;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (m < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(listView, m);
                if (tag instanceof SystemMessageViewHolder) {
                    ((SystemMessageViewHolder) tag).refreshDirectly(message);
                }
            }
        });
    }
    
    private void showLongClickMenu(final SystemMessage message) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        alertDialog.setTitle("删除提示");
        alertDialog.addItem("删除该条系统消息？", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                deleteSystemMessage(message);
            }
        });
        alertDialog.show();
    }

    private void deleteSystemMessage(final SystemMessage message) {
        NIMClient.getService(SystemMessageService.class).deleteSystemMessage(message.getMessageId());
        items.remove(message);
        refresh();
        ToastUtil.showShortToast("删除成功");
    }
}