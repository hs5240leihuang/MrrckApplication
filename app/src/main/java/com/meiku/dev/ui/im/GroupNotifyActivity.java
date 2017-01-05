package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ScrollView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.GroupNotifySwipeAdapter;
import com.meiku.dev.adapter.GroupNotifySwipeAdapter.onRightItemClickListener;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MySwipeListView;

/**
 * 申请入群通知列表
 */
public class GroupNotifyActivity extends BaseActivity {

	private MySwipeListView mListView;
	private List<GroupUserEntity> showList = new ArrayList<GroupUserEntity>();
	private GroupNotifySwipeAdapter adapter;
	private PullToRefreshScrollView pull_refreshSV;// 下拉刷新
	private int page = 1;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_groupnotify;
	}

	@Override
	public void initView() {
		initPullView();
		mListView = (MySwipeListView) findViewById(R.id.listview);
		adapter = new GroupNotifySwipeAdapter(GroupNotifyActivity.this,
				showList, mListView.getRightViewWidth());
		adapter.setOnRightItemClickListener(new onRightItemClickListener() {

			@Override
			public void onRightItemClick(View v, int position) {
				Caozuo(3, showList.get(position).getId());
			}

			@Override
			public void onRightBtnClick(View v, int position) {
				Caozuo(1, showList.get(position).getId());
			}

			@Override
			public void onLeftItemClick(View v, int position) {
				if (showList.get(position).getApproveStatus() == 0) {
					startActivityForResult(new Intent(GroupNotifyActivity.this,
							HandleGroupRequestActivity.class).putExtra(
							"groupUserId", showList.get(position).getId()),
							reqCodeOne);
				}

			}
		});
		mListView.setAdapter(adapter);

	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.e("555", requestCode + "##" + reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = reqBase.getBody().get("data").toString();
			List<GroupUserEntity> data = new ArrayList<GroupUserEntity>();
			try {
				data = (List<GroupUserEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<GroupUserEntity>>() {
						}.getType());
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(data)) {
				ToastUtil.showShortToast("没有更多验证通知");
			} else {
				showList.addAll(data);
			}
			adapter.notifyDataSetChanged();
			pull_refreshSV.onRefreshComplete();
			break;
		case reqCodeTwo:
			downRefreshData();
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (pull_refreshSV != null) {
			pull_refreshSV.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase reqBase = (ReqBase) arg0;
			ToastUtil.showShortToast(reqBase.getHeader().getRetMessage());
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				downRefreshData();
			}
		}
	}

	public void GetDate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("pageNum", ConstantKey.PageNum);
		map.put("page", page);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18049));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullView() {
		pull_refreshSV = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
		pull_refreshSV.setMode(PullToRefreshBase.Mode.BOTH);
		pull_refreshSV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						upRefreshData();
					}
				});

	}

	protected void upRefreshData() {
		page++;
		GetDate();
	}

	protected void downRefreshData() {
		page = 1;
		showList.clear();
		GetDate();
	}

	public void Caozuo(int type, int groupUserId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupUserId", groupUserId);
		map.put("type", type);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18050));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}
}
