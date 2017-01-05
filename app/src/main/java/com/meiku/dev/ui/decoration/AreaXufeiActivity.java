package com.meiku.dev.ui.decoration;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.DecorateOrderCityEntity;
import com.meiku.dev.bean.MkDataConfigReleaseMonths;
import com.meiku.dev.bean.MkDataConfigTopPrice;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;

/**
 * 续费区域
 */
public class AreaXufeiActivity extends BaseActivity {

	/** 购买城市 */
	private TextView tv_city;
	/** 发布时间 */
	private TextView tv_pubTime;
	/** 发布时长 */
	private TextView tv_pubMonth;
	/** 发布费用 */
	private TextView tv_pubMoney;
	private ArrayList<PopupData> monthList;
	private List<MkDataConfigReleaseMonths> monthDBList;
	protected int buyMonthNum;
	private int orderCityId;
	private String validStartTime, validEndTime;
	private String orderAllAmount;
	private TextView tv_allMoney;
	private String cityJsonArrayStr;
	private String validEndTimeAdd;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_areaxufei;
	}

	@Override
	public void initView() {
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_pubTime = (TextView) findViewById(R.id.tv_pubTime);
		tv_pubMonth = (TextView) findViewById(R.id.tv_pubMonth);
		tv_pubMoney = (TextView) findViewById(R.id.tv_pubMoney);
		tv_allMoney = (TextView) findViewById(R.id.tv_allMoney);
	}

	@Override
	public void initValue() {
		orderCityId = getIntent().getIntExtra("orderCityId", 0);
		getArea();
		monthList = new ArrayList<PopupData>();
		monthDBList = MKDataBase.getInstance().getReleaseMonthsList(0);
		for (int i = 0, size = monthDBList.size(); i < size; i++) {
			monthList.add(new PopupData(monthDBList.get(i).getMonthsName(), 0));
		}
		List<MkDataConfigTopPrice> pricesTipList = MKDataBase.getInstance()
				.getTopPriceList(2);
		if (!Tool.isEmpty(pricesTipList)) {
			TextView butAreaTip1 = (TextView) findViewById(R.id.butAreaTip1);
			TextView butAreaTip2 = (TextView) findViewById(R.id.butAreaTip2);
			TextView butAreaTip3 = (TextView) findViewById(R.id.butAreaTip3);
			for (int i = 0; i < pricesTipList.size(); i++) {
				switch (i) {
				case 0:
					butAreaTip1.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					changetext(butAreaTip1, 0, 1, "#FF3939");
					break;
				case 1:
					butAreaTip2.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					changetext(butAreaTip2, 0, 1, "#FF3939");
					break;
				case 2:
					butAreaTip3.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					changetext(butAreaTip3, 0, 1, "#FF3939");
					break;
				default:
					break;
				}
			}
		} else {
			findViewById(R.id.layout_openTips).setVisibility(View.GONE);
		}
	}

	// 改变部分字体颜色
	public void changetext(TextView tv, int start, int resault, String color) {
		SpannableStringBuilder builder = new SpannableStringBuilder(tv
				.getText().toString());
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				Color.parseColor(color));
		builder.setSpan(redSpan, start, resault,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(builder);
	}

	@Override
	public void bindListener() {
		tv_pubMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new NewPopupwindows(AreaXufeiActivity.this, monthList,
						new popwindowListener() {

							@Override
							public void doChoose(int position) {
								tv_pubMonth.setText(monthList.get(position)
										.getName());
								buyMonthNum = Integer.parseInt(monthDBList.get(
										position).getMonthsValue());
								validEndTimeAdd = DateTimeUtil.addDateTime(
										validEndTime, buyMonthNum, 0);
								tv_pubTime.setText(validStartTime + " 至 "
										+ validEndTimeAdd);
								getPubMoney();
							}
						}, 0).show(tv_pubMonth);

			}
		});
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Tool.isEmpty(buyMonthNum) || buyMonthNum == 0) {
					ToastUtil.showShortToast("请选择发布时长");
					return;
				}
				dingDang();
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				cityJsonArrayStr = resp.getBody().get("data").toString();
			}
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("dataList"))
					&& resp.getBody().get("dataList").toString().length() > 2) {
				DecorateOrderCityEntity entity = (DecorateOrderCityEntity) JsonUtil
						.jsonToObj(DecorateOrderCityEntity.class, resp
								.getBody().get("dataList").toString());
				if (!Tool.isEmpty(entity)) {
					validStartTime = entity.getRenewValidStartTime();
					validEndTime = entity.getRenewValidEndTime();
					validEndTimeAdd = validEndTime;
					if (!DateTimeUtil.compareDate(validEndTime)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						String nowStr = sdf.format(new Date());
						validStartTime = nowStr;
						validEndTime = nowStr;
						validEndTimeAdd = validEndTime;
					}
					tv_city.setText(entity.getShowCityName());
					tv_pubTime
							.setText(validStartTime + " 至 " + validEndTimeAdd);

					// List<DecorateOrderCityContentEntity> ocList = entity
					// .getDecorateOrderCityContentList();
					//
					// ArrayList<SelectCityBean> dataList = new
					// ArrayList<SelectCityBean>();
					// HashSet<String> provincesSet = new HashSet<String>();
					// for (int i = 0, size = ocList.size(); i < size; i++) {
					// if (ocList.size() == 1
					// && "100000".equals(ocList.get(0).getCityCode())) {
					// SelectCityBean bean = new SelectCityBean();
					// bean.setCityCode(Integer.parseInt(ocList.get(0)
					// .getCityCode()));
					// bean.setParentCode(Integer.parseInt(ocList.get(0)
					// .getCityCode()));
					// bean.setCityName(ocList.get(0).getCityName());
					// dataList.add(bean);
					// break;
					// }
					// if (ocList.get(0).getCityCode()
					// .equals(ocList.get(0).getProvinceCode())) {
					// SelectCityBean bean = new SelectCityBean();
					// bean.setCityCode(Integer.parseInt(ocList.get(0)
					// .getCityCode()));
					// bean.setParentCode(0);
					// bean.setCityName(ocList.get(0).getCityName());
					//
					// ArrayList<SelectCityBean> cityList = new
					// ArrayList<SelectCityBean>();
					// SelectCityBean cityBean = new SelectCityBean();
					// cityBean.setCityCode(Integer.parseInt(ocList.get(0)
					// .getCityCode()));
					// cityBean.setParentCode(Integer.parseInt(ocList.get(
					// 0).getCityCode()));
					// cityBean.setCityName(ocList.get(0).getCityName());
					// cityList.add(cityBean);
					//
					// bean.setCityJsonArray(JsonUtil
					// .listToJsonArray(cityList));
					// dataList.add(bean);
					// continue;
					// }
					// provincesSet.add(ocList.get(0).getProvinceName());
					// }
					// Iterator<String> iterator = provincesSet.iterator();
					// while (iterator.hasNext()) {
					// SelectCityBean proBean = new SelectCityBean();
					// ArrayList<SelectCityBean> childList = new
					// ArrayList<SelectCityBean>();
					// for (int i = 0, size = ocList.size(); i < size; i++) {
					// if (iterator.next().equals(
					// ocList.get(i).getProvinceName())) {
					// SelectCityBean cityBean = new SelectCityBean();
					// cityBean.setCityCode(Integer.parseInt(ocList
					// .get(i).getCityCode()));
					// cityBean.setParentCode(Integer.parseInt(ocList
					// .get(i).getProvinceCode()));
					// cityBean.setCityName(ocList.get(i)
					// .getCityName());
					// childList.add(cityBean);
					// }
					// }
					// proBean.setCityJsonArray(JsonUtil
					// .listToJsonArray(childList));
					// proBean.setCityCode(childList.get(0).getParentCode());
					// proBean.setParentCode(0);
					// proBean.setCityName(iterator.next());
					// dataList.add(proBean);
					// }
					// cityJsonArray = JsonUtil.listToJsonArray(dataList);
					// LogUtil.e("hl", "组装的地区jsonarray ==》" + cityJsonArray);
				}

			}
			break;
		case reqCodeTwo:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))) {
				orderAllAmount = resp.getBody().get("data").toString();
				tv_allMoney.setText(orderAllAmount + "元");
				tv_pubMoney.setText(orderAllAmount + "元");
			}
			break;
		case reqCodeThree:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				PayOrderGroupEntity payOrdergroup = (PayOrderGroupEntity) JsonUtil
						.jsonToObj(PayOrderGroupEntity.class, resp.getBody()
								.get("data").toString());
				Bundle b = new Bundle();
				b.putSerializable("MkPayOrders", (Serializable) payOrdergroup);
				startActivityForResult(new Intent(AreaXufeiActivity.this,
						PayStyleActivity.class).putExtras(b), reqCodeThree);
			} else {
				ToastUtil.showShortToast("下单失败！");
			}
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
						AreaXufeiActivity.this, "提示", resp.getHeader()
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

	// 获取地区
	public void getArea() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("orderCityId", orderCityId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300034));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
		Log.d("hl", "getArea--" + map);
	}

	private void getPubMoney() {// 后台计算费用
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("validEndTime", validEndTimeAdd);
		map.put("validMonth", buyMonthNum);
		map.put("cityJsonArrayStr", cityJsonArrayStr);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300040));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	public void dingDang() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceType", "10");
		map.put("flag", "0");
		/** 0续费 1付款 */
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		map.put("validMonth", buyMonthNum);// 有效月
		map.put("orderAllAmount", orderAllAmount);// 总付费金额
		map.put("orderCityAmount", orderAllAmount);// 购买地区费金额
		map.put("orderCityId", orderCityId);// 发布城市
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeThree, AppConfig.PUBLICK_APIPAY, req);
	}
}
