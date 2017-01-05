package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.views.MyViewpager;
import com.umeng.analytics.MobclickAgent;

/**
 * 新的赛事主页面
 * 
 */
public class NewMatchActivity extends BaseFragmentActivity implements
		OnClickListener {

	/** tab图片未选中 */
	private int[] imgUnSelected = new int[] { R.drawable.m_cansaizuopin,
			R.drawable.m_saishijieshao, R.drawable.m_saiqu,
			R.drawable.m_paiming };
	/** tab图片选中 */
	private int[] imgSelected = new int[] { R.drawable.m_cansaixuanzhong,
			R.drawable.m_jieshaoxuanzhong, R.drawable.m_saiquxuanzhong,
			R.drawable.m_paimingxuanzhong };

	/** 各标题 */
	private int[] titles = new int[] { R.string.match_works,
			R.string.match_intro, R.string.match_area, R.string.match_rainking };
	/** tab文字textview 的id */
	private int[] tvIDs = new int[] { R.id.txt_main_bottom_one,
			R.id.txt_main_bottom_two, R.id.txt_main_bottom_three,
			R.id.txt_main_bottom_four };
	/** tab图片imageview 的id */
	private int[] ivIDs = new int[] { R.id.id_img_tab_one, R.id.id_img_tab_two,
			R.id.id_img_tab_three, R.id.id_img_tab_four };
	/** tab布局的id */
	private int[] layoutIDs = new int[] { R.id.id_tab_one, R.id.id_tab_two,
			R.id.id_tab_three, R.id.id_tab_four };
	private TextView[] tab_tvs = new TextView[4];
	private ImageView[] tab_ivs = new ImageView[4];
	private RelativeLayout[] tab_layouts = new RelativeLayout[4];
	private int tabsize;
	private MyViewpager viewpager;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private HomeViewPagerAdapter adapter;

	private MatchWorksFragment fragmentworks = new MatchWorksFragment();// 参赛作品
	private MatchIntroduceFragment activityRuleFragment = new MatchIntroduceFragment();// 赛事介绍
	private MatchAreaFragment areaFragment = new MatchAreaFragment();// 赛区
	private MatchRankingFragment rainkingFragment = new MatchRankingFragment();// 冠军排名

	private int matchId;
	private int flag = 0;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_newmatch;
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }
    
	@Override
	public void initView() {
		initTabs();
		initViewpager();
		showCurrentPageByIndex(0);
	}

	private void initTabs() {
		tabsize = tvIDs.length;
		for (int i = 0; i < tabsize; i++) {
			tab_tvs[i] = (TextView) findViewById(tvIDs[i]);
			tab_ivs[i] = (ImageView) findViewById(ivIDs[i]);
			tab_layouts[i] = (RelativeLayout) findViewById(layoutIDs[i]);
			tab_layouts[i].setOnClickListener(this);
		}
		// 我要报名
		((TextView) findViewById(R.id.txt_main_bottom_mid))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
							ShowLoginDialogUtil
									.showTipToLoginDialog(NewMatchActivity.this);
							return;
						}
						Intent intent = new Intent(NewMatchActivity.this,
								EnrollActivity.class);
						intent.putExtra("flag", flag);
						startActivity(intent);
					}
				});
	}

	@SuppressLint("NewApi")
	private void initViewpager() {
		matchId = getIntent().getIntExtra("matchId", -1);
		LogUtil.d("hl", "matchId=" + matchId);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("matchId", matchId);
		fragmentworks.setArguments(bundle);
		activityRuleFragment.setArguments(bundle);
		areaFragment.setArguments(bundle);
		rainkingFragment.setArguments(bundle);
		transaction.commit();

		listFragment.add(fragmentworks);
		listFragment.add(activityRuleFragment);
		listFragment.add(areaFragment);
		listFragment.add(rainkingFragment);

		viewpager = (MyViewpager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(4);
	}

	private void showCurrentPageByIndex(int index) {
		for (int i = 0; i < tabsize; i++) {
			tab_tvs[i].setText(titles[i]);
			tab_tvs[i].setTextColor(index == i ? getResources().getColor(
					R.color.home_buttom_txt_red) : getResources().getColor(
					R.color.home_buttom_txt_gray));
			tab_ivs[i].setBackgroundResource(index == i ? imgSelected[i]
					: imgUnSelected[i]);
		}
		viewpager.setCurrentItem(index, false);
	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				getWindow().getDecorView().getWindowToken(), 0);
		switch (v.getId()) {
		case R.id.id_tab_one:
			showCurrentPageByIndex(0);
			break;
		case R.id.id_tab_two:
			showCurrentPageByIndex(1);
			break;
		case R.id.id_tab_three:
			showCurrentPageByIndex(2);
			break;
		case R.id.id_tab_four:
			showCurrentPageByIndex(3);
			break;

		}
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
