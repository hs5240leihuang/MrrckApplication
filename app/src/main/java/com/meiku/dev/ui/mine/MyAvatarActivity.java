package com.meiku.dev.ui.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.circleimage.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 个人头像页面
 * 
 */
public class MyAvatarActivity extends BaseFragmentActivity {

	private String url;
	protected String picPath;
	private ImageView mImageView;
	private PhotoViewAttacher mAttacher;
	protected CommonDialog commonDialog;
	private boolean avaterChanged = false;
	private String photoPath;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_myavatar;
	}

	@Override
	public void initView() {
		mImageView = (ImageView) findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		findViewById(R.id.right_res_title).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						selectPicture(reqCodeOne);
					}
				});
		findViewById(R.id.goback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finishWhenTip();
			}
		});
	}

	@Override
	public void initValue() {
		url = getIntent().getStringExtra("picUrl");
		if (!Tool.isEmpty(url)) {
			showImg(url);
		} else {
			ToastUtil.showShortToast("图片路径为空");
		}
	}

	private void showImg(final String picUrl) {
		ImageLoader.getInstance().displayImage(picUrl, mImageView,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						mAttacher.update();
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						mAttacher.update();
					}
				});
	}

	@Override
	public void bindListener() {
	}

	/**
	 * 选择图片
	 * 
	 * @param resultCode
	 */
	private void selectPicture(int resultCode) {
		Intent intent = new Intent(this, SelectPictureActivity.class);
		intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
		intent.putExtra("MAX_NUM", 1);// 选择的张数
		intent.putExtra("showSaveBtn", true);
		intent.putExtra("saveImgUrl", url);
		startActivityForResult(intent, resultCode);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		try {
			ReqBase reqBase = (ReqBase) arg0;
			MkUser userbean = (MkUser) JsonUtil.jsonToObj(MkUser.class, reqBase
					.getBody().get("userEntity").toString());
			if (!Tool.isEmpty(userbean)) {
				AppContext.getInstance().getUserInfo()
						.setClientHeadPicUrl(userbean.getClientHeadPicUrl());
				AppContext
						.getInstance()
						.getUserInfo()
						.setClientThumbHeadPicUrl(
								userbean.getClientThumbHeadPicUrl());
				AppContext.getInstance().setLocalUser(
						AppContext.getInstance().getUserInfo());
				ToastUtil.showShortToast("个人头像修改成功");
				showImg(userbean.getClientHeadPicUrl());
				sendBroadcast(new Intent(BroadCastAction.ACTION_CHANGE_AVATAR));
				avaterChanged = true;
			} else {
				ToastUtil.showShortToast("个人头像修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("个人头像修改失败");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				photoPath = data.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
				if (!Tool.isEmpty(photoPath)) {
					if (ConstantKey.USE_PIC_CUT) {
						Uri uri = Uri.fromFile(new File(photoPath));
						cropImageUri(uri, 200, 200, reqCodeTwo);
					} else {
						CompressPic(photoPath);
					}
				} else {
					ToastUtil.showShortToast("获取图片失败");
				}
				break;
			case reqCodeTwo:
				CompressPic(photoPath);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param photoPath
	 */
	private void CompressPic(String photoPath) {
		showProgressDialog("图片压缩中...");
		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				picPath = result;
				doUploadAvatar();
				super.onPostExecute(result);
			}

			@Override
			protected String doInBackground(String... arg0) {
				String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
				LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
				return photoPath;
			}

		}.execute(photoPath);
	}

	/**
	 * 上传头像
	 */
	private void doUploadAvatar() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CHANGE_AVATAR));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		FormFileBean formFile = new FormFileBean(new File(picPath),
				"avatarPhoto.png");
		formFileBeans.add(formFile);
		mapFileBean.put("photo", formFileBeans);
		uploadFiles(reqCodeOne, AppConfig.USER_REQUEST_MAPPING, mapFileBean,
				reqBase, false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		if (avaterChanged) {
			setResult(RESULT_OK);
		}
		finish();
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}
}
