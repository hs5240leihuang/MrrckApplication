package com.meiku.dev.ui.recruit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.CompanyCustomContentEntity;
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
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/**
 * 公司信息添加自定义详情
 * 
 */
public class AddOneCompDetailActivity extends BaseActivity {

	private EditText editTitle;
	private EditText editDetail;
	private MyGridView gridview_uploadpic;
	private CommonDialog commonDialog;
	private ArrayList<String> picPathList = new ArrayList<String>();// 上传图片--选择的图片路径
	private int useType;// 0发布添加，1编辑添加，2编辑修改
	private ArrayList<String> oldPicPaths = new ArrayList<String>();
	private int uploadNum;
	private ArrayList<String> newAddPathList = new ArrayList<String>();// 新增加的图片list
	private ArrayList<String> oldPicIds = new ArrayList<String>();
	protected String delAttachmentIds = "";
	private CompanyCustomContentEntity entity;
	private int needUploadPicSize;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_addonecasedetail;
	}

	@Override
	public void initView() {
		((TextView) findViewById(R.id.center_txt_title)).setText("添加自定义项");
		editTitle = (EditText) findViewById(R.id.editTitle);
		editTitle.setHint("点击输入标题");
		editDetail = (EditText) findViewById(R.id.editDetail);
		editDetail.setHint("点击输入详细介绍");
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String titleStr = editTitle.getText().toString().trim();
				String detailStr = editDetail.getText().toString().trim();
				if (Tool.isEmpty(titleStr)) {
					ToastUtil.showShortToast("标题不能为空");
					return;
				}
				// if (picPathList.size() == 1) {
				// ToastUtil.showShortToast("请添加图片");
				// return;
				// }
				if (useType == 0) {
					Intent intent = new Intent();
					intent.putExtra("title", titleStr);
					intent.putExtra("detail", detailStr);
					intent.putStringArrayListExtra("pics", picPathList);
					setResult(RESULT_OK, intent);
					finish();
				} else if (useType == 1) {
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("type", 1);
					map.put("companyId", AppContext.getInstance().getUserInfo()
							.getCompanyEntity().getId());
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					CompanyCustomContentEntity entity = new CompanyCustomContentEntity();
					entity.setTitle(titleStr);
					entity.setContent(detailStr);
					if (picPathList.size() <= 1) {
						entity.setFileUrlJSONArray(JsonUtil
								.listToJsonArray(new ArrayList<String>()));
					} else {
						List<UploadImg> imgList = new ArrayList<UploadImg>();
						for (int i = 0; i < picPathList.size() - 1; i++) {
							UploadImg ui = new UploadImg();
							ui.setFileType("0");
							ui.setFileUrl("");
							String picPath = picPathList.get(i);
							if (!Tool.isEmpty(picPath)) {
								ui.setFileThumb(picPath.substring(
										picPath.lastIndexOf(".") + 1,
										picPath.length()));
							} else {
								ui.setFileThumb("");
							}
							imgList.add(ui);
						}
						entity.setFileUrlJSONArray(JsonUtil
								.listToJsonArray(imgList));
					}
					map.put("companyCustomContent",
							JsonUtil.Entity2JsonObj(entity));
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_COMPLANYINFO_RT_90066));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING,
							reqBase);
				} else if (useType == 2) {
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("type", 2);
					map.put("companyId", AppContext.getInstance().getUserInfo()
							.getCompanyEntity().getId());
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("delAttachmentIds",
							Tool.checkEmptyAndDeleteEnd(delAttachmentIds));
					entity.setTitle(titleStr);
					entity.setContent(detailStr);
					newAddPathList.clear();
					List<UploadImg> imgList = new ArrayList<UploadImg>();
					for (int i = 0, size = picPathList.size() - 1; i < size; i++) {
						if (!Tool.isEmpty(picPathList.get(i))
								&& !picPathList.get(i).contains("http")) {
							newAddPathList.add(picPathList.get(i));
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
					entity.setFileUrlJSONArray(JsonUtil
							.listToJsonArray(imgList));
					map.put("companyCustomContent",
							JsonUtil.Entity2JsonObj(entity));
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_COMPLANYINFO_RT_90066));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));
					httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING,
							reqBase);
				}
			}
		});
		findViewById(R.id.left_res_title).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishWhenTip();
					}
				});
		commonDialog = new CommonDialog(AddOneCompDetailActivity.this, "提示",
				"是否放弃操作并推出？", "确定", "取消");
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
												AddOneCompDetailActivity.this,
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
										if (useType == 2
												&& picPathList.get(
														viewHolder
																.getPosition())
														.contains("http")) {

											for (int i = 0; i < oldPicPaths
													.size(); i++) {
												if (picPathList.get(
														viewHolder
																.getPosition())
														.equals(oldPicPaths
																.get(i))) {
													delAttachmentIds += oldPicIds
															.get(i) + ",";
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
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (useType == 2
												&& picPathList.get(
														viewHolder
																.getPosition())
														.contains("http")) {

											for (int i = 0; i < oldPicPaths
													.size(); i++) {
												if (picPathList.get(
														viewHolder
																.getPosition())
														.equals(oldPicPaths
																.get(i))) {
													delAttachmentIds += oldPicIds
															.get(i) + ",";
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

	@Override
	public void initValue() {
		useType = getIntent().getIntExtra("useType", 0);
		String oldData = getIntent().getStringExtra("oldData");
		if (useType == 2) {
			if (!Tool.isEmpty(oldData)) {
				CompanyCustomContentEntity oldInfo = (CompanyCustomContentEntity) JsonUtil
						.jsonToObj(CompanyCustomContentEntity.class, oldData);
				entity = oldInfo;
				editTitle.setText(oldInfo.getTitle());
				editDetail.setText(oldInfo.getContent());
				oldPicPaths = oldInfo.getPics();
				picPathList.addAll(oldPicPaths);
				oldPicIds = oldInfo.getIds();
			}
		}
		picPathList.add("+");// 添加加号显示
		setSelectPic(true);
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==>" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (picPathList.size() <= 1) {
				setResult(RESULT_OK);
				finish();
			} else {
				if (!Tool.isEmpty(resp.getBody().get("uploadFileEntityList"))) {
					JsonArray picJsonArray = resp.getBody()
							.get("uploadFileEntityList").getAsJsonArray();
					if (useType == 2) {
						needUploadPicSize = newAddPathList.size();
					} else {
						needUploadPicSize = picPathList.size() - 1;
					}
					for (int i = 0; i < picJsonArray.size(); i++) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						List<String> oneInfo = new ArrayList<String>();
						oneInfo.add(picJsonArray.get(i).toString());
						map.put("fileUrlJSONArray",
								JsonUtil.listToJsonArray(oneInfo));
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");
						if (useType == 2) {
							mainFfb.setFile(new File(newAddPathList.get(i)));
							LogUtil.d("hl", "上传自定义项图片__" + i
									+ newAddPathList.get(i).toString());
						} else {
							mainFfb.setFile(new File(picPathList.get(i)));
							LogUtil.d("hl",
									"上传自定义项图片__" + i
											+ picPathList.get(i).toString());
						}
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			}
			break;
		case reqCodeTwo:
			if (Tool.isEmpty(newAddPathList)) {
				setResult(RESULT_OK);
				finish();
			} else {
				if (!Tool.isEmpty(resp.getBody().get("uploadFileEntityList"))) {
					JsonArray picJsonArray = resp.getBody()
							.get("uploadFileEntityList").getAsJsonArray();
					needUploadPicSize = newAddPathList.size();
					for (int i = 0; i < picJsonArray.size(); i++) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						List<String> oneInfo = new ArrayList<String>();
						oneInfo.add(picJsonArray.get(i).toString());
						map.put("fileUrlJSONArray",
								JsonUtil.listToJsonArray(oneInfo));
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");
						if (useType == 2) {
							mainFfb.setFile(new File(newAddPathList.get(i)));
							LogUtil.d("hl", "上传自定义项图片__" + i
									+ newAddPathList.get(i).toString());
						} else {
							mainFfb.setFile(new File(picPathList.get(i)));
							LogUtil.d("hl",
									"上传自定义项图片__" + i
											+ picPathList.get(i).toString());
						}
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			}
			break;
		default:
			if (requestCode > 2000) {
				uploadNum++;
				if (uploadNum == needUploadPicSize) {
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
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						AddOneCompDetailActivity.this, "提示", resp.getHeader()
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
}
