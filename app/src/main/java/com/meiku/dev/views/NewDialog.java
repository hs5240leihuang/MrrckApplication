package com.meiku.dev.views;

import com.meiku.dev.R;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewDialog extends Dialog {

	private int buttonNum;
	private Context context;
	private String title;
	private String message;
	private String confirmButtonText="";
	private String cacelButtonText="";
	private String okButtonText="";
	private String meiku="";
	private String nicheng="";
	private ClickListenerInterface clickListenerInterface;
	private TextView tvMessage;
	private Button tvConfirm;
	private Button tvCancel;
	private Button tvOK;
	private TextView tvmeiku;
	private TextView tvnicheng;
	private LinearLayout linmeiku;
	private LinearLayout linnicheng;

	public interface ClickListenerInterface {
		public void doConfirm();

		public void doCancel();
	}

	public NewDialog(Context context, String title, String message,
			String meiku, String nicheng, String confirmButtonText,
			String cacelButtonText) {
		super(context, R.style.MyDialog);
		this.meiku = meiku;
		this.nicheng = nicheng;
		this.context = context;
		this.title = title;
		this.message = message;
		this.confirmButtonText = confirmButtonText;
		this.cacelButtonText = cacelButtonText;
		buttonNum = 2;
	}

	public NewDialog(Context context, String title, String message,
			String okButtonText) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.message = message;
		this.okButtonText = okButtonText;
		buttonNum = 1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setCancelable(false);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.regis_dialog, null);
		setContentView(view);

		TextView tvTitle = (TextView) view.findViewById(R.id.title);
		linmeiku = (LinearLayout) view.findViewById(R.id.lin_meiku);
		linnicheng = (LinearLayout) view.findViewById(R.id.lin_nicheng);
		tvMessage = (TextView) view.findViewById(R.id.message);
		tvmeiku = (TextView) view.findViewById(R.id.meiku);
		tvnicheng = (TextView) view.findViewById(R.id.nicheng);
		tvConfirm = (Button) view.findViewById(R.id.confirm);
		tvCancel =  (Button) view.findViewById(R.id.cancel);
		tvOK=(Button) view.findViewById(R.id.ok);
		tvTitle.setText(title);
		tvMessage.setText(message);
		tvmeiku.setText(meiku);
		tvnicheng.setText(nicheng);
		tvConfirm.setText(confirmButtonText);
		tvCancel.setText(cacelButtonText);
		tvOK.setText(okButtonText);

		tvConfirm.setOnClickListener(new clickListener());
		tvCancel.setOnClickListener(new clickListener());
		tvOK.setOnClickListener(new clickListener());
		if (buttonNum == 1) {
			tvCancel.setVisibility(View.GONE);
			tvConfirm.setVisibility(View.GONE);
			tvOK.setVisibility(View.VISIBLE);
			linmeiku.setVisibility(View.GONE);
			linnicheng.setVisibility(View.GONE);
		} else {
			tvCancel.setVisibility(View.VISIBLE);
			tvConfirm.setVisibility(View.VISIBLE);
			tvOK.setVisibility(View.GONE);
			linmeiku.setVisibility(View.VISIBLE);
			linnicheng.setVisibility(View.VISIBLE);
		}
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
			case R.id.ok:
				if (clickListenerInterface == null) {
					dismiss();
				} else {
					clickListenerInterface.doConfirm();
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
