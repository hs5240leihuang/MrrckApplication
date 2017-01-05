package com.meiku.dev.ui.community;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseActivity;

/**
 * 显示大字号文本信息
 *
 */
public class ShowDetailTxtActivity extends BaseActivity {

	private TextView tv;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_showdetailtxt;
	}

	@Override
	public void initView() {
		tv = (TextView) findViewById(R.id.txt);
		tv.setText(getIntent().getStringExtra("txt"));
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
