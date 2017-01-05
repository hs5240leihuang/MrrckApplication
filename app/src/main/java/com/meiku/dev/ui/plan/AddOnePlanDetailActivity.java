package com.meiku.dev.ui.plan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ProductDetailInfos;
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
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加一条案例详情
 * 
 */
public class AddOnePlanDetailActivity extends BaseActivity {

	private EditText editTitle;
	private EditText editDetail;
	private MyGridView gridview_uploadpic;
	private CommonDialog commonDialog;
	private ArrayList<String> picPathList = new ArrayList<String>();// 上传图片--选择的图片路径
	private int useType;// 0发布添加，1编辑添加，2编辑修改
	private int needUploadPicSize;
	private int picUploadedNum;
	private int caseId;
	protected String delAttachmentIds = "";
	private ArrayList<String> oldPicIds = new ArrayList<String>();
	private ArrayList<String> oldPicPaths = new ArrayList<String>();
	private ArrayList<String> newAddPathList = new ArrayList<String>();// 新增加的图片list
	private int detailId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_addonecasedetail;
	}

	@Override
	public void initView() {
		editTitle = (EditText) findViewById(R.id.editTitle);
		editDetail = (EditText) findViewById(R.id.editDetail);
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
				if (picPathList.size() == 1) {
					ToastUtil.showShortToast("请设置案例细节图片");
					return;
				}
				if (useType == 0) {
					Intent intent = new Intent();
					intent.putExtra("title", titleStr);
					intent.putExtra("detail", detailStr);
					intent.putStringArrayListExtra("pics", picPathList);
					setResult(RESULT_OK, intent);
					finish();
				} else if (useType == 1) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getUserId());
					map.put("id", caseId);
					map.put("title", titleStr);
					map.put("remark", detailStr);
					List<UploadImg> imgList = new ArrayList<UploadImg>();
					for (int i = 0, size = picPathList.size() - 1; i < size; i++) {
						UploadImg ui = new UploadImg();
						ui.setFileType("0");
						ui.setFileUrl("");
						ui.setFileThumb(picPathList.get(i).substring(
								picPathList.get(i).lastIndexOf(".") + 1,
								picPathList.get(i).length()));
						imgList.add(ui);
					}
					map.put("fileUrlJSONArray",
							JsonUtil.listToJsonArray(imgList));
					req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300035));
					req.setBody(JsonUtil.Map2JsonObj(map));
					LogUtil.d("hl", "————" + map);
					httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
				} else if (useType == 2) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getUserId());
					map.put("id", detailId);
					map.put("title", titleStr);
					map.put("remark", detailStr);
					map.put("delAttachmentIds",
							Tool.checkEmptyAndDeleteEnd(delAttachmentIds));
					List<UploadImg> imgList = new ArrayList<UploadImg>();
					// 过滤网页图片
					newAddPathList.clear();
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
					map.put("fileUrlJSONArray",
							JsonUtil.listToJsonArray(imgList));
					req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300033));
					req.setBody(JsonUtil.Map2JsonObj(map));
					LogUtil.d("hl", "————" + map);
					httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
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
		commonDialog = new CommonDialog(AddOnePlanDetailActivity.this, "提示",
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
												AddOnePlanDetailActivity.this,
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
		useType = getIntent().getIntExtra("usetype", 0);
		caseId = getIntent().getIntExtra("caseId", 0);
		if (useType == 2) {
			((TextView) findViewById(R.id.center_txt_title)).setText("编辑案例细节");
			String oldData = getIntent().getStringExtra("oldData");
			detailId = getIntent().getIntExtra("detailId", 0);
			if (!Tool.isEmpty(oldData)) {
				ProductDetailInfos oldInfo = (ProductDetailInfos) JsonUtil
						.jsonToObj(ProductDetailInfos.class, oldData);
				editTitle.setText(oldInfo.getTitle());
				editDetail.setText(oldInfo.getDetail());
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
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				JsonArray picJsonArray = resp.getBody().get("data")
						.getAsJsonArray();
				if (useType == 2) {
					needUploadPicSize = newAddPathList.size();
				} else {
					needUploadPicSize = picPathList.size() - 1;
				}
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
								+ picJsonArray.size() + "==>" + map);
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");
						if (useType == 2) {
							mainFfb.setFile(new File(newAddPathList.get(i)));
						} else {
							mainFfb.setFile(new File(picPathList.get(i)));
						}
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			} else {
				setResult(RESULT_OK);
				finish();
			}
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
