package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * 选择招商省份
 * 
 */
public class ZhaoShangProvincesActivity extends BaseActivity {

	private GridView gridview;
	private CommonAdapter<AreaEntity> showAdapter;
	private List<AreaEntity> provincesData = new ArrayList<AreaEntity>();
	protected String allProvinces = "";
	protected String allCityCode = "";
	private String oldSelectedProvincesCode;
	private String[] oldPricinceCode;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.acticity_zhaoshangprovinces;
	}

	@Override
	public void initView() {
		findViewById(R.id.tip).setVisibility(View.VISIBLE);
		AreaEntity wholeCountry = new AreaEntity();
		wholeCountry.setCityName("全国");
		wholeCountry.setCityCode(100000);
		provincesData.add(wholeCountry);
		provincesData.addAll(MKDataBase.getInstance().getCity());
		oldSelectedProvincesCode = getIntent().getStringExtra("OLD_SELECT");
		setCancleAllSelect(Tool.isEmpty(oldSelectedProvincesCode));
		gridview = (GridView) findViewById(R.id.gridview);
		showAdapter = new CommonAdapter<AreaEntity>(
				ZhaoShangProvincesActivity.this, R.layout.item_txt,
				provincesData) {

			@Override
			public void convert(ViewHolder viewHolder, final AreaEntity t) {
				Button tv = viewHolder.getView(R.id.txt);
				tv.setText(t.getCityName());
				if (t.getDelStatus() == 1) {
					tv.setBackgroundResource(R.drawable.shape_btn_press);
					tv.setTextColor(Color.WHITE);
				} else {
					tv.setBackgroundResource(R.drawable.shape_btn_normal);
					// tv.setTextColor(Color.parseColor("#ff504d"));
					tv.setTextColor(getResources().getColor(R.color.gray));
				}
				final int position = viewHolder.getPosition();
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (position != 0) {
							provincesData.get(0).setDelStatus(0);
							provincesData.get(position)
									.setDelStatus(
											provincesData.get(position)
													.getDelStatus() == 1 ? 0
													: 1);
						} else {
							setCancleAllSelect(true);
							provincesData.get(0).setDelStatus(1);
						}
						showAdapter.notifyDataSetChanged();

					}
				});
			}
		};
		gridview.setAdapter(showAdapter);
		findViewById(R.id.right_txt_title).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						for (int i = 0, provincesSize = provincesData.size(); i < provincesSize; i++) {
							if (provincesData.get(i).getDelStatus() == 1) {
								allProvinces += provincesData.get(i)
										.getCityName() + ",";
								allCityCode += provincesData.get(i)
										.getCityCode() + ",";
							}
						}
						Intent intent = new Intent();
						if (!Tool.isEmpty(allProvinces)) {
							allProvinces = allProvinces.substring(0,
									allProvinces.length() - 1);
						}
						if (!Tool.isEmpty(allCityCode)) {
							allCityCode = allCityCode.substring(0,
									allCityCode.length() - 1);
						}
						intent.putExtra("allProvinces", allProvinces);
						intent.putExtra("allCityCode", allCityCode);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
	}

	/**
	 * 
	 * @param cancleAllSelect
	 *            true=取消所有选择， false =设为默认初始选择
	 */
	private void setCancleAllSelect(boolean cancleAllSelect) {
		for (int i = 0, provincesSize = provincesData.size(); i < provincesSize; i++) {
			provincesData.get(i).setDelStatus(0);// 使用DelStatus做选择标记
		}
		if (!cancleAllSelect) {
			oldPricinceCode = oldSelectedProvincesCode.split(",");
			for (int m = 0; m < oldPricinceCode.length; m++) {
				for (int n = 0, provincesSize = provincesData.size(); n < provincesSize; n++) {
					if (oldPricinceCode[m].equals(provincesData.get(n)
							.getCityCode()+"")) {
						provincesData.get(n).setDelStatus(1);// 使用DelStatus做选择标记
					}
				}
			}

		}

	}

	@Override
	public void initValue() {
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
