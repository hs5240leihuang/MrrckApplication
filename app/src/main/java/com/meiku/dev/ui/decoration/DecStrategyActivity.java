package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.TopicEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 装修攻略
 * 
 */
public class DecStrategyActivity extends BaseFragmentActivity implements
		OnClickListener {

	private int TAG_ADDNEWPOST = 1;
	private int TAG_REFRESHPOST = 2;
	private TextView tv_left, tv_right;
	private View lineLeft, lineRight;
	private StrategyTypsFragment strategyTypsFragment;
	private StrategyAskFragment strategyAskFragment;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private ViewPager viewpager;
	private HomeViewPagerAdapter adapter;
	private int menuId = -1;
	private int loadFlag = 0;
	private List<TopicEntity> leftTopicList;
	private List<PopupData> leftPopList = new ArrayList<PopupData>();
	private NewPopupwindows leftPopindows;
	private String selectBoardId;
	private String leftBoardId, rightBoardId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_decstrategy;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计activity
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计activity
		MobclickAgent.onPause(this);
	}

	@Override
	public void initView() {
		initSelectBar();
		initViewpager();
	}

	private void initSelectBar() {
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_left.setText("装修经验");
		tv_right.setText("在线问答");
		lineLeft = (View) findViewById(R.id.lineLeft);
		lineRight = (View) findViewById(R.id.lineRight);
	}

	private void initViewpager() {
		menuId = getIntent().getIntExtra("menuId", -1);
		strategyTypsFragment = new StrategyTypsFragment();
		strategyAskFragment = new StrategyAskFragment();
		listFragment.add(strategyTypsFragment);
		listFragment.add(strategyAskFragment);
		strategyTypsFragment.setMenuId(menuId);
		strategyAskFragment.setMenuId(menuId);
		viewpager = (ViewPager) findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(2);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				showLeftOrRight(arg0 == 0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		showLeftOrRight(true);
	}

	protected void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", -1);
		map.put("menuId", menuId);
		map.put("topicId", -1);
		map.put("loadFlag", loadFlag);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_STRATEGYList));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "getTopicData————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	public void initValue() {
		getData();
	}

	@Override
	public void bindListener() {
		findViewById(R.id.right_res_title).setOnClickListener(this);
		findViewById(R.id.tab_left).setOnClickListener(this);
		findViewById(R.id.arrowLeft).setOnClickListener(this);
		findViewById(R.id.tab_right).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "" + resp.getBody());
		if (!Tool.isEmpty(resp.getBody())) {
			if (!Tool.isEmpty(resp.getBody().get("data"))) {
				List<TopicEntity> topicList = (List<TopicEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<TopicEntity>>() {
								}.getType());
				if (topicList == null) {
					ToastUtil.showShortToast("获取话题失败！");
					return;
				}
				if (topicList.size() >= 1) {
					// 左侧装修经验有话题数据
					leftTopicList = topicList.get(0).getTopicList();
					leftPopList.clear();
					for (int i = 0, size = leftTopicList.size(); i < size; i++) {
						leftPopList.add(new PopupData(leftTopicList.get(i)
								.getName(), 0));
					}
					if (leftPopList.size() > 0) {// 先添加左侧话题
						if (strategyTypsFragment != null) {
							leftBoardId = leftTopicList.get(0).getBoardId()
									+ "";
							selectBoardId = leftBoardId;
							strategyTypsFragment.setBoardId(leftTopicList
									.get(0).getBoardId());
							strategyTypsFragment.downRefreshData();
						}
						leftPopindows = new NewPopupwindows(
								DecStrategyActivity.this, leftPopList,
								new popwindowListener() {

									@Override
									public void doChoose(int position) {
										showLeftOrRight(true);
										tv_left.setText(leftPopList.get(
												position).getName());
										if (strategyTypsFragment != null) {
											strategyTypsFragment
													.setBoardId(leftTopicList
															.get(position)
															.getBoardId());
											strategyTypsFragment
													.setTopicId(leftTopicList
															.get(position)
															.getId());
											strategyTypsFragment
													.downRefreshData();
										}
									}
								}, 0);
					}
				}
			}
			if (!Tool.isEmpty(resp.getBody().get("boardId"))) {
				if (strategyAskFragment != null) {
					rightBoardId = resp.getBody().get("boardId").toString();
					strategyAskFragment.setBoardId(Integer
							.parseInt(rightBoardId));
					strategyAskFragment.downRefreshData();
				}
			}
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_res_title:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				Intent intent = new Intent(DecStrategyActivity.this,
						NewPublicPostActivity.class);
				intent.putExtra("boardId", selectBoardId);
				intent.putExtra("menuId", menuId);
				startActivityForResult(intent, TAG_ADDNEWPOST);
			} else {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
			}
			break;
		case R.id.tab_left:
			selectBoardId = leftBoardId;
			showLeftOrRight(true);
			break;
		case R.id.tab_right:
			showLeftOrRight(false);
			selectBoardId = rightBoardId;
			break;
		case R.id.arrowLeft:
			if (leftPopindows != null) {
				leftPopindows.show(v);
			}
			break;
		default:
			break;
		}
	}

	private void showLeftOrRight(boolean b) {
		lineLeft.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
		lineRight.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
		tv_left.setTextColor(b ? getResources().getColor(R.color.clo_ff3499)
				: getResources().getColor(R.color.black_light));
		tv_right.setTextColor(b ? getResources().getColor(R.color.black_light)
				: getResources().getColor(R.color.clo_ff3499));
		viewpager.setCurrentItem(b ? 0 : 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// 添加新帖子,查看帖子详细回来之后要刷新页面的帖子数和回复数
			if (requestCode == TAG_ADDNEWPOST || requestCode == TAG_REFRESHPOST) {
				if (strategyTypsFragment != null) {
					strategyTypsFragment.downRefreshData();
				}
				if (strategyAskFragment != null) {
					strategyAskFragment.downRefreshData();
				}
			}
		}
	}
}
