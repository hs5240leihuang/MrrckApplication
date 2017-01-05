package com.meiku.dev.ui.im;

import android.content.Intent;
import android.view.View;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseActivity;

public class ReportSuccessActivity extends BaseActivity {

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_report_success;
	}

	@Override
	public void initView() {
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
		findViewById(R.id.tv_rule).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(ReportSuccessActivity.this,
								ReportRuleActivity.class));
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
