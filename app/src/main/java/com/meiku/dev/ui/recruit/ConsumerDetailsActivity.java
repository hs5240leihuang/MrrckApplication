package com.meiku.dev.ui.recruit;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.CompanyInfoCountEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumeDownloadEntity;
import com.meiku.dev.bean.ResumeEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class ConsumerDetailsActivity extends BaseActivity {
	private int page = 1;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<ResumeDownloadEntity> showAdapter;
	private List<ResumeDownloadEntity> showlist = new ArrayList<ResumeDownloadEntity>();
	private String url;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_consumerdetail;
	}

	@Override
	public void initView() {
		initPullListView();
	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("1234567", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("company").toString().length() > 2) {
				List<ResumeDownloadEntity> listData = (List<ResumeDownloadEntity>) JsonUtil
						.jsonToList(resp.getBody().get("company").toString(),
								new TypeToken<List<ResumeDownloadEntity>>() {
								}.getType());
				showlist.addAll(listData);
			}
			if (resp.getBody().get("companyResumeDetailUrl").toString()
					.length() > 2) {
				url = resp.getBody().get("companyResumeDetailUrl")
						.getAsString();
			} else {
				ToastUtil.showShortToast("无数据");
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
		// 下来刷新
		mPullRefreshListView.getLoadingLayoutProxy(true, false)
				.setLastUpdatedLabel("下拉刷新");
		mPullRefreshListView.getLoadingLayoutProxy(true, false)
				.setPullLabel("");
		mPullRefreshListView.getLoadingLayoutProxy(true, false)
				.setRefreshingLabel("正在刷新");
		mPullRefreshListView.getLoadingLayoutProxy(true, false)
				.setReleaseLabel("放开以刷新");
		// 下来加载更多
		mPullRefreshListView.getLoadingLayoutProxy(false, true)
				.setLastUpdatedLabel("上拉加载");
		mPullRefreshListView.getLoadingLayoutProxy(false, true)
				.setPullLabel("");
		mPullRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel("正在加载...");
		mPullRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel("放开以加载");
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

		showAdapter = new CommonAdapter<ResumeDownloadEntity>(
				ConsumerDetailsActivity.this, R.layout.item_consumerdetail,
				showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final ResumeDownloadEntity t) {
				viewHolder.setText(R.id.tv_name, t.getResumeRealName());
				viewHolder.setText(R.id.tv_time, t.getCreateDate());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								String webUrl;
								webUrl = url + t.getResumeId();
								Intent intent = new Intent(
										ConsumerDetailsActivity.this,
										WebViewActivity.class);
								intent.putExtra("webUrl", webUrl);
								startActivity(intent);
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

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		// map.put("companyId", 137);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_CONSUMERDETAIL));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}
}
