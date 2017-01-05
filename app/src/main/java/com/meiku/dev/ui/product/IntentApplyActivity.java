package com.meiku.dev.ui.product;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ProductWillEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

//意向申请
public class IntentApplyActivity extends BaseActivity {

	private TextView tv_name, tv_category;
	private EditText et_instruct, et_contact, et_phone;
	private Button btn_publish;
	private ProductWillEntity intentdata;
	private String pubUserId, productId, productName, productCatagory;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_intent_apply;
	}

	@Override
	public void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_category = (TextView) findViewById(R.id.tv_category);
		et_instruct = (EditText) findViewById(R.id.et_instruct);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_publish = (Button) findViewById(R.id.btn_publish);
	}

	@Override
	public void initValue() {
		productId = getIntent().getStringExtra("product");
		pubUserId = getIntent().getStringExtra("productUserId");
		productName = getIntent().getStringExtra("productName");
		productCatagory = getIntent().getStringExtra("category");
		tv_name.setText(productName);
		tv_category.setText(productCatagory);
		btn_publish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsReady()) {
					addIntent();
					finish();
				}
			}
		});
		getData();
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	/** 查询最近产品意向填写的用户信息接口 */
	public void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_INTENTION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, req);
	}

	/** 新增产品意向申请接口 */
	public void addIntent() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pubUserId", pubUserId);
		map.put("willUserId", AppContext.getInstance().getUserInfo()
				.getUserId());
		map.put("productId", productId);
		map.put("contactName", et_contact.getText().toString());
		map.put("contactPhone", et_phone.getText().toString());
		map.put("willContent", et_instruct.getText().toString());
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_ADD_INTENTION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PRODUCT_REQUEST_MAPPING, req);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// ToastUtil.showShortToast("请求成功");
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("kkk", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("productContact").toString().length() > 2) {
				String jsonstr = resp.getBody().get("productContact")
						.toString();
				intentdata = (ProductWillEntity) JsonUtil.jsonToObj(
						ProductWillEntity.class, jsonstr);
				xuanran();
			} else {

			}

			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("提交成功");
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		case reqCodeOne:
			ToastUtil.showShortToast("失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("失败");
			break;
		default:
			break;
		}

	}

	public void xuanran() {
		et_instruct.setText(intentdata.getWillContent());
		et_contact.setText(intentdata.getContactName());
		et_phone.setText(intentdata.getContactPhone());
	}

	/**
	 * 检测是否具有提交的条件
	 * 
	 * @return
	 */
	private boolean checkIsReady() {
		if (Tool.isEmpty(et_contact.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写联系人");
			return false;
		}
		if (Tool.isEmpty(et_phone.getText().toString().trim())) {
			ToastUtil.showShortToast("请填联系人电话");
			return false;
		}
		if (Tool.isEmpty(et_instruct.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写意向说明");
			return false;
		}

		return true;
	}
}
