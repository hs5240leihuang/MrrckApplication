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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
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
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 编辑帖子
 */
public class EditPostActivity extends BaseActivity implements OnClickListener {

	public static final int TAKE_PHOTO_IMG = 100;// 图片选择
	private String picPath;// 选择的图片路径
	private boolean showEmotion = false;
	private int moduleType = 17;// 服务端指定的
	public static final int REQCODE_ATTACHEMENT = 1002;// 话题附件
	private EmotionEditText et_title, et_content;
	private ImageView btn_express;
	PostsEntity postBean;
	private LinearLayout emotionLayout;
	private MySimpleDraweeView imageView3;
	private MySimpleDraweeView btn_picture;
	private static final int REQCODE_EDITCONTENT = 8000;
	private ImageView img_delect;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_edit_post;
	}

	@Override
	public void initView() {
		img_delect = (ImageView) findViewById(R.id.img_delect);
		et_title = (EmotionEditText) findViewById(R.id.et_title);
		et_title.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
				InputTools.ShowKeyboard(et_title);
			}
		});
		et_title.setOnClickListener(this);
		et_content = (EmotionEditText) findViewById(R.id.et_content);
		et_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
				InputTools.ShowKeyboard(et_content);
			}
		});
		et_content.setOnClickListener(this);
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		layout_addImage.setOnClickListener(this);
		btn_picture = new MySimpleDraweeView(EditPostActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(btn_picture, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		LinearLayout layout_Image = (LinearLayout) findViewById(R.id.layout_Image);
		imageView3 = new MySimpleDraweeView(EditPostActivity.this);
		layout_Image.removeAllViews();
		layout_Image.addView(imageView3, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		btn_express = (ImageView) findViewById(R.id.btn_express);
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		emotionLayout.addView(new EmotionView(EditPostActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						if (et_title.hasFocus()) {
							showEmotionTxt(et_title, emotionText);
						} else if (et_content.hasFocus()) {
							showEmotionTxt(et_content, emotionText);
						}
					}
				}).getView());
	}

	protected void showEmotionTxt(EmotionEditText etContent, String emotionText) {
		int start = etContent.getSelectionStart();
		StringBuffer sb = new StringBuffer(etContent.getText());
		if (emotionText.equals(":l7:")) {// 删除图标
			if (start >= 4
					&& EmotionHelper.getLocalEmoMap(EditPostActivity.this)
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
		postBean = (PostsEntity) getIntent().getSerializableExtra("postBean");
		et_title.setText(postBean.getTitle());
		et_content.setText(postBean.getContent());
		et_title.setSelection(postBean.getTitle().length());
		imageView3.setUrlOfImage(postBean.getClientHeadPicUrl());
		if (!Tool.isEmpty(postBean.getAttachmentList())
				&& !Tool.isEmpty(postBean.getAttachmentList().get(0)
						.getClientFileUrl())) {
			picPath = postBean.getAttachmentList().get(0).getClientFileUrl();
			btn_picture.setUrlOfImage(picPath);
			img_delect.setVisibility(View.VISIBLE);
		} 
//		else {
//			btn_picture.setUrlOfImage("res://com.meiku.dev/"
//					+ R.drawable.icon_tupian);
//		}
	}

	@Override
	public void bindListener() {
		btn_express.setOnClickListener(this);
		img_delect.setOnClickListener(this);
		findViewById(R.id.right_txt_title).setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case REQCODE_ATTACHEMENT:
			btn_picture.setUrlOfImage("file://" + picPath);
			dismissProgressDialog();
			break;
		case REQCODE_EDITCONTENT:
			setResult(RESULT_OK);
			finish();
			ToastUtil.showShortToast("编辑成功");
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("修改失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_txt_title:
			if (TextUtils.isEmpty(et_content.getText().toString())) {
				ToastUtil.showShortToast("内容不能为空");

			} else if (TextUtils.isEmpty(et_title.getText().toString())) {
				ToastUtil.showShortToast("标题不能为空");
			} else {
				InputTools.HideKeyboard(v);
				edit();
			}
			break;
		case R.id.layout_addImage:
			InputTools.HideKeyboard(v);
			selectPicture();
			break;
		case R.id.btn_express:
			showEmotion = !showEmotion;
			showEmotionView(showEmotion);
			break;
		case R.id.goback:
			InputTools.HideKeyboard(v);
			setResult(RESULT_OK);
			finish();
		case R.id.et_title:
			et_title.requestFocus();
			showEmotion = false;
			emotionLayout.setVisibility(View.GONE);
			InputTools.ShowKeyboard(et_title);
			break;
		case R.id.et_content:
			et_content.requestFocus();
			showEmotion = false;
			emotionLayout.setVisibility(View.GONE);
			InputTools.ShowKeyboard(et_content);
			break;
		case R.id.img_delect:
			btn_picture.setVisibility(View.INVISIBLE);
			img_delect.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void edit() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("title", et_title.getText().toString());
		map.put("postsId", postBean.getPostsId());
		map.put("content", EmotionHelper.getSendEmotion(EditPostActivity.this,
				et_content.getText().toString()));
		map.put("attachmentId", -1);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_HOME_EDITPOST));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		mapFileBean.put("photo", formFileBeans);
		uploadFiles(REQCODE_EDITCONTENT, AppConfig.PUBLICK_BOARD, mapFileBean,
				reqBase, true);
	}

	private void showEmotionView(boolean show) {
		View v;
		if (et_title.hasFocus()) {
			v = et_title;
		} else {
			v = et_content;
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
				img_delect.setVisibility(View.VISIBLE);
				btn_picture.setVisibility(View.VISIBLE);
				if (Tool.isEmpty(picPath)) {
					picPath = result;
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("moduleType", moduleType);
					map.put("moduleId", postBean.getId());
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
					picPath = result;
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("postsId", postBean.getPostsId());
					map.put("content", "");
					map.put("attachmentId", postBean.getAttachmentList().get(0)
							.getAttachmentId());
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_HOME_EDITPOST));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
					FormFileBean formFile = new FormFileBean(new File(picPath),
							"photo.png");
					formFileBeans.add(formFile);
					mapFileBean.put("photo", formFileBeans);
					uploadFiles(REQCODE_ATTACHEMENT, AppConfig.PUBLICK_BOARD,
							mapFileBean, reqBase, true);
				}
				LogUtil.d("hl", "压缩后处理__" + result);
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
