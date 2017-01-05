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
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.ShootVideoActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

/**
 * 视频报名类
 * 暂不使用 跳PublishPhotoWorksActivity
 *
 */
public class PublishVedioWorksActivity extends BaseActivity implements OnClickListener {
	
	private FrameLayout layout_vedio;
	private String videoPath;
	private String videoSeconds;
	private ImageView iv_vedio;
	private String thumbnailPath;
	private TextView tv_addVedio,tv_category;
	private EditText et_name,et_introduce;
	private Button btn_publish;
	private String categoryId;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_publish_works;
	}

	@Override
	public void initView() {
		layout_vedio = (FrameLayout) findViewById(R.id.layout_vedio);
		iv_vedio = (ImageView) findViewById(R.id.iv_vedio);
		tv_addVedio = (TextView) findViewById(R.id.tv_addVedio);
		tv_category = (TextView) findViewById(R.id.tv_category);
		et_name = (EditText) findViewById(R.id.et_name);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		btn_publish = (Button) findViewById(R.id.btn_publish);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
		layout_vedio.setOnClickListener(this);
		tv_addVedio.setOnClickListener(this);
		btn_publish.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			finish();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_vedio:
			if (null != videoPath && !"".equals(videoPath)) {
				Intent intent = new Intent();
				intent.putExtra("mrrck_videoPath", videoPath);
				intent.setClass(this, TestVideoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, ShootVideoActivity.class);
				startActivityForResult(intent, reqCodeOne);
			}
			break;
		case R.id.tv_addVedio:
			Intent intent = new Intent(this, ShootVideoActivity.class);
			startActivityForResult(intent, reqCodeOne);
			break;
		case R.id.btn_publish:
			if (isValidate()) {
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("name",et_name.getText().toString());
				map.put("categoryId", 1);//categoryId);
				map.put("provinceCode",
						MrrckApplication.getInstance().provinceCode);
				map.put("cityCode", MrrckApplication.getInstance().cityCode);
				map.put("remark", et_introduce.getText().toString());
				map.put("fileType", 1);
				if (!Tool.isEmpty(videoPath)) {// 视频作品贴
					MediaPlayer mediaPlayer = new MediaPlayer();
					try {
						mediaPlayer.setDataSource(videoPath);
						mediaPlayer.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					}
					int duration = mediaPlayer.getDuration();
					int fileSeconds = duration / 1000;
					map.put("fileSeconds", fileSeconds);
//				}
				reqBase.setHeader(new ReqHead(
						AppConfig.BUSINESS_PUBLISHED_WORKS_COMMON));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				LogUtil.d("hl", "报名_请求=" + map);

				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
				FormFileBean formFile = new FormFileBean(new File(
						thumbnailPath), "photo.png");
				formFileBeans.add(formFile);
				mapFileBean.put("photo", formFileBeans);

				List<FormFileBean> formFileBeans2 = new ArrayList<FormFileBean>();
				FormFileBean formFile2 = new FormFileBean(new File(
						videoPath), "video.mp4");
				formFileBeans2.add(formFile2);
				mapFileBean.put("video", formFileBeans2);
				uploadFiles(reqCodeOne, AppConfig.PUBLICK_BOARD,
						mapFileBean, reqBase, true);
				}
			}
			break;
		default:
			break;
		}
	}

	private boolean isValidate() {
		if(Tool.isEmpty(tv_addVedio.getText().toString())){
			ToastUtil.showShortToast("请选择作品类别");
			return false;
		}else if(Tool.isEmpty(et_name.getText().toString())){
			ToastUtil.showShortToast("请输入作品名称");
			return false;
		}else if(Tool.isEmpty(et_introduce.getText().toString())){
			ToastUtil.showShortToast("请输入作品介绍");
			return false;
		}else if(Tool.isEmpty(videoPath)){
			ToastUtil.showShortToast("请拍摄视频");
			return false;
		}else{
			return true;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			switch (requestCode) {
			case reqCodeOne:
				videoPath = data.getStringExtra("videoPath");
				videoSeconds = data.getStringExtra("videoSeconds");
				iv_vedio.setImageBitmap(getVideoImg(videoPath));
				String bitMapPath = data.getStringExtra("bitMapPath");
				thumbnailPath = bitMapPath;
				break;
			default:
				break;
			}
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
	
}
