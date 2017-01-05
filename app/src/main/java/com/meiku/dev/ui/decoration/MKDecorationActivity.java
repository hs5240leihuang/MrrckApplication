package com.meiku.dev.ui.decoration;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 美库装修保
 * 
 */
public class MKDecorationActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_city;
	private EditText et_name, et_phone;
	private Button btn_ok;
	protected int selectCityCode;
	protected int selectProvinceCode;
	protected String selectProvinceName;
	protected String selectCityName;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_mkdecoration;
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
		tv_city = (TextView) findViewById(R.id.tv_city);
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
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
		btn_ok = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	public void initValue() {

	}

	@Override
	public void bindListener() {
		btn_ok.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		et_name.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==>" + resp.getBody());
		final CommonDialog commonDialog = new CommonDialog(
				MKDecorationActivity.this, "提示", resp.getHeader()
						.getRetMessage(), "确定");
		commonDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void doConfirm() {
				commonDialog.dismiss();
				finish();
			}

			@Override
			public void doCancel() {
				commonDialog.dismiss();
			}
		});
		commonDialog.show();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp!=null&&resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						MKDecorationActivity.this, "提示", resp.getHeader()
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
		case R.id.tv_city:
			new WheelSelectCityDialog(MKDecorationActivity.this, false,
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
		case R.id.btn_ok:
			if (Tool.isEmpty(tv_city.getText().toString())) {
				ToastUtil.showShortToast("请选择城市");
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
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			map.put("phone", et_phone.getText().toString());
			map.put("name", et_name.getText().toString());
			map.put("provinceCode", selectProvinceCode);
			map.put("provinceName", selectProvinceName);
			map.put("cityCode", selectCityCode);
			map.put("cityName", selectCityName);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_MKZXB));
			req.setBody(JsonUtil.Map2JsonObj(map));
			LogUtil.d("hl", "————" + map);
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
