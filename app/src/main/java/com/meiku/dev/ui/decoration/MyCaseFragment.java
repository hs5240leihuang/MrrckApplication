package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCaseEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的发布--> 我的案例
 * 
 */
public class MyCaseFragment extends BaseFragment {
	private View layout_view;
	private Button btn_publish, btn_add;
	private LinearLayout lin_buju;
	private RelativeLayout rel_buju;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateCaseEntity> showAdapter;
	private List<DecorateCaseEntity> showlist = new ArrayList<DecorateCaseEntity>();
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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.activity_mycasefragment, null);
		initPullListView();
		return layout_view;

	}

	@Override
	public void initValue() {
		rel_buju = (RelativeLayout) layout_view.findViewById(R.id.rel_buju);
		lin_buju = (LinearLayout) layout_view.findViewById(R.id.lin_buju);
		btn_publish = (Button) layout_view.findViewById(R.id.btn_publish);
		btn_add = (Button) layout_view.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().startActivityForResult(
						new Intent(getActivity(), PublishCaseActivity.class),
						reqCodeOne);
			}
		});
		btn_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().startActivityForResult(
						new Intent(getActivity(), PublishCaseActivity.class),
						reqCodeOne);
			}
		});
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("00000", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {

				List<DecorateCaseEntity> listData = (List<DecorateCaseEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateCaseEntity>>() {
								}.getType());
				showlist.addAll(listData);

			} else {

			}
			if (showlist.size() > 0) {
				mPullRefreshListView.setVisibility(View.VISIBLE);
				lin_buju.setVisibility(View.VISIBLE);
				rel_buju.setVisibility(View.GONE);
			} else {
				mPullRefreshListView.setVisibility(View.GONE);
				lin_buju.setVisibility(View.GONE);
				rel_buju.setVisibility(View.VISIBLE);
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
		showAdapter = new CommonAdapter<DecorateCaseEntity>(getActivity(),
				R.layout.item_mycasefragment, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCaseEntity t) {
				final Button isopen = viewHolder.getView(R.id.btn_isopen);
				final int position = viewHolder.getPosition();
				if (t.getIsOpen() == 0) {
					isopen.setText("关闭");
				} else {
					isopen.setText("开启");
				}
				LinearLayout lin_picture = viewHolder.getView(R.id.lin_picture);
				lin_picture.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MySimpleDraweeView imageView = new MySimpleDraweeView(
						getActivity());
				imageView.setLayoutParams(layoutParams);
				lin_picture.addView(imageView);
				imageView.setUrlOfImage(t.getClientCaseImgFileUrlThumb());
				viewHolder.setText(R.id.tv_casename, t.getCaseName());
				// String cityname = "";
				// for (int i = 0, size = t
				// .getDecorateOrderCityContentEntityList().size(); i < size;
				// i++) {
				// cityname += t.getDecorateOrderCityContentEntityList()
				// .get(i).getCityName();
				// if (i != size - 1) {
				// cityname += ",";
				// }
				// }

				viewHolder.setText(R.id.tv_cityname,
						"发布地区:" + t.getShowCityName());
				viewHolder.setText(R.id.tv_area, "店铺面积:" + t.getShopAreaSize());
				viewHolder.setText(R.id.tv_leixing,
						"店铺类型:" + t.getShopCategoryName());
				viewHolder.setText(R.id.tv_price, "价格:" + t.getCasePice());
				viewHolder.setText(R.id.tv_name, "店铺名称:" + t.getShopName());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								getActivity()
										.startActivityForResult(
												new Intent(
														getActivity(),
														CaseDetailActivity.class)
														.putExtra(
																"shareTitle",
																t.getShareTitle())
														.putExtra(
																"shareContent",
																t.getShareContent())
														.putExtra("shareImg",
																t.getShareImg())
														.putExtra("shareUrl",
																t.getShareUrl())
														.putExtra("userId",
																t.getUserId())
														.putExtra("sourceId",
																t.getId())
														.putExtra("loadUrl",
																t.getLoadUrl()),
												reqCodeOne);
							}
						});
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										getActivity(), "提示", "确定删除该案例？", "确定",
										"取消");
								commonDialog
										.setClicklistener(new CommonDialog.ClickListenerInterface() {
											@Override
											public void doConfirm() {
												commonDialog.dismiss();
												Delete(t.getId());
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});
								commonDialog.show();
							}
						});
				viewHolder.getView(R.id.btn_update).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Update(t.getId());

							}
						});
				viewHolder.getView(R.id.btn_edit).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								getActivity().startActivityForResult(
										new Intent(getActivity(),
												PublishCaseActivity.class)
												.putExtra("flag", 1).putExtra(
														"sourceId", t.getId()),
										reqCodeOne);
							}
						});
				isopen.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (t.getIsOpen() == 0) {
							Isopen(t.getId(), 1);
							showlist.get(position).setIsOpen(1);
							notifyDataSetChanged();
						} else {
							Isopen(t.getId(), 0);
							showlist.get(position).setIsOpen(0);
							notifyDataSetChanged();
						}

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
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300012));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 删除请求
	public void Delete(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("deleteType", 2);
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
		map.put("refreshType", 2);
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
		map.put("type", 2);
		map.put("isOpen", isOpen);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300020));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeFour, AppConfig.PUBLICK_DECORATION, req);
		Log.d("wangke", isOpen + "");
	}
}
