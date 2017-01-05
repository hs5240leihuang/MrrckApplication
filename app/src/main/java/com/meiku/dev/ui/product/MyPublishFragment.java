package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ProductInfoEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.ViewHolder;

/** 我的发布 */
public class MyPublishFragment extends BaseFragment {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<ProductInfoEntity> showAdapter;
	private List<ProductInfoEntity> showlist = new ArrayList<ProductInfoEntity>();
	private int page = 1;
	private final int reqCodeFive = 500;
	private ProductInfoEntity productInfoEntity = new ProductInfoEntity();
	private LocalReceiver localReceiver;
	private LocalBroadcastManager localBroadcastManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_receiveintent, null);
		initPullListView();
		registerReceiver();
		return layout_view;
	}

	@Override
	public void initValue() {
		downRefreshData(true);
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
						downRefreshData(true);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						upRefreshData();
					}
				});

		showAdapter = new CommonAdapter<ProductInfoEntity>(getActivity(),
				R.layout.item_my_publish, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final ProductInfoEntity t) {
				viewHolder.setImage(R.id.iv_img, t.getClientPosterThum());
				viewHolder.setText(R.id.tv_name, t.getProductName());
				viewHolder.setText(R.id.tv_province,
						"招商省份：" + t.getProvinceNames());
				viewHolder.setText(R.id.tv_effectivelength,
						"有效时长：" + t.getBuyMonths() + "个月");
				if (!Tool.isEmpty(t.getBuyStartTime())
						&& !Tool.isEmpty(t.getBuyEndTime())) {
					viewHolder.setText(R.id.tv_effectivetime,
							t.getBuyStartTime() + " 至 " + t.getBuyEndTime());
				} else {
					viewHolder.setText(R.id.tv_effectivetime, "");
				}
				viewHolder.setText(R.id.tv_browse, "浏览：" + t.getViewNum());
				if (t.getTopFlag() == 1 && t.getPublicFlag() == 1) {
					viewHolder.setText(R.id.tv_state, "状态：开启、置顶");
				} else if (t.getTopFlag() == 1) {
					viewHolder.setText(R.id.tv_state, "状态：置顶");
				} else if (t.getPublicFlag() == 1) {
					viewHolder.setText(R.id.tv_state, "状态：开启");
				} else if (t.getPublicFlag() == 0) {
					viewHolder.setText(R.id.tv_state, "状态：关闭");
				} else {
					viewHolder.setText(R.id.tv_state, "");
				}

				if (t.getPublicFlag() == 1) {
					((Button) viewHolder.getView(R.id.btn_close)).setText("关闭");
					viewHolder.getView(R.id.btn_close).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									ReqBase req = new ReqBase();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("userId", AppContext.getInstance()
											.getUserInfo().getUserId());
									map.put("productId", t.getProductId());
									LogUtil.e(map + "");
									req.setHeader(new ReqHead(
											AppConfig.BUSINESS_PRODUCT_CLOSE_OUTLINE));
									req.setBody(JsonUtil.Map2JsonObj(map));
									httpPost(reqCodeFour,
											AppConfig.PRODUCT_REQUEST_MAPPING,
											req);
								}
							});
					viewHolder.getView(R.id.btn_top).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (DateTimeUtil.compareDate(t
											.getTopEndTime())) {
										ToastUtil.showShortToast("该产品已经处于置顶状态");
									} else {
										Intent i = new Intent(getActivity(),
												BuyTopActivity.class);
										i.putExtra("productId",
												t.getProductId() + "");
										i.putExtra("productName",
												t.getProductName());
										startActivity(i);
									}
								}
							});
				} else {
					((Button) viewHolder.getView(R.id.btn_close)).setText("开启");
					viewHolder.getView(R.id.btn_close).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									productInfoEntity = t;
									ReqBase req = new ReqBase();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("userId", AppContext.getInstance()
											.getUserInfo().getUserId());
									map.put("productId", t.getProductId());
									LogUtil.e(map + "");
									req.setHeader(new ReqHead(
											AppConfig.BUSINESS_PRODUCT_OPEN_ONLINE));
									req.setBody(JsonUtil.Map2JsonObj(map));
									httpPost(reqCodeFive,
											AppConfig.PRODUCT_REQUEST_MAPPING,
											req);
								}
							});
					viewHolder.getView(R.id.btn_top).setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									final CommonDialog commonDialog = new CommonDialog(
											getActivity(), "提示",
											"抱歉，该产品未开启,请先开启产品。", "确定");
									commonDialog
											.setClicklistener(new ClickListenerInterface() {

												@Override
												public void doConfirm() {
													// Intent i = new
													// Intent(getActivity(),PayProductActivity.class);
													// i.putExtra("productId",
													// t.getProductId());
													// i.putExtra("productName",
													// t.getProductName());
													// i.putExtra("provinceNames",
													// t.getProvinceNames());
													// i.putExtra("provinceCodes",
													// t.getProvinceCodes());
													// startActivity(i);
													commonDialog.dismiss();
												}

												@Override
												public void doCancel() {
													commonDialog.dismiss();
												}
											});
									commonDialog.show();

								}
							});
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										ProductDetailActivity.class);
								i.putExtra("productId", t.getProductId() + "");
								startActivity(i);
							}
						});
				viewHolder.getView(R.id.tv_add_pro).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										EditProductActivity.class);
								i.putExtra("productId", t.getProductId());
								i.putExtra("FLAG", 2);
								startActivity(i);
							}
						});
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										getActivity(), "提示", "是否删除该产品？", "确定",
										"取消");
								commonDialog
										.setClicklistener(new ClickListenerInterface() {

											@Override
											public void doConfirm() {
												ReqBase req = new ReqBase();
												Map<String, Object> map = new HashMap<String, Object>();
												map.put("userId", AppContext
														.getInstance()
														.getUserInfo()
														.getUserId());
												map.put("productId",
														t.getProductId());
												LogUtil.e(map + "");
												req.setHeader(new ReqHead(
														AppConfig.BUSINESS_PRODUCT_DELECT));
												req.setBody(JsonUtil
														.Map2JsonObj(map));
												httpPost(
														reqCodeThree,
														AppConfig.PRODUCT_REQUEST_MAPPING,
														req);
												commonDialog.dismiss();
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});
								commonDialog.show();
							}
						});
				Button btn_renew = viewHolder.getView(R.id.btn_renew);
				if (t.getPayRecordFlag() == 1) {
					btn_renew.setBackgroundResource(R.drawable.xufeibg);
				} else {
					btn_renew.setBackgroundResource(R.drawable.fukuanbg);
				}
				btn_renew.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (t.getPayRecordFlag() == 1) {
							Intent i = new Intent(getActivity(),
									XuFeiActivity.class);
							i.putExtra("productId", t.getProductId());
							i.putExtra("productName", t.getProductName());
							i.putExtra("buyEndTime", t.getBuyEndTime());
							i.putExtra("provinceNames", t.getProvinceNames());
							i.putExtra("provinceCodes", t.getProvinceCodes());
							startActivity(i);
						} else {
							Intent intent_pay = new Intent(getActivity(),
									PayProductActivity.class);
							intent_pay.putExtra("productId", t.getProductId());
							intent_pay.putExtra("productName",
									t.getProductName());
							intent_pay.putExtra("provinceNames",
									t.getProvinceNames());
							intent_pay.putExtra("provinceCodes",
									t.getProvinceCodes());
							startActivity(intent_pay);
						}
					}
				});
				viewHolder.getView(R.id.btn_edit).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(getActivity(),
										EditProductActivity.class);
								i.putExtra("productId", t.getProductId());
								i.putExtra("FLAG", 1);
								startActivity(i);
							}
						});
				viewHolder.getView(R.id.btn_update).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								ReqBase req = new ReqBase();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("userId", AppContext.getInstance()
										.getUserInfo().getUserId());
								map.put("productId", t.getProductId());
								LogUtil.e(map + "");
								req.setHeader(new ReqHead(
										AppConfig.BUSINESS_PRODUCT_REFRESH));
								req.setBody(JsonUtil.Map2JsonObj(map));
								httpPost(reqCodeTwo,
										AppConfig.PRODUCT_REQUEST_MAPPING, req);

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

	protected void downRefreshData(boolean isShowDialog) {
		showlist.clear();
		page = 1;
		getData(isShowDialog);
	}

	// 请求排名数据
	public void getData(boolean isShowDialog) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_MY_PUBLIC));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, req,
				isShowDialog);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("kkk", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("productInfo").toString().length() > 2) {
				List<ProductInfoEntity> listData = (List<ProductInfoEntity>) JsonUtil
						.jsonToList(resp.getBody().get("productInfo")
								.toString(),
								new TypeToken<List<ProductInfoEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
				ToastUtil.showShortToast("无更多产品");
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("刷新成功");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("删除成功");
			downRefreshData(true);
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("关闭成功");
			downRefreshData(true);
			break;
		case reqCodeFive:
			ToastUtil.showShortToast("开启成功");
			downRefreshData(true);
			break;
		default:
			break;
		}
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk", "######" + resp.getHeader().getRetStatus().toString());
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			getActivity().finish();
			break;
		case reqCodeOne:
			if (null != mPullRefreshListView) {
				mPullRefreshListView.onRefreshComplete();
			}
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("刷新失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("删除失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("关闭失败");
			break;
		case reqCodeFive:
			if (resp.getHeader().getRetStatus().equals("8")) {
				final CommonDialog commonDialog = new CommonDialog(
						getActivity(), "提示", "该产品已过期，请续费", "确定", "取消");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						Intent i = new Intent(getActivity(),
								XuFeiActivity.class);
						startActivity(i);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			} else if (resp.getHeader().getRetStatus().equals("9")) {
				final CommonDialog commonDialog = new CommonDialog(
						getActivity(), "提示", "抱歉，该产品未付款,点击确定去付款。", "确定", "取消");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						Intent i = new Intent(getActivity(),
								PayProductActivity.class);
						i.putExtra("productId",
								productInfoEntity.getProductId());
						i.putExtra("productName",
								productInfoEntity.getProductName());
						i.putExtra("provinceNames",
								productInfoEntity.getProvinceNames());
						i.putExtra("provinceCodes",
								productInfoEntity.getProvinceCodes());
						startActivity(i);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			} else if (resp.getHeader().getRetStatus().equals("6")) {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage());
				downRefreshData(true);
			} else {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			}

			break;
		default:
			break;
		}

	}

	class LocalReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_MYPRODUCT.equals(intent.getAction())) {
				downRefreshData(false);
			}
		}
	}

	private void registerReceiver() {
		localBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadCastAction.ACTION_MYPRODUCT);
		localReceiver = new LocalReceiver();
		localBroadcastManager.registerReceiver(localReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		localBroadcastManager.unregisterReceiver(localReceiver);
	}

}
