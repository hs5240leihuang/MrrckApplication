package com.meiku.dev.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

public class AdViewPagerAdapter extends PagerAdapter {

	private List<ImageView> views;

	public AdViewPagerAdapter(List<ImageView> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		if (views.size()<3) {
			return views.size();
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		if (view == object) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Warning：不要在这里调用removeView
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 对ViewPager页号求模取出View列表中要显示的项
		if (views.size()==0) {
			return false;
		}
		else {
			position %= views.size();
		}
		
		if (position < 0) {
			position = views.size() + position;
		}
		ImageView view = views.get(position);
		// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
		ViewParent vp = view.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}
		container.addView(view);
		// add listeners here if necessary
		return view;
	}

}
