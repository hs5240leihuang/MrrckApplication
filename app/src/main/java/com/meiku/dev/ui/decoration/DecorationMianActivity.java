package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.AdViewPagerAdapter;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AdvertiseBannerEntity;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.DecorateCaseEntity;
import com.meiku.dev.bean.MkDecorateIndexConfig;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.community.ListPostActivity;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.ui.product.ProductDetailActivity;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.IndicatorView;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 找装修主页面
 * 
 */
public class DecorationMianActivity extends BaseActivity implements
		OnClickListener {
	// 顶部输入栏-----
	/** 输入框 */
	private EditText et_msg_search;
	/** 城市选择 */
	private TextView tv_productcatagory;
	/** 城市选择 */
	private int ProvincesCode;
	// 广告栏-----
	private RelativeLayout ADlayout;
	private IndicatorView indicatorGroup;
	private ViewPager adVpager;
	protected int currentItem;
	/** 广告的数据 */
	private List<AdvertiseBannerEntity> adData = new ArrayList<AdvertiseBannerEntity>();
	private List<ImageView> guides = new ArrayList<ImageView>();
	// 菜单栏-----
	private MyGridView gridMenu1;
	private CommonAdapter<MkDecorateIndexConfig> menuGridAdapter1;
	private List<MkDecorateIndexConfig> showMenuList1 = new ArrayList<MkDecorateIndexConfig>();
	// 菜单栏2-----
	private List<MkDecorateIndexConfig> showMenuList2 = new ArrayList<MkDecorateIndexConfig>();
	private CommonAdapter<MkDecorateIndexConfig> menuGridAdapter2;
	private MyGridView gridMenu2;
	// 推荐案例-----
	private PullToRefreshScrollView pull_refreshSV;
	private MyListView decorationListView;
	private CommonAdapter<DecorateCaseEntity> decorationListAdapter;
	private List<DecorateCaseEntity> decorationListData = new ArrayList<DecorateCaseEntity>();
	protected int selectCityCode = -1;
	protected int selectProvinceCode = -1;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_decorationmain;
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
		initPullView();
		initTitle();
		initAdViews();
		initMenuGrid();
		initDecorationList();
	}

	private void initTitle() {
		et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		tv_productcatagory = (TextView) findViewById(R.id.tv_productcatagory);
		et_msg_search.setHint("请输入公司名称搜索");
		et_msg_search.setKeyListener(null);
		findViewById(R.id.searchbar).setOnClickListener(this);
		et_msg_search.setOnClickListener(this);
		Drawable drawable = ContextCompat.getDrawable(
				DecorationMianActivity.this, R.drawable.triangle);
		drawable.setBounds(0, 0,
				ScreenUtil.dip2px(DecorationMianActivity.this, 6),
				ScreenUtil.dip2px(DecorationMianActivity.this, 6));
		tv_productcatagory.setCompoundDrawables(null, null, drawable, null);
		if (Tool.isEmpty(MrrckApplication.cityCode)
				|| MrrckApplication.cityCode == "-1"
				|| Tool.isEmpty(MrrckApplication.provinceCode)
				|| MrrckApplication.provinceCode == "-1") {
			selectCityCode = -1;
			tv_productcatagory.setText("全国");
		} else {
			selectCityCode = Integer.parseInt(MrrckApplication.cityCode);
			selectProvinceCode = Integer
					.parseInt(MrrckApplication.provinceCode);
			tv_productcatagory.setText(MrrckApplication.cityName);
		}

		tv_productcatagory.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	private void initDecorationList() {
		decorationListView = (MyListView) findViewById(R.id.decorationListView);
		decorationListAdapter = new CommonAdapter<DecorateCaseEntity>(
				DecorationMianActivity.this, R.layout.item_decoration,
				decorationListData) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCaseEntity t) {
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				MyRoundDraweeView IV_head = new MyRoundDraweeView(
						DecorationMianActivity.this);
				lin_head.removeAllViews();
				lin_head.addView(IV_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				IV_head.setUrlOfImage(t.getClientHeadPicUrl());

				LinearLayout lin_showimg = viewHolder.getView(R.id.lin_showimg);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						DecorationMianActivity.this);
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
										DecorationMianActivity.this,
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
		decorationListView.setAdapter(decorationListAdapter);
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullView() {
		pull_refreshSV = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
		pull_refreshSV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		pull_refreshSV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						page = 1;
						decorationListData.clear();
						getData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						page++;
						getData();
					}
				});
	}

	private void initMenuGrid() {
		gridMenu1 = (MyGridView) findViewById(R.id.decorationGridview);
		menuGridAdapter1 = new CommonAdapter<MkDecorateIndexConfig>(
				DecorationMianActivity.this, R.layout.item_decorationmenu,
				showMenuList1) {

			@Override
			public void convert(ViewHolder viewHolder, MkDecorateIndexConfig t) {
				viewHolder.setText(R.id.id_txt, t.getTitle());
				LinearLayout lin_showimg = viewHolder.getView(R.id.layout_img);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						DecorationMianActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientImgUrl());
			}
		};
		gridMenu1.setAdapter(menuGridAdapter1);
		gridMenu1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (showMenuList1.get(arg2).getIsClient() == 0) {// 终端=0,H5=1
					String funTag = showMenuList1.get(arg2).getFunctionUrl();
					if (funTag.contains("APP_MODEL_RAIDER")) {// 装修攻略
						String menuId = "-1";
						if (funTag.contains("menuId=")) {
							menuId = funTag.split("=")[1];
						}
						startActivity(new Intent(DecorationMianActivity.this,
								DecStrategyActivity.class).putExtra("menuId",
								Integer.parseInt(menuId)));
					} else if (funTag.contains("APP_MODEL_CASE")) {// 装修案例
						startActivity(new Intent(DecorationMianActivity.this,
								DecCaseActivity.class).putExtra(
								"selectCityCode", selectCityCode).putExtra(
								"selectProvinceCode", selectProvinceCode));
					} else if (funTag.contains("APP_MODEL_COMPANY")) {// 装修公司
						startActivity(new Intent(DecorationMianActivity.this,
								DecCompanyActivity.class)
								.putExtra("selectCityCode", selectCityCode)
								.putExtra("selectProvinceCode",
										selectProvinceCode)
								.putExtra("cityName",
										tv_productcatagory.getText().toString()));
					}
				} else {
					Intent intent = new Intent(DecorationMianActivity.this,
							WebViewActivity.class);
					intent.putExtra("webUrl", showMenuList1.get(arg2)
							.getFunctionUrl());
					startActivity(intent);
				}
			}
		});

		gridMenu2 = (MyGridView) findViewById(R.id.menuGridview);
		menuGridAdapter2 = new CommonAdapter<MkDecorateIndexConfig>(
				DecorationMianActivity.this, R.layout.item_decorationmenu2,
				showMenuList2) {

			@Override
			public void convert(ViewHolder viewHolder, MkDecorateIndexConfig t) {
				viewHolder.setText(R.id.id_txt, t.getTitle());
				LinearLayout lin_showimg = viewHolder.getView(R.id.layout_img);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						DecorationMianActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientImgUrl());
			}
		};
		gridMenu2.setAdapter(menuGridAdapter2);
		gridMenu2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (showMenuList2.get(arg2).getIsClient() == 0) {// 终端=0,H5=1
					String funTag = showMenuList2.get(arg2).getFunctionUrl();
					if (funTag.contains("APP_MODEL_WANT_DECORATE")) {// 我要装修
						if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
							ShowLoginDialogUtil
									.showTipToLoginDialog(DecorationMianActivity.this);
							return;
						}
						startActivity(new Intent(DecorationMianActivity.this,
								IneedDecActivity.class));
					} else if (funTag.contains("APP_MODEL_COMPANY_DESIGN_FREE")) {// 免费设计
						startActivity(new Intent(DecorationMianActivity.this,
								FreedesignActivity.class).putExtra(
								"selectCityCode", selectCityCode).putExtra(
								"selectProvinceCode", selectProvinceCode));
					} else if (funTag.contains("APP_MODEL_DECORATE_PICE")) {// 智能报价
						if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
							ShowLoginDialogUtil
									.showTipToLoginDialog(DecorationMianActivity.this);
							return;
						}
						startActivity(new Intent(DecorationMianActivity.this,
								QuotedPriceActivity.class));
					} else if (funTag.contains("APP_MODEL_DECORATE_PROTECTE")) {// 美库装修宝
						if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
							ShowLoginDialogUtil
									.showTipToLoginDialog(DecorationMianActivity.this);
							return;
						}
						startActivity(new Intent(DecorationMianActivity.this,
								MKDecorationActivity.class));
					}
				} else {
					Intent intent = new Intent(DecorationMianActivity.this,
							WebViewActivity.class);
					intent.putExtra("webUrl", showMenuList2.get(arg2)
							.getFunctionUrl());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void initValue() {
		getData();
	}

	protected void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_HOME));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "getHomeData————" + map);
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	private void initAdViews() {
		ADlayout = (RelativeLayout) findViewById(R.id.ADlayout);
		// 点阵
		indicatorGroup = (IndicatorView) findViewById(R.id.indicatorGroup);

		adVpager = (ViewPager) findViewById(R.id.adPager);
		adVpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				currentItem = position;
				// 实现无限制循环播放
				if (!Tool.isEmpty(guides)) {
					position %= guides.size();
					indicatorGroup.setSelectedPosition(position);
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
					break;
				default:
					break;
				}
			}

		});
	}

	/**
	 * 添加一个广告图片
	 */
	private void addOneAD(String url, final String webUrl, int isClientApp,
			final int typeString, final int idString) {
		MySimpleDraweeView iv = new MySimpleDraweeView(this);
		GenericDraweeHierarchy hierarchy = iv.getHierarchy();
		hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		iv.setLayoutParams(params);
		iv.setUrlOfImage(url);
		if (isClientApp == 1) {
			if (!TextUtils.isEmpty(webUrl)) {
				iv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(DecorationMianActivity.this,
								WebViewActivity.class);
						intent.putExtra("webUrl", webUrl);
						startActivity(intent);
					}
				});
			}
		} else {
			if (Tool.isEmpty(typeString) || Tool.isEmpty(idString)) {
				return;
			} else {
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						switch (typeString) {
						case 1:// 一般帖子
							Intent intent1 = new Intent(
									DecorationMianActivity.this,
									PostDetailNewActivity.class);
							intent1.putExtra(ConstantKey.KEY_POSTID,
									String.valueOf(idString));
							startActivity(intent1);
							break;
						case 2:// 赛事帖子
							Intent intent2 = new Intent(
									DecorationMianActivity.this,
									ShowMainActivity.class);
							intent2.putExtra("postsId", idString);
							startActivity(intent2);
							break;
						case 3:// 社区版块
							Intent intent3 = new Intent(
									DecorationMianActivity.this,
									ListPostActivity.class);
							intent3.putExtra(ConstantKey.KEY_BOARDID,
									String.valueOf(idString));
							startActivity(intent3);
							break;
						case 4:// 产品页面
							Intent intent4 = new Intent(
									DecorationMianActivity.this,
									ProductDetailActivity.class);
							intent4.putExtra("productId",
									String.valueOf(idString));
							startActivity(intent4);
							break;

						default:
							break;
						}
					}

				});
			}

		}
		guides.add(iv);
	}

	@Override
	public void bindListener() {
		findViewById(R.id.btn_stor).setOnClickListener(this);
		findViewById(R.id.btn_company).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			String companyEntityStr = resp.getBody().get("company").toString();
			LogUtil.d("hl", "检测认证company__" + companyEntityStr);
			if (Tool.isEmpty(companyEntityStr)
					|| companyEntityStr.length() == 2) {
				final CommonDialog commonDialog = new CommonDialog(
						DecorationMianActivity.this, "提示", "您还不是企业用户，是否去认证",
						"是", "否");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						startActivity(new Intent(DecorationMianActivity.this,
								CompanyCertificationActivity.class).putExtra(
								"flag", "2"));
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
			} else {
				CompanyEntity companyEntity = (CompanyEntity) JsonUtil
						.jsonToObj(CompanyEntity.class, companyEntityStr);
				if (companyEntity.getAuthPass().equals("1")) {// 0：审核中 1：已完成
																// 2:不通过
					Intent intent = new Intent(DecorationMianActivity.this,
							MyDecorationPublishActivity.class);
					startActivity(intent);
				} else if (companyEntity.getAuthPass().equals("0")) {
					Intent intent = new Intent(DecorationMianActivity.this,
							EditCompInfoActivity.class);
					startActivity(intent);
				} else {
					final CommonDialog commonDialog = new CommonDialog(
							DecorationMianActivity.this, "提示",
							"您的企业信息未通过审核！\n 原因："
									+ companyEntity.getAuthResult(), "修改", "取消");
					commonDialog.show();
					commonDialog
							.setClicklistener(new CommonDialog.ClickListenerInterface() {
								@Override
								public void doConfirm() {
									commonDialog.dismiss();
									Intent intent = new Intent(
											DecorationMianActivity.this,
											EditCompInfoActivity.class);
									startActivity(intent);
								}

								@Override
								public void doCancel() {
									commonDialog.dismiss();
								}
							});
				}
			}
			break;
		case reqCodeTwo:
			if (!Tool.isEmpty(resp.getBody().get("decorateBannerList"))
					|| resp.getBody().get("decorateBannerList").toString()
							.length() > 2) {
				List<AdvertiseBannerEntity> data = new ArrayList<AdvertiseBannerEntity>();
				try {
					data = (List<AdvertiseBannerEntity>) JsonUtil
							.jsonToList(
									resp.getBody().get("decorateBannerList")
											.toString(),
									new TypeToken<List<AdvertiseBannerEntity>>() {
									}.getType());
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				adData.clear();
				guides.clear();
				adData.addAll(data);
				int ADCount = adData.size();
				LogUtil.d("hl", "===ADCount===" + ADCount);
				if (adData == null || ADCount == 0) {
					// 无广告数据则隐藏头部
					ADlayout.setVisibility(View.GONE);
				} else {
					ADlayout.setVisibility(View.VISIBLE);
					indicatorGroup.setPointCount(DecorationMianActivity.this,
							ADCount);
					indicatorGroup.setSelectedPosition(0);
					int typeString = 0;
					int idString = 0;
					for (int i = 0; i < ADCount; i++) {
						int isClientApp = adData.get(i).getIsClientApp();
						if (isClientApp == 0) {
							try {
								typeString = Integer.parseInt(adData.get(i)
										.getUrl().substring(5, 6));
								idString = Integer.parseInt(adData.get(i)
										.getUrl().substring(7));
							} catch (Exception e) {
							}
						}
						addOneAD(adData.get(i).getClientImgUrl(), adData.get(i)
								.getUrl(), isClientApp, typeString, idString);
					}
					AdViewPagerAdapter adVpagerAdapter = new AdViewPagerAdapter(
							guides);
					adVpager.setAdapter(adVpagerAdapter);
					if (guides.size() >= 2) {
						handler.sendEmptyMessageDelayed(MSG_BREAK_SILENT,
								MSG_DELAY);
					}
				}
			}
			if (!Tool.isEmpty(resp.getBody().get("decorateIndexConfigZero"))
					|| resp.getBody().get("decorateIndexConfigZero").toString()
							.length() > 2) {
				showMenuList1.clear();
				try {
					List<MkDecorateIndexConfig> menuListZero = (List<MkDecorateIndexConfig>) JsonUtil
							.jsonToList(
									resp.getBody()
											.get("decorateIndexConfigZero")
											.toString(),
									new TypeToken<List<MkDecorateIndexConfig>>() {
									}.getType());
					showMenuList1.addAll(menuListZero);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				menuGridAdapter1.notifyDataSetChanged();
			}
			if (!Tool.isEmpty(resp.getBody().get("decorateIndexConfigOne"))
					|| resp.getBody().get("decorateIndexConfigOne").toString()
							.length() > 2) {
				showMenuList2.clear();
				try {
					List<MkDecorateIndexConfig> menuListOne = (List<MkDecorateIndexConfig>) JsonUtil
							.jsonToList(
									resp.getBody()
											.get("decorateIndexConfigOne")
											.toString(),
									new TypeToken<List<MkDecorateIndexConfig>>() {
									}.getType());
					showMenuList2.addAll(menuListOne);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				menuGridAdapter2.notifyDataSetChanged();
			}
			if (!Tool.isEmpty(resp.getBody().get("decorateCaseList"))
					|| resp.getBody().get("decorateCaseList").toString()
							.length() > 2) {
				try {
					List<DecorateCaseEntity> caseList = (List<DecorateCaseEntity>) JsonUtil
							.jsonToList(resp.getBody().get("decorateCaseList")
									.toString(),
									new TypeToken<List<DecorateCaseEntity>>() {
									}.getType());
					decorationListData.addAll(caseList);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				decorationListAdapter.notifyDataSetChanged();
			}
			pull_refreshSV.onRefreshComplete();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (pull_refreshSV != null) {
			pull_refreshSV.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						DecorationMianActivity.this, "提示", resp.getHeader()
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
		case R.id.searchbar:
		case R.id.et_msg_search:
			startActivity(new Intent(DecorationMianActivity.this,
					SearchCaseActivity.class)
					.putExtra("selectCityCode", selectCityCode)
					.putExtra("selectProvinceCode", selectProvinceCode)
					.putExtra("searchType", 1));
			break;
		case R.id.btn_stor:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(DecorationMianActivity.this);
				return;
			}
			startActivity(new Intent(DecorationMianActivity.this,
					MyDecorationCollectActivity.class));
			break;
		case R.id.btn_company:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(DecorationMianActivity.this);
				return;
			}
			// 检测公司是否认证
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			req.setBody(JsonUtil.Map2JsonObj(map));
			req.setHeader(new ReqHead(AppConfig.BUSINESS_VERIFY));
			httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
			break;
		case R.id.tv_productcatagory:
			new WheelSelectCityDialog(DecorationMianActivity.this, true,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_productcatagory.setText(cityName);
							selectCityCode = cityCode;
							selectProvinceCode = provinceCode;
							decorationListData.clear();
							page = 1;
							getData();
						}

					}).show();
			break;
		default:
			break;
		}

	}

	protected static final int MSG_UPDATE_IMAGE = 1;
	protected static final int MSG_KEEP_SILENT = 2;
	protected static final int MSG_BREAK_SILENT = 3;
	protected static final long MSG_DELAY = 6000;
	private boolean showOneAd = true;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (hasMessages(MSG_UPDATE_IMAGE)) {
				removeMessages(MSG_UPDATE_IMAGE);
			}
			switch (msg.what) {
			case MSG_UPDATE_IMAGE:
				if (guides.size() == 2) {
					showOneAd = !showOneAd;
					adVpager.setCurrentItem(showOneAd ? 0 : 1);
				} else {
					currentItem++;
					adVpager.setCurrentItem(currentItem);
				}
				// 准备下次播放
				sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			case MSG_KEEP_SILENT:
				// 只要不发送消息就暂停了
				break;
			case MSG_BREAK_SILENT:
				sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

}
