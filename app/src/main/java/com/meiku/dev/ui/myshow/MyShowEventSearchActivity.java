package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class MyShowEventSearchActivity extends BaseActivity {
	private EditText et_msg_search;
	private PullToRefreshListView mPullRefreshListView;;// 下拉刷新
	private List<PostsEntity> showList = new ArrayList<PostsEntity>();
	private CommonAdapter<PostsEntity> commonAdapter;
	private TextView cancel;
	protected String searchContent = "";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myshow_eventsearch;
	}

	@Override
	public void initView() {
		initSearch();
		initPullListView();
	}

	private void initSearch() {
		et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		et_msg_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				searchContent = et_msg_search.getText().toString();
				if (!Tool.isEmpty(searchContent)) {
					downRefreshData();
				} else {
					showList.clear();
					commonAdapter.notifyDataSetChanged();
				}
			}
		});
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setTextSize(17);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(et_msg_search);
				finish();
			}
		});
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk", "##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = resp.getBody().get("postsList").toString();
			try {
				List<PostsEntity> data = (List<PostsEntity>) JsonUtil
						.jsonToList(jsonstr,
								new TypeToken<List<PostsEntity>>() {
								}.getType());
				if (!Tool.isEmpty(data)) {
					showList.addAll(data);
					commonAdapter.notifyDataSetChanged();
				} else {
					ToastUtil.showShortToast("没有更多数据");
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
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
			ToastUtil.showShortToast("请求失败");
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
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MyShowEventSearchActivity.this,
						ShowMainActivity.class);
				intent.putExtra("postsId", showList.get(arg2 - 1).getPostsId());
				startActivity(intent);

			}
		});
		commonAdapter = new CommonAdapter<PostsEntity>(
				MyShowEventSearchActivity.this, R.layout.item_myshow_event,
				showList) {

			@Override
			public void convert(ViewHolder viewHolder, PostsEntity t) {
				viewHolder.setText(R.id.tv_Propaganda_language, t.getTitle());
				viewHolder.setImage(R.id.img_page, t.getClientImgUrl());
				if (t.getSignupFlag().equals("0")) {
					viewHolder.setImage(R.id.img_flag, R.drawable.weikaishi);
				} else if (t.getSignupFlag().equals("1")) {
					viewHolder.setImage(R.id.img_flag, R.drawable.jinxingzhong);
				} else {
					viewHolder.setImage(R.id.img_flag, R.drawable.yijieshu);
				}
				viewHolder.setText(R.id.tv_time,
						t.getStartDate() + "-" + t.getEndDate());
				viewHolder.setText(R.id.tv_city, t.getActiveCityName());
			}
		};
		mPullRefreshListView.setAdapter(commonAdapter);
	}

	protected void upRefreshData() {
		page++;
		getDataSearch(page);
	}

	protected void downRefreshData() {
		page = 1;
		showList.clear();
		getDataSearch(page);
	}

	// 赛事搜索请求
	public void getDataSearch(int searchpage) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("page", searchpage);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("title", searchContent);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_SOUSUO_EVENT_SHOW));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, reqBase);
	}

}
