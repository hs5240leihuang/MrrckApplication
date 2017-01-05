package com.meiku.dev.ui.decoration;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecDetailBean;
import com.meiku.dev.bean.DecorateCaseEntity;
import com.meiku.dev.bean.DecorateCaseSummaryEntity;
import com.meiku.dev.bean.DecorateOrderCityContentEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.ProductDetailInfos;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectCityActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectDialog;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;

/**
 * 发布装修案例（编辑）
 * 
 */
public class PublishCaseActivity extends BaseActivity implements
		OnClickListener {

	/** 首页主图 */
	private ImageView iv_mianPic;
	/** 案例名称 */
	private EditText et_caseName;
	/** 发布城市 */
	private TextView tv_city;
	/** 主案设计 */
	private EditText et_design;
	/** 店铺类型 */
	private TextView tv_type;
	/** 店铺面积 */
	private EditText et_size;
	/** 价格 */
	private TextView et_price;
	/** 店面名称 */
	private EditText et_storName;
	/** 店面地址 */
	private EditText et_storAddress;
	/** 装修起止时间 */
	private TextView tv_startTime, tv_endTime;
	/** 介绍 */
	private EditText et_introduce;
	public String mainPicPath;
	private List<MkDecorateCategory> typeList;
	private String[] typeStrs;
	/** 店铺类别 */
	private int shopType;
	private final int PUBLISHCITY = 2;
	private final int EDIT_HOME = 3;
	private final int EDIT_UPLOADMAINPIC = 4;
	private final int EDIT_ADDDETAIL = 5;
	private final int EDIT_EDITETAIL = 6;

	private boolean isWhole = false;// 是否是全国
	private ArrayList<ProductDetailInfos> detailsInfoList = new ArrayList<ProductDetailInfos>();
	private LinearLayout addDetailLayout;
	private int picUploadedNum;
	private int needUploadPicSize;
	private int sourceId;
	private TextView center_txt_title;
	private LinearLayout layoutAreaGroup;
	private String orderCityContentIds = "";
	private ArrayList<String> canSelectAreaList = new ArrayList<String>();
	// 后台返回的可以选择的城市
	private List<DecorateOrderCityContentEntity> areaList = new ArrayList<DecorateOrderCityContentEntity>();
	private int updateType;// 页面刷新区域1全部，2装修细节详情
	private int flag;// 页面类型，0发布，1编辑
	private String[] yusuanMoneyStrs;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_publishcase;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		iv_mianPic = (ImageView) findViewById(R.id.iv_mianPic);
		et_caseName = (EditText) findViewById(R.id.tv_caseName);
		tv_city = (TextView) findViewById(R.id.tv_city);
		et_design = (EditText) findViewById(R.id.tv_design);
		tv_type = (TextView) findViewById(R.id.tv_type);
		et_size = (EditText) findViewById(R.id.tv_size);
		et_price = (TextView) findViewById(R.id.tv_price);
		et_storName = (EditText) findViewById(R.id.tv_storName);
		et_storAddress = (EditText) findViewById(R.id.tv_storAddress);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_endTime = (TextView) findViewById(R.id.tv_endTime);

		layoutAreaGroup = (LinearLayout) findViewById(R.id.layoutAreaGroup);

		addDetailLayout = (LinearLayout) findViewById(R.id.addDetailLayout);
		addDetailLayout.removeAllViews();
	}

	@Override
	public void initValue() {
		sourceId = getIntent().getIntExtra("sourceId", -1);
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {
			center_txt_title.setText("编辑装修案例");
			GetData(1);
		} else {
			checkIsHasOpenCitys();
			center_txt_title.setText("发布装修案例");
		}
		typeList = MKDataBase.getInstance().getDecorateCategoryList(0, 0);
		if (!Tool.isEmpty(typeList)) {
			int size = typeList.size();
			typeStrs = new String[size];
			for (int i = 0; i < size; i++) {
				typeStrs[i] = typeList.get(i).getName();
			}
		}
		List<MkDecorateCategory> yusuanMoneyList = MKDataBase.getInstance()
				.getDecorateCategoryList(5, 0);
		if (!Tool.isEmpty(yusuanMoneyList)) {
			int size = yusuanMoneyList.size();
			yusuanMoneyStrs = new String[size];
			for (int i = 0; i < size; i++) {
				yusuanMoneyStrs[i] = yusuanMoneyList.get(i).getName();
			}
		}
	}

	@Override
	public void bindListener() {
		iv_mianPic.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_type.setOnClickListener(this);
		et_price.setOnClickListener(this);
		tv_startTime.setOnClickListener(this);
		tv_endTime.setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.addOneInfoLayout).setOnClickListener(this);
	}

	private void checkIsHasOpenCitys() {// 检测是否有开通城市
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300029));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==>" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (Tool.isEmpty(orderCityContentIds)) {
				startActivity(new Intent(PublishCaseActivity.this,
						OpenPermissionActivity.class).putExtra("usetype", 0));
			}
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				JsonArray picJsonArray = resp.getBody().get("data")
						.getAsJsonArray();
				List<String> allPicLocalPathList = new ArrayList<String>();
				allPicLocalPathList.add(mainPicPath);// 主图路径
				for (int i = 0, itemSize = detailsInfoList.size(); i < itemSize; i++) {
					ArrayList<String> detailPicPathList = detailsInfoList
							.get(i).getPics();
					for (int j = 0, PicSize = detailPicPathList.size() - 1; j < PicSize; j++) {
						allPicLocalPathList.add(detailPicPathList.get(j));// 所有细节图路径
					}
				}
				needUploadPicSize = allPicLocalPathList.size();
				int webResultPicSize = picJsonArray.size();
				Log.e("hl", "upload pic==>" + webResultPicSize + "==="
						+ needUploadPicSize);
				picUploadedNum = 0;
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
								+ allPicLocalPathList.size() + "==>" + map);
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");
						mainFfb.setFile(new File(allPicLocalPathList.get(i)));
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			}
			break;
		case reqCodeTwo:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("dataList"))
					&& resp.getBody().get("dataList").toString().length() > 2) {
				layoutAreaGroup.setVisibility(View.VISIBLE);
				areaList = (List<DecorateOrderCityContentEntity>) JsonUtil
						.jsonToList(
								resp.getBody().get("dataList").toString(),
								new TypeToken<List<DecorateOrderCityContentEntity>>() {
								}.getType());
				for (int i = 0, size = areaList.size(); i < size; i++) {
					canSelectAreaList.add(areaList.get(i).getCityCode());
				}
				Log.e("hl", "发布案例--提供的可选择城市数==>" + canSelectAreaList.size());
			} else {// 未开通权限，不显示选择地区
				layoutAreaGroup.setVisibility(View.GONE);
			}
			break;
		case reqCodeThree:
			if ((resp.getBody().get("data") + "").length() > 2) {
				String jsString = resp.getBody().get("data").toString();
				DecorateCaseEntity data = (DecorateCaseEntity) JsonUtil
						.jsonToObj(DecorateCaseEntity.class, jsString);
				XuanRan(data);
			} else {

			}
			break;
		case reqCodeFour:// 删除详情
			GetData(2);
			break;
		case EDIT_HOME:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {// 更换主图片
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				ArrayList<String> path = new ArrayList<String>();
				path.add(resp.getBody().get("data").getAsString());
				map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(path));
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
				FormFileBean formFile = new FormFileBean(new File(mainPicPath),
						"photo.png");
				formFileBeans.add(formFile);
				mapFileBean.put("file", formFileBeans);
				Log.e("wangke", map + "");
				uploadResFiles(EDIT_UPLOADMAINPIC, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			} else {// 无主图替换则为主体编辑成功
				ToastUtil.showShortToast("编辑成功");
				setResult(RESULT_OK);
				finish();
			}
			break;
		case EDIT_UPLOADMAINPIC:
			ToastUtil.showShortToast("编辑成功");
			setResult(RESULT_OK);
			finish();
			break;
		default:
			if (requestCode > 2000) {
				picUploadedNum++;
				if (picUploadedNum == needUploadPicSize) {// 所有图片都上传则发布成功
					ToastUtil.showShortToast("发布成功");
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
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						PublishCaseActivity.this, "提示", resp.getHeader()
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
		case R.id.iv_mianPic:
			startActivityForResult(
					new Intent(this, SelectPictureActivity.class).putExtra(
							"MAX_NUM", 1).putExtra("SELECT_MODE",
							SelectPictureActivity.MODE_SINGLE), reqCodeOne);
			break;
		case R.id.tv_city:
			if (flag == 1) {
				ToastUtil.showShortToast("发布地区不可修改");
				return;
			}
			if (Tool.isEmpty(canSelectAreaList)) {
				ToastUtil.showShortToast("没有可供发布的地区！");
			} else if (canSelectAreaList.size() == 1
					&& "10000".equals(canSelectAreaList.get(0))) {// 只有一个，是选择全国
				startActivityForResult(new Intent(this,
						SelectCityActivity.class).putExtra("selectType",
						ConstantKey.SELECT_AREA_ALL), PUBLISHCITY);
			} else {// 选择提供的城市
				startActivityForResult(
						new Intent(this, SelectCityActivity.class).putExtra(
								"selectType", ConstantKey.SELECT_AREA_HASAERA)
								.putStringArrayListExtra("canSelectcitys",
										canSelectAreaList), PUBLISHCITY);
			}
			break;
		case R.id.tv_type:
			if (Tool.isEmpty(typeStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(PublishCaseActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							tv_type.setText(str);
							shopType = typeList.get(itemIndex).getCode();
						}
					}, typeStrs).show();
			break;
		case R.id.btn_ok:

			if (flag == 0 && Tool.isEmpty(mainPicPath)) {
				ToastUtil.showShortToast("请添加首页广告图");
				return;
			}
			if (Tool.isEmpty(et_caseName.getText().toString())) {
				ToastUtil.showShortToast("请填写案例名称");
				return;
			}
			// if (Tool.isEmpty(tv_city.getText().toString())) {
			// ToastUtil.showShortToast("请选择发布城市");
			// return;
			// }
			if (Tool.isEmpty(et_design.getText().toString())) {
				ToastUtil.showShortToast("请填写主案设计");
				return;
			}
			if (Tool.isEmpty(tv_type.getText().toString())) {
				ToastUtil.showShortToast("请选择店铺类型");
				return;
			}
			if (Tool.isEmpty(et_size.getText().toString())) {
				ToastUtil.showShortToast("请填写店铺面积");
				return;
			}
			if (Tool.isEmpty(et_price.getText().toString())) {
				ToastUtil.showShortToast("请填写价格");
				return;
			}
			if (Tool.isEmpty(tv_startTime.getText().toString())) {
				ToastUtil.showShortToast("请选择装修开始时间");
				return;
			}
			if (Tool.isEmpty(tv_endTime.getText().toString())) {
				ToastUtil.showShortToast("请选择装修结束时间");
				return;
			}
			if (Tool.isEmpty(et_introduce.getText().toString())) {
				ToastUtil.showShortToast("请填写案例概述");
				return;
			}
			ReqBase req = new ReqBase();
			if (flag == 1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("companyId", AppContext.getInstance().getUserInfo()
						.getCompanyEntity().getId());
				map.put("id", sourceId);
				map.put("caseName", et_caseName.getText().toString());
				map.put("caseDesignName", et_design.getText().toString());
				map.put("shopCategory", shopType);
				map.put("shopAreaSize", et_size.getText().toString());
				map.put("casePice", et_price.getText().toString());
				map.put("shopName", et_storName.getText().toString());
				map.put("shopAddress", et_storAddress.getText().toString());
				map.put("decorateProjectStartTime", tv_startTime.getText()
						.toString());
				map.put("decorateProjectEndTime", tv_endTime.getText()
						.toString());
				map.put("summary", et_introduce.getText().toString());
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				UploadImg ui = new UploadImg();
				ui.setFileType("0");
				ui.setFileUrl("");
				if (!Tool.isEmpty(mainPicPath)) {
					ui.setFileThumb(mainPicPath.substring(
							mainPicPath.lastIndexOf(".") + 1,
							mainPicPath.length()));
				} else {
					ui.setFileThumb("");
				}
				imgList.add(ui);
				if (!Tool.isEmpty(mainPicPath)) {
					map.put("fileUrlJSONArray",
							JsonUtil.listToJsonArray(imgList));
				} else {
					map.put("fileUrlJSONArray", JsonUtil
							.listToJsonArray(new ArrayList<UploadImg>()));
				}
				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300031));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.d("wangke", "edit————" + map);
				httpPost(EDIT_HOME, AppConfig.PUBLICK_DECORATION, req);
			} else {
				for (int i = 0, size = detailsInfoList.size(); i < size; i++) {
					if (detailsInfoList.get(i).getPics().size() <= 1) {
						ToastUtil.showShortToast("所有案例细节必须添加图片");
						return;
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("companyId", AppContext.getInstance().getUserInfo()
						.getCompanyEntity().getId());
				map.put("caseName", et_caseName.getText().toString());
				// map.put("provinceCode", selectProvinceCode);
				// map.put("provinceName", selectProvinceName);
				// map.put("cityCode", selectCityCode);
				// map.put("cityName", selectCityName);
				map.put("caseDesignName", et_design.getText().toString());
				map.put("shopCategory", shopType);
				map.put("shopAreaSize", et_size.getText().toString());
				map.put("casePice", et_price.getText().toString());
				map.put("shopName", et_storName.getText().toString());
				map.put("shopAddress", et_storAddress.getText().toString());
				map.put("decorateProjectStartTime", tv_startTime.getText()
						.toString());
				map.put("decorateProjectEndTime", tv_endTime.getText()
						.toString());
				map.put("orderCityContentIds", orderCityContentIds);
				map.put("summary", et_introduce.getText().toString());
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				UploadImg ui = new UploadImg();
				ui.setFileType("0");
				ui.setFileUrl("");
				if (!Tool.isEmpty(mainPicPath)) {
					ui.setFileThumb(mainPicPath.substring(
							mainPicPath.lastIndexOf(".") + 1,
							mainPicPath.length()));
				} else {
					ui.setFileThumb("");
				}
				imgList.add(ui);
				map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));
				List<DecDetailBean> ddbList = new ArrayList<DecDetailBean>();
				for (int i = 0, size = detailsInfoList.size(); i < size; i++) {
					DecDetailBean ddb = new DecDetailBean();
					ddb.setTitle(detailsInfoList.get(i).getTitle());
					ddb.setRemark(detailsInfoList.get(i).getDetail());
					List<UploadImg> detailPicList = new ArrayList<UploadImg>();
					for (int j = 0, picNum = detailsInfoList.get(i).getPics()
							.size() - 1; j < picNum; j++) {
						UploadImg detailU = new UploadImg();
						String path = detailsInfoList.get(i).getPics().get(j);
						detailU.setFileThumb(path.substring(
								path.lastIndexOf(".") + 1, path.length()));
						detailU.setFileType("0");
						detailU.setFileUrl("");
						detailPicList.add(detailU);
					}
					ddb.setFileUrlJSONArray(JsonUtil
							.listToJsonArray(detailPicList));
					ddbList.add(ddb);
				}
				map.put("caseSummary", JsonUtil.listToJsonArray(ddbList));

				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300013));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.d("wangke", "————" + map);
				httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
			}
			break;
		case R.id.addOneInfoLayout:
			if (detailsInfoList.size() >= 3) {
				ToastUtil.showShortToast("最多可添加3个案例细节");
				return;
			}
			if (flag == 1) {
				startActivityForResult(
						new Intent(PublishCaseActivity.this,
								AddOneCaseDetailActivity.class).putExtra(
								"caseId", sourceId).putExtra("usetype", 1),
						EDIT_ADDDETAIL);
			} else {
				startActivityForResult(new Intent(PublishCaseActivity.this,
						AddOneCaseDetailActivity.class), reqCodeTwo);
			}
			break;
		case R.id.tv_startTime:
			new TimeSelectDialog(PublishCaseActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							try {
								if (!Tool.isEmpty(tv_endTime.getText()
										.toString())) {
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd");
									Date topData = sdf.parse(time);
									Date buyData = sdf.parse(tv_endTime
											.getText().toString());
									if (DateTimeUtil.compare(buyData, topData) < 0) {
										ToastUtil
												.showShortToast("开始时间必须在结束时间之前");
										return;
									}
								}

							} catch (ParseException e) {
								e.printStackTrace();
							}
							tv_startTime.setText(time);
						}
					}).show();
			break;
		case R.id.tv_endTime:
			new TimeSelectDialog(PublishCaseActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {

							try {
								if (!Tool.isEmpty(tv_startTime.getText()
										.toString())) {
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd");
									Date topData = sdf.parse(time);
									Date buyData = sdf.parse(tv_startTime
											.getText().toString());
									if (DateTimeUtil.compare(buyData, topData) > 0) {
										ToastUtil
												.showShortToast("结束时间必须开始时间在之后");
										return;
									}
								}

							} catch (ParseException e) {
								e.printStackTrace();
							}
							tv_endTime.setText(time);
						}
					}).show();
			break;
		case R.id.tv_price:
			if (Tool.isEmpty(yusuanMoneyStrs)) {
				ToastUtil.showShortToast("数据库获取数据失败！");
				return;
			}
			new WheelSelectDialog(PublishCaseActivity.this,
					new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							et_price.setText(str);
						}
					}, yusuanMoneyStrs).show();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (pics != null) {// 多选图片返回
					CompressPic(reqCodeOne, pics.get(0));
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqCodeOne, photoPath);
				}
			} else if (requestCode == reqCodeTwo) {
				ProductDetailInfos oneDetail = new ProductDetailInfos();
				oneDetail.setTitle(data.getStringExtra("title"));
				oneDetail.setDetail(data.getStringExtra("detail"));
				oneDetail.setPics(data.getStringArrayListExtra("pics"));
				detailsInfoList.add(oneDetail);
				addOneProductDetailItem(detailsInfoList.size() - 1, oneDetail);
			} else if (requestCode == EDIT_ADDDETAIL
					|| requestCode == EDIT_EDITETAIL) {
				GetData(2);
			} else if (requestCode == PUBLISHCITY) {
				String selectCityCode = data.getStringExtra("cityCode");
				String orderCityNames = data.getStringExtra("cityName");
				tv_city.setText(orderCityNames);
				LogUtil.e("hl", "选择发布城市=>" + orderCityNames + "///"
						+ selectCityCode);
				if (!Tool.isEmpty(selectCityCode)) {
					String[] selectCityCodes = selectCityCode.split(",");
					for (int i = 0, size = selectCityCodes.length; i < size; i++) {
						for (int j = 0, areaSize = areaList.size(); j < areaSize; j++) {
							if (areaList.get(j).getCityCode()
									.equals(selectCityCodes[i])) {
								orderCityContentIds += (areaList.get(j).getId() + ",");
							}
						}
					}
				}
				if (!Tool.isEmpty(orderCityContentIds)
						&& orderCityContentIds.endsWith(",")) {
					orderCityContentIds = orderCityContentIds.substring(0,
							orderCityContentIds.length() - 1);// 去除最后都逗号
				}
				LogUtil.e("hl", "选择发布城市 对应的orderCityContentIds=>"
						+ orderCityContentIds);
			} else {
				LogUtil.d("hl", "defaultrequestCode=" + requestCode);
				if (requestCode >= 1000) {
					List<String> addPics = data
							.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
					if (!Tool.isEmpty(addPics)) {// 多选图片返回
						for (int i = 0; i < addPics.size(); i++) {
							CompressPic(requestCode, addPics.get(i));
						}

					} else {// 拍照返回
						String photoPath = data
								.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
						CompressPic(requestCode, photoPath);
					}
				}
			}
		}
	}

	private void setGridPic2(final MyGridView gridview,
			final ArrayList<String> pisList, final int position) {
		gridview.setAdapter(new CommonAdapter<String>(this,
				R.layout.item_pic_product, pisList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final String imgItem) {
				viewHolder.setImage(R.id.img_item, imgItem, 0);
				viewHolder.getView(R.id.delete_btn).setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 添加一条详情
	 * 
	 * @param position
	 * @param isEdit
	 * @param title
	 * @param detail
	 * @param pics
	 */
	private void addOneProductDetailItem(final int position,
			final ProductDetailInfos detailInfo) {
		View itemView = LayoutInflater.from(PublishCaseActivity.this).inflate(
				R.layout.include_addproductinfo, null, false);
		EditText editTitle = (EditText) itemView.findViewById(R.id.editTitle);
		editTitle.setText(detailInfo.getTitle());
		EditText editDetail = (EditText) itemView.findViewById(R.id.editDetail);
		Button editBtn = (Button) itemView.findViewById(R.id.edit);
		editDetail.setText(detailInfo.getDetail());
		editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(
						new Intent(PublishCaseActivity.this,
								AddOneCaseDetailActivity.class)
								.putExtra("caseId", sourceId)
								.putExtra("detailId",
										detailInfo.getProductDetailId())
								.putExtra("usetype", 2)
								.putExtra("oldData",
										JsonUtil.objToJson(detailInfo)),
						EDIT_EDITETAIL);
			}
		});
		MyGridView gridview_uploadpic = (MyGridView) itemView
				.findViewById(R.id.gridview_uploadpic);
		if (flag == 1) {
			editDetail.setEnabled(false);
			editTitle.setEnabled(false);
			editBtn.setVisibility(View.VISIBLE);
			setGridPic2(gridview_uploadpic, detailInfo.getPics(), position);
		} else {
			editTitle.setHint("点击输入详细介绍");
			editDetail.setHint("点击添加详细说明文字");
			editBtn.setVisibility(View.GONE);
			setGridPic(gridview_uploadpic, detailInfo.getPics(), position);
		}

		Button deleteIcon = (Button) itemView.findViewById(R.id.delete);
		deleteIcon.setVisibility(View.VISIBLE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						PublishCaseActivity.this, "提示", "确定删除本条详情?", "确定", "取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {
							@Override
							public void doConfirm() {
								if (flag == 1) {
									DeleteDetailInfo(detailInfo
											.getProductDetailId());
								} else {
									detailsInfoList.remove(position);
									addDetailLayout.removeAllViews();
									for (int i = 0; i < detailsInfoList.size(); i++) {
										addOneProductDetailItem(i,
												detailsInfoList.get(i));
									}
								}
								deleteTipDialog.dismiss();
							}

							@Override
							public void doCancel() {
								deleteTipDialog.dismiss();
							}
						});
				deleteTipDialog.show();
			}
		});
		editTitle.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				detailsInfoList.get(position).setTitle(editable.toString());
			}
		});
		// 产品详细输入框监听
		editDetail.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				detailsInfoList.get(position).setDetail(editable.toString());
			}
		});
		addDetailLayout.addView(itemView);
	}

	private void setGridPic(final MyGridView gridview,
			final ArrayList<String> pisList, final int position) {
		gridview.setAdapter(new CommonAdapter<String>(this,
				R.layout.item_pic_product, pisList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final String imgItem) {
				// 如果大于8个删除第一个图片也就是增加图片按钮
				if (pisList.size() < 9) {
					if (imgItem == getItem(pisList.size() - 1)) {
						viewHolder.setImage(R.id.img_item, R.drawable.addphoto);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												PublishCaseActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 10 - pisList.size();
										intent.putExtra("MAX_NUM",
												(picNum > 0) ? picNum : 0);// 选择的张数
										startActivityForResult(intent,
												1000 + position);// 标记是哪一行Item的详情添加图片
									}
								});

					} else {
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pisList.remove(imgItem);
										setGridPic(gridview, pisList, position);
									}
								});

					}
				} else {
					if (imgItem == getItem(pisList.size() - 1)) {
						viewHolder.getView(R.id.add_pic_framelayout)
								.setVisibility(View.GONE);
					} else {
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pisList.remove(imgItem);
										setGridPic(gridview, pisList, position);
									}
								});

					}
				}
			}

		});
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param reqcode
	 * 
	 * @param photoPath
	 */
	private void CompressPic(int reqcode, String photoPath) {
		showProgressDialog("图片压缩中...");
		new MyTask(reqcode).execute(photoPath);
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
			case reqCodeOne:
				mainPicPath = result;
				BitmapUtils bitmapUtils = new BitmapUtils(
						PublishCaseActivity.this);
				bitmapUtils.display(iv_mianPic, mainPicPath);
				dismissProgressDialog();
				break;
			default:
				if (id >= 1000) {
					int index = Math.abs(id) % 1000;
					detailsInfoList
							.get(index)
							.getPics()
							.add(detailsInfoList.get(index).getPics().size() - 1,
									result);
					addDetailLayout.removeAllViews();
					for (int i = 0; i < detailsInfoList.size(); i++) {
						addOneProductDetailItem(i, detailsInfoList.get(i));
					}
					dismissProgressDialog();
				}
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

	/**
	 * 数据请求,updateType页面刷新区域1全部，2装修细节详情
	 * 
	 * @param updateType
	 */
	public void GetData(int updateType) {
		this.updateType = updateType;
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("id", sourceId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300025));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_DECORATION, req);
	}

	// 删除一详情
	public void DeleteDetailInfo(int id) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300032));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeFour, AppConfig.PUBLICK_DECORATION, req);
	}

	public void XuanRan(DecorateCaseEntity data) {
		if (updateType == 1) {
			BitmapUtils bitmapUtils = new BitmapUtils(PublishCaseActivity.this);
			bitmapUtils
					.display(iv_mianPic, data.getClientCaseImgFileUrlThumb());
			shopType = data.getShopCategory();
			et_caseName.setText(data.getCaseName());
			tv_city.setText(data.getShowCityName());
			et_design.setText(data.getCaseDesignName());
			tv_type.setText(data.getShopCategoryName());
			et_size.setText(data.getShopAreaSize());
			et_price.setText(data.getCasePice());
			et_storName.setText(data.getShopName());
			et_storAddress.setText(data.getShopAddress());
			tv_startTime.setText(data.getDecorateProjectStartTime());
			tv_endTime.setText(data.getDecorateProjectEndTime());
			et_introduce.setText(data.getSummary());
		}
		if (updateType >= 1) {
			detailsInfoList.clear();
			List<DecorateCaseSummaryEntity> detailInfoList = data.getDcsList();
			for (int i = 0, size = detailInfoList.size(); i < size; i++) {
				ProductDetailInfos info = new ProductDetailInfos();
				info.setTitle(detailInfoList.get(i).getTitle());
				info.setDetail(detailInfoList.get(i).getRemark());
				info.setProductDetailId(detailInfoList.get(i).getId());
				ArrayList<String> pics = new ArrayList<String>();
				ArrayList<String> picIds = new ArrayList<String>();
				for (int j = 0, picSize = detailInfoList.get(i).getDaList()
						.size(); j < picSize; j++) {
					pics.add(detailInfoList.get(i).getDaList().get(j)
							.getClientFileUrl());
					picIds.add(detailInfoList.get(i).getDaList().get(j).getId()
							+ "");
				}
				info.setPics(pics);
				info.setIds(picIds);
				detailsInfoList.add(info);
			}
			addDetailLayout.removeAllViews();
			for (int i = 0; i < detailsInfoList.size(); i++) {
				addOneProductDetailItem(i, detailsInfoList.get(i));
			}
		}
	}
}
