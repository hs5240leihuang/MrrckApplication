package com.meiku.dev.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;

/**
 * Toast信息，每次执行toast无需等待，当前toast内容直接替换之前内容
 * 
 * @date 2015-1-28
 */
public class ToastUtil {
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};
	private static TextView showTv;

	public static void showToast(Context mContext, String text, int duration) {
		if (!Tool.isEmpty(mContext)) {
			mHandler.removeCallbacks(r);
			if (mToast != null && showTv != null) {
				showTv.setText(text);
			} else {
				View v = LayoutInflater.from(mContext).inflate(
						R.layout.toast, null);
				showTv = (TextView) v.findViewById(R.id.text);
				showTv.setText(text);
				mToast = new Toast(mContext);
				mToast.setView(v);
			}
			mToast.setGravity(Gravity.CENTER, 0,
					ScreenUtil.dip2px(mContext, 10));
			mHandler.postDelayed(r, duration);

			mToast.show();
		}

	}

	public static void showShortToast(String msg) {
		showToast(MrrckApplication.getInstance(), msg, 2000);
	}

	public static void showLongToast(String msg) {
		showToast(MrrckApplication.getInstance(), msg, 5000);
	}
}
