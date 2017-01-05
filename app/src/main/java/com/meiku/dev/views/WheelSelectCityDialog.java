package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.meiku.dev.R;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.wheel.widget.OnWheelChangedListener;
import com.wheel.widget.WheelView;
import com.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 滚轮选择省市
 * 
 */
public class WheelSelectCityDialog extends Dialog {

	private Context context;
	private SelectCityListener listener;
	private WheelView wheel_provinces;
	private ArrayWheelAdapter<String> adapter_provinces;
	private WheelView wheel_citys;
	private List<AreaEntity> provincesData = new ArrayList<AreaEntity>();
	private String[] provincesShowStr;
	private List<AreaEntity> citysData = new ArrayList<AreaEntity>();
	private String[] citysShowStr;
	private boolean hasAll;
	private ArrayWheelAdapter<String> adapter_citys;
	protected String selectedProvinceName, selectedCityName;
	protected int selectedProvinceCode, selectedCityCode;

	public interface SelectCityListener {
		void ChooseOneCity(int provinceCode, String provinceName, int cityCode,
				String cityName);
	}

	/**
	 * 
	 * @param context
	 * @param hasAll
	 *            是否有全国
	 * @param listener
	 */
	public WheelSelectCityDialog(Context context, boolean hasAll,
			SelectCityListener listener) {
		super(context, R.style.DialogStyleBottom);
		this.context = context;
		this.hasAll = hasAll;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_wheelselectcity);
		initView();
		Window window = this.getWindow();
		// 此处可以设置dialog显示的位置
		window.setGravity(Gravity.BOTTOM);
		// 占满屏幕
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	private void initView() {
		provincesData.clear();
		if (hasAll) {
			AreaEntity allCountry = new AreaEntity();
			allCountry.setCityCode(-1);
			allCountry.setCityName("全国");
			provincesData.add(allCountry);
		}
		provincesData.addAll(MKDataBase.getInstance().getCity());
		if (Tool.isEmpty(provincesData)) {
			ToastUtil.showShortToast("获取省市数据失败！");
			dismiss();
			return;
		}
		provincesShowStr = new String[provincesData.size()];
		for (int i = 0, size = provincesData.size(); i < size; i++) {
			provincesShowStr[i] = provincesData.get(i).getCityName();
		}
		wheel_provinces = (WheelView) findViewById(R.id.wheel_provinces);
		adapter_provinces = new ArrayWheelAdapter<String>(context,
				provincesShowStr);
		adapter_provinces.setTextColor(0x111111);
		wheel_provinces.setViewAdapter(adapter_provinces);
		wheel_provinces.setVisibleItems(5);
		wheel_provinces.setCurrentItem(0);
		wheel_provinces.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setCityByProvinces(newValue);
			}
		});
		wheel_citys = (WheelView) findViewById(R.id.wheel_citys);
		wheel_citys.setVisibleItems(5);
		setCityByProvinces(0);
		wheel_citys.setCurrentItem(0);
		wheel_citys.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				selectedCityName = citysData.get(newValue).getCityName();
				selectedCityCode = citysData.get(newValue).getCityCode();
						//citysData.get(newValue).getId();
			}
		});
		findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.ChooseOneCity(selectedProvinceCode,
						selectedProvinceName, selectedCityCode,
						selectedCityName);
				dismiss();
			}
		});
		findViewById(R.id.cancle).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismiss();
					}
				});
	}

	/**
	 * 根据当前省份设置城市
	 * 
	 * @param index
	 */
	protected void setCityByProvinces(int index) {
		citysData.clear();
		selectedProvinceName = provincesData.get(index).getCityName();
		selectedProvinceCode = provincesData.get(index).getCityCode();
				//provincesData.get(index).getId();
		if (hasAll && index == 0) {// 选择全国，
			AreaEntity ae = new AreaEntity();
			ae.setParentCode(provincesData.get(0).getCityCode());
			ae.setCityCode(-1);
			ae.setCityName("全国");
			citysData.add(ae);
		} else if ("上海市".equals(selectedProvinceName)
				|| "天津市".equals(selectedProvinceName)
				|| "重庆市".equals(selectedProvinceName)
				|| "北京市".equals(selectedProvinceName)) {// 过滤直辖市
			AreaEntity ae = new AreaEntity();
			ae.setParentCode(provincesData.get(index).getCityCode());
			ae.setCityCode(provincesData.get(index).getCityCode());
			ae.setCityName(selectedProvinceName);
			citysData.add(ae);
		} else {
			citysData.addAll(MKDataBase.getInstance().getDistrict(
					provincesData.get(index).getCityCode()));
		}
		if (Tool.isEmpty(citysData)) {//台湾，香港等没有子级，显示父级
			AreaEntity ae = new AreaEntity();
			ae.setParentCode(provincesData.get(index).getCityCode());
			ae.setCityCode(provincesData.get(index).getCityCode());
			ae.setCityName(selectedProvinceName);
			citysData.add(ae);
		}
		citysShowStr = new String[citysData.size()];
		for (int i = 0, size = citysData.size(); i < size; i++) {
			citysShowStr[i] = citysData.get(i).getCityName();
		}
		adapter_citys = new ArrayWheelAdapter<String>(context, citysShowStr);
		adapter_citys.setTextColor(0x111111);
		wheel_citys.setViewAdapter(adapter_citys);
		selectedCityName = citysData.get(0).getCityName();
		//selectedCityCode = citysData.get(0).getId();
		selectedCityCode = citysData.get(0).getCityCode();
	}
}
