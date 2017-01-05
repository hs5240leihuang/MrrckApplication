package com.meiku.dev.ui.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meiku.dev.R;
import com.meiku.dev.adapter.GuidePagerAdapter;

/**
 * 引导页
 */
public class GuidePageActivity extends Activity {

	private int[] ids = { R.drawable.initview01, R.drawable.initview02,
			R.drawable.initview03, R.drawable.initview04 };
	private List<View> guides = new ArrayList<View>();
	private ViewPager pager;
	private String start;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guidepage);
		start = getIntent().getStringExtra("START");
		LinearLayout exit = (LinearLayout) findViewById(R.id.exit);
		exit.setVisibility(View.GONE);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(start)) {// 首次启动进来为1，否则为在设置中查看进入
					Intent intent = new Intent(GuidePageActivity.this,
							HomeActivity.class);
					intent.putExtra("Flag", 1);

					intent.putExtra("type", getIntent().getStringExtra("type"));
					intent.putExtra("data", getIntent().getStringExtra("data"));
					intent.putExtra("h5url", getIntent()
							.getStringExtra("h5url"));
					intent.putExtra("arga", getIntent().getIntExtra("arga", -1));
					intent.putExtra("argb", getIntent().getStringExtra("argb"));
					intent.putExtra("argc", getIntent().getStringExtra("argc"));
					startActivity(intent);
				}
				finish();
			}
		});

		for (int i = 0; i < ids.length; i++) {
			ImageView iv = new ImageView(getApplicationContext());
			iv.setImageResource(ids[i]);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			guides.add(iv);
		}
		// 最后一个引导图点击进主页
		guides.get(ids.length - 1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(start)) {// 首次启动进来为1，否则为在设置中查看进入
					Intent intent = new Intent(GuidePageActivity.this,
							HomeActivity.class);
					intent.putExtra("Flag", 1);

					intent.putExtra("type", getIntent().getStringExtra("type"));
					intent.putExtra("data", getIntent().getStringExtra("data"));
					intent.putExtra("h5url", getIntent()
							.getStringExtra("h5url"));
					intent.putExtra("arga", getIntent().getIntExtra("arga", -1));
					intent.putExtra("argb", getIntent().getStringExtra("argb"));
					intent.putExtra("argc", getIntent().getStringExtra("argc"));
					startActivity(intent);
				}
				finish();
			}
		});
		GuidePagerAdapter adapter = new GuidePagerAdapter(guides);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		pager = (ViewPager) findViewById(R.id.contentPager);
		pager.setAdapter(adapter);
	}

}