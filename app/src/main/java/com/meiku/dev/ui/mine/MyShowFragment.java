package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
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
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * 我的秀场-1我的秀场
 * 
 */
public class MyShowFragment extends BaseFragment implements OnClickListener {

	private View layout_view;
	private PullToRefreshGridView pull_refreshGV;
	private LinearLayout editlayout;
	private TextView btnShare, btnSelectAll, btnDelete;
	private int page;
	protected boolean isEditModel;
	protected BaseAdapter showAdapter;
	protected boolean isUpRefresh;
	private boolean isSelectAll;
	private String signupIds;
	private List<ShowPostsSignupEntity> showlist = new ArrayList<ShowPostsSignupEntity>();
	private List<HashMap<String, Object>> Indexlist = new ArrayList<HashMap<String, Object>>();
	private int userId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_myshow, null);
		initView();
		return layout_view;
	}

	private void initView() {
		userId = getArguments().getInt("userId");
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
		showAdapter = new CommonAdapter<ShowPostsSignupEntity>(getActivity(),
				R.layout.item_grid_myshow, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final ShowPostsSignupEntity t) {
				final int position = viewHolder.getPosition();
				LinearLayout outside = viewHolder.getView(R.id.outside);
				View view_righttop = viewHolder.getView(R.id.IV_righttop);
				if (Tool.isEmpty(t.getCreateDate())) {
					outside.setVisibility(View.INVISIBLE);
				} else {
					outside.setVisibility(View.VISIBLE);
					RelativeLayout linelou = viewHolder.getView(R.id.linelou);
					ImageView timeIcon = viewHolder.getView(R.id.timeIcon);
					TextView time = viewHolder.getView(R.id.time);
					time.setText(t.getCreateDate().subSequence(0, 10));
					if (!Tool.isEmpty(t.getCreateDate())
							&& position > 0
							&& !Tool.isEmpty(showlist.get(position - 1)
									.getCreateDate())
							&& showlist
									.get(position - 1)
									.getCreateDate()
									.subSequence(0, 10)
									.equals(t.getCreateDate()
											.subSequence(0, 10))) {
						if (position % 2 == 0) {
							linelou.setVisibility(View.VISIBLE);
						} else {
							linelou.setVisibility(View.INVISIBLE);
						}
						time.setVisibility(View.INVISIBLE);
						timeIcon.setVisibility(View.GONE);
					} else {
						time.setVisibility(View.VISIBLE);
						linelou.setVisibility(View.VISIBLE);
						timeIcon.setVisibility(View.VISIBLE);
					}
					viewHolder.setImage(R.id.workImg, t.getClientFileUrl());
					viewHolder.setText(R.id.btnPinlun, t.getCommentNum() + "");
					TextView tv_zan = viewHolder.getView(R.id.btnzan);
					final boolean isCanSaiWork = t.getWorksFlag() == 1;// 0:普通作品,1:比赛作品
					if (!isCanSaiWork) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.ms_dianzan);
						drawable.setBounds(0, 0,
								ScreenUtil.dip2px(getActivity(), 15),
								ScreenUtil.dip2px(getActivity(), 15));
						tv_zan.setCompoundDrawables(drawable, null, null, null);
						viewHolder.setText(R.id.btnzan, t.getLikeNum() + "");
					} else {

						Drawable drawable = getResources().getDrawable(
								R.drawable.piao);
						drawable.setBounds(0, 0,
								ScreenUtil.dip2px(getActivity(), 20),
								ScreenUtil.dip2px(getActivity(), 20));
						tv_zan.setCompoundDrawables(drawable, null, null, null);
						viewHolder.setText(R.id.btnzan, t.getTotalVoteNum() + "");
					}
					ImageView iv_select = viewHolder.getView(R.id.iv_select);
					if (isCanSaiWork) {
						view_righttop
								.setBackgroundResource(R.drawable.cansaizuopin);
						view_righttop.setVisibility(View.VISIBLE);
					} else {
						view_righttop.setBackgroundDrawable(null);
						view_righttop.setVisibility(View.GONE);
					}
					if (isEditModel && !isCanSaiWork) {
						iv_select.setVisibility(View.VISIBLE);
						view_righttop.setVisibility(View.GONE);
					} else {
						iv_select.setVisibility(View.GONE);
						view_righttop.setVisibility(View.VISIBLE);
					}
					if (t.isSelected()) {
						iv_select
								.setBackgroundResource(R.drawable.icon_xuanzhong_circle);
					} else {
						iv_select
								.setBackgroundResource(R.drawable.icon_weixuanzhong_circle);
					}
					viewHolder.getConvertView().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (isEditModel) {
										if (isCanSaiWork) {
											ToastUtil
													.showShortToast("参赛作品，不可删除！");
											return;
										}
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
			}
		};
		pull_refreshGV.setAdapter(showAdapter);
	}

	/**
	 * 是否显示底部的编辑条
	 */
	private void isShowBottomEditBar(boolean show) {
		editlayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected void upRefreshData() {
		page++;
		Indexlist.clear();
		getMyShowData();
	}

	protected void downRefreshData() {
		page = 1;
		showlist.clear();
		Indexlist.clear();
		getMyShowData();
	}

	/**
	 * 获取我的秀场数据
	 */
	private void getMyShowData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MINE_MYSHOW));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	/**
	 * 删除选择的收藏
	 */
	private void deleteSelectedMyWorks() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("signupIds", signupIds);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MINE_DELETE_MYSHOW));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
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
			if ((resp.getBody().get("postsSignup") + "").length() > 2) {
				List<ShowPostsSignupEntity> data = (List<ShowPostsSignupEntity>) JsonUtil
						.jsonToList(resp.getBody().get("postsSignup")
								.toString(),
								new TypeToken<List<ShowPostsSignupEntity>>() {
								}.getType());
				if (!Tool.isEmpty(data)) {
					showlist.addAll(data);
					LogUtil.d("hl", "data.size()=" + data.size());
					for (int i = 0, size = showlist.size(); i < size; i++) {
						if (i == 0) {
							String currentTime = showlist.get(i)
									.getCreateDate().substring(0, 10);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("TIME", currentTime);
							map.put("INDEX", 0);
							Indexlist.add(map);
						} else {
							if (!showlist
									.get(i)
									.getCreateDate()
									.subSequence(0, 10)
									.equals(showlist.get(i - 1).getCreateDate()
											.subSequence(0, 10))) {
								HashMap<String, Object> map2 = new HashMap<String, Object>();
								map2.put("TIME", showlist.get(i)
										.getCreateDate().subSequence(0, 10));
								map2.put("INDEX", i);
								Indexlist.add(map2);
							}
						}
					}
					int k = 0;
					for (int j = 0, indexSize = Indexlist.size(); j < indexSize; j++) {
						if (j == 0) {

						} else {
							int currentIndex = (Integer) Indexlist.get(j).get(
									"INDEX");
							int distense = currentIndex
									- ((Integer) Indexlist.get(j - 1).get(
											"INDEX"));
							if (distense % 2 == 1) {
								LogUtil.d("hl", "empty——currentIndex="
										+ currentIndex);
								showlist.add(currentIndex + k,
										new ShowPostsSignupEntity());
								k++;
							}
						}
					}
				}
			} else {
				if (isUpRefresh) {
					ToastUtil.showShortToast("无更多数据");
				} else {
					ToastUtil.showShortToast("暂无数据");
				}
			}
			showAdapter.notifyDataSetChanged();
			
			isUpRefresh = false;
			if (Tool.isEmpty(showlist)) {
				isSelectAll = false;
				btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
			}
			pull_refreshGV.onRefreshComplete();
			break;

		case reqCodeTwo:
			downRefreshData();
			break;
		}
		
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			getActivity().finish();
			break;
		case reqCodeOne:
			if (null!=pull_refreshGV) {
				pull_refreshGV.onRefreshComplete();
			}
			ToastUtil.showShortToast("获取秀场数据失败！");
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
				if (null != showlist.get(i).getWorksFlag()
						&& showlist.get(i).getWorksFlag() == 0) {// 0:普通作品,1:比赛作品
					showlist.get(i).setSelected(isSelectAll);
				}
			}
			showAdapter.notifyDataSetChanged();
			break;
		case R.id.btnDelete:
			signupIds = "";
			for (int i = 0, size = showlist.size(); i < size; i++) {
				if (showlist.get(i).isSelected()
						&& !Tool.isEmpty(showlist.get(i).getCreateDate())) {
					signupIds += "," + showlist.get(i).getSignupId();
				}
			}
			if (signupIds.length() > 1) {
				signupIds = signupIds.substring(1, signupIds.length());
				deleteSelectedMyWorks();
			} else {
				ToastUtil.showShortToast("请勾选需要删除的作品");
			}
			break;
		default:
			break;
		}
	}

}
