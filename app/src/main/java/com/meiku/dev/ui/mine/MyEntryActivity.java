package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.Tool;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的词条页面
 * 
 */
public class MyEntryActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private LinearLayout tab_my, tab_collect;
	private View myshow_line;
	private View mycollect_line;
	private int userId;
	private TextView tv_my;
	private TextView tv_mycollect;
	private MyEntryFragment myEntryFragment;
	private MyEntryCollectFragment myCollectEntryFragment;
	private ImageView left_res_title;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myentry;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void initView() {
		left_res_title =  (ImageView) findViewById(R.id.left_res_title);
		userId = getIntent().getIntExtra("userId",
				AppContext.getInstance().getUserInfo().getId());
		initTabs();
		initViewpager();
	}

	private void initTabs() {
		tab_my = (LinearLayout) findViewById(R.id.tab_my);
		tab_my.setOnClickListener(this);
		tab_collect = (LinearLayout) findViewById(R.id.tab_collect);
		tab_collect.setOnClickListener(this);
		tv_my = (TextView) findViewById(R.id.tv_my);
		tv_mycollect = (TextView) findViewById(R.id.tv_mycollect);
		myshow_line = (View) findViewById(R.id.myshow_line);
		mycollect_line = (View) findViewById(R.id.mycollect_line);
	}

	private void initViewpager() {
		FragmentTransaction trans = getFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("userId", userId);
		myEntryFragment = new MyEntryFragment();
		myEntryFragment.setArguments(bundle);
		listFragment.add(myEntryFragment);
		myCollectEntryFragment = new MyEntryCollectFragment();
		myCollectEntryFragment.setArguments(bundle);
		listFragment.add(myCollectEntryFragment);
		trans.commit();
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(2);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				showMyworksOrCollect(arg0 == 0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		showMyworksOrCollect(true);
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		left_res_title.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_my:
			showMyworksOrCollect(true);
			break;

		case R.id.tab_collect:
			showMyworksOrCollect(false);
			break;
		case R.id.left_res_title:
			setResult(RESULT_OK);
			finish();
			break;
		}

	}

	/**
	 * 
	 * @param b
	 *            true显示我的秀场，false显示我的收藏
	 */
	private void showMyworksOrCollect(boolean b) {
		myshow_line.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
		mycollect_line.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
		tv_my.setTextColor(b ? getResources().getColor(R.color.mrrck_bg)
				: getResources().getColor(R.color.black_light));
		tv_mycollect.setTextColor(b ? getResources().getColor(
				R.color.black_light) : getResources()
				.getColor(R.color.mrrck_bg));
		viewpager.setCurrentItem(b ? 0 : 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (!Tool.isEmpty(myEntryFragment)) {
				myEntryFragment.downRefreshData();
			}
			if (!Tool.isEmpty(myCollectEntryFragment)) {
				myCollectEntryFragment.downRefreshData();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
		}
		return super.onKeyDown(keyCode, event);
	}

}
