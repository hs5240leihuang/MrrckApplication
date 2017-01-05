package com.meiku.dev.ui.product;

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
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ProductWillEntity;
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
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.IntentDialog;
import com.meiku.dev.views.IntentDialog.IntentClickListenerInterface;
import com.meiku.dev.views.ViewHolder;

/** 收到意向 */
public class ReceiveIntentFragment extends BaseFragment {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<ProductWillEntity> showAdapter;
	private List<ProductWillEntity> showlist = new ArrayList<ProductWillEntity>();
	private int page = 1;
	private IntentDialog intentDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_productcollect, null);
		initPullListView();

		return layout_view;
	}

	@Override
	public void initValue() {
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

		showAdapter = new CommonAdapter<ProductWillEntity>(getActivity(),
				R.layout.item_receive_intent, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final ProductWillEntity t) {
				viewHolder.setImage(R.id.iv_img, t.getClientHeadPicUrl());
				viewHolder.setText(R.id.tv_contactname, t.getContactName());
				viewHolder.setText(R.id.tv_date, t.getClientCreateDate());
				viewHolder.setText(R.id.tv_content, t.getWillContent());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								intentDialog = new IntentDialog(getActivity(),
										t, new IntentClickListenerInterface() {

											@Override
											public void doConfirm() {
												Uri smsToUri = Uri.parse("tel:"
														+ t.getContactPhone());
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
										},0);
								intentDialog.show();
							}
						});
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										getActivity(), "提示", "是否删除该意向", "确定",
										"取消");
								commonDialog
										.setClicklistener(new ClickListenerInterface() {

											@Override
											public void doConfirm() {
												ReqBase req = new ReqBase();
												Map<String, Object> map = new HashMap<String, Object>();
												map.put("userId", AppContext
														.getInstance()
														.getUserInfo()
														.getUserId());
												map.put("productId",
														t.getProductId());
												map.put("productWillId",
														t.getId());
												LogUtil.e(map + "");
												req.setHeader(new ReqHead(
														AppConfig.BUSINESS_PRODUCT_DELECT_WILL));
												req.setBody(JsonUtil
														.Map2JsonObj(map));
												httpPost(
														reqCodeTwo,
														AppConfig.PRODUCT_REQUEST_MAPPING,
														req);
												commonDialog.dismiss();
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});
								commonDialog.show();
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
	}

	protected void upRefreshData() {
		page++;
		getData();
	}

	protected void downRefreshData() {
		showlist.clear();
		page = 1;
		getData();
	}

	public void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_MY_INTENTION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, req);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("productWill").toString().length() > 2) {
				List<ProductWillEntity> listData = (List<ProductWillEntity>) JsonUtil
						.jsonToList(resp.getBody().get("productWill")
								.toString(),
								new TypeToken<List<ProductWillEntity>>() {
								}.getType());
				showlist.addAll(listData);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("删除成功");
			downRefreshData();
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
			ToastUtil.showShortToast("删除失败");
			break;
		default:
			break;
		}

	}

}
