package com.meiku.dev.ui.findjob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.JobSearch;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 职位搜索Activity
 */
public class JobSearchActivity extends BaseActivity {

	private ClearEditText etSearch;
	private TextView cancel;
	private PullToRefreshScrollView mPullToRefreshListView;
	private CommonAdapter<JobSearch> adapter;
	private List<JobSearch> jobListData = new ArrayList<JobSearch>();
	protected String name = "";
	private String companyDetailUrl;
	private String jobDetailUrl;
	private MyListView matchListView;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_jobsearch;
	}

	@Override
	public void initView() {
		etSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		etSearch.setHint("请输入岗位、公司名称搜索");
		etSearch.addTextChangedListener(new TextWatcher() {
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
				name = etSearch.getText().toString().trim();
				if (Tool.isEmpty(name)) {
					page = 1;
					jobListData.clear();
					adapter.notifyDataSetChanged();
				} else {
					downRefreshData();
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
				InputTools.HideKeyboard(etSearch);
				finish();
			}
		});
		initPullListView();
	}

	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullToRefreshListView.getRefreshableView().setOnTouchListener(
				new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						InputTools.HideKeyboard(etSearch);// 触摸隐藏键盘
						return false;
					}
				});
		mPullToRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						upRefreshData();
					}
				});
		adapter = new CommonAdapter<JobSearch>(JobSearchActivity.this,
				R.layout.item_searchmatch, jobListData) {

			@Override
			public void convert(ViewHolder viewHolder, final JobSearch t) {
				viewHolder.setText(R.id.txt, t.getName());
			}
		};
		matchListView = (MyListView) findViewById(R.id.searchListView);
		matchListView.setAdapter(adapter);
		matchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(JobSearchActivity.this,
						CompanyInfoActivity.class)
						.putExtra(
								"comeUrl",
								jobListData.get(arg2).getType().equals("2") ? jobDetailUrl
										: companyDetailUrl)
						.putExtra("key", jobListData.get(arg2).getId())
						.putExtra(
								"type",
								jobListData.get(arg2).getType().equals("2") ? "fromJob"
										: "fromCompany"));
			}
		});

	}

	protected void upRefreshData() {
		page++;
		getJobData();
	}

	protected void downRefreshData() {
		page = 1;
		jobListData.clear();
		getJobData();
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
		LogUtil.d("hl", "searchjob_result=" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("data") + "").length() > 2) {
				List<JobSearch> jobData = new ArrayList<JobSearch>();
				try {
					jobData = (List<JobSearch>) JsonUtil.jsonToList(resp
							.getBody().get("data").toString(),
							new TypeToken<List<JobSearch>>() {
							}.getType());
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				if (jobData != null && jobData.size() > 0) {
					jobListData.addAll(jobData);
					if (Tool.isEmpty(companyDetailUrl)
							&& (resp.getBody().get("companyDetailUrl") + "")
									.length() > 2) {
						companyDetailUrl = resp.getBody()
								.get("companyDetailUrl").getAsString();
						LogUtil.d("hl", "companyDetailUrl=" + companyDetailUrl);
					}
					if (Tool.isEmpty(jobDetailUrl)
							&& (resp.getBody().get("jobDetailUrl") + "")
									.length() > 2) {
						jobDetailUrl = resp.getBody().get("jobDetailUrl")
								.getAsString();
						LogUtil.d("hl", "jobDetailUrl=" + jobDetailUrl);
					}
				}
			}
			adapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	/**
	 * 请求job数据
	 */
	private void getJobData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("name", name);
		LogUtil.d("hl", "searchjob=" + map);
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_FINDJOB_SEARCHJOB));
		httpPost(reqCodeOne, AppConfig.RESUME_REQUEST_MAPPING, req, false);
	}

}
