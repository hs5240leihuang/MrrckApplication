package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MkDecorateCasePriceApply;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.IntentDialog;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.IntentDialog.IntentClickListenerInterface;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的发布-->收到的意向
 * 
 */
public class ReceivedIntentFragment extends BaseFragment {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<MkDecorateCasePriceApply> showAdapter;
	private List<MkDecorateCasePriceApply> showlist = new ArrayList<MkDecorateCasePriceApply>();
	private int page = 1;
	private IntentDialog intentDialog;
	private LinearLayout lin_buju;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(
				R.layout.activity_receivedintentfragment, null);
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

		lin_buju = (LinearLayout) layout_view.findViewById(R.id.lin_buju);
		downRefreshData();
	}
 
	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("000002", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<MkDecorateCasePriceApply> listData = (List<MkDecorateCasePriceApply>) JsonUtil
						.jsonToList(
								resp.getBody().get("data").toString(),
								new TypeToken<List<MkDecorateCasePriceApply>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
			}
			if (showlist.size() > 0) {
				lin_buju.setVisibility(View.GONE);
			} else {
				lin_buju.setVisibility(View.VISIBLE);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			downRefreshData();
			break;
		case reqCodeThree:

			break;
		default:
			break;

		}
		mPullRefreshListView.onRefreshComplete();

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != mPullRefreshListView) {
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
		default:
			break;
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
		showAdapter = new CommonAdapter<MkDecorateCasePriceApply>(
				getActivity(), R.layout.item_receivedintentfragment, showlist) {

			@Override
			public void convert(final ViewHolder viewHolder,
					final MkDecorateCasePriceApply t) {
				LinearLayout lin_picture = viewHolder.getView(R.id.lin_img);
				lin_picture.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MyRoundDraweeView imageView = new MyRoundDraweeView(
						getActivity());
				imageView.setLayoutParams(layoutParams);
				lin_picture.addView(imageView);
				imageView.setUrlOfImage(t.getClientHeadPicUrl());
				if (t.getReadFlag() == 1) {
					viewHolder.getView(R.id.img_dian).setVisibility(
							View.VISIBLE);
				} else {
					viewHolder.getView(R.id.img_dian).setVisibility(
							View.INVISIBLE);
				}
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder.setText(R.id.tv_time, t.getCreateDate());
				viewHolder.setText(R.id.tv_intent, t.getTitle());
				viewHolder.setText(R.id.tv_xingming, "姓名：" + t.getName());
				viewHolder.setText(R.id.tv_phone, "手机：" + t.getPhone());
				viewHolder.setText(R.id.tv_position, "地址：" + t.getAddress());
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Delete(t.getId());
							}
						});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								if (t.getReadFlag() == 1) {
									Ready(t.getId());
									viewHolder.getView(R.id.img_dian)
											.setVisibility(View.INVISIBLE);
								}
								intentDialog = new IntentDialog(getActivity(),
										t, new IntentClickListenerInterface() {

											@Override
											public void doConfirm() {
												Uri smsToUri = Uri.parse("tel:"
														+ t.getPhone());
												Intent mIntent = new Intent(
														android.content.Intent.ACTION_DIAL,
														smsToUri);
												startActivity(mIntent);
												intentDialog.dismiss();
											}

											@Override
											public void doCancel() {
												intentDialog.dismiss();
											}
										}, 1);
								intentDialog.show();

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
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300016));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 删除请求
	public void Delete(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("deleteType", 3);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.ZX_300024));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	// 是否以读
	public void Ready(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300027));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_DECORATION, req);
	}
}
