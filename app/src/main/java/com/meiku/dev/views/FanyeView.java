package com.meiku.dev.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;

/**
 * 翻页的view
 * 
 * @author Administrator
 * 
 */
public class FanyeView {
	private Context context;
	public int currentPage;
	private FanyeClickListener listener;
	private View view;
	private LinearLayout pagesLayout;
	private int pageSize;
	private LinearLayout fatherLayout;
	private boolean isShowing = false;

	public FanyeView(Context context, int pageSize, int currentPage,
			LinearLayout layout, FanyeClickListener tabClickListener) {
		this.context = context;
		this.pageSize = pageSize;
		this.fatherLayout = layout;
		this.listener = tabClickListener;
		this.currentPage = currentPage;
		init();
	}

	public boolean isShowing() {
		return isShowing;
	}

	public View getView() {
		return view;
	}

	public interface FanyeClickListener {
		void onPageClick(int page);
	}

	private void init() {
		view = LayoutInflater.from(context).inflate(R.layout.view_fanye, null,
				false);
		pagesLayout = (LinearLayout) view.findViewById(R.id.pagesLayout);
		view.findViewById(R.id.btnFirst).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						SetAllTitleClo(0,true);
					}
				});
		view.findViewById(R.id.btnLast).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						SetAllTitleClo(pageSize - 1,true);
					}
				});
		LogUtil.d("hl", "翻页总数=" + pageSize);
		for (int i = 0; i < pageSize; i++) {
			addOnePageView(i, String.valueOf(i + 1));
		}
		fatherLayout.removeAllViews();
		fatherLayout.addView(view);
		SetAllTitleClo(currentPage-1,false);//index 从0开始,页数从1开始
	}

	public void showFanye(boolean show) {
		fatherLayout.setVisibility(show ? View.VISIBLE : View.GONE);
		isShowing = show;
	}

	/**
	 * 添加一个TitleTab
	 */
	private void addOnePageView(int position, String pageTxt) {
		LinearLayout layout = new LinearLayout(context);
		TextView tv = new TextView(context);
		LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dip2px(
				context, 30), ScreenUtil.dip2px(context, 30));
		tv.setLayoutParams(params);
		tv.setText(pageTxt);
		tv.setBackgroundResource(R.drawable.fanye_select);
		tv.setTextColor(context.getResources().getColor(R.color.white));
		tv.setGravity(Gravity.CENTER);
		tv.setOnClickListener(new TitleClick(position));
		layout.setPadding(ScreenUtil.dip2px(context, 5), 0,
				ScreenUtil.dip2px(context, 5), 0);
		layout.setGravity(Gravity.CENTER);
		layout.addView(tv);
		pagesLayout.addView(layout);
	}

	/**
	 * title tab 点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class TitleClick implements OnClickListener {

		private int position;

		public TitleClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			SetAllTitleClo(position,true);
		}

	}

	/**
	 * 
	 * @param selectedIndex 选中的index
	 * @param doClick 是否触发点击事件
	 */
	public void SetAllTitleClo(int selectedIndex,boolean doClick) {
		if (doClick) {
			listener.onPageClick(selectedIndex + 1);// index从0开始,显示页数是从1开始
		}
		for (int i = 0; i < pageSize; i++) {
			if (i == selectedIndex) {
				setTitleTabTextClo(i, R.color.white, R.drawable.fanye_unselect);
			} else {
				setTitleTabTextClo(i, R.color.white, R.drawable.fanye_select);
			}
		}
		showFanye(false);
	}

	/**
	 * 设置某个childview的文字颜色背景
	 * 
	 * @param childIndex
	 * @param clo
	 */
	private void setTitleTabTextClo(int childIndex, int txtclo, int bgclo) {
		LinearLayout layour = (LinearLayout) pagesLayout.getChildAt(childIndex);
		TextView tv = (TextView) layour.getChildAt(0);
		tv.setTextColor(context.getResources().getColor(txtclo));
		tv.setBackgroundResource(bgclo);
	}

}
