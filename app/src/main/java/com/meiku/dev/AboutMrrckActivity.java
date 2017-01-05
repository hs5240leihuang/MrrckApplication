package com.meiku.dev;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.VersionUtils;

public class AboutMrrckActivity extends BaseActivity {
	private ImageView back;
	private TextView tv_version;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_about_mrrck;
	}

	@Override
	public void initView() {
		back = (ImageView) findViewById(R.id.left_res_title);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("当前版本 ： "+VersionUtils.getVersionName(getApplicationContext()));
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
