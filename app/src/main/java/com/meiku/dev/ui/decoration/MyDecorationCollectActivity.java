package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.DoEditObs;
import com.umeng.analytics.MobclickAgent;

/**
 * 找装修--我是店家--我的收藏
 * 
 */
public class MyDecorationCollectActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private View myshow_line;
	private View mycollect_line;
	private TextView tv_case, tv_company;
	private TextView right_txt_title, center_txt_title;
	private boolean isSelectModel = false;
	private CollectCaseFragment myShowFragment;
	private CollectCompanyFragment myCollectFragment;

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
	protected int getCurrentLayoutID() {
		return R.layout.activity_myshowworks;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		center_txt_title.setText("我的收藏");
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(null);
		right_txt_title.setOnClickListener(this);
		initTabs();
		initViewpager();
	}

	private void initTabs() {
		findViewById(R.id.tab_myshow).setOnClickListener(this);
		findViewById(R.id.tab_collect).setOnClickListener(this);
		tv_case = (TextView) findViewById(R.id.tv_myshow);
		tv_case.setText("装修案例");
		tv_company = (TextView) findViewById(R.id.tv_mycollect);
		tv_company.setText("装修公司");
		myshow_line = (View) findViewById(R.id.myshow_line);
		mycollect_line = (View) findViewById(R.id.mycollect_line);
	}

	private void initViewpager() {
		myShowFragment = new CollectCaseFragment();
		myCollectFragment = new CollectCompanyFragment();
		listFragment.add(myShowFragment);
		listFragment.add(myCollectFragment);
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
		tv_case.setTextColor(b ? getResources().getColor(R.color.clo_ff3499)
				: getResources().getColor(R.color.black_light));
		tv_company.setTextColor(b ? getResources()
				.getColor(R.color.black_light) : getResources().getColor(
				R.color.clo_ff3499));
		viewpager.setCurrentItem(b ? 0 : 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (null != myShowFragment) {
				myShowFragment.downRefreshData();
			}
			if (null != myCollectFragment) {
				myCollectFragment.downRefreshData();
			}
		}
	}

	@Override
	protected void onDestroy() {
		DoEditObs.getInstance().clear();
		super.onDestroy();
	}

}
