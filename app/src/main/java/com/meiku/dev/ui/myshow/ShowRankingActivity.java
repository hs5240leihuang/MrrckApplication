package com.meiku.dev.ui.myshow;

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
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 才艺秀-排名
 * 
 */
public class ShowRankingActivity extends BaseActivity {
	private int page = 1;
	private CommonAdapter<ShowPostsSignupEntity> showAdapter;
	private List<ShowPostsSignupEntity> showlist = new ArrayList<ShowPostsSignupEntity>();
	private PullToRefreshListView mPullRefreshListView;
	private int postsId;
	private String title;
	private TextView center_txt_title;
	private TextView tv_myrank, tv_number, tv_name, tv_bianhao, tv_position,
			tv_depiaoshu;
	private LinearLayout lin_head;
	private ShowPostsSignupEntity postsSignupPersonal;
	private LinearLayout lin_myrank;
	private View in_myshow;
	private boolean hasMyRank;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.fragment_talent_show;
	}

	@Override
	public void initView() {
		in_myshow = findViewById(R.id.in_myshow);
		in_myshow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (postsSignupPersonal != null) {
					startActivity(new Intent(ShowRankingActivity.this,
							NewWorkDetailActivity.class).putExtra("SignupId",
							postsSignupPersonal.getSignupId()));
				}
			}
		});
		lin_myrank = (LinearLayout) findViewById(R.id.lin_myrank);
		tv_myrank = (TextView) findViewById(R.id.tv_myrank);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_bianhao = (TextView) findViewById(R.id.tv_bianhao);
		tv_position = (TextView) findViewById(R.id.tv_position);
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		tv_depiaoshu = (TextView) findViewById(R.id.tv_depiaoshu);
		lin_head = (LinearLayout) findViewById(R.id.lin_head);
		initPullListView();
	}

	@Override
	public void initValue() {
		postsId = getIntent().getIntExtra("postsId", 0);
		title = getIntent().getStringExtra("title");
		center_txt_title.setText(title);
		downRefreshData();
	}

	@Override
	public void bindListener() {

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

		showAdapter = new CommonAdapter<ShowPostsSignupEntity>(
				ShowRankingActivity.this, R.layout.item_fragment_talent_show,
				showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final ShowPostsSignupEntity t) {
				viewHolder.setText(R.id.tv_number, t.getRankNum() + "");
				// viewHolder
				// .setImageWithNewSize(R.id.img_head,
				// t.getClientThumbHeadPicUrl(),200,200);
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						ShowRankingActivity.this);
				lin_head.removeAllViews();
				lin_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder.setText(R.id.tv_position, t.getPositionName());
				viewHolder.setText(R.id.tv_bianhao, "编号：" + t.getSignupNo());
				viewHolder.setText(R.id.tv_depiaoshu, t.getTotalVoteNum() + "");
				final boolean isCanSaiWork = t.getWorksFlag() == 1;// 0:普通作品,1:比赛作品
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (isCanSaiWork) {// 是否参赛作品
									startActivity(new Intent(
											ShowRankingActivity.this,
											NewWorkDetailActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								} else {
									startActivity(new Intent(
											ShowRankingActivity.this,
											WorkDetailNewActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								}
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
	}

	protected void upRefreshData() {
		page++;
		getData(true);
	}

	protected void downRefreshData() {
		showlist.clear();
		page = 1;
		getData(true);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("postsSignup") + "").length() > 2) {
				List<ShowPostsSignupEntity> listData = (List<ShowPostsSignupEntity>) JsonUtil
						.jsonToList(resp.getBody().get("postsSignup")
								.toString(),
								new TypeToken<List<ShowPostsSignupEntity>>() {
								}.getType());
				if (!Tool.isEmpty(listData)) {
					showlist.addAll(listData);
				}
			}
			showAdapter.notifyDataSetChanged();

			if ((resp.getBody().get("postsSignupPersonal") + "").length() > 2) {
				String json = resp.getBody().get("postsSignupPersonal")
						.toString();
				postsSignupPersonal = (ShowPostsSignupEntity) JsonUtil
						.jsonToObj(ShowPostsSignupEntity.class, json);
				tv_myrank.setText(postsSignupPersonal.getRankNum() + "");
				tv_number.setText(postsSignupPersonal.getRankNum() + "");
				tv_name.setText(postsSignupPersonal.getNickName() + "");
				tv_bianhao.setText("编号：" + postsSignupPersonal.getSignupNo());
				tv_position.setText(postsSignupPersonal.getPositionName());
				tv_depiaoshu.setText(postsSignupPersonal.getVoteNum() + "");
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						ShowRankingActivity.this);
				lin_head.removeAllViews();
				lin_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(postsSignupPersonal
						.getClientThumbHeadPicUrl());
				hasMyRank = true;
			}
			if (!hasMyRank) {
				in_myshow.setVisibility(View.GONE);
				lin_myrank.setVisibility(View.GONE);
			}

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

	// 请求排名数据
	public void getData(boolean showProgress) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("cityCode", -1);
		map.put("postsId", postsId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.d("hl", "排名" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_INFORMATION_RANKING));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, showProgress);
	}

}
