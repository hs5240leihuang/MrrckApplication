package com.meiku.dev.ui.product;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meiku.dev.R;

import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;

//手持身份证照片
public class SelectPhotoStyleActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	private String cameratakePicPath;
	private final String TAG = "SelectPictureActivity";
	private Button product_take_photo, product_pick_photo;
	/** 获取到的图片路径 */
	private String picPath;

	private Intent lastIntent;

	private Uri photoUri;
	private int flag;
	private TextView centertxt,tv_hint;
	private ImageView img_productstyle;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_selectphotostyle;
	}

	@Override
	public void initView() {
		tv_hint=(TextView) findViewById(R.id.tv_hint);
		img_productstyle=(ImageView) findViewById(R.id.img_productstyle);
		centertxt = (TextView) findViewById(R.id.center_txt_title);
		product_take_photo = (Button) findViewById(R.id.product_take_photo);
		product_pick_photo = (Button) findViewById(R.id.product_pick_photo);
		lastIntent = getIntent();

	}

	@Override
	public void initValue() {
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {
			centertxt.setText("手持身份证照片");
			img_productstyle.setBackground(ContextCompat.getDrawable(SelectPhotoStyleActivity.this,R.drawable.product_photostyle));
			tv_hint.setText("拖动或缩放照片确认人像与身份证是否清晰");
		} else {
			if (flag == 2) {
				centertxt.setText("个人信息所在面");
				img_productstyle.setBackground(ContextCompat.getDrawable(SelectPhotoStyleActivity.this,R.drawable.product_style2));
				tv_hint.setText("请选择清晰的身份证正面照");
			} else {
				centertxt.setText("国徽图案面");
				img_productstyle.setBackground(ContextCompat.getDrawable(SelectPhotoStyleActivity.this,R.drawable.product_style3));
				tv_hint.setText("请选择清晰带有国徽图案面");
			}
		}

	}

	@Override
	public void bindListener() {
		product_take_photo.setOnClickListener(this);
		product_pick_photo.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:

			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:

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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.product_take_photo:
			takePhoto();
			break;
		case R.id.product_pick_photo:
			pickPhoto();
			break;
		default:
			break;
		}
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
		Cursor cursor = SelectPhotoStyleActivity.this.getContentResolver()
				.query(photoUri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		Log.i(TAG, "imagePath = " + picPath);
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG")
						|| picPath.endsWith(".jpeg") || picPath
							.endsWith(".JPEG"))) {
			lastIntent.putExtra(ConstantKey.KEY_PHOTO_PATH, picPath);
			LogUtil.d(TAG, "RESULT_OK--关闭页面");
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		} else {
			cursor.close();
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
}
