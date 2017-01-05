package com.meiku.dev.views;

import java.util.List;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.MyPopupwindow.popListener;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 公用弹出框PopupWindow
 * 
 */
public class NewPopupwindows extends PopupWindow {
	private Context context;
	private LayoutInflater inflater;
	private ListView popListView;
	private List<PopupData> list;
	private popwindowListener listener;
	private int flag = 0;

	public NewPopupwindows(Context context, List<PopupData> list,
			popwindowListener listener, int flag) {
		this.context = context;
		this.list = list;
		this.listener = listener;
		this.flag = flag;
		init();
	}

	public void init() {
		int Height = ScreenUtil.getWindowHeight(context);
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.activity_newpopupwindow, null);
		setContentView(view);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		// setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popbg));
		setOutsideTouchable(true);
		popListView = (ListView) view.findViewById(R.id.lvpop);
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.doChoose(arg2);
				dismiss();
			}
		});
		CommonAdapter<PopupData> myaCommonAdapter = new CommonAdapter<PopupData>(
				context, R.layout.item_newpopupwindow, list) {
			@Override
			public void convert(ViewHolder viewHolder, PopupData t) {
				int imgrs = list.get(viewHolder.getPosition()).getImgRes();
				if (Tool.isEmpty(imgrs) || imgrs == 0) {
					viewHolder.getView(R.id.pophead).setVisibility(View.GONE);
				} else {
					viewHolder.setImage(R.id.pophead, imgrs);
				}

				viewHolder.setText(R.id.poptext,
						list.get(viewHolder.getPosition()).getName());

			}
		};
		popListView.setAdapter(myaCommonAdapter);
		if (flag == 1) {
			view.findViewById(R.id.view).setVisibility(View.GONE);
			view.findViewById(R.id.view1).setVisibility(View.GONE);
			View v = view.findViewById(R.id.view2);
			v.setVisibility(View.VISIBLE);
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) v
//					.getLayoutParams(); // 取控件textView当前的布局参数
//			linearParams.height = Height-popListView.getHeight();// 控件的高强制设成20
//			v.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件

		}
	}

	public void show(View v) {
		showAsDropDown(v, ScreenUtil.dip2px(context, 0),
				ScreenUtil.dip2px(context, 0));

	}

	public interface popwindowListener {
		public void doChoose(int position);
	}
}
