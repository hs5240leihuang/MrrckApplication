package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.views.MyShowTabsLayout;
import com.meiku.dev.views.MyShowTabsLayout.TabClickListener;
import com.umeng.analytics.MobclickAgent;

public class MyCommunityActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnClickListener {

	/**
	 * 需要显示的tabsBar数据
	 */
	private String[] titletabs;
	private ViewPager viewpager;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private LinearLayout titleTabLayout;
	private MyShowTabsLayout titletabsBarView;
	private OderViewPagerAdapter pageAdapter;
	private android.app.FragmentManager manager;
	private FragmentTransaction transaction;
	private MyCommunityPostFragment myCommunityPostFragment;
	private MyCommunityReplyFragment myCommunityReplyFragment;
	private MyCommunityCollectFragment myCommunityCollectFragment;

	private int userId;
	private TextView center_txt_title;

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
	protected int getCurrentLayoutID() {
		return R.layout.activity_mycommunity;
	}

	@Override
	public void initView() {
		initTitleTabs();
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		pageAdapter = new OderViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(3);
	}

	/**
	 * 初始化动态title tab
	 */
	private void initTitleTabs() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		userId = getIntent().getIntExtra("userId",
				AppContext.getInstance().getUserInfo().getId());
		myCommunityPostFragment = new MyCommunityPostFragment();
		myCommunityReplyFragment = new MyCommunityReplyFragment();

		listFragment.add(myCommunityPostFragment);
		listFragment.add(myCommunityReplyFragment);
		if (userId == AppContext.getInstance().getUserInfo().getId()) {// 查看我的
			center_txt_title.setText("我的社区");
			titletabs = new String[] { "我发布的帖子", "我的回复", "我的收藏" };
			myCommunityCollectFragment = new MyCommunityCollectFragment();
			listFragment.add(myCommunityCollectFragment);
		} else {
			center_txt_title.setText("TA的社区");
			titletabs = new String[] { "TA发布的帖子", "TA的回复" };
		}
		FragmentTransaction trans = getFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("userId", userId);
		myCommunityPostFragment.setArguments(bundle);
		myCommunityReplyFragment.setArguments(bundle);
		if (userId == AppContext.getInstance().getUserInfo().getId()) {// 查看我的
			myCommunityCollectFragment.setArguments(bundle);
		}
		trans.commit();
		titleTabLayout = (LinearLayout) findViewById(R.id.tabLayout);
		titletabsBarView = new MyShowTabsLayout(MyCommunityActivity.this,
				titletabs, new TabClickListener() {

					@Override
					public void onTabClick(int index) {
						viewpager.setCurrentItem(index);
					}
				});
		titletabsBarView.setCurrentIndex(0);
		titleTabLayout.addView(titletabsBarView.getView());
	}

	@Override
	public void initValue() {

		titletabsBarView
				.SetSelecetedIndex(getIntent().getIntExtra("tabNum", 0));
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "00##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:

			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

	}

	class OderViewPagerAdapter extends FragmentPagerAdapter {

		private List<BaseFragment> arraylist;

		public OderViewPagerAdapter(FragmentManager fragmentManager,
				List<BaseFragment> arraylist) {
			super(fragmentManager);
			this.arraylist = arraylist;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			android.support.v4.app.Fragment fragment = arraylist.get(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return arraylist == null ? 0 : arraylist.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		titletabsBarView.SetSelecetedIndex(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (myCommunityPostFragment != null) {
				myCommunityPostFragment.downRefreshData();
			}
			if (myCommunityReplyFragment != null) {
				myCommunityReplyFragment.downRefreshData();
			}
			if (myCommunityCollectFragment != null) {
				myCommunityCollectFragment.downRefreshData();
			}
		}
	}

}
