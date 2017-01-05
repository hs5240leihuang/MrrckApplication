package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.utils.Tool;

/**
 * 选择省市dialog
 * 
 * @author huanglei
 * 
 */
public class SelectCityDialog extends Dialog {

	private SelectListener listener;
	private ListView listview_pro, listview_city;
	private List<AreaEntity> provincesListData = new ArrayList<AreaEntity>();
	private CommonAdapter<AreaEntity> proAdapter, cityAdapter;
	protected List<AreaEntity> district = new ArrayList<AreaEntity>();
	private Context context;
	private View view;

	public interface SelectListener {
		public void choiseOneCity(String provinceCode, String cityCode,
				String cityName);
	}

	public SelectCityDialog(Context context, SelectListener listener) {
		super(context, R.style.MyDialog2);
		this.context = context;
		this.listener = listener;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.dialog_selectcity, null);
		setContentView(view);
		showTitleBar(false);
		view.findViewById(R.id.left_res_title).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismiss();
					}
				});
		// 省
		listview_pro = (ListView) view.findViewById(R.id.lv_pro);
		getProvinceData();
		proAdapter = new CommonAdapter<AreaEntity>(context,
				R.layout.item_province, provincesListData) {

			@Override
			public void convert(ViewHolder viewHolder, AreaEntity t) {
				TextView txt = viewHolder.getView(R.id.txt);
				txt.setText(t.getCityName());
				if (getSelectedPosition() == viewHolder.getPosition()) {
					txt.setBackgroundColor(context.getResources().getColor(
							R.color.white));
				} else {
					txt.setBackgroundColor(context.getResources().getColor(
							R.color.activity_bg));
				}
			}
		};
		listview_pro.setAdapter(proAdapter);
		for (int i = 0, size = provincesListData.size(); i < size; i++) {

		}
		listview_pro.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				doProvinceClick(arg2);
			}
		});
		// 市
		listview_city = (ListView) view.findViewById(R.id.lv_city);
		cityAdapter = new CommonAdapter<AreaEntity>(context,
				R.layout.item_province, district) {

			@Override
			public void convert(ViewHolder viewHolder, AreaEntity t) {
				viewHolder.setText(R.id.txt, t.getCityName());
			}
		};
		listview_city.setAdapter(cityAdapter);
		listview_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.choiseOneCity(district.get(arg2).getParentCode() + "",
						district.get(arg2).getCityCode() + "",
						district.get(arg2).getCityName());
				dismiss();
			}
		});
		selectIndex(0);
	}

	private void selectIndex(int currnetProIndex) {
		doProvinceClick(currnetProIndex);
		listview_pro.setSelection(currnetProIndex);
	}

	/**
	 * 是否显示标题栏
	 * 
	 * @param show
	 */
	public void showTitleBar(boolean show) {
		view.findViewById(R.id.title_bar).setVisibility(
				show ? View.VISIBLE : View.GONE);
	}

	protected void doProvinceClick(int arg2) {
		district.clear();
		proAdapter.setSelectedPosition(arg2);
		proAdapter.notifyDataSetChanged();
		if (provincesListData.get(arg2).getCityCode() == -1) {
			district.add(addAllCountry());
		} else {
			// 添加一个全省
			AreaEntity ae = new AreaEntity();
			ae.setParentCode(provincesListData.get(arg2).getCityCode());
			ae.setCityCode(provincesListData.get(arg2).getCityCode());
			ae.setCityName(provincesListData.get(arg2).getCityName());
			district.add(ae);
			district.addAll(MKDataBase.getInstance().getDistrict(
					provincesListData.get(arg2).getCityCode()));
		}
		cityAdapter.notifyDataSetChanged();
	}

	/**
	 * 定位当前所在省的位置
	 * 
	 * @param provincesListData
	 * @return
	 */
	private int getCurrentProvinceIndex(String provinceCode) {
		int currentIndex = -1;
		for (int i = 0, size = provincesListData.size(); i < size; i++) {
			if (String.valueOf(provincesListData.get(i).getCityCode()).equals(
					provinceCode)) {
				currentIndex = i;
			}
		}
		return currentIndex;
	}

	/***
	 * 默认选中的省
	 * @param provinceCode
	 */
	public void setSelectIndex(String provinceCode) {
		int currnetProIndex = getCurrentProvinceIndex(provinceCode);
		if (currnetProIndex != -1) {
			selectIndex(currnetProIndex);
		}
	}

	private void getProvinceData() {
		// 添加全国
		provincesListData.add(addAllCountry());
		provincesListData.addAll(MKDataBase.getInstance().getCity());
	}

	/**
	 * 添加一个全国
	 * 
	 * @return
	 */
	private AreaEntity addAllCountry() {
		AreaEntity ae = new AreaEntity();
		ae.setParentCode(-1);
		ae.setCityCode(-1);
		ae.setCityName("全国");
		return ae;
	}

	public void ShowAtLocation(int x, int y) {
		Window dialogWindow = getWindow();
		// dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		// lp.width = (int) (d.widthPixels * 1);
		lp.height = (int) (d.heightPixels * 0.6);
		lp.y = y;
		lp.x = x;
		dialogWindow.setAttributes(lp);
		show();
	}

	/**
	 * 无全国选项（不带全国）
	 * 
	 * @return
	 */
	public void hasNoCountry() {
		provincesListData.remove(0);
	}

}
