package com.meiku.dev.ui.recruit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyDesignersEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
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
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;

/**
 * 添加设计师信息
 */
public class AddOneDesignerInfoActivity extends BaseActivity {

	private EditText et_name, et_position, et_workyears, et_introduce;
	public String headPicPath;
	private ImageView iv_addpic;
	private int useType;// 0发布添加，1编辑添加，2编辑修改
	private CompanyDesignersEntity entity;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_addonedesigner;
	}

	@Override
	public void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_position = (EditText) findViewById(R.id.et_position);
		et_workyears = (EditText) findViewById(R.id.et_workyears);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		iv_addpic = (ImageView) findViewById(R.id.iv_addpic);
	}

	@Override
	public void initValue() {
		useType = getIntent().getIntExtra("useType", 0);
		String oldData = getIntent().getStringExtra("oldData");
		if (useType == 2) {
			CompanyDesignersEntity oldInfo = (CompanyDesignersEntity) JsonUtil
					.jsonToObj(CompanyDesignersEntity.class, oldData);
			et_name.setText(oldInfo.getName());
			et_position.setText(oldInfo.getJobName());
			et_workyears.setText(oldInfo.getWorkYears());
			et_introduce.setText(oldInfo.getPersonalNote());
			if (!Tool.isEmpty(oldInfo.getClientHeadUrl())) {
				BitmapUtils bitmapUtils = new BitmapUtils(
						AddOneDesignerInfoActivity.this);
				bitmapUtils.display(iv_addpic, oldInfo.getClientHeadUrl());
			}
			entity = oldInfo;
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.iv_addpic).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AddOneDesignerInfoActivity.this,
						SelectPictureActivity.class);
				intent.putExtra("SELECT_MODE",
						SelectPictureActivity.MODE_SINGLE);// 选择模式
				intent.putExtra("MAX_NUM", 1);// 选择的张数
				startActivityForResult(intent, reqCodeOne);
			}
		});
		findViewById(R.id.right_txt_title).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (Tool.isEmpty(et_name.getText().toString())) {
							ToastUtil.showShortToast("请填写姓名");
							return;
						}
						// if (Tool.isEmpty(et_position.getText().toString())) {
						// ToastUtil.showShortToast("请填写职位");
						// return;
						// }
						// if (Tool.isEmpty(et_workyears.getText().toString()))
						// {
						// ToastUtil.showShortToast("请填写工作年限");
						// return;
						// }
						// if (Tool.isEmpty(et_introduce.getText().toString()))
						// {
						// ToastUtil.showShortToast("请填写详情描述");
						// return;
						// }
						// if (Tool.isEmpty(headPicPath)) {
						// ToastUtil.showShortToast("请上传设计师照片");
						// return;
						// }
						if (useType == 0) {
							Intent intent = new Intent();
							intent.putExtra("name", et_name.getText()
									.toString());
							intent.putExtra("position", et_position.getText()
									.toString());
							intent.putExtra("workyears", et_workyears.getText()
									.toString());
							intent.putExtra("detail", et_introduce.getText()
									.toString());
							intent.putExtra("headPicPath", headPicPath);
							setResult(RESULT_OK, intent);
							finish();
						} else if (useType == 1) {
							ReqBase reqBase = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("type", 1);
							map.put("companyId", AppContext.getInstance()
									.getUserInfo().getCompanyEntity().getId());
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getId());
							CompanyDesignersEntity entity = new CompanyDesignersEntity();
							entity.setName(et_name.getText().toString());
							entity.setJobName(et_position.getText().toString());
							entity.setWorkYears(et_workyears.getText()
									.toString());
							entity.setPersonalNote(et_introduce.getText()
									.toString());
							if (Tool.isEmpty(headPicPath)) {
								entity.setFileUrlJSONArray(JsonUtil
										.listToJsonArray(new ArrayList<String>()));
							} else {
								List<UploadImg> imgList = new ArrayList<UploadImg>();
								UploadImg ui = new UploadImg();
								ui.setFileType("0");
								ui.setFileUrl("");
								String picPath = headPicPath;
								if (!Tool.isEmpty(picPath)) {
									ui.setFileThumb(picPath.substring(
											picPath.lastIndexOf(".") + 1,
											picPath.length()));
								} else {
									ui.setFileThumb("");
								}
								imgList.add(ui);
								entity.setFileUrlJSONArray(JsonUtil
										.listToJsonArray(imgList));
							}
							map.put("designers",
									JsonUtil.Entity2JsonObj(entity));
							reqBase.setHeader(new ReqHead(
									AppConfig.BUSINESS_COMPLANYINFO_RT_90067));
							reqBase.setBody(JsonUtil.String2Object(JsonUtil
									.hashMapToJson(map)));
							httpPost(reqCodeOne,
									AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
						} else if (useType == 2) {
							ReqBase reqBase = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("type", 2);
							map.put("companyId", AppContext.getInstance()
									.getUserInfo().getCompanyEntity().getId());
							map.put("userId", AppContext.getInstance()
									.getUserInfo().getId());
							entity.setName(et_name.getText().toString());
							entity.setJobName(et_position.getText().toString());
							entity.setWorkYears(et_workyears.getText()
									.toString());
							entity.setPersonalNote(et_introduce.getText()
									.toString());
							if (Tool.isEmpty(headPicPath)) {
								entity.setFileUrlJSONArray(JsonUtil
										.listToJsonArray(new ArrayList<String>()));
							} else if (!headPicPath.contains("http")) {
								List<UploadImg> imgList = new ArrayList<UploadImg>();
								UploadImg ui = new UploadImg();
								ui.setFileType("0");
								ui.setFileUrl("");
								String picPath = headPicPath;
								if (!Tool.isEmpty(picPath)) {
									ui.setFileThumb(picPath.substring(
											picPath.lastIndexOf(".") + 1,
											picPath.length()));
								} else {
									ui.setFileThumb("");
								}
								imgList.add(ui);
								entity.setFileUrlJSONArray(JsonUtil
										.listToJsonArray(imgList));
							}
							map.put("designers",
									JsonUtil.Entity2JsonObj(entity));
							reqBase.setHeader(new ReqHead(
									AppConfig.BUSINESS_COMPLANYINFO_RT_90067));
							reqBase.setBody(JsonUtil.String2Object(JsonUtil
									.hashMapToJson(map)));
							httpPost(reqCodeOne,
									AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
						}
					}
				});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==>" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (Tool.isEmpty(headPicPath)) {
				setResult(RESULT_OK);
				finish();
			} else {
				if (!Tool.isEmpty(resp.getBody().get("uploadFileEntityList"))) {
					JsonArray picJsonArray = resp.getBody()
							.get("uploadFileEntityList").getAsJsonArray();
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileUrlJSONArray", picJsonArray);
					req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
					req.setBody(JsonUtil.Map2JsonObj(map));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
					FormFileBean mainFfb = new FormFileBean();
					mainFfb.setFileName("photo_designer.png");
					mainFfb.setFile(new File(headPicPath));
					LogUtil.d("hl", "上传设计师图片__" + headPicPath);
					details_FileBeans.add(mainFfb);
					mapFileBean.put("file", details_FileBeans);
					uploadResFiles(reqCodeTwo, AppConfig.PUBLICK_UPLOAD,
							mapFileBean, req, true);
				}
			}
			break;
		case reqCodeTwo:
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeThree:
			if (Tool.isEmpty(headPicPath) || headPicPath.contains("http")) {
				setResult(RESULT_OK);
				finish();
			} else {
				if (!Tool.isEmpty(resp.getBody().get("uploadFileEntityList"))) {
					JsonArray picJsonArray = resp.getBody()
							.get("uploadFileEntityList").getAsJsonArray();
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileUrlJSONArray", picJsonArray);
					req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
					req.setBody(JsonUtil.Map2JsonObj(map));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
					FormFileBean mainFfb = new FormFileBean();
					mainFfb.setFileName("photo_designer.png");
					mainFfb.setFile(new File(headPicPath));
					LogUtil.d("hl", "上传设计师图片__" + headPicPath);
					details_FileBeans.add(mainFfb);
					mapFileBean.put("file", details_FileBeans);
					uploadResFiles(reqCodeTwo, AppConfig.PUBLICK_UPLOAD,
							mapFileBean, req, true);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
		case reqCodeThree:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						AddOneDesignerInfoActivity.this, "提示", resp.getHeader()
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeOne) {
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				showProgressDialog("图片压缩中...");
				if (pics != null) {// 多选图片返回
					new MyTask(reqCodeOne).execute(pics.get(0));
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					new MyTask(reqCodeOne).execute(photoPath);
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
				headPicPath = result;
				BitmapUtils bitmapUtils = new BitmapUtils(
						AddOneDesignerInfoActivity.this);
				bitmapUtils.display(iv_addpic, headPicPath);
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

}
