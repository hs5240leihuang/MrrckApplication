package com.meiku.dev.views;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

/**
 * 选择String的dialog，传递ArrayList<String> 与title
 * 
 * @author huanglei
 * 
 */
public class ChooseTextDialog extends Dialog {

	private Context context;
	private ChooseOneStrListener listener;
	private String title;
	private ArrayList<String> allStr = new ArrayList<String>();
	private EditText et;

	public interface ChooseOneStrListener {
		public void doChoose(int position, String s);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param allStr
	 *            所有待选文字
	 * @param listener
	 *            返回接口
	 */
	public ChooseTextDialog(Context context, String title,
			ArrayList<String> allStr, ChooseOneStrListener listener) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.allStr = allStr;
		this.listener = listener;
		if (Tool.isEmpty(allStr)) {
			ToastUtil.showShortToast("没有可选项");
		}
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_choosestr, null);
		setContentView(view);

		ListView listview = (ListView) view.findViewById(R.id.listview);
		TextView tvTitle = (TextView) view.findViewById(R.id.title);
		if (!Tool.isEmpty(title)) {
			tvTitle.setText(title);
		}
		CommonAdapter<String> showAdapter = new CommonAdapter<String>(context,
				R.layout.custome_spinner_item, allStr) {

			@Override
			public void convert(ViewHolder viewHolder, final String t) {
				viewHolder.setText(R.id.text, t);
			}

		};
		listview.setAdapter(showAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.doChoose(arg2, allStr.get(arg2));
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
