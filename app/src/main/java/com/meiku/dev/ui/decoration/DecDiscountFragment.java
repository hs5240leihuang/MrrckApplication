package com.meiku.dev.ui.decoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCompanyFavourContentEntity;
import com.meiku.dev.bean.DecorateCompanyFavourEntity;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的发布-->装修优惠
 * 
 */
public class DecDiscountFragment extends BaseFragment {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateCompanyFavourEntity> showAdapter;
	private List<DecorateCompanyFavourEntity> showlist = new ArrayList<DecorateCompanyFavourEntity>();
	private int page = 1;
	private Button btn_publish;
	private LinearLayout lin_buju;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.activity_decdiscountfragment,
				null);
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
		btn_publish = (Button) layout_view.findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity()
						.startActivityForResult(
								new Intent(getActivity(),
										PublicDecorationConcessions.class),
								reqCodeFour);
			}
		});
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("000001", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<DecorateCompanyFavourEntity> listData = (List<DecorateCompanyFavourEntity>) JsonUtil
						.jsonToList(
								resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateCompanyFavourEntity>>() {
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
			downRefreshData();
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
		showAdapter = new CommonAdapter<DecorateCompanyFavourEntity>(
				getActivity(), R.layout.item_decdiscountfragment, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCompanyFavourEntity t) {
				final Button isopen = viewHolder.getView(R.id.btn_isopen);
				final int position = viewHolder.getPosition();
				if (t.getIsOpen() == 0) {
					isopen.setText("关闭");
				} else {
					isopen.setText("开启");
				}
				String name = "";
				for (int i = 0, size = t.getFavourContentList().size(); i < size; i++) {
					name += t.getFavourContentList().get(i).getName();
					if (i != size - 1) {
						name += "\n";
					}
				}

				viewHolder.setText(R.id.tv_youhui, name);
				viewHolder.getView(R.id.btn_edit).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(getActivity(),
										PublicDecorationConcessions.class);
								intent.putExtra("flag", 1);
								intent.putExtra("id", t.getId());
								Bundle bundle = new Bundle();
								List<DecorateCompanyFavourContentEntity> wkList = new ArrayList<DecorateCompanyFavourContentEntity>();
								wkList.addAll(t.getFavourContentList());
								if (t.getFavourContentList().size() < 4) {
									for (int i = 0, size = 4 - t
											.getFavourContentList().size(); i < size; i++) {
										wkList.add(new DecorateCompanyFavourContentEntity());
									}
								}
								bundle.putSerializable("youhui",
										(Serializable) wkList);
								intent.putExtras(bundle);
								getActivity().startActivityForResult(intent,
										reqCodeFour);
							}
						});
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {

								final CommonDialog commonDialog = new CommonDialog(
										getActivity(), "提示", "是否删除该优惠?", "确定",
										"取消");
								commonDialog.show();
								commonDialog
										.setClicklistener(new CommonDialog.ClickListenerInterface() {
											@Override
											public void doConfirm() {
												Delete(t.getId());
												commonDialog.dismiss();
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});

							}
						});
				isopen.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (t.getIsOpen() == 0) {
							Isopen(t.getId(), 1);
						} else {
							Isopen(t.getId(), 0);
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
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300019));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 删除请求
	public void Delete(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("deleteType", 4);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.ZX_300024));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	// 开启关闭请求
	public void Isopen(int id, int isOpen) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("type", 3);
		map.put("isOpen", isOpen);
		map.put("sourceId", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300020));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_DECORATION, req);
		Log.d("wangke", isOpen + "");
	}

}
