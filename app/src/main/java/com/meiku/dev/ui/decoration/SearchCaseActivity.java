package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCaseEntity;
import com.meiku.dev.bean.DecorateCompanyEntity;
import com.meiku.dev.bean.PopupData;
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
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

public class SearchCaseActivity extends BaseActivity implements OnClickListener {
	private NewPopupwindows newpopupwindows;
	private List<PopupData> list = new ArrayList<PopupData>();
	private CommonAdapter<DecorateCaseEntity> showCaseAdapter;
	private List<DecorateCaseEntity> showCaselist = new ArrayList<DecorateCaseEntity>();
	private CommonAdapter<DecorateCompanyEntity> showCompanyAdapter;
	private List<DecorateCompanyEntity> showCompanylist = new ArrayList<DecorateCompanyEntity>();
	private PullToRefreshListView mPullRefreshListView;
	private TextView tv_productcatagory;
	private EditText et_msg_search;
	private LinearLayout goback;
	private int searchType;
	private String searchName = "";
	private LinearLayout layoutEmpty;
	private TextView tv_emptyText;
	private int page_case;
	private int page_company;
	protected int selectCityCode;
	protected int selectProvinceCode;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_searchcase;
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
		goback = (LinearLayout) findViewById(R.id.goback);
		layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
		tv_emptyText = (TextView) findViewById(R.id.tv_text);
		et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		tv_productcatagory = (TextView) findViewById(R.id.tv_productcatagory);
		Drawable drawable = ContextCompat.getDrawable(SearchCaseActivity.this,
				R.drawable.triangle);
		drawable.setBounds(0, 0, ScreenUtil.dip2px(SearchCaseActivity.this, 6),
				ScreenUtil.dip2px(SearchCaseActivity.this, 6));
		tv_productcatagory.setCompoundDrawables(null, null, drawable, null);
		et_msg_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				searchName = arg0.toString();
				downRefreshData();
			}
		});
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
		showCaseAdapter = new CommonAdapter<DecorateCaseEntity>(this,
				R.layout.item_decoration, showCaselist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCaseEntity t) {
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				MyRoundDraweeView IV_head = new MyRoundDraweeView(
						SearchCaseActivity.this);
				lin_head.removeAllViews();
				lin_head.addView(IV_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				IV_head.setUrlOfImage(t.getClientHeadPicUrl());

				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						SearchCaseActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientCaseImgFileUrlThumb());

				viewHolder.setText(R.id.tv_righttop, t.getShopCategoryName());
				viewHolder.setText(R.id.tv_title, t.getCaseName());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder
						.setText(R.id.TV_rightbottom, t.getClientUpdateDate());
				viewHolder.setText(R.id.tv_info, t.getShowAreaSizeAndPice());

				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(
										SearchCaseActivity.this,
										CaseDetailActivity.class)
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
		showCompanyAdapter = new CommonAdapter<DecorateCompanyEntity>(this,
				R.layout.item_case, showCompanylist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCompanyEntity t) {

				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						SearchCaseActivity.this);
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
				// 是否已被美库认证 0:未认证 1:已认证
				viewHolder.getView(R.id.iv_yan).setVisibility(
						t.getMrrckAuthFlag() == 0 ? View.GONE : View.VISIBLE);
				// 置顶标记 0 置顶 1 未置顶
				viewHolder.getView(R.id.iv_top).setVisibility(
						t.getTopFlag() == 1 ? View.GONE : View.VISIBLE);
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(
										SearchCaseActivity.this,
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

	}

	@Override
	public void initValue() {
		list.clear();
		list.add(new PopupData("装修案例", 0));
		list.add(new PopupData("装修公司", 0));
		searchType = getIntent().getIntExtra("searchType", 1);
		selectCityCode = getIntent().getIntExtra("selectCityCode", -1);
		selectProvinceCode = getIntent().getIntExtra("selectProvinceCode", -1);
		if (searchType == 1) {
			tv_productcatagory.setText("装修案例");
			mPullRefreshListView.getRefreshableView().setDividerHeight(0);
			mPullRefreshListView.setAdapter(showCaseAdapter);
		} else {
			tv_productcatagory.setText("装修公司");
			mPullRefreshListView.getRefreshableView().setDividerHeight(1);
			mPullRefreshListView.setAdapter(showCompanyAdapter);
		}
		downRefreshData();
	}

	@Override
	public void bindListener() {
		goback.setOnClickListener(this);
		tv_productcatagory.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<DecorateCaseEntity> listData = (List<DecorateCaseEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateCaseEntity>>() {
								}.getType());
				showCaselist.addAll(listData);
			}
			if (Tool.isEmpty(showCaselist)) {
				tv_emptyText.setText("没有更多装修案例");
				layoutEmpty.setVisibility(View.VISIBLE);
			} else {
				layoutEmpty.setVisibility(View.GONE);
			}
			showCaseAdapter.setmDatas(showCaselist);
			showCaseAdapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
			try {
				if (!Tool.isEmpty(resp.getBody().get("dataList"))
						&& resp.getBody().get("dataList").toString().length() > 2) {

					List<DecorateCompanyEntity> caseList = (List<DecorateCompanyEntity>) JsonUtil
							.jsonToList(
									resp.getBody().get("dataList").toString(),
									new TypeToken<List<DecorateCompanyEntity>>() {
									}.getType());
					showCompanylist.addAll(caseList);
				}
				if (resp.getBody().get("data").toString().length() > 2) {
					List<DecorateCompanyEntity> listData = (List<DecorateCompanyEntity>) JsonUtil
							.jsonToList(
									resp.getBody().get("data").toString(),
									new TypeToken<List<DecorateCompanyEntity>>() {
									}.getType());
					showCompanylist.addAll(listData);
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(showCompanylist)) {
				tv_emptyText.setText("没有更多装修公司");
				layoutEmpty.setVisibility(View.VISIBLE);
			} else {
				layoutEmpty.setVisibility(View.GONE);
			}
			showCompanyAdapter.setmDatas(showCompanylist);
			showCompanyAdapter.notifyDataSetChanged();
			break;
		default:
			break;

		}
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						SearchCaseActivity.this, "提示", resp.getHeader()
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.goback:
			finish();
			break;
		case R.id.tv_productcatagory:
			newpopupwindows = new NewPopupwindows(SearchCaseActivity.this,
					list, new popwindowListener() {

						@Override
						public void doChoose(int position) {
							if (position == 0) {
								searchType = 1;
								mPullRefreshListView.getRefreshableView()
										.setDividerHeight(0);
								mPullRefreshListView
										.setAdapter(showCaseAdapter);
							} else {
								searchType = 2;
								mPullRefreshListView.getRefreshableView()
										.setDividerHeight(1);
								mPullRefreshListView
										.setAdapter(showCompanyAdapter);
							}
							tv_productcatagory.setText(list.get(position)
									.getName());
							downRefreshData();
						}
					}, 0);
			newpopupwindows.show(arg0);
			break;
		default:
			break;
		}
	}

	protected void upRefreshData() {
		if (searchType == 1) {
			page_case++;
			GetCaseData();
		} else {
			page_company++;
			GetCompanyData();
		}

	}

	protected void downRefreshData() {
		if (searchType == 1) {
			showCaselist.clear();
			page_case = 1;
			GetCaseData();
		} else {
			showCompanylist.clear();
			page_company = 1;
			GetCompanyData();
		}
	}

	// 数据请求
	public void GetCaseData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", 1);// // 案例 1 公司 2
		map.put("name", searchName);
		map.put("page", page_case);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300005));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req, false);
	}

	// 数据请求
	public void GetCompanyData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", 2);// // 案例 1 公司 2
		map.put("name", searchName);
		map.put("page", page_company);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		int companyId = -1;
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			companyId = AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId();
		}
		map.put("companyId", companyId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300005));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req, false);
	}
}
