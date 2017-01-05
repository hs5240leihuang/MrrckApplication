package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.MatchCityEntity;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.meiku.dev.views.ShowTitleTabsLayout2;
import com.meiku.dev.views.ShowTitleTabsLayout2.TabClickListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 赛事-1参赛作品
 * 
 */
public class MatchWorksFragment extends BaseFragment implements
		OnPageChangeListener {

	private View layout_view;
	private FragmentManager fragmentManager;
	private List<MkPostsActiveCategory> titletabList = new ArrayList<MkPostsActiveCategory>();
	private ClearEditText etSearch;
	private ShowTitleTabsLayout2 tabsBarView;
	protected Integer categoryId;
	protected Integer fileType;
	protected String categoryName;
	private int tabSize;
	/** 所有Fragment页面 */
	private List<BaseFragment> fragmentList_match = new ArrayList<BaseFragment>();
	private int matchId;
	private LinearLayout layout_saiqu;
	private TextView tv_matchArea;
	private ArrayList<PopupData> popList;
	protected MyPopupwindow myPopupwindow;
	private ImageView iv_arrow;
	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private List<MatchCityEntity> matchCityListData;
	protected boolean changeTabByClick;
	private LinearLayout topTabLayout;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_matchworks, null);
		fragmentManager = getChildFragmentManager();
		Bundle bundle = getArguments();
		matchId = bundle.getInt("matchId");
		LogUtil.d("hl", "matchId=" + matchId);
		initView();
		return layout_view;
	}

	private void initView() {
		initTabs();
		initSaiqu();
		initSearch();
		initViewPager();
	}

	private void initViewPager() {
		viewpager = (ViewPager) layout_view
				.findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getChildFragmentManager(),
				fragmentList_match);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(tabSize);
		viewpager.setCurrentItem(0);
	}

	private void initSaiqu() {
		layout_saiqu = (LinearLayout) layout_view
				.findViewById(R.id.layout_saiqu);
		tv_matchArea = (TextView) layout_view.findViewById(R.id.tv_matchArea);
		iv_arrow = (ImageView) layout_view.findViewById(R.id.iv_arrow);
		popList = new ArrayList<PopupData>();
		popList.add(new PopupData("全部赛区", 0));
		String matchCityJson = (String) PreferHelper.getSharedParam(
				"matchCity", "");
		if (!Tool.isEmpty(matchCityJson)) {
			matchCityListData = (List<MatchCityEntity>) JsonUtil.jsonToList(
					matchCityJson, new TypeToken<List<MatchCityEntity>>() {
					}.getType());
			if (!Tool.isEmpty(matchCityListData)) {
				for (int i = 0, size = matchCityListData.size(); i < size; i++) {
					popList.add(new PopupData(matchCityListData.get(i)
							.getMatchCityName(), 0));
				}
			}
		}
		layout_saiqu.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				myPopupwindow = new MyPopupwindow(getActivity(), popList,
						new popListener() {

							@Override
							public void doChoose(int position) {
								int selectedCityCode = -1;
								if (position > 0) {
									tv_matchArea.setText(matchCityListData.get(
											position - 1).getMatchCityName());
									selectedCityCode = matchCityListData.get(
											position - 1).getMatchCityCode();
								} else {
									tv_matchArea.setText("全部赛区");
								}
								PreferHelper.setSharedParam(
										"Match_selectedCityCode",
										selectedCityCode);
								RefreshObs.getInstance().notifyAllLisWithTag(
										"matchId");
							}
						});
				myPopupwindow.showAsDropDown(arg0);
				iv_arrow.setRotation(90);
				myPopupwindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						iv_arrow.setRotation(0);
					}
				});
			}
		});
	}

	private void initSearch() {
		etSearch = (ClearEditText) layout_view.findViewById(R.id.et_msg_search);
		etSearch.setKeyListener(null);
		etSearch.setHint("作品名、姓名、作品编号");
		etSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(),
						AllShowSearchActivity.class).putExtra("matchId",
						matchId));
			}
		});
	}

	private void initTabs() {
		titletabList.addAll(MKDataBase.getInstance().getMatchCategoryTabs());
		topTabLayout = (LinearLayout) layout_view
				.findViewById(R.id.topTabLayout);
		topTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						topTabLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						int tabBarWidth = topTabLayout.getMeasuredWidth();
						int tabSize = titletabList.size();
						int cellSize;
						if (tabSize == 0 || tabSize > 6) {
							cellSize = 6;
						} else {
							cellSize = tabSize;
						}
						tabsBarView = new ShowTitleTabsLayout2(getActivity(),
								tabBarWidth / cellSize, titletabList,
								new TabClickListener() {

									@Override
									public void onTabClick(int index) {
										changeTabByClick = true;
										categoryId = titletabList.get(index)
												.getId();
										fileType = titletabList.get(index)
												.getFileType();
										categoryName = titletabList.get(index)
												.getName();
										viewpager.setCurrentItem(index);
									}
								});
						topTabLayout.addView(tabsBarView.getView());
					}
				});

		tabSize = titletabList.size();
		for (int i = 0; i < tabSize; i++) {
			fragmentList_match.add(FragmentOneTypeShow.newInstance(i,
					titletabList.get(i).getId(), matchId));
		}
	}

	@Override
	public void initValue() {
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
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		if (tabsBarView != null) {
			tabsBarView.SetSelecetedIndex(arg0, true);
		}
		if (arg0 < titletabList.size()) {
			RefreshObs.getInstance().notifyOneGetFirstPageData("matchId",
					titletabList.get(arg0).getId());
		}
		changeTabByClick = false;
	}
}
