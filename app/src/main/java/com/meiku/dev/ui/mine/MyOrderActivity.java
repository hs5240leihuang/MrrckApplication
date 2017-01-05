package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;

/**
 * 我的订单
 * 
 */
public class MyOrderActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private TextView tv_allOrder, tv_finishedOrder, tv_needPayOrder;
	private View allOrder_line, finishedOrder_line, needPayOrder_line;
	private OrderFragment orderAllFragment, orderFinishedFragment,
			orderNeedPayFragment;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myorder;
	}

	@Override
	public void initView() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_PAYRESULT);
		registerReceiver(receiver, filter);
		findViewById(R.id.tab_allOrder).setOnClickListener(this);
		findViewById(R.id.tab_finishedOrder).setOnClickListener(this);
		findViewById(R.id.tab_needPayOrder).setOnClickListener(this);

		tv_allOrder = (TextView) findViewById(R.id.tv_allOrder);
		tv_finishedOrder = (TextView) findViewById(R.id.tv_finishedOrder);
		tv_needPayOrder = (TextView) findViewById(R.id.tv_needPayOrder);
		allOrder_line = (View) findViewById(R.id.allOrder_line);
		finishedOrder_line = (View) findViewById(R.id.finishedOrder_line);
		needPayOrder_line = (View) findViewById(R.id.needPayOrder_line);

		orderAllFragment = OrderFragment.newInstance(1, 0);
		orderFinishedFragment = OrderFragment.newInstance(2, 1);
		orderNeedPayFragment = OrderFragment.newInstance(3, 2);
		listFragment.add(orderAllFragment);
		listFragment.add(orderFinishedFragment);
		listFragment.add(orderNeedPayFragment);
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(3);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				showPageByType(arg0);
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
		showPageByType(0);
	}

	protected void showPageByType(int tabIndex) {
		allOrder_line.setVisibility(tabIndex == 0 ? View.VISIBLE
				: View.INVISIBLE);
		finishedOrder_line.setVisibility(tabIndex == 1 ? View.VISIBLE
				: View.INVISIBLE);
		needPayOrder_line.setVisibility(tabIndex == 2 ? View.VISIBLE
				: View.INVISIBLE);
		tv_allOrder.setTextColor(tabIndex == 0 ? Color.parseColor("#FF3499")
				: getResources().getColor(R.color.black_light));
		tv_finishedOrder.setTextColor(tabIndex == 1 ? Color
				.parseColor("#FF3499") : getResources().getColor(
				R.color.black_light));
		tv_needPayOrder.setTextColor(tabIndex == 2 ? Color
				.parseColor("#FF3499") : getResources().getColor(
				R.color.black_light));
		viewpager.setCurrentItem(tabIndex);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == reqCodeThree) {
				if (orderAllFragment != null) {
					orderAllFragment.downRefreshData();
				}
				if (orderFinishedFragment != null) {
					orderFinishedFragment.downRefreshData();
				}
				if (orderNeedPayFragment != null) {
					orderNeedPayFragment.downRefreshData();
				}
			}

		}
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
		case R.id.tab_allOrder:
			showPageByType(0);
			viewpager.setCurrentItem(0);
			break;
		case R.id.tab_finishedOrder:
			showPageByType(1);
			viewpager.setCurrentItem(1);
			break;
		case R.id.tab_needPayOrder:
			showPageByType(2);
			viewpager.setCurrentItem(2);
			break;
		}

	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_PAYRESULT.equals(intent.getAction())) {
				if (orderAllFragment != null) {
					orderAllFragment.downRefreshData();
				}
				if (orderFinishedFragment != null) {
					orderFinishedFragment.downRefreshData();
				}
				if (orderNeedPayFragment != null) {
					orderNeedPayFragment.downRefreshData();
				}
			}
		}
	};

}
