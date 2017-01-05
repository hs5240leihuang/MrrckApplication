package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.meiku.dev.bean.JobEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 职位列表
 * 
 */
public class PublicPositionFragment extends BaseFragment implements
		OnClickListener {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<JobEntity> showAdapter;
	private List<JobEntity> showlist = new ArrayList<JobEntity>();
	private int page = 1;
	private Integer topFlag;
	private String companyJobDetailUrl;
	private boolean firstLoad = true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_public_position, null);
		initPullListView();
		return layout_view;

	}

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
	public void initValue() {
		layout_view.findViewById(R.id.btn_public_position).setOnClickListener(
				this);
		layout_view.findViewById(R.id.topTip).setOnClickListener(this);
		regisBroadcast();
		downRefreshData();
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

		showAdapter = new CommonAdapter<JobEntity>(getActivity(),
				R.layout.item_fragment_public_position, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final JobEntity t) {
				viewHolder.setText(R.id.tv_findposition, t.getJobName());
				viewHolder.setText(R.id.tv_age_demand,
						"年龄要求：" + t.getAgeValue());
				viewHolder.setText(R.id.tv_position_decribe,
						"职位描述:" + t.getJobIntroduce());
				viewHolder.setText(R.id.tv_address, t.getWorkAddress());
				viewHolder.setText(R.id.tv_salary, "￥" + t.getSalaryValue());
				if ("2".equals(t.getDelStatus())) {
					viewHolder.setText(R.id.tv_ispublic, "未发布");
				} else if ("0".equals(t.getDelStatus())) {
					viewHolder.setText(R.id.tv_ispublic, "已发布");
				}

				viewHolder.getView(R.id.lin_top).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (t.getTopFlag() == 1) {
									ToastUtil.showShortToast("已经置顶");
								} else {
									zhidingGetData(t.getId());
								}

							}
						});
				viewHolder.getView(R.id.lin_update).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								shuanxinGetData(t.getId());
							}
						});
				viewHolder.getView(R.id.lin_delete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										getActivity(), "提示", "确定要删除此职位吗?",
										"确定", "取消");
								commonDialog.show();
								commonDialog
										.setClicklistener(new CommonDialog.ClickListenerInterface() {
											@Override
											public void doConfirm() {
												shanchuGetData(t.getId());
												commonDialog.dismiss();
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});

							}
						});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!Tool.isEmpty(companyJobDetailUrl)
										&& !Tool.isEmpty(t.getId())
										&& !Tool.isEmpty(t.getCompanyId())) {
									startActivity(new Intent(getActivity(),
											WebViewActivity.class).putExtra(
											"webUrl",
											companyJobDetailUrl + t.getId()
													+ "&companyId="
													+ t.getCompanyId() + ""));
								} else {
									ToastUtil.showShortToast("参数有误，查看职位详情失败！");
								}

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

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("kkk", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("job").toString().length() > 2) {
				List<JobEntity> listData = (List<JobEntity>) JsonUtil
						.jsonToList(resp.getBody().get("job").toString(),
								new TypeToken<List<JobEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
				if (!firstLoad) {
					ToastUtil.showShortToast("无更多职位");
				}
			}
			showAdapter.notifyDataSetChanged();
			if (Tool.isEmpty(companyJobDetailUrl)
					&& (resp.getBody().get("companyJobDetailUrl") + "")
							.length() > 2) {
				companyJobDetailUrl = resp.getBody().get("companyJobDetailUrl")
						.getAsString();
				LogUtil.d("hl", "companyJobDetailUrl=" + companyJobDetailUrl);
			}
			firstLoad = false;
			break;
		case reqCodeTwo:
			topFlag = 1;
			downRefreshData();
			ToastUtil.showShortToast("置顶成功");
			break;
		case reqCodeThree:
			downRefreshData();
			ToastUtil.showShortToast("删除成功");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("职位刷新时间成功");
			break;
		default:
			break;
		}
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

		switch (requestCode) {
		case reqCodeOne:
			if (null != mPullRefreshListView) {
				mPullRefreshListView.onRefreshComplete();
			}
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("置顶失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("删除失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("职位刷新时间失败");
			break;
		default:
			break;
		}

	}

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SELECT_PUBLIC_POSITION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req, false);
	}

	// 刷新请求
	public void shuanxinGetData(int jobId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("jobId", jobId);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_REFRESH_JOB));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeFour, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

	// 置顶请求
	public void zhidingGetData(int jobId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("jobId", jobId);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_POSITION_TOP));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

	// 删除请求
	public void shanchuGetData(int jobId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("jobId", jobId);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_DELETE_JOB));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_public_position:
			startActivity(new Intent(getActivity(), PublishJobActivity.class));
			break;
		case R.id.topTip:
			getActivity().sendBroadcast(
					new Intent(BroadCastAction.ACTION_RECRUIT_CHANGETAB));
			break;
		default:
			break;
		}
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_PUBLIC_POSITION);
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_PUBLIC_POSITION.equals(intent
					.getAction())) {
				downRefreshData();
			}
			if (BroadCastAction.ACTION_IM_REFRESH_COMPANY.equals(intent
					.getAction())) {
				downRefreshData();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

}
