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
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkMenu;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
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

/**
 * 服务页面发新贴
 * 
 */
public class PublishPostActivity extends BaseActivity implements
		OnClickListener {

	private TextView spTopic;
	private EmotionEditText etTitle;
	protected boolean showEmotion;
	protected LinearLayout emotionLayout;
	private EmotionEditText etContent;
	private ImageView btnPicture;
	private ImageView btnExpress;
	private ImageView ivBack;
	private ImageView tvConfirm;
	private MySimpleDraweeView headImageView;
	protected String picPath;
	public static final int TAKE_PHOTO_IMG = 100;// 图片选择
	private ArrayList<String> allStr = new ArrayList<String>();
	private List<MkMenu> boardList = new ArrayList<MkMenu>();
	protected String menuId;
	private int attacheNum = 0;
	private int moduleType = 17;// 服务端指定的
	private String newPostId;
	private ScrollView scrollview;
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
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		spTopic = (TextView) findViewById(R.id.spinner_topic);
		etTitle = (EmotionEditText) findViewById(R.id.et_title);
		etTitle.setCursorVisible(false);
		etTitle.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) {
					etTitle.setCursorVisible(true);
				}
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
		ivBack = (ImageView) findViewById(R.id.left_res_title);
		tvConfirm = (ImageView) findViewById(R.id.right_res_title);
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		headImageView = new MySimpleDraweeView(PublishPostActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(headImageView, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		headImageView.setUrlOfImage(AppContext.getInstance().getUserInfo()
				.getClientHeadPicUrl()
				+ ConstantKey.THUMB);
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		emotionLayout.addView(new EmotionView(PublishPostActivity.this,
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
					&& EmotionHelper.getLocalEmoMap(PublishPostActivity.this)
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
		getBoardData();
	}

	@Override
	public void bindListener() {
		spTopic.setOnClickListener(this);
		btnPicture.setOnClickListener(this);
		btnExpress.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tvConfirm.setOnClickListener(this);
		img_delect.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBaseRep = (ReqBase) arg0;
		LogUtil.d("hl", "创帖=" + reqBaseRep.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(reqBaseRep.getBody())
					&& !Tool.isEmpty(reqBaseRep.getBody().get("dataConfig"))
					&& reqBaseRep.getBody().get("dataConfig").toString()
							.length() > 4) {
				boardList = (List<MkMenu>) JsonUtil.jsonToList(reqBaseRep
						.getBody().get("dataConfig") + "",
						new TypeToken<List<MkMenu>>() {
						}.getType());
				if (!Tool.isEmpty(boardList)) {
					allStr.clear();
					for (int i = 0, size = boardList.size(); i < size; i++) {
						allStr.add(boardList.get(i).getRemark());
					}
					// 默认选第一个版块
					// menuId = boardList.get(0).getId() + "";
					// spTopic.setText(boardList.get(0).getRemark());
				}

			} else {
				ToastUtil.showShortToast("获取发帖话题失败！");
			}
			break;

		case reqCodeTwo:
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
				uploadFiles(reqCodeThree, AppConfig.PUBLICK_COMMON,
						mapFileBean, reqBase, true);
			} else {
				ToastUtil.showShortToast("创建成功");
				gotoDetailActivity(menuId, newPostId);
			}
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("创建成功");
			gotoDetailActivity(menuId, newPostId);
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
		Intent intent = new Intent(PublishPostActivity.this,
				PostDetailNewActivity.class);
		intent.putExtra(ConstantKey.KEY_POSTID, newPostId);
		intent.putExtra(ConstantKey.KEY_BOARDID, boardId);
		startActivity(intent);
		setResult(RESULT_OK, new Intent());
		finish();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询可发帖版块
	 * 
	 * @param boardId
	 */
	private void getBoardData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_GETBOARDDATA));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING, reqBase, true);
	}

	/**
	 * 发布帖子
	 * 
	 * @param boardId
	 */
	private void goCreatPost() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("menuId", menuId);
		map.put("title", EmotionHelper.getSendEmotion(PublishPostActivity.this,
				etTitle.getText().toString()));
		map.put("content", EmotionHelper.getSendEmotion(
				PublishPostActivity.this, etContent.getText().toString()));
		map.put("provinceCode",
				Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getProvinceCode()) ? "-1" : AppContext.getInstance()
						.getUserInfo().getProvinceCode());
		map.put("cityCode",
				Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getCityCode()) ? "-1" : AppContext.getInstance()
						.getUserInfo().getCityCode());
		map.put("attachmentNum", attacheNum);
		LogUtil.d("hl", "创帖请求=" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_SERVER_CREATPOST));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, reqBase, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.spinner_topic:
			if (Tool.isEmpty(allStr) || Tool.isEmpty(boardList)) {
				ToastUtil.showShortToast("暂无版块选择");
				return;
			}
			new ChooseTextDialog(PublishPostActivity.this, "选择话题", allStr,
					new ChooseOneStrListener() {

						@Override
						public void doChoose(int position, String s) {
							menuId = boardList.get(position).getId() + "";
							spTopic.setText(s);
						}
					}).show();
			break;
		case R.id.left_res_title:
			InputTools.HideKeyboard(v);
			finishWhenTip();
			break;
		case R.id.right_res_title:
			InputTools.HideKeyboard(v);
			if (validate()) {
				if (!TextUtils.isEmpty(picPath)) {
					attacheNum = 1;
				} else {
					attacheNum = 0;
				}
				goCreatPost();
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
			etTitle.setCursorVisible(true);
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
		if (Tool.isEmpty(menuId) || Tool.isEmpty(allStr)
				|| Tool.isEmpty(boardList)) {
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
						PublishPostActivity.this);
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
					PublishPostActivity.this, "提示", "是否放弃本次编辑?", "确定", "取消");
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
