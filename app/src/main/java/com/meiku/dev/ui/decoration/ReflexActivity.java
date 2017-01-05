package com.meiku.dev.ui.decoration;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;

public class ReflexActivity extends BaseActivity implements OnClickListener {
	private TextView right_txt_title;
	private EmotionEditText et_content;
	private ImageView jianpan;
	private ImageView btnExpress;
	protected boolean showEmotion;
	protected LinearLayout emotionLayout;
	private String postsId;// 帖子的id
	private String toUserId;// 对方用户的id
	private String toCommentId;// 回复评论的id 一级回复则为0
	private int fileType = -1;// 0:图片 1:视频 2音频 没有附件-1
	private String boardId;// 板块Id
	private String floorNum;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_reflex;
	}

	@Override
	public void initView() {
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		btnExpress = (ImageView) findViewById(R.id.btn_express);
		jianpan = (ImageView) findViewById(R.id.btn_jianpan);
		et_content = (EmotionEditText) findViewById(R.id.et_content);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
		right_txt_title.setTextColor(Color.parseColor("#FF3499"));
		et_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
			}
		});
		emotionLayout.addView(new EmotionView(ReflexActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						if (et_content.hasFocus()) {
							showEmotionTxt(et_content, emotionText);
						}

					}
				}).getView());
	}

	@Override
	public void initValue() {
		postsId = getIntent().getStringExtra("postsId");
		toUserId = getIntent().getStringExtra("toUserId");
		floorNum = getIntent().getStringExtra("floorNum");
		toCommentId = getIntent().getStringExtra("toCommentId");
		boardId = getIntent().getStringExtra("boardId");
		String toUserName = getIntent().getStringExtra("toUserName");
		if (Tool.isEmpty(floorNum) || "0".equals(floorNum)) {
			et_content.setHint(toUserName + ("（楼主）"));
		//	et_content.setText(toUserName + ("（楼主）"));
		} else {
			//et_content.setText(toUserName + "（" + floorNum + "楼）");
			et_content.setHint(toUserName + "（" + floorNum + "楼）");
		}

		LogUtil.d("hl", "floorNum=" + floorNum + ",toUserId=" + toUserId
				+ "，postsId=" + postsId + "，toCommentId=" + toCommentId
				+ "，boardId=" + boardId);
	}

	@Override
	public void bindListener() {
		et_content.setOnClickListener(this);
		right_txt_title.setOnClickListener(this);
		btnExpress.setOnClickListener(this);
		jianpan.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("回复失败！ ");
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right_txt_title:
			InputTools.HideKeyboard(arg0);
			if (validate()) {
				Publish();
			}
			break;

		case R.id.btn_express:
			InputTools.HideKeyboard(et_content);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					emotionLayout.setVisibility(View.VISIBLE);
				}
			}, 200);

			break;
		case R.id.et_content:
			et_content.requestFocus();
			InputTools.ShowKeyboard(et_content);
			emotionLayout.setVisibility(View.GONE);
			break;
		case R.id.btn_jianpan:
			InputTools.ShowKeyboard(et_content);
			emotionLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	// 回复
	public void Publish() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("postsId", postsId);
		map.put("toUserId", toUserId);
		map.put("toCommentId", toCommentId);
		map.put("content", EmotionHelper.getSendEmotion(ReflexActivity.this,
				et_content.getText().toString()));
		map.put("fileType", -1);
		map.put("floorNum", floorNum);
		map.put("boardId", boardId);
		req.setHeader(new ReqHead(AppConfig.COMM_REPLYTOPIC));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req);
		Log.e("wangke", map + "");
	}

	private boolean validate() {
		if (Tool.isEmpty(et_content.getText().toString())) {
			ToastUtil.showShortToast("请输入回复内容");
			return false;
		}
		return true;
	}

	// private void showEmotionView(boolean show) {
	// View v;
	// v = et_content;
	// if (show) {
	// InputTools.HideKeyboard(v);
	// new Handler().postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// emotionLayout.setVisibility(View.VISIBLE);
	// }
	// }, 200);
	// } else {
	// emotionLayout.setVisibility(View.GONE);
	// InputTools.ShowKeyboard(v);
	// }
	// }

	protected void showEmotionTxt(EmotionEditText etContent, String emotionText) {
		int start = etContent.getSelectionStart();
		StringBuffer sb = new StringBuffer(etContent.getText());
		if (emotionText.equals(":l7:")) {// 删除图标
			if (start >= 4
					&& EmotionHelper.getLocalEmoMap(ReflexActivity.this)
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

}
