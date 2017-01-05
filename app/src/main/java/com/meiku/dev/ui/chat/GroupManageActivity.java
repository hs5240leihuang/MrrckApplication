package com.meiku.dev.ui.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.GroupManageImageBean;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.QRcodeUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonEditDialog;
import com.meiku.dev.views.CommonEditDialog.EditClickOkListener;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

public class GroupManageActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout layout_groupname, layout_groupqrcpde,
			layout_groupnotification, layout_mygroupname, layout_allmember,
			layout_grouphead;
	private TextView tv_groupname, tv_membernum, tv_notification,
			tv_mygroupname, tv_cleanchatlog, center_txt_title, tv_groupnumber;
	private CommonAdapter<GroupManageImageBean> showAdapter;
	private List<GroupManageImageBean> showList = new ArrayList<GroupManageImageBean>();
	private GridView gv_head;// 头部表格
	private ImageView iv_qrcode;
	private ImageView tb_message, tb_public, tb_needYZ, tb_top;
	private Button btn_quit;
	private int chatRoomId;
	private GroupEntity groupEntity;
	private CommonDialog commonDialog;
	private List<String> picPathList = new ArrayList<String>();// 选择的图片路径
	private List<FormFileBean> formFileBeans;
	private FormFileBean formFile;
	protected final int upDateGroupNickName = 1;
	protected String myGroupNickName;
	private final int reqCodeFive = 500;
	public final int reqFlag = 900;
	public final int reqIS = 1000;
	public final int reqDisturb = 901;
	private int publicFlag; // 0公开,1私有
	private int msgFlag;// 0:正常接收,1:设置免打扰
	private MySimpleDraweeView iv_grouphead;
	private int needAgressFlag;// 0不需要,1需要
	private LinearLayout lin_iscommit;
	private TextView tv_hint;
	private Integer topFlag;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_group_manage;
	}

	@Override
	public void initView() {
		findViewById(R.id.goback).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(ConstantKey.RESULT_REFRESH_GROUP);
				finish();
			}
		});
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		lin_iscommit = (LinearLayout) findViewById(R.id.lin_iscommit);
		tv_groupnumber = (TextView) findViewById(R.id.tv_groupnumber);
		layout_grouphead = (LinearLayout) findViewById(R.id.layout_grouphead);
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		iv_grouphead = new MySimpleDraweeView(GroupManageActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(iv_grouphead, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		gv_head = (GridView) findViewById(R.id.gv_head);
		layout_allmember = (LinearLayout) findViewById(R.id.layout_allmember);
		tv_membernum = (TextView) findViewById(R.id.tv_membernum);
		layout_groupname = (LinearLayout) findViewById(R.id.layout_groupname);
		tv_groupname = (TextView) findViewById(R.id.tv_groupname);
		layout_groupqrcpde = (LinearLayout) findViewById(R.id.layout_groupqrcpde);
		iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
		layout_groupnotification = (LinearLayout) findViewById(R.id.layout_groupnotification);
		tv_notification = (TextView) findViewById(R.id.tv_notification);
		tb_message = (ImageView) findViewById(R.id.tb_message);
		tb_top = (ImageView) findViewById(R.id.tb_top);
		tb_public = (ImageView) findViewById(R.id.tb_public);
		tb_needYZ = (ImageView) findViewById(R.id.tb_needYZ);
		layout_mygroupname = (LinearLayout) findViewById(R.id.layout_mygroupname);
		tv_mygroupname = (TextView) findViewById(R.id.tv_mygroupname);
		tv_cleanchatlog = (TextView) findViewById(R.id.tv_cleanchatlog);
		btn_quit = (Button) findViewById(R.id.btn_quit);
		showAdapter = new CommonAdapter<GroupManageImageBean>(
				GroupManageActivity.this, R.layout.item_groupmanage, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final GroupManageImageBean t) {
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				if (t.getType() == 0) {
					// viewHolder.setImageWithNewSize(R.id.image,
					// t.getClientThumbHeadPicUrl(), 150, 150);
					MyRectDraweeView image = new MyRectDraweeView(
							GroupManageActivity.this);
					layout_addImage.removeAllViews();
					layout_addImage.addView(image,
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
					image.setUrlOfImage(t.getClientThumbHeadPicUrl());
					viewHolder.setText(R.id.tv_username, t.getNickName());
					layout_addImage
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent intent = new Intent(
											GroupManageActivity.this,
											PersonShowActivity.class);
									intent.putExtra(
											PersonShowActivity.TO_USERID_KEY,
											t.getUserId() + "");
									intent.putExtra("nickName", t.getNickName());
									startActivity(intent);
								}
							});
				} else {
					viewHolder.setText(R.id.tv_username, "");
					if (t.getType() == 1) {
						ImageView iv = new ImageView(GroupManageActivity.this);
						layout_addImage.removeAllViews();
						layout_addImage.addView(iv,
								new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.MATCH_PARENT));
						iv.setImageResource(Integer.parseInt(t
								.getClientPicUrl()));
						iv.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivityForResult(new Intent(
										GroupManageActivity.this,
										AddGroupMemberActivity.class).putExtra(
										"groupId", chatRoomId + ""), 1);
							}
						});
					} else {
						ImageView iv2 = new ImageView(GroupManageActivity.this);
						layout_addImage.removeAllViews();
						layout_addImage.addView(iv2,
								new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.MATCH_PARENT));
						iv2.setImageResource(Integer.parseInt(t
								.getClientPicUrl()));
						iv2.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivityForResult(new Intent(
										GroupManageActivity.this,
										DeleteMemberActivity.class).putExtra(
										"groupId", chatRoomId + ""), 1);
							}
						});
					}
				}
			}

		};
		gv_head.setAdapter(showAdapter);
		// for(int i = 0 ; i < 1;i++){
		// ImageView imageView = new ImageView(this);
		// LinearLayout.LayoutParams params = new
		// LinearLayout.LayoutParams(ScreenUtil.dip2px(this,100),
		// ScreenUtil.dip2px(this,100));
		// params.setMargins(5, 0, 0, 0);
		// imageView.setScaleType(ScaleType.CENTER_CROP);
		// imageView.setImageResource(R.drawable.sichat_placehold_icon);
		// imageView.setLayoutParams(params);
		// //ImageLoader.getInstance().displayImage(
		// imageList.get(i).getClientPicUrl()+"_thumb.png", imageView);
		// gv_head.addView(imageView);
		// }

	}

	@Override
	public void initValue() {
		chatRoomId = getIntent().getIntExtra(
				ConstantKey.KEY_IM_MULTI_CHATROOMID, 0);
		getGroupInformation();
	}

	private void getGroupInformation() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", chatRoomId);
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_NEARBY_GROUPINFORMATION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	@Override
	public void bindListener() {
		layout_groupname.setOnClickListener(this);
		layout_groupqrcpde.setOnClickListener(this);
		layout_groupnotification.setOnClickListener(this);
		layout_mygroupname.setOnClickListener(this);
		tv_cleanchatlog.setOnClickListener(this);
		layout_allmember.setOnClickListener(this);
		layout_grouphead.setOnClickListener(this);
		tb_public.setOnClickListener(this);
		tb_needYZ.setOnClickListener(this);
		tb_message.setOnClickListener(this);
		tb_top.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("成功");
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESHRECENT_LEAVEGROUP)
					.putExtra("leaveGroupTalkId", groupEntity.getOtherId()));// 发广播刷新消息页面
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeTwo:
			LogUtil.e(resp.getBody().toString());
			if (resp.getBody().has("group")
					&& (resp.getBody().get("group") + "").length() > 4) {
				groupEntity = (GroupEntity) JsonUtil.jsonToObj(
						GroupEntity.class, resp.getBody().get("group")
								.toString());
				if (groupEntity == null) {
					ToastUtil.showShortToast("获取群信息失败！");
					finish();
					return;
				}
				if (!"1".equals(groupEntity.getAddGroupFlag())) {
					ToastUtil.showShortToast("您已不在此群");
					finish();
					return;
				}
				if (groupEntity.getPublicFlag() != null) {
					publicFlag = groupEntity.getPublicFlag();
				}
				if (groupEntity.getMsgFlag() != null) {
					msgFlag = groupEntity.getMsgFlag();
				}
				if (groupEntity.getJoinmode() != null) {
					needAgressFlag = groupEntity.getJoinmode();
				}
				topFlag = (Integer) PreferHelper.getSharedParam(
						groupEntity.getOtherId(), 0);
				LogUtil.d("hl", "公开（0公开,1私有）=" + publicFlag
						+ ",\n免打扰（0:正常接收,1:设置免打扰）=" + msgFlag
						+ ",\n置顶（0:正常,1:置顶）=" + topFlag);
				dealWithData();
			} else {
				ToastUtil.showShortToast("该群已不存在！");
				finish();
			}

			break;
		case reqCodeThree:
			ToastUtil.showShortToast("成功");
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESHRECENT_LEAVEGROUP)
					.putExtra("leaveGroupTalkId", groupEntity.getOtherId()));// 发广播刷新消息页面
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeFive:
			if (!Tool.isEmpty(resp.getBody().get("group"))
					&& resp.getBody().get("group").toString().length() > 2) {
				GroupEntity groupEntity = (GroupEntity) JsonUtil.jsonToObj(
						GroupEntity.class, resp.getBody().get("group")
								.toString());
				iv_grouphead.setUrlOfImage(groupEntity
						.getClientThumbGroupPhoto());
				AppContext.getInstance().getGroupMap()
						.put(chatRoomId, groupEntity.getClientGroupPhoto());
			}
			dismissProgressDialog();
			break;
		case upDateGroupNickName:
			ToastUtil.showShortToast("修改成功！");
			tv_mygroupname.setText(myGroupNickName);
			AppContext.getMyGroupNickNameMap().put(chatRoomId, myGroupNickName);
			break;
		case reqFlag:
			publicFlag = (publicFlag == 0 ? 1 : 0);
			setPublicStatus(publicFlag);
			LogUtil.d("hl", "公开（0公开,1私有）=" + publicFlag
					+ ",免打扰（0:正常接收,1:设置免打扰）=" + msgFlag);
			break;
		case reqIS:
			needAgressFlag = (needAgressFlag == 0 ? 1 : 0);
			setYanZhengStatus(needAgressFlag);
			break;
		case reqDisturb:
			msgFlag = (msgFlag == 1 ? 0 : 1);
			setMessagDistrubStatus(msgFlag);
			LogUtil.d("hl", "公开（0公开,1私有）=" + publicFlag
					+ ",免打扰（0:正常接收,1:设置免打扰）=" + msgFlag);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置公开属性状态
	 */
	private void setPublicStatus(int publicFlag) {
		tb_public
				.setBackgroundResource(publicFlag == 0 ? R.drawable.ios7_switch_on
						: R.drawable.ios7_switch_off);
	}

	/**
	 * 设置是否验证
	 */
	private void setYanZhengStatus(int needAgressFlag) {
		tb_needYZ
				.setBackgroundResource(needAgressFlag == 0 ? R.drawable.ios7_switch_off
						: R.drawable.ios7_switch_on);
	}

	/**
	 * 设置是否置顶
	 */
	private void setTopStatus(int topFalg) {
		tb_top.setBackgroundResource(topFalg == 0 ? R.drawable.ios7_switch_off
				: R.drawable.ios7_switch_on);
	}

	/**
	 * 设置免打扰属性状态
	 * 
	 * @param publicFlag
	 */
	private void setMessagDistrubStatus(int msgFlag) {
		tb_message
				.setBackgroundResource(1 == msgFlag ? R.drawable.ios7_switch_on
						: R.drawable.ios7_switch_off);
	}

	@SuppressLint("NewApi")
	private void dealWithData() {
		List<GroupUserEntity> groupUserList = groupEntity.getGroupUserList();
		setPublicStatus(publicFlag);
		setMessagDistrubStatus(msgFlag);
		setYanZhengStatus(needAgressFlag);
		setTopStatus(topFlag);
		if (groupEntity.getUserId() != AppContext.getInstance().getUserInfo()
				.getUserId()) {// 非群主不显示公开属性
			findViewById(R.id.lin_ispublic).setVisibility(View.GONE);
			findViewById(R.id.view_public).setVisibility(View.GONE);
			findViewById(R.id.view_iscommit).setVisibility(View.GONE);
			tv_hint.setVisibility(View.GONE);
			lin_iscommit.setVisibility(View.GONE);
		}
		iv_grouphead.setUrlOfImage(groupEntity.getClientGroupPhoto()
				+ "_thumb.png");
		tv_groupname.setText(groupEntity.getGroupName());
		tv_groupnumber.setText(groupEntity.getGroupNo());
		tv_membernum.setText("全部群成员（" + groupEntity.getMemberNum() + "）");
		center_txt_title.setText("聊天设置");
		tv_mygroupname.setText(groupEntity.getNickName());
		tv_notification.setText(groupEntity.getRemark());
		try {
			iv_qrcode.setImageBitmap((QRcodeUtil.createQRCode(
					groupEntity.getId() + "",
					ScreenUtil.dpToPx(getResources(), 20))));
		} catch (WriterException e) {
			e.printStackTrace();
		}
		showList.clear();
		for (int i = 0; i < groupUserList.size() && i < 20; i++) {
			GroupManageImageBean groupManageImageBean = new GroupManageImageBean();
			groupManageImageBean
					.setNickName(groupUserList.get(i).getNickName());
			groupManageImageBean.setClientPicUrl(groupUserList.get(i)
					.getClientHeadPicUrl());
			groupManageImageBean.setClientThumbHeadPicUrl(groupUserList.get(i)
					.getClientThumbHeadPicUrl());
			groupManageImageBean.setUserId(groupUserList.get(i).getUserId()
					+ "");
			showList.add(groupManageImageBean);
		}

		GroupManageImageBean groupManageImageBeanAdd = new GroupManageImageBean();
		groupManageImageBeanAdd.setType(1);
		groupManageImageBeanAdd.setClientPicUrl(R.drawable.tianjia + "");
		groupManageImageBeanAdd.setNickName("");
		showList.add(groupManageImageBeanAdd);

		if (groupEntity.getUserId() == AppContext.getInstance().getUserInfo()
				.getUserId()) {
			GroupManageImageBean groupManageImageBeanDel = new GroupManageImageBean();
			groupManageImageBeanDel.setType(2);
			groupManageImageBeanDel.setClientPicUrl(R.drawable.shanjian + "");
			groupManageImageBeanAdd.setNickName("");
			showList.add(groupManageImageBeanDel);
			btn_quit.setText("解散群");
			btn_quit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					commonDialog = new CommonDialog(GroupManageActivity.this,
							"提示", "是否解散群", "确定", "取消");
					commonDialog.show();
					commonDialog
							.setClicklistener(new CommonDialog.ClickListenerInterface() {
								@Override
								public void doConfirm() {
									commonDialog.dismiss();
									ReqBase req = new ReqBase();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("userId", AppContext.getInstance()
											.getUserInfo().getId());
									map.put("groupId", chatRoomId);
									map.put("leanCloudUserName", AppContext
											.getInstance().getUserInfo()
											.getLeanCloudUserName());
									req.setHeader(new ReqHead(
											AppConfig.BUSSINESS_IM_DISSOLUTION));
									req.setBody(JsonUtil.Map2JsonObj(map));
									httpPost(reqCodeThree,
											AppConfig.PUBLICK_NEARBY_GROUP, req);
								}

								@Override
								public void doCancel() {
									commonDialog.dismiss();
								}
							});

				}
			});
		} else {
			btn_quit.setText("删除并退出");
			btn_quit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					commonDialog = new CommonDialog(GroupManageActivity.this,
							"提示", "是否删除并退出群", "确定", "取消");
					commonDialog.show();
					commonDialog
							.setClicklistener(new CommonDialog.ClickListenerInterface() {
								@Override
								public void doConfirm() {
									commonDialog.dismiss();
									ReqBase req = new ReqBase();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("userId", AppContext.getInstance()
											.getUserInfo().getId());
									map.put("groupId", chatRoomId);
									map.put("leanCloudUserName", AppContext
											.getInstance().getUserInfo()
											.getLeanCloudUserName());
									req.setHeader(new ReqHead(
											AppConfig.BUSINESS_NEARBY_QUITGROUP));
									req.setBody(JsonUtil.Map2JsonObj(map));
									httpPost(reqCodeOne,
											AppConfig.PUBLICK_NEARBY_GROUP, req);
								}

								@Override
								public void doCancel() {
									commonDialog.dismiss();
								}
							});

				}
			});
		}
		showAdapter.setmDatas(showList);
		showAdapter.notifyDataSetChanged();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("操作失败");
		switch (requestCode) {
		case reqCodeFive:
			dismissProgressDialog();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				getGroupInformation();
				break;
			case reqCodeFour:
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPics = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (pics != null) {// 多选图片返回
					for (int i = 0; i < pics.size(); i++) {
						CompressPic(reqCodeTwo, pics.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqCodeFour, photoPath);
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param reqcode
	 * 
	 * @param photoPath
	 */
	private void CompressPic(int reqcode, String photoPath) {
		showProgressDialog("图片压缩中...");
		new MyAsyncTask(reqcode).execute(photoPath);
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {

		private int reqcode;

		public MyAsyncTask(int reqcode) {
			this.reqcode = reqcode;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (reqcode) {
			case reqCodeFour:
				picPathList.clear();
				picPathList.add(result);
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getUserId());
				map.put("groupName", "");
				map.put("groupId", groupEntity.getId() + "");
				map.put("remark", "");
				reqBase.setHeader(new ReqHead(
						AppConfig.BUSINESS_NEARBY_GROUPNAME));
				reqBase.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				formFileBeans = new ArrayList<FormFileBean>();
				formFile = new FormFileBean(new File(result),
						System.currentTimeMillis() + ".png");
				formFileBeans.add(formFile);
				mapFileBean.put("photo", formFileBeans);
				uploadFiles(reqCodeFive, AppConfig.PUBLICK_NEARBY_GROUP,
						mapFileBean, reqBase, true);
				dismissProgressDialog();
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... arg0) {
			String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
			LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
			return photoPath;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_groupname:
			if (groupEntity == null) {
				return;
			}
			if (groupEntity.getUserId() == AppContext.getInstance()
					.getUserInfo().getUserId()) {
				Intent i = new Intent(this, GroupNameActivity.class);
				i.putExtra("groupName", groupEntity.getGroupName());
				i.putExtra("maxMemberNum", groupEntity.getMaxMemberNum() + "");
				i.putExtra("groupId", groupEntity.getId() + "");
				startActivityForResult(i, 1);
			} else {
				ToastUtil.showShortToast("只有群主才能修改群聊名称");
			}

			break;
		case R.id.layout_groupqrcpde:
			if (groupEntity == null) {
				return;
			}
			Intent intent = new Intent(this, GroupQRCodeActivity.class);
			intent.putExtra("groupName", groupEntity.getGroupName());
			intent.putExtra("clientGroupPhoto",
					groupEntity.getClientGroupPhoto());
			intent.putExtra("groupId", chatRoomId + "");
			startActivity(intent);
			break;
		case R.id.layout_groupnotification:
			if (groupEntity == null) {
				return;
			}
			if (groupEntity.getUserId() == AppContext.getInstance()
					.getUserInfo().getUserId()) {
				Intent intent1 = new Intent(this,
						GroupNotificationActivity.class);
				intent1.putExtra("groupId", groupEntity.getId() + "");
				intent1.putExtra("remark", groupEntity.getRemark());
				startActivityForResult(intent1, 1);
			} else {
				ToastUtil.showShortToast("只有群主才能更改群介绍");
			}
			break;
		case R.id.layout_mygroupname:
			new CommonEditDialog(GroupManageActivity.this, "我在本群中的昵称", "请填写昵称",
					tv_mygroupname.getText().toString(), 10, true,
					new EditClickOkListener() {

						@Override
						public void doConfirm(String inputString) {
							myGroupNickName = inputString;
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getUserId());
							map.put("groupId", chatRoomId);
							map.put("nickName", inputString);
							req.setHeader(new ReqHead(
									AppConfig.BUSSINESS_IM_GROUP_CHANGENICKNAME));
							req.setBody(JsonUtil.Map2JsonObj(map));
							httpPost(upDateGroupNickName,
									AppConfig.PUBLICK_NEARBY_GROUP, req);
						}
					}).show();
			break;
		case R.id.tv_cleanchatlog:
			commonDialog = new CommonDialog(this, "提示", "是否清空聊天记录", "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							// 删除与某个聊天对象的全部消息记录
							NIMClient.getService(MsgService.class)
									.clearChattingHistory(
											groupEntity.getOtherId(),
											SessionTypeEnum.Team);
							// 移除该群最近会话项
							NIMClient.getService(MsgService.class)
									.deleteRecentContact2(
											groupEntity.getOtherId(),
											SessionTypeEnum.Team);
							ToastUtil.showShortToast("清空记录成功");
							sendBroadcast(new Intent(
									BroadCastAction.ACTION_IM_CLEAR_GROUPMESSAGE));// 发广播刷新消息页面
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
			break;
		case R.id.layout_allmember:
			startActivityForResult(
					new Intent(this, GroupMemberActivity.class).putExtra(
							"groupId", chatRoomId + ""), 1);
			break;
		case R.id.layout_grouphead:
			if (groupEntity != null
					&& (groupEntity.getUserId() == AppContext.getInstance()
							.getUserInfo().getUserId())) {
				Intent i = new Intent(GroupManageActivity.this,
						SelectPictureActivity.class);
				i.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
				i.putExtra("MAX_NUM", 1);// 选择的张数
				startActivityForResult(i, reqCodeFour);
			} else {
				ToastUtil.showShortToast("只有群主才能更改群头像");
			}

			break;
		case R.id.tb_public:
			if (groupEntity.getUserId() != AppContext.getInstance()
					.getUserInfo().getUserId()) {
				ToastUtil.showShortToast("只有群主才能修改公开属性");
				return;
			}
			ReqBase reqBase = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo()
					.getUserId());
			map.put("groupName", "");
			map.put("groupId", chatRoomId);
			map.put("remark", "");
			map.put("publicFlag", publicFlag == 0 ? 1 : 0); // 0公开1私有
			reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_NEARBY_GROUPNAME));
			reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			httpPost(reqFlag, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
			break;
		case R.id.tb_needYZ:
			if (groupEntity.getUserId() != AppContext.getInstance()
					.getUserInfo().getUserId()) {
				ToastUtil.showShortToast("只有群主才能修改公开属性");
				return;
			}
			ReqBase reqB = new ReqBase();
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("userId", AppContext.getInstance().getUserInfo()
					.getUserId());
			map1.put("groupName", "");
			map1.put("groupId", chatRoomId);
			map1.put("joinmode", needAgressFlag == 0 ? 1 : 0);// 0不需要,1需要
			reqB.setHeader(new ReqHead(AppConfig.BUSINESS_NEARBY_GROUPNAME));
			reqB.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map1)));
			httpPost(reqIS, AppConfig.PUBLICK_NEARBY_GROUP, reqB, true);
			break;
		case R.id.tb_message:
			ReqBase req = new ReqBase();
			Map<String, Object> map_msg = new HashMap<String, Object>();
			map_msg.put("userId", AppContext.getInstance().getUserInfo()
					.getUserId());
			map_msg.put("msgFlag", 1 == msgFlag ? 0 : 1);// 0:正常接收,1:设置免打扰
			map_msg.put("groupId", chatRoomId);
			map_msg.put("otherId", groupEntity.getOtherId());
			map_msg.put("leanCloudUserName", AppContext.getInstance()
					.getUserInfo().getLeanCloudUserName());
			req.setHeader(new ReqHead(AppConfig.BUSSINESS_IM_GROUP_DISTURD));
			req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map_msg)));
			httpPost(reqDisturb, AppConfig.PUBLICK_NEARBY_GROUP, req, true);
			break;
		case R.id.tb_top:
			if (groupEntity == null) {
				return;
			}
			topFlag = ((topFlag == 1) ? 0 : 1);
			setTopStatus(topFlag);
			PreferHelper.setSharedParam(groupEntity.getOtherId(), topFlag);
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE).putExtra(
					"otherId", groupEntity.getOtherId()).putExtra(
							"topFlag", topFlag));// 发广播刷新消息页面
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(ConstantKey.RESULT_REFRESH_GROUP);
			finish();
		}
		return false;

	}

}
