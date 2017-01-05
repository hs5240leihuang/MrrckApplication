package com.meiku.dev.ui.mine;

import java.io.Serializable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.decoration.OrderDetailActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 我的订单--全部、已完成、待支付共用
 * 
 */
public class OrderFragment extends BaseFragment {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<PayOrderGroupEntity> showAdapter;
	private List<PayOrderGroupEntity> showlist = new ArrayList<PayOrderGroupEntity>();
	private LinearLayout lin_buju;
	private int page = 1;
	private int index;
	private int type;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_order, null);
		index = getArguments() != null ? getArguments().getInt("index") : 0;
		type = getArguments() != null ? getArguments().getInt("type") : 0;
		initPullListView();
		lin_buju = (LinearLayout) layout_view.findViewById(R.id.lin_buju);
		return layout_view;

	}

	public static OrderFragment newInstance(int index, int type) {
		final OrderFragment f = new OrderFragment();
		final Bundle args = new Bundle();
		args.putInt("index", index);
		args.putInt("type", type);
		f.setArguments(args);
		return f;
	}

	@Override
	public void initValue() {

		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke1", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<PayOrderGroupEntity> listData = (List<PayOrderGroupEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<PayOrderGroupEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
			}
			if (showlist.size() > 0) {
				lin_buju.setVisibility(View.INVISIBLE);
			} else {
				lin_buju.setVisibility(View.VISIBLE);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			downRefreshData();
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
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

		showAdapter = new CommonAdapter<PayOrderGroupEntity>(getActivity(),
				R.layout.item_order, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final PayOrderGroupEntity t) {

				LinearLayout lin_picture = viewHolder.getView(R.id.lin_picture);
				lin_picture.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MySimpleDraweeView img = new MySimpleDraweeView(getActivity());
				lin_picture.addView(img, layoutParams);
				img.setUrlOfImage(t.getClientWorkFileUrl());
				viewHolder.setText(R.id.tv_ordername, t.getOrderName()
						.replaceAll("%%", "\n"));
				viewHolder.setText(R.id.tv_detail, t.getOrderContent()
						.replaceAll("%%", "\n"));
				viewHolder.setText(R.id.tv_onstatus, t.getOrderStatusName());
				viewHolder.setText(R.id.tv_money, "￥" + t.getOrderAllAmount());
				Button shanchudingdan = viewHolder
						.getView(R.id.btn_shanchudingdan);
				Button quzhifu = viewHolder.getView(R.id.btn_quzhifu);
				Button zaicigoumai = viewHolder.getView(R.id.btn_zaicigoumai);
				if (t.getOrderPayType() == 1 || t.getOrderPayType() == 3) {
					zaicigoumai.setVisibility(View.GONE);
					quzhifu.setVisibility(View.GONE);
				} else {
					zaicigoumai.setVisibility(View.GONE);
					quzhifu.setVisibility(View.VISIBLE);
				}
				shanchudingdan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						final CommonDialog commonDialog = new CommonDialog(
								getActivity(), "提示", "确定删除该订单？", "确定", "取消");
						commonDialog
								.setClicklistener(new CommonDialog.ClickListenerInterface() {
									@Override
									public void doConfirm() {
										commonDialog.dismiss();
										Delete(t.getOrderGroupNumber());
									}

									@Override
									public void doCancel() {
										commonDialog.dismiss();
									}
								});
						commonDialog.show();
					}
				});
				quzhifu.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Bundle b = new Bundle();
						b.putSerializable("MkPayOrders", (Serializable) t);
						getActivity().startActivityForResult(new Intent(getActivity(),
								PayStyleActivity.class).putExtras(b),
								reqCodeThree);
					}
				});
				zaicigoumai.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(getActivity(),
										OrderDetailActivity.class);
								intent.putExtra("orderGroupNumber",
										t.getOrderGroupNumber());
								getActivity().startActivityForResult(intent,
										reqCodeThree);
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
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("type", type);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_22005));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_APIPAY, req);
	}

	// 删除订单
	public void Delete(String orderGroupNumber) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderGroupNumber", orderGroupNumber);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_22004));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
	}

}
