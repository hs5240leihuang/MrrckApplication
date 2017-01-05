package com.meiku.dev.ui.decoration;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.login.AgreeMentActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;

/**
 * 案例意向申请
 */
public class ApplicationDesignActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_city, tv_xieyi;
	private ImageView img_xieyi;
	private EditText et_name, et_phone;
	private String companyId;
	protected int selectCityCode = -1, selectProvinceCode = -1;
	protected String selectCityName, selectProvinceName;
	private boolean flag = false;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_applicationdesign;
	}

	@Override
	public void initView() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setCursorVisible(false);
		et_name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) {
					et_name.setCursorVisible(true);
				}
			}
		});
		img_xieyi = (ImageView) findViewById(R.id.img_xieyi);
		tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);
		tv_xieyi.setText("我已阅读并接受<<装修常见问题服务条款>>");
		Util.setTVShowCloTxt(tv_xieyi, tv_xieyi.getText().toString(), 7,
				tv_xieyi.getText().length(), Color.parseColor("#FF5073"));
		tv_city = (TextView) findViewById(R.id.tv_city);
		Drawable drawable = ContextCompat.getDrawable(
				ApplicationDesignActivity.this, R.drawable.decoration_down);
		drawable.setBounds(0, 0,
				ScreenUtil.dip2px(ApplicationDesignActivity.this, 6),
				ScreenUtil.dip2px(ApplicationDesignActivity.this, 6));
		tv_city.setCompoundDrawables(null, null, drawable, null);
		img_xieyi.setBackgroundResource(R.drawable.zhaozhuangxiuxieyi1);
		flag = true;
	}

	@Override
	public void initValue() {
		companyId = getIntent().getStringExtra("companyId");
	}

	@Override
	public void bindListener() {
		img_xieyi.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		et_name.setOnClickListener(this);
		tv_xieyi.setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ToastUtil.showShortToast("申请成功！");
		finish();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						ApplicationDesignActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_xieyi:
			if (flag) {
				img_xieyi.setBackgroundResource(R.drawable.zhaozhuangxiuxieyi);
				flag = false;
			} else {
				img_xieyi.setBackgroundResource(R.drawable.zhaozhuangxiuxieyi1);
				flag = true;
			}
			break;
		case R.id.tv_city:
			new WheelSelectCityDialog(ApplicationDesignActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_city.setText(cityName);
							selectCityCode = cityCode;
							selectProvinceCode = provinceCode;
							selectProvinceName = provinceName;
							selectCityName = cityName;
						}
					}).show();
			break;
		case R.id.tv_xieyi:
			startActivity(new Intent(ApplicationDesignActivity.this,
					AgreeMentActivity.class).putExtra("type", 1));
			break;
		case R.id.btn_ok:
			if (flag == false) {
				ToastUtil.showShortToast("请先接受《装修常见问题条款》");
				return;
			}
			if (Tool.isEmpty(et_name.getText().toString())) {
				ToastUtil.showShortToast("请填写您的姓名");
				return;
			}
			if (Tool.isEmpty(et_phone.getText().toString())) {
				ToastUtil.showShortToast("请输入手机号");
				return;
			}
			if (Tool.isEmpty(tv_city.getText().toString())) {
				ToastUtil.showShortToast("请选择城市");
				return;
			}
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			map.put("phone", et_phone.getText().toString());
			map.put("name", et_name.getText().toString());
			map.put("provinceCode", selectProvinceCode);
			map.put("provinceName", selectProvinceName);
			map.put("cityCode", selectCityCode);
			map.put("cityName", selectCityName);
			map.put("companyId", companyId);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300015));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
			break;
		case R.id.et_name:
			et_name.setCursorVisible(true);
			break;
		default:
			break;
		}
	}
}
