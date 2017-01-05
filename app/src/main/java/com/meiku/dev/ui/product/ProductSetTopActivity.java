package com.meiku.dev.ui.product;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MkDataConfigTopPrice;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * 产品置顶选择
 */
public class ProductSetTopActivity extends BaseActivity {
	private GridView gridview;
	private TextView tv_yuanjian, tv_money, tv_low_money, tv_allmoney, tv_name;
	private Button btnOK;
	private int requestmoney;
	private int setTopTimeString = 1;
	private CommonAdapter<MkDataConfigTopPrice> commonAdapter;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_product_settop;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return false;
	}

	@Override
	public void initView() {
		btnOK = (Button) findViewById(R.id.btnOK);
		btnOK.setText("确定置顶");
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("favourable", requestmoney);
				intent.putExtra("setTopTimeString", setTopTimeString);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		((TextView) findViewById(R.id.leftTv)).setText("置顶金额");
		findViewById(R.id.goback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						setResult(RESULT_OK);
						finish();
					}
				});
		tv_yuanjian = (TextView) findViewById(R.id.tv_yuanjian);
		tv_low_money = (TextView) findViewById(R.id.tv_low_money);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_allmoney = (TextView) findViewById(R.id.tv_allmoney);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_name.setText(getIntent().getStringExtra("productName"));
		gridview = (GridView) findViewById(R.id.gridview);
	}

	@Override
	public void initValue() {
		List<MkDataConfigTopPrice> butTopPricesList = MKDataBase.getInstance()
				.getTopPriceList(1);
		if (!Tool.isEmpty(butTopPricesList)) {
			requestmoney = Integer.parseInt(butTopPricesList.get(0)
					.getNowPrice());
			tv_money.setText("￥" + requestmoney + "元");
			tv_allmoney.setText("￥" + requestmoney + "元");
			tv_yuanjian.setText("（原价￥" + butTopPricesList.get(0).getCostPrice()
					+ "元）");
			tv_low_money.setText("（" + butTopPricesList.get(0).getLoseNum()
					+ "，（低至" + butTopPricesList.get(0).getLowestPriceEveryDay()
					+ "元/天）");
		}
		commonAdapter = new CommonAdapter<MkDataConfigTopPrice>(
				ProductSetTopActivity.this, R.layout.item_decorationsettop,
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
								tv_allmoney.setText("￥" + t.getNowPrice() + "元");
								tv_yuanjian.setText("（原价￥" + t.getCostPrice()
										+ "元）");
								tv_low_money.setText("（" + t.getLoseNum()
										+ "，低至" + t.getLowestPriceEveryDay()
										+ "元/天）");
								requestmoney = Integer.parseInt(t.getNowPrice());
								setTopTimeString = Integer.parseInt(t.getDay());
							}
						});

			}
		};
		gridview.setAdapter(commonAdapter);
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
