package com.meiku.dev.ui.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

/**
 * 更换背景图片
 * 
 */
public class ChangeCardActivity extends BaseActivity implements OnClickListener {
	private String cameratakePicPath;
	private final String TAG = "ChangeCardActivity";
	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;
	private final int selectCardBg = 10;
	private boolean isCompress = true;// 自定义的 图片是否压缩;
	private String clientBackgroundId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_change_background;
	}

	@Override
	public void initView() {
		findViewById(R.id.lin_phone_photo).setOnClickListener(this);
		findViewById(R.id.lin_one_photo).setOnClickListener(this);
		findViewById(R.id.lin_capture).setOnClickListener(this);
	}

	@Override
	public void initValue() {
		clientBackgroundId = getIntent().getStringExtra("clientBackgroundId");
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("更换个性背景图片失败");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_phone_photo:
			pickPhoto();
			break;
		case R.id.lin_one_photo:
			takePhoto();
			break;
		case R.id.lin_capture:
			Intent singleIntent = new Intent(this, SingleCardActivity.class);
			singleIntent.putExtra("clientBackgroundId", clientBackgroundId);
			startActivityForResult(singleIntent, selectCardBg);
			break;
		default:
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

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
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

	/***
	 * 从相册中取图片-1张
	 */
	private void pickPhoto() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}

		startActivityForResult(intent, ConstantKey.SELECT_PIC_BY_PICK_PHOTO_ONE);
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
						return;
					}
					if (FileHelper.isFileExist(new File(
							FileConstant.TAKEPHOTO_PATH))) {
						LogUtil.d(TAG, "onActivityResult--拍照路径存在");
						picPath = FileConstant.TAKEPHOTO_PATH;
						// Intent intent = new Intent();
						// intent.putExtra(ConstantKey.KEY_PHOTO_PATH,
						// FileConstant.TAKEPHOTO_PATH);
						// setResult(Activity.RESULT_OK, intent);
						FileConstant.TAKEPHOTO_PATH = "";
						// 是否压缩图片
						if (isCompress) {
							CompressPic(picPath);// 压缩后上传
						} else {
							updateCardBg();// 不压缩直接上传
						}
						LogUtil.d(TAG, "finish");
					} else {
						LogUtil.d(TAG, "拍照路径不存在");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case selectCardBg:
				if (data != null
						&& data.getBooleanExtra("changeSuccess", false)) {
					setResult(RESULT_OK);
					finish();
				}
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
		try {
			Cursor cursor = ChangeCardActivity.this.getContentResolver().query(
					photoUri, pojo, null, null, null);
			if (cursor != null) {
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				picPath = cursor.getString(columnIndex);
				cursor.close();
			}
			Log.i(TAG, "imagePath = " + picPath);
			if (picPath != null
					&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
							|| picPath.endsWith(".jpg")
							|| picPath.endsWith(".JPG")
							|| picPath.endsWith(".jpeg") || picPath
								.endsWith(".JPEG"))) {
				// lastIntent.putExtra(ConstantKey.KEY_PHOTO_PATH, picPath);
				LogUtil.d(TAG, "RESULT_OK--关闭页面");
				// setResult(Activity.RESULT_OK, lastIntent);
				// finish();
				// 是否压缩图片
				if (isCompress) {
					CompressPic(picPath);// 压缩后上传
				} else {
					updateCardBg();// 不压缩直接上传
				}
			} else {
				if (cursor != null) {
					cursor.close();	
				}
				Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 更新主页背景图片url
	public void updateCardBg() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("backGroundId", "");
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CHANGE_CARDBG));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(picPath)) {
			// 发帖图片
			List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
			logo_FileBeans.add(new FormFileBean(new File(picPath),
					"mycardbg.png"));
			mapFileBean.put("photo", logo_FileBeans);
		}
		uploadFiles(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
				mapFileBean, reqBase, true);
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
				updateCardBg();
				LogUtil.d("hl", "压缩后处理__" + result);
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

}
