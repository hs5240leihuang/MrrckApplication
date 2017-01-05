package com.meiku.dev.ui.findjob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.bean.JobEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 工作列表（筛选）
 * 
 */
public class JobListActivity extends BaseActivity implements OnClickListener {

	private ClearEditText et_msg_search;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<JobEntity> adapter;
	private List<JobEntity> jobListData = new ArrayList<JobEntity>();
	private TextView[] topTitleTv = new TextView[4];
	private ListView selectListview;
	private List<String> selectShowList = new ArrayList<String>();
	private LinearLayout selectLayout;
	private int selectTag;
	private final int TAG_BOSSTYPE = 0;
	private final int TAG_POSITION = 1;
	private final int TAG_SALARY = 2;
	private final int TAG_BENEFITS = 3;
	private CommonAdapter<String> showSelectAdapter;
	private List<DataconfigEntity> bossTypes_DBData, salary_DBData,
			benefit_DBData;
	private ArrayList<String> bossType_StrList, position_StrList,
			salary_StrList, benefit_StrList;
	private List<JobClassEntity> position_DBData;
	private int positionId = -1;
	private String salaryId = "-1";
	private String fringeBenefitsId = "";
	private String bossType = "-1";
	private int jobType = 1;
	private String cityName;
	private double longitude = -1;
	private double laitude = -1;
	private Drawable arrowDown, arrowUp;
	private String jobDetailUrl;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_joblist;
	}

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
	public void initView() {
		initTitle();
		initTopSelect();
		initPullListView();
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitle() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		findViewById(R.id.tv_productcatagory).setVisibility(View.GONE);
		et_msg_search.setHint("请输入岗位、公司名称搜索");
		et_msg_search.setKeyListener(null);
	}

	/**
	 * 初始化选择栏
	 */
	private void initTopSelect() {
		arrowDown = ContextCompat.getDrawable(JobListActivity.this,
				R.drawable.arrowdown);
		arrowDown.setBounds(0, 0, ScreenUtil.dip2px(this, 9),
				ScreenUtil.dip2px(this, 5));
		arrowUp = ContextCompat.getDrawable(JobListActivity.this,
				R.drawable.arrow_r_up);
		arrowUp.setBounds(0, 0, ScreenUtil.dip2px(this, 9),
				ScreenUtil.dip2px(this, 5));
		topTitleTv[0] = (TextView) findViewById(R.id.top_bossType);
		topTitleTv[0].setCompoundDrawables(null, null, arrowDown, null);
		topTitleTv[1] = (TextView) findViewById(R.id.top_position);
		topTitleTv[1].setCompoundDrawables(null, null, arrowDown, null);
		topTitleTv[2] = (TextView) findViewById(R.id.top_salary);
		topTitleTv[2].setCompoundDrawables(null, null, arrowDown, null);
		topTitleTv[3] = (TextView) findViewById(R.id.top_benefits);
		topTitleTv[3].setCompoundDrawables(null, null, arrowDown, null);
		selectLayout = (LinearLayout) findViewById(R.id.selectLayout);
		selectListview = (ListView) findViewById(R.id.messagelist);
		showSelectAdapter = new CommonAdapter<String>(JobListActivity.this,
				R.layout.item_dialogliststr, selectShowList) {

			@Override
			public void convert(ViewHolder viewHolder, String t) {
				viewHolder.setText(R.id.messagetext, t);
			}
		};
		selectListview.setAdapter(showSelectAdapter);
		selectListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (selectTag) {
				case TAG_BOSSTYPE:
					topTitleTv[0].setText(bossType_StrList.get(arg2));
					bossType = bossTypes_DBData.get(arg2).getCodeId();
					break;
				case TAG_POSITION:
					topTitleTv[1].setText(position_StrList.get(arg2));
					positionId = position_DBData.get(arg2).getId();
					break;
				case TAG_SALARY:
					topTitleTv[2].setText(salary_StrList.get(arg2));
					salaryId = salary_DBData.get(arg2).getCodeId();
					break;
				case TAG_BENEFITS:
					topTitleTv[3].setText(benefit_StrList.get(arg2));
					fringeBenefitsId = benefit_DBData.get(arg2).getCodeId();
					break;
				default:
					break;
				}
				setSelectPageVisible(false);
				downRefreshData();
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
		// 适配器
		adapter = new CommonAdapter<JobEntity>(JobListActivity.this,
				R.layout.item_findjob_jobitem, jobListData) {

			@Override
			public void convert(ViewHolder viewHolder, final JobEntity t) {
				viewHolder.setText(R.id.tv_jobName, t.getJobName());
				viewHolder.setText(R.id.tv_zp, "招聘：" + t.getNeedNum() + "  "
						+ t.getJobAgeName() + "  " + t.getEducationName());
				TextView tv_bossType = viewHolder.getView(R.id.tv_bossType);
				TextView tv_bossType2 = viewHolder.getView(R.id.tv_bossType2);
				if (Tool.isEmpty(t.getBossTypeName())) {
					tv_bossType.setVisibility(View.GONE);
					tv_bossType2.setVisibility(View.GONE);
				} else if (t.getBossTypeName().contains(",")) {
					tv_bossType.setText(t.getBossTypeName().split(",")[0]);
					tv_bossType.setVisibility(View.VISIBLE);
					tv_bossType2.setVisibility(View.VISIBLE);
					tv_bossType2.setText(t.getBossTypeName().split(",")[1]);
				} else {
					tv_bossType.setVisibility(View.VISIBLE);
					tv_bossType2.setVisibility(View.GONE);
					viewHolder.setText(R.id.tv_bossType, t.getBossTypeName());
				}
				viewHolder.setText(R.id.tv_compName, t.getCompanyName());
				viewHolder.setText(R.id.tv_money, t.getSalaryValue());
				viewHolder.setText(R.id.tv_address, t.getWorkAddress());
				viewHolder.setText(R.id.tv_time, t.getClientRefreshDate());
				viewHolder.getView(R.id.tv_top).setVisibility(
						t.getTopFlag() == 1 ? View.VISIBLE : View.INVISIBLE); // 0:普通,1:置顶
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!Tool.isEmpty(jobDetailUrl)
										&& !Tool.isEmpty(t.getId())) {
									startActivity(new Intent(
											JobListActivity.this,
											CompanyInfoActivity.class)
											.putExtra("comeUrl", jobDetailUrl)
											.putExtra("key", t.getId())
											.putExtra("type", "fromJob"));
								} else {
									ToastUtil.showShortToast("参数有误，查看详细信息失败！");
								}

							}
						});
			}

		};
		mPullRefreshListView.setAdapter(adapter);
	}

	/**
	 * 请求job数据
	 */
	private void getJobData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("positionId", positionId);
		map.put("salaryId", salaryId);
		map.put("fringeBenefitsId", fringeBenefitsId);
		map.put("bossType", bossType);
		map.put("jobType", jobType);
		map.put("longitude", longitude);
		map.put("latitude", laitude);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("cityName", cityName);
		LogUtil.d("hl", "findjob=" + map);
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_FINDJOB_JOBLIST));
		httpPost(reqCodeOne, AppConfig.RESUME_REQUEST_MAPPING, req, true);
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
		// 处理老板类型数据
		bossTypes_DBData = MKDataBase.getInstance().getBossTypes();
		DataconfigEntity dfe = new DataconfigEntity();
		dfe.setCodeId("-1");
		dfe.setValue("全部类型");
		bossTypes_DBData.add(0, dfe);
		bossType_StrList = new ArrayList<String>();
		for (int i = 0; i < bossTypes_DBData.size(); i++) {
			bossType_StrList.add(bossTypes_DBData.get(i).getValue());
		}
		// 处理职位数据
		position_DBData = MKDataBase.getInstance().getJobClassIntent();
		JobClassEntity je = new JobClassEntity();
		je.setId(-1);
		je.setName("职位不限");
		position_DBData.add(0, je);
		position_StrList = new ArrayList<String>();
		for (int i = 0; i < position_DBData.size(); i++) {
			position_StrList.add(position_DBData.get(i).getName());
		}
		// 处理薪资数据
		salary_DBData = MKDataBase.getInstance().getSalary();
		DataconfigEntity dfe_xz = new DataconfigEntity();
		dfe_xz.setCodeId("-1");
		dfe_xz.setValue("薪资不限");
		salary_DBData.add(0, dfe_xz);
		salary_StrList = new ArrayList<String>();
		for (int i = 0; i < salary_DBData.size(); i++) {
			salary_StrList.add(salary_DBData.get(i).getValue());
		}
		// 处理福利待遇数据
		benefit_DBData = MKDataBase.getInstance().getBenefits();
		DataconfigEntity dfe_bnf = new DataconfigEntity();
		dfe_bnf.setCodeId("-1");
		dfe_bnf.setValue("待遇不限");
		benefit_DBData.add(0, dfe_bnf);
		benefit_StrList = new ArrayList<String>();
		for (int i = 0; i < benefit_DBData.size(); i++) {
			benefit_StrList.add(benefit_DBData.get(i).getValue());
		}
		// 选择职位进来
		int comePositionId = getIntent().getIntExtra("positionId", -1);
		if (comePositionId != -1) {
			positionId = comePositionId;
			for (int i = 0; i < position_DBData.size(); i++) {
				if (positionId == position_DBData.get(i).getId()) {
					topTitleTv[1].setText(position_DBData.get(i).getName());
				}
			}
		} else {
			topTitleTv[1].setText(je.getName());
		}
		topTitleTv[0].setText(dfe.getValue());
		topTitleTv[2].setText(dfe_xz.getValue());
		// 选择兼职进来
		jobType = getIntent().getIntExtra("jobType", 1);
		// 选择附近进来
		boolean nearby = getIntent().getBooleanExtra("nearby", false);
		if (nearby) {
			longitude = MrrckApplication.getInstance().longitude;
			laitude = MrrckApplication.getInstance().laitude;
		} else {
			longitude = -1;
			laitude = -1;
		}
		// 选择保吃住进来
		boolean baochizhu = getIntent().getBooleanExtra("baochizhu", false);
		if (!baochizhu) {
			fringeBenefitsId = "-1";
			topTitleTv[3].setText(dfe_bnf.getValue());
		} else {
			for (int i = 0, size = benefit_DBData.size(); i < size; i++) {
				if ("包吃".equals(benefit_DBData.get(i).getValue())) {
					fringeBenefitsId += benefit_DBData.get(i).getCodeId() + ",";
				} else if ("包住".equals(benefit_DBData.get(i).getValue())) {
					fringeBenefitsId += benefit_DBData.get(i).getCodeId() + ",";
				}
			}
			if (!Tool.isEmpty(fringeBenefitsId)) {
				fringeBenefitsId = fringeBenefitsId.substring(0,
						fringeBenefitsId.length() - 1);
			} else {
				fringeBenefitsId = "180001,180002";// 取数据库失败使用
			}

			topTitleTv[3].setText("包吃住");
		}
		cityName = MrrckApplication.getInstance().cityName;
		getJobData();
	}

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		et_msg_search.setOnClickListener(this);
		findViewById(R.id.bottomEmpty).setOnClickListener(this);
		topTitleTv[0].setOnClickListener(this);
		topTitleTv[1].setOnClickListener(this);
		topTitleTv[2].setOnClickListener(this);
		topTitleTv[3].setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "findjob_result=" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("position") + "").length() > 2) {
				List<JobEntity> jobData = new ArrayList<JobEntity>();
				try {
					jobData = (List<JobEntity>) JsonUtil.jsonToList(resp
							.getBody().get("position").toString(),
							new TypeToken<List<JobEntity>>() {
							}.getType());
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				if (jobData != null && jobData.size() > 0) {
					jobListData.addAll(jobData);
				} else {
					ToastUtil.showShortToast("暂无此类信息");
				}
			} else {
				ToastUtil.showShortToast("暂无此类信息");
			}
			if (Tool.isEmpty(jobDetailUrl)) {
				jobDetailUrl = resp.getBody().get("jobDetailUrl").getAsString();
				LogUtil.d("hl", "jobDetailUrl=" + jobDetailUrl);
			}
			adapter.notifyDataSetChanged();
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
			if (mPullRefreshListView != null) {
				mPullRefreshListView.onRefreshComplete();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			finish();
			break;
		case R.id.bottomEmpty:
			setSelectPageVisible(false);
			break;
		case R.id.et_msg_search:
			startActivity(new Intent(this, JobSearchActivity.class));
			break;
		case R.id.top_bossType:
			if (selectTag == TAG_BOSSTYPE && isSelectPageShow()) {
				setSelectPageVisible(false);
				return;
			}
			setTagAndShowSelect(TAG_BOSSTYPE);
			break;
		case R.id.top_position:
			if (selectTag == TAG_POSITION && isSelectPageShow()) {
				setSelectPageVisible(false);
				return;
			}
			setTagAndShowSelect(TAG_POSITION);
			break;
		case R.id.top_salary:
			if (selectTag == TAG_SALARY && isSelectPageShow()) {
				setSelectPageVisible(false);
				return;
			}
			setTagAndShowSelect(TAG_SALARY);
			break;
		case R.id.top_benefits:
			if (selectTag == TAG_BENEFITS && isSelectPageShow()) {
				setSelectPageVisible(false);
				return;
			}
			setTagAndShowSelect(TAG_BENEFITS);
			break;
		default:
			break;
		}
	}

	/**
	 * 显示下拉选择
	 * 
	 * @param tag
	 */
	private void setTagAndShowSelect(int tag) {
		for (int i = 0; i < topTitleTv.length; i++) {
			topTitleTv[i].setTextColor(Color.parseColor("#666666"));
			topTitleTv[i].setCompoundDrawables(null, null, arrowDown, null);
		}
		selectTag = tag;
		topTitleTv[tag].setTextColor(getResources().getColor(R.color.mrrck_bg));
		topTitleTv[tag].setCompoundDrawables(null, null, arrowUp, null);
		selectShowList.clear();
		switch (tag) {
		case TAG_BOSSTYPE:
			selectShowList.addAll(bossType_StrList);
			break;
		case TAG_POSITION:
			selectShowList.addAll(position_StrList);
			break;
		case TAG_SALARY:
			selectShowList.addAll(salary_StrList);
			break;
		case TAG_BENEFITS:
			selectShowList.addAll(benefit_StrList);
			break;
		default:
			break;
		}
		setSelectPageVisible(true);
		showSelectAdapter.notifyDataSetInvalidated();
	}

	private void setSelectPageVisible(boolean showSelectPage) {
		selectLayout.setVisibility(showSelectPage ? View.VISIBLE : View.GONE);
		if (!showSelectPage) {
			topTitleTv[selectTag].setTextColor(Color.parseColor("#666666"));
			topTitleTv[selectTag].setCompoundDrawables(null, null, arrowDown,
					null);
		}
	}

	private boolean isSelectPageShow() {
		return selectLayout.getVisibility() == View.VISIBLE;
	}

}
