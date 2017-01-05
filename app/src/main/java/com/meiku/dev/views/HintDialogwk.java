package com.meiku.dev.views;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.R;

public class HintDialogwk extends Dialog {

	private int buttonNum;
	private Context context;
	private String message;
	private String confirmButtonText;
	private String cacelButtonText;
	private String oneButtonText;
	private ClickListenerInterface clickListenerInterface;
	private DoOneClickListenerInterface doOneClickListenerInterface;
	private TextView tvMessage;
	private TextView tvConfirm;
	private TextView tvCancel;
	private TextView tvone;
	private ImageView imageView;

	public interface ClickListenerInterface {
		public void doConfirm();

		public void doCancel();
	}
	public interface DoOneClickListenerInterface {
		public void doOne();
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
	public HintDialogwk(Context context, String message,
			String confirmButtonText, String cacelButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
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
	public HintDialogwk(Context context, String message, String oneButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.message = message;
		this.oneButtonText = oneButtonText;
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
		View view = inflater.inflate(R.layout.hintdialog_wk, null);
		setContentView(view);

		tvMessage = (TextView) view.findViewById(R.id.message);
		tvConfirm = (TextView) view.findViewById(R.id.confirm);
		tvCancel = (TextView) view.findViewById(R.id.cancel);
		tvone = (TextView) view.findViewById(R.id.btn_go);
		imageView = (ImageView) view.findViewById(R.id.img_cancel);

		if (1 == buttonNum) {
			tvCancel.setVisibility(View.GONE);
			tvConfirm.setVisibility(View.GONE);
			tvone.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.VISIBLE);
		} else {
			tvCancel.setVisibility(View.VISIBLE);
			tvConfirm.setVisibility(View.VISIBLE);
			tvone.setVisibility(View.GONE);
			imageView.setVisibility(View.GONE);
		}

		tvMessage.setText(message);
		tvConfirm.setText(confirmButtonText);
		tvCancel.setText(cacelButtonText);
		tvone.setText(oneButtonText);
		tvConfirm.setOnClickListener(new clickListener());
		tvCancel.setOnClickListener(new clickListener());
		tvone.setOnClickListener(new clickListener());
		imageView.setOnClickListener(new clickListener());
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 1); // 高度设置为屏幕的0.6
		lp.height = (int) (d.heightPixels * 1);
		dialogWindow.setAttributes(lp);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}
	public void setOneClicklistener(DoOneClickListenerInterface doOneClickListenerInterface) {
		this.doOneClickListenerInterface = doOneClickListenerInterface;
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
			case R.id.btn_go:
				if (doOneClickListenerInterface == null) {
					dismiss();
				} else {
					doOneClickListenerInterface.doOne();
				}
				break;
			case R.id.img_cancel:
				dismiss();
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
