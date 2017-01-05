package com.meiku.dev.ui.community;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.TopicData;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ChooseTextDialog;
import com.meiku.dev.views.ChooseTextDialog.ChooseOneStrListener;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.MySimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

//发表帖子
public class ReleaseTopicActivity extends BaseActivity implements
		OnClickListener {

	public final static String KEY_BOARDID = "KEY_BOARDID";
	public final static String KEY_TOPICID = "KEY_TOPICID";
	public final static String KEY_TOPICSTRING = "KEY_TOPICSTRING";
	private MySimpleDraweeView headImageView;

	private String picPath;// 选择的图片路径

	private TextView spTopic;
	private EmotionEditText etTitle;
	private EmotionEditText etContent;
	private ImageView btnPicture;
	private ImageView btnExpress;
	private List<TopicData> tList;// 话题列表

	private String boardId;// 板块id
	private String topicIdString;
	private int topicId;// 话题id
	private int attacheNum;// 附件数量
	private int moduleType = 17;// 服务端指定的
	private LinearLayout emotionLayout;

	public static final int TAKE_PHOTO_IMG = 100;// 图片选择
	public static final int REQCODE_GETTOPIC = 1000;// 获取话题
	public static final int REQCODE_RELEASETOPIC = 1001;// 发布话题
	public static final int REQCODE_ATTACHEMENT = 1002;// 话题附件
	private boolean showEmotion = false;
	private ArrayList<String> allStr;
	private String newPostId;
	private String topicString;
	private String menuId;
	private LinearLayout layout_topic;
	private ImageView img_delect;

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

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_release_topic;
	}

	@Override
	public void initView() {
		img_delect = (ImageView) findViewById(R.id.img_delect);
		layout_topic = (LinearLayout) findViewById(R.id.layout_topic);
		spTopic = (TextView) findViewById(R.id.spinner_topic);
		etTitle = (EmotionEditText) findViewById(R.id.et_title);
		etTitle.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
			}
		});
		etTitle.setOnClickListener(this);
		etContent = (EmotionEditText) findViewById(R.id.et_content);
		etContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
			}
		});
		etContent.setOnClickListener(this);
		btnPicture = (ImageView) findViewById(R.id.btn_picture);
		btnExpress = (ImageView) findViewById(R.id.btn_express);
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		headImageView = new MySimpleDraweeView(ReleaseTopicActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(headImageView, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		emotionLayout.addView(new EmotionView(ReleaseTopicActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						if (etTitle.hasFocus()) {
							showEmotionTxt(etTitle, emotionText);
						} else if (etContent.hasFocus()) {
							showEmotionTxt(etContent, emotionText);
						}

					}
				}).getView());
	}

	protected void showEmotionTxt(EmotionEditText etContent, String emotionText) {
		int start = etContent.getSelectionStart();
		StringBuffer sb = new StringBuffer(etContent.getText());
		if (emotionText.equals(":l7:")) {// 删除图标
			if (start >= 4
					&& EmotionHelper.getLocalEmoMap(ReleaseTopicActivity.this)
							.containsKey(sb.substring(start - 4, start))) {
				sb.replace(start - 4, start, "");
				etContent.setText(sb.toString());
				CharSequence info = etContent.getText();
				if (info instanceof Spannable) {
					Spannable spannable = (Spannable) info;
					Selection.setSelection(spannable, start - 4);
				}
				return;
			}
			return;
		}
		sb.replace(etContent.getSelectionStart(), etContent.getSelectionEnd(),
				emotionText);
		etContent.setText(sb.toString());

		CharSequence info = etContent.getText();
		if (info instanceof Spannable) {
			Spannable spannable = (Spannable) info;
			Selection.setSelection(spannable, start + emotionText.length());
		}
	}

	@Override
	public void initValue() {
		boardId = getIntent().getStringExtra(KEY_BOARDID);
		headImageView.setUrlOfImage(AppContext.getInstance().getUserInfo()
				.getClientHeadPicUrl()
				+ ConstantKey.THUMB);
		topicIdString = getIntent().getStringExtra(KEY_TOPICID);
		topicString = getIntent().getStringExtra(KEY_TOPICSTRING);
		menuId = getIntent().getStringExtra("menuId");
		if (!Tool.isEmpty(boardId)) {
			getTopic(boardId);
		} else {// boardId为空则为 首页发帖
			layout_topic.setVisibility(View.GONE);
		}
	}

	/**
	 * 查询话题
	 * 
	 * @param boardId
	 */
	private void getTopic(String boardId) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_COMMUNITY_TOPIC));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(REQCODE_GETTOPIC, AppConfig.PUBLICK_BOARD, reqBase, true);
	}

	@Override
	public void bindListener() {
		spTopic.setOnClickListener(this);
		btnPicture.setOnClickListener(this);
		btnExpress.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.right_txt_title).setOnClickListener(this);
		img_delect.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBaseRep = (ReqBase) arg0;
		switch (requestCode) {
		case REQCODE_GETTOPIC:
			String temp = reqBaseRep.getBody().toString();
			if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
					.getJSONType(temp)) {
				tList = new ArrayList<TopicData>();
				tList = (List<TopicData>) JsonUtil.jsonToList(
						(JsonUtil.String2Object(temp).get("topic")).toString(),
						new TypeToken<List<TopicData>>() {
						}.getType());
				allStr = new ArrayList<String>();
				for (int i = 0, size = tList.size(); i < size; i++) {
					allStr.add(tList.get(i).getName());
				}
			}
			// if (Tool.isEmpty(topicIdString) || "-1".equals(topicIdString)) {
			// topicId = tList.get(0).getId();
			// spTopic.setText(tList.get(0).getName());
			// } else {
			// topicId = Integer.parseInt(topicIdString);
			// spTopic.setText(topicString);
			// }
			break;
		case REQCODE_RELEASETOPIC:
			PostsEntity postBean = (PostsEntity) JsonUtil.jsonToObj(
					PostsEntity.class, reqBaseRep.getBody().get("postEntity")
							.toString());
			newPostId = String.valueOf(postBean.getId());
			if (attacheNum > 0) {
				String bodyString = reqBaseRep.getBody().toString();
				String postEntityString = JsonUtil.String2Object(bodyString)
						.get("postEntity").toString();
				int moduleId = Integer.parseInt((JsonUtil.String2Object(
						postEntityString).get("id").toString()));
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("moduleType", moduleType);
				map.put("moduleId", moduleId);
				map.put("title", "");
				map.put("remark", "");
				map.put("isPublic", "");
				map.put("isCover", "");
				reqBase.setHeader(new ReqHead(AppConfig.ALL_ATTACHMENT));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
				FormFileBean formFile = new FormFileBean(new File(picPath),
						"photo.png");
				formFileBeans.add(formFile);
				mapFileBean.put("photo", formFileBeans);
				uploadFiles(REQCODE_ATTACHEMENT, AppConfig.PUBLICK_COMMON,
						mapFileBean, reqBase, true);
			} else {
				ToastUtil.showShortToast("创建成功");
				gotoDetailActivity(boardId, newPostId);
			}
			break;
		case REQCODE_ATTACHEMENT:
			ToastUtil.showShortToast("创建成功");
			gotoDetailActivity(boardId, newPostId);
			break;
		case reqCodeThree:
			finish();
			ToastUtil.showShortToast("创建成功");
			break;
		default:
			break;
		}
	}

	/**
	 * 创建帖子成功,进入详细
	 * 
	 * @param boardId
	 * @param newPostId
	 */
	private void gotoDetailActivity(String boardId, String newPostId) {
		Intent intent = new Intent(ReleaseTopicActivity.this,
				PostDetailNewActivity.class);
		intent.putExtra(ConstantKey.KEY_POSTID, newPostId);
		intent.putExtra(ConstantKey.KEY_BOARDID, boardId);
		startActivity(intent);
		setResult(RESULT_OK, new Intent());
		finish();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		LogUtil.e(arg0.toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.spinner_topic:
			if (!Tool.isEmpty(allStr)) {
				new ChooseTextDialog(ReleaseTopicActivity.this, "选择话题", allStr,
						new ChooseOneStrListener() {

							@Override
							public void doChoose(int position, String s) {
								topicId = tList.get(position).getId();
								spTopic.setText(s);
							}
						}).show();
			} else {
				ToastUtil.showShortToast("暂无话题");
			}

			break;
		case R.id.goback:
			InputTools.HideKeyboard(v);
			finishWhenTip();
			break;
		case R.id.right_txt_title:
			InputTools.HideKeyboard(v);
			if (validate()) {
				if (!TextUtils.isEmpty(picPath)) {
					attacheNum = 1;
				}
				if (!Tool.isEmpty(boardId)) {
					release();
				} else {
					publishNewPost();
				}

			}
			break;
		case R.id.btn_picture:
			InputTools.HideKeyboard(v);
			selectPicture();
			break;
		case R.id.btn_express:
			showEmotion = !showEmotion;
			showEmotionView(showEmotion);
			break;
		case R.id.et_title:
			etTitle.requestFocus();
			showEmotion = false;
			emotionLayout.setVisibility(View.GONE);
			break;
		case R.id.et_content:
			etContent.requestFocus();
			showEmotion = false;
			emotionLayout.setVisibility(View.GONE);
			break;
		case R.id.img_delect:
			btnPicture.setImageDrawable(getResources().getDrawable(
					R.drawable.add_photo));
			img_delect.setVisibility(View.GONE);
			picPath = "";
			break;
		default:
			break;
		}
	}

	/**
	 * 社区首页进来-发布新帖
	 */
	private void publishNewPost() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuId", menuId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("title", EmotionHelper.getSendEmotion(
				ReleaseTopicActivity.this, etTitle.getText().toString()));
		map.put("content", EmotionHelper.getSendEmotion(
				ReleaseTopicActivity.this, etContent.getText().toString()));
		map.put("provinceCode",
				Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getProvinceCode()) ? "-1" : AppContext.getInstance()
						.getUserInfo().getProvinceCode());
		map.put("cityCode",
				Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getCityCode()) ? "-1" : AppContext.getInstance()
						.getUserInfo().getCityCode());
		map.put("attachmentNum", attacheNum);
		LogUtil.d("hl", "creat post =>" + map);
		reqBase.setHeader(new ReqHead(
				AppConfig.BUSINESS_COMMUNITY_PUBLISHNEWPOST));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();

		if (!Tool.isEmpty(picPath)) {
			// 发帖图片
			List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
			logo_FileBeans.add(new FormFileBean(new File(picPath),
					"postMainPhoto.png"));
			mapFileBean.put("photo", logo_FileBeans);
		}

		uploadFiles(reqCodeThree, AppConfig.PUBLICK_BOARD, mapFileBean,
				reqBase, true);
	}

	private void showEmotionView(boolean show) {
		View v;
		if (etTitle.hasFocus()) {
			v = etTitle;
		} else {
			v = etContent;
		}
		if (show) {
			InputTools.HideKeyboard(v);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					emotionLayout.setVisibility(View.VISIBLE);
				}
			}, 200);
		} else {
			emotionLayout.setVisibility(View.GONE);
			InputTools.ShowKeyboard(v);
		}
	}

	private boolean validate() {
		if (!Tool.isEmpty(boardId)
				&& spTopic.getText().toString().equals("点击选择")) {
			ToastUtil.showShortToast("请选择话题");
			return false;
		}
		if (Tool.isEmpty(etTitle.getText().toString())) {
			ToastUtil.showShortToast("请输入标题");
			return false;
		}
		if (Tool.isEmpty(etContent.getText().toString())) {
			ToastUtil.showShortToast("请输入内容");
			return false;
		}
		return true;
	}

	private void selectPicture() {
		Intent intent = new Intent(this, SelectPictureActivity.class);
		intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
		intent.putExtra("MAX_NUM", 1);// 选择的张数
		startActivityForResult(intent, TAKE_PHOTO_IMG);
	}

	private void release() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuId", menuId);
		map.put("boardId", boardId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("topicId", topicId);
		map.put("title", EmotionHelper.getSendEmotion(
				ReleaseTopicActivity.this, etTitle.getText().toString()));
		map.put("content", EmotionHelper.getSendEmotion(
				ReleaseTopicActivity.this, etContent.getText().toString()));
		map.put("provinceCode",
				Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getProvinceCode()) ? "-1" : AppContext.getInstance()
						.getUserInfo().getProvinceCode());
		map.put("cityCode",
				Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getCityCode()) ? "-1" : AppContext.getInstance()
						.getUserInfo().getCityCode());
		// map.put("provinceCode", MrrckApplication.provinceCode) ;
		// map.put("cityCode", MrrckApplication.cityCode);
		map.put("attachmentNum", attacheNum);
		LogUtil.d("hl", "creat post =>" + map);
		reqBase.setHeader(new ReqHead(AppConfig.COMM_RELEASETOPIC));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(REQCODE_RELEASETOPIC, AppConfig.PUBLICK_BOARD, reqBase, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKE_PHOTO_IMG:
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (pics != null) {// 多选图片返回
					CompressPic(pics.get(0));
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(photoPath);
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
	 * @param photoPath
	 */
	private void CompressPic(String photoPath) {
		showProgressDialog("图片压缩中...");
		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				picPath = result;
				BitmapUtils bitmapUtils = new BitmapUtils(
						ReleaseTopicActivity.this);
				bitmapUtils.display(btnPicture, picPath);
				img_delect.setVisibility(View.VISIBLE);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		if (!Tool.isEmpty(etTitle.getText().toString())
				|| !Tool.isEmpty(etContent.getText().toString())) {
			final CommonDialog commonDialog = new CommonDialog(
					ReleaseTopicActivity.this, "提示", "是否放弃本次编辑?", "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							finish();

						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
		} else {
			finish();
		}
	}
}
