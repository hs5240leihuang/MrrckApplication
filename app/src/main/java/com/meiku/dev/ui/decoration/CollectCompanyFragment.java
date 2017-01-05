package com.meiku.dev.ui.decoration;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCompanyEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.DoEditObs;
import com.meiku.dev.utils.DoEditObs.DoEditListener;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的收藏--装修公司
 * 
 */
public class CollectCompanyFragment extends BaseFragment implements
		OnClickListener {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateCompanyEntity> showAdapter;
	private List<DecorateCompanyEntity> showList = new ArrayList<DecorateCompanyEntity>();
	private LinearLayout editlayout;
	private TextView btnShare;
	private TextView btnSelectAll;
	private TextView btnDelete;
	protected boolean isEditModel;
	private boolean isSelectAll;
	private int page = 1;
	private RelativeLayout layoutEmpty;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(
				R.layout.fragment_mydecorationcasecollect, null);
		initView();
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

	private void initView() {
		layoutEmpty = (RelativeLayout) layout_view
				.findViewById(R.id.layoutEmpty);
		initBottomEditBar();
		initPullListView();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) layout_view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setDividerHeight(1);
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
		showAdapter = new CommonAdapter<DecorateCompanyEntity>(getActivity(),
				R.layout.item_case, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCompanyEntity t) {
				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						getActivity());
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview
						.setUrlOfImage(t.getClientCompanyLogo());
				ImageView iv_select = viewHolder.getView(R.id.iv_select);
				final int position = viewHolder.getPosition();
				boolean isselected = t.isSelect();
				if (isselected) {
					iv_select.setBackgroundResource(R.drawable.select_ok);
				} else {
					iv_select.setBackgroundResource(R.drawable.select_empty);
				}
				if (isEditModel) {
					iv_select.setVisibility(View.VISIBLE);
				} else {
					iv_select.setVisibility(View.GONE);
				}
				viewHolder.setText(R.id.tv_companyName, t.getName());
				viewHolder.setText(R.id.tv_serviceShopNum,
						"已有" + t.getHighCommentNum() + "家店铺评论");
				viewHolder.setText(R.id.iv_address, t.getAddress());
				LinearLayout layout_youhui = viewHolder
						.getView(R.id.layout_youhui);
				if (Tool.isEmpty(t.getDcfcList())) {
					layout_youhui.setVisibility(View.GONE);
					viewHolder.setText(R.id.tv_youhui, "");
				} else {
					layout_youhui.setVisibility(View.VISIBLE);
					String youhuiStrs = "";
					for (int i = 0, size = t.getDcfcList().size(); i < size; i++) {
						youhuiStrs += (t.getDcfcList().get(i).getName() + "\n");
					}
					viewHolder.setText(R.id.tv_youhui, youhuiStrs);
				}

				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (isEditModel) {
									showList.get(position).setSelect(
											!showList.get(position).isSelect());
									notifyDataSetChanged();
								} else {
									startActivity(new Intent(getActivity(),
											DecCompanyDetailActivity.class)
											.putExtra("shareTitle",
													t.getShareTitle())
											.putExtra("shareContent",
													t.getShareContent())
											.putExtra("shareImg",
													t.getShareImg())
											.putExtra("shareUrl",
													t.getShareUrl())
											.putExtra("userId", t.getUserId())
											.putExtra("sourceId", t.getId())
											.putExtra("loadUrl", t.getLoadUrl()));
								}
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

	}

	// 上拉加载更多数据
	private void upRefreshData() {
		page++;
		getData();
	}

	// 下拉重新刷新页面
	public void downRefreshData() {
		page = 1;
		showList.clear();
		getData();
	}

	private void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", 2);// // 案例 1 公司 2
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300021));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	private void initBottomEditBar() {
		editlayout = (LinearLayout) layout_view.findViewById(R.id.editlayout);
		btnShare = (TextView) layout_view.findViewById(R.id.btnShare);
		btnSelectAll = (TextView) layout_view.findViewById(R.id.btnSelectAll);
		btnDelete = (TextView) layout_view.findViewById(R.id.btnDelete);
		btnShare.setOnClickListener(this);
		btnSelectAll.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		DoEditObs.getInstance().registerListener(this.getClass().getName(),
				new DoEditListener() {

					@Override
					public void doEdit(boolean isSelectModel) {
						isShowBottomEditBar(isSelectModel);
						isEditModel = isSelectModel;
						showAdapter.notifyDataSetChanged();
					}

					@Override
					public void doRefresh() {
						downRefreshData();
					}

				});
	}

	/**
	 * 是否显示底部的编辑条
	 */
	private void isShowBottomEditBar(boolean show) {
		editlayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				try {
					List<DecorateCompanyEntity> caseList = (List<DecorateCompanyEntity>) JsonUtil
							.jsonToList(
									resp.getBody().get("data").toString(),
									new TypeToken<List<DecorateCompanyEntity>>() {
									}.getType());
					showList.addAll(caseList);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
			}
			layoutEmpty.setVisibility(Tool.isEmpty(showList) ? View.VISIBLE
					: View.GONE);
			showAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			break;
		case reqCodeTwo:
			downRefreshData();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						getActivity(), "提示", resp.getHeader().getRetMessage(),
						"确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnShare:

			break;
		case R.id.btnSelectAll:
			isSelectAll = !isSelectAll;
			btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
			for (int i = 0, size = showList.size(); i < size; i++) {
				showList.get(i).setSelect(isSelectAll);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case R.id.btnDelete:
			String sourceIds = "";
			for (int i = 0, size = showList.size(); i < size; i++) {
				if (showList.get(i).isSelect()) {
					sourceIds += "," + showList.get(i).getDCOCCId();
				}
			}
			if (sourceIds.length() > 1) {
				sourceIds = sourceIds.substring(1, sourceIds.length());
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", 1);// 案例 0 公司 1
				map.put("ids", sourceIds);
				map.put("collecteFlag", 2);// 1 表示收藏 2 表示取消收藏
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300022));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.d("hl", "————" + map);
				httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
			} else {
				ToastUtil.showShortToast("请勾选需要删除的公司");
			}
			break;
		default:
			break;
		}
	}

}
