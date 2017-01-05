package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateOrderCityEntity;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的发布-->发布的城市
 * 
 */
public class MyDecPublichCityActivity extends BaseFragment {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateOrderCityEntity> showAdapter;
	private List<DecorateOrderCityEntity> showlist = new ArrayList<DecorateOrderCityEntity>();
	private int page = 1;
	private NewPopupwindows newPopupwindows;
	private List<PopupData> list = new ArrayList<PopupData>();
	private LinearLayout lin_buju;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(
				R.layout.activity_mydecpubliccityfragment, null);
		layout_view.findViewById(R.id.btn_publish).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						getActivity().startActivityForResult(
								new Intent(getActivity(),
										OpenPermissionActivity.class).putExtra(
										"usetype", 1), reqCodeTwo);
					}
				});
		initPullListView();
		return layout_view;

	}

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
	public void initValue() {
		lin_buju = (LinearLayout) layout_view.findViewById(R.id.lin_buju);
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("000003", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<DecorateOrderCityEntity> listData = (List<DecorateOrderCityEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateOrderCityEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
			}
			if (showlist.size() > 0) {
				mPullRefreshListView.setVisibility(View.VISIBLE);
				lin_buju.setVisibility(View.GONE);
			} else {
				mPullRefreshListView.setVisibility(View.GONE);
				lin_buju.setVisibility(View.VISIBLE);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			downRefreshData();
			break;
		case reqCodeThree:
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			break;
		case reqCodeFour:

			break;
		default:
			break;

		}
		mPullRefreshListView.onRefreshComplete();

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
		case reqCodeThree:
		case reqCodeFour:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						getActivity(), "提示", resp.getHeader().getRetMessage(),
						"确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
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
		showAdapter = new CommonAdapter<DecorateOrderCityEntity>(getActivity(),
				R.layout.item_mydecpubliccityfragment, showlist) {

			@Override
			public void convert(final ViewHolder viewHolder,
					final DecorateOrderCityEntity t) {
				Button shuaxin = viewHolder.getView(R.id.btn_shuaxin);
				Button zhiding = viewHolder.getView(R.id.btn_zhiding);
				Button diandiandian = viewHolder.getView(R.id.diandaindian);
				Button xufei = viewHolder.getView(R.id.btn_xufei);
				if (t.getExpireFlag() == 0) {
					viewHolder.getView(R.id.img_guoqi).setVisibility(
							View.INVISIBLE);
					shuaxin.setEnabled(true);
					zhiding.setEnabled(true);
					diandiandian.setEnabled(true);
					xufei.setText("续费");
					shuaxin.setBackgroundResource(R.drawable.btn_three_wk);
					zhiding.setBackgroundResource(R.drawable.btn_three_wk);
					diandiandian.setBackgroundResource(R.drawable.btn_three_wk);
					// shuaxin.setTextColor(getResources().getColor(
					// R.drawable.text_three_wk));
					// zhiding.setTextColor(getResources().getColor(
					// R.drawable.text_three_wk));
					// diandiandian.setTextColor(getResources().getColor(
					// R.drawable.text_three_wk));
					shuaxin.setAlpha((float) 1);
					zhiding.setAlpha((float) 1);
					diandiandian.setAlpha((float) 1);
				} else {
					viewHolder.getView(R.id.img_guoqi).setVisibility(
							View.VISIBLE);
					shuaxin.setEnabled(false);
					zhiding.setEnabled(false);
					diandiandian.setEnabled(false);
					xufei.setText("付费");
					shuaxin.setBackgroundResource(R.drawable.shape_no);
					zhiding.setBackgroundResource(R.drawable.shape_no);
					diandiandian.setBackgroundResource(R.drawable.shape_no);
					// shuaxin.setTextColor(Color.parseColor("#AAAAAA"));
					// zhiding.setTextColor(Color.parseColor("#AAAAAA"));
					// diandiandian.setTextColor(Color.parseColor("#AAAAAA"));
					shuaxin.setAlpha((float) 0.3);
					zhiding.setAlpha((float) 0.3);
					diandiandian.setAlpha((float) 0.3);
				}
				String cityname = "";
				for (int i = 0, size = t.getDecorateOrderCityContentList()
						.size(); i < size; i++) {
					if (t.getDecorateOrderCityContentList().get(i).getTopFlag() == 0) {
						cityname += t.getDecorateOrderCityContentList().get(i)
								.getCityName()
								+ ",";

					}

				}
				LinearLayout lin_picture = viewHolder.getView(R.id.lin_img);
				lin_picture.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MySimpleDraweeView imageView = new MySimpleDraweeView(
						getActivity());
				imageView.setLayoutParams(layoutParams);
				lin_picture.addView(imageView);
				imageView.setUrlOfImage(t.getClientCompanyLogo());
				if (Tool.isEmpty(cityname)) {
					viewHolder.getView(R.id.img_ding).setVisibility(View.GONE);
					viewHolder.setText(R.id.tv_ding, "");
				} else {
					viewHolder.getView(R.id.img_ding).setVisibility(
							View.VISIBLE);
					viewHolder.setText(R.id.tv_ding, Tool.checkEmptyAndDeleteEnd(cityname));
				}

				viewHolder.setText(R.id.tv_area, "发布地区：" + t.getShowCityName());
				viewHolder.setText(R.id.tv_time, "有效时长：" + t.getValidMonth()
						+ "个月");
				viewHolder.setText(
						R.id.tv_timequjian,
						"有效时间：" + t.getValidStartTime() + "-"
								+ t.getValidEndTime());
				shuaxin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Update(t.getId());
					}
				});
				viewHolder.getView(R.id.btn_xufei).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								getActivity().startActivityForResult(
										new Intent(getActivity(),
												AreaXufeiActivity.class)
												.putExtra("orderCityId",
														t.getId()), reqCodeTwo);
							}
						});
				zhiding.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (t.getOrderTopFlag() == 1) {
							ToastUtil.showShortToast(t.getOrderTopFlagMsg());
							return;
						}
						getActivity().startActivityForResult(
								new Intent(getActivity(),
										DecorationSetTopActivity.class)
										.putExtra("orderCityId", t.getId()),
								reqCodeTwo);
					}
				});
				viewHolder.getView(R.id.diandaindian).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (t.getIsOpen() == 0) {
									list.clear();
									list.add(new PopupData("关闭", 0));
									list.add(new PopupData("删除", 0));
									list.add(new PopupData("取消", 0));
								} else {
									list.clear();
									list.add(new PopupData("开启", 0));
									list.add(new PopupData("删除", 0));
									list.add(new PopupData("取消", 0));
								}
								newPopupwindows = new NewPopupwindows(
										getActivity(), list,
										new popwindowListener() {

											@Override
											public void doChoose(int position) {
												switch (position) {
												case 0:

													if (t.getIsOpen() == 0) {
														Isopen(t.getId(), 1);
														showlist.get(
																viewHolder
																		.getPosition())
																.setIsOpen(1);
														notifyDataSetChanged();
													} else {
														Isopen(t.getId(), 0);
														showlist.get(
																viewHolder
																		.getPosition())
																.setIsOpen(0);
														notifyDataSetChanged();
													}

													break;
												case 1:
													final CommonDialog commonDialog = new CommonDialog(
															getActivity(),
															"提示", "确定删除该发布地区？",
															"确定", "取消");
													commonDialog
															.setClicklistener(new CommonDialog.ClickListenerInterface() {
																@Override
																public void doConfirm() {
																	commonDialog
																			.dismiss();
																	Delete(t.getId());
																}

																@Override
																public void doCancel() {
																	commonDialog
																			.dismiss();
																}
															});
													commonDialog.show();
													break;
												case 2:

													break;
												default:
													break;
												}
											}
										}, 1);
								newPopupwindows.showAtLocation(
										layout_view.findViewById(R.id.parent),
										Gravity.BOTTOM
												| Gravity.CENTER_HORIZONTAL, 0,
										0);
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

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

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300014));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 删除请求
	public void Delete(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("deleteType", 1);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.ZX_300024));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	// 刷新请求
	public void Update(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("refreshType", 1);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300023));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_DECORATION, req);
	}

	// 开启关闭请求
	public void Isopen(int id, int isOpen) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("type", 1);
		map.put("isOpen", isOpen);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300020));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeFour, AppConfig.PUBLICK_DECORATION, req);
		Log.d("wangke", isOpen + "");
	}
}
