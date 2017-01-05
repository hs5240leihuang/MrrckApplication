package com.meiku.dev.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import com.meiku.dev.R;
import com.meiku.dev.ui.community.ShowDetailTxtActivity;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.MessageDialog;
import com.meiku.dev.views.MessageDialog.messageListener;

/**
 * 长按事件显示 复制与全屏查看
 * 
 * @author Administrator
 * 
 */
public class TextLongClickListener implements OnLongClickListener {

	private TextView textView;
	private Context context;
	private EmotionEditText emotionEditText;

	public TextLongClickListener(Context context, TextView textView) {
		this.textView = textView;
		this.context = context;
	}

	public TextLongClickListener(Context context,
			EmotionEditText emotionEditText) {
		this.emotionEditText = emotionEditText;
		this.context = context;
	}

	@SuppressLint("NewApi") @Override
	public boolean onLongClick(View arg0) {
		List<String> messageList = new ArrayList<String>();
		messageList.add(context.getResources().getString(R.string.copy));
		messageList.add(context.getResources().getString(
				R.string.fullscreen_watch));
		new MessageDialog(context, messageList, new messageListener() {

			@Override
			public void positionchoose(int position) {
				switch (position) {
				case 0:
					ClipboardManager cm = (ClipboardManager) context
							.getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本数据复制到剪贴板
					if (!Tool.isEmpty(textView)) {
						cm.setText(textView.getText().toString());
					}
					if (!Tool.isEmpty(emotionEditText)) {
						String emotionStr = EmotionHelper.getSendEmotion(
								context, emotionEditText.getText().toString());
						cm.setText(emotionStr);
					}
					ToastUtil.showShortToast("复制成功");
					break;
				case 1:
					Intent intent = new Intent(context,
							ShowDetailTxtActivity.class);
					if (!Tool.isEmpty(textView)) {
						intent.putExtra("txt", textView.getText().toString());
					}
					if (!Tool.isEmpty(emotionEditText)) {
						intent.putExtra("txt", emotionEditText.getText()
								.toString());
					}
					context.startActivity(intent);
					break;
				default:
					break;
				}
			}
		}).show();
		return false;
	}
}
