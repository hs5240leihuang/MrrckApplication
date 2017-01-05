package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateNeedEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.ViewHolder;

/**
 * 我要装修--我的发布
 * 
 */
public class DecorationMypublishNeedActivity extends BaseActivity {
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateNeedEntity> showAdapter;
	private List<DecorateNeedEntity> showlist = new ArrayList<DecorateNeedEntity>();
	private LinearLayout lin_buju;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_decorationmypublishneed;
	}

	@Override
	public void initView() {
		lin_buju = (LinearLayout) findViewById(R.id.lin_buju);
		initPullListView();
	}

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public void bindListener() {
		findViewById(R.id.btnAddmore).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(
						DecorationMypublishNeedActivity.this,
						MyNeedDecPubActivity.class), reqCodeOne);
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke2", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<DecorateNeedEntity> listData = (List<DecorateNeedEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateNeedEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
			}
			if (showlist.size() > 0) {
				lin_buju.setVisibility(View.INVISIBLE);
			} else {
				lin_buju.setVisibility(View.VISIBLE);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			downRefreshData();
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			setResult(RESULT_OK);
			break;
		case reqCodeThree:
			setResult(RESULT_OK);
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
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						DecorationMypublishNeedActivity.this, "提示", resp
								.getHeader().getRetMessage(), "确定");
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

		showAdapter = new CommonAdapter<DecorateNeedEntity>(
				DecorationMypublishNeedActivity.this,
				R.layout.item_publish_need, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateNeedEntity t) {
				final Button isopen = viewHolder.getView(R.id.btn_isopen);
				final int position = viewHolder.getPosition();
				if (t.getIsOpen() == 0) {
					isopen.setText("关闭");
				} else {
					isopen.setText("开启");
				}
				viewHolder.setText(R.id.tv_needname, t.getNeedName());
				viewHolder.setText(R.id.tv_needdetail, t.getClientNeedDetail());
				viewHolder.setText(R.id.tv_cost, t.getClientCostBudgetName());
				viewHolder.setText(R.id.tv_date, t.getClientCreateDate());
				viewHolder.getView(R.id.btn_edit).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										DecorationMypublishNeedActivity.this,
										MyNeedDecPubActivity.class);
								intent.putExtra("flag", 1);
								intent.putExtra("id", t.getId());
								startActivityForResult(intent, reqCodeFour);
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
				viewHolder.getView(R.id.btn_delete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										DecorationMypublishNeedActivity.this,
										"提示", "确定删除该申请吗？", "确定", "取消");
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
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300044));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 删除请求
	public void Delete(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300047));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	// 开启关闭请求
	public void Isopen(int id, int isOpen) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isOpen", isOpen);
		map.put("id", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300046));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_DECORATION, req);
		Log.d("wangke", isOpen + "");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			downRefreshData();
			setResult(RESULT_OK);
		}
	}

}
