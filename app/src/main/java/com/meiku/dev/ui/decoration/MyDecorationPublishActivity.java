package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 找装修--我的发布
 * 
 */
public class MyDecorationPublishActivity extends BaseFragmentActivity implements
		OnClickListener {

	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private MyCaseFragment myCaseFragment;
	private MyDecPublichCityActivity myDecPublichCityActivity;
	private ReceivedIntentFragment receivedIntentFragment;
	private DecDiscountFragment decDiscountFragment;
	private int[] tabLayoutIds = new int[] { R.id.tab_1, R.id.tab_2,
			R.id.tab_3, R.id.tab_4 };
	private int[] tabTvIds = new int[] { R.id.tv_1, R.id.tv_2, R.id.tv_3,
			R.id.tv_4 };
	private int[] tabLineIds = new int[] { R.id.line1, R.id.line2, R.id.line3,
			R.id.line4 };
	private TextView[] tabTViews = new TextView[4];
	private View[] tabLines = new View[4];
	private int tabsize = 4;
	private int reqCodeFour = 400;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_mydecorationpublish;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计activity
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计activity
		MobclickAgent.onPause(this);
	}

	@Override
	public void initView() {
		initTitleTabs();
		initViewpager();
	}

	private void initTitleTabs() {
		for (int i = 0, size = tabTViews.length; i < size; i++) {
			tabTViews[i] = (TextView) findViewById(tabTvIds[i]);
			tabLines[i] = (View) findViewById(tabLineIds[i]);
			findViewById(tabLayoutIds[i]).setOnClickListener(this);
		}
	}

	@Override
	public void initValue() {
	}

	private void initViewpager() {
		myCaseFragment = new MyCaseFragment();
		myDecPublichCityActivity = new MyDecPublichCityActivity();
		receivedIntentFragment = new ReceivedIntentFragment();
		decDiscountFragment = new DecDiscountFragment();
		listFragment.add(myCaseFragment);
		listFragment.add(myDecPublichCityActivity);
		listFragment.add(receivedIntentFragment);
		listFragment.add(decDiscountFragment);
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(4);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				showCurrentPageByIndex(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		showCurrentPageByIndex(0);
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
		case R.id.tab_1:
			showCurrentPageByIndex(0);
			break;
		case R.id.tab_2:
			showCurrentPageByIndex(1);
			break;
		case R.id.tab_3:
			showCurrentPageByIndex(2);
			break;
		case R.id.tab_4:
			showCurrentPageByIndex(3);
			break;
		default:
			break;
		}

	}

	private void showCurrentPageByIndex(int index) {
		for (int i = 0; i < tabsize; i++) {
			tabTViews[i].setTextColor(index == i ? Color.parseColor("#FF3499")
					: Color.parseColor("#000000"));
			tabLines[i].setVisibility(index == i ? View.VISIBLE
					: View.INVISIBLE);
		}
		viewpager.setCurrentItem(index, false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == reqCodeOne) {
				if (myCaseFragment != null && myCaseFragment.isVisible()) {
					myCaseFragment.downRefreshData();
				}
			} else if (requestCode == reqCodeTwo) {
				if (myDecPublichCityActivity != null
						&& myDecPublichCityActivity.isVisible()) {
					myDecPublichCityActivity.downRefreshData();
				}
			} else if (requestCode == reqCodeFour) {
				if (decDiscountFragment != null
						&& decDiscountFragment.isVisible()) {
					decDiscountFragment.downRefreshData();
				}
			}
		}
	}
}
