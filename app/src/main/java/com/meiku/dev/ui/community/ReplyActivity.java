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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
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
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.MySimpleDraweeView;

//回复话题
public class ReplyActivity extends BaseActivity implements OnClickListener {

	private EditText etReplyTopic;
	private EmotionEditText etReplyContent;
	private ImageView btnPicture;
	private ImageView btnExpress;
	private ImageView ivBack;
	private MySimpleDraweeView headPicImageView;
	private ImageView tvConfirm;

	private String postsId;// 帖子的id
	private String toUserId;// 对方用户的id
	private String toCommentId;// 回复评论的id 一级回复则为0
	private int fileType = -1;// 0:图片 1:视频 2音频 没有附件-1

	public static final int TAKE_PHOTO_IMG = 100;// 图片选择
	public static final int REQCODE_REPLY = 10000;// 回复

	private String attachPath;// 选择的图片路径
	private String boardId;// 板块Id
	private String floorNum;
	private LinearLayout emotionLayout;
	private boolean showEmotion = false;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_reply;
	}

	@Override
	public void initView() {
		etReplyTopic = (EditText) findViewById(R.id.et_topic);
		etReplyTopic.setOnClickListener(this);
		etReplyContent = (EmotionEditText) findViewById(R.id.et_content);
		etReplyContent.setOnClickListener(this);
		etReplyContent.requestFocus();
		InputTools.ShowKeyboard(etReplyContent);
		btnPicture = (ImageView) findViewById(R.id.btn_picture);
		btnExpress = (ImageView) findViewById(R.id.btn_express);
		ivBack = (ImageView) findViewById(R.id.left_res_title);
		tvConfirm = (ImageView) findViewById(R.id.right_res_title);
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		headPicImageView = new MySimpleDraweeView(ReplyActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(headPicImageView,
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));

		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		emotionLayout.addView(new EmotionView(ReplyActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						int start = etReplyContent.getSelectionStart();
						StringBuffer sb = new StringBuffer(etReplyContent
								.getText());
						if (emotionText.equals(":l7:")) {// 删除图标
							if (start >= 4
									&& EmotionHelper.getLocalEmoMap(
											ReplyActivity.this).containsKey(
											sb.substring(start - 4, start))) {
								sb.replace(start - 4, start, "");
								etReplyContent.setText(sb.toString());
								CharSequence info = etReplyContent.getText();
								if (info instanceof Spannable) {
									Spannable spannable = (Spannable) info;
									Selection
											.setSelection(spannable, start - 4);
								}
								return;
							}
							return;
						}
						sb.replace(etReplyContent.getSelectionStart(),
								etReplyContent.getSelectionEnd(), emotionText);
						etReplyContent.setText(sb.toString());

						CharSequence info = etReplyContent.getText();
						if (info instanceof Spannable) {
							Spannable spannable = (Spannable) info;
							if (start + emotionText.length() < 1000) {
								Selection.setSelection(spannable, start
										+ emotionText.length());
							}
						}
					}
				}).getView());
	}

	private void showEmotionView(final boolean show) {
		if (show) {
			InputTools.HideKeyboard(etReplyContent);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					emotionLayout.setVisibility(View.VISIBLE);
				}
			}, 200);
		} else {
			emotionLayout.setVisibility(View.GONE);
			InputTools.ShowKeyboard(etReplyContent);
		}

	}

	@Override
	public void initValue() {
		headPicImageView.setUrlOfImage(AppContext.getInstance().getUserInfo()
				.getClientHeadPicUrl()
				+ ConstantKey.THUMB);
		postsId = getIntent().getStringExtra("postsId");
		toUserId = getIntent().getStringExtra("toUserId");
		floorNum = getIntent().getStringExtra("floorNum");
		toCommentId = getIntent().getStringExtra("toCommentId");
		boardId = getIntent().getStringExtra("boardId");
		String toUserName = getIntent().getStringExtra("toUserName");
		if (Tool.isEmpty(floorNum) || "0".equals(floorNum)) {
			etReplyTopic.setText(toUserName + ("（楼主）"));
		} else {
			etReplyTopic.setText(toUserName + "（" + floorNum + "楼）");
		}

		LogUtil.d("hl", "floorNum=" + floorNum + ",toUserId=" + toUserId
				+ "，postsId=" + postsId + "，toCommentId=" + toCommentId
				+ "，boardId=" + boardId);
	}

	@Override
	public void bindListener() {
		btnPicture.setOnClickListener(this);
		btnExpress.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tvConfirm.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case REQCODE_REPLY:
			setResult(RESULT_OK, new Intent());
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ReqBase bean = (ReqBase) arg0;
		switch (requestCode) {
		case REQCODE_REPLY:
			ToastUtil.showShortToast(bean.getBody().toString());
			LogUtil.e("ee:" + bean.getBody().toString());
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_res_title:
			InputTools.HideKeyboard(view);
			finishWhenTip();
			break;
		case R.id.right_res_title:
			InputTools.HideKeyboard(view);
			if (validate()) {
				confirm();
			}
			break;
		case R.id.btn_picture:
			InputTools.HideKeyboard(view);
			selectPicture();
			break;
		case R.id.btn_express:
			showEmotion = !showEmotion;
			showEmotionView(showEmotion);
			break;
		case R.id.et_content:
		case R.id.et_topic:
			showEmotion = false;
			showEmotionView(showEmotion);
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		if (etReplyContent.getText().toString().equals("")
				&& TextUtils.isEmpty(attachPath)) {
			ShowLoginDialogUtil.showTipDialog(this, "请输入回复内容或选择图片");
			return false;
		} else {
			return true;
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
				fileType = 0;
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
				attachPath = result;
				BitmapUtils bitmapUtils = new BitmapUtils(ReplyActivity.this);
				bitmapUtils.display(btnPicture, attachPath);
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

	private void confirm() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("postsId", postsId);
		map.put("toUserId", toUserId);
		map.put("toCommentId", toCommentId);
		map.put("content", EmotionHelper.getSendEmotion(ReplyActivity.this,
				etReplyContent.getText().toString()));
		map.put("fileType", fileType);
		map.put("floorNum", floorNum);
		map.put("boardId", boardId);
		reqBase.setHeader(new ReqHead(AppConfig.COMM_REPLYTOPIC));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (fileType != -1) {
			List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
			FormFileBean formFile;
			switch (fileType) {
			case 0:
				formFile = new FormFileBean(new File(attachPath), "photo.png");
				formFileBeans.add(formFile);
				mapFileBean.put("photo", formFileBeans);
				break;
			case 1:
				formFile = new FormFileBean(new File(attachPath), "video.mp4");
				formFileBeans.add(formFile);
				mapFileBean.put("video", formFileBeans);
				break;
			case 2:
				formFile = new FormFileBean(new File(attachPath), "audio.mp3");
				formFileBeans.add(formFile);
				mapFileBean.put("voice", formFileBeans);
				break;
			default:
				break;
			}
		}
		uploadFiles(REQCODE_REPLY, AppConfig.PUBLICK_BOARD, mapFileBean,
				reqBase, true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		if (!Tool.isEmpty(etReplyContent.getText().toString())) {
			final CommonDialog commonDialog = new CommonDialog(
					ReplyActivity.this, "提示", "是否放弃本次编辑?", "确定", "取消");
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
