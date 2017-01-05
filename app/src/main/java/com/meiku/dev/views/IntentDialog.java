package com.meiku.dev.views;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ProductWillEntity;
import com.meiku.dev.bean.MkDecorateCasePriceApply;

/**
 * 意向申请Dialog
 * 
 * @author zhunaixin
 * 
 */
public class IntentDialog extends Dialog {
	private Context context;
	private LayoutInflater inflater;
	private IntentClickListenerInterface clickListenerInterface;
	private ListView listview;
	private ProductWillEntity productWillEntity;
	private MkDecorateCasePriceApply MkDecorateCasePriceApply;
	private int flag;

	public IntentDialog(Context context, ProductWillEntity t,
			IntentClickListenerInterface listener, int flag) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.clickListenerInterface = listener;
		this.flag = flag;
		productWillEntity = t;
		init();
	}

	public IntentDialog(Context context, MkDecorateCasePriceApply t,
			IntentClickListenerInterface listener, int flag) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.clickListenerInterface = listener;
		this.flag = flag;
		MkDecorateCasePriceApply = t;
		init();
	}

	public interface IntentClickListenerInterface {
		public void doConfirm();

		public void doCancel();
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.tv_call:
				clickListenerInterface.doConfirm();
				break;
			case R.id.tv_cancel:
				clickListenerInterface.doCancel();
				break;
			}
		}
	}

	public void init() {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.intent_dialog, null);
		setContentView(view);
		view.findViewById(R.id.tv_call).setOnClickListener(new clickListener());
		view.findViewById(R.id.tv_cancel).setOnClickListener(
				new clickListener());
		if (flag == 0) {
			((TextView) view.findViewById(R.id.tv_intent))
					.setText(productWillEntity.getWillContent());
			((TextView) view.findViewById(R.id.tv_contactname))
					.setText(productWillEntity.getContactName());
			((TextView) view.findViewById(R.id.tv_phone))
					.setText(productWillEntity.getContactPhone());
		} else {
			((TextView) view.findViewById(R.id.tv_intent))
					.setText(MkDecorateCasePriceApply.getTitle());
			((TextView) view.findViewById(R.id.tv_contactname))
					.setText(MkDecorateCasePriceApply.getName());
			((TextView) view.findViewById(R.id.tv_phone))
					.setText(MkDecorateCasePriceApply.getPhone());
		}
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.7);
		dialogWindow.setAttributes(lp);
	}

}
