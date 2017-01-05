package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.meiku.dev.bean.NearUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 搜索好友
 * 
 */
public class SearchFriendActivity extends BaseActivity {
	private PullToRefreshListView mPullToRefreshListView;
	private CommonAdapter<NearUserEntity> showAdapter;
	private List<NearUserEntity> showlist = new ArrayList<NearUserEntity>();
	private TextView cancel;
	private ClearEditText et_msg_search;
	private String searchName;
	protected final int GETDATA = 1;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_search;
	}

	@Override
	public void initView() {
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
	}

	@Override
	public void initValue() {
		initPullListView();
	}

	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
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
		showAdapter = new CommonAdapter<NearUserEntity>(this,
				R.layout.activity_search_listitem, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final NearUserEntity t) {
				viewHolder.setText(R.id.tv_name, t.getNickName());
				// viewHolder.setImageWithNewSize(R.id.iv_head,
				// t.getClientThumbHeadPicUrl(), 150, 150);
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(
						SearchFriendActivity.this);
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.getView(R.id.layout_person).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										SearchFriendActivity.this,
										PersonShowActivity.class);
								intent.putExtra(
										PersonShowActivity.TO_USERID_KEY,
										t.getUserId() + "");
								intent.putExtra("nickName", t.getNickName());
								startActivity(intent);
							}
						});
			}
		};
		mPullToRefreshListView.setAdapter(showAdapter);
	}

	// 数据请求
	public void GetData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("searchName", searchName);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.e("NATHAN:" + map.toString());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_SEARCH_FRIEND));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, false);
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

	@Override
	public void bindListener() {
		et_msg_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				searchName = arg0.toString();
				if (myTimer!=null) {
					myTimer.cancel();
				}
				myTimer = new Timer();
				myTimer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						handler.sendEmptyMessage(GETDATA);
					}
				}, 500);
			}
		});
	}
	
	Timer myTimer;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETDATA:
				downRefreshData();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("user").toString().length() > 2) {
				List<NearUserEntity> listData = (List<NearUserEntity>) JsonUtil
						.jsonToList(resp.getBody().get("user").toString(),
								new TypeToken<List<NearUserEntity>>() {
								}.getType());
				if (!Tool.isEmpty(listData)) {
					showlist.addAll(listData);
				}
			} else {
				ToastUtil.showShortToast("没有更多数据");
			}
			showAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
			myTimer.cancel();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("获取失败");
		if (null != mPullToRefreshListView) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

}
