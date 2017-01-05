package com.meiku.dev.ui.activitys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.UserAttachmentEntity;
import com.meiku.dev.ui.fragments.ImageDetailFragment;
import com.meiku.dev.utils.SystemBarTintManager;
import com.meiku.dev.views.HackyViewPager;

/**
 * 滑动显示所有图片
 *  只显示一张图片用showOnePic参数,传递图片路径 
 *  多张图片用showMorePic参数,所有图片路径用","逗号连接
 * Created by huanglei on 2015/10/2.
 */
public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private List<AttachmentListDTO> imageDates;
	private List<String> urlList = new ArrayList<String>();
	private String showOnePicPath, showMorePicPath;
	private LinearLayout pointsView;// 图片计数View
	private int current_position = 0;
	private ImageView[] imgPoints;
	private List<UserAttachmentEntity> userAttachmentEntities;
	private SystemBarTintManager tintManager;

	/**
	 * 透明状态栏
	 */
	private void setTransparentStatusBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(Color.TRANSPARENT);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		setTransparentStatusBar();
		pointsView = (LinearLayout) findViewById(R.id.points_view);
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		showOnePicPath = getIntent().getStringExtra("showOnePic");
		showMorePicPath = getIntent().getStringExtra("showMorePic");
		imageDates = getIntent().getParcelableArrayListExtra("imageDates");
		if (imageDates != null) {
			for (int i = 0, size = imageDates.size(); i < size; i++) {
				urlList.add(imageDates.get(i).getClientFileUrl());
			}
		} else if (!TextUtils.isEmpty(showOnePicPath)) {
			urlList.add(showOnePicPath);
		} else if (!TextUtils.isEmpty(showMorePicPath)) {
			String[] urls = showMorePicPath.split(",");
			urlList = Arrays.asList(urls);
		}

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter;
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urlList);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		int pageCount = mPager.getAdapter().getCount();
		mPager.setOffscreenPageLimit(pageCount);
		CharSequence text = getString(R.string.viewpager_indicator, 1,
				pageCount);
		if (pageCount > 1) {
			if (pageCount > 9) {
				indicator.setVisibility(View.VISIBLE);
				pointsView.setVisibility(View.GONE);
			} else {
				indicator.setVisibility(View.GONE);
				addPoints(pageCount);
				pointsView.setVisibility(View.VISIBLE);
			}
		} else {// 一张图片不显示下标
			indicator.setVisibility(View.GONE);
			pointsView.setVisibility(View.GONE);
		}

		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
				current_position = arg0;
				changePoint();
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		private String oneUrl;
		public List<String> imageDates;

		public ImagePagerAdapter(FragmentManager fm, List<String> imageDates) {
			super(fm);
			this.imageDates = imageDates;
		}

		@Override
		public int getCount() {
			return imageDates.size();
		}

		public Fragment getItem(int position) {
			return ImageDetailFragment.newInstance(imageDates.get(position));
		}

	}

	private void addPoints(int size) {
		if (null != pointsView) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = 10;
			lp.rightMargin = 10;
			if (size > 0) {
				pointsView.removeAllViews();
				imgPoints = new ImageView[size];
				for (int k = 0; k < size; k++) {
					try {
						ImageView img = new ImageView(this);
						img.setLayoutParams(lp);
						if (current_position == k) {
							img.setImageResource(R.drawable.icon_point_red);
						} else {
							img.setImageResource(R.drawable.icon_point_gary);
						}
						imgPoints[k] = img;
						pointsView.addView(img);
					} catch (NullPointerException e) {
						// TODO: handle exception
					}

				}
			}
		}
	}

	private void changePoint() {
		if (null != imgPoints) {
			for (int k = 0; k < imgPoints.length; k++) {
				if (current_position == k) {
					imgPoints[k].setImageResource(R.drawable.icon_point_red);
				} else {
					imgPoints[k].setImageResource(R.drawable.icon_point_gary);
				}
			}
		}

	}
}