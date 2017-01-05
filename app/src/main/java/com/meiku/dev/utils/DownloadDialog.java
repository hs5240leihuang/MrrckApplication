package com.meiku.dev.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meiku.dev.R;

public class DownloadDialog extends Dialog {

	private Context context;
	private TextView tvPro;
	private ProgressBar progressBar;
	private boolean isForceUpdate;

	public TextView getTvPro() {
		return tvPro;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public DownloadDialog(Context context, boolean isForceUpdate) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.isForceUpdate = isForceUpdate;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCancelable(isForceUpdate);
		setContentView(R.layout.dialog_appdownload);
		init();
	}

	private void init() {
		progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
		tvPro = (TextView) findViewById(R.id.pro);
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		// lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
		lp.width = (int) ScreenUtil.dip2px(context, 302);
		dialogWindow.setAttributes(lp);
		dialogWindow.setGravity(Gravity.BOTTOM);
	}
}
