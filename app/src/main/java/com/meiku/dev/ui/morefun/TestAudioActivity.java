package com.meiku.dev.ui.morefun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;

import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CircleProgress;

public class TestAudioActivity extends BaseActivity implements
		View.OnClickListener {

	public final static int REQCODE_PUBLISHAUDIO = 1000;

	public static final String BUNDLE_IS_PUBLISH = "is_publish";

	private String filePath;// 文件路径
	private CircleProgress circleProgress;// 进度条
	private ImageView playAudioBtn;// 播放按钮
	private LinearLayout usePublicBtn;// 使用并公开按钮
	private LinearLayout usePrivateBtn;// 使用不公开按钮
	private TextView cancelBtn;// 取消按钮
	private MediaPlayer myPlayer;// 播放器
	private int fileSeconds;// 视频长度
	private static final int PUBLIC = 0;// 公开
	private static final int PRIVATE = 1;// 不公开

	private LinearLayout finishLL;

	private String isPublish;

	private String recordingTime;// 获取音频播放时间

	// 初始化控件
	@Override
	public void initView() {
		isPublish = getIntent().getStringExtra(BUNDLE_IS_PUBLISH);
		myPlayer = new MediaPlayer();
		filePath = getIntent().getStringExtra("filePath") + ".mp3";
		recordingTime = getIntent().getStringExtra("recordingTime");// 播放时间
		circleProgress = (CircleProgress) findViewById(R.id.progress_bar);
		playAudioBtn = (ImageView) findViewById(R.id.audio_play_btn);
		playAudioBtn.setOnClickListener(this);
		usePublicBtn = (LinearLayout) findViewById(R.id.tv_user_open);
		usePublicBtn.setOnClickListener(this);
		usePrivateBtn = (LinearLayout) findViewById(R.id.tv_user_not_open);
		usePrivateBtn.setOnClickListener(this);
		cancelBtn = (TextView) findViewById(R.id.tv_cancel);
		cancelBtn.setOnClickListener(this);
		finishLL = (LinearLayout) findViewById(R.id.finishLL);
		finishLL.setOnClickListener(this);

		if (!Tool.isEmpty(isPublish)) {
			finishLL.setVisibility(View.VISIBLE);
			usePublicBtn.setVisibility(View.GONE);
			usePrivateBtn.setVisibility(View.GONE);
		} else {
			finishLL.setVisibility(View.GONE);
			usePublicBtn.setVisibility(View.GONE);
			usePrivateBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_play_btn:
			if (myPlayer.isPlaying()) {
				stopAudio();
			} else {
				playAudio();
			}
			break;
		case R.id.tv_user_open:
			stopAudio();
			publishAudio(PUBLIC);
			break;
		case R.id.tv_user_not_open:
			stopAudio();
			publishAudio(PRIVATE);
			break;
		case R.id.tv_cancel: // 取消
			stopAudio();
			myPlayer.release();
			finishTakeAudio(null);
			break;
		case R.id.finishLL: // 完成
			stopAudio();
			finishTakeAudio(filePath);
			break;
		}
	}

	// 播放音频
	public void playAudio() {
		try {
			myPlayer.reset();
			// 播放本地音频
			myPlayer.setDataSource(filePath);

			// 播放网络音频
			// myPlayer.setDataSource("http://www.citynorth.cn/music/confucius.mp3");
			if (!myPlayer.isPlaying()) {
				myPlayer.prepare();
				myPlayer.start();
				playAudioBtn.setImageResource(R.drawable.record_audio_pause);
				fileSeconds = myPlayer.getDuration() / 1000;
				circleProgress.startCartoom(fileSeconds);
			} else {
				myPlayer.pause();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void finishTakeAudio(String file) {

		if (!TextUtils.isEmpty(file)) {
			Intent intent = new Intent();
			intent.putExtra("file", file);
			intent.putExtra("recordingTime", recordingTime);// 播放时间
			setResult(RESULT_OK, intent);
		} else {
			setResult(RESULT_OK);
		}

		finish();
	}

	// 停止播放
	public void stopAudio() {
		if (myPlayer.isPlaying()) {
			myPlayer.stop();
			playAudioBtn.setImageResource(R.drawable.record_audio_play);
			circleProgress.stopCartoom();
		}
	}

	// 发布音频
	private void publishAudio(int isPublic) {

		// UserDataLogic.getInstance().quickPublishAudioWithUserId(
		// AppData.getInstance().getLoginUser().getUserId(), filePath,
		// time, "", "", isPublic, new HttpCallback() {
		// @Override
		// public void onSuccess(Object arg) {
		// ToastUtil.showShortToast("发布成功");
		//
		// finishTakeAudio(null);
		// }
		//
		// @Override
		// public void onFailed(String arg) {
		// Toast.makeText(TestAudioActivity.this, arg,
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		// int time = Integer.parseInt(recordingTime);
		// ReqBase reqBase = new ReqBase();
		// reqBase.setHeader(new
		// ReqHead(AppConfig.BUSINESS_QUICK_PUBLISH_AUDIO));
		// Map<String,Object> body = new HashMap<String, Object>();
		// body.put("userId", AppContext.getInstance().getUserInfo().getId());
		// body.put("fileSeconds", time);
		// body.put("title", "");
		// body.put("remark", "");
		// body.put("isPublic", isPublic);
		// reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(body)));
		// Map<String,List<FormFileBean>> mapFileBean = new HashMap<String,
		// List<FormFileBean>>();
		// List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		// FormFileBean formFile = new FormFileBean(new
		// File(filePath),"audio.mp3");
		// formFileBeans.add(formFile);
		// mapFileBean.put("voice", formFileBeans);
		// uploadFiles(REQCODE_PUBLISHAUDIO, AppConfig.USER_REQUEST_MAPPING,
		// mapFileBean, reqBase, true);

		finishTakeAudio(filePath);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_test_audio;
	}

	@SuppressLint("NewApi")
	@Override
	public void initValue() {
		setFinishOnTouchOutside(false);
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case REQCODE_PUBLISHAUDIO:
			LogUtil.e((String) arg0);
			finishTakeAudio(null);
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

}
