package com.meiku.dev.ui.morefun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.meiku.dev.R;

/**
 * Created by Administrator on 2015/8/31.
 */
public class SelectVoiceActivity extends Activity implements
		View.OnClickListener {
	private Button playBtn, reVoiceBtn, cancelBtn;
	private LinearLayout dialogLayout;
	private String filePath;// 文件路径
	private String recordingTime;// 录音时间
	private String isPublish;
	public static final String BUNDLE_IS_PUBLISH = "is_publish";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_voice_activity_layout);
		isPublish = getIntent().getStringExtra(BUNDLE_IS_PUBLISH);
		filePath = getIntent().getStringExtra("filePath");
		recordingTime = getIntent().getStringExtra("recordingTime");
		initView();
	}

	private void initView() {
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		playBtn = (Button) findViewById(R.id.play_voice);
		playBtn.setOnClickListener(this);
		reVoiceBtn = (Button) findViewById(R.id.re_voice);
		reVoiceBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_voice:
			Intent intent = new Intent(this, TestAudioActivity.class);
			// String filePathname = filePath;
			filePath = filePath.substring(0, filePath.lastIndexOf("."));
			intent.putExtra("filePath", filePath);
			intent.putExtra("recordingTime", recordingTime + "");
			if (!TextUtils.isEmpty(isPublish)) {
				intent.putExtra(TestAudioActivity.BUNDLE_IS_PUBLISH, isPublish);
			}
			startActivityForResult(intent, 1);
			break;
		case R.id.re_voice:
			intent = new Intent(this, RecordActivity.class);
			intent.putExtra(RecordActivity.BUNDLE_IS_PUBLISH, "1");
			startActivityForResult(intent,
					RecruitmentTreasureActivity.TAKE_SOUND);
			finish();
			break;
		case R.id.btn_cancel:
			finish();
			SelectVoiceActivity.this.overridePendingTransition(
					R.anim.push_bottom_out, 0);
			break;
		}
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
}
