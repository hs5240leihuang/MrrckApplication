package com.meiku.dev.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.utils.ToastUtil;

public class CommonDialog extends Dialog {

	private int buttonNum;
	private Context context;
	private String title;
	private String message;
	private String confirmButtonText;
	private String cacelButtonText;
	private ClickListenerInterface clickListenerInterface;
	private TextView tvMessage;
	private TextView tvConfirm;
	private TextView tvCancel;

	public interface ClickListenerInterface {
		public void doConfirm();

		public void doCancel();
	}

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
	public CommonDialog(Context context, String title, String message,
			String confirmButtonText, String cacelButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.message = message;
		this.confirmButtonText = confirmButtonText;
		this.cacelButtonText = cacelButtonText;
		buttonNum = 2;
	}

	/**
	 * 初始化Dialog Button * 1
	 * 
	 * @param context
	 *            context
	 * @param title
	 *            标题
	 * @param message
	 *            详细信息
	 * @param confirmButtonText
	 *            确定按钮的文字
	 */
	public CommonDialog(Context context, String title, String message,
			String confirmButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.message = message;
		this.confirmButtonText = confirmButtonText;
		buttonNum = 1;
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
		View view = inflater.inflate(R.layout.common_newdialog, null);
		setContentView(view);

		TextView tvTitle = (TextView) view.findViewById(R.id.title);
		tvMessage = (TextView) view.findViewById(R.id.message);
		tvConfirm = (TextView) view.findViewById(R.id.confirm);
		tvCancel = (TextView) view.findViewById(R.id.cancel);
		View line = (View) view.findViewById(R.id.line);

		if (1 == buttonNum) {
			tvCancel.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}

		tvTitle.setText(title);
		tvMessage.setText(message);
		tvConfirm.setText(confirmButtonText);
		tvCancel.setText(cacelButtonText);

		tvConfirm.setOnClickListener(new clickListener());
		tvCancel.setOnClickListener(new clickListener());

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
		dialogWindow.setAttributes(lp);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.confirm:
				if (clickListenerInterface == null) {
					dismiss();
				} else {
					clickListenerInterface.doConfirm();
				}
				break;
			case R.id.cancel:
				if (clickListenerInterface == null) {
					dismiss();
				} else {
					clickListenerInterface.doCancel();
				}
				break;
			}
		}
	}

	// 改变部分字体颜色
	public void changetext(int start, int resault, int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder(tvMessage
				.getText().toString());
		ForegroundColorSpan redSpan = new ForegroundColorSpan(context
				.getResources().getColor(color));
		builder.setSpan(redSpan, start, resault,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvMessage.setText(builder);
	}

	// 设置按钮颜色
	public void setButton(int color, int color1) {
		tvConfirm.setBackgroundResource(color);
		tvConfirm.setTextColor(context.getResources().getColor(color1));
	}

	public void setContentLeft() {
		tvMessage.setGravity(Gravity.LEFT);
	}
}