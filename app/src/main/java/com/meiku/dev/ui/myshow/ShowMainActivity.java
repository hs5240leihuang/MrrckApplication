package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
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
import com.meiku.dev.bean.MatchCityEntity;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.vote.ActivityRuleFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.MyShowTabsLayout;
import com.meiku.dev.views.MyShowTabsLayout.TabClickListener;

/**
 * 才艺秀主页面
 * 
 */
@SuppressLint("NewApi")
public class ShowMainActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnClickListener,
		ShowHomeFragment.FragmentInteraction {

	/**
	 * 需要显示的tabsBar数据
	 */
	private String[] titletabs = new String[] { "首页", "介绍", "奖项设置" };
	private ViewPager viewpager;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private LinearLayout titleTabLayout;
	private MyShowTabsLayout titletabsBarView;
	private OderViewPagerAdapter pageAdapter;
	private Integer postsId;
	private TextView center_txt_title;
	private android.app.FragmentManager manager;
	private FragmentTransaction transaction;
	private ShowHomeFragment showHomeFragment;
	private ActivityRuleFragment activityRuleFragment;
	private AwardsFragment awardsFragment;
	private MatchCityEntity matchCity;
	private PostsEntity postsEntity;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myshowmain;
	}

	@Override
	public void initView() {
		postsId = getIntent().getIntExtra("postsId", 0);
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
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
		showHomeFragment = new ShowHomeFragment();
		activityRuleFragment = new ActivityRuleFragment();
		awardsFragment = new AwardsFragment();
		listFragment.add(showHomeFragment);
		listFragment.add(activityRuleFragment);
		listFragment.add(awardsFragment);
		broadcast();
		// title tabs bar
		titleTabLayout = (LinearLayout) findViewById(R.id.tabLayout);
		titletabsBarView = new MyShowTabsLayout(ShowMainActivity.this,
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
		matchCity = (MatchCityEntity) getIntent().getSerializableExtra(
				"matchCity");
		titletabsBarView
				.SetSelecetedIndex(getIntent().getIntExtra("tabNum", 0));
	}

	@Override
	public void bindListener() {
		findViewById(R.id.btn_enroll).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "00##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("signupFlag") + "").length() > 2) {
				String signupFlag = resp.getBody().get("signupFlag")
						.getAsString();
				if (signupFlag.equals("1")) {
					ToastUtil.showShortToast("您已报名");
				} else {
					Intent intent = new Intent(ShowMainActivity.this,
							EnrollActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("postEntity", postsEntity);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}

			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_enroll:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
				return;
			}
			if (postsEntity.getSignupFlag().equals("1")) {
				if ((postsEntity.getApplyMaxNum() - postsEntity.getApplyNum()) > 0) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getUserId());
					map.put("postsId", postsEntity.getPostsId());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_IS_ENROLL));
					req.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
				} else {
					ToastUtil.showShortToast("报名人数已满，请选择其他比赛");
				}
			} else if (postsEntity.getSignupFlag().equals("0")) {
				ToastUtil.showShortToast("报名尚未开始");
			} else {
				ToastUtil.showShortToast("比赛已结束");
			}
			break;
		default:
			break;
		}

	}

	@SuppressLint("NewApi")
	public void broadcast() {
		manager = getFragmentManager();
		transaction = manager.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("postsId", postsId);
		showHomeFragment.setArguments(bundle);
		awardsFragment.setArguments(bundle);
		activityRuleFragment.setArguments(bundle);
		transaction.commit();
	}

	@Override
	public void process(PostsEntity postsEntity) {
		this.postsEntity = postsEntity;
		center_txt_title.setText(postsEntity.getTitle());
	}
}
