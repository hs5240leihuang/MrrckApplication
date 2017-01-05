package com.meiku.dev.views;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.utils.Tool;

/**
 * 消息界面的longclick使用的Dialog
 * 
 * @author wangke
 * 
 */
public class MessageDialog extends Dialog {
	private Context context;
	private List<String> messageList;
	private LayoutInflater inflater;
	private messageListener listener;
	private ListView listview;

	public MessageDialog(Context context, List<String> messageList,
			messageListener listener) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.messageList = messageList;
		this.listener = listener;
		init();
	}

	public interface messageListener {
		void positionchoose(int position);
	}

	public void init() {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.message_dialog, null);
		setContentView(view);
		listview = (ListView) findViewById(R.id.messagelist);
		CommonAdapter<String> showAdapter = new CommonAdapter<String>(context,
				R.layout.item_messagelist, messageList) {

			@Override
			public void convert(ViewHolder viewHolder, String t) {
				viewHolder.setText(R.id.messagetext,
						messageList.get(viewHolder.getPosition()));
			}
		};
		listview.setAdapter(showAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (listener != null && !Tool.isEmpty(messageList)) {
					listener.positionchoose(arg2);
				}
				dismiss();
			}
		});
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.8);
		dialogWindow.setAttributes(lp);
	}
}
