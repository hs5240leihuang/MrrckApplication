package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.ShowTitleTabsLayout2;
import com.meiku.dev.views.ShowTitleTabsLayout2.TabClickListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 秀场
 * 
 */
public class AllShowFragment extends BaseFragment implements
		OnPageChangeListener {

	private View layout_view;
	/** 需要显示的tabsBar数据 */
	private List<MkPostsActiveCategory> titletabList = new ArrayList<MkPostsActiveCategory>();
	/** tabsBar */
	private ShowTitleTabsLayout2 tabsBarView;
	/** 赛事按钮 */
	private Button btn_saishi;
	/** 分类ID */
	private int categoryId = -1;
	/** 作品类型，0:图片， 1:视频 */
	protected int fileType;
	/** 发布秀场作品按钮 */
	private TextView right_txt_title;
	/** 分类名称 */
	private String categoryName;
	/** 输入框 */
	private ClearEditText etSearch;
	/** 所有Fragment页面 */
	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	/** tabs数量 */
	private int tabSize;
	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	protected boolean changeTabByClick;
	/** 放tabBar的布局 */
	private LinearLayout topTabLayout;
	private LinearLayout left_res_title;

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
		layout_view = inflater.inflate(R.layout.fragment_allshow, null);
		View titleEmptyTop = (View) layout_view.findViewById(R.id.statuslayout);
		int statusBarHeight = ScreenUtil
				.getStatusBarHeight(getActivity());
		titleEmptyTop.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, statusBarHeight));
		initView();
		return layout_view;
	}

	private void initView() {
		initTitle();
		initSearch();
		initTabs();
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
	}

	private void initSearch() {
		etSearch = (ClearEditText) layout_view.findViewById(R.id.et_msg_search);
		etSearch.setKeyListener(null);
		etSearch.setHint("作品名、姓名、作品编号");
		etSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(),
						AllShowSearchActivity.class).putExtra("categoryId", -1));// 默认从全部中搜索
			}
		});
	}

	private void initTitle() {
		left_res_title=(LinearLayout) layout_view.findViewById(R.id.goback);
		left_res_title.setVisibility(View.GONE);
		right_txt_title = (TextView) layout_view
				.findViewById(R.id.right_txt_title);
		right_txt_title.setTextColor(Color.parseColor("#000000"));
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
					return;
				}
				Intent intent = new Intent(getActivity(),
						PublishPhotoWorksActivity.class);
				intent.putExtra("categoryName", categoryName);
				intent.putExtra("categoryId", categoryId + "");
				intent.putExtra("fileType", fileType + "");
				startActivity(intent);
			}
		});
	}

	private void initTabs() {
		titletabList.addAll(MKDataBase.getInstance().getShowCategoryTabs());
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
		btn_saishi = (Button) layout_view.findViewById(R.id.btn_saishi);
		btn_saishi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getMatchId();
			}
		});
		tabSize = titletabList.size();
		for (int i = 0; i < tabSize; i++) {
			fragmentList.add(FragmentOneTypeShow.newInstance(i, titletabList
					.get(i).getId()));
		}
	}

	/**
	 * 获取当前赛事的ID
	 */
	protected void getMatchId() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityCode", MrrckApplication.getInstance().cityCode);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_ID));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public void initValue() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		if (!Tool.isEmpty(resp.getBody())
				&& resp.getBody().toString().length() > 2
				&& (resp.getBody().get("match") + "").length() > 2) {
			Map<String, String> map = JsonUtil.jsonToMap(resp.getBody().get(
					"match")
					+ "");
			String matchId = map.get("id");
			if (!Tool.isEmpty(matchId)) {
				LogUtil.d("hl", "matchId=" + matchId + map.get("matchName"));
				startActivity(new Intent(getActivity(), NewMatchActivity.class)
						.putExtra("matchId", Integer.parseInt(matchId)));
			}
			if ((resp.getBody().get("matchCity") + "").length() > 2) {
				PreferHelper.setSharedParam("matchCity",
						resp.getBody().get("matchCity").toString());
			}
		} else {
			ToastUtil.showShortToast("当前无赛事活动");
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("获取当前赛事失败！");
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
			RefreshObs.getInstance().notifyOneGetFirstPageData("normal",
					titletabList.get(arg0).getId());
			changeTabByClick = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
