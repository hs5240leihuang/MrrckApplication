package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.HomeViewPagerAdapter;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.chat.CreateGroupActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.IMTitleTabsLayout;
import com.meiku.dev.views.IMTitleTabsLayout.IMTabClickListener;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.umeng.analytics.MobclickAgent;

//即时通信
public class ImFragment extends BaseFragment implements OnPageChangeListener {

	private View layout_view;
	/** 头部tabs栏 */
	private IMTitleTabsLayout titletabsBarView;
	/** 职位列表数据库数据 */
	private List<JobClassEntity> resultList;
	/** 职位筛选展示数据 */
	private ArrayList<PopupData> joblist = new ArrayList<PopupData>();
	/** 需要显示的tabsBar数据 */
	private List<String> titletabList = new ArrayList<String>();
	private ViewPager viewpager;
	private List<BaseFragment> listFragment = new ArrayList<BaseFragment>();
	private HomeViewPagerAdapter adapter;
	/** 当前职位ID */
	private LinearLayout titleTabLayout;
	/** 放tabs栏的布局 */
	private TextView tv_shaixuan;
	/** 当前职位ID */
	protected int positionId;
	protected MyPopupwindow popupwindow;
	private int currentPage;
	private String selectedPositionname;

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
		layout_view = inflater.inflate(R.layout.fragment_im, null);
		View titleEmptyTop = (View) layout_view.findViewById(R.id.statuslayout);
		int statusBarHeight = ScreenUtil.getStatusBarHeight(getActivity());
		titleEmptyTop.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, statusBarHeight));
		regisBroadcast();
		initView();
		return layout_view;
	}

	/**
	 * 初始化动态title tab
	 */
	private void initTitleTabs() {
		titletabList.add("附近");
		titletabList.add("消息");
		titletabList.add("库群");
		titletabList.add("通讯录");
		listFragment.add(new NearbyCounterpartsFragment());
		listFragment.add(new MessageMainFragment());
		listFragment.add(new NewKuGroupFragment());
		listFragment.add(new AddressListFragment());
		titleTabLayout = (LinearLayout) layout_view
				.findViewById(R.id.tabLayout);
		titletabsBarView = new IMTitleTabsLayout(getActivity(), titletabList,
				new IMTabClickListener() {

					@Override
					public void onOneTabClick(int index) {
						viewpager.setCurrentItem(index);
					}
				});
		titletabsBarView.setCurrentIndex(0);
		titletabsBarView.setChildView(ScreenUtil.getWindowWidth(getActivity())
				- ScreenUtil.dip2px(getActivity(), 150));
		titleTabLayout.addView(titletabsBarView.getView());
		// 筛选
		tv_shaixuan = (TextView) layout_view.findViewById(R.id.tv_shaixuan);
		tv_shaixuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (currentPage) {
				case 0:
					if (Tool.isEmpty(joblist)) {
						return;
					}
					if (popupwindow != null && !popupwindow.isShowing()) {
						popupwindow.show(v);
					}
					break;
				case 2:
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
						return;
					}
					startActivity(new Intent(getActivity(),
							CreateGroupActivity.class));
					break;
				case 3:
					if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
						return;
					}
					startActivity(new Intent(getActivity(),
							AddFriendActivity.class));
					break;
				default:
					break;
				}
			}
		});
	}

	private void initView() {
		initTitleTabs();
		viewpager = (ViewPager) layout_view
				.findViewById(R.id.content_viewpager);
		adapter = new HomeViewPagerAdapter(getChildFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(4);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	public void initValue() {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				joblist.add(new PopupData("不限", 0));
				resultList = MKDataBase.getInstance().getJobClassIntent();
				for (int i = 0, size = resultList.size(); i < size; i++) {
					PopupData pData = new PopupData(
							resultList.get(i).getName(), 0);
					joblist.add(pData);
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean aBoolean) {
				if (!Tool.isEmpty(joblist)) {
					popupwindow = new MyPopupwindow(getActivity(), joblist,
							new popListener() {

								@Override
								public void doChoose(int position) {
									if (position == 0) {
										positionId = -1;
										tv_shaixuan.setText("筛选人员");
									} else {
										positionId = resultList.get(
												position - 1).getId();
										selectedPositionname = resultList.get(
												position - 1).getName();

										tv_shaixuan
												.setText(selectedPositionname);
									}
									Intent intent = new Intent(
											BroadCastAction.ACTION_NEARBY_PEOPLE);
									intent.putExtra(
											ConstantKey.KEY_BOARD_POSITIONID,
											String.valueOf(positionId));
									getActivity().sendBroadcast(intent);
								}
							});
				}
				super.onPostExecute(aBoolean);
			}
		}.execute();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		titletabsBarView.SetSelecetedIndex(arg0);
		currentPage = arg0;
		switch (arg0) {
		case 0:
			tv_shaixuan.setText(Tool.isEmpty(selectedPositionname) ? "筛选人员"
					: selectedPositionname);
			break;
		case 1:
			tv_shaixuan.setText("");
			break;
		case 2:
			tv_shaixuan.setText("创建群");
			break;
		case 3:
			tv_shaixuan.setText("找人");
			break;
		default:
			break;
		}
	}

	/** 注册广播 */
	private void regisBroadcast() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BroadCastAction.ACTION_IM_REFRESH_SHAIXUAN);
		intentFilter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
	}

	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					BroadCastAction.ACTION_IM_REFRESH_SHAIXUAN)) {
				tv_shaixuan.setText("筛选人员");
			} else if (intent.getAction().equals(
					BroadCastAction.ACTION_LOGIN_SUCCESS)) {// 登陆成功，刷新数据
				// TODO
			}
		}
	};

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

}
