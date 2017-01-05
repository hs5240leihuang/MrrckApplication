package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MatchResultEntity;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class PastMatchActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<MatchResultEntity> showAdapter;
	private List<MatchResultEntity> showList = new ArrayList<MatchResultEntity>();
	private int curPage = 1;
	private String matchId;
	private String matchCityId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_past_match;
	}

	@Override
	public void initView() {
		initPullListView();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
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
		showAdapter = new CommonAdapter<MatchResultEntity>(
				PastMatchActivity.this, R.layout.item_past_match, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final MatchResultEntity t) {
				viewHolder.getConvertView().setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										PastMatchActivity.this,
										ShowMainActivity.class);
								intent.putExtra("postsId", t.getPostsId());
								intent.putExtra("title", t.getResultName());
								intent.putExtra("tabNum", 3);
								startActivity(intent);
							}
						});
				viewHolder.setText(R.id.tv_name, t.getResultName());
				viewHolder.setText(R.id.tv_date, t.getCreateDate());
				viewHolder.setImage(R.id.iv_img, t.getClientThumbImgUrl());
				viewHolder.setText(R.id.tv_content, t.getResultRemark());
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
	}

	@Override
	public void initValue() {
		matchId = getIntent().getStringExtra("matchId");
		matchCityId = getIntent().getStringExtra("matchCityId");
		downRefreshData();
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d(resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("matchResult").toString())
					&& resp.getBody().get("matchResult").toString().length() > 2) {
				showList.addAll((List<MatchResultEntity>) JsonUtil.jsonToList(
						resp.getBody().get("matchResult").toString(),
						new TypeToken<List<MatchResultEntity>>() {
						}.getType()));
				showAdapter.notifyDataSetChanged();
			} else {
				ToastUtil.showShortToast("没有更多数据");
			}
			mPullRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

	// 上拉加载更多数据
	private void upRefreshData() {
		curPage++;
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matchId", matchId);
		map.put("matchCityId", matchCityId);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("page", curPage);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAST_MATCH));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		curPage = 1;
		showList.clear();
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matchId", matchId);
		map.put("matchCityId", matchCityId);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("page", curPage);
		LogUtil.d("hl", "请求版块信息_" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAST_MATCH));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}
}
