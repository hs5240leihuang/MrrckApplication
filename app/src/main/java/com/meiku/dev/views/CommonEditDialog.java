package com.meiku.dev.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

public class CommonEditDialog extends Dialog {

	private Context context;
	private EditClickOkListener listener;
	private String title;
	private String hint;
	private EditText et;
	private int maxLength;
	private String oldStr;
	private boolean singleLine;

	public interface EditClickOkListener {
		public void doConfirm(String inputString);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param hint
	 *            提示
	 * @param oldStr
	 *            之前旧Str
	 * @param maxLength
	 *            最大长度
	 * @param listener
	 *            监听器返回输入值
	 */
	public CommonEditDialog(Context context, String title, String hint,
			String oldStr, int maxLength, boolean singleLine,
			EditClickOkListener listener) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.hint = hint;
		this.maxLength = maxLength;
		this.singleLine = singleLine;
		this.oldStr = oldStr;
		this.listener = listener;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_commonedit);
		init();
	}

	private void init() {
		et = (EditText) findViewById(R.id.et);
		TextView center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		center_txt_title.setText(title);
		et.setHint(hint);
		et.setText(oldStr);
		et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxLength) });
		if (singleLine) {
			et.setSingleLine();
			et.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, ScreenUtil.dip2px(context, 40)));
		}
		if (oldStr == null) {
			oldStr = "";
		}
		if (oldStr.length() <= maxLength) {
			et.setSelection(oldStr.length());
		}
		InputTools.ShowKeyboard(et);
		TextView right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(null);
		right_txt_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String inputString = et.getText().toString().trim();
				if (Tool.isEmpty(inputString)) {
					ToastUtil.showShortToast(hint);
					return;
				}
				InputTools.HideKeyboard(et);
				dismiss();
				listener.doConfirm(inputString);
			}
		});
		findViewById(R.id.goback).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						InputTools.HideKeyboard(et);
						dismiss();
					}
				});
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 1);
		dialogWindow.setAttributes(lp);
	}

}
