package com.meiku.dev.ui.product;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ProductDetailEntity;
import com.meiku.dev.bean.ProductDetailInfos;
import com.meiku.dev.bean.ProductInfoEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/**
 * 编辑产品
 * 
 */
public class EditProductActivity extends BaseActivity implements
		OnClickListener {

	private ImageView img_neiceng, img_waiceng;
	private TextView tv_kinds, tv_provinces, tv_starttime, tv_month;
	private EditText et_productname, et_peopleName, et_phoneNum;
	private final int ADDINFO = 10;
	private final int EDITINFO = 11;
	private ArrayList<ProductDetailInfos> detailsInfoList = new ArrayList<ProductDetailInfos>();
	public String productMainImage = "";
	private CommonDialog exitTipDialog;
	private int categoryId;
	private LinearLayout addDetailLayout;
	private int productId;
	private ProductInfoEntity productInfo;
	private String oldMainImagePath = "";
	private int deletePosition;
	/** 1编辑，2添加招商省份 */
	private int flag;
	private int sortNo = 1;
	private String provinceNames;
	private String provinceCodes;
	private TextView tv_topTitle;
	private LinearLayout addOneInfoLayout;
	private String edit_ProvinceNames;
	private String edit_ProvinceCodes;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_publishproduct;
	}

	@Override
	public void initView() {
		flag = getIntent().getIntExtra("FLAG", 1);
		tv_topTitle = (TextView) findViewById(R.id.center_txt_title);
		img_neiceng = (ImageView) findViewById(R.id.img_neiceng);
		img_waiceng = (ImageView) findViewById(R.id.img_waiceng);
		img_neiceng.setImageDrawable(ContextCompat.getDrawable(
				EditProductActivity.this, R.drawable.add_photo));
		et_productname = (EditText) findViewById(R.id.et_productname);
		tv_kinds = (TextView) findViewById(R.id.tv_kinds);
		tv_provinces = (TextView) findViewById(R.id.tv_provinces);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		tv_month = (TextView) findViewById(R.id.tv_month);
		et_peopleName = (EditText) findViewById(R.id.et_peopleName);
		et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
		addDetailLayout = (LinearLayout) findViewById(R.id.addDetailLayout);
		addDetailLayout.removeAllViews();
		addOneInfoLayout = (LinearLayout) findViewById(R.id.addOneInfoLayout);
		if (flag == 2) {
			findViewById(R.id.iv_kinds_rightarrow)
					.setVisibility(View.INVISIBLE);
			et_productname.setKeyListener(null);
			et_peopleName.setKeyListener(null);
			et_phoneNum.setKeyListener(null);
			tv_topTitle.setText("新增招商省份");
			addOneInfoLayout.setVisibility(View.GONE);
		} else if (flag == 1) {
			tv_topTitle.setText("编辑产品");
		}
		initTipDialog();
	}

	/**
	 * 添加一条详情
	 * 
	 * @param position
	 * @param title
	 * @param detail
	 * @param pics
	 */
	private void addOneProductDetailItem(final int position,
			ProductDetailInfos detailInfo) {
		View itemView = LayoutInflater.from(EditProductActivity.this).inflate(
				R.layout.include_addproductinfo, null, false);
		EditText editTitle = (EditText) itemView.findViewById(R.id.editTitle);
		editTitle.setKeyListener(null);
		editTitle.setText(detailInfo.getTitle());
		editTitle.setHint("");
		EditText editDetail = (EditText) itemView.findViewById(R.id.editDetail);
		TextView tv_Detail = (TextView) itemView.findViewById(R.id.tv_Detail);
		tv_Detail.setText(detailInfo.getDetail());
		editDetail.setVisibility(View.GONE);
		tv_Detail.setVisibility(View.VISIBLE);
		MyGridView gridview_uploadpic = (MyGridView) itemView
				.findViewById(R.id.gridview_uploadpic);
		setGridPic(gridview_uploadpic, detailInfo.getPics(), position);
		Button deleteIcon = (Button) itemView.findViewById(R.id.delete);
		Button editIcon = (Button) itemView.findViewById(R.id.edit);
		deleteIcon.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
		editIcon.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						EditProductActivity.this, "提示", "确定删除本条详情?", "确定", "取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {

							@Override
							public void doConfirm() {
								deletePosition = position;
								deleteTipDialog.dismiss();
								getDeleteData(detailsInfoList.get(position)
										.getProductDetailId());
							}

							@Override
							public void doCancel() {
								deleteTipDialog.dismiss();
							}
						});
				deleteTipDialog.show();
			}
		});
		editIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent_info = new Intent(EditProductActivity.this,
						AddOneProductInfoActivity.class);
				intent_info.putExtra("flag", 2);
				intent_info.putExtra("sortNo", detailsInfoList.get(position)
						.getSortNo());
				intent_info.putExtra("productId", productId);
				intent_info.putExtra("productDetailId",
						detailsInfoList.get(position).getProductDetailId());
				startActivityForResult(intent_info, EDITINFO);
			}
		});
		addDetailLayout.addView(itemView);
	}

	/**
	 * 显示底部图文详情
	 */
	private void setDetailShow() {
		addDetailLayout.removeAllViews();
		for (int i = 0; i < detailsInfoList.size(); i++) {
			addOneProductDetailItem(i, detailsInfoList.get(i));
		}
	}

	private void setGridPic(final MyGridView gridview,
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

	@Override
	public void initValue() {
		productId = getIntent().getIntExtra("productId", -1);
		getProductData();
	}

	@Override
	public void bindListener() {
		img_neiceng.setOnClickListener(this);
		img_waiceng.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.layout_kinds).setOnClickListener(this);
		findViewById(R.id.layout_provinces).setOnClickListener(this);
		findViewById(R.id.layout_starttime).setOnClickListener(this);
		findViewById(R.id.layout_month).setOnClickListener(this);
		addOneInfoLayout.setOnClickListener(this);
		et_peopleName.setOnClickListener(this);
		et_phoneNum.setOnClickListener(this);
		et_productname.setOnClickListener(this);
		findViewById(R.id.btnOK).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			finishWhenTip();
			break;
		case R.id.img_waiceng:
			if (flag == 2) {
				return;// 添加招商省份，产品图片不可编辑
			}
			if (img_waiceng.getDrawable() == null) {

				Intent intent1 = new Intent(EditProductActivity.this,
						SelectPictureActivity.class);
				intent1.putExtra("SELECT_MODE",
						SelectPictureActivity.MODE_SINGLE);// 选择模式
				intent1.putExtra("MAX_NUM", 1);// 选择的张数
				startActivityForResult(intent1, reqCodeOne);
			} else {
				final CommonDialog commonDialog = new CommonDialog(
						EditProductActivity.this, "提示", "是否删除该照片", "确认", "取消");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						img_neiceng.setImageDrawable(ContextCompat.getDrawable(
								EditProductActivity.this, R.drawable.add_photo));
						img_waiceng.setImageDrawable(null);
						oldMainImagePath = "";
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
			}
			break;
		case R.id.layout_kinds:
			if (flag == 1) {
				Intent intent = new Intent(EditProductActivity.this,
						PublishDurationActivity.class);
				intent.putExtra("FLAG", "SELECT_TYPE");
				startActivityForResult(intent, reqCodeTwo);
			} else if (flag == 2) {
				ToastUtil.showShortToast("产品类型不能修改！");
			}
			break;
		case R.id.layout_provinces:
			if (flag == 1) {
				ToastUtil.showShortToast("招商省份不能修改！");
			} else if (flag == 2) {
				Intent intent_p = new Intent(EditProductActivity.this,
						ZhaoShangProvincesActivity.class);
				startActivityForResult(intent_p, reqCodeFour);
			}
			break;
		case R.id.layout_starttime:
			ToastUtil.showShortToast("发布时间不能修改！");
			break;
		case R.id.layout_month:
			ToastUtil.showShortToast("发布时长不能修改！");
			break;
		case R.id.addOneInfoLayout:
			Intent intent_info = new Intent(EditProductActivity.this,
					AddOneProductInfoActivity.class);
			intent_info.putExtra("flag", 1);
			intent_info.putExtra("productId", productId);
			if (!Tool.isEmpty(detailsInfoList)) {
				sortNo = detailsInfoList.get(detailsInfoList.size() - 1)
						.getSortNo() + 1;
			}
			intent_info.putExtra("sortNo", sortNo);
			startActivityForResult(intent_info, ADDINFO);
			break;
		case R.id.btnOK:
			if (flag == 1) {
				if (checkIsReadyForPublish()) {
					editProduct();
				}
			} else if (flag == 2) {
				if (Tool.isEmpty(tv_provinces.getText().toString())) {
					ToastUtil.showShortToast("招商省份未选择");
					return;
				}
				AddZhaoshangProvinces();
			}

			break;
		case R.id.et_productname:
			if (flag == 2) {
				ToastUtil.showShortToast("产品名称不能修改！");
			}
			break;
		case R.id.et_peopleName:
			if (flag == 2) {
				ToastUtil.showShortToast("产品联系人不能修改！");
			}
			break;
		case R.id.et_phoneNum:
			if (flag == 2) {
				ToastUtil.showShortToast("产品联系电话不能修改！");
			}
			break;
		}
	}

	/**
	 * 是否达到可发布的条件
	 * 
	 * @return
	 */
	private boolean checkIsReadyForPublish() {

		if (Tool.isEmpty(oldMainImagePath) && Tool.isEmpty(productMainImage)) {
			ToastUtil.showShortToast("产品图片未设定");
			return false;
		}
		if (Tool.isEmpty(et_productname.getText().toString())) {
			ToastUtil.showShortToast("产品名称未填写");
			return false;
		}
		if (Tool.isEmpty(tv_kinds.getText().toString())) {
			ToastUtil.showShortToast("产品类型未选择");
			return false;
		}
		if (Tool.isEmpty(tv_provinces.getText().toString())) {
			ToastUtil.showShortToast("招商省份未选择");
			return false;
		}
		// if (Tool.isEmpty(tv_starttime.getText().toString())) {
		// ToastUtil.showShortToast("发布时间未选择");
		// return false;
		// }
		// if (Tool.isEmpty(tv_month.getText().toString())) {
		// ToastUtil.showShortToast("发布时长未选择");
		// return false;
		// }
		if (Tool.isEmpty(et_peopleName.getText().toString())) {
			ToastUtil.showShortToast("产品联系人未填写");
			return false;
		}
		if (Tool.isEmpty(et_phoneNum.getText().toString())) {
			ToastUtil.showShortToast("产品联系电话未填写");
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			LogUtil.d("hl", "requestCode=" + requestCode);
			switch (requestCode) {
			case reqCodeOne:
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqCodeOne, pictrue.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqCodeOne, photoPath);
				}
				break;
			case reqCodeTwo:
				tv_kinds.setText(data.getStringExtra("value"));
				categoryId = data.getIntExtra("id", -1);
				break;
			case ADDINFO:
				ProductDetailInfos oneDetail = new ProductDetailInfos();
				oneDetail.setTitle(data.getStringExtra("title"));
				oneDetail.setDetail(data.getStringExtra("detail"));
				oneDetail.setPics(data.getStringArrayListExtra("pics"));
				oneDetail.setSortNo(data.getIntExtra("sortNo", sortNo));// 添加第几条详情
				oneDetail.setProductDetailId(data.getIntExtra(
						"productDetailId", 0));
				detailsInfoList.add(oneDetail);
				addOneProductDetailItem(detailsInfoList.size() - 1, oneDetail);
				break;
			case reqCodeFour:
				provinceNames = data.getStringExtra("allProvinces");
				provinceCodes = data.getStringExtra("allCityCode");
				tv_provinces.setText(provinceNames);
				break;
			case EDITINFO:
				getProductData();
				break;
			default:
				break;
			}

		}
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
		new MyAsyncTask(reqcode).execute(photoPath);
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {

		private int reqcode;

		public MyAsyncTask(int reqcode) {
			this.reqcode = reqcode;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (reqcode) {
			case reqCodeOne:
				BitmapUtils bitmapUtils = new BitmapUtils(
						EditProductActivity.this);
				bitmapUtils.display(img_neiceng, result);
				dismissProgressDialog();
				img_waiceng.setImageDrawable(ContextCompat.getDrawable(
						EditProductActivity.this, R.drawable.yinshi_jianhao));
				productMainImage = result;
				break;
			default:
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

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "结果__" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("productInfo") + "").length() > 2) {
				productInfo = (ProductInfoEntity) JsonUtil.jsonToObj(
						ProductInfoEntity.class,
						resp.getBody().get("productInfo").toString());
				if (!Tool.isEmpty(productInfo)) {
					BitmapUtils bitmapUtils = new BitmapUtils(
							EditProductActivity.this);
					oldMainImagePath = productInfo.getClientPosterMain();
					bitmapUtils.display(img_waiceng, oldMainImagePath);
					et_productname.setText(productInfo.getProductName());
					tv_kinds.setText(productInfo.getCategoryName());
					categoryId = productInfo.getCategoryId();
					if (flag == 1) {
						edit_ProvinceNames = productInfo.getProvinceNames();
						edit_ProvinceCodes = productInfo.getProvinceCodes();
						tv_provinces.setText(edit_ProvinceNames);
					}
					tv_starttime.setText(Tool.isEmpty(productInfo
							.getBuyStartTime()) ? "" : productInfo
							.getBuyStartTime().substring(0, 10));
					tv_month.setText(productInfo.getBuyMonths() + "个月");
					et_peopleName.setText(productInfo.getContactName());
					et_phoneNum.setText(productInfo.getContactPhone());
					List<ProductDetailEntity> productDetailList = productInfo
							.getProductDetailList();
					detailsInfoList.clear();
					for (int i = 0, size = productDetailList.size(); i < size; i++) {
						ProductDetailInfos info = new ProductDetailInfos();
						info.setTitle(productDetailList.get(i).getTitle());
						info.setDetail(productDetailList.get(i).getContent());
						ArrayList<String> pics = new ArrayList<String>();
						for (int j = 0, picSize = productDetailList.get(i)
								.getAttachmentList().size(); j < picSize; j++) {
							pics.add(productDetailList.get(i)
									.getAttachmentList().get(j)
									.getClientFileUrl());
						}
						info.setPics(pics);
						info.setProductDetailId(productDetailList.get(i)
								.getId());
						info.setSortNo(productDetailList.get(i).getSortNo());
						detailsInfoList.add(info);
					}
					setDetailShow();
				}
			} else {
				ToastUtil.showShortToast("获取产品数据失败");
			}
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("产品信息更新成功！");
			LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
					.getInstance(EditProductActivity.this);
			localBroadcastManager.sendBroadcast(new Intent(
					BroadCastAction.ACTION_MYPRODUCT));
			finish();
			break;
		case reqCodeThree:
			detailsInfoList.remove(deletePosition);
			setDetailShow();
			break;
		case reqCodeFour:
			if ((resp.getBody().get("productInfo") + "").length() > 2) {
				ProductInfoEntity newProductInfo = (ProductInfoEntity) JsonUtil
						.jsonToObj(ProductInfoEntity.class,
								resp.getBody().get("productInfo").toString());
				LocalBroadcastManager localBroadcastManager2 = LocalBroadcastManager
						.getInstance(EditProductActivity.this);
				localBroadcastManager2.sendBroadcast(new Intent(
						BroadCastAction.ACTION_MYPRODUCT));
				final Integer newproductId = newProductInfo.getId();
				final String newProvinceNames = newProductInfo
						.getProvinceNames();
				final String newProvinceCodes = newProductInfo
						.getProvinceCodes();
				Intent intent_pay = new Intent(
						EditProductActivity.this,
						PayProductActivity.class);
				intent_pay.putExtra("productId", newproductId);
				intent_pay.putExtra("productName",
						et_productname.getText().toString());
				intent_pay.putExtra("provinceNames",
						newProvinceNames);
				intent_pay.putExtra("provinceCodes",
						newProvinceCodes);
				startActivityForResult(intent_pay, reqCodeThree);
				finish();//
//				final CommonDialog tipDialog = new CommonDialog(
//						EditProductActivity.this, "提示",
//						"新增省份需要300元/月/省，点击确定进入支付页面？", "确定", "取消");
//				tipDialog
//						.setClicklistener(new CommonDialog.ClickListenerInterface() {
//							@Override
//							public void doConfirm() {
//								Intent intent_pay = new Intent(
//										EditProductActivity.this,
//										PayProductActivity.class);
//								intent_pay.putExtra("productId", newproductId);
//								intent_pay.putExtra("productName",
//										et_productname.getText().toString());
//								intent_pay.putExtra("provinceNames",
//										newProvinceNames);
//								intent_pay.putExtra("provinceCodes",
//										newProvinceCodes);
//								startActivityForResult(intent_pay, reqCodeThree);
//								finish();//
//
//							}
//
//							@Override
//							public void doCancel() {
//								tipDialog.dismiss();
//								ToastUtil
//										.showShortToast("产品未购买未生效，可在   我的产品-我的发布  中查看！");
//								finish();
//							}
//						});
//				tipDialog.show();
			} else {
				ToastUtil.showShortToast("修改失败！");
			}
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取产品数据失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("上传产品数据失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("删除该条详情失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("修改失败！");
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

	private void initTipDialog() {
		exitTipDialog = new CommonDialog(EditProductActivity.this, "提示",
				"是否放弃操作?", "确定", "取消");
		exitTipDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						finish();

					}

					@Override
					public void doCancel() {
						exitTipDialog.dismiss();
					}
				});
	}

	private void finishWhenTip() {
		if (exitTipDialog != null && !exitTipDialog.isShowing()) {
			exitTipDialog.show();
		} else {
			finish();
		}
	}

	/**
	 * 获取产品详情数据
	 */
	private void getProductData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productId", productId);
		LogUtil.d("hl", "请求产品详情" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_INFO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, req, true);
	}

	/**
	 * 修改基本产品数据
	 */
	private void editProduct() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productName", et_productname.getText().toString());
		map.put("categoryId", categoryId);
		map.put("remark", "");
		map.put("productId", productId);
		map.put("contactName", et_peopleName.getText().toString());
		map.put("contactPhone", et_phoneNum.getText().toString());
		LogUtil.d("hl", "" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_EDIT));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(productMainImage)) {
			// 产品图片
			List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
			logo_FileBeans.add(new FormFileBean(new File(productMainImage),
					"productMainPhoto.png"));
			mapFileBean.put("mainPhoto", logo_FileBeans);
		}
		uploadFiles(reqCodeTwo, AppConfig.PRODUCT_REQUEST_MAPPING, mapFileBean,
				req, true);
	}

	/**
	 * 删除一条详情数据
	 */
	private void getDeleteData(int productDetailId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productId", productId);
		map.put("productDetailId", productDetailId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_DELETEINFO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.PRODUCT_REQUEST_MAPPING, req, true);
	}

	/**
	 * 新增招商省份接口
	 */
	private void AddZhaoshangProvinces() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productId", productId);
		int companyId;
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			companyId = AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId();
		} else {
			companyId = -1;
		}
		map.put("companyId", companyId);
		map.put("provinceCodes", provinceCodes);
		map.put("provinceNames", provinceNames);
		LogUtil.d("hl", "" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_ADDPROVINCES));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeFour, AppConfig.PRODUCT_REQUEST_MAPPING, req, true);
	}
}
