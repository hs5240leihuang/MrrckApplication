package com.meiku.dev.ui.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.im.ChatActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 我的群组（群聊）
 * 
 */
public class ChooseGroupActivity extends BaseActivity {

	private PullToRefreshListView mPullToRefreshListView;
	private List<GroupEntity> showList = new ArrayList<GroupEntity>();
	private List<GroupEntity> searchResultList = new ArrayList<GroupEntity>();
	private CommonAdapter<GroupEntity> showAdapter;
	private String shareMessage;
	private ClearEditText etSearch;
	private LinearLayout searchLatout;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_choose_group;
	}

	@Override
	public void initView() {
		regisBroadcast();
		initSeatchBar();
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView
				.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
		mPullToRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// upRefreshData();
					}
				});
		// 适配器
		showAdapter = new CommonAdapter<GroupEntity>(ChooseGroupActivity.this,
				R.layout.item_choosegroup, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final GroupEntity t) {
				// viewHolder.setImageWithNewSize(R.id.iv_grouphead,
				// t.getClientThumbGroupPhoto(), 150, 150);
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_grouphead = new MyRectDraweeView(
						ChooseGroupActivity.this);
				layout_addImage.addView(iv_grouphead,
						new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));
				iv_grouphead.setUrlOfImage(t.getClientThumbGroupPhoto());
				viewHolder.setText(R.id.tv_groupname, t.getGroupName());
				viewHolder.setText(R.id.tv_membernum, "(" + t.getMemberNum()
						+ ")");
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (useType == ConstantKey.SEARCHPAGE_UESTYPE_FORWARD) {
									if (!Tool.isEmpty(forward_message)) {
										final CommonDialog tipDialog = new CommonDialog(
												ChooseGroupActivity.this, "提示",
												"确定发送到" + t.getGroupName()
														+ "?", "确定", "取消");
										tipDialog.show();
										tipDialog
												.setClicklistener(new CommonDialog.ClickListenerInterface() {
													@Override
													public void doConfirm() {
														tipDialog.dismiss();
														IMMessage message = MessageBuilder
																.createForwardMessage(
																		forward_message,
																		t.getOtherId(),
																		SessionTypeEnum.Team);
														if (message == null) {
															ToastUtil
																	.showShortToast("该类型不支持转发");
															return;
														}
														NIMClient
																.getService(
																		MsgService.class)
																.sendMessage(
																		message,
																		false);
														ToastUtil
																.showShortToast("已发送");
														setResult(
																RESULT_OK,
																new Intent()
																		.putExtra(
																				"selectId",
																				t.getOtherId()));
														finish();
													}

													@Override
													public void doCancel() {
														tipDialog.dismiss();
													}
												});
									} else {
										ToastUtil.showShortToast("消息有误，转发失败！");
										finish();
									}

								} else {
									// Intent intent = new Intent(
									// ChooseGroupActivity.this,
									// GroupChatActivity.class);
									// intent.putExtra(
									// ConstantKey.KEY_IM_MULTI_CHATROOM,
									// t.getGroupName());
									// intent.putExtra(
									// ConstantKey.KEY_IM_MULTI_CHATROOMID,
									// t.getId());
									// intent.putExtra(ConstantKey.KEY_SHARE_KEY,
									// shareMessage);
									// startActivity(intent);
									Intent intent = new Intent(
											ChooseGroupActivity.this,
											ChatActivity.class);
									intent.putExtra(
											ConstantKey.KEY_IM_TALKTO_NAME,
											t.getGroupName());
									intent.putExtra(ConstantKey.KEY_IM_TALKTO,
											t.getId());
									intent.putExtra(
											ConstantKey.KEY_IM_TALKTOACCOUNT,
											t.getOtherId());
									intent.putExtra(
											ConstantKey.KEY_IM_SESSIONTYPE,
											XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
									intent.putExtra(ConstantKey.KEY_SHARE_KEY,
											shareMessage);
									startActivity(intent);
									if (!Tool.isEmpty(shareMessage)) {
										finish();
									}
								}
							}
						});
			}
		};
		mPullToRefreshListView.setAdapter(showAdapter);
	}

	/**
	 * 是否显示搜索条
	 */
	private void isShowSeatchBar(boolean show) {
		searchLatout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 初始化搜索条
	 */
	private void initSeatchBar() {
		searchLatout = (LinearLayout) findViewById(R.id.searchLL);
		etSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		etSearch.setHint("请输入群组关键词搜索");
		etSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				etSearch.requestFocus();
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String currentContent = arg0.toString();
				if (Tool.isEmpty(currentContent)) {
					showAdapter.setmDatas(showList);
					showAdapter.notifyDataSetChanged();
				} else {
					searchResultList.clear();
					for (int i = 0, size = showList.size(); i < size; i++) {
						GroupEntity ge = showList.get(i);
						String groupName = ge.getGroupName();
						String groupName_pinyin = PinyinUtil.spell(groupName);// 名字全拼音
						String nameForShort = PinyinUtil.getForShort(groupName);// 名字缩写
						if (groupName.contains(currentContent)
								|| groupName_pinyin.contains(currentContent)
								|| nameForShort.contains(currentContent
										.toUpperCase())) {
							searchResultList.add(ge);
						}
					}
					showAdapter.setmDatas(searchResultList);
					showAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		downRefreshData();
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_GROUP_DISMISS);// 有人解散群或者被踢出群
		this.registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_GROUP_DISMISS.equals(intent.getAction())) {
				downRefreshData();
			}
		}
	};

	private int useType = 0;

	private IMMessage forward_message;

	protected void downRefreshData() {
		showList.clear();
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("loginUserId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_SEARCH_GROUP));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
	}

	@Override
	public void initValue() {
		showAdapter.notifyDataSetChanged();
		// 做选择群组发分享使用
		shareMessage = getIntent().getStringExtra(ConstantKey.KEY_SHARE_KEY);
		if (shareMessage != null) {
			((TextView) findViewById(R.id.center_txt_title)).setText("选择群组");
		}
		Bundle b = getIntent().getExtras();
		if (b != null) {
			useType = b.getInt("useType", 0);
		}
		if (useType == ConstantKey.SEARCHPAGE_UESTYPE_FORWARD) {// 转发消息3
			forward_message = (IMMessage) b
					.getSerializable(XmppConstant.IMMESSAGE_KEY);
			((TextView) findViewById(R.id.center_txt_title)).setText("选择群组");
		}
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			LogUtil.e("NATHAN:" + reqBase.getBody().toString());
			String jsonstr = reqBase.getBody().get("group").toString();
			if (!Tool.isEmpty(jsonstr) && jsonstr.length() > 2) {
				showList = (List<GroupEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<GroupEntity>>() {
						}.getType());
				showAdapter.setmDatas(showList);
				if (!Tool.isEmpty(showList)) {
					AppContext.setGroupList(showList);
				}else{
					ToastUtil.showShortToast("暂无任何群组信息");
				}
			}else{
				ToastUtil.showShortToast("暂无任何群组信息");
			}
			showAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
