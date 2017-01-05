package com.meiku.dev.ui.product;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.MkDataConfigReleaseMonths;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.WheelSelectDialog;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;

/**
 * 发布产品购买付费
 */
public class PayProductActivity extends BaseActivity implements OnClickListener {

	private RadioGroup group_settop;
	private TextView tv_name, tv_provinces, tv_topMoney, tv_needpay, tv_months,
			tv_startTime;
	private Button btnOK;
	private int pub_provinces_size = 0;// 省份数
	private int productMoney;// 产品需要的钱，不带置顶
	private int needPayMoney;// 总共需要的钱，带置顶
	private LinearLayout layout_topMoney;
	private int topMoney;
	private int monthSize = 0;
	private String provinceNames;
	private String provinceCodes;
	private int productId;
	private int topFlag = 0;
	private int topDays = 0;
	private boolean isWhole;
	private String dbQuanguoMoney;
	private CommonDialog exitTipDialog;
	private List<MkDataConfigReleaseMonths> monthDBList;
	private String[] buyMonthStrs;
	protected int monthsAllMoney;
	private TextView tv_publishPay;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_payproduct;
	}

	@Override
	public void initView() {
		initTipDialog();
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_provinces = (TextView) findViewById(R.id.tv_provinces);
		tv_months = (TextView) findViewById(R.id.tv_months);
		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_topMoney = (TextView) findViewById(R.id.tv_topMoney);
		tv_needpay = (TextView) findViewById(R.id.tv_needpay);
		tv_publishPay = (TextView) findViewById(R.id.tv_publishPay);
		group_settop = (RadioGroup) findViewById(R.id.group_settop);
		group_settop.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.btn_yes && topFlag == 0) {
					startActivityForResult(new Intent(PayProductActivity.this,
							ProductSetTopActivity.class).putExtra(
							"productName", tv_name.getText().toString()),
							reqCodeOne);
					topFlag = 1;
				} else if (arg1 == R.id.btn_no && topFlag == 1) {
					topMoney = 0;
					layout_topMoney.setVisibility(View.GONE);
					setAllNeedPayMoney();
					topFlag = 0;
				}
			}
		});
		layout_topMoney = (LinearLayout) findViewById(R.id.layout_topMoney);
		btnOK = (Button) findViewById(R.id.btnOK);
	}

	@Override
	public void initValue() {
		Intent intent = getIntent();
		tv_name.setText(intent.getStringExtra("productName"));
		provinceNames = intent.getStringExtra("provinceNames");
		provinceCodes = intent.getStringExtra("provinceCodes");
		productId = intent.getIntExtra("productId", -1);
		tv_provinces.setText(provinceNames);
		if (!Tool.isEmpty(provinceNames)) {
			pub_provinces_size = provinceNames.split(",").length;
			isWhole = provinceNames.contains("全国");
			dbQuanguoMoney = MKDataBase.getInstance().getProdectPrice(1);
			if (!Tool.isEmpty(dbQuanguoMoney)) {
				// setAllNeedPayMoney();
			} else {
				ToastUtil.showShortToast("数据库获取产品价格失败！请重新登录或者下载最新版本！");
				finish();
			}
		} else {
			ToastUtil.showShortToast("获取招商省份失败！");
			finish();
		}
		monthDBList = MKDataBase.getInstance().getReleaseMonthsList(1);
		if (!Tool.isEmpty(monthDBList)) {
			int size = monthDBList.size();
			buyMonthStrs = new String[size];
			for (int i = 0; i < size; i++) {
				buyMonthStrs[i] = monthDBList.get(i).getMonthsName();
			}
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		btnOK.setOnClickListener(this);
		tv_months.setOnClickListener(this);
		tv_startTime.setOnClickListener(this);
		layout_topMoney.setOnClickListener(this);
	}

	/**
	 * 下单
	 */
	private void PlaceAnOrder() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("sourceType", 1);// 0=充值 1=找产品发布 2=置顶找产品
		map.put("flag", 1);// 0续费发布产品 or 1付款
		map.put("productId", productId);
		map.put("buyMonths", monthSize);
		map.put("orderAmount", productMoney);
		map.put("provinceCodes", provinceCodes);
		map.put("provinceNames", provinceNames);
		map.put("buyStartTime", tv_startTime.getText().toString());
		map.put("topFlag", topFlag);
		map.put("spendMoney", topMoney);
		map.put("topDays", topDays);
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		LogUtil.d("hl", "PlaceAnOrder=" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:// 置顶返回结果
				if (data == null
						|| Tool.isEmpty(data.getIntExtra("favourable", 0))) {
					if (layout_topMoney.getVisibility() != View.VISIBLE) {
						group_settop.check(R.id.btn_no);
						topFlag = 0;
					}
					return;
				}
				layout_topMoney.setVisibility(View.VISIBLE);
				topMoney = data.getIntExtra("favourable", 0);
				topDays = data.getIntExtra("setTopTimeString", 0);
				tv_topMoney.setText("置顶时长 " + topDays + " 天  |  " + topMoney
						+ "元");
				setAllNeedPayMoney();
				break;
			case reqCodeThree:
				LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
						.getInstance(PayProductActivity.this);
				localBroadcastManager.sendBroadcast(new Intent(
						BroadCastAction.ACTION_MYPRODUCT));
				finish();
				break;
			}
		}
	}

	private void setAllNeedPayMoney() {
		if (isWhole) {
			productMoney = Integer.parseInt(dbQuanguoMoney) * monthSize;
		} else {
			productMoney = pub_provinces_size * monthsAllMoney;
		}
		tv_publishPay.setText(productMoney + "元");
		needPayMoney = productMoney + topMoney;
		tv_needpay.setText(needPayMoney + "元");
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeTwo:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				PayOrderGroupEntity payOrdergroup = (PayOrderGroupEntity) JsonUtil
						.jsonToObj(PayOrderGroupEntity.class, resp.getBody()
								.get("data").toString());
				Bundle b = new Bundle();
				b.putSerializable("MkPayOrders", (Serializable) payOrdergroup);
				startActivityForResult(new Intent(PayProductActivity.this,
						PayStyleActivity.class).putExtras(b), reqCodeThree);
			} else {
				ToastUtil.showShortToast("下单失败！");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("查询美库余额失败！");
			break;
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			final CommonDialog commonDialog = new CommonDialog(
					PayProductActivity.this, "提示", resp.getHeader()
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
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			finishWhenTip();
			break;
		case R.id.btnOK:
			if (Tool.isEmpty(tv_months.getText().toString())) {
				ToastUtil.showShortToast("请选择发布时长！");
				return;
			}
			if (Tool.isEmpty(tv_startTime.getText().toString())) {
				ToastUtil.showShortToast("请选择发布时间！");
				return;
			}
			PlaceAnOrder();
			break;
		case R.id.tv_months:
			if (Tool.isEmpty(monthDBList)) {
				ToastUtil.showShortToast("获取时间选项失败！");
				return;
			}
			new WheelSelectDialog(PayProductActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_months.setText(str);
							monthSize = Integer.parseInt(monthDBList.get(
									itemIndex).getMonthsValue());
							monthsAllMoney = Integer.parseInt(monthDBList.get(
									itemIndex).getMoney());
							setAllNeedPayMoney();
						}
					}, buyMonthStrs).show();
			break;
		case R.id.tv_startTime:
			new TimeSelectDialog(PayProductActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_startTime.setText(time);
						}
					}).show();
			break;
		case R.id.layout_topMoney:
			startActivityForResult(new Intent(PayProductActivity.this,
					ProductSetTopActivity.class).putExtra("productName",
					tv_name.getText().toString()), reqCodeOne);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void initTipDialog() {
		exitTipDialog = new CommonDialog(PayProductActivity.this, "提示",
				"是否放弃购买并退出?", "确定", "取消");
		exitTipDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						finish();

					}

					@Override
					public void doCancel() {
						exitTipDialog.dismiss();
					}
				});
	}

	private void finishWhenTip() {
		if (exitTipDialog != null && !exitTipDialog.isShowing()) {
			exitTipDialog.show();
		} else {
			finish();
		}
	}

}
