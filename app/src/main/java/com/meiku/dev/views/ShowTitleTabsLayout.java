package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;

/**
 * 秀场类别tabs选择，选中为红色背景
 * 
 */
public class ShowTitleTabsLayout {
	private TranslateAnimation animation;
	private List<MkPostsActiveCategory> titletabList = new ArrayList<MkPostsActiveCategory>();
	private ArrayList<Integer> titletabsIndexs = new ArrayList<Integer>();
	/**
	 * 每个Titletab的宽度
	 */
	private int titleTabWidth;
	private Context context;
	private LinearLayout selectBg;
	private LinearLayout TabGroups;
	public int currentIndex;
	private TabClickListener listener;
	private View view;

	public ShowTitleTabsLayout(Context context,
			List<MkPostsActiveCategory> titletabList,
			TabClickListener tabClickListener) {
		this.context = context;
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
		TabGroups = (LinearLayout) view.findViewById(R.id.TabGroups);
		selectBg = (LinearLayout) view.findViewById(R.id.selectBg);
		selectBg.setVisibility(View.GONE);
		titleTabWidth = ScreenUtil.getWindowWidth(context)
				/ titletabList.size();
		selectBg.setLayoutParams(new RelativeLayout.LayoutParams(titleTabWidth,
				LayoutParams.FILL_PARENT));
		for (int i = 0, size = titletabList.size(); i < size; i++) {
			addOneTitleTab(i, titletabList.get(i).getName());
			titletabsIndexs.add(titleTabWidth * i);
		}
		SetAllTitleClo(0);
	}

	/**
	 * title tab的选中穿梭动画
	 * 
	 * @param toIndex
	 */
	private void startAnim(int toIndex) {
		animation = new TranslateAnimation(titletabsIndexs.get(currentIndex),
				titletabsIndexs.get(toIndex), 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				SetAllTitleClo(currentIndex);
			}
		});
		selectBg.startAnimation(animation);
	}

	/**
	 * 添加一个TitleTab
	 */
	private void addOneTitleTab(int position, String titleName) {
		TextView tv = new TextView(context);
		LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, ScreenUtil.dip2px(context, 25));
		tv.setLayoutParams(params);
		tv.setText("   " + titleName + "   ");
		tv.setTextColor(context.getResources().getColor(R.color.titletabsclo));
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(13);
		tv.setOnClickListener(new TitleClick(position));
		LogUtil.d("hl", "#addOneTitleTab#_" + position);
		TabGroups.addView(tv);
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
			SetSelecetedIndex(position);
		}

	}

	public void SetSelecetedIndex(int index) {
		if (currentIndex != index) {
			SetAllTitleClo(index);
			listener.onTabClick(index);
			// startAnim(index);
			currentIndex = index;
		}
	}

	/**
	 * 设置所有title tab文字颜色
	 * 
	 * @param selectedIndex
	 */
	public void SetAllTitleClo(int selectedIndex) {
		for (int i = 0, size = TabGroups.getChildCount(); i < size; i++) {
			if (i == selectedIndex) {
				setTitleTabTextClo(i, i == selectedIndex);
			} else {
				setTitleTabTextClo(i, i == selectedIndex);
			}
		}
	}

	/**
	 * 设置某个childview的文字颜色
	 * 
	 * @param childIndex
	 * @param clo
	 */
	private void setTitleTabTextClo(int childIndex, boolean isSelected) {
		TextView tv = (TextView) TabGroups.getChildAt(childIndex);
		tv.setTextColor(isSelected ? context.getResources().getColor(
				R.color.white) : context.getResources().getColor(
				R.color.titletabsclo));
		if (isSelected) {
			tv.setBackgroundResource(R.drawable.radius_border_mrrck_solid);
		} else {
			tv.setBackgroundDrawable(null);
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * 直接选中
	 * 
	 * @param toIndex
	 */
	public void selectDirectlt(int toIndex) {
		SetAllTitleClo(toIndex);
		animation = new TranslateAnimation(titletabsIndexs.get(currentIndex),
				titletabsIndexs.get(toIndex), 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		selectBg.startAnimation(animation);
	}
}
