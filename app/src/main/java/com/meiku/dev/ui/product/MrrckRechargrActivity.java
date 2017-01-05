package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

public class MrrckRechargrActivity extends BaseActivity {
	private MyGridView myGridView;
	private CommonAdapter<String> commonAdapter;
	private List<String> list = new ArrayList<String>();
	private String[] moneny = { "300元", "500元", "1000元", "3000元", "5000元",
			"10000元" };
	private String[] zeng = { "0元", "50元", "100元", "450元", "750元",
			"2000元" };
	private TextView tvmoney;
	private LinearLayout lin_money;
	private TextView v_money;
	private TextView tv_chongzhi;
	private TextView tv_zengsong;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_meikupay;
	}

	@Override
	public void initView() {
		myGridView = (MyGridView) findViewById(R.id.myGridView);
		v_money = (TextView) findViewById(R.id.v_money);
		tv_chongzhi = (TextView) findViewById(R.id.tv_chongzhi);
		tv_zengsong = (TextView) findViewById(R.id.tv_zengsong);
	}

	@Override
	public void initValue() {
		for (int i = 0; i < moneny.length; i++) {
			list.add(moneny[i]);
		}
		v_money.setText("300元");
		tv_chongzhi.setText("充值：300元");
		tv_zengsong.setText("赠0元");
		commonAdapter = new CommonAdapter<String>(this,
				R.layout.item_meikurecharge, list) {

			@Override
			public void convert(ViewHolder viewHolder, String t) {
				final int position = viewHolder.getPosition();
				tvmoney = viewHolder.getView(R.id.money);
				if (position == getSelectedPosition()) {
					tvmoney.setTextColor(getResources().getColor(R.color.white));
					tvmoney.setBackgroundResource(R.drawable.btn_common_use);
				} else {
					tvmoney.setTextColor(getResources().getColor(R.color.black));
					tvmoney.setBackgroundResource(R.drawable.shape_btn_press);
				}
				viewHolder.setText(R.id.money, t);
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								commonAdapter.setSelectedPosition(position);
								commonAdapter.notifyDataSetChanged();
								for (int i = 0; i < moneny.length; i++) {
									if (i == position) {
										v_money.setText(moneny[i]);
										tv_chongzhi.setText("充值：" + moneny[i]);
										tv_zengsong.setText("赠"+zeng[i]);
									}
								}
							}
						});
			}
		};
		myGridView.setAdapter(commonAdapter);

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
