package com.meiku.dev.views;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.LinearLayout;

import com.meiku.dev.R;
import com.wheel.widget.OnWheelChangedListener;
import com.wheel.widget.WheelView;
import com.wheel.widget.adapters.NumericWheelAdapter;

public class TimeSelectDialog extends Dialog implements View.OnClickListener {

	public interface CallBackListener {
		void CallBackOfTimeString(String time);
	}

	private CallBackListener listener;
	private Context context;
	private LinearLayout layout;
	private WheelView whell_year;
	private WheelView whell_month;
	private WheelView whell_day;
	private NumericWheelAdapter daynumAdapter;
	public String str_year, str_month, str_day;
	private int year, month, day;
	private NumericWheelAdapter yearAdapter;
	private NumericWheelAdapter monthAdapter;

	private int startYear = 1910;
	private int endYear = 2030;

	public TimeSelectDialog(Context context, CallBackListener listener) {
		super(context, R.style.DialogStyleBottom);
		this.context = context;
		this.listener = listener;
	}

	public TimeSelectDialog(Context context, CallBackListener listener,
			int year, int month, int day) {
		super(context, R.style.DialogStyleBottom);
		this.context = context;
		this.listener = listener;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_selecttime);
		Window window = this.getWindow();
		// 此处可以设置dialog显示的位置
		window.setGravity(Gravity.BOTTOM);
		// 占满屏幕
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		initView();
	}

	private void initView() {
		Calendar calendar = Calendar.getInstance();
		int Systemyear = calendar.get(Calendar.YEAR);
		int SystemMonth = calendar.get(Calendar.MONTH);
		int SystemDay = calendar.get(Calendar.DAY_OF_MONTH);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		whell_year = (WheelView) findViewById(R.id.year);
		yearAdapter = new NumericWheelAdapter(context, startYear, endYear);
		whell_year.setViewAdapter(yearAdapter);

		if (year != 0) {
			whell_year.setCurrentItem(year - startYear);
			str_year = (String) yearAdapter.getItemText(year - startYear);
		} else {
			whell_year.setCurrentItem(Systemyear - startYear);
			str_year = (String) yearAdapter.getItemText(Systemyear - startYear);
		}

		whell_year.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				str_year = (String) yearAdapter.getItemText(newValue);
				Log.v("hl", newValue + "_" + str_year);
				if (2 == Integer.valueOf(str_month)) {
					doUpdateDay();
				}
			}

		});
		whell_year.setCyclic(true);
		whell_year.setInterpolator(new AnticipateOvershootInterpolator());

		whell_month = (WheelView) findViewById(R.id.month);
		whell_day = (WheelView) findViewById(R.id.day);

		monthAdapter = new NumericWheelAdapter(context, 1, 12);
		whell_month.setViewAdapter(monthAdapter);
		if (month != 0) {
			whell_month.setCurrentItem(month - 1);
			str_month = (String) monthAdapter.getItemText(month - 1);
			changDaysByMonth(month);
		} else {
			whell_month.setCurrentItem(SystemMonth);
			str_month = (String) monthAdapter.getItemText(SystemMonth);
			changDaysByMonth(SystemMonth + 1);
		}

		whell_month.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				str_month = (String) monthAdapter.getItemText(newValue);
				Log.v("hl", newValue + "_" + str_month);
				changDaysByMonth(newValue + 1);
			}
		});
		whell_month.setCyclic(true);
		whell_month.setInterpolator(new AnticipateOvershootInterpolator());

		if (day != 0) {
			whell_day.setCurrentItem(day - 1);
			str_day = (String) daynumAdapter.getItemText(day - 1);
		} else {
			whell_day.setCurrentItem(SystemDay - 1);
			str_day = (String) daynumAdapter.getItemText(SystemDay - 1);
		}
		whell_day.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				str_day = (String) daynumAdapter.getItemText(newValue);
				Log.v("hl", newValue + "_" + str_day);
			}
		});
		whell_day.setCyclic(true);
		whell_day.setInterpolator(new AnticipateOvershootInterpolator());
	}

	protected void changDaysByMonth(int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daynumAdapter = new NumericWheelAdapter(context, 1, 31);
			whell_day.setViewAdapter(daynumAdapter);
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			daynumAdapter = new NumericWheelAdapter(context, 1, 30);
			whell_day.setViewAdapter(daynumAdapter);
			break;
		case 2:
			doUpdateDay();
			break;
		default:
			break;
		}
	}

	private void doUpdateDay() {
		int year = Integer.valueOf(str_year);
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			daynumAdapter = new NumericWheelAdapter(context, 1, 29);
			whell_day.setViewAdapter(daynumAdapter);
			whell_day.setCurrentItem(0);
		} else {
			daynumAdapter = new NumericWheelAdapter(context, 1, 28);
			whell_day.setViewAdapter(daynumAdapter);
			whell_day.setCurrentItem(0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			listener.CallBackOfTimeString(str_year + "-" + str_month + "-"
					+ str_day);
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		}

	}
}
