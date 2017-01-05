package com.meiku.dev.ui.vote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.ShootVideoActivity;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.ToastUtil;

public class SelectVedioActivity extends BaseActivity implements
		OnClickListener {

	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PATH = "video_path";

	private LinearLayout dialogLayout;
	private Button takeBtn, pickBtn, cancelBtn;

	/** 获取到的路径 */
	private String path;

	private Intent lastIntent;

	private Uri uri;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_select_vedio;
	}

	@Override
	public void initView() {
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);

		takeBtn = (Button) findViewById(R.id.btn_take);
		takeBtn.setOnClickListener(this);

		pickBtn = (Button) findViewById(R.id.btn_pick);
		pickBtn.setOnClickListener(this);

		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);

		lastIntent = getIntent();
	}

	@Override
	public void initValue() {

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			SelectVedioActivity.this.overridePendingTransition(
					R.anim.push_bottom_out, 0);
			break;
		case R.id.btn_take:

			Intent i = new Intent(this, ShootVideoActivity.class);
			startActivityForResult(i, reqCodeTwo);
			break;
		case R.id.btn_pick:
			Intent intent;
			if (Build.VERSION.SDK_INT < 19) {
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("video/*");
			} else {
				intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
			}
			startActivityForResult(Intent.createChooser(intent, "选个文件"),
					reqCodeOne);
			break;
		default:
			finish();
			SelectVedioActivity.this.overridePendingTransition(
					R.anim.push_bottom_out, 0);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				doResult(requestCode, data);
				break;
			// case SELECT_PIC_BY_TACK_PHOTO:
			// doPhoto(requestCode,data);
			// break;
			// case SELECT_PIC_BY_PICK_PHOTO_MULIT:
			// case SELECT_PIC_BY_PICK_MRRCK_MULIT:
			// setResult(Activity.RESULT_OK, data);
			// finish();
			// break;
			case reqCodeTwo: // 录制视频
				String videoPath = data.getStringExtra("videoPath");
				String bitMapPath = data.getStringExtra("bitMapPath");
				String videoSeconds = data.getStringExtra("videoSeconds");

				lastIntent.putExtra("videoPath", videoPath);
				lastIntent.putExtra("bitMapPath", bitMapPath);
				lastIntent.putExtra("videoSeconds", videoSeconds);
				setResult(Activity.RESULT_OK, lastIntent);
				finish();

				// ReqBase reqBase1 = new ReqBase();
				// reqBase1.setHeader(new ReqHead(
				// AppConfig.BUSINESS_UPLOAD_MK_RESUME_MEDIA));
				// Map<String, Object> body1 = new HashMap<String, Object>();
				// body1.put("userId", AppContext.getInstance().getUserInfo()
				// .getUserId());
				// body1.put("fileSeconds", videoSeconds);
				// body1.put("title", "");
				// body1.put("remark", "");
				// body1.put("isPublic", 1);
				// reqBase1.setBody(JsonUtil.String2Object(JsonUtil
				// .hashMapToJson(body1)));
				// Map<String, List<FormFileBean>> mapFileBean1 = new
				// HashMap<String, List<FormFileBean>>();
				// List<FormFileBean> formFileBeans1 = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile1 = new FormFileBean(new File(
				// thumbnailPath), "photo.png");
				// formFileBeans1.add(formFile1);
				// mapFileBean1.put("photo", formFileBeans1);
				//
				// List<FormFileBean> formFileBeans2 = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile2 = new FormFileBean(new
				// File(videoPath),
				// "video.mp4");
				// formFileBeans2.add(formFile2);
				// mapFileBean1.put("video", formFileBeans2);
				// uploadFiles(REQCODE_MKRESUMEVEDIO,
				// AppConfig.RESUME_REQUEST_MAPPING, mapFileBean1,
				// reqBase1, true);
				break;
			default:
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 选择后，获取路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doResult(int requestCode, Intent data) {
		if (requestCode == reqCodeOne) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				Toast.makeText(this, "选择视频文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			uri = data.getData();
			if (uri == null) {
				Toast.makeText(this, "选择视频文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			path = cursor.getString(columnIndex);
			cursor.close();
		}
		if (path != null) {
			String bitMapPath = "";
			try {
				bitMapPath = saveVideoBitmap(getVideoImg(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
			lastIntent.putExtra("videoPath", path);
			lastIntent.putExtra("bitMapPath", bitMapPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		} else {
			Toast.makeText(this, "选择视频文件不正确", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 获取视频缩略图*
	 */
	public Bitmap getVideoImg(String filePath) {
		Bitmap bitmap = null;
		try {
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
			Matrix matrix = new Matrix();
			// matrix.setRotate(90);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}

	/**
	 * 获取视频缩略图*路径
	 */
	public String saveVideoBitmap(Bitmap mBitmap) throws IOException {
		// File file = new File("/sdcard/Note/" );
		File file = new File("/sdcard" + FileConstant.UPLOAD_PHOTO_PATH);
		if (!file.exists()) {
			file.mkdir();
		}
		File fileName = new File(file, System.currentTimeMillis() + ".png");
		if (fileName.exists()) {
			fileName.delete();
		}
		fileName.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName.getPath();
	}
}
