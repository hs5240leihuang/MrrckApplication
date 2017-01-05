package com.meiku.dev.ui.morefun;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.meiku.dev.R;
import com.umeng.analytics.MobclickAgent;

public class MapFragment extends LocalActivityManagerFragment {

	private TabHost mTabHost;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment, container, false);
		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.setup(getLocalActivityManager());

		TabSpec tab = mTabHost
				.newTabSpec("map")
				.setIndicator("map")
				.setContent(new Intent(getActivity(), NewMapResumeActivity.class));
		mTabHost.addTab(tab);
		return view;
	}
}
