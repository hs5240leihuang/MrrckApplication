package com.meiku.dev.views;

import java.util.List;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class PlotPopupwindows extends PopupWindow {
	private Context context;
	private LayoutInflater inflater;
	private ListView popListView;
	private List<PopupData> list;
	private popwindowListener listener;

	public PlotPopupwindows(Context context, List<PopupData> list,
			popwindowListener listener) {
		this.context = context;
		this.list = list;
		this.listener = listener;
		init();
	}

	public void init() {
		int Height = ScreenUtil.getWindowHeight(context);
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.activity_plotpopupwindows, null);
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
				context, R.layout.item_plotpopupwindows, list) {
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

	}

	public void show(View v) {
		showAsDropDown(v, ScreenUtil.dip2px(context, 0),
				ScreenUtil.dip2px(context, 0));

	}

	public interface popwindowListener {
		public void doChoose(int position);
	}
}
