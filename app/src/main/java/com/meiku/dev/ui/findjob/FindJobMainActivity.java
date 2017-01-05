package com.meiku.dev.ui.findjob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.FindJobEntity;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.product.ProductsMainActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.MyViewpager;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 找工作主页面
 * 
 */
public class FindJobMainActivity extends BaseFragmentActivity implements
		OnClickListener {

	private TextView tv_productcatagory;
	private ClearEditText et_msg_search;
	private int CityCode;
	private String currentCityName;
	private ArrayList<JobClassEntity> resultList;
	private CommonAdapter<JobClassEntity> menuAdapter;
	private View view_title1, view_title2, view_title3;
	private PullToRefreshScrollView pullRefreshView;
	private MyViewpager viewpager;
	private HomeViewPagerAdapter adapter;
	/** 滚动页面数 */
	private int viewpageSize;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_findjobmain;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void initView() {
		initTitle();
		pullRefreshView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
		pullRefreshView.setMode(PullToRefreshBase.Mode.DISABLED);
		view_title1 = findViewById(R.id.title1);
		((TextView) view_title1.findViewById(R.id.tv_modelName)).setText("推荐");
		view_title2 = findViewById(R.id.title2);
		((TextView) view_title2.findViewById(R.id.tv_modelName))
				.setText("在招职位");
		initPositionGrid();
		view_title3 = findViewById(R.id.title3);
		((TextView) view_title3.findViewById(R.id.tv_modelName))
				.setText("热点企业");
		((TextView) view_title3.findViewById(R.id.tv_tip)).setText("匠心时代，没你不行");
		viewpager = (MyViewpager) findViewById(R.id.content_viewpager);
		viewpager.setScrollable(true);
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				currentItem = position;
				if (currentItem == viewpageSize - 1) {
					currentItem = -1;
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

	private void initPositionGrid() {
		resultList = new ArrayList<JobClassEntity>();
		resultList = (ArrayList<JobClassEntity>) MKDataBase.getInstance()
				.getFindJobPositionList();
		MyGridView myGridView = (MyGridView) findViewById(R.id.gridview);
		menuAdapter = new CommonAdapter<JobClassEntity>(
				FindJobMainActivity.this, R.layout.item_findjob_position,
				resultList) {

			@Override
			public void convert(ViewHolder viewHolder, final JobClassEntity t) {
				viewHolder.setText(R.id.id_txt, t.getName());
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MySimpleDraweeView img = new MySimpleDraweeView(
						FindJobMainActivity.this);
				layout_addImage.addView(img, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				if (Tool.isEmpty(t.getJobImgUrl())) {
					img.setVisibility(View.INVISIBLE);
				} else {
					img.setVisibility(View.VISIBLE);
					int id = getResources().getIdentifier(
							t.getJobImgUrl().substring(0,
									t.getJobImgUrl().indexOf(".")), "drawable",
							getPackageName());
					img.setUrlOfImage("res://com.mrrck.dev/" + id);
				}
			}
		};
		myGridView.setAdapter(menuAdapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(FindJobMainActivity.this,
						JobListActivity.class).putExtra("positionId",
						resultList.get(arg2).getId()));
			}
		});
	}

	private void initTitle() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		tv_productcatagory = (TextView) findViewById(R.id.tv_productcatagory);
		et_msg_search.setHint("请输入岗位、公司名称搜索");
		et_msg_search.setKeyListener(null);
		Drawable drawable = ContextCompat.getDrawable(
				FindJobMainActivity.this, R.drawable.triangle);
		drawable.setBounds(0, 0,
				ScreenUtil.dip2px(FindJobMainActivity.this, 8),
				ScreenUtil.dip2px(FindJobMainActivity.this, 8));
		tv_productcatagory.setCompoundDrawables(null, null, drawable, null);
		if (!Tool.isEmpty(MrrckApplication.cityCode)) {
			CityCode = Integer.parseInt(MrrckApplication.cityCode);
			currentCityName = MKDataBase.getInstance().getCityNameByCityCode(
					CityCode);
		}
		tv_productcatagory
				.setText(!Tool.isEmpty(currentCityName) ? currentCityName
						: "请选择");
		// tv_productcatagory.setText("全国");
	}

	@Override
	public void initValue() {
		getData();
	}

	private void getData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityCode", CityCode);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_FINDJOB_HOME));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.RESUME_REQUEST_MAPPING, reqBase, true);
	}

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.tv_myResume).setOnClickListener(this);
		findViewById(R.id.tv_needJob).setOnClickListener(this);
		findViewById(R.id.bt_fj).setOnClickListener(this);
		findViewById(R.id.bt_bcz).setOnClickListener(this);
		findViewById(R.id.bt_jz).setOnClickListener(this);
		et_msg_search.setOnClickListener(this);
		tv_productcatagory.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "#找工作#" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("foundJobEntity") + "").length() > 2) {
				PreferHelper.setSharedParam(ConstantKey.FINDJOB_HOTCOMPANY,
						resp.getBody().get("foundJobEntity").toString());
				FindJobEntity jobEntity = (FindJobEntity) JsonUtil.jsonToObj(
						FindJobEntity.class,
						resp.getBody().get("foundJobEntity").toString());
				if ((resp.getBody().get("companyDetailUrl") + "").length() > 2) {
					PreferHelper.setSharedParam(ConstantKey.FINDJOB_COMPANYURL,
							resp.getBody().get("companyDetailUrl")
									.getAsString());
				}
				if (jobEntity != null && null != jobEntity.getCompanyList()) {
					int dataSize = jobEntity.getCompanyList().size();
					viewpageSize = dataSize % 6 == 0 ? dataSize / 6
							: (dataSize / 6 + 1);
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					ArrayList<BaseFragment> listFragment = new ArrayList<BaseFragment>();
					for (int i = 0; i < viewpageSize; i++) {
						HotCompanyFragment compFragment = new HotCompanyFragment();
						listFragment.add(compFragment);
						Bundle bundle = new Bundle();
						bundle.putInt("pageIndex", i + 1);
						bundle.putInt("dataSize", dataSize);
						compFragment.setArguments(bundle);
					}
					transaction.commit();
					adapter = new HomeViewPagerAdapter(
							getSupportFragmentManager(), listFragment);
					viewpager.setAdapter(adapter);
					viewpager.setOffscreenPageLimit(viewpageSize);
					viewpager.setCurrentItem(0);
					handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				}
			}
			break;
		case reqCodeTwo:
			String companyEntityStr = resp.getBody().get("company").toString();
			LogUtil.d("hl", "检测认证company__" + companyEntityStr);
			if (Tool.isEmpty(companyEntityStr)
					|| companyEntityStr.length() == 2) {
				final CommonDialog commonDialog = new CommonDialog(
						FindJobMainActivity.this, "提示", "您还不是企业用户，是否去认证", "是",
						"否");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						startActivity(new Intent(FindJobMainActivity.this,
								CompanyCertificationActivity.class).putExtra(
								"flag", "1"));
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
					// AppContext.getInstance().getUserInfo()
					// .setCompanyEntity(companyEntity);
					// Intent intent = new Intent(getActivity(),
					// WebViewActivity.class);
					// intent.putExtra("webUrl", zhaopingUrl);
					// startActivity(intent);

					Intent intent = new Intent(FindJobMainActivity.this,
							RecruitMainActivity.class);
					startActivity(intent);
				} else if (companyEntity.getAuthPass().equals("0")) {
					Intent intent = new Intent(FindJobMainActivity.this,
							EditCompInfoActivity.class);
					startActivity(intent);
				} else {
					final CommonDialog commonDialog = new CommonDialog(
							FindJobMainActivity.this, "提示",
							"您的企业信息未通过审核！\n 原因："
									+ companyEntity.getAuthResult(), "修改", "取消");
					commonDialog.show();
					commonDialog
							.setClicklistener(new CommonDialog.ClickListenerInterface() {
								@Override
								public void doConfirm() {
									commonDialog.dismiss();
									Intent intent = new Intent(
											FindJobMainActivity.this,
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
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		default:
			ToastUtil.showShortToast("获取数据失败！");
			finish();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_productcatagory:
			new WheelSelectCityDialog(FindJobMainActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_productcatagory.setText(cityName);
							CityCode = cityCode;
							getData();
						}

					}).show();
			break;
		case R.id.goback:
			finish();
			break;
		case R.id.et_msg_search:
			startActivity(new Intent(this, JobSearchActivity.class));
			break;
		case R.id.tv_myResume:// 我要求职
			// if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
			// ShowLoginDialogUtil
			// .showTipToLoginDialog(FindJobMainActivity.this);
			// return;
			// }
			
			// if (AppContext.getInstance().getUserInfo().getResumeId() == -1) {
			// final CommonDialog commonDialog = new CommonDialog(
			// FindJobMainActivity.this, "提示", "您还没有简历，是否去创建", "创建",
			// "取消");
			// commonDialog.show();
			// commonDialog.setClicklistener(new ClickListenerInterface() {
			//
			// @Override
			// public void doConfirm() {
			// startActivity(new Intent(FindJobMainActivity.this,
			// MrrckResumeActivity.class));
			// commonDialog.dismiss();
			// }
			//
			// @Override
			// public void doCancel() {
			// commonDialog.dismiss();
			// }
			// });

			// }

			// 公司验证
			// CheckCompanyIsCertificate();
			startActivity(new Intent(FindJobMainActivity.this,
					RecruitMainActivity.class));
			break;
		case R.id.tv_needJob:// 我的求职
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(FindJobMainActivity.this);
				return;
			}
			if (!TextUtils.isEmpty(AppContext.getInstance().getUserInfo()
					.getMyJobUrl())) {
				Intent i = new Intent(FindJobMainActivity.this,
						WebViewActivity.class);
				i.putExtra("webUrl", AppContext.getInstance().getUserInfo()
						.getMyJobUrl());
				i.putExtra("flag", 1);
				startActivity(i);
			} else {
				ToastUtil.showShortToast("暂无求职信息");
			}
			break;
		case R.id.bt_fj:// 附近
			startActivity(new Intent(FindJobMainActivity.this,
					JobListActivity.class).putExtra("nearby", true));// 附近
			break;
		case R.id.bt_bcz:// 包吃住
			startActivity(new Intent(this, JobListActivity.class).putExtra(
					"baochizhu", true));
			break;
		case R.id.bt_jz:// 兼职
			startActivity(new Intent(FindJobMainActivity.this,
					JobListActivity.class).putExtra("jobType", 2));// 全职1，兼职2
			break;
		default:
			break;
		}

	}

	/**
	 * 检测公司是否认证
	 */
	private void CheckCompanyIsCertificate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_VERIFY));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
	}

	protected static final int MSG_UPDATE_IMAGE = 1;
	protected static final int MSG_KEEP_SILENT = 2;
	protected static final int MSG_BREAK_SILENT = 3;
	protected static final long MSG_DELAY = 6000;
	private int currentItem = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (hasMessages(MSG_UPDATE_IMAGE)) {
				removeMessages(MSG_UPDATE_IMAGE);
			}
			switch (msg.what) {
			case MSG_UPDATE_IMAGE:
				currentItem++;
				viewpager.setCurrentItem(currentItem);
				if (currentItem == viewpageSize - 1) {
					currentItem = -1;
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
