package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;

/**
 * 秀场类别tabs选择，带下滑线
 * 
 */
public class ShowTitleTabsLayout2 {
	private List<MkPostsActiveCategory> titletabList = new ArrayList<MkPostsActiveCategory>();
	/**
	 * 每个Titletab的宽度
	 */
	private Context context;
	private LinearLayout TabGroups;
	public int currentIndex;
	private TabClickListener listener;
	private View view;
	private HorizontalScrollView scrollView_tabs;

	private ArrayList<Integer> xValue = new ArrayList<Integer>();
	private int cellWidth;
	private View bottomLine;

	public ShowTitleTabsLayout2(Context context, int cellWidth,
			List<MkPostsActiveCategory> titletabList,
			TabClickListener tabClickListener) {
		this.context = context;
		this.cellWidth = cellWidth;
		this.titletabList = titletabList;
		this.listener = tabClickListener;
		init();
	}

	public View getView() {
		return view;
	}

	public interface TabClickListener {
		void onTabClick(int index);
	}

	private void init() {
		view = LayoutInflater.from(context).inflate(
				R.layout.view_showtitletabs, null, false);
		view.setBackgroundColor(context.getResources().getColor(R.color.white));
		TabGroups = (LinearLayout) view.findViewById(R.id.TabGroups);
		scrollView_tabs = (HorizontalScrollView) view
				.findViewById(R.id.scrollView_tabs);
		bottomLine = view.findViewById(R.id.bottomLine);
		view.findViewById(R.id.selectBg).setVisibility(View.GONE);
		for (int i = 0, size = titletabList.size(); i < size; i++) {
			addOneTitleTab(i, titletabList.get(i).getName());
		}
		SetAllTitleClo(0);
	}

	/**
	 * 添加一个TitleTab
	 */
	private void addOneTitleTab(int position, String titleName) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		TabGroups.measure(w, h);
		xValue.add(TabGroups.getMeasuredWidth());
		LogUtil.d("hl", "xValue = " + TabGroups.getMeasuredWidth());
		View tabitem = LayoutInflater.from(context).inflate(
				R.layout.view_showtitleitem, null, false);
		TextView tv = (TextView) tabitem.findViewById(R.id.txt);
		View line = (View) tabitem.findViewById(R.id.line);
		tv.setText(titleName);
		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		tv.measure(spec, spec);
		int lineWidth = tv.getMeasuredWidth();
		line.setLayoutParams(new LinearLayout.LayoutParams(lineWidth,
				ScreenUtil.dip2px(context, 2)));
		tabitem.setOnClickListener(new TitleClick(position));
		// int needWidth = lineWidth + ScreenUtil.dip2px(context, 40);
		// cellWidth = needWidth > cellWidth ? needWidth : cellWidth;
		TabGroups.addView(tabitem, new LinearLayout.LayoutParams(cellWidth,
				LayoutParams.FILL_PARENT));

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
			SetSelecetedIndex(position, false);
		}

	}

	public void SetSelecetedIndex(int index, boolean doscroll) {
		if (currentIndex != index) {
			SetAllTitleClo(index);
			listener.onTabClick(index);
			currentIndex = index;
			if (doscroll) {
				scrollView_tabs.smoothScrollTo(xValue.get(index), 0);
			}
		}
	}

	/**
	 * 设置所有title tab文字颜色
	 * 
	 * @param selectedIndex
	 */
	public void SetAllTitleClo(int selectedIndex) {
		for (int i = 0, size = TabGroups.getChildCount(); i < size; i++) {
			setTitleTab(i, i == selectedIndex);
		}
	}

	/**
	 * 设置某个childview的，文字颜色，背景色
	 * 
	 * @param childIndex
	 * @param isSelected
	 */
	private void setTitleTab(int childIndex, boolean isSelected) {
		LinearLayout layout = (LinearLayout) TabGroups.getChildAt(childIndex);
		TextView childview = (TextView) layout.getChildAt(0);
		childview.setTextColor(context.getResources().getColor(
				isSelected ? R.color.clo_ff3499 : R.color.titletabsclo));
		layout.getChildAt(1).setVisibility(
				isSelected ? View.VISIBLE : View.INVISIBLE);
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public void showBottomLine(boolean show) {
		bottomLine.setVisibility(show ? View.VISIBLE : View.GONE);
	}

}
