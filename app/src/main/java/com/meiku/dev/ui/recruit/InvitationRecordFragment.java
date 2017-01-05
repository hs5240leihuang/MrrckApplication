package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.JobInterviewEntity;
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

/**
 * 邀请记录
 */
public class InvitationRecordFragment extends BaseFragment implements
		OnClickListener {
	private View layout_view;
	private LinearLayout editlayout;
	private TextView btnShare, btnSelectAll, btnDelete;
	private PullToRefreshListView mPullRefreshListView;
	private int page;
	private List<JobInterviewEntity> showlist = new ArrayList<JobInterviewEntity>();
	private CommonAdapter<JobInterviewEntity> showAdapter;
	private TextView tv_select;
	private boolean isSelectAll;
	private boolean isShowEdit;
	private String companyResumeDetailUrl;
	private boolean firstLoad = true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater
				.inflate(R.layout.fragment_myrecruit_resume, null);
		init();
		return layout_view;

	}

	private void init() {
		initPullListView();
		initBottomEditBar();
		regisBroadcast();
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
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

	private void initBottomEditBar() {
		layout_view.findViewById(R.id.cancle).setVisibility(View.VISIBLE);
		editlayout = (LinearLayout) layout_view.findViewById(R.id.editlayout);
		tv_select = (TextView) layout_view.findViewById(R.id.tv_select);
		btnShare = (TextView) layout_view.findViewById(R.id.btnShare);
		btnSelectAll = (TextView) layout_view.findViewById(R.id.btnSelectAll);
		btnDelete = (TextView) layout_view.findViewById(R.id.btnDelete);
		tv_select.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnSelectAll.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
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

		showAdapter = new CommonAdapter<JobInterviewEntity>(getActivity(),
				R.layout.item_my_recruit, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final JobInterviewEntity t) {
				ImageView iv_select = viewHolder.getView(R.id.iv_select);
				if (t.isSelected()) {
					iv_select
							.setBackgroundResource(R.drawable.icon_xuanzhong_circle);
				} else {
					iv_select
							.setBackgroundResource(R.drawable.icon_weixuanzhong_circle);
				}
				if (isShowEdit) {
					iv_select.setVisibility(View.VISIBLE);
				} else {
					iv_select.setVisibility(View.GONE);
				}
				viewHolder.setImage(R.id.img_head, t.getClientResumePhoto());
				viewHolder.setText(R.id.tv_nickname, t.getRealName());
				TextView tv_genderandage = viewHolder
						.getView(R.id.tv_genderandage);
				if (Tool.isEmpty(t.getAgeValue())) {
					tv_genderandage.setText("未知");
				} else {
					tv_genderandage.setText(t.getAgeValue());
				}
				if ("1".equals(t.getGender())) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.nan_white);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					tv_genderandage.setCompoundDrawables(drawable, null, null,
							null);
					tv_genderandage.setBackgroundResource(R.drawable.nanxing);
				} else {
					Drawable drawable = getResources().getDrawable(
							R.drawable.nv_white);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					tv_genderandage.setCompoundDrawables(drawable, null, null,
							null);
					tv_genderandage.setBackgroundResource(R.drawable.nvxing);
				}
				viewHolder.setText(R.id.tv_position, "面试职位：" + t.getJobName());
				viewHolder.setText(R.id.tv_working_age,
						"从业年龄：" + t.getJobAgeValue());
				viewHolder.setText(R.id.tv_skill, "技能：" + t.getKnowledge());
				if ("1".equals(t.getIsView())) {// 0:未查看 1:已查看
					viewHolder.setText(R.id.tv_status, "已查看");
				} else {
					viewHolder.setText(R.id.tv_status, "未查看");
				}
				viewHolder.setText(R.id.tv_update_time,
						"刷新时间：" + t.getClientRefreshDate());
				if ("1".equals(t.getIsVoiceResume())) {
					viewHolder.getView(R.id.img_yinping).setVisibility(
							View.VISIBLE);
					viewHolder.setImage(R.id.img_yinping,
							R.drawable.searchresume_yinping);
				} else {
					viewHolder.getView(R.id.img_yinping).setVisibility(
							View.INVISIBLE);
				}
				if ("1".equals(t.getIsVideoResume())) {
					viewHolder.getView(R.id.img_shiping).setVisibility(
							View.VISIBLE);
					viewHolder.setImage(R.id.img_shiping,
							R.drawable.searchresume_shipin);
				} else {
					viewHolder.getView(R.id.img_shiping).setVisibility(
							View.GONE);
				}
				if ("1".equals(t.getIsWordResume())) {
					viewHolder.getView(R.id.img_wenzi).setVisibility(
							View.VISIBLE);
					viewHolder.setImage(R.id.img_wenzi,
							R.drawable.searchsume_wenzi);
				} else {
					viewHolder.getView(R.id.img_wenzi).setVisibility(View.GONE);
				}

				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (isShowEdit) {
									t.setSelected(!t.isSelected());
									showAdapter.notifyDataSetChanged();
									return;
								}
								// 点击跳转简历详情
								if (!Tool.isEmpty(companyResumeDetailUrl)
										&& !Tool.isEmpty(t.getResumeId())) {
									startActivity(new Intent(getActivity(),
											ResumeNoHfive.class).putExtra(
											"webUrl", companyResumeDetailUrl
											+ t.getResumeId()).putExtra("resumeId", t.getResumeId()));
//									startActivity(new Intent(getActivity(),
//											WebViewActivity.class).putExtra(
//											"webUrl", companyResumeDetailUrl
//													+ t.getResumeId()));
								} else {
									ToastUtil.showShortToast("参数有误，查看简历详情失败！");
								}
							}
						});
				viewHolder.getConvertView().setOnLongClickListener(
						new OnLongClickListener() {

							private CommonDialog commonDialog;

							@Override
							public boolean onLongClick(View arg0) {
								commonDialog = new CommonDialog(getActivity(),
										"提示",
										"删除 " + t.getRealName() + " 的简历?",
										"确定", "取消");
								commonDialog
										.setClicklistener(new CommonDialog.ClickListenerInterface() {
											@Override
											public void doConfirm() {
												commonDialog.dismiss();
												deleteSelectResumes(t.getId()
														+ "");

											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});
								commonDialog.show();
								return false;
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

	/**
	 * 获取简历数据
	 */
	private void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("interviewUserId", AppContext.getInstance().getUserInfo()
				.getId());
		map.put("realName", "");
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_INTERVIEWLIST));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req, false);
	}

	/**
	 * 删除简历
	 */
	private void deleteSelectResumes(String jobInterviewId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("jobInterviewId", jobInterviewId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_INTERVIEW_DELETE));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

	protected void downRefreshData() {
		showlist.clear();
		page = 1;
		GetData();
	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "邀请记录######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("jobInterview").toString().length() > 2) {
				List<JobInterviewEntity> listData = (List<JobInterviewEntity>) JsonUtil
						.jsonToList(resp.getBody().get("jobInterview")
								.toString(),
								new TypeToken<List<JobInterviewEntity>>() {
								}.getType());
				showlist.addAll(listData);
				showAdapter.notifyDataSetChanged();
				if (Tool.isEmpty(companyResumeDetailUrl)
						&& (resp.getBody().get("companyResumeDetailUrl") + "")
								.length() > 2) {
					companyResumeDetailUrl = resp.getBody()
							.get("companyResumeDetailUrl").getAsString();
					LogUtil.d("hl", "companyResumeDetailUrl="
							+ companyResumeDetailUrl);
				}
			} else {
				if (!firstLoad) {
					ToastUtil.showShortToast("无更多数据");
				}

			}
			mPullRefreshListView.onRefreshComplete();
			firstLoad = false;
			break;
		case reqCodeTwo:
			downRefreshData();
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取简历失败！");
			mPullRefreshListView.onRefreshComplete();
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("删除简历失败！");
			downRefreshData();
			break;
		}
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_select:
			isShowEditBar(true);
			isShowEdit = true;
			tv_select.setVisibility(View.GONE);
			showAdapter.notifyDataSetChanged();
			break;
		case R.id.btnSelectAll:
			isSelectAll = !isSelectAll;
			btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
			for (int i = 0, size = showlist.size(); i < size; i++) {
				showlist.get(i).setSelected(isSelectAll);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case R.id.btnDelete:
			String resumeSendIds = "";
			for (int i = 0, size = showlist.size(); i < size; i++) {
				if (showlist.get(i).isSelected()) {
					resumeSendIds += "," + showlist.get(i).getId();
				}
			}
			if (resumeSendIds.length() > 1) {
				resumeSendIds = resumeSendIds.substring(1,
						resumeSendIds.length());
				deleteSelectResumes(resumeSendIds);
			} else {
				ToastUtil.showShortToast("请勾选需要删除的简历");
			}
			break;
		case R.id.btnShare:
			isShowEditBar(false);
			isShowEdit = false;
			showAdapter.notifyDataSetChanged();
			tv_select.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	private void isShowEditBar(boolean show) {
		editlayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}
