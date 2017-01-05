package com.meiku.dev.ui.activitys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.chat.ChooseGroupActivity;
import com.meiku.dev.ui.im.AddressListSearchActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.ui.service.GetLoacationActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.NetworkUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.RecordButton;
import com.meiku.dev.yunxin.AutoRefreshListView;
import com.meiku.dev.yunxin.CustomAlertDialog;
import com.meiku.dev.yunxin.FileBrowserActivity;
import com.meiku.dev.yunxin.ListViewUtil;
import com.meiku.dev.yunxin.MessageAudioControl;
import com.meiku.dev.yunxin.MessageListPanelHelper;
import com.meiku.dev.yunxin.MessageListView;
import com.meiku.dev.yunxin.MsgAdapter;
import com.meiku.dev.yunxin.MsgViewHolderBase;
import com.meiku.dev.yunxin.MsgViewHolderFactory;
import com.meiku.dev.yunxin.TAdapterDelegate;
import com.meiku.dev.yunxin.TViewHolder;
import com.meiku.dev.yunxin.UserPreferences;
import com.meiku.dev.yunxin.VoiceTrans;
import com.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

public abstract class BaseChatActivity extends BaseActivity implements
		OnClickListener {

	/** 刷新控件 */
	protected MessageListView mPullRefreshListView;
	/** 适配器 */
	protected MsgAdapter adapter;
	/** 标题栏右侧按钮 */
	protected ImageView right_res_title;
	/** 标题栏右侧文字 */
	protected TextView right_txt_title;
	/** 标题栏标题 */
	protected TextView center_txt_title;
	protected LinearLayout chatAudioLayout, chatAddLayout;
	protected LinearLayout chatTextLayout;
	protected View sendBtn, turnToAudioBtn;
	/** 表情布局 */
	protected LinearLayout chatEmotionLayout;
	/** 录音按钮 */
	private RecordButton recordBtn;
	/** 输入框 */
	protected EmotionEditText contentEdit;

	protected final int TAKE_PHOTO_IMG = 1000;
	protected final int TO_SELECT_VIDEO = 1001;
	protected final int GET_ADDRESS = 1002;
	protected final int REQCODE_CHOISECARD = 1003;
	protected final int CHOISEVIDEO = 1004;
	protected final int CHOISEFILE = 1005;
	protected final int MANAGEGROUP = 1006;
	protected final int FORWARDMESSAGE = 1007;
	protected LinearLayout headLayout;
	private Button showAddBtn;

	protected List<com.netease.nimlib.sdk.msg.model.IMMessage> items = new ArrayList<com.netease.nimlib.sdk.msg.model.IMMessage>();
	protected String talkToAccount;
	protected SessionTypeEnum sessionType = SessionTypeEnum.P2P;
	// 从服务器拉取消息记录
	private boolean remote = false;
	// 仅显示消息记录，不接收和发送消息
	private boolean recordOnly = false;
	// 当前是否可以消息长按
	public boolean isItemLongClickEnabled = true;
	// 待转发消息
	protected com.netease.nimlib.sdk.msg.model.IMMessage forwardMessage;
	private Button comeMsgTip;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_chat;
	}

	/**
	 * 当前是否群聊天使用
	 * 
	 * @return
	 */
	protected boolean isTeamTalk() {
		return sessionType == SessionTypeEnum.Team;
	}

	@Override
	public void initView() {
		initTitlebar();
		initPullListView();
		initBottomBar();
		init();
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitlebar() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_res_title = (ImageView) findViewById(R.id.right_res_title);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		headLayout = (LinearLayout) findViewById(R.id.headLayout);
	}

	/**
	 * 是否显示headview
	 * 
	 * @param show
	 */
	public void isShowHeadView(boolean show) {
		headLayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 初始化下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (MessageListView) findViewById(R.id.messageListView);
		mPullRefreshListView.requestDisallowInterceptTouchEvent(true);
		mPullRefreshListView.setMode(AutoRefreshListView.Mode.START);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mPullRefreshListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		adapter = new MsgAdapter(this, items, new TAdapterDelegate() {

			@Override
			public int getViewTypeCount() {
				return MsgViewHolderFactory.getViewTypeCount();
			}

			@Override
			public Class<? extends TViewHolder> viewHolderAtPosition(
					int position) {
				return MsgViewHolderFactory.getViewHolderByType(items
						.get(position));
			}

			@Override
			public boolean enabled(int position) {
				return false;
			}
		});
		adapter.setEventListener(new MsgItemEventListener());
		mPullRefreshListView.setAdapter(adapter);
		mPullRefreshListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// 触摸隐藏键盘
				hideBottomLayout();
				hideSoftInputView();
				return false;
			}
		});
	}

	/**
	 * 取历史消息
	 */
	public void setMessageLoader() {
		if (adapter != null) {
			mPullRefreshListView.setOnRefreshListener(new MessageLoader(null,
					remote));
		}
	}

	/**
	 * 底部输入栏初始化
	 */
	private void initBottomBar() {
		comeMsgTip = (Button) findViewById(R.id.comeMsgTip);
		chatTextLayout = (LinearLayout) findViewById(R.id.chatTextLayout);
		chatAudioLayout = (LinearLayout) findViewById(R.id.chatRecordLayout);
		chatAddLayout = (LinearLayout) findViewById(R.id.chatAddLayout);
		showAddBtn = (Button) findViewById(R.id.showAddBtn);
		contentEdit = (EmotionEditText) findViewById(R.id.textEdit);
		turnToAudioBtn = findViewById(R.id.turnToAudioBtn);
		sendBtn = findViewById(R.id.sendBtn);
		chatEmotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		chatEmotionLayout.addView(new EmotionView(BaseChatActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						StringBuffer sb = new StringBuffer(contentEdit
								.getText());
						int start = contentEdit.getSelectionStart();
						if (emotionText.equals(":l7:")) {// 删除图标
							if (start >= 4
									&& EmotionHelper.getLocalEmoMap(
											BaseChatActivity.this).containsKey(
											sb.substring(start - 4, start))) {
								sb.replace(start - 4, start, "");
								contentEdit.setText(sb.toString());
								CharSequence info = contentEdit.getText();
								if (info instanceof Spannable) {
									Spannable spannable = (Spannable) info;
									Selection
											.setSelection(spannable, start - 4);
								}
								return;
							}
							return;
						}
						sb.replace(contentEdit.getSelectionStart(),
								contentEdit.getSelectionEnd(), emotionText);
						contentEdit.setText(sb.toString());
						CharSequence info = contentEdit.getText();
						if (info instanceof Spannable) {
							Spannable spannable = (Spannable) info;
							Selection.setSelection(spannable, start
									+ emotionText.length());
						}
					}
				}).getView());
		contentEdit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				doScrollToLast();
				return false;
			}
		});
		initRecordBtn();
	}

	/**
	 * 语音按钮
	 */
	public void initRecordBtn() {
		recordBtn = (RecordButton) findViewById(R.id.recordBtn);
		recordBtn
				.setRecordEventListener(new RecordButton.RecordEventListener() {
					@Override
					public void onFinishedRecord(final String audioPath,
							int secs) {
						// 发送语音消息
						sendAudioMessage(audioPath, secs);
					}

					@Override
					public void onStartRecord() {

					}
				});
	}

	/**
	 * 发送语音消息
	 * 
	 * @param secs
	 */
	public abstract void sendAudioMessage(String audioPath, int secs);

	/**
	 * 发送文字消息
	 */
	public abstract void sendTxtMessage();

	/**
	 * 下拉刷新事件
	 */
	public abstract void doPullDownToRefresh();

	/**
	 * 标题栏左侧按钮事件
	 */
	public abstract void doTitleLeftBtnClick();

	/**
	 * 标题栏右侧按钮事件
	 * 
	 * @param id
	 */
	public abstract void doTitleRightBtnClick(int id);

	/**
	 * 初始化
	 */
	public abstract void init();

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		right_res_title.setOnClickListener(this);
		right_txt_title.setOnClickListener(this);
		setEditTextChangeListener();
		findViewById(R.id.turnToTextBtn).setOnClickListener(this);
		showAddBtn.setOnClickListener(this);
		findViewById(R.id.showEmotionBtn).setOnClickListener(this);
		findViewById(R.id.addImg).setOnClickListener(this);
		findViewById(R.id.addCardBtn).setOnClickListener(this);
		findViewById(R.id.addLocationBtn).setOnClickListener(this);
		findViewById(R.id.addVideoBtn).setOnClickListener(this);
		findViewById(R.id.addFile).setOnClickListener(this);
		// findViewById(R.id.layoutVideo).setVisibility(View.GONE);
		turnToAudioBtn.setOnClickListener(this);
		contentEdit.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		comeMsgTip.setOnClickListener(this);
	}

	/**
	 * 输入框监听
	 */
	public void setEditTextChangeListener() {
		contentEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
				if (charSequence.length() > 0) {
					sendBtn.setEnabled(true);
					showSendBtn();
				} else {
					sendBtn.setEnabled(false);
					showTurnToRecordBtn();
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}

	public void showSendBtn() {
		sendBtn.setVisibility(View.VISIBLE);
		showAddBtn.setVisibility(View.GONE);
	}

	private void showTurnToRecordBtn() {
		sendBtn.setVisibility(View.GONE);
		showAddBtn.setVisibility(View.VISIBLE);
	}

	public void showRightTxtOrImg(boolean showRightTxt) {
		if (showRightTxt) {
			right_res_title.setVisibility(View.GONE);
			right_txt_title.setVisibility(View.VISIBLE);
		} else {
			right_res_title.setVisibility(View.VISIBLE);
			right_txt_title.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			doTitleLeftBtnClick();
			break;
		case R.id.right_res_title:
		case R.id.right_txt_title:
			doTitleRightBtnClick(v.getId());
			break;
		case R.id.sendBtn:// 发送文字
			// if (activityType != XmppConstant.IM_CHAT_TALKTYPE_SINGLE
			// && !XmppTool.checkIsConnectToService())
			// return;
			sendTxtMessage();
			break;
		case R.id.turnToAudioBtn:
			showAudioLayout();
			break;
		case R.id.turnToTextBtn:
			showTextLayout();
			break;
		case R.id.showAddBtn:
			toggleBottomAddLayout();
			break;
		case R.id.showEmotionBtn:
			toggleEmotionLayout();
			break;
		case R.id.textEdit:
			hideBottomLayoutAnddoScrollToLast();
			break;
		case R.id.addImg:// 添加图片
			// if (activityType != XmppConstant.IM_CHAT_TALKTYPE_SINGLE
			// && !XmppTool.checkIsConnectToService())
			// return;
			selectPicture();
			break;
		case R.id.addCardBtn:// 添加名片
			// if (activityType != XmppConstant.IM_CHAT_TALKTYPE_SINGLE
			// && !XmppTool.checkIsConnectToService())
			// return;
			ChoisePersonCard();
			break;
		case R.id.addLocationBtn:// 添加地址
			// if (activityType != XmppConstant.IM_CHAT_TALKTYPE_SINGLE
			// && !XmppTool.checkIsConnectToService())
			// return;
			startActivityForResult(
					new Intent(this, GetLoacationActivity.class), GET_ADDRESS);
			break;
		case R.id.addVideoBtn:// 添加小视屏
			// if (activityType != XmppConstant.IM_CHAT_TALKTYPE_SINGLE
			// && !XmppTool.checkIsConnectToService())
			// return;

			// startActivityForResult(new Intent(this,
			// MultiVideoSelectorActivity.class).putExtra(
			// MultiVideoSelectorActivity.EXTRA_SELECT_MODE,
			// MultiVideoSelectorActivity.MODE_SINGLE), CHOISEVIDEO);

			Intent i = new Intent(this, SelectPictureActivity.class);
			i.putExtra(ConstantKey.BUNDLE_SELECT_VIDEO, true);// 选择视频模式
			startActivityForResult(i, TO_SELECT_VIDEO);
			break;
		case R.id.addFile:
			FileBrowserActivity.startActivityForResult(this, CHOISEFILE);
			break;
		case R.id.comeMsgTip:
			comeMsgTip.setVisibility(View.GONE);
			doScrollToLast();
			break;
		}

	}

	/**
	 * 选择好友名片
	 */
	private void ChoisePersonCard() {
		Intent intent = new Intent(this, AddressListSearchActivity.class);
		intent.putExtra("useType", ConstantKey.SEARCHPAGE_UESTYPE_CARD);// useType=1选择好友发名片
		startActivityForResult(intent, REQCODE_CHOISECARD);
	}

	/**
	 * 显示发语音栏
	 */
	private void showAudioLayout() {
		chatTextLayout.setVisibility(View.GONE);
		chatAudioLayout.setVisibility(View.VISIBLE);
		hideBottomLayout();
		hideSoftInputView();
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInputView() {
		InputTools.HideKeyboard(contentEdit);
	}

	/**
	 * 显示发文字栏
	 */
	private void showTextLayout() {
		chatTextLayout.setVisibility(View.VISIBLE);
		chatAudioLayout.setVisibility(View.GONE);
		hideBottomLayoutAnddoScrollToLast();
		contentEdit.requestFocus();
		InputTools.ShowKeyboard(contentEdit);
	}

	/**
	 * 是否显示底部添加更多
	 */
	private void toggleBottomAddLayout() {
		if (chatAddLayout.getVisibility() == View.VISIBLE) {
			showAddLayout(false);
		} else {
			showEmotionLayout(false);
			hideSoftInputView();
			showAddLayout(true);
		}
	}

	/**
	 * 表情栏
	 */
	private void toggleEmotionLayout() {
		if (chatEmotionLayout.getVisibility() == View.VISIBLE) {
			showEmotionLayout(false);
		} else {
			chatTextLayout.setVisibility(View.VISIBLE);
			chatAudioLayout.setVisibility(View.GONE);
			contentEdit.requestFocus();
			hideSoftInputView();
			if (chatAddLayout.getVisibility() == View.VISIBLE) {
				showAddLayout(false);
				showEmotionLayout(true);
			} else {
				showAddLayout(false);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						showEmotionLayout(true);
					}
				}, 200);
			}
		}
	}

	/**
	 * 是否显示更多
	 * 
	 * @param show
	 */
	private void showAddLayout(boolean show) {
		chatAddLayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 是否显示表情栏
	 * 
	 * @param show
	 */
	private void showEmotionLayout(boolean show) {
		chatEmotionLayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 隐藏底部栏并滚动到最后
	 */
	private void hideBottomLayoutAnddoScrollToLast() {
		hideBottomLayout();
		doScrollToLast();
	}

	/**
	 * 隐藏底部栏
	 */
	protected void hideBottomLayout() {
		showAddLayout(false);
		showEmotionLayout(false);
	}

	/**
	 * 滚动到最后
	 */
	public void doScrollToLast() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mPullRefreshListView.postDelayed(new Runnable() {
					@Override
					public void run() {
						ListViewUtil.scrollToBottom(mPullRefreshListView);
					}
				}, 200);
			}
		});
	}

	/**
	 * 结束刷新
	 */
	public void refreshComplete() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mPullRefreshListView.post(new Runnable() {
					@Override
					public void run() {
						mPullRefreshListView.onRefreshComplete();
					}
				});
			}
		});
	}

	/**
	 * 选择图片
	 */
	private void selectPicture() {
		// Intent intent = new Intent(this, SelectPictureActivity.class);
		// intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);//
		// 选择模式
		// intent.putExtra("MAX_NUM", 1);// 选择的张数
		// startActivityForResult(intent, TAKE_PHOTO_IMG);
		startActivityForResult(new Intent(this,
				MultiImageSelectorActivity.class), TAKE_PHOTO_IMG);
		toggleBottomAddLayout();
	}

	/**
	 * ****************** 观察者 **********************
	 */
	protected void registerObservers(boolean register) {
		MsgServiceObserve service = NIMClient
				.getService(MsgServiceObserve.class);
		service.observeMsgStatus(messageStatusObserver, register);
		service.observeAttachmentProgress(attachmentProgressObserver, register);
		service.observeRevokeMessage(revokeMessageObserver, register);
		service.observeReceiveMessage(incomingMessageObserver, register);
		service.observeMessageReceipt(messageReceiptObserver, register);
		MessageListPanelHelper.getInstance().registerObserver(
				incomingLocalMessageObserver, register);
	}

	/**
	 * 消息撤回观察者
	 */
	Observer<com.netease.nimlib.sdk.msg.model.IMMessage> revokeMessageObserver = new Observer<com.netease.nimlib.sdk.msg.model.IMMessage>() {
		@Override
		public void onEvent(com.netease.nimlib.sdk.msg.model.IMMessage message) {
			if (message == null
					|| !talkToAccount.equals(message.getSessionId())) {
				return;
			}
			deleteItem(message, false);
		}
	};

	// 删除消息
	private void deleteItem(
			com.netease.nimlib.sdk.msg.model.IMMessage messageItem,
			boolean isRelocateTime) {
		NIMClient.getService(MsgService.class).deleteChattingHistory(
				messageItem);
		List<com.netease.nimlib.sdk.msg.model.IMMessage> messages = new ArrayList<com.netease.nimlib.sdk.msg.model.IMMessage>();
		for (com.netease.nimlib.sdk.msg.model.IMMessage message : items) {
			if (message.getUuid().equals(messageItem.getUuid())) {
				continue;
			}
			messages.add(message);
		}
		updateReceipt(messages);
		adapter.deleteItem(messageItem, isRelocateTime);
	}

	/**
	 * 消息附件上传/下载进度观察者
	 */
	Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
		@Override
		public void onEvent(AttachmentProgress progress) {
			onAttachmentProgressChange(progress);
		}
	};

	private void onAttachmentProgressChange(AttachmentProgress progress) {
		int index = getItemIndex(progress.getUuid());
		if (index >= 0 && index < items.size()) {
			com.netease.nimlib.sdk.msg.model.IMMessage item = items.get(index);
			float value = (float) progress.getTransferred()
					/ (float) progress.getTotal();
			adapter.putProgress(item, value);
			refreshViewHolderByIndex(index);
		}
	}

	/**
	 * 消息状态变化观察者
	 */
	Observer<com.netease.nimlib.sdk.msg.model.IMMessage> messageStatusObserver = new Observer<com.netease.nimlib.sdk.msg.model.IMMessage>() {
		@Override
		public void onEvent(com.netease.nimlib.sdk.msg.model.IMMessage message) {
			if (isMyMessage(message)) {
				onMessageStatusChange(message);
			}
		}
	};

	private int getItemIndex(String uuid) {
		for (int i = 0; i < items.size(); i++) {
			com.netease.nimlib.sdk.msg.model.IMMessage message = items.get(i);
			if (TextUtils.equals(message.getUuid(), uuid)) {
				return i;
			}
		}
		return -1;
	}

	private void onMessageStatusChange(
			com.netease.nimlib.sdk.msg.model.IMMessage message) {
		int index = getItemIndex(message.getUuid());
		if (index >= 0 && index < items.size()) {
			com.netease.nimlib.sdk.msg.model.IMMessage item = items.get(index);
			item.setStatus(message.getStatus());
			item.setAttachStatus(message.getAttachStatus());
			if (item.getAttachment() instanceof AudioAttachment) {
				item.setAttachment(message.getAttachment());
			}
			refreshViewHolderByIndex(index);
		}
	}

	/**
	 * 刷新单条消息
	 * 
	 * @param index
	 */
	private void refreshViewHolderByIndex(final int index) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (index < 0) {
					return;
				}
				Object tag = ListViewUtil.getViewHolderByIndex(
						mPullRefreshListView, index);
				if (tag instanceof MsgViewHolderBase) {
					MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;
					viewHolder.refreshCurrentItem();
				}
			}
		});
	}

	/**
	 * 本地消息接收观察者
	 */
	MessageListPanelHelper.LocalMessageObserver incomingLocalMessageObserver = new MessageListPanelHelper.LocalMessageObserver() {
		@Override
		public void onAddMessage(
				com.netease.nimlib.sdk.msg.model.IMMessage message) {
			if (message == null
					|| !talkToAccount.equals(message.getSessionId())) {
				return;
			}

			onMsgSend(message);
		}

		@Override
		public void onClearMessages(String account) {
			items.clear();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
		}
	};

	// 发送消息后，更新本地消息列表
	public void onMsgSend(com.netease.nimlib.sdk.msg.model.IMMessage message) {
		// add to listView and refresh
		items.add(message);
		List<com.netease.nimlib.sdk.msg.model.IMMessage> addedListItems = new ArrayList<com.netease.nimlib.sdk.msg.model.IMMessage>(
				1);
		addedListItems.add(message);
		adapter.updateShowTimeItem(addedListItems, false, true);

		adapter.notifyDataSetChanged();
		ListViewUtil.scrollToBottom(mPullRefreshListView);
	}

	/**
	 * 消息接收观察者
	 */
	Observer<List<com.netease.nimlib.sdk.msg.model.IMMessage>> incomingMessageObserver = new Observer<List<com.netease.nimlib.sdk.msg.model.IMMessage>>() {
		@Override
		public void onEvent(
				List<com.netease.nimlib.sdk.msg.model.IMMessage> messages) {
			if (messages == null || messages.isEmpty()) {
				return;
			}
			onIncomingMessage(messages);// 处理消息
			sendMsgReceipt(); // 发送已读回执
		}
	};

	private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
		@Override
		public void onEvent(List<MessageReceipt> messageReceipts) {
			receiveReceipt();
		}
	};
	public VoiceTrans voiceTrans;

	/**
	 * 发送已读回执（需要过滤）
	 */
	public void sendMsgReceipt() {
		if (talkToAccount == null || sessionType != SessionTypeEnum.P2P) {
			return;
		}
		com.netease.nimlib.sdk.msg.model.IMMessage message = getLastReceivedMessage();
		if (!sendReceiptCheck(message)) {
			return;
		}
		NIMClient.getService(MsgService.class).sendMessageReceipt(
				talkToAccount, message);
	}

	private com.netease.nimlib.sdk.msg.model.IMMessage getLastReceivedMessage() {
		com.netease.nimlib.sdk.msg.model.IMMessage lastMessage = null;
		for (int i = items.size() - 1; i >= 0; i--) {
			if (sendReceiptCheck(items.get(i))) {
				lastMessage = items.get(i);
				break;
			}
		}
		return lastMessage;
	}

	private boolean sendReceiptCheck(
			final com.netease.nimlib.sdk.msg.model.IMMessage msg) {
		if (msg == null || msg.getDirect() != MsgDirectionEnum.In
				|| msg.getMsgType() == MsgTypeEnum.tip
				|| msg.getMsgType() == MsgTypeEnum.notification) {
			return false; // 非收到的消息，Tip消息和通知类消息，不要发已读回执
		}
		return true;
	}

	/**
	 * 收到已读回执
	 */
	/**
	 * 收到已读回执（更新VH的已读label）
	 */

	public void receiveReceipt() {
		updateReceipt(items);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

	public void updateReceipt(
			final List<com.netease.nimlib.sdk.msg.model.IMMessage> messages) {
		for (int i = messages.size() - 1; i >= 0; i--) {
			if (receiveReceiptCheck(messages.get(i))) {
				adapter.setUuid(messages.get(i).getUuid());
				break;
			}
		}
	}

	private boolean receiveReceiptCheck(
			final com.netease.nimlib.sdk.msg.model.IMMessage msg) {
		if (msg != null && msg.getSessionType() == SessionTypeEnum.P2P
				&& msg.getDirect() == MsgDirectionEnum.Out
				&& msg.getMsgType() != MsgTypeEnum.tip
				&& msg.getMsgType() != MsgTypeEnum.notification
				&& msg.isRemoteRead()) {
			return true;
		}

		return false;
	}

	public void onIncomingMessage(
			List<com.netease.nimlib.sdk.msg.model.IMMessage> messages) {
		boolean needScrollToBottom = ListViewUtil
				.isLastMessageVisible(mPullRefreshListView);
		boolean needRefresh = false;
		List<com.netease.nimlib.sdk.msg.model.IMMessage> addedListItems = new ArrayList<com.netease.nimlib.sdk.msg.model.IMMessage>(
				messages.size());
		for (com.netease.nimlib.sdk.msg.model.IMMessage message : messages) {
			if (isMyMessage(message) && Util.AllowedMessage(message)) {
				items.add(message);
				addedListItems.add(message);
				needRefresh = true;
			}
		}
		if (needRefresh) {
			sortMessages(items);
			adapter.notifyDataSetChanged();
		}
		adapter.updateShowTimeItem(addedListItems, false, true);
		IMMessage lastMsg = messages.get(messages.size() - 1);
		if (isMyMessage(lastMsg)) {
			if (needScrollToBottom) {
				ListViewUtil.scrollToBottom(mPullRefreshListView);
			} else if (comeMsgTip != null
					&& lastMsg.getSessionType() != SessionTypeEnum.ChatRoom) {
				comeMsgTip.setVisibility(View.VISIBLE);
				uiHandler.removeCallbacks(showNewMessageTipLayoutRunnable);
				uiHandler
						.postDelayed(showNewMessageTipLayoutRunnable, 5 * 1000);
			}
		}
	}

	private Handler uiHandler = new Handler();
	private Runnable showNewMessageTipLayoutRunnable = new Runnable() {

		@Override
		public void run() {
			comeMsgTip.setVisibility(View.GONE);
		}
	};

	private void removeHandlerCallback() {
		if (showNewMessageTipLayoutRunnable != null) {
			uiHandler.removeCallbacks(showNewMessageTipLayoutRunnable);
		}
	}

	public boolean isMyMessage(
			com.netease.nimlib.sdk.msg.model.IMMessage message) {
		return message.getSessionType() == sessionType
				&& message.getSessionId() != null
				&& message.getSessionId().equals(talkToAccount);
	}

	/**
	 * **************************** 排序 ***********************************
	 */
	private void sortMessages(
			List<com.netease.nimlib.sdk.msg.model.IMMessage> list) {
		if (list.size() == 0) {
			return;
		}
		Collections.sort(list, comp);
	}

	private static Comparator<com.netease.nimlib.sdk.msg.model.IMMessage> comp = new Comparator<com.netease.nimlib.sdk.msg.model.IMMessage>() {

		@Override
		public int compare(com.netease.nimlib.sdk.msg.model.IMMessage o1,
				com.netease.nimlib.sdk.msg.model.IMMessage o2) {
			long time = o1.getTime() - o2.getTime();
			return time == 0 ? 0 : (time < 0 ? -1 : 1);
		}
	};

	private class MessageLoader implements
			AutoRefreshListView.OnRefreshListener {

		private static final int LOAD_MESSAGE_COUNT = 20;

		private QueryDirectionEnum direction = null;

		private com.netease.nimlib.sdk.msg.model.IMMessage anchor;
		private boolean remote;

		private boolean firstLoad = true;

		public MessageLoader(com.netease.nimlib.sdk.msg.model.IMMessage anchor,
				boolean remote) {
			this.anchor = anchor;
			this.remote = remote;
			if (remote) {
				loadFromRemote();
			} else {
				if (anchor == null) {
					loadFromLocal(QueryDirectionEnum.QUERY_OLD);
				} else {
					// 加载指定anchor的上下文
					loadAnchorContext();
				}
			}
		}

		private RequestCallback<List<com.netease.nimlib.sdk.msg.model.IMMessage>> callback = new RequestCallbackWrapper<List<com.netease.nimlib.sdk.msg.model.IMMessage>>() {
			@Override
			public void onResult(int code,
					List<com.netease.nimlib.sdk.msg.model.IMMessage> messages,
					Throwable exception) {
				if (messages != null) {
					onMessageLoaded(messages);
				}
			}
		};

		private void loadAnchorContext() {
			this.direction = QueryDirectionEnum.QUERY_OLD;
			mPullRefreshListView.onRefreshStart(AutoRefreshListView.Mode.START);
			NIMClient
					.getService(MsgService.class)
					.queryMessageListEx(anchor(), direction,
							LOAD_MESSAGE_COUNT, true)
					.setCallback(
							new RequestCallbackWrapper<List<com.netease.nimlib.sdk.msg.model.IMMessage>>() {
								@Override
								public void onResult(
										int code,
										List<com.netease.nimlib.sdk.msg.model.IMMessage> messages,
										Throwable exception) {
									if (code != ResponseCode.RES_SUCCESS
											|| exception != null) {
										return;
									}
									onMessageLoaded(messages);

									// query new
									direction = QueryDirectionEnum.QUERY_NEW;
									mPullRefreshListView
											.onRefreshStart(AutoRefreshListView.Mode.END);
									NIMClient
											.getService(MsgService.class)
											.queryMessageListEx(anchor(),
													direction,
													LOAD_MESSAGE_COUNT, true)
											.setCallback(
													new RequestCallbackWrapper<List<com.netease.nimlib.sdk.msg.model.IMMessage>>() {
														@Override
														public void onResult(
																int code,
																List<com.netease.nimlib.sdk.msg.model.IMMessage> messages,
																Throwable exception) {
															if (code != ResponseCode.RES_SUCCESS
																	|| exception != null) {
																return;
															}
															onMessageLoaded(messages);
															// scroll to
															// position
															scrollToAnchor(anchor);
														}
													});
								}
							});
		}

		private void loadFromLocal(QueryDirectionEnum direction) {
			this.direction = direction;
			mPullRefreshListView
					.onRefreshStart(direction == QueryDirectionEnum.QUERY_NEW ? AutoRefreshListView.Mode.END
							: AutoRefreshListView.Mode.START);
			NIMClient
					.getService(MsgService.class)
					.queryMessageListEx(anchor(), direction,
							LOAD_MESSAGE_COUNT, true).setCallback(callback);
		}

		private void loadFromRemote() {
			this.direction = QueryDirectionEnum.QUERY_OLD;
			NIMClient.getService(MsgService.class)
					.pullMessageHistory(anchor(), LOAD_MESSAGE_COUNT, true)
					.setCallback(callback);
		}

		private com.netease.nimlib.sdk.msg.model.IMMessage anchor() {
			if (items.size() == 0) {
				return anchor == null ? MessageBuilder.createEmptyMessage(
						talkToAccount, sessionType, 0) : anchor;
			} else {
				int index = (direction == QueryDirectionEnum.QUERY_NEW ? items
						.size() - 1 : 0);
				return items.get(index);
			}
		}

		/**
		 * 历史消息加载处理
		 * 
		 * @param messages
		 */
		private void onMessageLoaded(
				List<com.netease.nimlib.sdk.msg.model.IMMessage> messages) {
			int count = messages.size();

			if (remote) {
				Collections.reverse(messages);
			}

			if (firstLoad && items.size() > 0) {
				// 在第一次加载的过程中又收到了新消息，做一下去重
				for (com.netease.nimlib.sdk.msg.model.IMMessage message : messages) {
					for (com.netease.nimlib.sdk.msg.model.IMMessage item : items) {
						if (item.isTheSame(message)) {
							items.remove(item);
							break;
						}
					}
				}
			}

			if (firstLoad && anchor != null) {
				items.add(anchor);
			}

			List<com.netease.nimlib.sdk.msg.model.IMMessage> result = new ArrayList<com.netease.nimlib.sdk.msg.model.IMMessage>();
			for (com.netease.nimlib.sdk.msg.model.IMMessage message : messages) {
				// 屏蔽通知类消息
				if (Util.AllowedMessage(message)) {
					result.add(message);
				}
			}
			if (direction == QueryDirectionEnum.QUERY_NEW) {
				items.addAll(result);
			} else {
				items.addAll(0, result);
			}

			// 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
			if (firstLoad) {
				ListViewUtil.scrollToBottom(mPullRefreshListView);
				sendMsgReceipt(); // 发送已读回执
			}

			adapter.updateShowTimeItem(items, true, firstLoad);
			updateReceipt(items); // 更新已读回执标签

			refreshMessageList();
			mPullRefreshListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT,
					true);

			firstLoad = false;
		}

		/**
		 * *************** OnRefreshListener ***************
		 */
		@Override
		public void onRefreshFromStart() {
			if (remote) {
				loadFromRemote();
			} else {
				loadFromLocal(QueryDirectionEnum.QUERY_OLD);
			}
		}

		@Override
		public void onRefreshFromEnd() {
			if (!remote) {
				loadFromLocal(QueryDirectionEnum.QUERY_NEW);
			}
		}
	}

	// 刷新消息列表
	public void refreshMessageList() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

	public void scrollToAnchor(
			final com.netease.nimlib.sdk.msg.model.IMMessage anchor) {
		if (anchor == null) {
			return;
		}

		int position = -1;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getUuid().equals(anchor.getUuid())) {
				position = i;
				break;
			}
		}

		if (position >= 0) {
			final int jumpTo = position == 0 ? 0 : position
					+ mPullRefreshListView.getHeaderViewsCount();
			mPullRefreshListView.postDelayed(new Runnable() {
				@Override
				public void run() {
					ListViewUtil.scrollToPosition(
							mPullRefreshListView,
							jumpTo,
							jumpTo == 0 ? 0 : ScreenUtil.dip2px(
									BaseChatActivity.this, 30));
				}
			}, 30);
		}
	}

	private class MsgItemEventListener implements
			MsgAdapter.ViewHolderEventListener {

		@Override
		public void onFailedBtnClick(
				com.netease.nimlib.sdk.msg.model.IMMessage message) {
			if (message.getDirect() == MsgDirectionEnum.Out) {
				// 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
				if (message.getStatus() == MsgStatusEnum.fail) {
					resendMessage(message); // 重发
				} else {
					if (message.getAttachment() instanceof FileAttachment) {
						FileAttachment attachment = (FileAttachment) message
								.getAttachment();
						if (TextUtils.isEmpty(attachment.getPath())
								&& TextUtils.isEmpty(attachment.getThumbPath())) {
							showReDownloadConfirmDlg(message);
						}
					} else {
						resendMessage(message);
					}
				}
			} else {
				showReDownloadConfirmDlg(message);
			}
		}

		@Override
		public boolean onViewHolderLongClick(View clickView,
				View viewHolderView,
				com.netease.nimlib.sdk.msg.model.IMMessage item) {
			if (isItemLongClickEnabled) {
				showLongClickAction(item);
			}
			return true;
		}

		// 重新下载(对话框提示)
		private void showReDownloadConfirmDlg(
				final com.netease.nimlib.sdk.msg.model.IMMessage message) {
			final CommonDialog commonDialog = new CommonDialog(
					BaseChatActivity.this, "提示", "重新下载?", "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							commonDialog.dismiss();
							if (message.getAttachment() != null
									&& message.getAttachment() instanceof FileAttachment)
								NIMClient.getService(MsgService.class)
										.downloadAttachment(message, true);
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
		}

		// 重发消息到服务器
		private void resendMessage(
				com.netease.nimlib.sdk.msg.model.IMMessage message) {
			// 重置状态为unsent
			int index = getItemIndex(message.getUuid());
			if (index >= 0 && index < items.size()) {
				com.netease.nimlib.sdk.msg.model.IMMessage item = items
						.get(index);
				item.setStatus(MsgStatusEnum.sending);
				refreshViewHolderByIndex(index);
			}
			NIMClient.getService(MsgService.class).sendMessage(message, true);
		}

		/**
		 * ****************************** 长按菜单 ********************************
		 */

		// 长按消息Item后弹出菜单控制
		private void showLongClickAction(
				com.netease.nimlib.sdk.msg.model.IMMessage selectedItem) {
			onNormalLongClick(selectedItem);
		}

		/**
		 * 长按菜单操作
		 * 
		 * @param item
		 */
		private void onNormalLongClick(
				com.netease.nimlib.sdk.msg.model.IMMessage item) {
			CustomAlertDialog alertDialog = new CustomAlertDialog(
					BaseChatActivity.this);
			alertDialog.setCancelable(true);
			alertDialog.setCanceledOnTouchOutside(true);

			prepareDialogItems(item, alertDialog);
			alertDialog.show();
		}

		// 长按消息item的菜单项准备。如果消息item的MsgViewHolder处理长按事件(MsgViewHolderBase#onItemLongClick),且返回为true，
		// 则对应项的长按事件不会调用到此处
		private void prepareDialogItems(
				final com.netease.nimlib.sdk.msg.model.IMMessage selectedItem,
				CustomAlertDialog alertDialog) {
			MsgTypeEnum msgType = selectedItem.getMsgType();

			MessageAudioControl.getInstance(BaseChatActivity.this).stopAudio();

			// 0 EarPhoneMode
			longClickItemEarPhoneMode(alertDialog, msgType);
			// 1 resend
			longClickItemResend(selectedItem, alertDialog);
			// 2 copy
			longClickItemCopy(selectedItem, alertDialog, msgType);
			// 3 revoke
			if (selectedItem.getDirect() == MsgDirectionEnum.Out
					&& selectedItem.getStatus() == MsgStatusEnum.success
					&& !recordOnly) {
				longClickRevokeMsg(selectedItem, alertDialog);
			}
			// 4 delete
			longClickItemDelete(selectedItem, alertDialog);
			// 5 trans
			// longClickItemVoidToText(selectedItem, alertDialog, msgType);

			if (!shouldIgnoreZhuanfa(selectedItem) && !recordOnly) {
				// 6 forward to person
				longClickItemForwardToPerson(selectedItem, alertDialog);
				// 7 forward to team
				longClickItemForwardToTeam(selectedItem, alertDialog);
			}
		}

		// 长按菜单项--重发
		private void longClickItemResend(
				final com.netease.nimlib.sdk.msg.model.IMMessage item,
				CustomAlertDialog alertDialog) {
			if (item.getStatus() != MsgStatusEnum.fail) {
				return;
			}
			alertDialog.addItem(getString(R.string.repeat_send_has_blank),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							onResendMessageItem(item);
						}
					});
		}

		private void onResendMessageItem(
				com.netease.nimlib.sdk.msg.model.IMMessage message) {
			int index = getItemIndex(message.getUuid());
			if (index >= 0) {
				showResendConfirm(message, index); // 重发确认
			}
		}

		private void showResendConfirm(
				final com.netease.nimlib.sdk.msg.model.IMMessage message,
				final int index) {

			final CommonDialog commonDialog = new CommonDialog(
					BaseChatActivity.this, "提示",
					getString(R.string.repeat_send_message), "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							commonDialog.dismiss();
							resendMessage(message);
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
		}

		// 长按菜单项--复制
		private void longClickItemCopy(
				final com.netease.nimlib.sdk.msg.model.IMMessage item,
				CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
			if (msgType != MsgTypeEnum.text) {
				return;
			}
			alertDialog.addItem(getString(R.string.copy_has_blank),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							onCopyMessageItem(item);
						}
					});
		}

		private void onCopyMessageItem(
				com.netease.nimlib.sdk.msg.model.IMMessage item) {
			ClipboardManager cm = (ClipboardManager) BaseChatActivity.this
					.getSystemService(Context.CLIPBOARD_SERVICE);
			// 将文本数据复制到剪贴板
			if (cm != null) {
				String emotionStr = EmotionHelper.getSendEmotion(
						BaseChatActivity.this, item.getContent());
				cm.setText(emotionStr);
			}
			ToastUtil.showShortToast("复制成功");
		}

		// 长按菜单项--删除
		private void longClickItemDelete(
				final com.netease.nimlib.sdk.msg.model.IMMessage selectedItem,
				CustomAlertDialog alertDialog) {
			if (recordOnly) {
				return;
			}
			alertDialog.addItem(getString(R.string.delete_has_blank),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							deleteItem(selectedItem, true);
						}
					});
		}

		// 长按菜单项 -- 音频转文字
		private void longClickItemVoidToText(
				final com.netease.nimlib.sdk.msg.model.IMMessage item,
				CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
			if (msgType != MsgTypeEnum.audio)
				return;

			if (item.getDirect() == MsgDirectionEnum.In
					&& item.getAttachStatus() != AttachStatusEnum.transferred)
				return;
			if (item.getDirect() == MsgDirectionEnum.Out
					&& item.getAttachStatus() != AttachStatusEnum.transferred)
				return;

			alertDialog.addItem(getString(R.string.voice_to_text),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							onVoiceToText(item);
						}
					});
		}

		// 语音转文字
		private void onVoiceToText(
				com.netease.nimlib.sdk.msg.model.IMMessage item) {
			if (voiceTrans == null)
				voiceTrans = new VoiceTrans(BaseChatActivity.this);
			voiceTrans.voiceToText(item);
			if (item.getDirect() == MsgDirectionEnum.In
					&& item.getStatus() != MsgStatusEnum.read) {
				item.setStatus(MsgStatusEnum.read);
				NIMClient.getService(MsgService.class).updateIMMessageStatus(
						item);
				adapter.notifyDataSetChanged();
			}
		}

		// 长按菜单项 -- 听筒扬声器切换
		private void longClickItemEarPhoneMode(CustomAlertDialog alertDialog,
				MsgTypeEnum msgType) {
			if (msgType != MsgTypeEnum.audio)
				return;

			String content = null;
			if (UserPreferences.isEarPhoneModeEnable()) {
				content = "切换成扬声器播放";
			} else {
				content = "切换成听筒播放";
			}
			final String finalContent = content;
			alertDialog.addItem(content,
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							ToastUtil.showShortToast(finalContent);
							setEarPhoneMode(!UserPreferences
									.isEarPhoneModeEnable());
						}
					});
		}

		// 长按菜单项 -- 转发到个人
		private void longClickItemForwardToPerson(
				final com.netease.nimlib.sdk.msg.model.IMMessage item,
				CustomAlertDialog alertDialog) {
			alertDialog.addItem(getString(R.string.forward_to_person),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							forwardMessage = item;
							Bundle b = new Bundle();
							b.putSerializable(XmppConstant.IMMESSAGE_KEY,
									forwardMessage);
							b.putInt("useType",
									ConstantKey.SEARCHPAGE_UESTYPE_FORWARD);
							startActivityForResult(new Intent(
									BaseChatActivity.this,
									AddressListSearchActivity.class)
									.putExtras(b), FORWARDMESSAGE);
						}
					});
		}

		// 长按菜单项 -- 转发到群组
		private void longClickItemForwardToTeam(
				final com.netease.nimlib.sdk.msg.model.IMMessage item,
				CustomAlertDialog alertDialog) {
			alertDialog.addItem(getString(R.string.forward_to_team),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							forwardMessage = item;
							Bundle b = new Bundle();
							b.putSerializable(XmppConstant.IMMESSAGE_KEY,
									forwardMessage);
							b.putInt("useType",
									ConstantKey.SEARCHPAGE_UESTYPE_FORWARD);
							startActivityForResult(new Intent(
									BaseChatActivity.this,
									ChooseGroupActivity.class).putExtras(b),
									FORWARDMESSAGE);
						}
					});
		}

		// 长按菜单项 -- 撤回消息
		private void longClickRevokeMsg(
				final com.netease.nimlib.sdk.msg.model.IMMessage item,
				CustomAlertDialog alertDialog) {
			alertDialog.addItem(getString(R.string.withdrawn_msg),
					new CustomAlertDialog.onSeparateItemClickListener() {

						@Override
						public void onClick() {
							if (!NetworkUtil
									.isNetAvailable(BaseChatActivity.this)) {
								ToastUtil
										.showShortToast(getString(R.string.network_is_not_available));
								return;
							}
							NIMClient.getService(MsgService.class)
									.revokeMessage(item)
									.setCallback(new RequestCallback<Void>() {
										@Override
										public void onSuccess(Void param) {
											deleteItem(item, false);
											DoOnRevokeMessage(item);
										}

										@Override
										public void onFailed(int code) {
											if (code == ResponseCode.RES_OVERDUE) {
												ToastUtil
														.showShortToast(getString(R.string.revoke_failed));
											} else {
												ToastUtil
														.showShortToast("revoke msg failed, code:"
																+ code);
											}
										}

										@Override
										public void onException(
												Throwable exception) {

										}
									});
						}
					});
		}

	}

	// 消息撤回
	public abstract void DoOnRevokeMessage(IMMessage item);

	public boolean shouldIgnoreZhuanfa(
			com.netease.nimlib.sdk.msg.model.IMMessage message) {
		if (message.getDirect() == MsgDirectionEnum.In
				&& (message.getAttachStatus() == AttachStatusEnum.transferring || message
						.getAttachStatus() == AttachStatusEnum.fail)) {
			// 接收到的消息，附件没有下载成功，不允许转发
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (voiceTrans != null && voiceTrans.isShow()) {// 语音转文字页面先消失
				voiceTrans.hide();
				return true;
			} else {
				removeHandlerCallback();
				doTitleLeftBtnClick();
			}
		}
		return false;
	}

	private void setEarPhoneMode(boolean earPhoneMode) {
		UserPreferences.setEarPhoneModeEnable(earPhoneMode);
		MessageAudioControl.getInstance(BaseChatActivity.this)
				.setEarPhoneModeEnable(earPhoneMode);
	}
}
