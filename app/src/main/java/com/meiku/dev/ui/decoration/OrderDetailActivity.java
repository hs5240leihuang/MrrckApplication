package com.meiku.dev.ui.decoration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;

public class OrderDetailActivity extends BaseActivity implements
		OnClickListener {
	private String orderGroupNumber;
	private PayOrderGroupEntity mkPayOrder;
	private TextView tv_ordername;
	private TextView tv_onstatus;
	private LinearLayout lin_picture;
	private TextView tv_detail;
	private TextView tv_money;
	private TextView tv_dingdanbianhao;
	private TextView tv_xiadanshijian;
	private TextView tv_chengjiaoshijian;
	private Button btn_shanchudingdan, btn_quzhifu, btn_quxiaodingdan;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_order_detail;
	}

	@Override
	public void initView() {
		btn_shanchudingdan = (Button) findViewById(R.id.btn_shanchudingdan);
		btn_quzhifu = (Button) findViewById(R.id.btn_quzhifu);
		btn_quxiaodingdan = (Button) findViewById(R.id.btn_quxiaodingdan);
		tv_chengjiaoshijian = (TextView) findViewById(R.id.tv_chengjiaoshijian);
		tv_xiadanshijian = (TextView) findViewById(R.id.tv_xiadanshijian);
		tv_dingdanbianhao = (TextView) findViewById(R.id.tv_dingdanbianhao);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_detail = (TextView) findViewById(R.id.tv_detail);
		lin_picture = (LinearLayout) findViewById(R.id.lin_picture);
		tv_onstatus = (TextView) findViewById(R.id.tv_onstatus);
		tv_ordername = (TextView) findViewById(R.id.tv_ordername);
	}

	@Override
	public void initValue() {
		orderGroupNumber = getIntent().getStringExtra("orderGroupNumber");
		getData();
	}

	@Override
	public void bindListener() {
		btn_shanchudingdan.setOnClickListener(this);
		btn_quzhifu.setOnClickListener(this);
		btn_quxiaodingdan.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke1", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				mkPayOrder = (PayOrderGroupEntity) JsonUtil.jsonToObj(
						PayOrderGroupEntity.class, resp.getBody().get("data")
								.toString());
				tv_ordername.setText(mkPayOrder.getOrderName().replaceAll("%%",
						"\n"));
				tv_onstatus.setText(mkPayOrder.getOrderStatusName());
				lin_picture.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MySimpleDraweeView img = new MySimpleDraweeView(
						OrderDetailActivity.this);
				lin_picture.addView(img, layoutParams);
				img.setUrlOfImage(mkPayOrder.getClientWorkFileUrl());
				tv_detail.setText(mkPayOrder.getOrderContent().replaceAll("%%",
						"\n"));
				tv_money.setText("￥" + mkPayOrder.getOrderAllAmount());
				tv_dingdanbianhao.setText(mkPayOrder.getOrderGroupNumber());
				tv_xiadanshijian.setText(mkPayOrder.getCreateDate());
				tv_chengjiaoshijian.setText(mkPayOrder.getPayTime());
				
				Integer orderPayType = mkPayOrder.getOrderPayType();
				if (orderPayType == 1) {
					btn_quzhifu.setVisibility(View.GONE);
					btn_quxiaodingdan.setVisibility(View.GONE);
				} else {
					if (orderPayType == 2) {
						btn_quzhifu.setVisibility(View.VISIBLE);
						btn_quxiaodingdan.setVisibility(View.VISIBLE);
					} else {
						btn_quzhifu.setVisibility(View.GONE);
						btn_quxiaodingdan.setVisibility(View.GONE);
					}
				}
			} else {
				ToastUtil.showShortToast("没有更多数据");
			}
			break;
		case reqCodeTwo:
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeThree:
			setResult(RESULT_OK);
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			btn_quzhifu.setVisibility(View.GONE);
			btn_quxiaodingdan.setVisibility(View.GONE);
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
		case reqCodeThree:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						OrderDetailActivity.this, "提示", resp.getHeader()
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

	// 请求订单详情
	public void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderGroupNumber", orderGroupNumber);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_22006));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_APIPAY, req);
	}

	// 删除订单
	public void Delete() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderGroupNumber", orderGroupNumber);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_22004));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
	}

	// 取消订单
	public void cancel() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("orderGroupNumber", orderGroupNumber);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_22007));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_APIPAY, req);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_shanchudingdan:
			final CommonDialog commonDialog = new CommonDialog(
					OrderDetailActivity.this, "提示", "确定删除该订单？", "确定", "取消");
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							commonDialog.dismiss();
							Delete();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
			commonDialog.show();
			break;
		case R.id.btn_quzhifu:
			Bundle b = new Bundle();
			b.putSerializable("MkPayOrders", (Serializable) mkPayOrder);
			startActivityForResult(new Intent(OrderDetailActivity.this,
					PayStyleActivity.class).putExtras(b), reqCodeOne);
			break;
		case R.id.btn_quxiaodingdan:
			cancel();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			setResult(RESULT_OK);
			getData();
		}
	}
}
