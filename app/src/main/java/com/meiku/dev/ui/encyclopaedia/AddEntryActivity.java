package com.meiku.dev.ui.encyclopaedia;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.MkMrrckBaikeBasic;
import com.meiku.dev.bean.MkMrrckBaikeContent;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;

public class AddEntryActivity extends BaseActivity {
	private int moreflag;
	private TextView center_txt_title;
	private EditText et_citiao, et_addintroduce;
	private MkMrrckBaikeContent mkMrrckBaikeContent;
	private MkMrrckBaikeBasic mkMrrckBaikeBasic;
	private int baikeId;
	private int categoryId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_addcitiao;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		et_citiao = (EditText) findViewById(R.id.et_citiao);
		et_addintroduce = (EditText) findViewById(R.id.et_addintroduce);
	}

	@Override
	public void initValue() {
		moreflag = getIntent().getIntExtra("moreflag", 0);
		if (moreflag == 1) {
			center_txt_title.setText("添加达人词条");
		} else if (moreflag == 2) {
			center_txt_title.setText("添加名企词条");
		} else if (moreflag == 3) {
			center_txt_title.setText("添加公共词条");
		} else if (moreflag == 4) {
			center_txt_title.setText("添加基本信息");
			findViewById(R.id.view_red).setVisibility(View.GONE);
		} else if (moreflag == 5) {
			center_txt_title.setText("编辑自定义详细信息");
			mkMrrckBaikeContent = (MkMrrckBaikeContent) getIntent()
					.getSerializableExtra("baikeContentInfo");
			baikeId = getIntent().getIntExtra("baikeId", -1);
			categoryId = getIntent().getIntExtra("addiIntent", -1);
			if (!Tool.isEmpty(mkMrrckBaikeContent)) {
				et_citiao.setText(mkMrrckBaikeContent.getTitle());
				et_addintroduce.setText(mkMrrckBaikeContent.getContent());
			}
		} else if (moreflag == 6) {
			center_txt_title.setText("编辑自定义基本信息");
			mkMrrckBaikeBasic = (MkMrrckBaikeBasic) getIntent()
					.getSerializableExtra("baseInfo");
			baikeId = getIntent().getIntExtra("baikeId", -1);
			categoryId = getIntent().getIntExtra("addiIntent", -1);
			if (!Tool.isEmpty(mkMrrckBaikeBasic)) {
				et_citiao.setText(mkMrrckBaikeBasic.getTitle());
				et_addintroduce.setText(mkMrrckBaikeBasic.getContent());
			}
		} else if (moreflag == 7) {
			center_txt_title.setText("添加自定义详细信息");
			baikeId = getIntent().getIntExtra("baikeId", -1);
			categoryId = getIntent().getIntExtra("addiIntent", -1);
		} else if (moreflag == 8) {
			center_txt_title.setText("添加自定义基本信息");
			baikeId = getIntent().getIntExtra("baikeId", -1);
			categoryId = getIntent().getIntExtra("addiIntent", -1);
			findViewById(R.id.view_red).setVisibility(View.GONE);
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Tool.isEmpty(et_citiao.getText().toString())) {
					ToastUtil.showShortToast("标题不能为空");
					return;
				}
				if (moreflag == 1 || moreflag == 2 || moreflag == 3
						|| moreflag == 4) {
					setResult(
							RESULT_OK,
							new Intent().putExtra("title",
									et_citiao.getText().toString()).putExtra(
									"detail",
									et_addintroduce.getText().toString()));
					finish();
				} else if (moreflag == 5) {// 编辑时--修改详细信息
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("baikeId", baikeId);
					map.put("categoryId", categoryId);
					map.put("contentId", mkMrrckBaikeContent.getId());
					map.put("title", et_citiao.getText().toString());
					map.put("content", et_addintroduce.getText().toString());
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_BK_DITONEDETAIL));
					req.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeOne, AppConfig.PUBLICK_BAIKE, req, true);
				} else if (moreflag == 6) {// 编辑时--修改基本信息
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("baikeId", baikeId);
					map.put("categoryId", categoryId);
					map.put("basicId", mkMrrckBaikeBasic.getId());
					map.put("title", et_citiao.getText().toString());
					map.put("content", et_addintroduce.getText().toString());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_BK_EDITONEBASE));
					req.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeTwo, AppConfig.PUBLICK_BAIKE, req, true);
				} else if (moreflag == 7) {// 编辑时--新增详细信息
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("baikeId", baikeId);
					map.put("categoryId", categoryId);
					map.put("title", et_citiao.getText().toString());
					map.put("content", et_addintroduce.getText().toString());
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_BK_ADDONEDETAIL));
					req.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeThree, AppConfig.PUBLICK_BAIKE, req, true);
				} else if (moreflag == 8) {// 编辑时--新增基本信息
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("baikeId", baikeId);
					map.put("categoryId", categoryId);
					map.put("title", et_citiao.getText().toString());
					map.put("content", et_addintroduce.getText().toString());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_BK_ADDONEBASE));
					req.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeFour, AppConfig.PUBLICK_BAIKE, req, true);
				}
			}
		});
		findViewById(R.id.goback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputTools.HideKeyboard(v);
						finishWhenTip();
					}
				});

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hl", "result__" + reqBase.getBody());
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("修改成功");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("修改基本信息成功");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("添加成功");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("添加基本信息成功");
			break;
		default:
			break;
		}
		setResult(
				RESULT_OK,
				new Intent().putExtra("title",
						et_citiao.getText().toString()).putExtra(
						"detail",
						et_addintroduce.getText().toString()));
		finish();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("修改失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("修改基本信息失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("添加失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("添加基本信息失败");
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		if (!Tool.isEmpty(et_citiao.getText().toString())
				|| !Tool.isEmpty(et_addintroduce.getText().toString())) {
			final CommonDialog commonDialog = new CommonDialog(
					AddEntryActivity.this, "提示", "是否放弃当前操作?", "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							finish();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
		} else {
			finish();
		}
	}
}
