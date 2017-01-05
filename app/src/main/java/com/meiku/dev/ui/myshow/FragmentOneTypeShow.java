package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.RefreshObs.NeedRefreshListener;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 秀场/赛事 某分类的作品列表
 * 
 */
public class FragmentOneTypeShow extends BaseFragment {

	private View layout_view;
	private PullToRefreshGridView pull_refreshGV;
	private CommonAdapter<ShowPostsSignupEntity> showAdapter;
	private List<ShowPostsSignupEntity> showList = new ArrayList<ShowPostsSignupEntity>();
	protected int page = 1;
	protected boolean isUpRefresh;
	private int categoryId;
	private int matchId = -1;
	private boolean firstLoad;
	private int selectedCityCode = -1;
	private String regisTag;
	private int index;
	private int layoutID;
	private static int errorDefaultCode = -100;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_ontypeshow, null);
		categoryId = getArguments() != null ? getArguments().getInt(
				"categoryId") : errorDefaultCode;
		matchId = getArguments() != null ? getArguments().getInt("matchId")
				: errorDefaultCode;
		index = getArguments() != null ? getArguments().getInt("index") : 0;
		initView();
		return layout_view;
	}

	private void initView() {
		regisRefresh();
		initPullView();
	}

	private void regisRefresh() {
		if (matchId != errorDefaultCode) {// 赛事作品tag
			regisTag = "FragmentOneTypeShow_" + "matchId=" + matchId + "_"
					+ categoryId;
		} else {// 非赛事作品tag
			regisTag = "FragmentOneTypeShow_" + "normal=" + categoryId;
		}
		LogUtil.d("hl", "regisTag=" + regisTag + ", matchId = " + matchId
				+ ", categoryId=" + categoryId);
		RefreshObs.getInstance().registerListener(regisTag,
				new NeedRefreshListener() {

					@Override
					public void onPageRefresh() {
						selectedCityCode = (Integer) PreferHelper
								.getSharedParam("Match_selectedCityCode", -1);
						showList.clear();
						page = 1;
						firstLoad = true;
						getShowData(page, false);
					}

					@Override
					public void getFirstPageData(int key) {
						if (Tool.isEmpty(showList) && key == categoryId) {
							selectedCityCode = (Integer) PreferHelper
									.getSharedParam("Match_selectedCityCode",
											-1);
							page = 1;
							firstLoad = false;
							getShowData(page, true);
						}
					}
				});
	}

	private void initPullView() {
		pull_refreshGV = (PullToRefreshGridView) layout_view
				.findViewById(R.id.pull_refresh_grid);
		pull_refreshGV.setMode(PullToRefreshGridView.Mode.BOTH);
		if (matchId != errorDefaultCode) {
			pull_refreshGV.getRefreshableView().setNumColumns(1);
		}
		pull_refreshGV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						isUpRefresh = true;
						upRefreshData();
					}
				});
		if (matchId != errorDefaultCode) {
			layoutID = R.layout.item_grid_show_match;
		} else {
			layoutID = R.layout.item_grid_show;
		}
		showAdapter = new CommonAdapter<ShowPostsSignupEntity>(getActivity(),
				layoutID, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final ShowPostsSignupEntity t) {
				viewHolder.setText(R.id.TV_name, t.getName());
				viewHolder.setText(R.id.TV_job, t.getNickName());

				TextView TV_area = viewHolder.getView(R.id.TV_area);
				if (matchId != errorDefaultCode) {
					viewHolder.setText(R.id.TV_rightbottom,
							t.getClientCreateDate());
					viewHolder.setText(R.id.TV_leftbottom, "作品编号："+t.getSignupNo());
					TV_area.setText(t.getSignupArea());
				} else {
					viewHolder.setText(R.id.TV_leftbottom,
							t.getClientCreateDate());
					viewHolder.setText(R.id.TV_rightbottom, "");
					TV_area.setText("");
				}
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				MyRoundDraweeView IV_head = new MyRoundDraweeView(getActivity());
				lin_head.removeAllViews();
				lin_head.addView(IV_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				IV_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						getActivity());
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientPhotoFileUrl());
				View view_righttop = viewHolder.getView(R.id.IV_righttop);
				final boolean isCanSaiWork = t.getWorksFlag() == 1;// 0:普通作品,1:比赛作品
				if (isCanSaiWork) {
					viewHolder.setImage(R.id.IV_piao, R.drawable.piao);
					TextView TV_piaoNum = viewHolder.getView(R.id.TV_piaoNum);
					TV_piaoNum.setText(t.getTotalVoteNum() + "");
					TV_piaoNum.setTextColor(Color.parseColor("#FF5073"));
				} else {
					viewHolder.setImage(R.id.IV_piao, R.drawable.collect_off);
					TextView TV_piaoNum = viewHolder.getView(R.id.TV_piaoNum);
					TV_piaoNum.setText(t.getLikeNum() + "");
					TV_piaoNum.setTextColor(Color.parseColor("#999999"));
				}
				if (t.getHotFlag() == 1) {// 0:不是 1:是
					view_righttop.setBackgroundResource(R.drawable.icon_hot);
					view_righttop.setVisibility(View.VISIBLE);
				} else if (isCanSaiWork) {
					view_righttop
							.setBackgroundResource(R.drawable.cansaizuopin);
					view_righttop.setVisibility(View.VISIBLE);
				} else {
					view_righttop.setBackgroundDrawable(null);
					view_righttop.setVisibility(View.GONE);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (isCanSaiWork) {// 是否参赛作品
									startActivity(new Intent(getActivity(),
											NewWorkDetailActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								} else {
									startActivity(new Intent(getActivity(),
											WorkDetailNewActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								}
							}
						});
			}

		};
		pull_refreshGV.setAdapter(showAdapter);
	}

	protected void upRefreshData() {
		page++;
		getShowData(page, true);
	}

	protected void downRefreshData() {
		showList.clear();
		page = 1;
		getShowData(page, true);
	}

	/**
	 * 获取秀场数据
	 * 
	 * @param page
	 */
	private void getShowData(int page, boolean showProgress) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("name", "");
		map.put("categoryId", categoryId);
		map.put("pageNum", ConstantKey.PageNum);
		if (matchId != errorDefaultCode) {
			map.put("matchId", matchId);
			map.put("cityCode", selectedCityCode);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_WORKS));
		} else {
			req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_SHOW));
		}
		LogUtil.d("hl", map + "");
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, !firstLoad
				&& showProgress);
	}

	@Override
	public void initValue() {
		if (index == 0) {// 首次只加载第一个类型
			downRefreshData();
		}
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "1111111" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("postsSignup") + "").length() > 2) {
				List<ShowPostsSignupEntity> listData = (List<ShowPostsSignupEntity>) JsonUtil
						.jsonToList(resp.getBody().get("postsSignup")
								.toString(),
								new TypeToken<List<ShowPostsSignupEntity>>() {
								}.getType());
				if (!Tool.isEmpty(listData)) {
					showList.addAll(listData);
				} else {
					if (!firstLoad) {
						ToastUtil.showShortToast("无更多数据");
					}
				}
			} else {
				if (!firstLoad) {
					ToastUtil.showShortToast("无更多数据");
				}
			}
			showAdapter.notifyDataSetChanged();
			pull_refreshGV.onRefreshComplete();
			isUpRefresh = false;
			firstLoad = false;
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != pull_refreshGV) {
			pull_refreshGV.onRefreshComplete();
		}
		isUpRefresh = false;
	}

	public static FragmentOneTypeShow newInstance(int index, int categoryId) {
		final FragmentOneTypeShow f = new FragmentOneTypeShow();
		final Bundle args = new Bundle();
		args.putInt("categoryId", categoryId);
		args.putInt("matchId", errorDefaultCode);
		args.putInt("index", index);
		f.setArguments(args);
		return f;
	}

	public static FragmentOneTypeShow newInstance(int index, int categoryId,
			int matchId) {
		final FragmentOneTypeShow f = new FragmentOneTypeShow();
		final Bundle args = new Bundle();
		args.putInt("categoryId", categoryId);
		args.putInt("matchId", matchId);
		args.putInt("index", index);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onDestroy() {
		RefreshObs.getInstance().UnRegisterListener(regisTag);
		super.onDestroy();
	}

}
