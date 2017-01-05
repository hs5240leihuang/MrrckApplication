package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PostsSignupCollectEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.myshow.WorkDetailNewActivity;
import com.meiku.dev.utils.DoEditObs;
import com.meiku.dev.utils.DoEditObs.DoEditListener;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 我的秀场-2我的收藏
 * 
 */
public class MyCollectFragment extends BaseFragment implements OnClickListener {
	private View layout_view;
	private PullToRefreshGridView pull_refreshGV;
	private LinearLayout editlayout;
	private TextView btnShare, btnSelectAll, btnDelete;
	private int page;
	private String signupCollectIds = "";
	private CommonAdapter<PostsSignupCollectEntity> showAdapter;
	private List<PostsSignupCollectEntity> showlist = new ArrayList<PostsSignupCollectEntity>();
	protected boolean isEditModel;
	private boolean isSelectAll = false;
	protected boolean isUpRefresh;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_mycollect, null);
		initView();
		return layout_view;
	}

	private void initView() {
		initPullView();
		initBottomEditBar();
		downRefreshData();
	}

	private void initBottomEditBar() {
		editlayout = (LinearLayout) layout_view.findViewById(R.id.editlayout);
		btnShare = (TextView) layout_view.findViewById(R.id.btnShare);
		btnSelectAll = (TextView) layout_view.findViewById(R.id.btnSelectAll);
		btnDelete = (TextView) layout_view.findViewById(R.id.btnDelete);
		btnShare.setOnClickListener(this);
		btnSelectAll.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		DoEditObs.getInstance().registerListener(this.getClass().getName(),
				new DoEditListener() {

					@Override
					public void doEdit(boolean isSelectModel) {
						isShowBottomEditBar(isSelectModel);
						isEditModel = isSelectModel;
						showAdapter.notifyDataSetChanged();
					}

					@Override
					public void doRefresh() {
						downRefreshData();
					}

				});
	}

	private void initPullView() {
		pull_refreshGV = (PullToRefreshGridView) layout_view
				.findViewById(R.id.pull_refresh_grid);
		pull_refreshGV.setMode(PullToRefreshGridView.Mode.BOTH);
		pull_refreshGV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						isUpRefresh = true;
						upRefreshData();
					}
				});
		showAdapter = new CommonAdapter<PostsSignupCollectEntity>(
				getActivity(), R.layout.item_grid_show, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final PostsSignupCollectEntity t) {
				ImageView iv_select = viewHolder.getView(R.id.iv_select);
				if (t.isSelected()) {
					iv_select
							.setBackgroundResource(R.drawable.icon_xuanzhong_circle);
				} else {
					iv_select
							.setBackgroundResource(R.drawable.icon_weixuanzhong_circle);
				}
				final boolean isCanSaiWork = t.getWorksFlag() == 1;// 0:普通作品,1:比赛作品
				final int position = viewHolder.getPosition();
				viewHolder.setText(R.id.TV_name, t.getName());
				viewHolder.setText(R.id.TV_job, t.getNickName());
				viewHolder.setText(R.id.TV_leftbottom, "");

				// viewHolder.setImageWithNewSize(R.id.IV_head,
				// t.getClientHeadPicUrl(), 150, 150);
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				MyRoundDraweeView IV_head = new MyRoundDraweeView(getActivity());
				lin_head.removeAllViews();
				lin_head.addView(IV_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				IV_head.setUrlOfImage(t.getClientHeadPicUrl());
				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						getActivity());
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientThumbFileUrl());
				View view_righttop = viewHolder.getView(R.id.IV_righttop);
				if (isCanSaiWork) {
					viewHolder.setImage(R.id.IV_piao, R.drawable.piao);
					TextView TV_piaoNum = viewHolder.getView(R.id.TV_piaoNum);
					TV_piaoNum.setText(t.getTotalVoteNum() + "");
					TV_piaoNum.setTextColor(Color.parseColor("#FF5073"));
				} else {
					viewHolder.setImage(R.id.IV_piao, R.drawable.collect_off);
					TextView TV_piaoNum = viewHolder.getView(R.id.TV_piaoNum);
					TV_piaoNum.setText(t.getLikeNum() + "");
					TV_piaoNum.setTextColor(Color.parseColor("#999999"));
				}
				// if (t.getHotFlag() == 1) {// 0:不是 1:是
				// view_righttop.setBackgroundResource(R.drawable.icon_hot);
				// view_righttop.setVisibility(View.VISIBLE);
				// } else
				if (isCanSaiWork) {
					view_righttop
							.setBackgroundResource(R.drawable.cansaizuopin);
					view_righttop.setVisibility(View.VISIBLE);
				} else {
					view_righttop.setBackgroundDrawable(null);
					view_righttop.setVisibility(View.GONE);
				}

				if (isEditModel) {
					iv_select.setVisibility(View.VISIBLE);
					view_righttop.setVisibility(View.GONE);
				} else {
					view_righttop.setVisibility(View.VISIBLE);
					iv_select.setVisibility(View.GONE);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (isEditModel) {
									showlist.get(position).setSelected(
											!t.isSelected());
									showAdapter.notifyDataSetChanged();
									return;
								}
								if (isCanSaiWork) {// 是否参赛作品
									startActivity(new Intent(getActivity(),
											NewWorkDetailActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								} else {
									startActivity(new Intent(getActivity(),
											WorkDetailNewActivity.class)
											.putExtra("SignupId",
													t.getSignupId()));
								}
							}
						});
			}
		};
		pull_refreshGV.setAdapter(showAdapter);
	}

	/**
	 * 获取收藏的数据
	 */
	private void getCollectData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MINE_MYCOLLECT));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	/**
	 * 删除选择的收藏
	 */
	private void deleteSelectedCollect() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("signupCollectIds", signupCollectIds);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MINE_DELETE_MYCOLLECT));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	}

	/**
	 * 是否显示底部的编辑条
	 */
	private void isShowBottomEditBar(boolean show) {
		editlayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected void upRefreshData() {
		page++;
		getCollectData();
	}

	protected void downRefreshData() {
		page = 1;
		showlist.clear();
		getCollectData();
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("postsSignupCollect") + "").length() > 2) {
				List<PostsSignupCollectEntity> data = (List<PostsSignupCollectEntity>) JsonUtil
						.jsonToList(
								resp.getBody().get("postsSignupCollect")
										.toString(),
								new TypeToken<List<PostsSignupCollectEntity>>() {
								}.getType());
				if (!Tool.isEmpty(data)) {
					showlist.addAll(data);
				}
			} else {
				if (isUpRefresh) {
					ToastUtil.showShortToast("无更多收藏");
				} else {
					ToastUtil.showShortToast("暂无收藏");
				}
			}
			showAdapter.notifyDataSetChanged();
			pull_refreshGV.onRefreshComplete();
			isUpRefresh = false;
			if (Tool.isEmpty(showlist)) {
				isSelectAll = false;
				btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
			}
			break;

		case reqCodeTwo:
			downRefreshData();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			if (null != pull_refreshGV) {
				pull_refreshGV.onRefreshComplete();
			}
			ToastUtil.showShortToast("获取收藏数据失败！");
			break;
		case reqCodeTwo:
			isUpRefresh = false;
			downRefreshData();
			ToastUtil.showShortToast("删除失败！");
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnShare:

			break;
		case R.id.btnSelectAll:
			isSelectAll = !isSelectAll;
			btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
			for (int i = 0, size = showlist.size(); i < size; i++) {
				showlist.get(i).setSelected(isSelectAll);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case R.id.btnDelete:
			signupCollectIds = "";
			for (int i = 0, size = showlist.size(); i < size; i++) {
				if (showlist.get(i).isSelected()) {
					signupCollectIds += "," + showlist.get(i).getId();
				}
			}
			if (signupCollectIds.length() > 1) {
				signupCollectIds = signupCollectIds.substring(1,
						signupCollectIds.length());
				deleteSelectedCollect();
			} else {
				ToastUtil.showShortToast("请勾选需要删除的收藏");
			}
			break;
		default:
			break;
		}
	}

}
