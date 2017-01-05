package com.meiku.dev.ui.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.im.GroupInfoActivity;
import com.meiku.dev.ui.product.ProductDetailActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

public class TaGroupActivity extends BaseActivity {

	private PullToRefreshListView mPullToRefreshListView;

	private List<GroupEntity> showList = new ArrayList<GroupEntity>();
	private CommonAdapter<GroupEntity> showAdapter;
	private String userId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_ta_group;
	}

	@Override
	public void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView
				.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);

		// 下来刷新
		mPullToRefreshListView.getLoadingLayoutProxy(true, false)
				.setLastUpdatedLabel("下拉刷新");
		mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(
				"");
		mPullToRefreshListView.getLoadingLayoutProxy(true, false)
				.setRefreshingLabel("正在刷新");
		mPullToRefreshListView.getLoadingLayoutProxy(true, false)
				.setReleaseLabel("放开以刷新");
		// 下来加载更多
		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
				.setLastUpdatedLabel("上拉加载");
		mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				"");
		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel("正在加载...");
		mPullToRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel("放开以加载");
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
						// upRefreshData();
					}
				});

		// 适配器
		showAdapter = new CommonAdapter<GroupEntity>(TaGroupActivity.this,
				R.layout.item_choosegroup, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final GroupEntity t) {
				// viewHolder.setImageWithNewSize(R.id.iv_grouphead,
				// t.getClientThumbGroupPhoto(), 150, 150);
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MySimpleDraweeView iv_grouphead = new MySimpleDraweeView(
						TaGroupActivity.this);
				layout_addImage.addView(iv_grouphead,
						new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));
				iv_grouphead.setUrlOfImage(t.getClientThumbGroupPhoto());
				viewHolder.setText(R.id.tv_groupname, t.getGroupName());
				viewHolder.setText(R.id.tv_membernum, "(" + t.getMemberNum()
						+ ")");
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// Intent intent = new Intent(
								// TaGroupActivity.this,
								// GroupChatActivity.class);
								// intent.putExtra(
								// ConstantKey.KEY_IM_MULTI_CHATROOM,
								// t.getGroupName());
								// intent.putExtra(
								// ConstantKey.KEY_IM_MULTI_CHATROOMID,
								// t.getId());
								// startActivity(intent);
								if (!NetworkTools.isNetworkAvailable(TaGroupActivity.this)) {
									ToastUtil.showShortToast(getResources()
											.getString(R.string.netNoUse));
									return;
								}
								Intent intent = new Intent(
										TaGroupActivity.this,
										GroupInfoActivity.class);
								intent.putExtra(
										ConstantKey.KEY_IM_MULTI_CHATROOMID,
										t.getId() + "");
								startActivity(intent);
							}
						});
			}

		};
		mPullToRefreshListView.setAdapter(showAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		downRefreshData();
	}

	protected void downRefreshData() {
		showList.clear();
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("loginUserId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_SEARCH_GROUP));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
	}

	@Override
	public void initValue() {
		userId = getIntent().getStringExtra("userId");
		if ((AppContext.getInstance().getUserInfo().getId() + "")
				.equals(userId)) {
			((TextView) findViewById(R.id.center_txt_title)).setText("我的群组");
		}
		showAdapter.notifyDataSetChanged();
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			LogUtil.e("NATHAN:" + reqBase.getBody().toString());
			String jsonstr = reqBase.getBody().get("group").toString();
			if (!Tool.isEmpty(jsonstr) && jsonstr.length() > 2) {
				showList = (List<GroupEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<GroupEntity>>() {
						}.getType());
				showAdapter.setmDatas(showList);
			}
			showAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}
}
