package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.meiku.dev.views.WheelSelectDialog;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 智能报价
 * 
 */
public class QuotedPriceActivity extends BaseActivity implements
		OnClickListener {

	/** 店铺面积 */
	private EditText et_size;
	/** 店铺类别 */
	private TextView tv_type;
	private int shopType;
	/** 材料费等级 */
	private TextView tv_materialLevel;
	private int materialPiceLevel;
	/** 施工队伍等级 */
	private TextView tv_teamLevel;
	private int buildTeamLevel;
	/** 设计费等级 */
	private TextView tv_designLevel;
	private int designPiceLevel;
	/** 是否需要监理 */
	private ImageView iv_supervisor;
	private int supervisionFlag = 0;// 1 需要0不需要
	/** 是否加急 */
	private ImageView iv_urgent;
	private int urgentDecorateFlag = 0;// 1 需要0不需要
	/** 所在城市 */
	private TextView tv_city;
	private String[] typeStrs, materialPiceLevelStrs, buildTeamLevelStrs,
			designPiceLevelStrs;
	private int selectProvinceCode = -1;
	private int selectCityCode = -1;
	private EditText et_name;
	private EditText et_phone;
	private List<MkDecorateCategory> typeList = new ArrayList<MkDecorateCategory>();
	private List<MkDecorateCategory> materialPiceLevelList = new ArrayList<MkDecorateCategory>();
	private List<MkDecorateCategory> buildTeamLevelList = new ArrayList<MkDecorateCategory>();
	private List<MkDecorateCategory> designPiceLevelList = new ArrayList<MkDecorateCategory>();
	private TextView tv_zxbjPrice, tv_rgPrice, tv_clPrice, tv_sjPrice,
			tv_jlPrice,tv_jiajiPrice;
	private String materialPiceValue;
	private String buildTeamValue;
	private String designPiceValue;
	protected String selectProvinceName;
	protected String selectCityName;

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
	protected int getCurrentLayoutID() {
		return R.layout.activity_quotedprice;
	}

	@Override
	public void initView() {
		et_size = (EditText) findViewById(R.id.et_size);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_materialLevel = (TextView) findViewById(R.id.tv_materialLevel);
		tv_teamLevel = (TextView) findViewById(R.id.tv_teamLevel);
		tv_designLevel = (TextView) findViewById(R.id.tv_designLevel);
		tv_city = (TextView) findViewById(R.id.tv_city);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
		iv_supervisor = (ImageView) findViewById(R.id.iv_supervisor);
		iv_urgent = (ImageView) findViewById(R.id.iv_urgent);
		tv_zxbjPrice = (TextView) findViewById(R.id.tv_zxbjPrice);
		tv_rgPrice = (TextView) findViewById(R.id.tv_rgfPrice);
		tv_clPrice = (TextView) findViewById(R.id.tv_clPrice);
		tv_sjPrice = (TextView) findViewById(R.id.tv_sjPrice);
		tv_jlPrice = (TextView) findViewById(R.id.tv_jlPrice);
		tv_jiajiPrice = (TextView) findViewById(R.id.tv_jiajiPrice);
	}

	@Override
	public void initValue() {
		typeList = MKDataBase.getInstance().getDecorateCategoryList(0, 0);
		if (!Tool.isEmpty(typeList)) {
			int size = typeList.size();
			typeStrs = new String[size];
			for (int i = 0; i < size; i++) {
				typeStrs[i] = typeList.get(i).getName();
			}
		}
		materialPiceLevelList = MKDataBase.getInstance()
				.getDecorateCategoryList(1);
		if (!Tool.isEmpty(typeList)) {
			int size = materialPiceLevelList.size();
			materialPiceLevelStrs = new String[size];
			for (int i = 0; i < size; i++) {
				materialPiceLevelStrs[i] = materialPiceLevelList.get(i)
						.getName();
			}
		}
		buildTeamLevelList = MKDataBase.getInstance()
				.getDecorateCategoryList(1);
		if (!Tool.isEmpty(typeList)) {
			int size = buildTeamLevelList.size();
			buildTeamLevelStrs = new String[size];
			for (int i = 0; i < size; i++) {
				buildTeamLevelStrs[i] = buildTeamLevelList.get(i).getName();
			}
		}
		designPiceLevelList = MKDataBase.getInstance().getDecorateCategoryList(
				3);
		if (!Tool.isEmpty(typeList)) {
			int size = designPiceLevelList.size();
			designPiceLevelStrs = new String[size];
			for (int i = 0; i < size; i++) {
				designPiceLevelStrs[i] = designPiceLevelList.get(i).getName();
			}
		}
	}

	@Override
	public void bindListener() {
		tv_type.setOnClickListener(this);
		tv_materialLevel.setOnClickListener(this);
		tv_teamLevel.setOnClickListener(this);
		tv_designLevel.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		iv_supervisor.setOnClickListener(this);
		iv_urgent.setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==>" + resp.getBody());
		if (!Tool.isEmpty(resp.getBody())
				&& !Tool.isEmpty(resp.getBody().get("data"))) {
			Map<String, String> map = JsonUtil.jsonToMap(resp.getBody()
					.get("data").toString());
			tv_zxbjPrice.setText("¥ " + map.get("formulaResult"));
			tv_rgPrice.setText("人工费    ¥ " + map.get("buildTeamResult"));
			tv_clPrice.setText("材料费    ¥ " + map.get("materialPiceResult"));
			tv_sjPrice.setText("设计费    ¥ " + map.get("designPiceResult"));
			tv_jlPrice.setText("监理费    ¥ " + map.get("supervisionPice"));
			tv_jiajiPrice.setText("加急费    ¥ " + map.get("urgentDecoratePice"));
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						QuotedPriceActivity.this, "提示", resp.getHeader()
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_type:
			if (Tool.isEmpty(typeStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(QuotedPriceActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_type.setText(str);
							shopType = typeList.get(itemIndex).getCode();
						}
					}, typeStrs).show();
			break;
		case R.id.tv_materialLevel:
			if (Tool.isEmpty(materialPiceLevelStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(QuotedPriceActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_materialLevel.setText(str);
							materialPiceLevel = materialPiceLevelList.get(
									itemIndex).getCode();
							materialPiceValue = materialPiceLevelList.get(
									itemIndex).getRemark();
						}
					}, materialPiceLevelStrs).show();
			break;
		case R.id.tv_teamLevel:
			if (Tool.isEmpty(buildTeamLevelStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(QuotedPriceActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_teamLevel.setText(str);
							buildTeamLevel = buildTeamLevelList.get(itemIndex)
									.getCode();
							buildTeamValue = buildTeamLevelList.get(itemIndex)
									.getRemark();
						}
					}, buildTeamLevelStrs).show();
			break;
		case R.id.tv_designLevel:
			if (Tool.isEmpty(designPiceLevelStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(QuotedPriceActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_designLevel.setText(str);
							designPiceLevel = designPiceLevelList
									.get(itemIndex).getCode();
							designPiceValue = designPiceLevelList
									.get(itemIndex).getRemark();
						}
					}, designPiceLevelStrs).show();
			break;
		case R.id.tv_city:
			new WheelSelectCityDialog(QuotedPriceActivity.this, false,
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
		case R.id.iv_supervisor:
			supervisionFlag = (supervisionFlag == 1) ? 0 : 1;
			iv_supervisor
					.setBackgroundResource(supervisionFlag == 1 ? R.drawable.ios7_switch_on
							: R.drawable.ios7_switch_off);
			break;
		case R.id.iv_urgent:
			urgentDecorateFlag = (urgentDecorateFlag == 1) ? 0 : 1;
			iv_urgent
					.setBackgroundResource(urgentDecorateFlag == 1 ? R.drawable.ios7_switch_on
							: R.drawable.ios7_switch_off);
			break;
		case R.id.btn_ok:
			if (Tool.isEmpty(et_size.getText().toString())) {
				ToastUtil.showShortToast("请输入您的店铺面积");
				return;
			}
			if (Tool.isEmpty(shopType)) {
				ToastUtil.showShortToast("请输入您的店铺类别");
				return;
			}
			if (Tool.isEmpty(materialPiceLevel)) {
				ToastUtil.showShortToast("请选择材料费等级");
				return;
			}
			if (Tool.isEmpty(buildTeamLevel)) {
				ToastUtil.showShortToast("请选择施工队伍等级");
				return;
			}
			if (Tool.isEmpty(designPiceLevel)) {
				ToastUtil.showShortToast("请选择设计费等级");
				return;
			}
			if (Tool.isEmpty(tv_city.getText().toString())) {
				ToastUtil.showShortToast("请选择所在城市");
				return;
			}
			if (Tool.isEmpty(et_name.getText().toString())) {
				ToastUtil.showShortToast("请输入姓名");
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
			map.put("areaSize", et_size.getText().toString());
			map.put("shopType", shopType);
			map.put("materialPiceLevel", materialPiceLevel);
			map.put("materialPiceValue", materialPiceValue);
			map.put("buildTeamLevel", buildTeamLevel);
			map.put("buildTeamValue", buildTeamValue);
			map.put("designPiceLevel", designPiceLevel);
			map.put("designPiceValue", designPiceValue);
			map.put("supervisionFlag", supervisionFlag);
			map.put("urgentDecorateFlag", urgentDecorateFlag);
			map.put("provinceCode", selectProvinceCode);
			map.put("provinceName", selectProvinceName);
			map.put("cityCode", selectCityCode);
			map.put("cityName", selectCityName);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_ZNBJ));
			req.setBody(JsonUtil.Map2JsonObj(map));
			LogUtil.d("hl", "————" + map);
			httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
			break;
		default:
			break;
		}

	}

}
