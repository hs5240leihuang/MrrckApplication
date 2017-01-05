package com.meiku.dev.adapter;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.meiku.dev.ui.fragments.BaseFragment;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> arraylist;

	public HomeViewPagerAdapter(FragmentManager fm, List<BaseFragment> arraylist) {
		super(fm);
		this.arraylist = arraylist;
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		android.support.v4.app.Fragment fragment = arraylist.get(position);
		return fragment;
	}

	@Override
	public int getCount() {
		return arraylist == null ? 0 : arraylist.size();
	}
}
