package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的招聘
 * 
 */
public class MyRecruitFragment extends BaseFragment implements OnClickListener,
		OnPageChangeListener {

	private View layout_view;
	int tabSize = 5;
	private TextView[] tab_tvs = new TextView[tabSize];
	private int[] tvIDs = new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4,
			R.id.tv5 };
	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	/** 所有Fragment页面 */
	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private int currentShowPage = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_myrecruit, null);
		init();
		return layout_view;

	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
	}

	private void init() {
		fragmentList.add(new ReceiveResumeFragment());
		fragmentList.add(new InvitationRecordFragment());
		fragmentList.add(new CollectResumeFragment());
		fragmentList.add(new CompanyInformationFragment());
		fragmentList.add(new UsageSummaryFragment());
		for (int i = 0; i < tabSize; i++) {
			tab_tvs[i] = (TextView) layout_view.findViewById(tvIDs[i]);
			tab_tvs[i].setOnClickListener(this);
		}
		initViewPager();
	}

	private void initViewPager() {
		viewpager = (ViewPager) layout_view
				.findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getChildFragmentManager(),
				fragmentList);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(tabSize);
		viewpager.setCurrentItem(0);
		setTabSelected(0, true);
	}

	@Override
	public void initValue() {
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
		int currentClickIndex = 0;
		for (int i = 0; i < tabSize; i++) {
			if (v == tab_tvs[i]) {
				currentClickIndex = i;
			}
			setTabSelected(i, v == tab_tvs[i]);
		}
		if (currentShowPage != currentClickIndex) {
			viewpager.setCurrentItem(currentClickIndex);
		}
	}

	private void setTabSelected(int index, boolean selected) {
		if (selected) {
			tab_tvs[index].setTextColor(getResources().getColor(R.color.white));
			if (index == 0) {
				tab_tvs[index]
						.setBackgroundResource(R.drawable.leftradius_mrrck_solid);
			} else if (index == 4) {
				tab_tvs[index]
						.setBackgroundResource(R.drawable.rightradius_mrrck_solid);
			} else {
				tab_tvs[index].setBackgroundColor(getResources().getColor(
						R.color.mrrck_bg));
			}

		} else {
			tab_tvs[index].setTextColor(getResources().getColor(R.color.gray));
			tab_tvs[index].setBackgroundColor(getResources().getColor(
					R.color.transparent));
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
	public void onPageSelected(int index) {
		currentShowPage = index;
		for (int i = 0; i < tabSize; i++) {
			setTabSelected(i, i == index);
		}
	}

}
