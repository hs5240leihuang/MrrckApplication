package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MatchCityEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 赛事-3赛区
 * 
 */
public class MatchAreaFragment extends BaseFragment {

	private int matchId;
	private View layout_view;
	private MyGridView gv_openedCity, gv_nextOpenCity;
	private CommonAdapter<MatchCityEntity> openedCityAdapter,
			nextOpenCityAdapter;
	private List<MatchCityEntity> cityList_open = new ArrayList<MatchCityEntity>();
	private List<MatchCityEntity> cityList_nextopen = new ArrayList<MatchCityEntity>();

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
		layout_view = inflater.inflate(R.layout.activity_match_district, null);
		initView();
		return layout_view;
	}

	private void initView() {
		gv_openedCity = (MyGridView) layout_view.findViewById(R.id.gv_start);
		openedCityAdapter = new CommonAdapter<MatchCityEntity>(getActivity(),
				R.layout.item_city, cityList_open) {

			@Override
			public void convert(ViewHolder viewHolder, MatchCityEntity t) {
				String cityname = t.getMatchCityName();
				TextView tv=viewHolder.getView(R.id.tv_cityname);
				tv.setText(cityname);
				tv.setTextColor(Color.parseColor("#000000"));
			}

		};
		gv_openedCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), 
						ShowMainActivity.class);
				intent.putExtra("postsId", cityList_open.get(arg2).getPostsId());
				Bundle bundle = new Bundle();
				bundle.putSerializable("matchCity", cityList_open.get(arg2));
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
		gv_openedCity.setAdapter(openedCityAdapter);
		gv_nextOpenCity = (MyGridView) layout_view
				.findViewById(R.id.gv_start_soon);
		nextOpenCityAdapter = new CommonAdapter<MatchCityEntity>(getActivity(),
				R.layout.item_city, cityList_nextopen) {

			@Override
			public void convert(ViewHolder viewHolder, MatchCityEntity t) {
				String cityname = t.getMatchCityName();
				TextView tv=viewHolder.getView(R.id.tv_cityname);
				tv.setText(cityname);
			}

		};
		gv_nextOpenCity.setAdapter(nextOpenCityAdapter);
	}

	@Override
	public void initValue() {
		Bundle bundle = getArguments();
		matchId = bundle.getInt("matchId");
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
		LogUtil.d("hl", "获取赛区列表matchId=" + matchId);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "100##" + resp.getBody().toString());
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
