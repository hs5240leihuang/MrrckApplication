package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DefriendEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 黑名单
 * 
 */
public class DefriendListActivity extends BaseActivity {

	private PullToRefreshListView mPullToRefreshListView;
	private CommonAdapter<DefriendEntity> showAdapter;
	private List<DefriendEntity> showList = new ArrayList<DefriendEntity>();

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_defriend;
	}

	@Override
	public void initView() {
		initPullListView();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView
				.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
		mPullToRefreshListView
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

		showAdapter = new CommonAdapter<DefriendEntity>(
				DefriendListActivity.this, R.layout.activity_defriend_item,
				showList) {

			@Override
			public void convert(final ViewHolder viewHolder, DefriendEntity t) {
				viewHolder.setText(R.id.name, t.getNickName());
				// viewHolder.setImageWithNewSize(R.id.headimg,
				// t.getClientHeadPicUrl(), 150, 150);
				LinearLayout layout_addImage = viewHolder.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(DefriendListActivity.this);
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientHeadPicUrl());
				viewHolder.setText(R.id.time, t.getCreateDate());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										DefriendListActivity.this,
										PersonShowActivity.class);
								intent.putExtra(
										PersonShowActivity.TO_USERID_KEY,
										showList.get(viewHolder.getPosition())
												.getDeFriendId() + "");

								intent.putExtra("nickName",
										showList.get(viewHolder.getPosition())
												.getNickName());
								startActivity(intent);
							}
						});
			}
		};

		mPullToRefreshListView.setAdapter(showAdapter);
	}

	// 上拉加载更多数据
	private void upRefreshData() {
		getdate();
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		showList.clear();
		getdate();
	}

	@Override
	public void initValue() {
		if (Tool.isEmpty(showList)) {
			downRefreshData();
		} else {
			// showList.addAll(AppContext.getBlackList());
			showAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void bindListener() {

	}

	public void getdate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_DEFRIEND));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "##" + reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = reqBase.getBody().get("defriend").toString();
			List<DefriendEntity> data = new ArrayList<DefriendEntity>();
			try {
				data = (List<DefriendEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<DefriendEntity>>() {
						}.getType());
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(data)) {
				ToastUtil.showShortToast("没有更多数据");

			} else {
				showList.addAll(data);
				showAdapter.notifyDataSetChanged();
			}
			// AppContext.setBlackList(data);
			mPullToRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("获取失败");
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

}
