package com.meiku.dev.ui.plan;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PlanCaseEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.RefreshObs.NeedRefreshListener;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 赵策策划案例分类一列表Fragment
 */
public class FragmentCaseList extends BaseFragment {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<PlanCaseEntity> showAdapter;
	private List<PlanCaseEntity> showList = new ArrayList<PlanCaseEntity>();
	private int shopCategory = -1;// 店铺类型
	private int caseType;// 策划10大类型code
	private int page = 1;
	private int index;
	private LinearLayout layoutEmpty;

	public static FragmentCaseList newInstance(int index, int shopCategory,
			int caseType) {
		final FragmentCaseList f = new FragmentCaseList();
		final Bundle args = new Bundle();
		args.putInt("shopCategory", shopCategory);
		args.putInt("index", index);
		args.putInt("caseType", caseType);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.onlypulllist, null);
		shopCategory = getArguments() != null ? getArguments().getInt(
				"shopCategory") : -1;
		caseType = getArguments() != null ? getArguments().getInt("caseType")
				: -1;
		index = getArguments() != null ? getArguments().getInt("index") : 0;
		initView();
		return layout_view;
	}

	private void initView() {
		layoutEmpty = (LinearLayout) layout_view.findViewById(R.id.layoutEmpty);
		((TextView) layout_view.findViewById(R.id.tv_text)).setText("");
		ImageView imgEmpty = (ImageView) layout_view
				.findViewById(R.id.img_picture);
		imgEmpty.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil
				.dip2px(getActivity(), 155), ScreenUtil.dip2px(getActivity(),
				165)));
		imgEmpty.setBackgroundResource(R.drawable.searchempty);
		initPullListView();
		String regisTag = "PLAN_case_shopCategory_" + shopCategory;
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
		showAdapter = new CommonAdapter<PlanCaseEntity>(getActivity(),
				R.layout.item_decoration, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final PlanCaseEntity t) {
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
				if (!Tool.isEmpty(t.getPlanCaseAttachmentEntityList())) {
					showImgview.setUrlOfImage(t
							.getPlanCaseAttachmentEntityList().get(0)
							.getClientFileUrl());
				} else {
					showImgview.setUrlOfImage("");
				}

				viewHolder.setText(R.id.tv_righttop, t.getShopTypeName());
				viewHolder.setText(R.id.tv_title, t.getTitle());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				// viewHolder
				// .setText(R.id.TV_rightbottom, t.getClientUpdateDate());
				viewHolder.setText(R.id.tv_info, t.getClientUpdateDate());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(getActivity(),
										PlotDetailActivity.class)
										.putExtra("shareTitle",
												t.getShareTitle())
										.putExtra("shareContent",
												t.getShareContent())
										.putExtra("shareImg", t.getShareImg())
										.putExtra("shareUrl", t.getShareUrl())
										.putExtra("loadUrl", t.getLoadUrl())
										.putExtra("userId", t.getUserId()));
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
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		if (shopCategory != -1) {
			map.put("shopType", shopCategory);
		}
		map.put("caseType", caseType);
		req.setHeader(new ReqHead(AppConfig.PLAN_500002));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", map + "");
		httpPost(reqCodeOne, AppConfig.PLAN_REQUEST_MAPPING, req, showProgress);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				try {
					List<PlanCaseEntity> caseList = (List<PlanCaseEntity>) JsonUtil
							.jsonToList(resp.getBody().get("data").toString(),
									new TypeToken<List<PlanCaseEntity>>() {
									}.getType());
					showList.addAll(caseList);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
			}
			layoutEmpty.setVisibility(Tool.isEmpty(showList) ? View.VISIBLE
					: View.GONE);
			showAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

}
