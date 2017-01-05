package com.meiku.dev.ui.plan;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.MkDataConfigPlan;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.views.ShowTitleTabsLayout2;
import com.meiku.dev.views.ShowTitleTabsLayout2.TabClickListener;

/**
 * 找策划某一类列表1（带店家分类）
 */
public class PlanOneActivity extends BaseFragmentActivity implements
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
	private List<MkDataConfigPlan> typeList;
	private int planType;// 页面使用类型 0案例类型，1帖子类型
	private int caseType;// 策划10大类型code

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_deccase;
	}

	@Override
	public void initView() {
		planType = getIntent().getIntExtra("planType", 0);
		caseType = getIntent().getIntExtra("caseType", -1);
		TextView center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		center_txt_title.setText(getIntent().getStringExtra("title"));
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
		typeList = MKDataBase.getInstance().getPlanShopList(1);
		for (int i = 0, size = typeList.size(); i < size; i++) {
			MkPostsActiveCategory cate = new MkPostsActiveCategory();
			cate.setName(typeList.get(i).getFunctionName());
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
		tabsBarView = new ShowTitleTabsLayout2(PlanOneActivity.this,
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
			if (planType == 0) {
				fragmentList.add(FragmentCaseList.newInstance(i, typeList
						.get(i).getCode(), caseType));
			} else {
				findViewById(R.id.topline).setVisibility(View.VISIBLE);
				fragmentList.add(FragmentPostList.newInstance(i, typeList
						.get(i).getCode(), caseType));
			}
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
			if (planType == 0) {
				RefreshObs.getInstance().notifyOneGetFirstPageData(
						"PLAN_case_shopCategory", shopCategory);
			} else {
				RefreshObs.getInstance().notifyOneGetFirstPageData(
						"PLAN_post_shopCategory", shopCategory);
			}
			changeTabByClick = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
