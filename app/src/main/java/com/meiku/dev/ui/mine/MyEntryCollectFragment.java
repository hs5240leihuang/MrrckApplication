package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.android.bbalbs.common.a.c;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.BaikeMkEntity;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.encyclopaedia.EntriesDetailActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 我的--我收藏的词条
 * 
 */
public class MyEntryCollectFragment extends BaseFragment {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<BaikeMkEntity> showAdapter;
	private List<BaikeMkEntity> showlist = new ArrayList<BaikeMkEntity>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_citiaocollect, null);
		initPullListView();
		return layout_view;

	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("baiKe").toString().length() > 2) {
				List<BaikeMkEntity> listData = (List<BaikeMkEntity>) JsonUtil
						.jsonToList(resp.getBody().get("baiKe").toString(),
								new TypeToken<List<BaikeMkEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
				ToastUtil.showShortToast("没有更多数据");
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			downRefreshData();
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
			ToastUtil.showShortToast("请求数据失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("取消收藏失败失败");
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
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
					}
				}); 

		showAdapter = new CommonAdapter<BaikeMkEntity>(getActivity(),
				R.layout.item_mycitiao, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final BaikeMkEntity t) {
				viewHolder.getView(R.id.img_do).setBackground(
						ContextCompat.getDrawable(getActivity(),
								R.drawable.entry_cancelcollect));
				LinearLayout lin_photo = viewHolder.getView(R.id.lin_photo);
				lin_photo.removeAllViews();
				MyRectDraweeView img_photo = new MyRectDraweeView(getActivity());
				lin_photo.addView(img_photo, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_photo.setUrlOfImage(t.getMainPhotoThumb());
				viewHolder.setText(R.id.tv_name, t.getName());
				viewHolder.setText(R.id.tv_summary, t.getSummary());
				viewHolder.getView(R.id.img_do).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								CancelCollect(t.getId());
							}
						});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(getActivity(),
										EntriesDetailActivity.class);
								intent.putExtra("baikeId", t.getId());
								intent.putExtra("categoryId", t.getCategoryId());
								intent.putExtra("userId", t.getUserId());
								intent.putExtra("loadUrl", t.getLoadUrl());
								intent.putExtra("shareUrl", t.getShareUrl());
								intent.putExtra("title", t.getShareTitle());
								intent.putExtra("content", t.getShareContent());
								intent.putExtra("image", t.getMainPhotoThumb());
								startActivity(intent);
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

	}

	protected void downRefreshData() {
		showlist.clear();
		GetData();
	}

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_CITIAOCOLLECT));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BAIKE, req);
	}

	// 取消收藏
	public void CancelCollect(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("baikeId", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_CANCEL));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BAIKE, req);
	}
}
