package com.meiku.dev.ui.myshow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 图片和视频报名类(非比赛)
 * 
 */
public class PublishPhotoWorksActivity extends BaseActivity implements
		OnClickListener {

	// private MyGridView gv_show;
	// private List<GroupManageImageBean> photolist = new
	// ArrayList<GroupManageImageBean>();
	// private CommonAdapter<GroupManageImageBean> roomAdapter;
	private Button btn_publish;
	private TextView tv_category;
	private EditText et_name, et_introduce;
	private String categoryId;
	private LinearLayout layout_category, layout_top;
	private String fileType, categoryName;

	// private FrameLayout layout_vedio;
	// private String videoPath;
	// private String videoSeconds;
	private ImageView img_neiceng, img_waiceng;
	private String thumbnailPath;
	private String imgresault;
	private LinearLayout lin_top;

	// private TextView tv_addVedio;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_publish_photo_works;
	}

	@Override
	public void initView() {
		// gv_show = (MyGridView) findViewById(R.id.gv_show);
		img_neiceng = (ImageView) findViewById(R.id.img_neiceng);
		img_waiceng = (ImageView) findViewById(R.id.img_waiceng);
		btn_publish = (Button) findViewById(R.id.btn_publish);
		tv_category = (TextView) findViewById(R.id.tv_category);
		et_name = (EditText) findViewById(R.id.et_name);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		layout_category = (LinearLayout) findViewById(R.id.layout_category);
		// layout_vedio_text = (LinearLayout)
		// findViewById(R.id.layout_vedio_text);

		// layout_vedio = (FrameLayout) findViewById(R.id.layout_vedio);
		// iv_vedio = (ImageView) findViewById(R.id.iv_vedio);
		// tv_addVedio = (TextView) findViewById(R.id.tv_addVedio);
		lin_top = (LinearLayout) findViewById(R.id.lin_top);

		layout_top = (LinearLayout) findViewById(R.id.layout_top);
	}

	@Override
	public void initValue() {
		fileType = getIntent().getStringExtra("fileType");
		categoryId = getIntent().getStringExtra("categoryId");
		if (categoryId.equals("-1")) {

		} else {
			tv_category.setText(getIntent().getStringExtra("categoryName"));
		}
		// if (fileType.equals("0")) {
		// gv_show.setVisibility(View.VISIBLE);
		// layout_vedio_text.setVisibility(View.GONE);
		// } else {
		// gv_show.setVisibility(View.GONE);
		// layout_vedio_text.setVisibility(View.VISIBLE);
		// }
		// initphotoGrid();
		getTop();
	}

	private void getTop() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("cityCode",
				Tool.isEmpty(MrrckApplication.getInstance().cityCode) ? -1
						: MrrckApplication.getInstance().cityCode);
		req.setHeader(new ReqHead(
				AppConfig.BUSINESS_SEARCH_SOLICITATION_OF_PUBLICITY));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	}

	// private void initphotoGrid() {
	// photolist.clear();
	// GroupManageImageBean groupManageImageBeanAdd = new
	// GroupManageImageBean();
	// groupManageImageBeanAdd.setClientPicUrl(R.drawable.tianjia + "");
	// photolist.add(groupManageImageBeanAdd);
	// // if (!Tool.isEmpty(userAttachmentList)) {
	// //
	// // for (int i = 0; i < userAttachmentList.size(); i++) {
	// // GroupManageImageBean groupManageImageBean = new
	// // GroupManageImageBean();
	// // groupManageImageBean.setClientPicUrl(userAttachmentList.get(i)
	// // .getClientFileUrl());
	// // photolist.add(groupManageImageBean);
	// // }
	// // }
	//
	// roomAdapter = new CommonAdapter<GroupManageImageBean>(this,
	// R.layout.item_photogrid, photolist) {
	//
	// @Override
	// public void convert(final ViewHolder viewHolder,
	// final GroupManageImageBean t) {
	// if (viewHolder.getPosition() == 0) {
	// viewHolder.setImage(R.id.image_out,
	// Integer.parseInt(t.getClientPicUrl()));
	// viewHolder.getConvertView().setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// if (photolist.size() >= 9) {
	// ToastUtil.showShortToast("最多只能上传8张照片！");
	// return;
	// }
	// Intent intent = new Intent(
	// PublishPhotoWorksActivity.this,
	// SelectPictureActivity.class);
	// intent.putExtra(
	// "SELECT_MODE",
	// SelectPictureActivity.MODE_SINGLE);// 选择模式
	// intent.putExtra("MAX_NUM", 1);// 选择的张数
	// startActivityForResult(intent, reqCodeThree);
	//
	// }
	// });
	// } else {
	// viewHolder.setImage(R.id.image_out,
	// R.drawable.yinshi_jianhao);
	// viewHolder.setImage(R.id.image, t.getClientPicUrl(), 1);
	// viewHolder.getConvertView().setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// final CommonDialog commonDialog = new CommonDialog(
	// PublishPhotoWorksActivity.this,
	// "提示", "是否删除该照片", "确认", "取消");
	// commonDialog.show();
	// commonDialog
	// .setClicklistener(new ClickListenerInterface() {
	//
	// @Override
	// public void doConfirm() {
	// // 删除照片请求
	// // ReqBase req = new
	// // ReqBase();
	// // Map<String, Object> map =
	// // new HashMap<String,
	// // Object>();
	// // map.put("userId",
	// // AppContext
	// // .getInstance()
	// // .getUserInfo()
	// // .getId());
	// // map.put("attachmentId",
	// // userAttachmentList
	// // .get(viewHolder
	// // .getPosition()-1)
	// // .getAttachmentId());
	// // req.setHeader(new
	// // ReqHead(
	// // AppConfig.BUSINESS_DELETE_PHOTO));
	// // req.setBody(JsonUtil
	// // .Map2JsonObj(map));
	// // httpPost(
	// // reqCodeFive,
	// // AppConfig.PUBLIC_REQUEST_MAPPING,
	// // req);
	// photolist.remove(viewHolder
	// .getPosition());
	// roomAdapter
	// .notifyDataSetChanged();
	// commonDialog.dismiss();
	// }
	//
	// @Override
	// public void doCancel() {
	// // TODO Auto-generated
	// // method stub
	// commonDialog.dismiss();
	// }
	// });
	// }
	// });
	// }
	//
	// }
	//
	// };
	// gv_show.setAdapter(roomAdapter);
	// }

	@Override
	public void bindListener() {
		btn_publish.setOnClickListener(this);
		layout_category.setOnClickListener(this);
		img_neiceng.setOnClickListener(this);
		img_waiceng.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		// layout_vedio.setOnClickListener(this);
		// tv_addVedio.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase req = (ReqBase) arg0;
		LogUtil.d(req.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			RefreshObs.getInstance().notifyAllLisWithTag("normal");
			finish();
			break;
		case reqCodeTwo:
			final PostsEntity postsGuide;
			if (req.getBody().get("postsGuide").toString().length() > 2) {
				postsGuide = (PostsEntity) JsonUtil.jsonToObj(
						PostsEntity.class, req.getBody().get("postsGuide")
								.toString());
				if (Tool.isEmpty(postsGuide)
						|| Tool.isEmpty(postsGuide.getClientRecommendImgUrl())) {
					layout_top.setVisibility(View.GONE);
				} else {
					MySimpleDraweeView iv_top = new MySimpleDraweeView(
							PublishPhotoWorksActivity.this);
					lin_top.removeAllViews();
					lin_top.addView(iv_top, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					iv_top.setUrlOfImage(postsGuide.getClientRecommendImgUrl());
					lin_top.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							startActivity(new Intent(
									PublishPhotoWorksActivity.this,
									ShowMainActivity.class).putExtra("postsId",
									postsGuide.getPostsId()));
						}
					});
				}
			} else {
				layout_top.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("失败");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeThree:
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqCodeThree, pictrue.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqCodeThree, photoPath);
				}
				break;
			case reqCodeOne:
				categoryId = data.getStringExtra("categoryId");
				tv_category.setText(data.getStringExtra("categoryName"));
				// fileType = data.getStringExtra("fileType");
				// if (fileType.equals("0")) {
				// gv_show.setVisibility(View.VISIBLE);
				// layout_vedio_text.setVisibility(View.GONE);
				// } else {
				// gv_show.setVisibility(View.GONE);
				// layout_vedio_text.setVisibility(View.VISIBLE);
				// }
				break;
			case reqCodeTwo:
				// videoPath = data.getStringExtra("videoPath");
				// videoSeconds = data.getStringExtra("videoSeconds");
				// iv_vedio.setImageBitmap(getVideoImg(videoPath));
				// String bitMapPath = data.getStringExtra("bitMapPath");
				// thumbnailPath = bitMapPath;
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
			case reqCodeThree:
				BitmapUtils bitmapUtils = new BitmapUtils(
						PublishPhotoWorksActivity.this);
				bitmapUtils.display(img_neiceng, result);
				img_waiceng.setImageDrawable(ContextCompat.getDrawable(
						PublishPhotoWorksActivity.this,
						R.drawable.yinshi_jianhao));
				imgresault = result;
				// GroupManageImageBean groupManageImageBean = new
				// GroupManageImageBean();
				// groupManageImageBean.setClientPicUrl(result);
				// photolist.add(groupManageImageBean);
				// roomAdapter.notifyDataSetChanged();
				dismissProgressDialog();
				// ReqBase reqBase = new ReqBase();
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("moduleType", 22);
				// map.put("moduleId", AppContext.getInstance().getUserInfo()
				// .getId());
				// map.put("userId", AppContext.getInstance().getUserInfo()
				// .getId());
				// reqBase.setHeader(new ReqHead(
				// AppConfig.BUSINESS_SHANGCHUAN_PHOTO));
				// reqBase.setBody(JsonUtil.Map2JsonObj(map));
				// Map<String, List<FormFileBean>> mapFileBean = new
				// HashMap<String, List<FormFileBean>>();
				// formFileBeans = new ArrayList<FormFileBean>();
				// formFile = new FormFileBean(new File(result),
				// System.currentTimeMillis() + ".png");
				// formFileBeans.add(formFile);
				// mapFileBean.put("photo", formFileBeans);
				// uploadFiles(reqCodeSix, AppConfig.PUBLIC_REQUEST_MAPPING,
				// mapFileBean, reqBase, true);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_publish:
			if (validate()) {
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("name", et_name.getText().toString());
				map.put("categoryId", categoryId);
				map.put("provinceCode",
						MrrckApplication.getInstance().provinceCode);
				map.put("cityCode", MrrckApplication.getInstance().cityCode);
				map.put("remark", et_introduce.getText().toString());
				map.put("fileType", "0");
				// if (fileType.equals("1")) {// 视频作品贴
				// MediaPlayer mediaPlayer = new MediaPlayer();
				// try {
				// mediaPlayer.setDataSource(videoPath);
				// mediaPlayer.prepare();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// int duration = mediaPlayer.getDuration();
				// int fileSeconds = duration / 1000;
				// if(fileSeconds>60){
				// ToastUtil.showShortToast("视频长度超过60s,请重新选择");
				// return;
				// }
				// map.put("fileSeconds", fileSeconds);
				// }

				// LogUtil.d("hl", "报名_请求=" + map);
				//
				reqBase.setHeader(new ReqHead(
						AppConfig.BUSINESS_PUBLISHED_WORKS_COMMON));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				// if (fileType.equals("0")) {// 图片作品贴
				List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
				FormFileBean formFile = new FormFileBean(new File(imgresault),
						"photo.png");
				formFileBeans.add(formFile);

				mapFileBean.put("photo", formFileBeans);
				// }
				// else {// 视频
				// List<FormFileBean> formFileBeans = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile = new FormFileBean(new File(
				// thumbnailPath), "photo.png");
				// formFileBeans.add(formFile);
				// mapFileBean.put("photo", formFileBeans);
				//
				// List<FormFileBean> formFileBeans2 = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile2 = new FormFileBean(new File(
				// videoPath), "video.mp4");
				// formFileBeans2.add(formFile2);
				// mapFileBean.put("video", formFileBeans2);
				// }

				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				uploadFiles(reqCodeOne, AppConfig.PUBLICK_BOARD, mapFileBean,
						reqBase, true);
			}
			break;
		case R.id.layout_category:
			startActivityForResult(new Intent(PublishPhotoWorksActivity.this,
					WorkCategoryActivity.class), reqCodeOne);
			break;
		// case R.id.layout_vedio:
		// if (null != videoPath && !"".equals(videoPath)) {
		// Intent intent = new Intent();
		// intent.putExtra("mrrck_videoPath", videoPath);
		// intent.setClass(this, TestVideoActivity.class);
		// startActivity(intent);
		// } else {
		// // Intent intent = new Intent(this, ShootVideoActivity.class);
		// // startActivityForResult(intent, reqCodeTwo);
		// Intent intent = new Intent(this, SelectVedioActivity.class);
		// startActivityForResult(intent, reqCodeTwo);
		// }
		// break;
		// case R.id.tv_addVedio:
		// // Intent intent = new Intent(this, ShootVideoActivity.class);
		// // startActivityForResult(intent, reqCodeTwo);
		// Intent intent = new Intent(this, SelectVedioActivity.class);
		// startActivityForResult(intent, reqCodeTwo);
		// break;
		case R.id.img_waiceng:
			if (img_waiceng.getDrawable() == null) {
				Intent intent1 = new Intent(PublishPhotoWorksActivity.this,
						SelectPictureActivity.class);
				intent1.putExtra("SELECT_MODE",
						SelectPictureActivity.MODE_SINGLE);// 选择模式
				intent1.putExtra("MAX_NUM", 1);// 选择的张数
				startActivityForResult(intent1, reqCodeThree);
			} else {
				final CommonDialog commonDialog = new CommonDialog(
						PublishPhotoWorksActivity.this, "提示", "是否删除该照片", "确认",
						"取消");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						img_waiceng.setImageDrawable(null);
						img_neiceng.setImageDrawable(ContextCompat.getDrawable(
								PublishPhotoWorksActivity.this,
								R.drawable.addphoto));
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
			}
			break;
		case R.id.img_neiceng:
			break;
		case R.id.goback:
			final CommonDialog commonDialog = new CommonDialog(this, "提示",
					"是否放弃本次编辑", "确定", "取消");
			commonDialog.setClicklistener(new ClickListenerInterface() {

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
			commonDialog.show();
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		if (Tool.isEmpty(tv_category.getText().toString())) {
			ToastUtil.showShortToast("请选择作品类别");
			return false;
		} else if (Tool.isEmpty(et_name.getText().toString())) {
			ToastUtil.showShortToast("请输入作品名称");
			return false;
		} else if (Tool.isEmpty(et_introduce.getText().toString())) {
			ToastUtil.showShortToast("请输入作品介绍");
			return false;
		} else if (img_waiceng.getDrawable() == null) {
			ToastUtil.showShortToast("请上传图片");
			return false;
		}
		// else {
		// if(fileType.equals("1")){
		// File file = new File(videoPath);
		// LogUtil.e(file.length()+"");
		// if(file.length()>20971520){
		// ToastUtil.showShortToast("视频过大请重新上传");
		// return false;
		// }else{
		// return true;
		// }
		// }
		else {
			return true;
		}
	}

	/**
	 * 获取视频缩略图*
	 */
	@SuppressLint("NewApi")
	public Bitmap getVideoImg(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				// retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final CommonDialog commonDialog = new CommonDialog(this, "提示",
					"是否放弃本次编辑", "确定", "取消");
			commonDialog.setClicklistener(new ClickListenerInterface() {

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
			commonDialog.show();
		}
		return true;
	}

}
