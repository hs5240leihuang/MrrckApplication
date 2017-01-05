package com.meiku.dev.ui.morefun;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.BitmapUtil;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.yunxin.CaptureVideoActivity;
import com.meiku.dev.yunxin.StorageType;
import com.meiku.dev.yunxin.StorageUtil;

public class SelectPictureActivity extends BaseActivity implements
		View.OnClickListener {

	public static final String EXTRA_RESULT = "result";

	public static final int MODE_MULTI = 1;
	public static int MODE_SINGLE = 0;

	private final String TAG = "SelectPictureActivity";
	private final int MAX_LOCAL_VIDEO_FILE_SIZE = 20 * 1024 * 1024;
	/**
	 * 选择模式 单选or多选
	 */
	private int SELECT_MODE;

	/**
	 * 多选可选择的数量
	 */
	private int MAXNUM = 9;

	private LinearLayout dialogLayout;
	private Button takePhotoBtn, pickPhotoBtn, pickMrrckBtn, cancelBtn;

	/** 获取到的图片路径 */
	private String picPath;

	private Intent lastIntent;

	private Uri photoUri;

	/**
	 * 是否显示美库相册 0 不显示 1 显示
	 */
	private int showMrrck;
	private String cameratakePicPath;
	/**
	 * 是否显示保存图片
	 */
	private boolean showSaveBtn;

	private String saveImgUrl;

	private boolean mode_selectVideo;

	private File shootVideoFile;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			SelectPictureActivity.this.overridePendingTransition(
					R.anim.push_bottom_out, 0);
			break;
		case R.id.btn_take_photo:
			if (mode_selectVideo) {
				takeVideo();
			} else {
				takePhoto();
			}
			break;
		case R.id.btn_pick_photo:
			if (SELECT_MODE == MODE_SINGLE) {
				if (mode_selectVideo) {
					chooseVideoFromLocal();
				} else {
					pickPhoto();
				}
			} else {
				// pickMulitPhoto();
			}
			break;
		case R.id.btn_pick_mrrck:
			break;
		case R.id.btn_save:
			if (!Tool.isEmpty(saveImgUrl)) {
				new getBitMap().execute(saveImgUrl);
			} else {
				ToastUtil.showShortToast("图片路径有误");
			}
			break;
		default:
			finish();
			SelectPictureActivity.this.overridePendingTransition(
					R.anim.push_bottom_out, 0);
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); // MediaStore.ACTION_IMAGE_CAPTURE
			// ContentValues values = new ContentValues();
			// photoUri =
			// SelectPictureActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			// values);
			// intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			cameratakePicPath = FileConstant.CacheFilePath
					+ System.currentTimeMillis() + ".jpg";
			FileConstant.TAKEPHOTO_PATH = cameratakePicPath;
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(cameratakePicPath)));
			intent.putExtra("return-data", true);
			LogUtil.d(TAG, "takePhoto--拍照路径存" + FileConstant.TAKEPHOTO_PATH);
			/** ----------------- */
			startActivityForResult(intent, ConstantKey.SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	// 请求加载系统照相机
	private static final int REQUEST_CAMERA_VIDEO = 100;
	private static final int REQUEST_SELECT_VIDEO = 101;

	/**
	 * 拍视频
	 */
	private void takeVideo() {
		// startActivityForResult(new Intent(SelectPictureActivity.this,
		// ShootVideoActivity.class), REQUEST_CAMERA_VIDEO);

		if (!StorageUtil.hasEnoughSpaceForWrite(SelectPictureActivity.this,
				StorageType.TYPE_VIDEO, true)) {
			return;
		}
		String videoFilePath = StorageUtil.getWritePath(
				SelectPictureActivity.this, Util.get36UUID()
						+ ConstantKey.FileSuffix.MP4, StorageType.TYPE_TEMP);
		shootVideoFile = new File(videoFilePath);

		// 启动视频录制
		CaptureVideoActivity.start(SelectPictureActivity.this, videoFilePath,
				REQUEST_CAMERA_VIDEO);
	}

	/***
	 * 从相册中取图片-1张
	 */
	private void pickPhoto() {
		try {
			// Intent intent = new Intent();
			// intent.setType("image/*");
			// intent.setAction(Intent.ACTION_GET_CONTENT);
			Intent intent;
			if (Build.VERSION.SDK_INT < 19) {
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
			} else {
				intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			}

			startActivityForResult(intent,
					ConstantKey.SELECT_PIC_BY_PICK_PHOTO_ONE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从本地相册中选择视频
	 */
	protected void chooseVideoFromLocal() {
		if (Build.VERSION.SDK_INT >= 19) {
			chooseVideoFromLocalKitKat();
		} else {
			chooseVideoFromLocalBeforeKitKat();
		}
	}

	public static final String MIME_VIDEO_ALL = "video/*";

	/**
	 * API19 之后选择视频
	 */
	protected void chooseVideoFromLocalKitKat() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		try {
			startActivityForResult(intent, REQUEST_SELECT_VIDEO);
		} catch (ActivityNotFoundException e) {
			ToastUtil.showShortToast(getString(R.string.gallery_invalid));
		} catch (SecurityException e) {

		}
	}

	/**
	 * API19 之前选择视频
	 */
	protected void chooseVideoFromLocalBeforeKitKat() {
		Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
		mIntent.setType(MIME_VIDEO_ALL);
		mIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		try {
			startActivityForResult(mIntent, REQUEST_SELECT_VIDEO);
		} catch (ActivityNotFoundException e) {
			ToastUtil.showShortToast(getString(R.string.gallery_invalid));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		SelectPictureActivity.this.overridePendingTransition(
				R.anim.push_bottom_out, 0);
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK)
			LogUtil.d(TAG, "onActivityResult--requestCode==" + requestCode);
		{
			switch (requestCode) {
			case ConstantKey.SELECT_PIC_BY_PICK_PHOTO_ONE:
				doPhoto(requestCode, data);
				break;
			case ConstantKey.SELECT_PIC_BY_TACK_PHOTO:
				// doPhoto(requestCode, data);
				try {
					if (Tool.isEmpty(FileConstant.TAKEPHOTO_PATH)) {
						LogUtil.d(TAG, "拍照路径不存在");
						finish();
						return;
					}
					if (FileHelper.isFileExist(new File(
							FileConstant.TAKEPHOTO_PATH))) {
						LogUtil.d(TAG, "onActivityResult--拍照路径存在");
						Intent intent = new Intent();
						intent.putExtra(ConstantKey.KEY_PHOTO_PATH,
								FileConstant.TAKEPHOTO_PATH);
						setResult(Activity.RESULT_OK, intent);
						FileConstant.TAKEPHOTO_PATH = "";
						finish();
						LogUtil.d(TAG, "finish");
					} else {
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
					finish();
				}

				break;
			case ConstantKey.SELECT_PIC_BY_PICK_PHOTO_MULIT:
			case ConstantKey.SELECT_PIC_BY_PICK_MRRCK_MULIT:
				LogUtil.d(TAG, "onActivityResult---多选--关闭页面");
				setResult(Activity.RESULT_OK, data);
				finish();
				break;
			case REQUEST_CAMERA_VIDEO:// 拍摄视频返回
				// if (data != null) {
				// Intent intent = new Intent();
				// intent.putExtra(ConstantKey.KEY_VIDEO_PATH,
				// data.getStringExtra("videoPath"));
				// setResult(Activity.RESULT_OK, intent);
				// }
				// finish();
				if (shootVideoFile == null || !shootVideoFile.exists()) {
					return;
				}
				// N930拍照取消也产生字节为0的文件
				if (shootVideoFile.length() <= 0) {
					shootVideoFile.delete();
					return;
				}
				String shootVideoPath = shootVideoFile.getPath();
				Intent intent = new Intent();
				intent.putExtra(ConstantKey.KEY_VIDEO_PATH, shootVideoPath);
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
			case REQUEST_SELECT_VIDEO:// 选择视频返回
				if (data != null) {
					Uri videoUri = data.getData();
					String videoPath = "";
					double videoSize = 0;
					double videoWidth = 0;
					double videoHeight = 0;
					if (videoUri == null) {
						// Toast.makeText(this, "选择图片文件出错",
						// Toast.LENGTH_LONG).show();
						ToastUtil.showShortToast("获取视频路径失败！");
						finish();
						return;
					}
					String[] pojo = { MediaStore.Images.Media.DATA,
							MediaStore.Images.Media.SIZE,
							MediaStore.Images.Media.WIDTH,
							MediaStore.Images.Media.HEIGHT };
					Cursor cursor = SelectPictureActivity.this
							.getContentResolver().query(videoUri, pojo, null,
									null, null);
					if (cursor != null) {
						int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
						int columnIndex2 = cursor
								.getColumnIndexOrThrow(pojo[1]);
						int columnIndex3 = cursor
								.getColumnIndexOrThrow(pojo[2]);
						int columnIndex4 = cursor
								.getColumnIndexOrThrow(pojo[3]);
						cursor.moveToFirst();
						videoPath = cursor.getString(columnIndex);
						videoSize = cursor.getDouble(columnIndex2);
						videoWidth = cursor.getDouble(columnIndex3);
						videoHeight = cursor.getDouble(columnIndex4);
						LogUtil.d("hl", "select_videoPath = " + videoPath);
						LogUtil.d("hl", "select_videoSize = " + videoSize);
						LogUtil.d("hl", "select_videoHeight = " + videoHeight);
						LogUtil.d("hl", "select_videoWidth= " + videoWidth);
						cursor.close();
					} else {
						// miui 2.3 有可能为null
						videoPath = videoUri.getPath();
					}
					if (!Tool.isEmpty(videoPath)
							&& FileHelper.isFileExist(new File(videoPath))) {
						if (videoSize > 20 * 1024 * 1024
								|| new File(videoPath).length() > 20 * 1024 * 1024) {
							ToastUtil.showShortToast("选择视频不能超过20M");
							finish();
							return;
						}
						Intent intentChoiseVideo = new Intent();
						intentChoiseVideo.putExtra(ConstantKey.KEY_VIDEO_PATH,
								videoPath);
						intentChoiseVideo.putExtra(ConstantKey.KEY_VIDEO_SIZE,
								videoSize);
						intentChoiseVideo.putExtra(
								ConstantKey.KEY_VIDEO_HEIGHT, videoHeight);
						intentChoiseVideo.putExtra(ConstantKey.KEY_VIDEO_WITH,
								videoWidth);
						setResult(Activity.RESULT_OK, intentChoiseVideo);
					} else {
						ToastUtil.showShortToast("获取视频路径失败！");
					}

				} else {
					// ToastUtil.showShortToast("未能获取视频路径！");
				}
				finish();
				break;
			default:
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == ConstantKey.SELECT_PIC_BY_PICK_PHOTO_ONE) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				// Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				// Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		// Cursor cursor = context.getContentResolver().query(photoUri,
		// pojo, null, null,null);
		LogUtil.d(TAG, "managedQuery__start" + photoUri);
		Cursor cursor = SelectPictureActivity.this.getContentResolver().query(
				photoUri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		} else {
			picPath = photoUri.getPath();
		}
		Log.i(TAG, "imagePath = " + picPath);
		if (picPath != null) {
			lastIntent.putExtra(ConstantKey.KEY_PHOTO_PATH, picPath);
			LogUtil.d(TAG, "RESULT_OK--关闭页面");
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		} else {
			if (cursor != null) {
				cursor.close();
			}
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			SelectPictureActivity.this.overridePendingTransition(
					R.anim.push_bottom_out, 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_select_picture;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);

		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
		takePhotoBtn.setOnClickListener(this);

		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
		pickPhotoBtn.setOnClickListener(this);

		pickMrrckBtn = (Button) findViewById(R.id.btn_pick_mrrck);
		pickMrrckBtn.setOnClickListener(this);
		if (0 == showMrrck) {
			pickMrrckBtn.setVisibility(View.GONE);
		}

		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);

		lastIntent = getIntent();

		SELECT_MODE = getIntent().getIntExtra("SELECT_MODE", MODE_SINGLE);
		MAXNUM = getIntent().getIntExtra("MAX_NUM", 9);
		showSaveBtn = getIntent().getBooleanExtra("showSaveBtn", false);

		View btn_save = findViewById(R.id.btn_save);
		btn_save.setVisibility(showSaveBtn ? View.VISIBLE : View.GONE);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void initValue() {
		showMrrck = getIntent().getIntExtra(
				ConstantKey.BUNDLE_SHOW_MRRCK_ALBUM, 1);
		saveImgUrl = getIntent().getStringExtra("saveImgUrl");
		mode_selectVideo = getIntent().getBooleanExtra(
				ConstantKey.BUNDLE_SELECT_VIDEO, false);
		if (mode_selectVideo) {
			takePhotoBtn.setText("拍摄视频");
			pickPhotoBtn.setText("选择视频");
			pickMrrckBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		LogUtil.d(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		LogUtil.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		LogUtil.d(TAG, "onDestroy");
		super.onDestroy();
	}

	// 把一个url的网络图片变成一个本地的BitMap
	private class getBitMap extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			URL myFileUrl = null;
			InputStream is = null;
			Bitmap bitMap = null;
			try {
				myFileUrl = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				bitMap = Util.decodeSampledBitmapFromStream(is, 400, 300);
				// byte[] data = getBytes(is);
				// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			return bitMap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			Bitmap bm = BitmapUtil.compBitmap(bitmap, 300, 400);
			String savePath = MediaStore.Images.Media.insertImage(
					getContentResolver(), bm, "title", null);
			if (!"".equals(savePath) && null != savePath) {
				ToastUtil.showShortToast("保存成功!");
				finish();
			} else {
				ToastUtil.showShortToast("保存失败!");
			}
			super.onPostExecute(bm);
		}
	}

}
