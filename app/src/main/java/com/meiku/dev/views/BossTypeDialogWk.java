package com.meiku.dev.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.meiku.dev.R;
import com.meiku.dev.utils.ScreenUtil;

public class BossTypeDialogWk extends Dialog {

	private Context context;
	private ImageView imageView;

	/**
	 * 初始化Dialog Button * 2
	 * 
	 * @param context
	 *            context
	 * @param title
	 *            标题
	 * @param message
	 *            详细信息
	 * @param confirmButtonText
	 *            确定按钮的文字
	 * @param cacelButtonText
	 *            取消按钮的文字
	 */
	public BossTypeDialogWk(Context context) {
		super(context, R.style.MyDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setCancelable(false);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.bosstypedialog_wk, null);
		setContentView(view);

		imageView = (ImageView) view.findViewById(R.id.img_cancel);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) ScreenUtil.SCREEN_WIDTH; // 高度设置为屏幕的0.6
		lp.height = (int) ScreenUtil.SCREEN_HEIGHT;
		dialogWindow.setAttributes(lp);
	}

}
