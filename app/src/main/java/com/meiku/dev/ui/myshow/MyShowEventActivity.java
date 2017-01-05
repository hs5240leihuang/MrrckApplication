package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.SelectCityDialog;
import com.meiku.dev.views.SelectCityDialog.SelectListener;
import com.meiku.dev.views.ViewHolder;

/**
 * 赛事
 * 
 */
public class MyShowEventActivity extends BaseActivity {
	private TextView right_txt_title;
	private EditText et_msg_search;
	private PullToRefreshListView mPullRefreshListView;;// 下拉刷新
	private List<PostsEntity> showList = new ArrayList<PostsEntity>();
	private CommonAdapter<PostsEntity> commonAdapter;
	private int page = 1;
	protected String cityCode = "-1";
	protected String provinceCode="-1";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myshow_event;
	}

	@Override
	public void initView() {
		initTitle();
		initSearchView();
		initPullListView();
	}

	private void initTitle() {
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setTextSize(12);
		right_txt_title.setBackground(null);
		Drawable drawable = getResources().getDrawable(R.drawable.bottom_raw);
		drawable.setBounds(0, 0, ScreenUtil.dip2px(this, 20),
				ScreenUtil.dip2px(this, 20));
		right_txt_title.setCompoundDrawables(null, null, drawable, null);
		right_txt_title.setSingleLine(false);
		right_txt_title.setLines(2);
		right_txt_title.setEllipsize(TruncateAt.END);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Drawable drawable = ContextCompat.getDrawable(MyShowEventActivity.this,
						R.drawable.top_raw);
				drawable.setBounds(0, 0,
						ScreenUtil.dip2px(MyShowEventActivity.this, 20),
						ScreenUtil.dip2px(MyShowEventActivity.this, 20));
				right_txt_title
						.setCompoundDrawables(null, null, drawable, null);
				SelectCityDialog dialog = new SelectCityDialog(
						MyShowEventActivity.this, new SelectListener() {

							@Override
							public void choiseOneCity(String provinceCode,
									String cityCode, String cityName) {
								right_txt_title.setText(cityName);
								MyShowEventActivity.this.provinceCode = provinceCode;
								MyShowEventActivity.this.cityCode = cityCode;
								downRefreshData();
							}
						});
				dialog.setSelectIndex(provinceCode);
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.bottom_raw);
						drawable.setBounds(
								0,
								0,
								ScreenUtil.dip2px(MyShowEventActivity.this, 20),
								ScreenUtil.dip2px(MyShowEventActivity.this, 20));
						right_txt_title.setCompoundDrawables(null, null,
								drawable, null);
					}
				});
				dialog.ShowAtLocation(0,
						ScreenUtil.dip2px(MyShowEventActivity.this, 50));
				;
			}
		});
	}

	private void initSearchView() {
		et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		et_msg_search.setKeyListener(null);
		et_msg_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MyShowEventActivity.this,
						MyShowEventSearchActivity.class));
			}
		});
	}

	@Override
	public void initValue() {
		getData(page);
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk",resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = resp.getBody().get("postsList").toString();
			try {
				showList.addAll((List<PostsEntity>) JsonUtil.jsonToList(
						jsonstr, new TypeToken<List<PostsEntity>>() {
						}.getType()));
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(showList)) {
				ToastUtil.showShortToast("没有更多数据");
			}
			commonAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("请求失败");
			if (null!=mPullRefreshListView) {
				mPullRefreshListView.onRefreshComplete();
			}
			break;

		default:
			break;
		}
	}

	// 请求数据
	public void getData(int page) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("cityCode", cityCode);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_EVENT_SHOW));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, reqBase);
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
		commonAdapter = new CommonAdapter<PostsEntity>(
				MyShowEventActivity.this, R.layout.item_myshow_event, showList) {

			@Override
			public void convert(ViewHolder viewHolder, PostsEntity t) {
				viewHolder.setText(R.id.tv_Propaganda_language, t.getTitle());
				viewHolder.setImage(R.id.img_page, t.getClientImgUrl());
				if (t.getSignupFlag().equals("0")) {
					viewHolder.setImage(R.id.img_flag, R.drawable.weikaishi);
				} else if (t.getSignupFlag().equals("1")) {
					viewHolder.setImage(R.id.img_flag, R.drawable.jinxingzhong);
				} else {
					viewHolder.setImage(R.id.img_flag, R.drawable.yijieshu);
				}
				viewHolder.setText(R.id.tv_time,
						t.getApplyStartDate() + "-" + t.getApplyEndDate());
				viewHolder.setText(R.id.tv_city, t.getActiveCityName());
			}
		};
		mPullRefreshListView.setAdapter(commonAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MyShowEventActivity.this,
						ShowMainActivity.class);
				intent.putExtra("postsId", showList.get(arg2 - 1).getPostsId());
				intent.putExtra("title", showList.get(arg2 - 1).getTitle());
				startActivity(intent);

			}
		});
	}

	protected void upRefreshData() {
		page++;
		getData(page);
	}

	protected void downRefreshData() {
		page = 1;
		showList.clear();
		getData(page);
	}

	// 赛事搜索请求
	public void getDataSearch(int searchpage) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("page", searchpage);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("title", et_msg_search.getText().toString());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_SOUSUO_EVENT_SHOW));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, reqBase);
	}

}
