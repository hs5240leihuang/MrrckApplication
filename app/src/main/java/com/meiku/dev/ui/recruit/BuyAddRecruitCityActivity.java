package com.meiku.dev.ui.recruit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyInfoCountEntity;
import com.meiku.dev.bean.MkDataConfigTopPrice;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;

/**
 * 购买新增招聘宝城市
 */
public class BuyAddRecruitCityActivity extends BaseActivity {

	private TextView tv_vipType, tv_vipMonth, tv_starttime, tv_endtime,
			tv_city, tv_tip, tv_money, tv_allmoney_open, tv_allmoney_bottom;
	private String selectCityCode;
	private String selectProvinceCode;
	private String wholeCode;
	private String selectCityName;
	private String selectProvinceName;
	private String wholeName;
	private ImageView iv_vipImg;
	private CompanyInfoCountEntity companyinfocountentity;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_addrecruitcity;
	}

	@Override
	public void initView() {
		tv_vipType = (TextView) findViewById(R.id.tv_vipType);
		tv_vipMonth = (TextView) findViewById(R.id.tv_vipMonth);
		iv_vipImg = (ImageView) findViewById(R.id.iv_vipImg);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		tv_endtime = (TextView) findViewById(R.id.tv_endtime);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_allmoney_open = (TextView) findViewById(R.id.tv_allmoney_open);
		tv_allmoney_bottom = (TextView) findViewById(R.id.tv_allmoney_bottom);
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Tool.isEmpty(tv_city.getText().toString())) {
					ToastUtil.showShortToast("请选择城市");
					return;
				}
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getUserId());
				map.put("sourceType", 4);
				map.put("phone", AppContext.getInstance().getUserInfo()
						.getPhone());
				if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getCompanyEntity())) {
					map.put("companyId", AppContext.getInstance().getUserInfo()
							.getCompanyEntity().getId());
				} else {
					map.put("companyId", -1);
				}
				map.put("wholeCode", wholeCode);
				map.put("wholeName", wholeName);
				map.put("provinceCode", selectProvinceCode);
				map.put("cityCode", selectCityCode);
				map.put("cityName", selectCityName);
				map.put("provinceName", selectProvinceName);
				LogUtil.d("hl", "PlaceAnOrder=" + map);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
				req.setBody(JsonUtil.Map2JsonObj(map));
				httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
			}
		});
		tv_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(
						new Intent(BuyAddRecruitCityActivity.this,
								RecruitSelectCityActivity.class).putExtra(
								"selectType", ConstantKey.SELECT_AREA_ALL)
								.putExtra("fromOpenPermission", true),
						reqCodeOne);
			}
		});
	}

	@Override
	public void initValue() {
		getinformation();
		List<MkDataConfigTopPrice> pricesTipList = MKDataBase.getInstance()
				.getTopPriceList(3);
		if (!Tool.isEmpty(pricesTipList)) {
			TextView butAreaTipTitle = (TextView) findViewById(R.id.butAreaTipTitle);
			TextView butAreaTip1 = (TextView) findViewById(R.id.butAreaTip1);
			TextView butAreaTip2 = (TextView) findViewById(R.id.butAreaTip2);
			TextView butAreaTip3 = (TextView) findViewById(R.id.butAreaTip3);
			TextView butAreaTip4 = (TextView) findViewById(R.id.butAreaTip4);
			for (int i = 0; i < pricesTipList.size(); i++) {
				switch (i) {
				case 0:
					butAreaTipTitle.setText(pricesTipList.get(i).getNowPrice());
					break;
				case 1:
					butAreaTip1.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					Util.setTVShowCloTxt(butAreaTip1, butAreaTip1.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				case 2:
					butAreaTip2.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					Util.setTVShowCloTxt(butAreaTip2, butAreaTip2.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				case 3:
					butAreaTip3.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					Util.setTVShowCloTxt(butAreaTip3, butAreaTip3.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				case 4:
					butAreaTip4.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					Util.setTVShowCloTxt(butAreaTip4, butAreaTip4.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				default:
					break;
				}
			}
		} else {
			findViewById(R.id.layout_openTips).setVisibility(View.GONE);
		}

	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "__" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				Map<String, String> map = JsonUtil.jsonToMap(resp.getBody()
						.get("data").toString());
				if (map.containsKey("money")) {
					String payMoney = map.get("money");
					tv_money.setText("¥" + payMoney + "元");
					tv_allmoney_open.setText("¥" + payMoney + "元");
					tv_allmoney_bottom.setText("¥" + payMoney + "元");
				}
				if (map.containsKey("moneyRemark")) {
					tv_tip.setText(map.get("moneyRemark"));
				}
			}
			break;
		case reqCodeTwo:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				PayOrderGroupEntity payOrdergroup = (PayOrderGroupEntity) JsonUtil
						.jsonToObj(PayOrderGroupEntity.class, resp.getBody()
								.get("data").toString());
				Bundle b = new Bundle();
				b.putSerializable("MkPayOrders", (Serializable) payOrdergroup);
				startActivityForResult(
						new Intent(BuyAddRecruitCityActivity.this,
								PayStyleActivity.class).putExtras(b),
						reqCodeTwo);
			} else {
				ToastUtil.showShortToast("下单失败！");
			}
			break;
		case reqCodeThree:
			if (resp.getBody().get("company").toString().length() > 2) {
				String json = resp.getBody().get("company").toString();
				companyinfocountentity = (CompanyInfoCountEntity) JsonUtil
						.jsonToObj(CompanyInfoCountEntity.class, json);
			}
			tv_vipType.setText(companyinfocountentity.getVipTypeName());
			tv_vipMonth.setText(companyinfocountentity.getMonthName());
			tv_starttime.setText(companyinfocountentity.getStartVipDate());
			tv_endtime.setText(companyinfocountentity.getEndVipDate());
			BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.display(iv_vipImg,
					companyinfocountentity.getVipTypeImgUrl());
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
		case reqCodeThree:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						BuyAddRecruitCityActivity.this, "提示", resp.getHeader()
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				String multiSelectedProvincesNames = data
						.getStringExtra("provinceName");// 选中的所有省名称
				LogUtil.e("hl", "选择的省==》" + multiSelectedProvincesNames);
				// 选择全国
				if ("全国".equals(multiSelectedProvincesNames)) {
					wholeName = "全国";
					tv_city.setText(wholeName);
					wholeCode = ConstantKey.CITYCODE_QUANGUO + "";

					selectCityCode = "";
					selectCityName = "";
					selectProvinceCode = "";
					selectProvinceName = "";
					getNeedPaymoney();
					return;
				}
				tv_city.setText(data.getStringExtra("showSelect").replaceAll(
						",", "  "));
				wholeName = "";
				selectCityCode = data.getStringExtra("cityCode");
				selectProvinceCode = data.getStringExtra("provinceCode");
				selectCityName = data.getStringExtra("cityName");
				selectProvinceName = multiSelectedProvincesNames;
				LogUtil.e("hl", "选择的城市==》" + selectCityName);
				LogUtil.e("hl", "选择的cityCode==》" + selectCityCode);
				LogUtil.e("hl", "选择的provinceCode==》" + selectProvinceCode);
				if (tv_city.getLineCount() == 1) {
					tv_city.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
				} else {
					tv_city.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				}
				wholeCode = "";
				getNeedPaymoney();
			} else if (requestCode == reqCodeTwo) {
				sendBroadcast(new Intent(
						BroadCastAction.ACTION_RECRUIT_ADD_CITY));
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	private void getNeedPaymoney() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("wholeCode", wholeCode);
		map.put("wholeName", wholeName);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		map.put("cityName", selectCityName);
		map.put("provinceName", selectProvinceName);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZPB_BUYCITYMONEY));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

	private void getinformation() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		req.setHeader(new ReqHead(AppConfig.ADDPUBLISHCITY_INFORMATION_90064));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("wangke", "————" + map);
		httpPost(reqCodeThree, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

}
