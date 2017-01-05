package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MatchCityEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

public class MatchDistrictActivity extends BaseActivity {
	
	private MyGridView gv_start,gv_start_soon;
	private CommonAdapter<MatchCityEntity> openedCityAdapter,
	nextOpenCityAdapter;
	private List<MatchCityEntity> cityList_open = new ArrayList<MatchCityEntity>();
	private List<MatchCityEntity> cityList_nextopen = new ArrayList<MatchCityEntity>();


	private List<MatchCityEntity> cityList = new ArrayList<MatchCityEntity>();
	private String matchId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_match_district;
	}

	@Override
	public void initView() {
		gv_start = (MyGridView) findViewById(R.id.gv_start);
		gv_start_soon = (MyGridView) findViewById(R.id.gv_start_soon);
		openedCityAdapter = new CommonAdapter<MatchCityEntity>(this,
				R.layout.item_city, cityList_open) {

			@Override
			public void convert(ViewHolder viewHolder, final MatchCityEntity t) {
				//viewHolder.setText(R.id.tv_cityname, t.getMatchCityName());
				TextView cityname=viewHolder.getView(R.id.tv_cityname);
				cityname.setTextColor(Color.parseColor("#000000"));
				cityname.setText(t.getMatchCityName());
				viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent =new Intent();
						Bundle bundle =new Bundle();
						bundle.putSerializable("matchCity", t);
						intent.putExtras(bundle);
						setResult(RESULT_OK,intent);
						finish();
					}
				});
			}

		};
		nextOpenCityAdapter = new CommonAdapter<MatchCityEntity>(this,
				R.layout.item_city, cityList_nextopen) {

			@Override
			public void convert(ViewHolder viewHolder, MatchCityEntity t) {
				viewHolder.setText(R.id.tv_cityname, t.getMatchCityName());
			}

		};
		gv_start.setAdapter(openedCityAdapter);
		gv_start_soon.setAdapter(nextOpenCityAdapter);
	}

	@Override
	public void initValue() {
		matchId = getIntent().getStringExtra("matchId");
		getOpenedCity();
	}

	/**
	 * 获取赛区列表
	 */
	private void getOpenedCity() {
		cityList_open.clear();
		cityList_nextopen.clear();
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("matchId", matchId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_AREA));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
		LogUtil.d("hl", "获取赛区列表" + matchId);
	}
	
	public void bindListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("matchCity") + "").length() > 2) {
				List<MatchCityEntity> cityList = (List<MatchCityEntity>) JsonUtil
						.jsonToList(resp.getBody().get("matchCity").toString(),
								new TypeToken<List<MatchCityEntity>>() {
								}.getType());
				cityList_open.addAll(cityList);
			}

			openedCityAdapter.notifyDataSetChanged();
			if ((resp.getBody().get("matchCityTemp") + "").length() > 2) {
				List<MatchCityEntity> cityData = (List<MatchCityEntity>) JsonUtil
						.jsonToList(resp.getBody().get("matchCityTemp")
								.toString(),
								new TypeToken<List<MatchCityEntity>>() {
								}.getType());
				cityList_nextopen.addAll(cityData);
			}
			nextOpenCityAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub
		
	}
}
