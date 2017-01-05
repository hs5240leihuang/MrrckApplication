package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.DoEditObs;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的秀场（Ta的秀场）
 * 
 */
public class MyShowWorksActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private LinearLayout tab_myshow, tab_collect;
	private View myshow_line;
	private View mycollect_line;
	private TextView tv_myshow, tv_mycollect;
	private TextView right_txt_title, center_txt_title;
	private boolean isSelectModel = false;
	private MyShowFragment myShowFragment;
	private MyCollectFragment myCollectFragment;
	private int userId;
	private LinearLayout floatTablayout;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myshowworks;
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
		userId = getIntent().getIntExtra("userId",
				AppContext.getInstance().getUserInfo().getId());
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(null);
		right_txt_title.setOnClickListener(this);
		initTabs();
		initViewpager();
	}

	private void initTabs() {
		floatTablayout = (LinearLayout) findViewById(R.id.floatTablayout);
		tab_myshow = (LinearLayout) findViewById(R.id.tab_myshow);
		tab_myshow.setOnClickListener(this);
		tab_collect = (LinearLayout) findViewById(R.id.tab_collect);
		tab_collect.setOnClickListener(this);
		tv_myshow = (TextView) findViewById(R.id.tv_myshow);
		tv_mycollect = (TextView) findViewById(R.id.tv_mycollect);
		myshow_line = (View) findViewById(R.id.myshow_line);
		mycollect_line = (View) findViewById(R.id.mycollect_line);
	}

	private void initViewpager() {
		myShowFragment = new MyShowFragment();
		FragmentTransaction trans = getFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("userId", userId);
		myCollectFragment = new MyCollectFragment();
		if (userId == AppContext.getInstance().getUserInfo().getId()) {
			listFragment.add(myShowFragment);
			listFragment.add(myCollectFragment);
			myCollectFragment.setArguments(bundle);
			myShowFragment.setArguments(bundle);
			floatTablayout.setVisibility(View.VISIBLE);
			center_txt_title.setText("我的秀场");
		} else {
			myShowFragment.setArguments(bundle);
			listFragment.add(myShowFragment);
			floatTablayout.setVisibility(View.GONE);
			center_txt_title.setText("TA的秀场");
			right_txt_title.setVisibility(View.GONE);
		}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_myshow:
			showMyworksOrCollect(true);
			break;

		case R.id.tab_collect:
			showMyworksOrCollect(false);
			break;
		case R.id.right_txt_title:
			isSelectModel = !isSelectModel;
			right_txt_title.setText(isSelectModel ? "取消" : "选择");
			DoEditObs.getInstance().notifyDoEdit(isSelectModel);
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
		tv_myshow.setTextColor(b ? getResources().getColor(R.color.mrrck_bg)
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
			myShowFragment.downRefreshData();
			myCollectFragment.downRefreshData();
		}
	}

	@Override
	protected void onDestroy() {
		DoEditObs.getInstance().clear();
		super.onDestroy();
	}
}
