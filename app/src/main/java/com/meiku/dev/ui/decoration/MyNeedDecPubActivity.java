package com.meiku.dev.ui.decoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.DecorateNeedEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.PostsAttachment;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.meiku.dev.views.WheelSelectDialog;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;

/**
 * 发布装修需求（编辑）
 */
public class MyNeedDecPubActivity extends BaseActivity implements
		OnClickListener {

	private EditText et_theme/* 主题 */, et_size/* 面积 */, et_name/* 姓名 */,
			et_phone/* 手机号 */;
	private TextView topTip, tv_shopType/* 店铺类型 */, tv_decTime/* 装修时间 */,
			tv_yusuanMoney/* 费用预算 */, tv_city/* 所在城市 */, tv_kefu, tv_xieyi;
	private ImageView img_xieyi/* 协议勾选 */;
	protected String selectCityCode = "", selectProvinceCode = "";
	protected String selectCityName = "", selectProvinceName = "";
	private List<MkDecorateCategory> typeList = new ArrayList<MkDecorateCategory>();
	protected String shopTypeCode = "", yusuanCode = "";
	private List<MkDecorateCategory> yusuanMoneyList;
	private String[] typeStrs, yusuanMoneyStrs;
	private boolean isAgree = true;
	private MyGridView gridview_uploadpic;
	private List<Object> picPathList = new ArrayList<Object>();// 上传店面图片
	private int needUploadPicSize;
	private final int SELECT_COMPANYPICS = 1003;
	private TextView tv_hint;
	public int addpicCount;// 添加图片数量
	private int useType;// 0添加，1修改
	private String postsDeleteIds = "";
	private String attachmentDeleteIds = "";
	private int needId;
	private List<PostsAttachment> oldPic = new ArrayList<PostsAttachment>();
	private ArrayList<String> newAddPathList = new ArrayList<String>();// 新增加的图片list
	private String H5URL;
	private int postsId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myneeddecpub;
	}

	@Override
	public void initView() {
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		picPathList.add("+");
		setSelectPic(true);
		topTip = (TextView) findViewById(R.id.topTip);
		topTip.setVisibility(View.GONE);
		et_theme = (EditText) findViewById(R.id.et_theme);
		tv_shopType = (TextView) findViewById(R.id.tv_shopType);
		et_size = (EditText) findViewById(R.id.et_size);
		tv_decTime = (TextView) findViewById(R.id.tv_decTime);
		tv_yusuanMoney = (TextView) findViewById(R.id.tv_yusuanMoney);
		tv_city = (TextView) findViewById(R.id.tv_city);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
		img_xieyi = (ImageView) findViewById(R.id.img_xieyi);
		tv_kefu = (TextView) findViewById(R.id.tv_kefu);
		tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);
		tv_xieyi.setText("我已阅读并接受<<美库装修宝信息发布规定>>");
		Util.setTVShowCloTxt(tv_xieyi, tv_xieyi.getText().toString(), 7,
				tv_xieyi.getText().length(), Color.parseColor("#FF5073"));
	}

	@Override
	public void initValue() {
		useType = getIntent().getIntExtra("flag", 0);
		needId = getIntent().getIntExtra("id", 0);
		typeList = MKDataBase.getInstance().getDecorateCategoryList(0, 0);
		if (!Tool.isEmpty(typeList)) {
			int size = typeList.size();
			typeStrs = new String[size];
			for (int i = 0; i < size; i++) {
				typeStrs[i] = typeList.get(i).getName();
			}
		}
		yusuanMoneyList = MKDataBase.getInstance()
				.getDecorateCategoryList(5, 0);
		if (!Tool.isEmpty(yusuanMoneyList)) {
			int size = yusuanMoneyList.size();
			yusuanMoneyStrs = new String[size];
			for (int i = 0; i < size; i++) {
				yusuanMoneyStrs[i] = yusuanMoneyList.get(i).getName();
			}
		}
		if (useType == 1) {
			topTip.setVisibility(View.GONE);
			findViewById(R.id.layout_xieyi).setVisibility(View.GONE);
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", needId);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300042));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
		} else if (useType == 0) {
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300048));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeFour, AppConfig.PUBLICK_DECORATION, req);
		}
		String caogaoStr = (String) PreferHelper.getSharedParam(
				ConstantKey.SP_PUB_DECNEED, "");
		if (!Tool.isEmpty(caogaoStr)) {
			try {
				Map<String, String> caogaoMap = JsonUtil.jsonToMap(caogaoStr);
				if (caogaoMap.containsKey("userId")
						&& (AppContext.getInstance().getUserInfo().getId() + "")
								.equals(caogaoMap.get("userId"))) {
					if (caogaoMap.containsKey("needName")) {
						et_theme.setText(caogaoMap.get("needName"));
					}
					if (caogaoMap.containsKey("shopCategoryName")) {
						tv_shopType.setText(caogaoMap.get("shopCategoryName"));
					}
					if (caogaoMap.containsKey("shopCategory")) {
						shopTypeCode = caogaoMap.get("shopCategory");
					}
					if (caogaoMap.containsKey("areaSize")) {
						et_size.setText(caogaoMap.get("areaSize"));
					}
					if (caogaoMap.containsKey("decorateTime")) {
						tv_decTime.setText(caogaoMap.get("decorateTime"));
					}
					if (caogaoMap.containsKey("costBudgetName")) {
						tv_yusuanMoney.setText(caogaoMap.get("costBudgetName"));
					}
					if (caogaoMap.containsKey("costBudget")) {
						yusuanCode = caogaoMap.get("costBudget");
					}
					if (caogaoMap.containsKey("cityName")) {
						tv_city.setText(caogaoMap.get("cityName"));
						selectCityName = caogaoMap.get("cityName");
					}
					if (caogaoMap.containsKey("cityCode")) {
						selectCityCode = caogaoMap.get("cityCode");
					}
					if (caogaoMap.containsKey("provinceCode")) {
						selectProvinceCode = caogaoMap.get("provinceCode");
					}
					if (caogaoMap.containsKey("provinceName")) {
						selectProvinceName = caogaoMap.get("provinceName");
					}
					if (caogaoMap.containsKey("contactName")) {
						et_name.setText(caogaoMap.get("contactName"));
					}
					if (caogaoMap.containsKey("contactPhone")) {
						et_phone.setText(caogaoMap.get("contactPhone"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void bindListener() {
		tv_shopType.setOnClickListener(this);
		tv_decTime.setOnClickListener(this);
		tv_yusuanMoney.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		img_xieyi.setOnClickListener(this);
		tv_kefu.setOnClickListener(this);
		tv_xieyi.setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			PreferHelper.setSharedParam(ConstantKey.SP_PUB_DECNEED, "");
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				JsonArray picJsonArray = resp.getBody().get("data")
						.getAsJsonArray();

				needUploadPicSize = picPathList.size() - 1;

				int webResultPicSize = picJsonArray.size();

				addpicCount = 0;
				if (!Tool.isEmpty(picJsonArray)
						&& needUploadPicSize == webResultPicSize) {
					for (int i = 0; i < webResultPicSize; i++) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						List<String> oneInfo = new ArrayList<String>();
						oneInfo.add(picJsonArray.get(i).toString());
						map.put("fileUrlJSONArray",
								JsonUtil.listToJsonArray(oneInfo));
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Log.e("hl", "upload pic request " + (i + 1) + "/"
								+ picJsonArray.size() + "==>" + map);
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");

						mainFfb.setFile(new File((String) picPathList.get(i)));

						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			} else {
				ToastUtil.showShortToast("发布成功！");
				setResult(RESULT_OK);
				finish();
			}
			break;
		case reqCodeTwo:
			if (resp.getBody().get("data").toString().length() > 2) {
				DecorateNeedEntity entity = (DecorateNeedEntity) JsonUtil
						.jsonToObj(DecorateNeedEntity.class, resp.getBody()
								.get("data").toString());
				if (entity != null) {
					postsId = entity.getPostsId();
					et_theme.setText(entity.getNeedName());
					tv_shopType.setText(entity.getShopCategoryName());
					shopTypeCode = entity.getShopCategory();
					et_size.setText(entity.getAreaSize() + "");
					tv_decTime.setText(entity.getDecorateTime());
					tv_yusuanMoney.setText(entity.getClientCostBudgetName());
					yusuanCode = entity.getCostBudget();
					tv_city.setText(entity.getCityName());
					selectProvinceCode = entity.getProvinceCode();
					selectProvinceName = entity.getProvinceName();
					selectCityCode = entity.getCityCode();
					selectCityName = entity.getCityName();
					et_name.setText(entity.getContactName());
					et_phone.setText(entity.getContactPhone());
					// 装修需求照片
					oldPic = entity.getPostsAttachmentList();
					if (picPathList.size() > 0) {
						tv_hint.setVisibility(View.GONE);
					}
					picPathList.clear();
					if (!Tool.isEmpty(oldPic)) {
						for (int i = 0, size = oldPic.size(); i < size; i++) {
							picPathList.add(oldPic.get(i).getClientPicUrl());
						}
					}
					picPathList.add("+");
					setSelectPic(false);
				}
			} else {
				ToastUtil.showShortToast("获取详情失败！");
				finish();
			}
			break;
		case reqCodeThree:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				JsonArray picJsonArray = resp.getBody().get("data")
						.getAsJsonArray();
				needUploadPicSize = newAddPathList.size();
				int webResultPicSize = picJsonArray.size();
				Log.e("hl", "upload pic==>" + webResultPicSize + "==="
						+ needUploadPicSize);
				addpicCount = 0;
				if (!Tool.isEmpty(picJsonArray)
						&& needUploadPicSize == webResultPicSize) {
					for (int i = 0; i < webResultPicSize; i++) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						List<String> oneInfo = new ArrayList<String>();
						oneInfo.add(picJsonArray.get(i).toString());
						map.put("fileUrlJSONArray",
								JsonUtil.listToJsonArray(oneInfo));
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Log.e("hl", "upload pic request " + (i + 1) + "/"
								+ picJsonArray.size() + "==>" + map);
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");
						mainFfb.setFile(new File(newAddPathList.get(i)));
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			} else {
				ToastUtil.showShortToast("编辑成功！");
				setResult(RESULT_OK);
				finish();
			}

			break;
		case reqCodeFour:
			if (!Tool.isEmpty(resp.getBody().get("H5"))) {
				H5URL = resp.getBody().get("H5").getAsString();
			}
			if (!Tool.isEmpty(resp.getBody().get("shopCount"))) {
				topTip.setVisibility(View.VISIBLE);
				topTip.setText(resp.getBody().get("shopCount").getAsString());
			} else {
				topTip.setVisibility(View.GONE);
			}
			break;
		default:
			if (requestCode > 2000) {
				addpicCount++;
				if (addpicCount == needUploadPicSize) {// 所有图片都上传则发布成功
					ToastUtil.showShortToast("成功");
					setResult(RESULT_OK);
					finish();
				}
			}
			break;

		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
		case reqCodeThree:
		case reqCodeFour:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						MyNeedDecPubActivity.this, "提示", resp.getHeader()
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
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_shopType:
			if (Tool.isEmpty(typeStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(MyNeedDecPubActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_shopType.setText(str);
							shopTypeCode = typeList.get(itemIndex).getCode()
									+ "";
						}
					}, typeStrs).show();
			break;
		case R.id.tv_decTime:
			new TimeSelectDialog(MyNeedDecPubActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_decTime.setText(time);
						}
					}).show();
			break;
		case R.id.tv_yusuanMoney:
			if (Tool.isEmpty(yusuanMoneyStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(MyNeedDecPubActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_yusuanMoney.setText(str);
							yusuanCode = yusuanMoneyList.get(itemIndex)
									.getCode() + "";
						}
					}, yusuanMoneyStrs).show();
			break;
		case R.id.tv_city:
			new WheelSelectCityDialog(MyNeedDecPubActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_city.setText(cityName);
							selectCityCode = cityCode + "";
							selectProvinceCode = provinceCode + "";
							selectProvinceName = provinceName;
							selectCityName = cityName;
						}
					}).show();
			break;
		case R.id.img_xieyi:
			isAgree = !isAgree;
			img_xieyi
					.setBackgroundResource(isAgree ? R.drawable.zhaozhuangxiuxieyi1
							: R.drawable.zhaozhuangxiuxieyi);
			break;
		case R.id.btn_ok:
			if (Tool.isEmpty(et_theme.getText().toString())) {
				ToastUtil.showShortToast("请填写需求主题");
				return;
			}
			if (Tool.isEmpty(tv_shopType.getText().toString())) {
				ToastUtil.showShortToast("请选择店铺类型");
				return;
			}
			if (Tool.isEmpty(et_size.getText().toString())) {
				ToastUtil.showShortToast("请填写面积");
				return;
			}
			if (Tool.isEmpty(tv_decTime.getText().toString())) {
				ToastUtil.showShortToast("请选择装修时间");
				return;
			}
			if (Tool.isEmpty(tv_yusuanMoney.getText().toString())) {
				ToastUtil.showShortToast("请选择费用预算");
				return;
			}
			if (Tool.isEmpty(tv_city.getText().toString())) {
				ToastUtil.showShortToast("请选择所在城市");
				return;
			}
			if (Tool.isEmpty(et_name.getText().toString())) {
				ToastUtil.showShortToast("请填写您的姓名");
				return;
			}
			if (Tool.isEmpty(et_phone.getText().toString())) {
				ToastUtil.showShortToast("请填写手机号");
				return;
			}
			if (useType == 0) {// 添加
				if (!isAgree) {
					ToastUtil.showShortToast("需要接受<<美库装修宝信息发布规定>>");
					return;
				}
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("needName", et_theme.getText().toString());
				map.put("shopCategory", shopTypeCode);
				map.put("areaSize", et_size.getText().toString());
				map.put("decorateTime", tv_decTime.getText().toString());
				map.put("costBudget", yusuanCode);
				map.put("provinceCode", selectProvinceCode);
				map.put("provinceName", selectProvinceName);
				map.put("cityCode", selectCityCode);
				map.put("cityName", selectCityName);
				map.put("contactPhone", et_phone.getText().toString());
				map.put("contactName", et_name.getText().toString());
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				for (int i = 0, size = picPathList.size() - 1; i < size; i++) {
					UploadImg ui = new UploadImg();
					ui.setFileType("0");
					ui.setFileUrl("");
					ui.setFileThumb(((String) picPathList.get(i)).substring(
							((String) picPathList.get(i)).lastIndexOf(".") + 1,
							((String) picPathList.get(i)).length()));
					imgList.add(ui);
				}
				map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));
				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300041));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.d("hl", "————" + map);
				httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
			} else if (useType == 1) {// 编辑
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", needId);
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getUserId());
				map.put("postsId", postsId);
				map.put("needName", et_theme.getText().toString());
				map.put("shopCategory", shopTypeCode);
				map.put("areaSize", et_size.getText().toString());
				map.put("decorateTime", tv_decTime.getText().toString());
				map.put("costBudget", yusuanCode);
				map.put("provinceCode", selectProvinceCode);
				map.put("provinceName", selectProvinceName);
				map.put("cityCode", selectCityCode);
				map.put("cityName", selectCityName);
				map.put("contactPhone", et_phone.getText().toString());
				map.put("contactName", et_name.getText().toString());
				map.put("postsDeleteIds",
						Tool.checkEmptyAndDeleteEnd(postsDeleteIds));
				map.put("attachmentDeleteIds",
						Tool.checkEmptyAndDeleteEnd(attachmentDeleteIds));
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				// 过滤网页图片
				newAddPathList.clear();
				for (int i = 0, size = picPathList.size() - 1; i < size; i++) {
					if (!Tool.isEmpty(picPathList.get(i))
							&& !((String) picPathList.get(i)).contains("http")) {
						newAddPathList.add((String) picPathList.get(i));
					}
				}
				for (int i = 0, size = newAddPathList.size(); i < size; i++) {
					UploadImg ui = new UploadImg();
					ui.setFileType("0");
					ui.setFileUrl("");
					ui.setFileThumb(newAddPathList.get(i).substring(
							newAddPathList.get(i).lastIndexOf(".") + 1,
							newAddPathList.get(i).length()));
					imgList.add(ui);
				}
				map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));
				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300043));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.d("hl", "————" + map);
				httpPost(reqCodeThree, AppConfig.PUBLICK_DECORATION, req);
			}
			break;
		case R.id.tv_xieyi:
			startActivity(new Intent(MyNeedDecPubActivity.this,
					WebViewActivity.class).putExtra("webUrl", H5URL));
			break;
		case R.id.tv_kefu:
			final CommonDialog commonDialog = new CommonDialog(this, "拨打电话",
					"400-688-6800", "拨打", "取消");
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:4006886800"));
							startActivity(intent);
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
			commonDialog.show();
			break;
		case R.id.goback:
			finishWhenTip();
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
		if (useType == 0) {
			final CommonDialog commonDialog = new CommonDialog(
					MyNeedDecPubActivity.this, "提示", "是否保存当前内容到草稿箱？", "是", "否");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getId()
									+ "");
							if (!Tool.isEmpty(et_theme.getText().toString())) {
								map.put("needName", et_theme.getText()
										.toString());
							}
							if (!Tool.isEmpty(shopTypeCode)) {
								map.put("shopCategory", shopTypeCode);
							}
							if (!Tool.isEmpty(tv_shopType.getText().toString())) {
								map.put("shopCategoryName", tv_shopType
										.getText().toString());
							}
							if (!Tool.isEmpty(et_size.getText().toString())) {
								map.put("areaSize", et_size.getText()
										.toString());
							}
							if (!Tool.isEmpty(tv_decTime.getText().toString())) {
								map.put("decorateTime", tv_decTime.getText()
										.toString());
							}
							if (!Tool.isEmpty(yusuanCode)) {
								map.put("costBudget", yusuanCode);
							}
							if (!Tool.isEmpty(tv_yusuanMoney.getText()
									.toString())) {
								map.put("costBudgetName", tv_yusuanMoney
										.getText().toString());
							}
							if (!Tool.isEmpty(selectProvinceCode)) {
								map.put("provinceCode", selectProvinceCode);
							}
							if (!Tool.isEmpty(selectProvinceName)) {
								map.put("provinceName", selectProvinceName);
							}
							if (!Tool.isEmpty(selectCityCode)) {
								map.put("cityCode", selectCityCode);
							}
							if (!Tool.isEmpty(selectCityName)) {
								map.put("cityName", selectCityName);
							}
							if (!Tool.isEmpty(et_phone.getText().toString())) {
								map.put("contactPhone", et_phone.getText()
										.toString());
							}
							if (!Tool.isEmpty(et_name.getText().toString())) {
								map.put("contactName", et_name.getText()
										.toString());
							}
							PreferHelper.setSharedParam(
									ConstantKey.SP_PUB_DECNEED, map.toString());
							commonDialog.dismiss();
							finish();
						}

						@Override
						public void doCancel() {
							PreferHelper.setSharedParam(
									ConstantKey.SP_PUB_DECNEED, "");
							commonDialog.dismiss();
							finish();
						}
					});
		} else if (useType == 1) {
			final CommonDialog commonDialog = new CommonDialog(
					MyNeedDecPubActivity.this, "提示", "是否退出编辑?", "确定", "取消");
			commonDialog.show();
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
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
		}
	}

	private void setSelectPic(boolean isAdd) {
		gridview_uploadpic.setAdapter(new CommonAdapter<Object>(this,
				R.layout.add_talent_pic_gridview_item, picPathList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final Object imgItem) {
				// 如果大于7个删除第一个图片也就是增加图片按钮
				if (picPathList.size() < 7) {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item,
								R.drawable.addpicture);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												MyNeedDecPubActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 5 - picPathList.size();
										intent.putExtra("MAX_NUM",
												(picNum > 0) ? picNum : 0);// 选择的张数
										startActivityForResult(intent,
												SELECT_COMPANYPICS);
									}
								});

					} else {
						String imagePath = "";
						if (imgItem.getClass().getName()
								.equals(AttachmentListDTO.class.getName())) {
							AttachmentListDTO attachmentListDTO = (AttachmentListDTO) imgItem;
							imagePath = attachmentListDTO.getClientFileUrl();
						} else {
							imagePath = (String) imgItem;
						}
						viewHolder.setImage(R.id.img_item, imagePath, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (useType == 1) {

											for (int i = 0; i < oldPic.size(); i++) {
												if (picPathList
														.get(viewHolder
																.getPosition())
														.equals(oldPic
																.get(i)
																.getClientPicUrl())) {
													postsDeleteIds += oldPic
															.get(i).getId()
															+ ",";
													attachmentDeleteIds += oldPic
															.get(i)
															.getSourceId()
															+ ",";
												}
											}

										}
										picPathList.remove(imgItem);
										setSelectPic(false);
									}
								});

					}
				} else {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.getView(R.id.add_pic_framelayout)
								.setVisibility(View.GONE);

					} else {
						String imagePath = "";
						if (imgItem.getClass().getName()
								.equals(AttachmentListDTO.class.getName())) {
							AttachmentListDTO attachmentListDTO = (AttachmentListDTO) imgItem;
							imagePath = attachmentListDTO.getClientFileUrl();
						} else {
							imagePath = (String) imgItem;
						}
						viewHolder.setImage(R.id.img_item, imagePath, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (useType == 1) {
											for (int i = 0; i < oldPic.size(); i++) {
												if (picPathList
														.get(viewHolder
																.getPosition())
														.equals(oldPic
																.get(i)
																.getClientPicUrl())) {
													postsDeleteIds += oldPic
															.get(i).getId()
															+ ",";
													attachmentDeleteIds += oldPic
															.get(i)
															.getSourceId()
															+ ",";
												}
											}

										}
										picPathList.remove(imgItem);
										setSelectPic(false);
									}
								});

					}
				}
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_COMPANYPICS) { // 选择图片
				if (!Tool.isEmpty(data)) {
					List<String> pics = data
							.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
					List<AttachmentListDTO> mrrckPics = data
							.getParcelableArrayListExtra("Mrrck_Album_Result");
					showProgressDialog("图片压缩中...");
					addpicCount = 0;
					if (pics != null) {// 多选图片返回
						for (int i = 0, size = pics.size(); i < size; i++) {
							new MyTask(SELECT_COMPANYPICS, size).execute(pics
									.get(i));
						}
					} else {// 拍照返回
						String photoPath = data
								.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
						new MyTask(SELECT_COMPANYPICS, 1).execute(photoPath);
					}
				}

			}
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param photoPath
	 */
	private class MyTask extends AsyncTask<String, Integer, String> {

		private int id;
		private int size;

		public MyTask(int id) {
			this.id = id;
		}

		public MyTask(int id, int size) {
			this.id = id;
			this.size = size;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (id) {

			case SELECT_COMPANYPICS:
				addpicCount++;
				picPathList.add(picPathList.size() - 1, result);
				setSelectPic(true);
				if (addpicCount == size) {
					dismissProgressDialog();
				}
				tv_hint.setVisibility(View.GONE);
				break;

			}

			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... arg0) {
			String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
			LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
			return photoPath; 
		}

	}
}
