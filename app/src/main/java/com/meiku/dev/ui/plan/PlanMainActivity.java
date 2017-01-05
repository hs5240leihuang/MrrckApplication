package com.meiku.dev.ui.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.AdViewPagerAdapter;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AdvertiseBannerEntity;
import com.meiku.dev.bean.MkDataConfigPlan;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.product.MyCollectActivity;
import com.meiku.dev.ui.product.MyProductActivity;
import com.meiku.dev.ui.product.ProductsMainActivity;
import com.meiku.dev.ui.product.PublishProductActivity;
import com.meiku.dev.ui.product.SelectDentificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 找策划主页面
 */
public class PlanMainActivity extends BaseActivity implements OnClickListener {

	private MyGridView gridMenuA;// 第一栏菜单
	private CommonAdapter<MkDataConfigPlan> menuGridAdapter1;
	private List<MkDataConfigPlan> showMenuList1 = new ArrayList<MkDataConfigPlan>();
	private MyGridView gridMenuB;// 第二栏菜单
	private List<MkDataConfigPlan> showMenuList2 = new ArrayList<MkDataConfigPlan>();
	private CommonAdapter<MkDataConfigPlan> menuGridAdapter2;
	private LinearLayout flowLayout_modelsC /* 第三栏菜单 */,
			flowLayout_modelsD/* 第四栏菜单 */;
	private String authenType;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_planmain;
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsAuthentication();
	}

	@Override
	public void initView() {
		EditText et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		et_msg_search.setHint("请输入关键词搜索");
		et_msg_search.setKeyListener(null);
		et_msg_search.setOnClickListener(this);
		initMenuGrid();
	}

	private void initMenuGrid() {
		showMenuList1 = MKDataBase.getInstance().getPlanMenuList(0, 1);
		gridMenuA = (MyGridView) findViewById(R.id.myGridView_modela);
		menuGridAdapter1 = new CommonAdapter<MkDataConfigPlan>(
				PlanMainActivity.this, R.layout.item_planmenu1, showMenuList1) {

			@Override
			public void convert(ViewHolder viewHolder, MkDataConfigPlan t) {
				viewHolder.setText(R.id.id_txt, t.getFunctionName());
				LinearLayout lin_showimg = viewHolder.getView(R.id.layout_img);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						PlanMainActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getFunctionPhoto());
			}
		};
		gridMenuA.setAdapter(menuGridAdapter1);
		gridMenuA.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (showMenuList1.get(arg2).getIsClientApp() == 0) {
					String fu = showMenuList1.get(arg2).getFunctionUrl();
					if ("1".equals(fu)) {
						startActivity(new Intent(PlanMainActivity.this,
								PlanOneActivity.class)
								.putExtra("planType", 0)
								.putExtra(
										"title",
										showMenuList1.get(arg2)
												.getFunctionName())
								.putExtra("caseType",
										showMenuList1.get(arg2).getCode()));
					} else if ("2".equals(fu)) {
						startActivity(new Intent(PlanMainActivity.this,
								PlanOneActivity.class)
								.putExtra("planType", 1)
								.putExtra(
										"title",
										showMenuList1.get(arg2)
												.getFunctionName())
								.putExtra("caseType",
										showMenuList1.get(arg2).getCode()));
					} else {
						startActivity(new Intent(PlanMainActivity.this,
								PlanTwoActivity.class).putExtra("title",
								showMenuList1.get(arg2).getFunctionName())
								.putExtra("caseType",
										showMenuList1.get(arg2).getCode()));
					}
				} else {
					Intent intent = new Intent(PlanMainActivity.this,
							WebViewActivity.class);
					intent.putExtra("webUrl", showMenuList1.get(arg2)
							.getFunctionUrl());
					startActivity(intent);
				}
			}
		});

		showMenuList2 = MKDataBase.getInstance().getPlanMenuList(0, 2);
		gridMenuB = (MyGridView) findViewById(R.id.myGridView_modelb);
		menuGridAdapter2 = new CommonAdapter<MkDataConfigPlan>(
				PlanMainActivity.this, R.layout.item_planmenu2, showMenuList2) {

			@Override
			public void convert(ViewHolder viewHolder, MkDataConfigPlan t) {
				viewHolder.setText(R.id.name, t.getFunctionName());
				viewHolder.setText(R.id.tip, t.getFunctionRemark());
				LinearLayout lin_showimg = viewHolder.getView(R.id.layout_img);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						PlanMainActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getFunctionPhoto());
			}
		};
		gridMenuB.setAdapter(menuGridAdapter2);
		gridMenuB.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (showMenuList2.get(arg2).getIsClientApp() == 0) {
					startActivity(new Intent(PlanMainActivity.this,
							PlanTwoActivity.class).putExtra("title",
							showMenuList2.get(arg2).getFunctionName())
							.putExtra("caseType",
									showMenuList2.get(arg2).getCode()));
				} else {
					Intent intent = new Intent(PlanMainActivity.this,
							WebViewActivity.class);
					intent.putExtra("webUrl", showMenuList2.get(arg2)
							.getFunctionUrl());
					startActivity(intent);
				}
			}
		});

		flowLayout_modelsC = (LinearLayout) findViewById(R.id.flowLayout_modelsC);
		flowLayout_modelsC.removeAllViews();
		int layoutWidth = ScreenUtil.SCREEN_WIDTH
				- ScreenUtil.dip2px(PlanMainActivity.this, 30);
		List<MkDataConfigPlan> listC = MKDataBase.getInstance()
				.getPlanMenuList(0, 3);
		if (!Tool.isEmpty(listC)) {
			if (listC.size() == 1) {
				addBottomMenu(layoutWidth, listC.get(0), flowLayout_modelsC,
						Gravity.CENTER);
			} else if (listC.size() == 2) {
				addBottomMenu((int) (layoutWidth * 0.6), listC.get(0),
						flowLayout_modelsC, Gravity.CENTER);
				addBottomMenu((int) (layoutWidth * 0.4), listC.get(1),
						flowLayout_modelsC, Gravity.CENTER);
			} else {
				for (int i = 0; i < listC.size(); i++) {
					addBottomMenu((int) (layoutWidth * 0.5), listC.get(i),
							flowLayout_modelsC, Gravity.CENTER);
				}
			}
		}

		flowLayout_modelsD = (LinearLayout) findViewById(R.id.flowLayout_modelsD);
		flowLayout_modelsD.removeAllViews();
		List<MkDataConfigPlan> listD = MKDataBase.getInstance()
				.getPlanMenuList(0, 4);
		if (!Tool.isEmpty(listD)) {
			if (listD.size() == 1) {
				addBottomMenu(layoutWidth, listD.get(0), flowLayout_modelsD,
						Gravity.CENTER);
			} else if (listD.size() == 2) {
				addBottomMenu((int) (layoutWidth * 0.4), listD.get(0),
						flowLayout_modelsD, Gravity.CENTER);
				addBottomMenu((int) (layoutWidth * 0.6), listD.get(1),
						flowLayout_modelsD, Gravity.CENTER);
			} else {
				for (int i = 0; i < listC.size(); i++) {
					addBottomMenu((int) (layoutWidth * 0.5), listD.get(i),
							flowLayout_modelsD, Gravity.CENTER);
				}
			}
		}
	}

	/**
	 * 添加menu
	 */
	private void addBottomMenu(int width, final MkDataConfigPlan planData,
			LinearLayout flowLayout_models, int position) {
		RelativeLayout relatL = new RelativeLayout(PlanMainActivity.this);
		LayoutParams params = new LinearLayout.LayoutParams(width,
				LayoutParams.MATCH_PARENT);
		int margin = ScreenUtil.dip2px(PlanMainActivity.this, 10);
		params.setMargins(0, 0, margin, 0);
		LinearLayout layoutImg = new LinearLayout(PlanMainActivity.this);
		MySimpleDraweeView showImgview = new MySimpleDraweeView(
				PlanMainActivity.this);
		layoutImg.removeAllViews();
		layoutImg.addView(showImgview, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		showImgview.setUrlOfImage(planData.getFunctionPhoto());
		relatL.addView(layoutImg, params);
		LinearLayout tvLayout = new LinearLayout(PlanMainActivity.this);
		tvLayout.setGravity(position);
		if (flowLayout_models == flowLayout_modelsD) {
			tvLayout.setPadding(0, 0, 0, margin);
		}
		TextView tv = new TextView(PlanMainActivity.this);
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundColor(Color.BLACK);
		tv.setTextColor(Color.WHITE);
		tv.setText(planData.getFunctionName());
		tv.setPadding(12, 0, 12, 0);
		tvLayout.addView(tv);
		relatL.addView(tvLayout, params);
		flowLayout_models.addView(relatL, params);
		relatL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (planData.getIsClientApp() == 0) {
					startActivity(new Intent(PlanMainActivity.this,
							PlanTwoActivity.class).putExtra("title",
							planData.getFunctionName()).putExtra("caseType",
							planData.getCode()));
				} else {
					Intent intent = new Intent(PlanMainActivity.this,
							WebViewActivity.class);
					intent.putExtra("webUrl", planData.getFunctionUrl());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
		findViewById(R.id.searchbar).setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.btn_goPublish).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody() + "").length() > 2) {
				authenType = resp.getBody().get("data").getAsString();
				LogUtil.d("hl", "authenType=" + authenType);
				if (ConstantKey.TYPE_AUTH_NO.equals(authenType)) {

				} else if (ConstantKey.TYPE_AUTH_COM.equals(authenType)
						|| ConstantKey.TYPE_AUTH_PERSION.equals(authenType)) {
				}
			} else {
				ToastUtil.showShortToast("获取认证数据失败");
			}
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:// 返回退出
			finish();
			break;
		case R.id.searchbar:
		case R.id.et_msg_search:// 去搜索
			startActivity(new Intent(PlanMainActivity.this,
					PlanSearchActivity.class));
			break;
		case R.id.btn_goPublish:
			if (!NetworkTools.isNetworkAvailable(PlanMainActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				if (ConstantKey.TYPE_AUTH_COM.equals(authenType)
						|| ConstantKey.TYPE_AUTH_PERSION.equals(authenType)) {
					startActivity(new Intent(PlanMainActivity.this,
							PublishProductActivity.class));
				} else {
					startActivity(new Intent(PlanMainActivity.this,
							SelectDentificationActivity.class));
				}
			} else {
				ShowLoginDialogUtil.showTipToLoginDialog(PlanMainActivity.this);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 是否经过认证
	 */
	private void checkIsAuthentication() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_YESORNO_DENTIFICATION));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER, req, false);
	}

}
