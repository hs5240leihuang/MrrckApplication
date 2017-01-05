package com.meiku.dev.ui.plan;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;

/**
 * 找策划某一类列表2（不带店家分类）
 */
public class PlanTwoActivity extends BaseFragmentActivity {

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_plantwo;
	}

	@Override
	public void initView() {
		TextView center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		center_txt_title.setText(getIntent().getStringExtra("title"));
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FragmentPostList fragment = FragmentPostList.newInstance(0, -1,
				getIntent().getIntExtra("caseType", -1));
		ft.add(R.id.layout_main, fragment);
		ft.commit();
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
