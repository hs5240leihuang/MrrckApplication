package com.meiku.dev.ui.decoration;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateOrderCityContentEntity;
import com.meiku.dev.bean.MkDataConfigTopPrice;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectCityActivity;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;

//发布地区置顶页面
public class DecorationSetTopActivity extends BaseActivity implements
		OnClickListener {
	private MyGridView gridview;
	private LinearLayout lin_time;
	private LinearLayout lin_area;
	private TextView tv_fabutime;
	private TextView tv_youhui;
	private TextView tv_zhekou;
	private TextView yuanjia;
	private TextView tv_low;
	private TextView tv_selectcity;
	private TextView tv_allmoney;
	private TextView tv_start_time;
	private CommonAdapter<MkDataConfigTopPrice> commonAdapter;
	private int orderCityId;
	private TextView tv_hint;
	private String title;
	private String topStartTime;
	private String topEndTime;
	private Button btn_tijiaodingdan;
	private ArrayList<String> canSelectAreaList = new ArrayList<String>();
	// 后台返回的可以选择的城市
	private List<DecorateOrderCityContentEntity> areaList = new ArrayList<DecorateOrderCityContentEntity>();
	private int orderCityContentIds;
	private String allmoney = "20";
	private String day = "1";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_decoratron_settop;
	}

	@Override
	public void initView() {
		btn_tijiaodingdan = (Button) findViewById(R.id.btn_tijiaodingdan);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		tv_allmoney = (TextView) findViewById(R.id.tv_allmoney);
		tv_selectcity = (TextView) findViewById(R.id.tv_selectcity);
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		gridview = (MyGridView) findViewById(R.id.gridview);
		lin_time = (LinearLayout) findViewById(R.id.lin_time);
		lin_area = (LinearLayout) findViewById(R.id.lin_area);
		tv_fabutime = (TextView) findViewById(R.id.tv_fabutime);
		tv_youhui = (TextView) findViewById(R.id.tv_youhui);
		tv_zhekou = (TextView) findViewById(R.id.tv_zhekou);
		yuanjia = (TextView) findViewById(R.id.yuanjia);
		tv_low = (TextView) findViewById(R.id.tv_low);
		tv_youhui.setText("￥20元");
		yuanjia.setText("（原价￥20元）");
		tv_low.setText("（低至20元/天）");
		tv_zhekou.setText("（无折扣）");
		tv_allmoney.setText("￥20元");
		changetext(3, 5, "#FF3939");
	}

	@Override
	public void initValue() {
		orderCityId = getIntent().getIntExtra("orderCityId", -1);
		getCity();
		List<MkDataConfigTopPrice> butTopPricesList = MKDataBase.getInstance()
				.getTopPriceList(0);
		commonAdapter = new CommonAdapter<MkDataConfigTopPrice>(
				DecorationSetTopActivity.this, R.layout.item_decorationsettop,
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
								tv_allmoney.setText("￥" + t.getNowPrice() + "元");
								allmoney = t.getNowPrice();
								day = t.getDay();
								tv_youhui.setText("￥" + t.getNowPrice() + "元");
								yuanjia.setText("（原价￥" + t.getCostPrice()
										+ "元）");
								tv_low.setText("（低至"
										+ t.getLowestPriceEveryDay() + "元/天）");
								tv_zhekou.setText("（" + t.getLoseNum() + "）");
								changetext(3, 5, "#FF3939");
								if ("有效时间".equals(tv_start_time.getText()
										.toString())) {
									tv_fabutime.setText(topStartTime
											+ "-"
											+ addDateTime(
													topEndTime,
													null,
													Integer.parseInt(t.getDay())));
								}
							}
						});

			}
		};
		gridview.setAdapter(commonAdapter);

	}

	@Override
	public void bindListener() {
		lin_time.setOnClickListener(this);
		lin_area.setOnClickListener(this);
		btn_tijiaodingdan.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("000000", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			title = resp.getBody().get("data").getAsString();
			tv_hint.setText(title);
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("dataList"))
					&& resp.getBody().get("dataList").toString().length() > 2) {
				areaList = (List<DecorateOrderCityContentEntity>) JsonUtil
						.jsonToList(
								resp.getBody().get("dataList").toString(),
								new TypeToken<List<DecorateOrderCityContentEntity>>() {
								}.getType());
				for (int i = 0, size = areaList.size(); i < size; i++) {
					canSelectAreaList.add(areaList.get(i).getCityCode());
				}
				Log.e("hl", "发布案例--提供的可选择城市数==>" + canSelectAreaList.size());
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
						new Intent(DecorationSetTopActivity.this,
								PayStyleActivity.class).putExtras(b),
						reqCodeThree);
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
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						DecorationSetTopActivity.this, "提示", resp.getHeader()
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
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_time:
			if (Tool.isEmpty(tv_selectcity.getText().toString())) {
				ToastUtil.showShortToast("请先选择地区");
			} else {
				if ("有效时间".equals(tv_start_time.getText().toString())) {
					ToastUtil.showShortToast("时间不可选择");
				} else {
					new TimeSelectDialog(DecorationSetTopActivity.this,
							new TimeSelectDialog.CallBackListener() {
								@Override
								public void CallBackOfTimeString(String time) {
									tv_fabutime.setText(time);
								}
							}).show();
				}

			}

			break;
		case R.id.lin_area:
			if (Tool.isEmpty(canSelectAreaList)) {
				ToastUtil.showShortToast("没有可供发布的地区！");
			} else {// 选择提供的城市
				startActivityForResult(
						new Intent(this, SelectCityActivity.class)
								.putExtra("selectType",
										ConstantKey.SELECT_AREA_HASAERA)
								.putStringArrayListExtra("canSelectcitys",
										canSelectAreaList)
								.putExtra("isSingleSelect", true), reqCodeOne);
			}
			break;
		case R.id.btn_tijiaodingdan:
			tiJiao();
			break;
		default:
			break;
		}
	}

	// 改变部分字体颜色
	public void changetext(int start, int resault, String color) {
		SpannableStringBuilder builder = new SpannableStringBuilder(tv_low
				.getText().toString());
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				Color.parseColor(color));
		builder.setSpan(redSpan, start, resault,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_low.setText(builder);
	}

	// 获取发布地区
	public void getCity() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("orderCityId", orderCityId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300030));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
		Log.d("wangke", orderCityId + "");
	}

	// 提交订单
	public void tiJiao() {
		if (Tool.isEmpty(tv_selectcity.getText().toString())) {
			ToastUtil.showShortToast("请选择发布地区");
			return;
		}
		if (Tool.isEmpty(tv_fabutime.getText().toString())) {
			ToastUtil.showShortToast("请选择开始时间");
			return;
		}
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		map.put("sourceType", 11);
		map.put("orderCityId", orderCityId);
		map.put("orderCityContentId", orderCityContentIds);
		if ("有效时间".equals(tv_start_time.getText().toString())) {
			map.put("topStartTime", topStartTime);
		} else {
			map.put("topStartTime", tv_fabutime.getText().toString());
		}
		map.put("orderTopDayAmount", allmoney);
		map.put("days", day);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
		Log.d("wangke", map + "");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				String selectCityCode = data.getStringExtra("cityCode");
				String orderCityNames = data.getStringExtra("cityName");
				tv_selectcity.setText(orderCityNames);
				LogUtil.e("hl", "选择发布城市=>" + orderCityNames + "///"
						+ selectCityCode);
				if (!Tool.isEmpty(selectCityCode)) {
					for (int j = 0, areaSize = areaList.size(); j < areaSize; j++) {
						if (areaList.get(j).getCityCode()
								.equals(selectCityCode)) {
							orderCityContentIds = areaList.get(j).getId();
						}
					}
				}
				LogUtil.e("hl", "选择发布城市 对应的orderCityContentIds=>"
						+ orderCityContentIds);
				for (int j = 0, areaSize = areaList.size(); j < areaSize; j++) {
					if (areaList.get(j).getId() == orderCityContentIds) {
						topStartTime = areaList.get(j).getTopStartTime();
						topEndTime = areaList.get(j).getTopEndTime();
					}
				}

				if (Tool.isEmpty(topEndTime)) {
					tv_start_time.setText("开始时间");
					tv_fabutime.setText("");
				} else {
					if (compareDate(topEndTime)) {
						tv_start_time.setText("有效时间");
						tv_fabutime.setText(topStartTime + "-" + topEndTime);
					} else {
						tv_start_time.setText("开始时间");
						tv_fabutime.setText("");
					}

				}
			}
			if (requestCode == reqCodeThree) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	/**
	 * 
	 * @Description: 时间与当前时间比较
	 * @Title: compareDate
	 * @param timeStr
	 *            要比较的时间字符串（格式:yyyy-MM-dd）
	 * @return
	 */
	public boolean compareDate(String timeStr) {
		if (!checkNotNull(timeStr)) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowStr = sdf.format(new Date());
		try {
			Date nowDt = sdf.parse(nowStr);
			Date timeDt = sdf.parse(timeStr);
			if (timeDt.getTime() >= nowDt.getTime()) {
				return true;
			}

			return false;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @Description: 时间增加M月D日
	 * @Title: addDateTime
	 * @param str
	 *            初始时间字符串（格式:yyyy-MM-dd）
	 * @return
	 */
	public static String addDateTime(String str, Integer month, Integer day) {
		String retStr = "";
		if (!checkNotNull(str)) {
			return retStr;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = sdf.parse(str);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			if (month != null && month > 0) {
				rightNow.add(Calendar.MONTH, month);// 日期加M个月
			}
			if (day != null && day > 0) {
				rightNow.add(Calendar.DAY_OF_YEAR, day);// 日期加D天
			}
			Date nowdt = rightNow.getTime();
			retStr = sdf.format(nowdt);
		} catch (ParseException e) {
		}
		return retStr;
	}

	/**
	 * 判断一个字符串是否为 NUll 或为空 返回false
	 * 
	 * @param inStr
	 *            inStr
	 * @return boolean
	 */
	public static boolean checkNotNull(String str) {
		boolean flag = false;

		if (str != null && str.trim().length() != 0)
			flag = true;
		return flag;
	}
}
