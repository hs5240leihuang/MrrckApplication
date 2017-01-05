package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ProductCollectEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.myshow.WorkDetailNewActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;

/** 产品收藏 */
public class ProductCollectFragment extends BaseFragment {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<ProductCollectEntity> showAdapter;
	private List<ProductCollectEntity> showlist = new ArrayList<ProductCollectEntity>();
	private int page = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_productcollect, null);
		initPullListView();
		return layout_view;
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

		showAdapter = new CommonAdapter<ProductCollectEntity>(getActivity(),
				R.layout.item_my_collect, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final ProductCollectEntity t) {
				viewHolder.setImage(R.id.iv_img, t.getClientPosterThum());
				viewHolder.setText(R.id.tv_name, t.getProductName());
				viewHolder.setText(R.id.tv_comp, t.getCompanyName());
				viewHolder.setText(R.id.tv_time, t.getClientCreateDate());
				viewHolder.setText(R.id.tv_pro, "招商省份:" + t.getProvinceNames());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										ProductDetailActivity.class);
								i.putExtra("productId", t.getProductId() + "");
								startActivity(i);
							}
						});
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										getActivity(), "提示", "是否删除该收藏", "确定",
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
												map.put("productCollectId",
														t.getId());
												LogUtil.e(map + "");
												req.setHeader(new ReqHead(
														AppConfig.BUSINESS_PRODUCT_DELECT_COLLECTION));
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
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_MY_COLLECTION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, req);
	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("productCollect").toString().length() > 2) {
				List<ProductCollectEntity> listData = (List<ProductCollectEntity>) JsonUtil
						.jsonToList(resp.getBody().get("productCollect")
								.toString(),
								new TypeToken<List<ProductCollectEntity>>() {
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
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

}
