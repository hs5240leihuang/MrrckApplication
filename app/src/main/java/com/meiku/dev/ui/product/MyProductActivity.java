package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.views.MyProductTabsLayout;
import com.meiku.dev.views.MyProductTabsLayout.TabClickListener;
import com.umeng.analytics.MobclickAgent;

/** 我的产品 */
public class MyProductActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnClickListener {

	/**
	 * 需要显示的tabsBar数据
	 */
	private String[] titletabs = new String[] { "我的发布", "收到意向", "产品收藏" };
	private ViewPager viewpager;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private LinearLayout titleTabLayout;
	private MyProductTabsLayout titletabsBarView;
	private OderViewPagerAdapter pageAdapter;
	private android.app.FragmentManager manager;
	private FragmentTransaction transaction;
	private MyPublishFragment myPublishFragment;
	private ReceiveIntentFragment receiveIntentFragment;
	private ProductCollectFragment productCollectFragment;

	private TextView right_txt_title;
	private int selectIndex;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_my_product;
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
		selectIndex = getIntent().getIntExtra("index", 0);
		initTitleTabs();
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		pageAdapter = new OderViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(3);
		viewpager.setCurrentItem(selectIndex);
	}

	/**
	 * 初始化动态title tab
	 */
	private void initTitleTabs() {
		myPublishFragment = new MyPublishFragment();
		receiveIntentFragment = new ReceiveIntentFragment();
		productCollectFragment = new ProductCollectFragment();
		listFragment.add(myPublishFragment);
		listFragment.add(receiveIntentFragment);
		listFragment.add(productCollectFragment);
		// title tabs bar
		titleTabLayout = (LinearLayout) findViewById(R.id.topLayout);
		titletabsBarView = new MyProductTabsLayout(MyProductActivity.this,
				titletabs, new TabClickListener() {

					@Override
					public void onTabClick(int index) {
						viewpager.setCurrentItem(index);
					}
				});
		titletabsBarView.setCurrentIndex(selectIndex);
		titleTabLayout.addView(titletabsBarView.getView());
	}

	@Override
	public void initValue() {
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.radius_border_bg_white));
		right_txt_title.setTextColor(getResources().getColor(R.color.mrrck_bg));
	}

	@Override
	public void bindListener() {

		right_txt_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MyProductActivity.this,
						PublishProductActivity.class);
				startActivity(i);
			}
		});
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		titletabsBarView.SetSelecetedIndex(arg0);
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
}
