package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MatchCityEntity;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 赛事-4冠军排名
 * 
 */
public class MatchRankingFragment extends BaseFragment {

	private int matchId;
	private View layout_view;
	private TextView right_txt_title;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<MatchCityEntity> showAdapter;
	private List<MatchCityEntity> showlist = new ArrayList<MatchCityEntity>();
	private List<MatchCityEntity> showlist1 = new ArrayList<MatchCityEntity>();
	private List<Integer> cityCodeList = new ArrayList<Integer>();
	protected MyPopupwindow myPopupwindow;
	private List<PopupData> cityList = new ArrayList<PopupData>();
	private int cityCode = -1;
	private int page = 1;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_matchrainking, null);
		initView();
		return layout_view;
	}

	private void initView() {
		initTitle();
		initPopupWindow();
		initPullListView();
	}

	/**
	 * 获取排名数据
	 */
	private void getRainkingData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matchId", matchId);
		map.put("cityCode", cityCode);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_RAINKING));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
		LogUtil.d("hl", "获取排名数据matchId=" + matchId);
	}

	private void initPopupWindow() {

		myPopupwindow = new MyPopupwindow(getActivity(), cityList,
				new popListener() {

					@Override
					public void doChoose(int position) {
						cityCode = cityCodeList.get(position);
						right_txt_title.setText(cityList.get(position)
								.getName());
						downRefreshData();
					}
				});
		myPopupwindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				Drawable drawable = getResources().getDrawable(
						R.drawable.triangle);
				drawable.setBounds(0, 0, ScreenUtil.dip2px(getActivity(), 8),
						ScreenUtil.dip2px(getActivity(), 8));
				right_txt_title
						.setCompoundDrawables(null, null, drawable, null);
			}
		});
	}

	@SuppressLint("NewApi")
	private void initTitle() {
		right_txt_title = (TextView) layout_view
				.findViewById(R.id.right_txt_title);
		right_txt_title.setTextSize(12);
		right_txt_title.setTextColor(Color.parseColor("#000000"));
		right_txt_title.setBackground(null);
		Drawable drawable = getResources().getDrawable(R.drawable.triangle);
		drawable.setBounds(0, 0, ScreenUtil.dip2px(getActivity(), 8),
				ScreenUtil.dip2px(getActivity(), 8));
		right_txt_title.setCompoundDrawables(null, null, drawable, null);
		right_txt_title.setSingleLine(false);
		right_txt_title.setLines(2);
		right_txt_title.setEllipsize(TruncateAt.END);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.triangle);
				drawable.setBounds(0, 0, ScreenUtil.dip2px(getActivity(), 8),
						ScreenUtil.dip2px(getActivity(), 8));
				right_txt_title
						.setCompoundDrawables(null, null, drawable, null);

				myPopupwindow.showAsDropDown(arg0,
						ScreenUtil.dip2px(getActivity(), -80),
						ScreenUtil.dip2px(getActivity(), 20));
			}
		});
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
		showAdapter = new CommonAdapter<MatchCityEntity>(getActivity(),
				R.layout.item_guanjunpaiming, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final MatchCityEntity t) {
				viewHolder.setText(R.id.tv_saishishuoming,
						t.getMatchCityRankName());
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				LinearLayout lin_addview = (LinearLayout) viewHolder
						.getView(R.id.lin_addview);
				lin_addview.removeAllViews();
				for (int i = 0; i < t.getPostsSignupList().size(); i++) {
					final int num = i;
					LinearLayout view = (LinearLayout) inflater.inflate(
							R.layout.item_fragment_matchrank, null);
					view.setLayoutParams(lp);
					((TextView) view.findViewById(R.id.tv_number)).setText(t
							.getPostsSignupList().get(i).getRankNum()
							+ "");
					LinearLayout lin_head = (LinearLayout) view
							.findViewById(R.id.lin_head);
					MyRoundDraweeView iv_head = new MyRoundDraweeView(
							getActivity());
					lin_head.removeAllViews();
					lin_head.addView(iv_head, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					iv_head.setUrlOfImage(t.getPostsSignupList().get(i)
							.getClientThumbHeadPicUrl());
					((TextView) view.findViewById(R.id.tv_name)).setText(t
							.getPostsSignupList().get(i).getNickName());
					((TextView) view.findViewById(R.id.tv_position)).setText(t
							.getPostsSignupList().get(i).getPositionName());
					((TextView) view.findViewById(R.id.tv_bianhao))
							.setText("编号："
									+ t.getPostsSignupList().get(i)
											.getSignupNo());
					((TextView) view.findViewById(R.id.tv_depiaoshu)).setText(t
							.getPostsSignupList().get(i).getTotalVoteNum()
							+ "");
					if (i == (t.getPostsSignupList().size() - 1)) {
						view.findViewById(R.id.line).setVisibility(
								View.INVISIBLE);
					}
					view.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(getActivity(),
									NewWorkDetailActivity.class);
							intent.putExtra("SignupId", t.getPostsSignupList()
									.get(num).getSignupId());
							startActivity(intent);
						}
					});
					lin_addview.addView(view);
				}
				viewHolder.getView(R.id.layout_title).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent2 = new Intent(getActivity(),
										ShowRankingActivity.class);
								intent2.putExtra("postsId", t.getPostsId());
								intent2.putExtra("title",
										t.getMatchCityRankName());
								// intent2.putExtra("tabNum", 3);
								// Bundle bundle = new Bundle();
								// bundle.putSerializable("matchCity", t);
								// intent2.putExtras(bundle);
								startActivity(intent2);
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
	}

	protected void upRefreshData() {
		page++;
		getRainkingData();
	}

	protected void downRefreshData() {
		page = 1;
		showlist.clear();
		// cityList.clear();
		getRainkingData();
	}

	@Override
	public void initValue() {
		Bundle bundle = getArguments();
		matchId = bundle.getInt("matchId");
		// getOpenedCity();
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", "reqCodeOnee" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("matchCityRankSignup") + "").length() > 2) {
				mPullRefreshListView.onRefreshComplete();
				List<MatchCityEntity> listData = (List<MatchCityEntity>) JsonUtil
						.jsonToList(resp.getBody().get("matchCityRankSignup")
								.toString(),
								new TypeToken<List<MatchCityEntity>>() {
								}.getType());
				List<MatchCityEntity> listData1 = (List<MatchCityEntity>) JsonUtil
						.jsonToList(resp.getBody().get("matchCity").toString(),
								new TypeToken<List<MatchCityEntity>>() {
								}.getType());
				if (!Tool.isEmpty(listData)) {
					showlist.addAll(listData);
				}
				if (!Tool.isEmpty(listData1)) {
					showlist1.addAll(listData1);
				}

				if (Tool.isEmpty(cityList)) {
					cityList.add(new PopupData("全部", 0));
					cityCodeList.add(-1);
					for (int i = 0; i < listData1.size(); i++) {
						cityList.add(new PopupData(showlist1.get(i)
								.getMatchCityName(), 0));
						cityCodeList.add(showlist1.get(i).getMatchCityCode());
					}

				}

			}

			showAdapter.notifyDataSetChanged();
			break;

		case reqCodeTwo:
			break;
		}

		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("请求失败");
			break;

		default:
			break;
		}
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

}
