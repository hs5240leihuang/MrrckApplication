package com.meiku.dev.ui.myshow;

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
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PostsGuideEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 赛事-2赛事介绍
 * 
 */
public class MatchIntroduceFragment extends BaseFragment {
	private PullToRefreshListView mPullRefreshListView;
	private View layout_view;
	private int page = 1;
	private int matchId;
	private List<PostsGuideEntity> showlist = new ArrayList<PostsGuideEntity>();
	private CommonAdapter<PostsGuideEntity> showAdapter;

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
		layout_view = inflater.inflate(R.layout.fragment_matchintroduce, null);
		initPullListView();
		return layout_view;
	}

	@Override
	public void initValue() {
		Bundle bundle = getArguments();
		matchId = bundle.getInt("matchId");
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("89898989", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("matchCity").toString().length() > 2) {
				List<PostsGuideEntity> listData = (List<PostsGuideEntity>) JsonUtil
						.jsonToList(resp.getBody().get("matchCity").toString(),
								new TypeToken<List<PostsGuideEntity>>() {
								}.getType());
				if (listData != null) {
					showlist.addAll(listData);
				}

			} else {
				ToastUtil.showShortToast("无更多数据");
			}
			showAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("请求赛事介绍数据失败");
			break;

		default:
			break;
		}
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
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

		showAdapter = new CommonAdapter<PostsGuideEntity>(getActivity(),
				R.layout.item_matchintroduce, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final PostsGuideEntity t) {
				viewHolder.setText(R.id.tv_title, t.getTitle());
				viewHolder.setText(R.id.tv_time, t.getApplyStartDate());
				viewHolder.setImage(R.id.img_background, t.getClientImgUrl());
				viewHolder.setText(R.id.tv_introduce, t.getContent());
				if ("0".equals(t.getSignupFlag())) {
					viewHolder.setImage(R.id.img_statue, R.drawable.weikaishi);
				} else {
					if ("1".equals(t.getSignupFlag())) {
						viewHolder.setImage(R.id.img_statue,
								R.drawable.jinxingzhong);
					} else {
						viewHolder.setImage(R.id.img_statue,
								R.drawable.yijieshu);
					}
				}

				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(getActivity(),
										ShowMainActivity.class).putExtra(
										"postsId", t.getId()));
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

	}

	protected void upRefreshData() {
		page++;
		GetData();
	}

	protected void downRefreshData() {
		showlist.clear();
		page = 1;
		GetData();
	}

	/**
	 * 获取赛事介绍
	 */
	private void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matchId", matchId);
		map.put("matchCityCode", -1);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_MATCH_INTRODUCE));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req);
		LogUtil.d("000", map + "");
	}

}
