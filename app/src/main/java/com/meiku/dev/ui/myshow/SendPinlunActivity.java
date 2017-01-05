package com.meiku.dev.ui.myshow;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 发表评论页面
 * 
 */
public class SendPinlunActivity extends BaseActivity implements OnClickListener {

	private EditText etReplyTopic;
	private EmotionEditText etReplyContent;
	private ImageView btnPicture;
	private ImageView btnExpress;
	private LinearLayout ivBack;
	private MySimpleDraweeView headPicImageView;
	private TextView tvConfirm;
	private LinearLayout lin_waibu;

	public static final int REQCODE_REPLY = 10000;// 回复

	private LinearLayout emotionLayout;
	private boolean showEmotion = true;
	private int signupId;
	private int toUserId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_reply;
	}

	@Override
	public void initView() {
		((TextView) findViewById(R.id.center_txt_title)).setText("发表评论");
		etReplyTopic = (EditText) findViewById(R.id.et_topic);
		etReplyContent = (EmotionEditText) findViewById(R.id.et_content);
		etReplyContent.requestFocus();
		InputTools.ShowKeyboard(etReplyContent);
		btnPicture = (ImageView) findViewById(R.id.btn_picture);
		btnPicture.setVisibility(View.GONE);
		btnExpress = (ImageView) findViewById(R.id.btn_express);
		ivBack = (LinearLayout) findViewById(R.id.goback);
		tvConfirm = (TextView) findViewById(R.id.right_txt_title);
		tvConfirm.setTextColor(Color.parseColor("#000000"));
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		headPicImageView = new MySimpleDraweeView(SendPinlunActivity.this);
		layout_addImage.addView(headPicImageView,
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		lin_waibu = (LinearLayout) findViewById(R.id.lin_waibu);
		lin_waibu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// 触摸隐藏键盘
				hideBottomLayout();
				hideSoftInputView();
				return false;
			}
		});
		emotionLayout.addView(new EmotionView(SendPinlunActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						int start = etReplyContent.getSelectionStart();
						StringBuffer sb = new StringBuffer(etReplyContent
								.getText());
						if (emotionText.equals(":l7:")) {// 删除图标
							if (start >= 4
									&& EmotionHelper.getLocalEmoMap(
											SendPinlunActivity.this)
											.containsKey(
													sb.substring(start - 4,
															start))) {
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
		signupId = getIntent().getIntExtra("signupId", 0);
		toUserId = getIntent().getIntExtra("toUserId", 0);
		headPicImageView.setUrlOfImage(AppContext.getInstance().getUserInfo()
				.getClientHeadPicUrl()
				+ ConstantKey.THUMB);
		String toUserName = getIntent().getStringExtra("toUserName");
		etReplyTopic.setText(toUserName);
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
			setResult(RESULT_OK);
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
		case R.id.goback:
			finishWhenTip();
			break;
		case R.id.right_txt_title:
			if (validate()) {
				addpinglun();
			}
			break;
		case R.id.btn_express:
			showEmotionView(showEmotion);
			showEmotion = !showEmotion;
			break;
		default:
			break;
		}
	}

	// 添加评论
	public void addpinglun() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("signupId", signupId);
		map.put("toUserId", toUserId);
		map.put("toCommentId", 0);
		map.put("content", EmotionHelper.getSendEmotion(
				SendPinlunActivity.this, etReplyContent.getText().toString()));
		map.put("fileType", 0);
		map.put("floorNum", 0);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ADD_COMMENT));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(REQCODE_REPLY, AppConfig.PUBLICK_BOARD, req, true);
	}

	private boolean validate() {
		if (etReplyContent.getText().toString().equals("")) {
			ShowLoginDialogUtil.showTipDialog(this, "请输入回复内容");
			return false;
		} else {
			return true;
		}

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
					SendPinlunActivity.this, "提示", "是否放弃本次编辑?", "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							setResult(RESULT_OK);
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

	/**
	 * 隐藏底部栏
	 */
	protected void hideBottomLayout() {
		showEmotionView(false);
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInputView() {
		InputTools.HideKeyboard(etReplyContent);
	}
}
