package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.bean.NearUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.services.LocationService;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 附近同行
 * 
 */
public class NearbyCounterpartsFragment extends BaseFragment {
	private View layout_view;
	/** 当前页数 */
	private int page = 1;
	/** 职位ID */
	private String positionId = "-1";
	/** 附近人数据 */
	private List<NearUserEntity> showlist = new ArrayList<NearUserEntity>();
	/** 下拉列表控件 */
	private PullToRefreshListView mPullRefreshListView;
	/** 适配器 */
	private CommonAdapter<NearUserEntity> showAdapter;
	/** 是否通过定位来刷新 */
	protected boolean isUpdateByLocationChange = false;
	/** 是否上拉 */
	protected boolean isupRefresh;
	/** 定位服务 */
	private LocationService locationService;
	/** 显示网络状态的布局 */
	private LinearLayout stateLayout;
	/** 显示网络状态提示的TextView */
	private TextView tv_tips;

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
		layout_view = inflater.inflate(R.layout.fragment_nearbycounterparts,
				null);
		regisBroadcast();
		initView();
		return layout_view;
	}

	private void initView() {
		initStatusView();
		initPullListView();
	}

	/**
	 * 初始化显示网络状态的一栏
	 */
	private void initStatusView() {
		stateLayout = (LinearLayout) layout_view
				.findViewById(R.id.im_client_state_view);
		stateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!NetworkTools.isNetworkAvailable(getActivity())) {
					Intent intent = new Intent("android.settings.WIFI_SETTINGS");
					startActivity(intent);
					return;
				}
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
					return;
				}

			}
		});
		tv_tips = (TextView) layout_view.findViewById(R.id.tv_tips);
	}

	/**
	 * 开启定位
	 */
	private void startLocation() {
		locationService = MrrckApplication.getInstance().locationService;
		locationService.registerListener(mListener);
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		locationService.start();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) layout_view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						startLocation();
						mPullRefreshListView.postDelayed(new Runnable() {

							@Override
							public void run() {
								mPullRefreshListView.onRefreshComplete();
							}
						}, 5 * 1000);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isupRefresh = true;
						upRefreshData();
					}
				});

		showAdapter = new CommonAdapter<NearUserEntity>(getActivity(),
				R.layout.item_nearby, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final NearUserEntity t) {
				viewHolder.setText(R.id.tv_name, t.getNickName());
				LinearLayout layout_head = viewHolder.getView(R.id.layout_head);
				layout_head.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(getActivity());
				layout_head.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.getView(R.id.majorImg).setVisibility(View.GONE);
				String intro = t.getIntroduce();
				if (Tool.isEmpty(intro)) {
					intro = "我期望通过手艺交同行朋友";
				}
				viewHolder.setText(R.id.tv_intro, intro);
				TextView zhiwei = viewHolder.getView(R.id.tv_major);
				if (Tool.isEmpty(t.getPositionName())) {
					zhiwei.setVisibility(View.GONE);
				} else {
					zhiwei.setVisibility(View.VISIBLE);
					viewHolder.setText(R.id.tv_major, t.getPositionName());
				}

				viewHolder.setText(R.id.tv_time, t.getKmDistance() + "  |  "
						+ t.getIntervalTime());
				TextView biaoqian = viewHolder.getView(R.id.tv_biaoqian);
				if (Tool.isEmpty(t.getColor()) || Tool.isEmpty(t.getTagName())) {
					biaoqian.setVisibility(View.GONE);
				} else {
					biaoqian.setVisibility(View.VISIBLE);
					Drawable drawable = biaoqian.getBackground();
					int color = Color.parseColor(t.getColor());
					drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
					biaoqian.setText(t.getTagName());
				}
				LinearLayout layout_sex = viewHolder.getView(R.id.layout_sex);
				ImageView iv_sex = viewHolder.getView(R.id.iv_sex);
				if ("1".equals(t.getGender())) {
					iv_sex.setBackgroundResource(R.drawable.nan_white);
					layout_sex.setBackgroundResource(R.drawable.sex_bg_man);
				} else {
					iv_sex.setBackgroundResource(R.drawable.nv_white);
					layout_sex.setBackgroundResource(R.drawable.sex_bg_woman);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!Tool.isEmpty(showlist)) {
									Intent intent = new Intent(getActivity(),
											PersonShowActivity.class);
									intent.putExtra(
											PersonShowActivity.TO_USERID_KEY,
											t.getUserId() + "");
									intent.putExtra("nickName", t.getNickName());
									startActivity(intent);
								}
							}
						});
			}

		};
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
		mPullRefreshListView.setAdapter(showAdapter);
	}

	protected void upRefreshData() {
		page++;
		getNearByPeople(true);
	}

	protected void doRefreshData(boolean showDialog) {
		if (!Util.isGPSOPen(getActivity())) {
			mPullRefreshListView.post(new Runnable() {

				@Override
				public void run() {
					mPullRefreshListView.onRefreshComplete();
				}
			});
			showlist.clear();// pgs未开启，清空页面
			showAdapter.notifyDataSetChanged();
			final CommonDialog commonDialog = new CommonDialog(getActivity(),
					"定位服务未开启", "请在手机设置中开启定位服务以看到附近用户", "开启定位", "忽略");
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							// 转到手机设置界面，用户设置GPS
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							// MrrckApplication.getInstance().laitude = 0;
							// MrrckApplication.getInstance().longitude = 0;
							commonDialog.dismiss();
						}
					});
			commonDialog.show();
		} else {
			showlist.clear();
			page = 1;
			getNearByPeople(showDialog);
		}
	}

	/**
	 * 定位监听
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				StringBuffer sb = new StringBuffer(256);
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					sb.append("gps定位成功");
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					sb.append("网络定位成功");
				} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					sb.append("离线定位成功");
				} else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("服务端网络定位失败");
				} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("网络不同导致定位失败，请检查网络是否通畅");
				} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}
				LogUtil.d("hl", "baidu_location--------->" + sb.toString());
				if (!Tool.isEmpty(location.getLatitude())
						&& !Tool.isEmpty(location.getLatitude())) {
					if (!Tool.isEmpty(location.getCity())) {
						MrrckApplication.cityName = location.getCity();
						MrrckApplication.getInstance().initLocationData();// 通过城市名称，查询获取数据库对应省市code
					}
					// 百度定位失败偶尔会出现4.9E-324情况
					if (!"4.9E-324".equals(String.valueOf(location
							.getLatitude()))
							&& !"4.9E-324".equals(String.valueOf(location
									.getLongitude()))) {
						MrrckApplication.laitude = location.getLatitude();
						MrrckApplication.longitude = location.getLongitude();
						showlist.clear();
						page = 1;
						getNearByPeople(true);
					}
					locationService.unregisterListener(mListener); // 注销掉监听
					locationService.stop(); // 停止定位服务 break;

				} else {
					mPullRefreshListView.onRefreshComplete();
				}
			} else {
				mPullRefreshListView.onRefreshComplete();
			}
		}

	};

	/**
	 * 获取附近同行数据
	 */
	public void getNearByPeople(boolean showDialog) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("positionId", positionId);
		map.put("longitude", MrrckApplication.getLongitudeStr());
		map.put("latitude", MrrckApplication.getLaitudeStr());
		map.put("page", page);
		map.put("pageNum", 40);// 5-27日修改为40，原为ConstantKey.PageNum
		LogUtil.d("hl", page + "附近request" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_18042));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req, showDialog);
	}

	@Override
	public void initValue() {
		doRefreshData(true);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// 处理部分手机进入后定位延迟，获取只有自己或者为空的情况
				if (Tool.isEmpty(showlist) || showlist.size() == 1) {
					showlist.clear();
					page = 1;
					getNearByPeople(false);
				}
			}
		}, 3000);
		// 备用
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// 处理部分手机进入后定位延迟，获取只有自己或者为空的情况
				if (Tool.isEmpty(showlist) || showlist.size() == 1) {
					showlist.clear();
					page = 1;
					getNearByPeople(false);
				}
			}
		}, 6000);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hhh", requestCode + "##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = resp.getBody().get("nearUser").toString();
			final List<NearUserEntity> data = new ArrayList<NearUserEntity>();
			// if (AppContext.getInstance().isHasLogin()) {
			// MkUser tempMkUser = AppContext.getInstance().getUserInfo();
			// NearUserEntity nearUserEntity = new NearUserEntity();
			// nearUserEntity.setNickName(tempMkUser.getNickName());
			// nearUserEntity.setClientHeadPicUrl(tempMkUser.getClientHeadPicUrl());
			// nearUserEntity.setClientPositionImgUrl(tempMkUser.getClientPostionUrl);
			// nearUserEntity.setIntroduce(tempMkUser.getIntroduce());
			// nearUserEntity.setPositionName(tempMkUser.getPositionName());
			// nearUserEntity.setKmDistance("0.00km");
			// nearUserEntity.setIntervalTime("1分钟前");
			// nearUserEntity.setAgeValue(tempMkUser.get());
			// nearUserEntity.setGender(tempMkUser.getGender());
			// nearUserEntity.setUserId(tempMkUser.getUserId());
			// data.add(nearUserEntity);
			// }
			try {
				data.addAll((List<NearUserEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<NearUserEntity>>() {
						}.getType()));
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(data) && !isUpdateByLocationChange && isupRefresh) {
				ToastUtil.showShortToast("没有更多人");
			} else {
				showlist.addAll(data);
				new AsyncTask<Integer, Integer, Boolean>() {
					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
					}

					@Override
					protected Boolean doInBackground(Integer... params) {
						for (NearUserEntity user : data) {// 成员存DB
							IMYxUserInfo userInfo = new IMYxUserInfo();
							userInfo.setNickName(user.getNickName());
							userInfo.setUserHeadImg(user
									.getClientThumbHeadPicUrl());
							userInfo.setYxAccount(user.getLeanCloudUserName());
							userInfo.setUserId(user.getUserId());
							MsgDataBase.getInstance().saveOrUpdateYxUser(
									userInfo);
						}
						return null;
					}
				}.execute();
			}
			showAdapter.notifyDataSetChanged();
			isUpdateByLocationChange = false;
			isupRefresh = false;
			mPullRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != mPullRefreshListView) {
			mPullRefreshListView.post(new Runnable() {

				@Override
				public void run() {
					mPullRefreshListView.onRefreshComplete();
				}
			});
		}
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_NEARBY_PEOPLE);
		filter.addAction(BroadCastAction.ACTION_NEARBY_PEOPLE_BYLOCATION);
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_SHAIXUAN);
		filter.addAction(BroadCastAction.ACTION_CHANGE_AVATAR);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_NEARBY_PEOPLE.equals(intent.getAction())) {
				if (null != intent.getExtras()) {
					positionId = intent
							.getStringExtra(ConstantKey.KEY_BOARD_POSITIONID);
					doRefreshData(true);
				} else {
					doRefreshData(true);
				}
			} else if (BroadCastAction.ACTION_NEARBY_PEOPLE_BYLOCATION
					.equals(intent.getAction())) {// 10分钟刷新经纬度,更新附近同行
				isUpdateByLocationChange = true;
				showlist.clear();
				page = 1;
				getNearByPeople(false);
				MrrckApplication.getInstance().initLocationData();
				// 10分钟刷新经纬度，会来次广播，此时需上传经纬度坐标
				if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
					upLoadLocation();
				}
			} else if (BroadCastAction.ACTION_IM_REFRESH_SHAIXUAN.equals(intent
					.getAction())) {
				positionId = "-1";
				doRefreshData(true);
			} else if (BroadCastAction.ACTION_CHANGE_AVATAR.equals(intent
					.getAction())) {// 跟换头像后刷新，让自己头像改变
				doRefreshData(true);
			} else if (BroadCastAction.ACTION_LOGIN_SUCCESS.equals(intent
					.getAction())) {// 登陆成功，刷新数据
				showlist.clear();
				page = 1;
				getNearByPeople(false);
			}
		}
	};

	/**
	 * 网络状况
	 */
	protected void ShowNetWorkStatus() {
		if (!NetworkTools.isNetworkAvailable(getActivity())) {
			tv_tips.setText(R.string.netNoUse);
			showConnectTips(true);
			ToastUtil
					.showLongToast(getResources().getString(R.string.netNoUse));
		} else {
			showConnectTips(false);
		}
	}

	/**
	 * 是否显示网络断开连接的提示
	 * 
	 * @param show
	 */
	private void showConnectTips(boolean show) {
		stateLayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * 上传经纬度
	 */
	private void upLoadLocation() {
		if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			map.put("longitude", MrrckApplication.getInstance()
					.getLongitudeStr());
			map.put("latitude", MrrckApplication.getInstance().getLaitudeStr());
			LogUtil.d("hl", "10min_upLoadLocation==" + map);
			req.setHeader(new ReqHead(AppConfig.UPLOAD_COORDINATES));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeTwo, AppConfig.USER_REQUEST_MAPPING, req, false);
		}
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		if (null != locationService) {
			locationService.stop(); // 停止定位服务;
		}
		super.onDestroy();
	}
}
