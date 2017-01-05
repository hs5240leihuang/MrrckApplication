package com.meiku.dev.ui.morefun;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.CommontWorkAdressBean;
import com.meiku.dev.bean.CommontWorkAdressReq;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * Created by Administrator on 2015/8/31.
 */
public class CommonDescriptionActivity extends BaseActivity implements
		View.OnClickListener {
	private TextView more;
	private EditText editText;
	private TextView topTitle;// 标题
	private ListView listview_comment;
	private LinearLayout edit_layout;// 职位描述布局
	private LinearLayout search_layout;// 上班地址布局
	private ImageView search_image;
	private EditText search_edit;
	private List<CommontWorkAdressBean> list = new ArrayList<CommontWorkAdressBean>();
	private CommonAdapter<CommontWorkAdressBean> coMadapter;// 城市

	@Override
	public void initView() {
		more = (TextView) findViewById(R.id.right_txt_title);
		more.setOnClickListener(this);

		topTitle = (TextView) findViewById(R.id.center_txt_title);
		editText = (EditText) findViewById(R.id.description_edit);
		search_edit = (EditText) findViewById(R.id.search_edit);
		findViewById(R.id.goback).setOnClickListener(this);

		listview_comment = (ListView) findViewById(R.id.listview_comment);

		search_layout = (LinearLayout) findViewById(R.id.search_layout);
		edit_layout = (LinearLayout) findViewById(R.id.edit_layout);

		search_image = (ImageView) findViewById(R.id.search_image);
		search_image.setOnClickListener(this);

		int requestCode;
		requestCode = getIntent().getIntExtra("requestCode", 0);
		switch (requestCode) {
		case RecruitmentTreasureActivity.JOBDESCRIPTION:
			topTitle.setText("职位描述");
			edit_layout.setVisibility(View.VISIBLE);
			search_layout.setVisibility(View.GONE);
			break;
		case RecruitmentTreasureActivity.MARRYSTARE:
			topTitle.setText("上班地址");
			search_layout.setVisibility(View.VISIBLE);
			edit_layout.setVisibility(View.GONE);
			break;
		}
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 提交
		 */
		case R.id.right_txt_title:
			Intent intent = new Intent();
			String selected = editText.getText().toString();
			if ("".equals(selected)) {
				ToastUtil.showShortToast("请填写内容");
			} else {
				intent.putExtra(getIntent().getStringExtra("tag"), selected);
				setResult(RESULT_OK, intent);
				finish();
			}

			break;
		case R.id.goback:
			finish();
			break;
		case R.id.search_image:
			final String url;
			String cityname = search_edit.getText().toString();
			if (null != cityname) {
				url = String.format(AppConfig.BAIDU_MAP_HTTP_API, cityname,
						"全国", "nPe37O7tGilHjNGpzM5szgHN");
				httpGetWithUrl(reqCodeOne, url, String.class, true, null);
			}
			break;

		}

	}

	private void getData() {
		coMadapter = new CommonAdapter<CommontWorkAdressBean>(
				getApplicationContext(), R.layout.select_item_group, list) {
			@Override
			public void convert(ViewHolder viewHolder,
					final CommontWorkAdressBean jobClassEntities) {
				final String name = jobClassEntities.getName();
				final String district = jobClassEntities.getDistrict();
				final String city = jobClassEntities.getCity();
				viewHolder.setText(R.id.text_ku, city + district + name);
				viewHolder.getView(R.id.text_ku).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								// intent.putExtra("cityCode",)
								intent.putExtra(
										getIntent().getStringExtra("tag"), city
												+ district + name);
								setResult(RESULT_OK, intent);
								finish();
							}
						});
			}
		};
		listview_comment.setAdapter(coMadapter);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.common_description_activity;
	}

	@Override
	public void initValue() {
		initData();
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		Log.v("hl", "CommonDescriptionActivity__" + arg0);
		CommontWorkAdressReq commontWorkAdressReq = (CommontWorkAdressReq) JsonUtil
				.jsonToObj(CommontWorkAdressReq.class, (String) arg0);
		if (!Tool.isEmpty(commontWorkAdressReq)) {
			list = commontWorkAdressReq.result;
			getData();
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
