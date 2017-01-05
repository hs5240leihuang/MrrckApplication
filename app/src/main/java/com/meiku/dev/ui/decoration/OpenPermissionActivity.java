package com.meiku.dev.ui.decoration;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.DecorateOrderCityContentEntity;
import com.meiku.dev.bean.MkDataConfigReleaseMonths;
import com.meiku.dev.bean.MkDataConfigTopPrice;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.SelectCityBean;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectCityActivity;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;

/**
 * 开通装修广告发布权限
 * 
 */
public class OpenPermissionActivity extends BaseActivity implements
		OnClickListener {

	/** 购买城市 */
	private TextView tv_city;
	/** 发布时间 */
	private TextView tv_pubTime;
	/** 发布时长 */
	private TextView tv_pubMonth;
	/** 发布费用 */
	private TextView tv_pubMoney;
	/** 发布费用优惠提示 */
	private TextView tv_pubMoneyTip;
	/** 置顶开关 */
	private ImageView topSwitch;
	/** 置顶时间 */
	private TextView tv_topTime;
	/** 置顶费用 */
	private TextView tv_topMoney;
	/** 置顶费用优惠提示 */
	private TextView tv_topMoneyTip, tv_realTopMoney, tv_topzhekou;
	/** 置顶天数选择gridview */
	private MyGridView gridview;
	/** 总费用 */
	private TextView tv_allMoney;
	/** 提交按钮 */
	private Button btnOK;
	/** 置顶折叠布局 */
	private LinearLayout setTopLatout;
	private boolean openTop = false;
	private ArrayList<PopupData> monthList;
	private CommonAdapter<MkDataConfigTopPrice> commonAdapter;
	private List<MkDataConfigReleaseMonths> monthDBList;
	private final int BUYCITY = 2;
	private TextView topTip, topName, centerTitle;
	private ArrayList<String> canSelectAreaList = new ArrayList<String>();
	private JsonArray cityJsonArray;// 最终返回接口的选择省市JsonArray
	private int usetype;// 页面用处类型0开通权限，1购买城市
	private TextView tv_pubMoney_bottom, tv_buyMonth_bottom,
			tv_topMoney_bottom, tv_topArea_bottom, tv_allmoney_bottom;
	private static HashMap<String, List<AreaEntity>> mapALLselectData;
	protected int buyMonthNum = 12;// 默认12个月
	private int orderTopDayAmount;// 置顶单价
	private String topDays = "1";// 置顶天数（默认选择1天）
	private boolean forceCloseTop = false;// 是否强制关闭置顶（达到置顶条件）
	private String orderAllAmount;
	private String orderCityAmount;
	private String orderTopAllAmount;
	private String orderTopFlagMessage = "发布地区为全国或包含省不可设置置顶";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_openpermission;
	}

	@Override
	public void initView() {
		centerTitle = (TextView) findViewById(R.id.center_txt_title);
		topName = (TextView) findViewById(R.id.topName);
		topTip = (TextView) findViewById(R.id.topTip);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_pubTime = (TextView) findViewById(R.id.tv_pubTime);
		tv_pubMonth = (TextView) findViewById(R.id.tv_pubMonth);
		tv_pubMoney = (TextView) findViewById(R.id.tv_pubMoney);
		tv_pubMoneyTip = (TextView) findViewById(R.id.tv_pubMoneyTip);
		tv_topzhekou = (TextView) findViewById(R.id.tv_topzhekou);
		topSwitch = (ImageView) findViewById(R.id.topSwitch);
		setTopLatout = (LinearLayout) findViewById(R.id.setTopLatout);
		setTopLatout.setVisibility(View.GONE);
		tv_topTime = (TextView) findViewById(R.id.tv_topTime);
		tv_topMoney = (TextView) findViewById(R.id.tv_topMoney);
		tv_topMoneyTip = (TextView) findViewById(R.id.tv_topMoneyTip);
		tv_realTopMoney = (TextView) findViewById(R.id.tv_realTopMoney);
		gridview = (MyGridView) findViewById(R.id.gridview);
		tv_allMoney = (TextView) findViewById(R.id.tv_allMoney);
		btnOK = (Button) findViewById(R.id.btnOK);

		tv_pubMoney_bottom = (TextView) findViewById(R.id.tv_pubMoney_bottom);
		tv_buyMonth_bottom = (TextView) findViewById(R.id.tv_buyMonth_bottom);
		tv_topMoney_bottom = (TextView) findViewById(R.id.tv_topMoney_bottom);
		tv_topMoney_bottom.setText("0元");
		tv_topArea_bottom = (TextView) findViewById(R.id.tv_topArea_bottom);
		tv_allmoney_bottom = (TextView) findViewById(R.id.tv_allmoney_bottom);
	}

	@Override
	public void initValue() {
		checkIsHasOpenCitys();
		usetype = getIntent().getIntExtra("usetype", 0);
		if (usetype == 0) {
			centerTitle.setText("开通装修广告发布");
			topName.setText("开通装修广告发布");
		} else if (usetype == 1) {
			centerTitle.setText("开通装修广告发布地区");
			topName.setText("开通装修广告发布地区");
		} else if (usetype == 2) {
			centerTitle.setText("续费装修广告发布地区");
			topName.setText("续费装修广告发布地区");
		}
		monthList = new ArrayList<PopupData>();
		monthDBList = MKDataBase.getInstance().getReleaseMonthsList(0);
		for (int i = 0, size = monthDBList.size(); i < size; i++) {
			monthList.add(new PopupData(monthDBList.get(i).getMonthsName(), 0));
		}
		tv_pubMonth.setText(buyMonthNum + "个月");
		List<MkDataConfigTopPrice> butTopPricesList = MKDataBase.getInstance()
				.getTopPriceList(0);
		if (!Tool.isEmpty(butTopPricesList)) {
			orderTopDayAmount = Integer.parseInt(butTopPricesList.get(0)
					.getNowPrice());
			tv_topzhekou.setText("（" + butTopPricesList.get(0).getLoseNum()
					+ "）");
			tv_topMoney.setText("￥" + orderTopDayAmount + "元");
			tv_topMoneyTip.setText("（原价￥"
					+ butTopPricesList.get(0).getCostPrice() + "元）");
			tv_realTopMoney
					.setText("（低至"
							+ butTopPricesList.get(0).getLowestPriceEveryDay()
							+ "元/天）");
			Util.setTVShowCloTxt(tv_realTopMoney, tv_realTopMoney.getText()
					.toString(), 3, 5, Color.parseColor("#FF3939"));
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
					Util.setTVShowCloTxt(butAreaTip1, butAreaTip1.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				case 1:
					
					butAreaTip2.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					Util.setTVShowCloTxt(butAreaTip2, butAreaTip2.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				case 2:
					butAreaTip3.setText("*"
							+ pricesTipList.get(i).getNowPrice());
					Util.setTVShowCloTxt(butAreaTip3, butAreaTip3.getText()
							.toString(), 0, 1, Color.parseColor("#FF3939"));
					break;
				default:
					break;
				}
			}
		} else {
			findViewById(R.id.layout_openTips).setVisibility(View.GONE);
		}
		commonAdapter = new CommonAdapter<MkDataConfigTopPrice>(
				OpenPermissionActivity.this, R.layout.item_decorationsettop,
				butTopPricesList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final MkDataConfigTopPrice t) {

				final int position = viewHolder.getPosition();
				if (position == getSelectedPosition()) {
					TextView data = viewHolder.getView(R.id.tv_data);
					data.setBackgroundColor(Color.parseColor("#FF3499"));
					data.setTextColor(Color.parseColor("#FFFFFF"));
				} else {
					TextView data = viewHolder.getView(R.id.tv_data);
					data.setBackgroundResource(R.drawable.shape_ingwithoutroung);
					data.setTextColor(Color.parseColor("#FA3396"));
				}
				viewHolder.setText(R.id.tv_data,
						t.getDay() + "天 " + t.getNowPrice() + "元");
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								commonAdapter.setSelectedPosition(position);
								commonAdapter.notifyDataSetChanged();
								tv_topMoney.setText("￥" + t.getNowPrice() + "元");
								tv_topzhekou.setText("（" + t.getLoseNum() + "）");
								tv_topMoneyTip.setText("（原价￥"
										+ t.getCostPrice() + "元）");
								tv_realTopMoney.setText("（低至"
										+ t.getLowestPriceEveryDay() + "元/天）");
								Util.setTVShowCloTxt(tv_realTopMoney,
										tv_realTopMoney.getText().toString(),
										3, 5, Color.parseColor("#FF3939"));
								//
								topDays = t.getDay();
								orderTopDayAmount = Integer.parseInt(t
										.getNowPrice());
								getPubMoneyAndTopMoney();
							}
						});

			}
		};
		gridview.setAdapter(commonAdapter);
	}

	private void checkIsHasOpenCitys() {// 检测是否有开通城市
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300029));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	public void bindListener() {
		tv_city.setOnClickListener(this);
		tv_pubTime.setOnClickListener(this);
		tv_pubMonth.setOnClickListener(this);
		topSwitch.setOnClickListener(this);
		tv_topTime.setOnClickListener(this);
		btnOK.setOnClickListener(this);
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
				topTip.setText(resp.getBody().get("data").getAsString());
			}
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("dataList"))
					&& resp.getBody().get("dataList").toString().length() > 2) {
				List<DecorateOrderCityContentEntity> areaList = (List<DecorateOrderCityContentEntity>) JsonUtil
						.jsonToList(
								resp.getBody().get("dataList").toString(),
								new TypeToken<List<DecorateOrderCityContentEntity>>() {
								}.getType());
				for (int i = 0, size = areaList.size(); i < size; i++) {
					canSelectAreaList.add(areaList.get(i).getCityCode());
				}
			}
			break;
		case reqCodeTwo:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				Map<String, String> resultMap = JsonUtil.jsonToMap(resp
						.getBody().get("data").toString());
				// 是否能够操作置顶标记 0 能置顶, 1 不能置顶
				if (resultMap.containsKey("orderTopFlag")) {
					if ("1".equals(resultMap.get("orderTopFlag"))) {// 关闭置顶
						setTopLatout.setVisibility(View.GONE);
						topSwitch
								.setBackgroundResource(R.drawable.ios7_switch_off);
						openTop = false;
						forceCloseTop = true;
					} else {
						forceCloseTop = false;
					}
				}
				if (resultMap.containsKey("orderCityAmount")) {// 城市购买的钱
					orderCityAmount = resultMap.get("orderCityAmount");
					tv_pubMoney.setText(orderCityAmount + "元");
					tv_pubMoney_bottom.setText(orderCityAmount + "元");
					tv_buyMonth_bottom.setText(buyMonthNum + "个月");
				}
				if (resultMap.containsKey("orderAllAmount")) {// 共计钱
					orderAllAmount = resultMap.get("orderAllAmount");
					tv_allMoney.setText(orderAllAmount + "元");
					tv_allmoney_bottom.setText(orderAllAmount + "元");
				}
				if (resultMap.containsKey("orderTopAllAmount")) {// 置顶的钱
					orderTopAllAmount = resultMap.get("orderTopAllAmount");
					if (openTop) {
						tv_topMoney_bottom.setText(orderTopAllAmount + "元");
					} else {
						tv_topMoney_bottom.setText("0元");
					}
				}
				if (resultMap.containsKey("orderCityCount")) {// 区域数
					if (openTop) {
						tv_topArea_bottom.setText(resultMap
								.get("orderCityCount") + "个地区");
					} else {
						tv_topArea_bottom.setText("");
					}
				}
				if (resultMap.containsKey("showCityName")) {// 共计钱
					tv_city.setText(resultMap.get("showCityName"));
				}
				if (resultMap.containsKey("orderTopFlagMessage")) {// 共计钱
					orderTopFlagMessage = resultMap.get("orderTopFlagMessage");
				}

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
				startActivityForResult(new Intent(OpenPermissionActivity.this,
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
						OpenPermissionActivity.this, "提示", resp.getHeader()
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
		case R.id.tv_city:
			if (Tool.isEmpty(canSelectAreaList)) {// 没有买过城市，则可以买全国范围
				startActivityForResult(
						new Intent(this, SelectCityActivity.class).putExtra(
								"selectType", ConstantKey.SELECT_AREA_ALL)
								.putExtra("fromOpenPermission", true), BUYCITY);
			} else {// 提供的城市以外的可以购买
				startActivityForResult(
						new Intent(this, SelectCityActivity.class)
								.putExtra("selectType",
										ConstantKey.SELECT_AREA_NOTHAS)
								.putStringArrayListExtra("canSelectcitys",
										canSelectAreaList)
								.putExtra("fromOpenPermission", true), BUYCITY);
			}
			break;
		case R.id.tv_pubTime:
			new TimeSelectDialog(OpenPermissionActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_pubTime.setText(time);
							getPubMoneyAndTopMoney();
						}
					}).show();
			break;
		case R.id.tv_pubMonth:
			new NewPopupwindows(OpenPermissionActivity.this, monthList,
					new popwindowListener() {

						@Override
						public void doChoose(int position) {
							tv_pubMonth.setText(monthList.get(position)
									.getName());
							buyMonthNum = Integer.parseInt(monthDBList.get(
									position).getMonthsValue());
							getPubMoneyAndTopMoney();
						}
					}, 0).show(v);
			break;
		case R.id.topSwitch:
			if (Tool.isEmpty(tv_city.getText().toString())
					|| Tool.isEmpty(cityJsonArray)) {
				ToastUtil.showShortToast("请先选择发布地区");
				return;
			}
			if (Tool.isEmpty(tv_pubTime.getText().toString())) {
				ToastUtil.showShortToast("请先选择发布时间");
				return;
			}
			if (forceCloseTop) {
				ToastUtil.showShortToast(orderTopFlagMessage);
				return;
			}
			if (openTop) {
				setTopLatout.setVisibility(View.GONE);
				topSwitch.setBackgroundResource(R.drawable.ios7_switch_off);
				openTop = false;
			} else {
				setTopLatout.setVisibility(View.VISIBLE);
				topSwitch.setBackgroundResource(R.drawable.ios7_switch_on);
				openTop = true;
			}
			getPubMoneyAndTopMoney();
			break;
		case R.id.tv_topTime:
			new TimeSelectDialog(OpenPermissionActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							try {
								if (!Tool.isEmpty(tv_pubTime)) {
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd");
									Date topData = sdf.parse(time);
									Date buyData = sdf.parse(tv_pubTime
											.getText().toString());
									if (DateTimeUtil.compare(buyData, topData) > 0) {
										ToastUtil
												.showShortToast("置顶时间需要在发布时间之后！");
										return;
									}
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							tv_topTime.setText(time);
							getPubMoneyAndTopMoney();
						}
					}).show();
			break;
		case R.id.btnOK:

			if (Tool.isEmpty(tv_city.getText().toString())
					|| Tool.isEmpty(cityJsonArray)) {
				ToastUtil.showShortToast("请选择发布地区");
				return;
			}
			if (Tool.isEmpty(tv_pubTime.getText().toString())) {
				ToastUtil.showShortToast("请选择发布时间");
				return;
			}
			if (openTop && Tool.isEmpty(tv_topTime.getText().toString())) {
				ToastUtil.showShortToast("请选择置顶开始时间");
				return;
			}
			if (openTop && !Tool.isEmpty(tv_pubTime)
					&& !Tool.isEmpty(tv_topTime)) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date topData;

					topData = sdf.parse(tv_topTime.getText().toString());
					Date buyData = sdf.parse(tv_pubTime.getText().toString());
					if (DateTimeUtil.compare(buyData, topData) > 0) {
						ToastUtil.showShortToast("置顶时间需要在发布时间之后！");
						return;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dingDang();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeThree) {
				setResult(RESULT_OK);
				finish();
			}

			if (requestCode == BUYCITY) {
				String multiSelectedProvincesNames = data
						.getStringExtra("provinceName");// 选中的所有省名称
				LogUtil.e("hl", "选择所包含的省==》" + multiSelectedProvincesNames);
				List<SelectCityBean> selectedDataList = new ArrayList<SelectCityBean>();// 最终组装的list
				// 选择全国
				if ("全国".equals(multiSelectedProvincesNames)) {
					SelectCityBean scb = new SelectCityBean();
					scb.setCityName("全国");
					scb.setCityCode(100000);
					scb.setParentCode(100000);
					selectedDataList.add(scb);
					cityJsonArray = JsonUtil.listToJsonArray(selectedDataList);
					LogUtil.e("hl", "组装的地区jsonarray ==》" + cityJsonArray);
					tv_city.setText("全国");
					getPubMoneyAndTopMoney();
					return;
				}

				if (getMapALLCityData() != null
						&& !Tool.isEmpty(multiSelectedProvincesNames)) {
					String[] selectPros = multiSelectedProvincesNames
							.split(",");
					List<AreaEntity> allProvincesLiast = MKDataBase
							.getInstance().getCity();
					// 拼装jsonarray
					for (int i = 0, selectProSize = selectPros.length; i < selectProSize; i++) {
						for (int j = 0, allproSize = allProvincesLiast.size(); j < allproSize; j++) {
							if (selectPros[i].equals(allProvincesLiast.get(j)
									.getCityName())) {
								// 组装当前省
								SelectCityBean scb = new SelectCityBean();
								scb.setCityName(allProvincesLiast.get(j)
										.getCityName());
								scb.setCityCode(allProvincesLiast.get(j)
										.getCityCode());
								scb.setParentCode(allProvincesLiast.get(j)
										.getParentCode());
								// 所选的城市过滤
								List<AreaEntity> cityList = getMapALLCityData()
										.get(selectPros[i]);
								List<SelectCityBean> selectedCityList = new ArrayList<SelectCityBean>();
								for (int k = 0, citySize = cityList.size(); k < citySize; k++) {
									if (cityList.get(k).getDelStatus() == 1) {
										SelectCityBean citybean = new SelectCityBean();
										citybean.setCityName(cityList.get(k)
												.getCityName());
										citybean.setCityCode(cityList.get(k)
												.getCityCode());
										citybean.setParentCode(cityList.get(k)
												.getParentCode());
										selectedCityList.add(citybean);
									}
								}
								// 组装省下的城市jsonarray
								scb.setCityJsonArray(JsonUtil
										.listToJsonArray(selectedCityList));
								selectedDataList.add(scb);
							}
						}
					}
					cityJsonArray = JsonUtil.listToJsonArray(selectedDataList);
					LogUtil.e("hl", "组装的地区jsonarray ==》" + cityJsonArray);
					tv_city.setText(data.getStringExtra("showSelect"));
					getPubMoneyAndTopMoney();
				}

			}
		}
	}

	public HashMap<String, List<AreaEntity>> getMapALLCityData() {
		return mapALLselectData;
	}

	public static void setMapALLCityData(
			HashMap<String, List<AreaEntity>> mapALLCityData) {
		mapALLselectData = mapALLCityData;
	}

	private void getPubMoneyAndTopMoney() {// 后台计算费用
		if (Tool.isEmpty(cityJsonArray)) {
			return;
		}
		// if (openTop && !Tool.isEmpty(tv_topTime.getText().toString())) {//
		// return;
		// }
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("cityJsonArray", cityJsonArray);
		map.put("validStartTime", tv_pubTime.getText().toString());
		map.put("validMonth", buyMonthNum);

		map.put("topFlag", openTop ? 0 : 1); // 0 置顶 1 未置顶
		map.put("days", topDays);
		map.put("topStartTime", tv_topTime.getText().toString());
		map.put("orderTopDayAmount", orderTopDayAmount);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300039));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	public void dingDang() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (usetype == 0 || usetype == 1) {
			map.put("sourceType", "10");
			map.put("flag", "1");
			/** 0续费 1付款 */
		} else {
			map.put("sourceType", "");
			map.put("flag", "0");
			/** 0续费 1付款 */
		}
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		map.put("validStartTime", tv_pubTime.getText().toString());// 有效开始时间
		map.put("validMonth", buyMonthNum);// 有效月
		map.put("topFlag", openTop ? 0 : 1); // 0 置顶 1 未置顶
		map.put("topStartTime", tv_topTime.getText().toString());
		map.put("days", topDays);
		map.put("orderAllAmount", orderAllAmount);// 总付费金额
		map.put("orderCityAmount", orderCityAmount);// 购买地区费金额
		map.put("orderTopAllAmount", orderTopAllAmount);// 置顶总费金额
		map.put("orderTopDayAmount", orderTopDayAmount);// 置顶费金额/每天费
		map.put("cityJsonArray", cityJsonArray);// 发布城市
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeThree, AppConfig.PUBLICK_APIPAY, req);
	}
}
