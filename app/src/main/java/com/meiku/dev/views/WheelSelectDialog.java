package com.meiku.dev.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.meiku.dev.R;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;
import com.wheel.widget.OnWheelChangedListener;
import com.wheel.widget.OnWheelClickedListener;
import com.wheel.widget.WheelView;
import com.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 滚轮选择string的dialog
 * 
 */
public class WheelSelectDialog extends Dialog implements OnWheelChangedListener {

	public interface SelectStrListener {
		void ChooseOneString(int itemIndex, String str);
	}

	private SelectStrListener listener;
	private Context context;
	private WheelView wheel;
	private String[] showList;
	private ArrayWheelAdapter<String> adapter;
	private int selectedIndex;
	private int defaultIndex = 0;

	public WheelSelectDialog(Context context) {
		super(context);
	}

	public WheelSelectDialog(Context context, SelectStrListener listener,
			String[] showList) {
		super(context, R.style.alertdialog);
		this.context = context;
		this.showList = showList;
		this.listener = listener;
	}

	public WheelSelectDialog(Context context, int defaultIndex,
			SelectStrListener listener, String[] showList) {
		super(context, R.style.alertdialog);
		this.context = context;
		this.defaultIndex = defaultIndex;
		this.showList = showList;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_selectstring);
		initView();
	}

	private void initView() {
		wheel = (WheelView) findViewById(R.id.wheel);
		wheel.setLayoutParams(new LinearLayout.LayoutParams(
				ScreenUtil.SCREEN_WIDTH, LayoutParams.WRAP_CONTENT));
		adapter = new ArrayWheelAdapter<String>(context, showList);
		wheel.setViewAdapter(adapter);
		// 设置可见条目数量
		wheel.setVisibleItems(5);
		wheel.setCurrentItem(defaultIndex);
		wheel.addChangingListener(this);
		findViewById(R.id.shadow).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismiss();
					}
				});
		findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.ChooseOneString(selectedIndex, showList[selectedIndex]);
				dismiss();
			}
		});
		findViewById(R.id.cancle).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// listener.ChooseOneString(selectedIndex,
						// showList[selectedIndex]);
						dismiss();
					}
				});
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		selectedIndex = newValue;
	}
}
