package com.meiku.dev.ui.product;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.meiku.dev.bean.ProductDetail;
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
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的产品--发布产品
 * 
 */
public class PublishProductActivity extends BaseActivity implements
		OnClickListener {

	private ImageView img_neiceng, img_waiceng;
	private TextView tv_kinds, tv_provinces, tv_starttime, tv_month;
	private EditText et_productname, et_peopleName, et_phoneNum;
	private final int ADDINFO = 10;
	private final int PAY_PRODUCT = 11;
	private ArrayList<ProductDetailInfos> detailsInfoList = new ArrayList<ProductDetailInfos>();
	public String productMainImage = "";

	private CommonDialog exitTipDialog;
	private int categoryId = -1;
	private String provinceNames;
	private String provinceCodes;
	private int companyId;
	private int monthSize;
	private LinearLayout addDetailLayout;

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
		return R.layout.activity_publishproduct;
	}

	@Override
	public void initView() {
		img_neiceng = (ImageView) findViewById(R.id.img_neiceng);
		img_waiceng = (ImageView) findViewById(R.id.img_waiceng);
		img_neiceng.setBackgroundResource(R.drawable.addphoto);
		et_productname = (EditText) findViewById(R.id.et_productname);
		tv_kinds = (TextView) findViewById(R.id.tv_kinds);
		tv_provinces = (TextView) findViewById(R.id.tv_provinces);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		tv_month = (TextView) findViewById(R.id.tv_month);
		et_peopleName = (EditText) findViewById(R.id.et_peopleName);
		et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
		addDetailLayout = (LinearLayout) findViewById(R.id.addDetailLayout);
		addDetailLayout.removeAllViews();
		initTipDialog();
		addFirstEmpptyItemData();
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
		View itemView = LayoutInflater.from(PublishProductActivity.this)
				.inflate(R.layout.include_addproductinfo, null, false);
		EditText editTitle = (EditText) itemView.findViewById(R.id.editTitle);
		editTitle.setText(detailInfo.getTitle());
		EditText editDetail = (EditText) itemView.findViewById(R.id.editDetail);
		editDetail.setText(detailInfo.getDetail());
		MyGridView gridview_uploadpic = (MyGridView) itemView
				.findViewById(R.id.gridview_uploadpic);
		setGridPic(gridview_uploadpic, detailInfo.getPics(), position);
		Button deleteIcon = (Button) itemView.findViewById(R.id.delete);
		deleteIcon.setVisibility(View.VISIBLE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						PublishProductActivity.this, "提示", "确定删除本条详情?", "确定",
						"取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {
							@Override
							public void doConfirm() {
								deleteTipDialog.dismiss();
								detailsInfoList.remove(position);
								addDetailLayout.removeAllViews();
								for (int i = 0; i < detailsInfoList.size(); i++) {
									addOneProductDetailItem(i,
											detailsInfoList.get(i));
								}
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

	/**
	 * 默认显示第一个空白详情
	 */
	private void addFirstEmpptyItemData() {
		ProductDetailInfos oneDetail = new ProductDetailInfos();
		oneDetail.setTitle("");
		oneDetail.setDetail("");
		ArrayList<String> firstItem_pics = new ArrayList<String>();
		firstItem_pics.add("+");
		oneDetail.setPics(firstItem_pics);
		detailsInfoList.add(oneDetail);
		addOneProductDetailItem(0, oneDetail);
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
												PublishProductActivity.this,
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

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

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
		findViewById(R.id.addOneInfoLayout).setOnClickListener(this);
		findViewById(R.id.btnOK).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback:
			finishWhenTip();
			break;
		case R.id.img_waiceng:
			if (img_waiceng.getDrawable() == null) {
				Intent intent1 = new Intent(PublishProductActivity.this,
						SelectPictureActivity.class);
				intent1.putExtra("SELECT_MODE",
						SelectPictureActivity.MODE_SINGLE);// 选择模式
				intent1.putExtra("MAX_NUM", 1);// 选择的张数
				startActivityForResult(intent1, reqCodeOne);
			} else {
				final CommonDialog commonDialog = new CommonDialog(
						PublishProductActivity.this, "提示", "是否删除该照片", "确认",
						"取消");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						img_neiceng.setBackgroundResource(R.drawable.addphoto);
						img_waiceng.setImageDrawable(null);
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
			Intent intent = new Intent(PublishProductActivity.this,
					PublishDurationActivity.class);
			intent.putExtra("FLAG", "SELECT_TYPE");
			startActivityForResult(intent, reqCodeTwo);
			break;
		case R.id.layout_provinces:
			Intent intent_p = new Intent(PublishProductActivity.this,
					ZhaoShangProvincesActivity.class);
			intent_p.putExtra("OLD_SELECT", provinceCodes);
			startActivityForResult(intent_p, reqCodeFour);
			break;
		case R.id.layout_starttime:
			Calendar calendar = Calendar.getInstance();
			int Systemyear = calendar.get(Calendar.YEAR);
			int SystemMonth = calendar.get(Calendar.MONTH);
			int SystemDay = calendar.get(Calendar.DAY_OF_MONTH);
			new TimeSelectDialog(PublishProductActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_starttime.setText(time);
						}
					}, Systemyear, SystemMonth + 1, SystemDay).show();
			break;
		case R.id.layout_month:
			Intent intent_m = new Intent(PublishProductActivity.this,
					PublishDurationActivity.class);
			intent_m.putExtra("FLAG", "SELECT_MONTH");
			startActivityForResult(intent_m, reqCodeThree);
			break;
		case R.id.addOneInfoLayout:
			Intent intent_info = new Intent(PublishProductActivity.this,
					AddOneProductInfoActivity.class);
			startActivityForResult(intent_info, ADDINFO);
			break;
		case R.id.btnOK:
			if (checkIsReadyForPublish()) {
				doPublish();
			}
			break;
		}
	}

	private void doPublish() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productName", et_productname.getText().toString());
		map.put("categoryId", categoryId);
		map.put("provinceCodes", provinceCodes);
		map.put("provinceNames", provinceNames);
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			companyId = AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId();
		} else {
			companyId = -1;
		}
		map.put("companyId", companyId);
		map.put("remark", "");
		// map.put("buyStartTime", tv_starttime.getText().toString());
		// map.put("buyMonths", monthSize);
		map.put("contactName", et_peopleName.getText().toString());
		map.put("contactPhone", et_phoneNum.getText().toString());
		ArrayList<ProductDetail> productDetailList = new ArrayList<ProductDetail>();
		for (int i = 0, itemSize = detailsInfoList.size(); i < itemSize; i++) {
			if (Tool.isEmpty(detailsInfoList.get(i).getTitle())
					&& Tool.isEmpty(detailsInfoList.get(i).getDetail())
					&& detailsInfoList.get(i).getPics().size() == 1) {// 某一item为空白数据则不提交
				continue;
			}
			ProductDetail productDetail = new ProductDetail();
			productDetail.setTitle(detailsInfoList.get(i).getTitle());
			productDetail.setContent(detailsInfoList.get(i).getDetail());
			productDetail.setAttachmentNum(detailsInfoList.get(i).getPics()
					.size() - 1);
			productDetail.setSortNo(i + 1);
			productDetailList.add(productDetail);
			LogUtil.d("hl", "详情title__" + productDetail.getTitle()
					+ productDetail.getContent());
		}
		map.put("productDetailList", productDetailList);
		LogUtil.d("hl", "发布产品__" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_PUBLIC));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));

		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		// 产品图片
		List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
		logo_FileBeans.add(new FormFileBean(new File(productMainImage),
				"productMainPhoto.png"));
		mapFileBean.put("mainPhoto", logo_FileBeans);
		// 详情图片
		List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
		for (int i = 0, itemSize = detailsInfoList.size(); i < itemSize; i++) {
			ArrayList<String> picPathList = detailsInfoList.get(i).getPics();
			for (int j = 0, PicSize = picPathList.size(); j < PicSize - 1; j++) {
				String imgPathString = picPathList.get(j);
				FormFileBean ffb = new FormFileBean();
				ffb.setFileName("detailPhoto" + (i + 1) + "_" + (j + 1)
						+ ".png");
				LogUtil.d("hl", "发布图片产品：" + "detailPhoto" + (i + 1) + "_"
						+ (j + 1) + ".png");
				ffb.setFile(new File((String) imgPathString));
				details_FileBeans.add(ffb);
			}
		}

		mapFileBean.put("photo", details_FileBeans);
		uploadFiles(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, mapFileBean,
				reqBase, true);
	}

	/**
	 * 是否达到可发布的条件
	 * 
	 * @return
	 */
	private boolean checkIsReadyForPublish() {
		if (Tool.isEmpty(productMainImage)) {
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
		if (Tool.isEmpty(tv_provinces.getText().toString())
				|| Tool.isEmpty(provinceCodes)) {
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
		for (int i = 0, size = detailsInfoList.size(); i < size; i++) {
			boolean isTitleEmpty = Tool.isEmpty(detailsInfoList.get(i)
					.getTitle());
			boolean isContentEmpty = Tool.isEmpty(detailsInfoList.get(i)
					.getDetail());
			boolean isPicsEmpty = detailsInfoList.get(i).getPics().size() == 1;
			if (!isTitleEmpty && (isContentEmpty && isPicsEmpty)) {
				ToastUtil.showLongToast("每个产品说明的标题或者图片都必须填一项（可都填）");
				return false;
			}
			if (isTitleEmpty && (!isContentEmpty || !isPicsEmpty)) {
				ToastUtil.showLongToast("检查所有详细说明的标题是否填写");
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
			case reqCodeThree:
				tv_month.setText(data.getStringExtra("value"));
				monthSize = data.getIntExtra("index", -1) + 1;// 下标是从0开始，要加1才是月数
				break;
			case reqCodeFour:
				provinceNames = data.getStringExtra("allProvinces");
				provinceCodes = data.getStringExtra("allCityCode");
				tv_provinces.setText(provinceNames);
				break;
			case ADDINFO:
				ProductDetailInfos oneDetail = new ProductDetailInfos();
				oneDetail.setTitle(data.getStringExtra("title"));
				oneDetail.setDetail(data.getStringExtra("detail"));
				oneDetail.setPics(data.getStringArrayListExtra("pics"));
				detailsInfoList.add(oneDetail);
				addOneProductDetailItem(detailsInfoList.size() - 1, oneDetail);
				break;
			default:
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
						PublishProductActivity.this);
				bitmapUtils.display(img_neiceng, result);
				dismissProgressDialog();
				img_waiceng.setBackgroundResource(R.drawable.yinshi_jianhao);
				productMainImage = result;
				break;
			default:
				if (reqcode >= 1000) {
					int index = Math.abs(reqcode) % 1000;
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

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hl", "发布结果__" + reqBase.getBody());
		if ((reqBase.getBody().get("productInfo") + "").length() > 2) {
			ProductInfoEntity newProductInfo = (ProductInfoEntity) JsonUtil
					.jsonToObj(ProductInfoEntity.class,
							reqBase.getBody().get("productInfo").toString());
			final Integer newproductId = newProductInfo.getId();
			final String newProvinceNames = newProductInfo.getProvinceNames();
			final String newProvinceCodes = newProductInfo.getProvinceCodes();
			final String newproductName = newProductInfo.getProductName();
			final CommonDialog tipDialog = new CommonDialog(
					PublishProductActivity.this, "提示",
					"发布此项产品需要购买，点击确定进入支付页面？", "确定", "取消");
			tipDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							Intent intent_pay = new Intent(
									PublishProductActivity.this,
									PayProductActivity.class);
							intent_pay.putExtra("productId", newproductId);
							intent_pay.putExtra("productName", newproductName);
							intent_pay.putExtra("provinceNames",
									newProvinceNames);
							intent_pay.putExtra("provinceCodes",
									newProvinceCodes);
							startActivityForResult(intent_pay, PAY_PRODUCT);
							finish();//

						}

						@Override
						public void doCancel() {
							tipDialog.dismiss();
							ToastUtil
									.showShortToast("产品未购买未生效，可在   我的产品-我的发布  中查看！");
							finish();
						}
					});
			tipDialog.show();
			LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
					.getInstance(PublishProductActivity.this);
			localBroadcastManager.sendBroadcast(new Intent(
					BroadCastAction.ACTION_MYPRODUCT));
		} else {
			ToastUtil.showShortToast("发布失败！");
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("发布失败！");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void initTipDialog() {
		exitTipDialog = new CommonDialog(PublishProductActivity.this, "提示",
				"是否放弃发布?", "确定", "取消");
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

}
