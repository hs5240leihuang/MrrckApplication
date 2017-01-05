package com.meiku.dev.ui.product;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.WheelSelectDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;

/**
 * 续费页面
 * 
 */
public class XuFeiActivity extends BaseActivity implements OnClickListener {

	private TextView tv_name, tv_provinces, tv_needpay, tv_worktime;
	private Button btnOK;
	private int pub_provinces_size = 0;
	private int needPayMoney;
	private TextView tv_xufeiTime;
	private int monthSize;
	private String provinceNames;
	private String productName;
	private String provinceCodes;
	private String buyEndTime;
	private int productId;
	/** 是在有效期 true-未过期，false-过期 */
	private boolean isInWork;
	private String toDayStr;
	private String nowBuyEndTime;
	private boolean isWhole;
	private String dbQuanguoMoney;
	private CommonDialog exitTipDialog;
	private List<MkDataConfigReleaseMonths> monthDBList;
	private String[] buyMonthStrs;
	protected int monthsAllMoney;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_xufei;
	}

	@Override
	public void initView() {
		initTipDialog();
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_provinces = (TextView) findViewById(R.id.tv_provinces);
		tv_needpay = (TextView) findViewById(R.id.tv_needpay);
		tv_xufeiTime = (TextView) findViewById(R.id.tv_xufeiTime);
		tv_worktime = (TextView) findViewById(R.id.tv_worktime);
		btnOK = (Button) findViewById(R.id.btnOK);
	}

	protected void setEndDate() {
		if (isInWork) {
			nowBuyEndTime = DateTimeUtil.addDateTime(buyEndTime, monthSize, 0);
			tv_worktime.setText(buyEndTime + "  至  " + nowBuyEndTime);
		} else {
			nowBuyEndTime = DateTimeUtil.addDateTime(toDayStr, monthSize, 0);
			tv_worktime.setText(toDayStr + "  至  " + nowBuyEndTime);
		}
	}

	@Override
	public void initValue() {
		productId = getIntent().getIntExtra("productId", -1);
		productName = getIntent().getStringExtra("productName");
		tv_name.setText(productName);
		buyEndTime = getIntent().getStringExtra("buyEndTime");
		provinceNames = getIntent().getStringExtra("provinceNames");
		provinceCodes = getIntent().getStringExtra("provinceCodes");
		tv_provinces.setText(provinceNames);
		if (!Tool.isEmpty(buyEndTime)) {
			isInWork = DateTimeUtil.compareDate(buyEndTime);
		}
		LogUtil.d("hl", "######buyEndTime=" + buyEndTime + "在有效期=" + isInWork);
		toDayStr = DateTimeUtil.toString(DateTimeUtil.getShortNowDate());
		if (!Tool.isEmpty(provinceNames)) {
			pub_provinces_size = provinceNames.split(",").length;
			isWhole = provinceNames.contains("全国");
			dbQuanguoMoney = MKDataBase.getInstance().getProdectPrice(1);
			if (!Tool.isEmpty(dbQuanguoMoney)) {
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
		tv_xufeiTime.setOnClickListener(this);
	}

	private void setAllNeedPayMoney() {
		if (isWhole) {
			needPayMoney = Integer.parseInt(dbQuanguoMoney) * monthSize;
		} else {
			needPayMoney = pub_provinces_size * monthsAllMoney;
		}
		tv_needpay.setText(needPayMoney + "元");
	}

	private void PlaceAnOrder() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("sourceType", 1);// 0=充值 1=找产品发布 2=置顶找产品
		map.put("flag", 0);// 0续费发布产品 or 1付款
		map.put("productId", productId);
		map.put("buyMonths", monthSize);
		map.put("moneyAmount", needPayMoney);
		map.put("provinceCodes", provinceCodes);
		map.put("provinceNames", provinceNames);
		map.put("nowBuyEndTime", nowBuyEndTime);
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		LogUtil.d("hl", "PlaceAnOrder=" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
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
				startActivityForResult(new Intent(XuFeiActivity.this,
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
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			final CommonDialog commonDialog = new CommonDialog(
					XuFeiActivity.this, "提示", resp.getHeader().getRetMessage(),
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
			if (monthSize == 0) {
				ToastUtil.showShortToast("续费时长至少一个月！");
				return;
			}
			PlaceAnOrder();
			break;
		case R.id.tv_xufeiTime:
			new WheelSelectDialog(XuFeiActivity.this, new SelectStrListener() {

				@Override
				public void ChooseOneString(int itemIndex, String str) {
					tv_xufeiTime.setText(str);
					monthSize = Integer.parseInt(monthDBList.get(itemIndex)
							.getMonthsValue());
					monthsAllMoney = Integer.parseInt(monthDBList
							.get(itemIndex).getMoney());
					setAllNeedPayMoney();
					setEndDate();
				}
			}, buyMonthStrs).show();
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
		exitTipDialog = new CommonDialog(XuFeiActivity.this, "提示",
				"是否放弃续费并退出?", "确定", "取消");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeThree:
				LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
						.getInstance(XuFeiActivity.this);
				localBroadcastManager.sendBroadcast(new Intent(
						BroadCastAction.ACTION_MYPRODUCT));
				finish();
				break;
			}
		}
	}

}
