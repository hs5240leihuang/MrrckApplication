package com.meiku.dev.ui.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.im.ChatActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.yunxin.TipAttachment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;

/**
 * 创建群
 * 
 */
public class CreateGroupActivity extends BaseActivity implements
		OnClickListener {

	private ImageView iv_xuxian, iv_grouphead;
	private TextView tv_addhead;
	private EditText et_groupname, et_introduce;
	// private LinearLayout layout_city;
	private Button btn_confirm;

	private List<String> picPathList = new ArrayList<String>();// 选择的图片路径
	private String position = "";
	private int publicFlag = 0;
	private int needAgressFlag = 0;// 0不需要,1需要
	private ToggleButton tb_opened;// 0公开1私有
	protected int selectProvinceCode = -1;
	protected int selectCityCode = -1;
	private ToggleButton tb_needYZ;
	private CommonDialog commonDialog;
	private boolean uploadPicFinished;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_create_group;
	}

	@Override
	public void initView() {
		iv_xuxian = (ImageView) findViewById(R.id.iv_xuxian);
		iv_grouphead = (ImageView) findViewById(R.id.iv_grouphead);
		tv_addhead = (TextView) findViewById(R.id.tv_addhead);
		// tv_category = (TextView) findViewById(R.id.tv_category);
		// tv_city = (TextView) findViewById(R.id.tv_city);
		et_groupname = (EditText) findViewById(R.id.et_groupname);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		// layout_category = (LinearLayout) findViewById(R.id.layout_category);
		// layout_city = (LinearLayout) findViewById(R.id.layout_city);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		tb_opened = (ToggleButton) findViewById(R.id.tb_opened);
		tb_opened.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				publicFlag = arg1 ? 0 : 1;
				LogUtil.d("hl", "publicFlag=" + publicFlag);
			}
		});
		tb_needYZ = (ToggleButton) findViewById(R.id.tb_needYZ);
		tb_needYZ.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				needAgressFlag = arg1 ? 1 : 0;
				LogUtil.e("555", "needAgressFlag=" + needAgressFlag);
			}
		});
	}

	@Override
	public void initValue() {
		// position = MKDataBase.getInstance().getFirstJobIDByPositionId(
		// AppContext.getInstance().getUserInfo().getPosition())
		// + "";
		// tv_category.setText(AppContext.getInstance().getUserInfo()
		// .getPositionName());
	}

	@Override
	public void bindListener() {
		iv_xuxian.setOnClickListener(this);
		// layout_category.setOnClickListener(this);
		// layout_city.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeThree:
			if (resp.getBody().get("data") != null
					&& resp.getBody().get("data").toString().length() > 4) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", resp.getBody().get("data")
						.getAsJsonArray());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Log.e("hl", "upload group_picture request" + "==>" + map);
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("photo.png");
				mainFfb.setFile(new File(picPathList.get(0)));
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(reqCodeFour, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, false);
			}
			final String groupString = resp.getBody().get("group").toString();
			commonDialog = new CommonDialog(CreateGroupActivity.this, "提示",
					resp.getHeader().getRetMessage(), "确定");
			commonDialog.setClicklistener(new ClickListenerInterface() {

				@Override
				public void doConfirm() {
					commonDialog.dismiss();
					try {
						if (!Tool.isEmpty(groupString)
								&& groupString.length() > 2) {
							final GroupEntity groupEntity = (GroupEntity) JsonUtil
									.jsonToObj(GroupEntity.class, groupString);
							if (groupEntity == null) {
								return;
							}
							AppContext
									.getInstance()
									.getGroupTalkIDMap()
									.put(groupEntity.getOtherId(),
											groupEntity.getId());
							AppContext
									.getInstance()
									.getGroupMap()
									.put(groupEntity.getId(),
											groupEntity
													.getClientThumbGroupPhoto());
							AppContext
									.getInstance()
									.getGroupNameMap()
									.put(groupEntity.getId(),
											groupEntity.getGroupName());

//							TipAttachment attch = new TipAttachment("["
//									+ AppContext.getInstance().getUserInfo()
//									.getNickName() + "]创建了此群");
//							com.netease.nimlib.sdk.msg.model.IMMessage msg = MessageBuilder
//									.createCustomMessage(groupEntity.getOtherId(), // 聊天对象的
//																		// ID，如果是单聊，为用户帐号，如果是群聊，为群组
//																		// ID
//											SessionTypeEnum.Team, // 聊天类型，单聊或群组
//											attch);
//							Map<String, Object> content = new HashMap<String, Object>(4);
//							content.put("nickName", AppContext.getInstance().getUserInfo()
//									.getNickName());
//							content.put("clientHeadPicUrl", AppContext.getInstance().getUserInfo()
//									.getClientThumbHeadPicUrl());
//							content.put("userId", AppContext.getInstance().getUserInfo().getId()
//									+ "");
//							content.put("groupId", groupEntity.getOtherId());
//							msg.setRemoteExtension(content);
//							msg.setPushPayload(content);
//							CustomMessageConfig config = new CustomMessageConfig();
//							config.enablePushNick = false; 
//							msg.setConfig(config);
//							NIMClient.getService(MsgService.class)
//									.sendMessage(msg, true);
							
							Intent intent = new Intent(
									CreateGroupActivity.this,
									ChatActivity.class);
							intent.putExtra(ConstantKey.KEY_IM_TALKTO_NAME,
									groupEntity.getGroupName());
							intent.putExtra(ConstantKey.KEY_IM_TALKTO,
									groupEntity.getId());
							intent.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT,
									groupEntity.getOtherId());
							intent.putExtra(ConstantKey.KEY_IM_SESSIONTYPE,
									XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
							startActivity(intent);
							if (uploadPicFinished) {
								finish();
							} else {
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										finish();
									}
								}, 3000);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void doCancel() {
					commonDialog.dismiss();
				}
			});
			commonDialog.show();
			break;
		case reqCodeFour:
			uploadPicFinished = true;
			if (commonDialog != null && commonDialog.isShowing()) {

			} else {
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		btn_confirm.setClickable(true);
		if (arg0 != null) {
			ReqBase resp = (ReqBase) arg0;
			if (!Tool.isEmpty(resp.getHeader().getRetMessage())) {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_xuxian:
			Intent intent = new Intent(CreateGroupActivity.this,
					SelectPictureActivity.class);
			intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
			intent.putExtra("MAX_NUM", 1);// 选择的张数
			startActivityForResult(intent, reqCodeOne);
			break;
		// case R.id.layout_category:
		// startActivityForResult(new Intent(CreateGroupActivity.this,
		// SelectPositionActivity.class).putExtra("id", "-1"),
		// reqCodeTwo);
		// break;
		case R.id.btn_confirm:
			if (isValidate()) {
				btn_confirm.setClickable(false);
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("nickName", AppContext.getInstance().getUserInfo()
						.getNickName());
				map.put("groupName", et_groupname.getText().toString());
				// map.put("libGroupId", position);
				map.put("publicFlag", publicFlag);
				map.put("joinmode", needAgressFlag);
				if (Tool.isEmpty(MrrckApplication.provinceCode)) {
					map.put("provinceCode", -1);
				} else {
					map.put("provinceCode", MrrckApplication.provinceCode);
				}
				if (Tool.isEmpty(MrrckApplication.cityCode)) {
					map.put("cityCode", -1);
				} else {
					map.put("cityCode", MrrckApplication.cityCode);
				}
				// map.put("provinceCode", selectProvinceCode);
				// map.put("cityCode", selectCityCode);
				map.put("remark", et_introduce.getText().toString());
				// map.put("groupUserList", new ArrayList<GroupUserDTO>());
				map.put("leanCloudUserName", AppContext.getInstance()
						.getUserInfo().getLeanCloudUserName());
				// photo（群头像）
				// Map<String, List<FormFileBean>> mapFileBean = new
				// HashMap<String, List<FormFileBean>>();
				// if (null != picPathList && picPathList.size() > 0) {
				// List<FormFileBean> formFileBeans = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile = new FormFileBean(new File(
				// picPathList.get(0)), "photo.png");
				// formFileBeans.add(formFile);
				// mapFileBean.put("photo", formFileBeans);
				// } else {
				// mapFileBean.put("photo", new ArrayList<FormFileBean>());
				// }
				// uploadFiles(reqCodeThree, AppConfig.PUBLICK_NEARBY_GROUP,
				// mapFileBean, req, true);
				if (!Tool.isEmpty(picPathList)) {
					List<UploadImg> imgList = new ArrayList<UploadImg>();
					UploadImg ui = new UploadImg();
					ui.setFileType("0");
					ui.setFileUrl("");
					ui.setFileThumb(picPathList.get(0).substring(
							picPathList.get(0).lastIndexOf(".") + 1,
							picPathList.get(0).length()));
					imgList.add(ui);
					map.put("fileUrlJSONArray",
							JsonUtil.listToJsonArray(imgList));
				}
				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18043));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.e("303030", "————" + map);
				httpPost(reqCodeThree, AppConfig.PUBLICK_NEARBY_GROUP, req);
			}

			break;
		// case R.id.layout_city:
		// new WheelSelectCityDialog(CreateGroupActivity.this, false,
		// new SelectCityListener() {
		//
		// @Override
		// public void ChooseOneCity(int provinceCode,
		// String provinceName, int cityCode,
		// String cityName) {
		// tv_city.setText(cityName);
		// selectProvinceCode = provinceCode;
		// selectCityCode = cityCode;
		// }
		// }).show();
		// break;
		default:
			break;
		}
	}

	private boolean isValidate() {
		// if (picPathList.size() == 0 || Tool.isEmpty(picPathList.get(0))) {
		// ToastUtil.showShortToast("请添加群头像");
		// return false;
		// }
		if (Tool.isEmpty(et_groupname.getText().toString())) {
			ToastUtil.showShortToast("请输入群名称");
			return false;
		}
		// else if (Tool.isEmpty(position)) {
		// ToastUtil.showShortToast("请选择群类别");
		// return false;
		// }
		// else if (Tool.isEmpty(tv_city.getText().toString())) {
		// ToastUtil.showShortToast("请选择所在城市");
		// return false;
		// }
		// else if (TextUtils.isEmpty(et_introduce.getText().toString())) {
		// ToastUtil.showShortToast("请输入群介绍");
		// return false;
		// }
		else {
			return true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d("hl", "onActivityResult--requestCode==" + requestCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPics = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (pics != null) {// 多选图片返回
					for (int i = 0; i < pics.size(); i++) {
						CompressPic(pics.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(photoPath);
				}
				break;
			// case reqCodeTwo:
			// tv_category.setText(data.getStringExtra("name"));
			// position = data.getIntExtra("id", 0) + "";
			// LogUtils.e(position);
			// break;
			default:
				break;
			}
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param photoPath
	 */
	private void CompressPic(String photoPath) {
		showProgressDialog("图片压缩中...");
		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				picPathList.clear();
				picPathList.add(result);
				iv_grouphead.setVisibility(View.INVISIBLE);
				tv_addhead.setVisibility(View.GONE);
				BitmapUtils bitmapUtils = new BitmapUtils(
						CreateGroupActivity.this);
				bitmapUtils.display(iv_xuxian, picPathList.get(0).toString());
				LogUtil.d("hl", "压缩后处理__" + result);
				dismissProgressDialog();
				super.onPostExecute(result);
			}

			@Override
			protected String doInBackground(String... arg0) {
				String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
				LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
				return photoPath;
			}

		}.execute(photoPath);
	}
}
