package com.meiku.dev.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck
 * 
 * 类描述： 类名称：com.meiku.dev.utils.ScreenUtil 创建人：admin 创建时间：2015-10-30 上午10:22:43
 * 
 * @version V3.0
 */
public class ScreenUtil {
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int SCREEN_DPI;
	private static Resources resources = Resources.getSystem();
	private static float scaledDensity = resources.getDisplayMetrics().scaledDensity;

	public static void initScreenData(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
		SCREEN_DPI = dm.densityDpi;

	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

	/**
	 * 获取屏幕高度
	 */
	@SuppressWarnings("deprecation")
	public static int getWindowHeight(Context cxt) {
		WindowManager wm = (WindowManager) cxt
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();

	}

	/**
	 * 获取屏幕宽度
	 */
	@SuppressWarnings("deprecation")
	public static int getWindowWidth(Context cxt) {
		WindowManager wm = (WindowManager) cxt
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();

	}

	public static int sp2px(float value) {
		float spvalue = value * scaledDensity;
		return (int) (spvalue + 0.5f);
	}

	public static int getStatusBarHeight(Context cxt) {
		int result = 0;
		int resourceId = cxt.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = cxt.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
