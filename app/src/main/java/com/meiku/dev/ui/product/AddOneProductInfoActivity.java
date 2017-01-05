package com.meiku.dev.ui.product;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ProductDetailEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UserAttachmentEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/**
 * 添加产品说明
 * 
 */
public class AddOneProductInfoActivity extends BaseActivity {

	private EditText editTitle, editDetail;
	private MyGridView gridview_uploadpic;
	private ArrayList<String> picPathList = new ArrayList<String>();// 上传公司图片--选择的图片路径
	private CommonDialog commonDialog;
	/** 2编辑修改详情,1编辑添加详情，0发布添加详情 */
	private int flag;
	// 添加详情使用
	private int productId;
	private int sortNo;
	// 编辑详情使用
	private ArrayList<UserAttachmentEntity> doEdit_pics = new ArrayList<UserAttachmentEntity>();
	private int doEdit_productDetailId;
	private int doEdit_productId;
	private List<UserAttachmentEntity> oldAttachmentList;
	private String attachmentIds = "";
	private TextView tv_topTitle;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_addoneinfo;
	}

	@Override
	public void initView() {
		tv_topTitle = (TextView) findViewById(R.id.center_txt_title);
		editTitle = (EditText) findViewById(R.id.editTitle);
		editDetail = (EditText) findViewById(R.id.editDetail);
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String titleStr = editTitle.getText().toString().trim();
				if (Tool.isEmpty(titleStr)) {
					ToastUtil.showShortToast("标题不能为空");
					return;
				}
				String detailStr = editDetail.getText().toString().trim();
				if (Tool.isEmpty(detailStr) && picPathList.size() == 1) {
					ToastUtil.showShortToast("详细说明或者图片必填一个（可都填）");
					return;
				}
				if (flag == 2) {
					EditOneDetailData();
				} else if (flag == 1) {
					AddOneDetailData();
				} else {
					Intent intent = new Intent();
					intent.putExtra("title", titleStr);
					intent.putExtra("detail", detailStr);
					intent.putStringArrayListExtra("pics", picPathList);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		findViewById(R.id.goback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishWhenTip();
					}
				});
	}

	@Override
	public void initValue() {
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {
			picPathList.add("+");// 添加加号显示
			productId = getIntent().getIntExtra("productId", -1);
			sortNo = getIntent().getIntExtra("sortNo", 0);
			setSelectPic(true);
		} else if (flag == 2) {
			tv_topTitle.setText("编辑产品说明");
			doEdit_productId = getIntent().getIntExtra("productId", -1);
			doEdit_productDetailId = getIntent().getIntExtra("productDetailId",
					-1);
			sortNo = getIntent().getIntExtra("sortNo", 0);
			getOneDetailData();
		} else {
			picPathList.add("+");// 添加加号显示
			setSelectPic(true);
		}
		initTipDialog();
	}

	private void setSelectPic(boolean isAdd) {
		gridview_uploadpic.setAdapter(new CommonAdapter<String>(this,
				R.layout.item_pic_product, picPathList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final String imgItem) {
				// 如果大于8个删除第一个图片也就是增加图片按钮
				if (picPathList.size() < 9) {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item, R.drawable.addphoto);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												AddOneProductInfoActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 9 - picPathList.size();
										intent.putExtra("MAX_NUM",
												(picNum > 0) ? picNum : 0);// 选择的张数
										startActivityForResult(intent,
												reqCodeOne);
									}
								});

					} else {
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (viewHolder.getPosition() < doEdit_pics
												.size()) {
											attachmentIds += doEdit_pics.get(
													viewHolder.getPosition())
													.getAttachmentId()
													+ ",";
											doEdit_pics.remove(viewHolder
													.getPosition());
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
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (viewHolder.getPosition() < doEdit_pics
												.size()) {
											attachmentIds += doEdit_pics.get(
													viewHolder.getPosition())
													.getAttachmentId()
													+ ",";
											doEdit_pics.remove(viewHolder
													.getPosition());
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
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) { // 选择图片
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				showProgressDialog("图片压缩中...");
				if (pics != null) {// 多选图片返回
					for (int i = 0, size = pics.size(); i < size; i++) {
						new MyTask(reqCodeOne, size).execute(pics.get(i));
					}
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					if (!Tool.isEmpty(photoPath)) {
						new MyTask(reqCodeOne, 1).execute(photoPath);
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
			case reqCodeOne:
				picPathList.add(picPathList.size() - 1, result);
				setSelectPic(false);
				dismissProgressDialog();
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

	private void initTipDialog() {
		String dialogContent = "";
		if (flag == 2) {
			dialogContent = "是否放弃修改?";
		} else {
			dialogContent = "是否放弃本次添加?";
		}
		commonDialog = new CommonDialog(AddOneProductInfoActivity.this, "提示",
				dialogContent, "确定", "取消");
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		if (commonDialog != null && !commonDialog.isShowing()) {
			commonDialog.show();
		} else {
			finish();
		}
	}

	/**
	 * 编辑--修改一条详情数据
	 */
	private void EditOneDetailData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productId", doEdit_productId);
		map.put("title", editTitle.getText().toString());
		map.put("content", editDetail.getText().toString());
		map.put("attachmentNum", picPathList.size() - 1);// 去除加号
		map.put("productDetailId", doEdit_productDetailId);
		if (!Tool.isEmpty(attachmentIds)) {
			attachmentIds = attachmentIds.substring(0,
					attachmentIds.length() - 1);
		}
		map.put("attachmentIds", attachmentIds);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_EDITINFO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		LogUtil.d("hl", "修改详情请求__" + map);
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
		LogUtil.d("hl", picPathList.size() - 1 - doEdit_pics.size() + "修改"
				+ picPathList.size() + "____" + doEdit_pics.size());
		// doEdit_pics为原始编辑前/后的图片，他的size之后的则为新添加的图片
		for (int i = 0, size = picPathList.size() - 1 - doEdit_pics.size(); i < size; i++) {
			FormFileBean ffb = new FormFileBean();
			ffb.setFileName("detailPhoto" + (sortNo) + "_"
					+ (i + 1 + picPathList.size()) + ".png");
			LogUtil.d("hl", "发布图片产品：" + "detailPhoto" + (sortNo) + "_"
					+ (i + 1 + picPathList.size()) + ".png");
			ffb.setFile(new File((String) picPathList.get(i
					+ doEdit_pics.size())));
			details_FileBeans.add(ffb);
		}
		mapFileBean.put("photo", details_FileBeans);
		uploadFiles(reqCodeTwo, AppConfig.PRODUCT_REQUEST_MAPPING, mapFileBean,
				req, true);
	}

	/**
	 * 编辑--添加一条详情数据
	 */
	private void AddOneDetailData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productId", productId);
		map.put("title", editTitle.getText().toString());
		map.put("content", editDetail.getText().toString());
		map.put("attachmentNum", picPathList.size() - 1);// 去除加号
		map.put("sortNo", sortNo);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ADDINFO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		LogUtil.d("hl", "添加详情请求__" + map);
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
		for (int i = 0, size = picPathList.size() - 1; i < size; i++) {
			FormFileBean ffb = new FormFileBean();
			ffb.setFileName("detailPhoto" + (sortNo) + "_" + (i + 1) + ".png");
			LogUtil.d("hl", "发布图片产品：" + "detailPhoto" + (sortNo) + "_"
					+ (i + 1) + ".png");
			ffb.setFile(new File((String) picPathList.get(i)));
			details_FileBeans.add(ffb);
		}
		mapFileBean.put("photo", details_FileBeans);
		uploadFiles(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, mapFileBean,
				req, true);
	}

	/**
	 * 获取单条详情数据
	 */
	private void getOneDetailData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("productId", doEdit_productId);
		map.put("productDetailId", doEdit_productDetailId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ONEINFO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.PRODUCT_REQUEST_MAPPING, req, true);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hl", "详情__" + reqBase.getBody());
		switch (requestCode) {
		case reqCodeOne:// 添加返回
			if ((reqBase.getBody().get("productDetail") + "").length() > 2) {
				ProductDetailEntity productDetailEntity = (ProductDetailEntity) JsonUtil
						.jsonToObj(ProductDetailEntity.class, reqBase.getBody()
								.get("productDetail").toString());
				Intent intent = new Intent();
				Bundle b = new Bundle();
				intent.putExtra("title", productDetailEntity.getTitle());
				intent.putExtra("detail", productDetailEntity.getContent());
				intent.putExtra("productDetailId", productDetailEntity.getId());
				intent.putExtra("sortNo", productDetailEntity.getSortNo());
				ArrayList<String> picList = new ArrayList<String>();
				for (int i = 0, size = productDetailEntity.getAttachmentList()
						.size(); i < size; i++) {
					picList.add(productDetailEntity.getAttachmentList().get(i)
							.getClientFileUrl());
				}
				intent.putStringArrayListExtra("pics", picList);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				ToastUtil.showShortToast("添加失败");
			}
			break;
		case reqCodeTwo:
			setResult(RESULT_OK, new Intent());
			finish();
			break;
		case reqCodeThree:// 获取单条详情
			if ((reqBase.getBody().get("productDetail") + "").length() > 2) {
				ProductDetailEntity productDetailEntity = (ProductDetailEntity) JsonUtil
						.jsonToObj(ProductDetailEntity.class, reqBase.getBody()
								.get("productDetail").toString());
				editTitle.setText(productDetailEntity.getTitle());
				editDetail.setText(productDetailEntity.getContent());
				oldAttachmentList = productDetailEntity.getAttachmentList();
				for (int i = 0, size = oldAttachmentList.size(); i < size; i++) {
					doEdit_pics.add(oldAttachmentList.get(i));
					picPathList
							.add(oldAttachmentList.get(i).getClientFileUrl());
				}
				if (!Tool.isEmpty(doEdit_pics)) {
					picPathList.add("+");// 图片最后添加加号显示
				} else {
					picPathList.add("+");// 添加加号显示
				}
				setSelectPic(true);
			}
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("添加失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("修改失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("获取详情失败");
			break;
		}
	}

}
