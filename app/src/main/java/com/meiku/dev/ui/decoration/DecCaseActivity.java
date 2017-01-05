package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.views.ShowTitleTabsLayout2;
import com.meiku.dev.views.ShowTitleTabsLayout2.TabClickListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 装修案例
 * 
 */
public class DecCaseActivity extends BaseFragmentActivity implements
		OnPageChangeListener {

	private List<MkPostsActiveCategory> titletabList = new ArrayList<MkPostsActiveCategory>();
	private LinearLayout topTabLayout;
	private ShowTitleTabsLayout2 tabsBarView;
	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	/** 所有Fragment页面 */
	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private int tabSize;
	protected boolean changeTabByClick;
	private int shopCategory = -1;
	private List<MkDecorateCategory> typeList;
	private int selectCityCode, selectProvinceCode;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_deccase;
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
		selectCityCode = getIntent().getIntExtra("selectCityCode", -1);
		selectProvinceCode = getIntent().getIntExtra("selectProvinceCode", -1);
		initTabs();
		initViewPager();
	}

	private void initViewPager() {
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				fragmentList);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(tabSize);
		viewpager.setCurrentItem(0);
	}

	@Override
	public void initValue() {
	}

	private void initTabs() {
		typeList = MKDataBase.getInstance().getDecorateCategoryList(0);
		for (int i = 0, size = typeList.size(); i < size; i++) {
			MkPostsActiveCategory cate = new MkPostsActiveCategory();
			cate.setName(typeList.get(i).getName());
			titletabList.add(cate);
		}
		topTabLayout = (LinearLayout) findViewById(R.id.topTabLayout);
		tabSize = titletabList.size();
		int cellSize;
		if (tabSize == 0 || tabSize > 6) {
			cellSize = 6;
		} else {
			cellSize = tabSize;
		}
		int tabBarWidth = ScreenUtil.SCREEN_WIDTH;
		tabsBarView = new ShowTitleTabsLayout2(DecCaseActivity.this,
				tabBarWidth / cellSize, titletabList, new TabClickListener() {

					@Override
					public void onTabClick(int index) {
						changeTabByClick = true;
						shopCategory = typeList.get(index).getCode();
						viewpager.setCurrentItem(index);
					}
				});
		tabsBarView.showBottomLine(false);
		topTabLayout.addView(tabsBarView.getView());
		for (int i = 0; i < tabSize; i++) {
			fragmentList.add(FragmentOneTypeCaseList.newInstance(i, typeList
					.get(i).getCode(), selectCityCode, selectProvinceCode));
		}
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		try {
			tabsBarView.SetSelecetedIndex(arg0, !changeTabByClick);
			RefreshObs.getInstance().notifyOneGetFirstPageData(
					"ZX_case_shopCategory", shopCategory);
			changeTabByClick = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
