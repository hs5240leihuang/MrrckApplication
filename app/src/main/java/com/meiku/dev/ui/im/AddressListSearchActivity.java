package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.chat.ChooseGroupActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.InputTools;
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
 * 通讯录搜索
 * 
 */
public class AddressListSearchActivity extends BaseActivity {
	private PullToRefreshListView mPullToRefreshListView;
	private CommonAdapter<FriendEntity> showAdapter;
	private TextView cancel;
	private ClearEditText etSearch;
	private List<FriendEntity> friendsList = new ArrayList<FriendEntity>();
	private int useType;
	private List<FriendEntity> allFriendsList;
	private String shareMessage;
	private TextView tv_gotogroup;
	private com.netease.nimlib.sdk.msg.model.IMMessage forward_message;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_search;
	}

	@Override
	public void initView() {
		allFriendsList = AppContext.getAddressList();
		((TextView) findViewById(R.id.tv_titleTip)).setText("通讯录好友");
		etSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		etSearch.setHint("请输入群组关键词搜索");
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (!Tool.isEmpty(allFriendsList)) {
					String currentContent = etSearch.getText().toString()
							.trim();
					friendsList.clear();
					if (Tool.isEmpty(currentContent)) {
						friendsList.addAll(allFriendsList);
						showAdapter.notifyDataSetChanged();
						return;
					}
					for (int i = 0, size = allFriendsList.size(); i < size; i++) {
						FriendEntity fe = allFriendsList.get(i);
						String aliasName = fe.getAliasName();
						String aliasName_pinyin = PinyinUtil.spell(aliasName);// 名字全拼音
						String nameForShort = PinyinUtil.getForShort(aliasName);// 名字缩写
						if (aliasName.contains(currentContent)
								|| aliasName_pinyin.contains(currentContent)
								|| nameForShort.contains(currentContent
										.toUpperCase())) {
							friendsList.add(fe);
						}
					}
					showAdapter.notifyDataSetChanged();
				}
			}
		});
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setTextSize(17);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(etSearch);
				finish();
			}
		});
		initPullListView();
		tv_gotogroup = (TextView) findViewById(R.id.tv_gotogroup);
		tv_gotogroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle b = new Bundle();
				b.putSerializable(XmppConstant.IMMESSAGE_KEY, forward_message);
				b.putInt("useType", ConstantKey.SEARCHPAGE_UESTYPE_FORWARD);
				startActivityForResult(new Intent(
						AddressListSearchActivity.this,
						ChooseGroupActivity.class).putExtras(b), reqCodeOne);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				setResult(
						RESULT_OK,
						new Intent().putExtra("selectId",
								data.getStringExtra("selectId")));
				finish();
			}
		}
	}

	@Override
	public void initValue() {
		// 搜索通讯录0，选择好友发名片1，转发消息2，
		Bundle b = getIntent().getExtras();
		if (b != null) {
			useType = b.getInt("useType", 0);
		}
		shareMessage = getIntent().getStringExtra(ConstantKey.KEY_SHARE_KEY);
		if (useType == ConstantKey.SEARCHPAGE_UESTYPE_FORWARD) {
			tv_gotogroup.setVisibility(View.VISIBLE);
			forward_message = (IMMessage) b
					.getSerializable(XmppConstant.IMMESSAGE_KEY);
		}
	}

	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
		showAdapter = new CommonAdapter<FriendEntity>(this,
				R.layout.item_roster, friendsList) {

			@Override
			public void convert(ViewHolder viewHolder, final FriendEntity fe) {
				viewHolder.setText(R.id.tv_name, fe.getAliasName());
				viewHolder.setText(R.id.tv_major, fe.getPositionName());
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(
						AddressListSearchActivity.this);
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(fe.getClientThumbHeadPicUrl());
				// viewHolder
				// .setImage(R.id.majorImg, fe.getClientPositionImgUrl());
				String intro = fe.getIntroduce();
				if (Tool.isEmpty(intro)) {
					intro = "我期望通过手艺交同行朋友";
				}
				viewHolder.setText(R.id.tv_intro, intro);
				LinearLayout layout_sex = viewHolder.getView(R.id.layout_sex);
				ImageView iv_sex = viewHolder.getView(R.id.iv_sex);
				if ("1".equals(fe.getGender())) {
					iv_sex.setBackgroundResource(R.drawable.nan_white);
					layout_sex.setBackgroundResource(R.drawable.sex_bg_man);
				} else {
					iv_sex.setBackgroundResource(R.drawable.nv_white);
					layout_sex.setBackgroundResource(R.drawable.sex_bg_woman);
				}
				layout_addImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(mContext,
								PersonShowActivity.class);
						intent.putExtra(PersonShowActivity.TO_USERID_KEY,
								fe.getFriendId() + "");
						intent.putExtra("nickName", fe.getAliasName());
						mContext.startActivity(intent);
					}
				});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								switch (useType) {
								case ConstantKey.SEARCHPAGE_UESTYPE_SEARCH:
									Intent i = new Intent(mContext,
											ChatActivity.class);
									i.putExtra(ConstantKey.KEY_IM_TALKTO,
											fe.getFriendId());
									i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
											fe.getAliasName());
									i.putExtra(
											ConstantKey.KEY_IM_TALKTOACCOUNT,
											fe.getLeanCloudUserName());
									i.putExtra(
											ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
											fe.getClientHeadPicUrl());
									mContext.startActivity(i);
									finish();
									break;
								case ConstantKey.SEARCHPAGE_UESTYPE_CARD:// 发送名片
									final CommonDialog commonDialog = new CommonDialog(
											AddressListSearchActivity.this,
											"提示", "发送" + fe.getAliasName()
													+ "的名片到当前聊天？", "确定", "取消");
									commonDialog.show();
									commonDialog
											.setClicklistener(new CommonDialog.ClickListenerInterface() {
												@Override
												public void doConfirm() {
													Intent intent = new Intent();
													intent.putExtra(
															ConstantKey.KEY_IM_TALKTO,
															fe.getFriendId()
																	+ "");
													intent.putExtra(
															ConstantKey.KEY_IM_TALKTO_NAME,
															fe.getAliasName());
													intent.putExtra(
															ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
															fe.getClientHeadPicUrl());
													setResult(RESULT_OK, intent);
													finish();
													commonDialog.dismiss();
												}

												@Override
												public void doCancel() {
													commonDialog.dismiss();
												}
											});
									break;
								case ConstantKey.SEARCHPAGE_UESTYPE_SHARE:// 发送分享
									Intent sendShareIntent = new Intent(
											mContext, ChatActivity.class);
									sendShareIntent.putExtra(
											ConstantKey.KEY_IM_TALKTO,
											fe.getFriendId());
									sendShareIntent.putExtra(
											ConstantKey.KEY_IM_TALKTOACCOUNT,
											fe.getLeanCloudUserName());
									sendShareIntent.putExtra(
											ConstantKey.KEY_IM_TALKTO_NAME,
											fe.getAliasName());
									sendShareIntent
											.putExtra(
													ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
													fe.getClientHeadPicUrl());
									sendShareIntent.putExtra(
											ConstantKey.KEY_SHARE_KEY,
											shareMessage);
									mContext.startActivity(sendShareIntent);
									finish();
									break;
								case ConstantKey.SEARCHPAGE_UESTYPE_FORWARD:// 转发消息
									if (!Tool.isEmpty(forward_message)) {
										final CommonDialog tipDialog = new CommonDialog(
												AddressListSearchActivity.this,
												"提示", "确定发送给"
														+ fe.getAliasName()
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
																		fe.getLeanCloudUserName(),
																		SessionTypeEnum.P2P);
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
																				fe.getLeanCloudUserName()));
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

									break;
								default:
									break;
								}
							}
						});

			}
		};

		if (!Tool.isEmpty(allFriendsList)) {
			friendsList.addAll(allFriendsList);
		}
		mPullToRefreshListView.setAdapter(showAdapter);
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}
				});
		mPullToRefreshListView.getRefreshableView().setOnTouchListener(
				new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// 触摸隐藏键盘
						InputTools.HideKeyboard(etSearch);
						return false;
					}
				});
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
