package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
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
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/** 我的收藏 */
public class MyCollectActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<ProductCollectEntity> showAdapter;
	private List<ProductCollectEntity> showlist = new ArrayList<ProductCollectEntity>();
	private int page = 1;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_my_collect;
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
		initPullListView();
	}

	@Override
	public void initValue() {
		downRefreshData();
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

		showAdapter = new CommonAdapter<ProductCollectEntity>(this,
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
								Intent i = new Intent(MyCollectActivity.this,
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
										MyCollectActivity.this, "提示",
										"是否删除该收藏", "确定", "取消");
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
	public void bindListener() {

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
