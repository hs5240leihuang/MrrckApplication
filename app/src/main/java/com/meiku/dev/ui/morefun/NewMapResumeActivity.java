package com.meiku.dev.ui.morefun;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumeBean;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.recruit.ResumeNoHfive;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CircleImageView;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.HintDialogwk.DoOneClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.volleyextend.StringJsonRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 附近简历-百度地图实现
 * 
 * @author huanglei
 */
public class NewMapResumeActivity extends Activity {
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private HashMap<Marker, ResumeBean> map;
	private LocationClient mLocClient;
	boolean isFirstLoc = true; // 是否首次定位
	protected boolean hasCompany;
	protected String authPassFlag;
	protected String authResult;
	private LinearLayout lin_renzheng;
	private Button btn_renzheng;
	protected double first_lat;
	protected double first_lng;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_resume_activity);
		regisBroadcast();
		initView();
		CheckCompanyIsCertificate();
	}

	private void initView() {
		lin_renzheng = (LinearLayout) findViewById(R.id.lin_renzheng);
		btn_renzheng = (Button) findViewById(R.id.btn_renzheng);
		btn_renzheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(NewMapResumeActivity.this);
					return;
				}
				if (hasCompany) {
					if (authPassFlag.equals("0")) {
						Intent intent = new Intent(NewMapResumeActivity.this,
								EditCompInfoActivity.class);
						startActivity(intent);
					} else if (authPassFlag.equals("2")) {
						final CommonDialog commonDialog = new CommonDialog(
								NewMapResumeActivity.this, "提示",
								"您的企业信息未通过审核！\n 原因：" + authResult, "修改", "取消");
						commonDialog.show();
						commonDialog
								.setClicklistener(new CommonDialog.ClickListenerInterface() {
									@Override
									public void doConfirm() {
										commonDialog.dismiss();
										Intent intent = new Intent(
												NewMapResumeActivity.this,
												EditCompInfoActivity.class);
										startActivity(intent);
									}

									@Override
									public void doCancel() {
										commonDialog.dismiss();
									}
								});
					}
				} else {
					startActivityForResult(new Intent(
							NewMapResumeActivity.this,
							CompanyCertificationActivity.class).putExtra(
							"flag", "3"), 100);
				}
			}
		});
		mapView = (MapView) findViewById(R.id.map);
		mBaiduMap = mapView.getMap();
		// 显示我的定位位置-------------start
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mapView == null) {
					return;
				}
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					first_lat = location.getLatitude();
					first_lng = location.getLongitude();
					if (first_lat == 0 || first_lng == 0) {
						first_lat = MrrckApplication.laitude;
						first_lng = MrrckApplication.longitude;
					}
					LatLng ll = new LatLng(first_lat, first_lng);
					MapStatus.Builder builder = new MapStatus.Builder();
					builder.target(ll).zoom(15.0f);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder.build()));
				}
			}
		});
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 显示我的定位位置-------------end
		init();
		if (MrrckApplication.laitude != 0 && MrrckApplication.longitude != 0) {
			LatLng ll = new LatLng(MrrckApplication.laitude,
					MrrckApplication.longitude);
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ll).zoom(15.0f);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(builder.build()));
		}
	}

	private void init() {
		MkUser userData = AppContext.getInstance().getUserInfo();
		int userId = userData.getId();
		int compayId = -1;
		if (null != userData.getCompanyEntity()
				&& !Tool.isEmpty(userData.getCompanyEntity().getId())) {
			compayId = userData.getCompanyEntity().getId();
		}
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("companyId", compayId);
		if (MrrckApplication.laitude != 0 && MrrckApplication.longitude != 0) {
			map.put("longitude", MrrckApplication.longitude);
			map.put("latitude", MrrckApplication.laitude);
		} else {
			if (first_lat != 0 && first_lng != 0) {
				map.put("longitude", first_lng);
				map.put("latitude", first_lat);
			} else {
				map.put("longitude", -1);
				map.put("latitude", -1);
			}
		}
		LogUtil.d("hl", "请求附近简历=" + map);
		reqBase.setHeader(new ReqHead(AppConfig.COORDINATE));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		StringJsonRequest request = new StringJsonRequest(AppConfig.DOMAIN
				+ AppConfig.EMPLOY_REQUEST_MAPPING, new Listener<String>() {
			@Override
			public void onResponse(String resp) {
				ReqBase basejson = (ReqBase) JsonUtil.jsonToObj(ReqBase.class,
						resp);
				ReqHead head = basejson.getHeader();
				if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
					if (basejson.getBody().get("resume") != null
							&& basejson.getBody().get("resume").toString()
									.length() > 2) {
						List<ResumeBean> resumeReq = (List<ResumeBean>) JsonUtil
								.jsonToList(basejson.getBody().get("resume")
										.toString(),
										new TypeToken<List<ResumeBean>>() {
										}.getType());
						if (resumeReq != null && resumeReq.size() > 0) {
							getLocationResum(resumeReq);
						}
					}
				}
			}
		}, JsonUtil.objToJson(reqBase), new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MrrckApplication.getInstance().addToRequestQueue(request);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			private InfoWindow mInfoWindow;

			@Override
			public boolean onMarkerClick(Marker marker) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil
							.showTipToLoginDialog(NewMapResumeActivity.this);
					return false;
				}
				if (hasCompany) {
					if (authPassFlag.equals("1")) {
						mBaiduMap.hideInfoWindow();
						LatLng ll = marker.getPosition();
						OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
							public void onInfoWindowClick() {
								mBaiduMap.hideInfoWindow();
							}
						};
						mInfoWindow = new InfoWindow(setOneInfoView(marker),
								ll, -47);
						mBaiduMap.showInfoWindow(mInfoWindow);
						MapStatus.Builder builder = new MapStatus.Builder();
						builder.target(marker.getPosition());
						mBaiduMap.animateMapStatus(MapStatusUpdateFactory
								.newMapStatus(builder.build()));
					} else if (authPassFlag.equals("0")) {
						Intent intent = new Intent(NewMapResumeActivity.this,
								EditCompInfoActivity.class);
						startActivity(intent);
					} else {
						final CommonDialog commonDialog = new CommonDialog(
								NewMapResumeActivity.this, "提示",
								"您的企业信息未通过审核！\n 原因：" + authResult, "修改", "取消");
						commonDialog.show();
						commonDialog
								.setClicklistener(new CommonDialog.ClickListenerInterface() {
									@Override
									public void doConfirm() {
										commonDialog.dismiss();
										Intent intent = new Intent(
												NewMapResumeActivity.this,
												EditCompInfoActivity.class);
										startActivity(intent);
									}

									@Override
									public void doCancel() {
										commonDialog.dismiss();
									}
								});
					}
				} else {
					final HintDialogwk dialog = new HintDialogwk(
							NewMapResumeActivity.this, "您还不是企业用户，马上去认证", "Go >");
					dialog.setOneClicklistener(new DoOneClickListenerInterface() {

						@Override
						public void doOne() {
							startActivityForResult(new Intent(
									NewMapResumeActivity.this,
									CompanyCertificationActivity.class)
									.putExtra("flag", "3"), 100);
							dialog.dismiss();
						}
					});
					dialog.show();
				}
				return false;
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
			}
		});
	}

	public void getLocationResum(List<ResumeBean> listResum) {
		map = new HashMap<Marker, ResumeBean>();
		for (int i = 0, size = listResum.size(); i < size; i++) {
			final ResumeBean resume = listResum.get(i);
			if (!Tool.isEmpty(resume.getLatitude())
					&& !Tool.isEmpty(resume.getLongitude())) {
				final LatLng laln = new LatLng(Double.parseDouble(resume
						.getLatitude()), Double.parseDouble(resume
						.getLongitude()));
				if (Tool.isEmpty(listResum.get(i).getClientThumbResumePhoto())) {
					View view = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.near_resume_mark_layout, null);
					Marker marker = (Marker) mBaiduMap
							.addOverlay(new MarkerOptions()
									.position(laln)
									.icon(BitmapDescriptorFactory
											.fromView(view)).zIndex(9)
									.draggable(false));
					map.put(marker, resume);
				} else {
					ImageLoader.getInstance().loadImage(
							listResum.get(i).getClientThumbResumePhoto(),
							new ImageSize(100, 100),
							PictureUtil.normalImageOptions,
							new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String arg0,
										View arg1) {
								}

								@Override
								public void onLoadingFailed(String arg0,
										View arg1, FailReason arg2) {
									View view = LayoutInflater.from(
											getApplicationContext()).inflate(
											R.layout.near_resume_mark_layout,
											null);
									Marker marker = (Marker) mBaiduMap
											.addOverlay(new MarkerOptions()
													.position(laln)
													.icon(BitmapDescriptorFactory
															.fromView(view))
													.zIndex(9).draggable(false));
									map.put(marker, resume);

								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									View view = LayoutInflater.from(
											getApplicationContext()).inflate(
											R.layout.near_resume_mark_layout,
											null);
									CircleImageView head = (CircleImageView) view
											.findViewById(R.id.group_head_img_id);
									head.setImageBitmap(arg2);
									Marker marker = (Marker) mBaiduMap
											.addOverlay(new MarkerOptions()
													.position(laln)
													.icon(BitmapDescriptorFactory
															.fromView(view))
													.zIndex(9).draggable(false));
									map.put(marker, resume);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									// TODO Auto-generated method stub

								}
							});
				}

			}
		}
	}

	public View setOneInfoView(Marker marker) {
		final ResumeBean data = map.get(marker);
		View view = LayoutInflater.from(this).inflate(
				R.layout.near_resume_layout, null);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView work = (TextView) view.findViewById(R.id.work);
		TextView skill_content = (TextView) view
				.findViewById(R.id.skill_content);
		TextView distance = (TextView) view.findViewById(R.id.distance);

		LinearLayout lin_head = (LinearLayout) view.findViewById(R.id.lin_head);
		MySimpleDraweeView img_head = new MySimpleDraweeView(
				NewMapResumeActivity.this);
		lin_head.removeAllViews();
		lin_head.addView(img_head, new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
		img_head.setUrlOfImage(data.getClientThumbResumePhoto());
		ImageView gender_img = (ImageView) view.findViewById(R.id.gender);
		TextView check_your_resume = (TextView) view
				.findViewById(R.id.check_your_resume);
		TextView launch_view = (TextView) view.findViewById(R.id.launch_view);

		name.setText(data.getRealName());// 姓名
		work.setText(data.getJobAgeValue() + "");// 美业年龄
		skill_content.setText(data.getKnowledge());// 专业技能
		if (!Tool.isEmpty(data.getLatitude())
				&& !Tool.isEmpty(data.getLongitude())) {
			distance.setText(getDistance(
					Double.parseDouble(data.getLatitude()),
					Double.parseDouble(data.getLongitude())));// 距离
		}
		if ("1".equals(data.getGender())) {
			gender_img.setBackgroundResource(R.drawable.boy_r);
		} else {
			gender_img.setBackgroundResource(R.drawable.girl_r);
		}
		launch_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBaiduMap.hideInfoWindow();
			}
		});
		/**
		 * 查看简历
		 */
		check_your_resume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mBaiduMap.hideInfoWindow();
				// Intent intent = new Intent(NewMapResumeActivity.this,
				// WebViewActivity.class);
				// intent.putExtra("webUrl", data.getDetailUrl() + "?userId="
				// + data.getUserId() + "&resumeId=" + data.getId());
				// intent.putExtra("title", data.getRealName() + "的简历");
				// startActivity(intent);
				startActivity(new Intent(NewMapResumeActivity.this,
						ResumeNoHfive.class).putExtra(
						"webUrl",
						data.getDetailUrl() + "?userId=" + data.getUserId()
								+ "&resumeId=" + data.getId()).putExtra(
						"resumeId", data.getId()));
			}
		});
		return view;
	}

	private String getDistance(double latitude, double longitude) {
		float[] results = new float[1];
		Location.distanceBetween(latitude, longitude, MrrckApplication.laitude,
				MrrckApplication.longitude, results);
		double intdistance = (double) results[0] / 1000;
		DecimalFormat df = new DecimalFormat("#####.##");
		String st = df.format(intdistance);
		return st + "km";
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.onPause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mapView != null) {
			mapView.onResume();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mapView != null) {
			mapView.onDestroy();
		}
		unregisterReceiver(receiver);
		if (map != null) {
			Iterator<Entry<Marker, ResumeBean>> iter = map.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				((Marker) entry.getKey()).getIcon().recycle();
			}
		}
		System.gc();
	}

	/**
	 * 检测公司是否认证
	 */
	private void CheckCompanyIsCertificate() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_VERIFY));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		StringJsonRequest request = new StringJsonRequest(AppConfig.DOMAIN
				+ AppConfig.EMPLOY_REQUEST_MAPPING, new Listener<String>() {
			@Override
			public void onResponse(String resp) {
				ReqBase basejson = (ReqBase) JsonUtil.jsonToObj(ReqBase.class,
						resp);
				ReqHead head = basejson.getHeader();
				if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
					String companyEntityStr = basejson.getBody().get("company")
							.toString();
					LogUtil.d("hl", "检测认证company__" + companyEntityStr);
					if (Tool.isEmpty(companyEntityStr)
							|| companyEntityStr.length() == 2) {
						hasCompany = false;
						lin_renzheng.setVisibility(View.VISIBLE);
					} else {
						CompanyEntity companyEntity = (CompanyEntity) JsonUtil
								.jsonToObj(CompanyEntity.class,
										companyEntityStr);
						hasCompany = true;
						authPassFlag = companyEntity.getAuthPass();// 0:审核中,1:已完成,2:不通过
						authResult = companyEntity.getAuthResult();// 提示语
						if ("1".equals(authPassFlag)) {
							lin_renzheng.setVisibility(View.GONE);
						} else {
							lin_renzheng.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		}, JsonUtil.objToJson(reqBase), new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MrrckApplication.getInstance().addToRequestQueue(request);
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_IM_REFRESH_COMPANY.equals(intent
					.getAction())) {
				CheckCompanyIsCertificate();
			}
		}
	};
}
