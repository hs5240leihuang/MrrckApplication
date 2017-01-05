package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCaseEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.RefreshObs.NeedRefreshListener;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 某一类型的装修案例列表
 * 
 */
public class FragmentOneTypeCaseList extends BaseFragment {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateCaseEntity> showAdapter;
	private List<DecorateCaseEntity> showList = new ArrayList<DecorateCaseEntity>();
	private int shopCategory = -1;
	private int page = 1;
	private int index;
	private int selectCityCode, selectProvinceCode;

	public static FragmentOneTypeCaseList newInstance(int index,
			int shopCategory, int selectCityCode, int selectProvinceCode) {
		final FragmentOneTypeCaseList f = new FragmentOneTypeCaseList();
		final Bundle args = new Bundle();
		args.putInt("shopCategory", shopCategory);
		args.putInt("index", index);
		args.putInt("selectCityCode", selectCityCode);
		args.putInt("selectProvinceCode", selectProvinceCode);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.onlypulllist, null);
		shopCategory = getArguments() != null ? getArguments().getInt(
				"shopCategory") : -1;
		index = getArguments() != null ? getArguments().getInt("index") : 0;
		selectCityCode = getArguments() != null ? getArguments().getInt(
				"selectCityCode") : -1;
		selectProvinceCode = getArguments() != null ? getArguments().getInt(
				"selectProvinceCode") : -1;
		initView();
		return layout_view;
	}

	private void initView() {
		initPullListView();
		String regisTag = "ZX_case_shopCategory_" + shopCategory;
		RefreshObs.getInstance().registerListener(regisTag,
				new NeedRefreshListener() {

					@Override
					public void onPageRefresh() {
						page = 1;
						showList.clear();
						getData(page, false);
					}

					@Override
					public void getFirstPageData(int key) {
						if (Tool.isEmpty(showList) && key == shopCategory) {
							page = 1;
							getData(page, true);
						}
					}
				});
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) layout_view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						upRefreshData();
					}
				});

		// 适配器
		showAdapter = new CommonAdapter<DecorateCaseEntity>(getActivity(),
				R.layout.item_decoration, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCaseEntity t) {
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				MyRoundDraweeView IV_head = new MyRoundDraweeView(getActivity());
				lin_head.removeAllViews();
				lin_head.addView(IV_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				IV_head.setUrlOfImage(t.getClientHeadPicUrl());

				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						getActivity());
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientCaseImgFileUrlThumb());

				viewHolder.setText(R.id.tv_righttop, t.getShopCategoryName());
				viewHolder.setText(R.id.tv_title, t.getCaseName());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder
						.setText(R.id.TV_rightbottom, t.getClientUpdateDate());
				viewHolder.setText(R.id.tv_info, t.getShowAreaSizeAndPice());

				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(getActivity(),
										CaseDetailActivity.class)
										.putExtra("shareTitle",
												t.getShareTitle())
										.putExtra("shareContent",
												t.getShareContent())
										.putExtra("shareImg", t.getShareImg())
										.putExtra("shareUrl", t.getShareUrl())
										.putExtra("userId", t.getUserId())
										.putExtra("sourceId", t.getId())
										.putExtra("loadUrl", t.getLoadUrl()));
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
	}

	// 上拉加载更多数据
	private void upRefreshData() {
		page++;
		getData(page, true);
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		page = 1;
		showList.clear();
		getData(page, true);
	}

	@Override
	public void initValue() {
		if (index == 0) {// 首次只加载第一个类型
			downRefreshData();
		}
	}

	protected void getData(int page, boolean showProgress) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopCategory", shopCategory);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_ZXANLI));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", shopCategory + "=shopCategory==>" + resp.getBody());
		if (!Tool.isEmpty(resp.getBody().get("data"))
				|| resp.getBody().get("data").toString().length() > 2) {
			try {
				List<DecorateCaseEntity> caseList = (List<DecorateCaseEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateCaseEntity>>() {
								}.getType());
				if (Tool.isEmpty(caseList)) {
					ToastUtil.showShortToast("没有更多数据");
				} else {
					showList.addAll(caseList);
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
		} else {
			ToastUtil.showShortToast("没有更多数据");
		}
		showAdapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

}
