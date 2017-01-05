package com.meiku.dev.ui.product;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;


//选择认证
public class SelectDentificationActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_company, img_person;

	@Override
	protected int getCurrentLayoutID() {

		return R.layout.activity_selectrenzheng;
	}

	@Override
	public void initView() {
		img_company = (ImageView) findViewById(R.id.img_company);
		img_person = (ImageView) findViewById(R.id.img_person);
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		img_company.setOnClickListener(this);
		img_person.setOnClickListener(this);

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		String companyEntityStr = resp.getBody().get("company").toString();
		LogUtil.d("hl", "检测认证company__" + companyEntityStr);
		switch (requestCode) {
		case reqCodeThree:
			if (Tool.isEmpty(companyEntityStr)
					|| companyEntityStr.length() == 2) {
				final CommonDialog commonDialog = new CommonDialog(
						SelectDentificationActivity.this, "提示",
						"您还不是企业用户，是否去认证", "去认证", "不了");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						Intent intent1 = new Intent(SelectDentificationActivity.this,
								CompanyCertificationActivity.class);
						startActivityForResult(intent1, reqCodeTwo);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
			} else {
				CompanyEntity companyEntity = (CompanyEntity) JsonUtil
						.jsonToObj(CompanyEntity.class, companyEntityStr);
				if (companyEntity.getAuthPass().equals("1")) {// 0：审核中 1：已完成
																// 2:不通过
					// AppContext.getInstance().getUserInfo()
					// .setCompanyEntity(companyEntity);
					// Intent intent = new Intent(getActivity(),
					// WebViewActivity.class);
					// intent.putExtra("webUrl", zhaopingUrl);
					// startActivity(intent);

					Intent intent = new Intent(SelectDentificationActivity.this,
							PublishProductActivity.class);
					startActivity(intent);
				} else if (companyEntity.getAuthPass().equals("0")) {
					Intent intent = new Intent(SelectDentificationActivity.this,
							EditCompInfoActivity.class);
					startActivity(intent);
				} else {
					final CommonDialog commonDialog = new CommonDialog(
							SelectDentificationActivity.this, "提示", "您的企业信息未通过审核！\n 原因："
									+ companyEntity.getAuthResult(), "修改", "取消");
					commonDialog.show();
					commonDialog
							.setClicklistener(new CommonDialog.ClickListenerInterface() {
								@Override
								public void doConfirm() {
									commonDialog.dismiss();
									Intent intent = new Intent(SelectDentificationActivity.this,
											EditCompInfoActivity.class);
									startActivity(intent);
								}

								@Override 
								public void doCancel() {
									commonDialog.dismiss();
								}
							});
				}

			}

			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_company:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				CheckCompanyIsCertificate();
			} else {
				ShowLoginDialogUtil
						.showTipToLoginDialog(SelectDentificationActivity.this);
			}
			break;
		case R.id.img_person:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				Intent intent2 = new Intent(SelectDentificationActivity.this,
						PersonDentificationActivity.class);
				startActivityForResult(intent2, reqCodeOne);
			} else {
				ShowLoginDialogUtil
						.showTipToLoginDialog(SelectDentificationActivity.this);
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == reqCodeOne) {
				finish();
			} else if (requestCode == reqCodeTwo) {
				finish();
			}
		}
	}

	/**
	 * 检测公司是否认证
	 */
	private void CheckCompanyIsCertificate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_VERIFY));
		httpPost(reqCodeThree, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
	}
}
