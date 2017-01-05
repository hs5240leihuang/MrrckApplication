package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCompanyEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 装修公司
 * 
 */
public class DecCompanyActivity extends BaseActivity implements OnClickListener {

	private EditText et_msg_search;
	private TextView tv_productcatagory;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateCompanyEntity> showAdapter;
	private List<DecorateCompanyEntity> showList = new ArrayList<DecorateCompanyEntity>();
	protected int selectCityCode;
	protected int selectProvinceCode;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_deccompany;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void initView() {
		initTitle();
		initPullListView();
	}

	private void initTitle() {
		et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		tv_productcatagory = (TextView) findViewById(R.id.tv_productcatagory);
		et_msg_search.setHint("请输入公司名称搜索");
		et_msg_search.setKeyListener(null);
		et_msg_search.setOnClickListener(this);
		findViewById(R.id.searchbar).setOnClickListener(this);
		Drawable drawable = ContextCompat.getDrawable(DecCompanyActivity.this,
				R.drawable.triangle);
		drawable.setBounds(0, 0, ScreenUtil.dip2px(DecCompanyActivity.this, 6),
				ScreenUtil.dip2px(DecCompanyActivity.this, 6));
		tv_productcatagory.setCompoundDrawables(null, null, drawable, null);
		// if (Tool.isEmpty(MrrckApplication.cityCode)
		// || MrrckApplication.cityCode == "-1"
		// || Tool.isEmpty(MrrckApplication.provinceCode)
		// || MrrckApplication.provinceCode == "-1") {
		// selectCityCode = -1;
		// tv_productcatagory.setText("全国");
		// } else {
		// selectCityCode = Integer.parseInt(MrrckApplication.cityCode);
		// selectProvinceCode = Integer
		// .parseInt(MrrckApplication.provinceCode);
		// tv_productcatagory.setText(MrrckApplication.cityName);
		// }
		selectCityCode = getIntent().getIntExtra("selectCityCode", -1);
		selectProvinceCode = getIntent().getIntExtra("selectProvinceCode", -1);
		tv_productcatagory.setText(getIntent().getStringExtra("cityName"));
		tv_productcatagory.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	@Override
	public void initValue() {
		downRefreshData();
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
		// 适配器
		showAdapter = new CommonAdapter<DecorateCompanyEntity>(
				DecCompanyActivity.this, R.layout.item_case, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCompanyEntity t) {
				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						DecCompanyActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview
						.setUrlOfImage(t.getClientCompanyLogo());
				viewHolder.setText(R.id.tv_companyName, t.getName());
				viewHolder.setText(R.id.tv_serviceShopNum,
						"已有" + t.getHighCommentNum() + "家店铺评论");
				viewHolder.setText(R.id.iv_address, t.getAddress());
				LinearLayout layout_youhui = viewHolder
						.getView(R.id.layout_youhui);
				if (Tool.isEmpty(t.getDcfcList())) {
					layout_youhui.setVisibility(View.GONE);
				} else {
					layout_youhui.setVisibility(View.VISIBLE);
					String youhuiStrs = "";
					for (int i = 0, size = t.getDcfcList().size(); i < size; i++) {
						youhuiStrs += t.getDcfcList().get(i).getName() + "\n";
					}
					viewHolder.setText(R.id.tv_youhui, youhuiStrs);
				}
				// 0 已经认证 1 未认证
				viewHolder.getView(R.id.iv_bao).setVisibility(
						t.getProtecteFlag() == 1 ? View.GONE : View.VISIBLE);
				//是否已被美库认证 0:未认证 1:已认证
				viewHolder.getView(R.id.iv_yan).setVisibility(
						t.getMrrckAuthFlag() == 0 ? View.GONE : View.VISIBLE);
				//置顶标记  0  置顶  1 未置顶
				viewHolder.getView(R.id.iv_top).setVisibility(
						t.getTopFlag()== 1 ? View.GONE : View.VISIBLE);
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(
										DecCompanyActivity.this,
										DecCompanyDetailActivity.class)
										.putExtra("shareTitle",
												t.getShareTitle())
										.putExtra("shareContent",
												t.getShareContent())
										.putExtra("shareImg", t.getShareImg())
										.putExtra("shareUrl", t.getShareUrl())
										.putExtra("userId", t.getUserId())
										.putExtra("sourceId", t.getId())
										.putExtra("loadUrl", t.getLoadUrl()));
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

	}

	// 上拉加载更多数据
	private void upRefreshData() {
		page++;
		getData();
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		page = 1;
		showList.clear();
		getData();
	}

	private void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", 2);// // 案例 1 公司 2
		map.put("name", "");
		map.put("page", page);
		int companyId = -1;
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			companyId = AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId();
		}
		map.put("companyId", companyId);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300005));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("dataList"))
					&& resp.getBody().get("dataList").toString().length() > 2) {
				try {
					List<DecorateCompanyEntity> caseList = (List<DecorateCompanyEntity>) JsonUtil
							.jsonToList(
									resp.getBody().get("dataList").toString(),
									new TypeToken<List<DecorateCompanyEntity>>() {
									}.getType());
					showList.addAll(caseList);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
			}
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				try {
					List<DecorateCompanyEntity> caseList = (List<DecorateCompanyEntity>) JsonUtil
							.jsonToList(
									resp.getBody().get("data").toString(),
									new TypeToken<List<DecorateCompanyEntity>>() {
									}.getType());
					showList.addAll(caseList);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
			}
			showAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						DecCompanyActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
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
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			finish();
			break;
		case R.id.tv_productcatagory:
			new WheelSelectCityDialog(DecCompanyActivity.this, true,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_productcatagory.setText(cityName);
							selectCityCode = cityCode;
							selectProvinceCode = provinceCode;
							downRefreshData();
						}

					}).show();
			break;
		case R.id.searchbar:
		case R.id.et_msg_search:
			startActivity(new Intent(DecCompanyActivity.this,
					SearchCaseActivity.class)
					.putExtra("selectCityCode", selectCityCode)
					.putExtra("selectProvinceCode", selectProvinceCode)
					.putExtra("searchType", 2));
			break;
		default:
			break;
		}

	}

}
