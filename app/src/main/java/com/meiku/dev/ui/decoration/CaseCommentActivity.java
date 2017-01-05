package com.meiku.dev.ui.decoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
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
 * 案例评论
 * 
 */
public class CaseCommentActivity extends BaseActivity {

	private MyGridView gridview_uploadpic;
	private CommonDialog commonDialog;
	private ArrayList<String> picPathList = new ArrayList<String>();// 上传图片--选择的图片路径
	private TextView starNum;
	private RatingBar ratingbar_all;
	private EditText et_info;
	private MyGridView gridview_shigong, gridview_fuwu, gridview_sheji;
	private ArrayList<MkDecorateCategory> shigongListData = new ArrayList<MkDecorateCategory>();
	private ArrayList<MkDecorateCategory> fuwuListData = new ArrayList<MkDecorateCategory>();
	private ArrayList<MkDecorateCategory> shejiListData = new ArrayList<MkDecorateCategory>();
	private CommonAdapter<MkDecorateCategory> shigongAdapter, fuwuAdapter,
			shejiAdapter;
	private int value_shigong, value_fuwu, value_sheji;
	protected int allEvaluate = 0;
	private String companyId;
	private int picUploadedNum;
	private int needUploadPicSize;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_casecomment;
	}

	@Override
	public void initView() {
		starNum = (TextView) findViewById(R.id.starNum);
		ratingbar_all = (RatingBar) findViewById(R.id.ratingbar_all);
		et_info = (EditText) findViewById(R.id.et_info);
		List<MkDecorateCategory> commentList = MKDataBase.getInstance()
				.getDecorateCategoryList(4);
		if (!Tool.isEmpty(commentList)) {//默认初始值
			value_shigong = commentList.get(0).getCode();
			value_fuwu = commentList.get(0).getCode();
			value_sheji = commentList.get(0).getCode();
		}
		//
		gridview_shigong = (MyGridView) findViewById(R.id.gridview_shigong);
		shigongListData.addAll(commentList);
		shigongAdapter = new CommonAdapter<MkDecorateCategory>(
				CaseCommentActivity.this, R.layout.item_txt, shigongListData) {

			@Override
			public void convert(ViewHolder viewHolder,
					final MkDecorateCategory t) {
				final int position = viewHolder.getPosition();
				TextView data = viewHolder.getView(R.id.txt);
				data.setText(t.getName());
				if (position == getSelectedPosition()) {
					data.setBackgroundResource(R.drawable.shape_ing);
					data.setTextColor(Color.parseColor("#FF3499"));
				} else {
					data.setBackgroundResource(R.drawable.shape_btn_normal);
					data.setTextColor(getResources().getColor(
							R.color.light_gray));
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								shigongAdapter.setSelectedPosition(position);
								value_shigong = shigongListData.get(position)
										.getCode();
								notifyDataSetChanged();
							}
						});
			}

		};
		gridview_shigong.setAdapter(shigongAdapter);
		//
		gridview_fuwu = (MyGridView) findViewById(R.id.gridview_fuwu);
		fuwuListData.addAll(commentList);
		fuwuAdapter = new CommonAdapter<MkDecorateCategory>(
				CaseCommentActivity.this, R.layout.item_txt, fuwuListData) {

			@Override
			public void convert(ViewHolder viewHolder,
					final MkDecorateCategory t) {
				final int position = viewHolder.getPosition();
				TextView data = viewHolder.getView(R.id.txt);
				data.setText(t.getName());
				if (position == getSelectedPosition()) {
					data.setBackgroundResource(R.drawable.shape_ing);
					data.setTextColor(Color.parseColor("#FF3499"));
				} else {
					data.setBackgroundResource(R.drawable.shape_btn_normal);
					data.setTextColor(getResources().getColor(
							R.color.light_gray));
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								fuwuAdapter.setSelectedPosition(position);
								value_fuwu = fuwuListData.get(position)
										.getCode();
								notifyDataSetChanged();
							}
						});
			}

		};
		gridview_fuwu.setAdapter(fuwuAdapter);
		//
		gridview_sheji = (MyGridView) findViewById(R.id.gridview_sheji);
		shejiListData.addAll(commentList);
		shejiAdapter = new CommonAdapter<MkDecorateCategory>(
				CaseCommentActivity.this, R.layout.item_txt, shejiListData) {

			@Override
			public void convert(ViewHolder viewHolder,
					final MkDecorateCategory t) {
				final int position = viewHolder.getPosition();
				TextView data = viewHolder.getView(R.id.txt);
				data.setText(t.getName());
				if (position == getSelectedPosition()) {
					data.setBackgroundResource(R.drawable.shape_ing);
					data.setTextColor(Color.parseColor("#FF3499"));
				} else {
					data.setBackgroundResource(R.drawable.shape_btn_normal);
					data.setTextColor(getResources().getColor(
							R.color.light_gray));
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								shejiAdapter.setSelectedPosition(position);
								value_sheji = shejiListData.get(position)
										.getCode();
								notifyDataSetChanged();
							}
						});
			}

		};
		gridview_sheji.setAdapter(shejiAdapter);
		//
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("companyId", companyId);
				map.put("allEvaluate", allEvaluate);
				map.put("designLevel", value_sheji);
				map.put("constructeQuality", value_shigong);
				map.put("serviceAttitude", value_fuwu);
				map.put("evaluateRemark", et_info.getText().toString());
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
				map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));

				req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300036));
				req.setBody(JsonUtil.Map2JsonObj(map));
				LogUtil.d("wangke", "————" + map);
				httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
			}
		});
		findViewById(R.id.left_res_title).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finishWhenTip();
					}
				});
		commonDialog = new CommonDialog(CaseCommentActivity.this, "提示",
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
		picPathList.add("+");// 添加加号显示
		setSelectPic(true);
	}

	@Override
	public void initValue() {
		companyId = getIntent().getStringExtra("companyId");
	}

	private void setSelectPic(boolean isAdd) {
		gridview_uploadpic.setAdapter(new CommonAdapter<String>(this,
				R.layout.add_talent_pic_gridview_item, picPathList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final String imgItem) {
				// 如果大于8个删除第一个图片也就是增加图片按钮
				if (picPathList.size() < 7) {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item,
								R.drawable.clickaddpic);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												CaseCommentActivity.this,
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
	public void bindListener() {
		ratingbar_all
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						allEvaluate = (int) arg1;
						starNum.setText(allEvaluate + "星");
					}
				});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (picPathList.size() > 1) {
				if (!Tool.isEmpty(resp.getBody())
						&& !Tool.isEmpty(resp.getBody().get("data"))
						&& resp.getBody().get("data").toString().length() > 2) {
					JsonArray picJsonArray = resp.getBody().get("data")
							.getAsJsonArray();
					int webResultPicSize = picJsonArray.size();
					needUploadPicSize = picPathList.size() - 1;
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
									+ (picPathList.size() - 1) + "==>" + map);
							Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
							List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
							FormFileBean mainFfb = new FormFileBean();
							mainFfb.setFileName("photo_" + i + ".png");
							mainFfb.setFile(new File(picPathList.get(i)));
							details_FileBeans.add(mainFfb);
							mapFileBean.put("file", details_FileBeans);
							uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
									mapFileBean, req, true);
						}
					}
				}
			} else {
				ToastUtil.showShortToast("发表成功");
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
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						CaseCommentActivity.this, "提示", resp.getHeader()
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
