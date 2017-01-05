package com.meiku.dev.ui.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.meiku.dev.R;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.utils.Tool;

/**
 * 点击获取地理位置，查看坐标位置
 * 
 * @author huanglei
 * 
 */
public class GetLoacationActivity extends Activity implements
		OnGetGeoCoderResultListener {
	private MapView mMapView;
	private TextView address;
	private float lat;
	private float lng;
	private TextView right_txt_title;
	private TextView center_txt_title;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	private GeoCoder mSearch;
	private boolean usedType_onlyWatch;// 本页面使用类型，true只查看位置，false带选择位置

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		address = (TextView) findViewById(R.id.address);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		mMapView = (MapView) findViewById(R.id.map);
		mBaiduMap = mMapView.getMap();
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("lat", lat);
				intent.putExtra("lng", lng);
				intent.putExtra("address", address.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		// 显示我的定位位置--------start
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 显示我的定位位置--------end
		usedType_onlyWatch = !Tool.isEmpty(getIntent().getStringExtra(
				ConstantKey.KEY_LAITUDE))
				&& !Tool.isEmpty(getIntent().getStringExtra(
						ConstantKey.KEY_LONGITUDE));
		if (usedType_onlyWatch) {
			right_txt_title.setVisibility(View.GONE);
			center_txt_title.setText("查看位置");
			lat = (float) Float.parseFloat(getIntent().getStringExtra(
					ConstantKey.KEY_LAITUDE));
			lng = (float) Float.parseFloat(getIntent().getStringExtra(
					ConstantKey.KEY_LONGITUDE));
			LatLng latlng = new LatLng(lat, lng);
			getLocationByLatlng(latlng);
			moveMapToLatLng(latlng);
		} else {
			center_txt_title.setText("选择位置");
			mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					return false;
				}

				@Override
				public void onMapClick(LatLng arg0) {
					lat = (float) arg0.latitude;
					lng = (float) arg0.longitude;
					getLocationByLatlng(new LatLng(lat, lng));
				}
			});
		}
	}

	/**
	 * 移动地图中心位置，缩放比例
	 * 
	 * @param latlng
	 */
	private void moveMapToLatLng(LatLng latlng) {
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(latlng).zoom(15.0f);
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder
				.build()));
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
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
				lat = (float) location.getLatitude();
				lng = (float) location.getLongitude();
				LatLng ll = new LatLng(lat, lng);
				if (!usedType_onlyWatch) {// 页面做选择位置使用时，默认显示我的位置为中心
					getLocationByLatlng(ll);
					moveMapToLatLng(ll);
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	protected void getLocationByLatlng(LatLng latlng) {
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			address.setText("抱歉，未能找到结果");
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_center)));
		address.setText(result.getAddress());
	}
}
