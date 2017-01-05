package com.meiku.dev.ui.morefun;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.AudioRecorderToMP3Util;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.SdcardUtil;

/**
 * 语音录制
 */
public class RecordActivity extends BaseActivity implements
		View.OnClickListener {

	public static final String BUNDLE_IS_PUBLISH = "is_publish";

	private TextView time;// 倒计时
	private TextView complateBtn;// 完成按钮
	private TextView cancelBtn;// 取消按钮
	private boolean canClean = false;// 是否可去取消
	AudioRecorderToMP3Util recorder = null;// 录音工具
	private TimeCount timeCount;// 计时器
	private String filePath;// 文件路径

	private String isPublish;
	private int voiceTime = 60;// 总时间十秒
	private int recordingTime;// 录音时间

	// 开始录音
	private void startRecord() {
		timeCount = new TimeCount(60000, 1000);
		timeCount.start();
		if (recorder == null) {
			recorder = new AudioRecorderToMP3Util(null, filePath + ".raw",
					filePath + ".mp3");
		}
		if (canClean) {
			recorder.cleanFile(AudioRecorderToMP3Util.MP3
					| AudioRecorderToMP3Util.RAW);
		}
		recorder.startRecording();
		canClean = true;
	}

	@Override
	public void onClick(View v) {
		timeCount.cancel();
		switch (v.getId()) {
		case R.id.tv_complete:
			finishRecord();
			break;
		case R.id.tv_cancal:
			cancelRecord();
			break;
		}
	}

	// 完成录音
	private void finishRecord() {
		if (recorder != null) {
			recorder.stopRecordingAndConvertFile();

			recorder.cleanFile(AudioRecorderToMP3Util.RAW);
			recorder.close();
			recorder = null;
		} else {
			Log.e("RecordAct", "null");
		}

		Intent intent = new Intent(this, TestAudioActivity.class);
		intent.putExtra("filePath", filePath);
		intent.putExtra("recordingTime", recordingTime + "");
		intent.putExtra(TestAudioActivity.BUNDLE_IS_PUBLISH, "1");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				setResult(RESULT_OK, data);
				finish();
				break;
			default:
				break;
			}
		}
	}

	// 取消录音
	private void cancelRecord() {
		if (recorder != null) {
			recorder.cleanFile(AudioRecorderToMP3Util.RAW);
			recorder.close();
			recorder = null;
		}
		finish();
		overridePendingTransition(R.anim.alpha_out, 0);
	}

	// 定时器
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			time.setText("00:00");
			finishRecord();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			time.setText("00:"
					+ ((millisUntilFinished / 1000) >= 10 ? millisUntilFinished / 1000
							: ("0" + millisUntilFinished / 1000)));
			if (((int) millisUntilFinished / 1000) <= 0) {
				recordingTime = 60;
			} else {
				recordingTime = voiceTime - (int) millisUntilFinished / 1000;
			}

		}
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
		return R.layout.activity_record;
	}

	@Override
	public void initView() {
		time = (TextView) findViewById(R.id.tv_time);
		complateBtn = (TextView) findViewById(R.id.tv_complete);
		complateBtn.setOnClickListener(this);
		cancelBtn = (TextView) findViewById(R.id.tv_cancal);
		cancelBtn.setOnClickListener(this);
		filePath = SdcardUtil.getPath()
				+ FileConstant.UPLOAD_AUDIO_PATH
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(System
						.currentTimeMillis());
	}

	@Override
	public void initValue() {
		setFinishOnTouchOutside(false);
		isPublish = getIntent().getStringExtra(BUNDLE_IS_PUBLISH);
		startRecord();
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

}
