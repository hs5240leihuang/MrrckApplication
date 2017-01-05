package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 秀场搜索结果页面
 * 
 */
public class AllShowSearchResultActivity extends BaseActivity {

	private ClearEditText etSearch;
	private PullToRefreshListView mPullRefreshListView;
	private List<ShowPostsSignupEntity> showlist = new ArrayList<ShowPostsSignupEntity>();
	private CommonAdapter<ShowPostsSignupEntity> showAdapter;
	private String searchName;
	private TextView cancel;
	private int matchId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_allshowsearchresult;
	}

	@Override
	public void initView() {
		matchId = getIntent().getIntExtra("matchId", -1);// matchId为-1则为秀场作品搜索，否则是赛事作品搜素
		initSearch();
		initPullListView();
	}

	/**
	 * 初始化搜索框
	 */
	private void initSearch() {
		etSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		etSearch.setEnabled(false);
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setTextSize(17);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(etSearch);
				finish();
			}
		});
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

		showAdapter = new CommonAdapter<ShowPostsSignupEntity>(
				AllShowSearchResultActivity.this,
				R.layout.item_show_searchresult, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final ShowPostsSignupEntity t) {
				// viewHolder.setImageWithNewSize(R.id.img_head,
				// t.getClientThumbHeadPicUrl(), 150, 150);
				LinearLayout layout_head = viewHolder.getView(R.id.layout_head);
				MyRoundDraweeView img_head = new MyRoundDraweeView(AllShowSearchResultActivity.this);
				layout_head.removeAllViews();
				layout_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder.setText(R.id.tv_position, t.getPositionName());
				viewHolder.setText(R.id.tv_time, t.getCreateDate());
				viewHolder.setText(R.id.tv_looknum, t.getViewNum().toString());
				viewHolder.setImage(R.id.iv_workImg, t.getClientPhotoFileUrl());
				viewHolder.setText(R.id.tv_workName, t.getName());
				viewHolder.setText(R.id.tv_intro, t.getRemark());
				final boolean isCanSaiWork = t.getWorksFlag() == 1;// 0:普通作品,1:比赛作品
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (isCanSaiWork) {// 是否参赛作品
									startActivity(new Intent(
											AllShowSearchResultActivity.this,
											NewWorkDetailActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								} else {
									startActivity(new Intent(
											AllShowSearchResultActivity.this,
											WorkDetailNewActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								}
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
	}

	protected void upRefreshData() {
		page++;
		if (matchId != -1) {
			getMatchWorksData();
		} else {
			getdata();
		}
	}

	protected void downRefreshData() {
		showlist.clear();
		page = 1;
		if (matchId != -1) {
			getMatchWorksData();
		} else {
			getdata();
		}
	}

	/**
	 * 获取匹配的秀场作品列表
	 */
	private void getdata() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchName", searchName);
		map.put("page", page);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_GET_MATHCHWORKS));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	/**
	 * 获取匹配的赛事作品列表
	 */
	private void getMatchWorksData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchName", searchName);
		map.put("page", page);
		map.put("matchId", matchId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_WORKSSEARCH));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	/**
	 * 添加搜素词条
	 */
	private void insertSearchWords() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchName", searchName);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_INSERT_CITIAO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public void initValue() {
		searchName = getIntent().getStringExtra("searchName");
		etSearch.setHint(searchName);
		if (matchId != -1) {
			getMatchWorksData();
		} else {
			getdata();
		}
		insertSearchWords();
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "_" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!("\"\"").equals(resp.getBody().get("postsSignup").toString())) {
				List<ShowPostsSignupEntity> listData = (List<ShowPostsSignupEntity>) JsonUtil
						.jsonToList(resp.getBody().get("postsSignup")
								.toString(),
								new TypeToken<List<ShowPostsSignupEntity>>() {
								}.getType());
				if (!Tool.isEmpty(listData)) {
					showlist.addAll(listData);
				}

			} else {
				ToastUtil.showShortToast("无更多数据");
			}
			showAdapter.notifyDataSetChanged();
			break;
		}
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

}
