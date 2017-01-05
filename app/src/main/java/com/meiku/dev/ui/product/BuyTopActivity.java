package com.meiku.dev.ui.product;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MkDataConfigTopPrice;
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
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;

/**
 * 置顶单独下单购买
 */
public class BuyTopActivity extends BaseActivity implements OnClickListener {
	private MyGridView gridview;
	private CommonAdapter<MkDataConfigTopPrice> commonAdapter;
	private TextView tv_money, tv_original_price, tv_money_perday,
			tv_starttime, tv_name;
	private Button btnOK;
	private LinearLayout layout_starttime;
	private String productId;
	private String topDays = "1";// 置顶天数（默认选择1天）
	private double spendMoney = 20;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_buy_top;
	}

	@Override
	public void initView() {
		gridview = (MyGridView) findViewById(R.id.gridview);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_original_price = (TextView) findViewById(R.id.tv_original_price);
		tv_money_perday = (TextView) findViewById(R.id.tv_money_perday);
		layout_starttime = (LinearLayout) findViewById(R.id.layout_starttime);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		btnOK = (Button) findViewById(R.id.btnOK);
		tv_name = (TextView) findViewById(R.id.tv_name);
	}

	@Override
	public void initValue() {
		List<MkDataConfigTopPrice> butTopPricesList = MKDataBase.getInstance()
				.getTopPriceList(1);
		if (!Tool.isEmpty(butTopPricesList)) {
			spendMoney = Integer
					.parseInt(butTopPricesList.get(0).getNowPrice());
			// tv_topzhekou.setText("（" + butTopPricesList.get(0).getLoseNum()
			// + "）");
			tv_money.setText("￥" + spendMoney + "元");
			tv_original_price.setText("（原价￥"
					+ butTopPricesList.get(0).getCostPrice() + "元）");
			tv_money_perday
					.setText("（"+butTopPricesList.get(0).getLoseNum()+",低至"
							+ butTopPricesList.get(0).getLowestPriceEveryDay()
							+ "元/天）");
		}

		commonAdapter = new CommonAdapter<MkDataConfigTopPrice>(
				BuyTopActivity.this, R.layout.item_decorationsettop,
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
								tv_money.setText("￥" + t.getNowPrice() + "元");
								tv_original_price.setText("（原价￥"
										+ t.getCostPrice() + "元）");
								tv_money_perday.setText("（"+t.getLoseNum()+",低至"
										+ t.getLowestPriceEveryDay() + "元/天）");
								topDays = t.getDay();
								spendMoney = Integer.parseInt(t.getNowPrice());
							}
						});

			}
		};
		gridview.setAdapter(commonAdapter);
		productId = getIntent().getStringExtra("productId");
		tv_name.setText(getIntent().getStringExtra("productName"));
	}

	@Override
	public void bindListener() {
		btnOK.setOnClickListener(this);
		layout_starttime.setOnClickListener(this);
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
				startActivityForResult(new Intent(BuyTopActivity.this,
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
					BuyTopActivity.this, "提示",
					resp.getHeader().getRetMessage(), "确定");
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btnOK:
			if (Tool.isEmpty(tv_starttime.getText().toString())) {
				ToastUtil.showShortToast("请选择置顶时间");
				return;
			}
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo()
					.getUserId());
			map.put("sourceType", 2);// 0=充值 1=找产品发布 2=置顶找产品
			map.put("productId", productId);
			map.put("topStartTime", tv_starttime.getText().toString());
			map.put("topDays", topDays);
			map.put("spendMoney", spendMoney);
			map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
			LogUtil.d("hl", "PlaceAnOrder=" + map);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
			break;
		case R.id.layout_starttime:
			Calendar calendar = Calendar.getInstance();
			int Systemyear = calendar.get(Calendar.YEAR);
			int SystemMonth = calendar.get(Calendar.MONTH);
			int SystemDay = calendar.get(Calendar.DAY_OF_MONTH);
			new TimeSelectDialog(BuyTopActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_starttime.setText(time);
						}
					}, Systemyear, SystemMonth + 1, SystemDay).show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeThree:
				LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
						.getInstance(BuyTopActivity.this);
				localBroadcastManager.sendBroadcast(new Intent(
						BroadCastAction.ACTION_MYPRODUCT));
				finish();
				break;
			}
		}
	}
}
