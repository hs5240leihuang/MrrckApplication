package com.meiku.dev.views;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.Tool;

/**
 * 公用弹出框PopupWindow
 * 
 */
public class MyPopupwindow extends PopupWindow {
	private Context context;
	private LayoutInflater inflater;
	private ListView popListView;
	private List<PopupData> list;
	private popListener listener;

	public MyPopupwindow(Context context, List<PopupData> list,
			popListener listener) {
		this.context = context;
		this.list = list;
		this.listener = listener;
		init();
	}

	public void init() {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.activity_popupwindow, null);
		setContentView(view);
	    int Height = ScreenUtil.getWindowHeight(context);
		setWidth(ScreenUtil.dip2px(context, 140));
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
//		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popbg));
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
				context, R.layout.activity_poplistitem, list) {
			@Override
			public void convert(ViewHolder viewHolder, PopupData t) {
				int imgrs = list.get(viewHolder.getPosition()).getImgRes();
				if (Tool.isEmpty(imgrs)||imgrs==0) {
					viewHolder.getView(R.id.pophead).setVisibility(View.GONE);
				} else {
					viewHolder.setImage(R.id.pophead, imgrs);
				}

				viewHolder.setText(R.id.poptext,
						list.get(viewHolder.getPosition()).getName());
				
			}
		};
		popListView.setAdapter(myaCommonAdapter);

	}

	public void show(View v) {
		showAsDropDown(v, ScreenUtil.dip2px(context, -80), ScreenUtil.dip2px(context, 0));
	
	}

	public interface popListener {
		public void doChoose(int position);
	}
}
