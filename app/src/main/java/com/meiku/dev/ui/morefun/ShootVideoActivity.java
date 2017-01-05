package com.meiku.dev.ui.morefun;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.vote.SelectVedioActivity;
import com.meiku.dev.utils.BitmapUtil;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.SdcardUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CameraPreview;

public class ShootVideoActivity extends Activity implements
		View.OnClickListener {

	private ImageView videoPhoto;// 录像截图

	private MediaRecorder mediaRecorder;// 录像

	private Camera camera;// 相机

	private CameraPreview preview;

	private Camera.Parameters cameraParams;

	private FrameLayout layout;

	private Button back; // 取消

	private String path;// 文件保存路劲

	private String fileName;// 文件保存的名字

	private boolean isTaking;//

	private TextView timer;// 计时

	private int hour = 0;// 小时

	private int minute = 0;// 分钟

	private int second = 0;// 秒

	private int allTime = 0;// 总时间多少秒

	private boolean bool;

	private ImageView videoPlay;// 开始录制

	private Camera.AutoFocusCallback myAutoFocusCallback = null;

	private boolean isControlEnable = true;

	private ImageView exchangeIV;// 切换录制视角

	private Button loadBtn;// 上传图像

	private static int flag = 0;// 按钮点击两次的状态

	private int cameraPosition = 1;//

	private String videoPath;// 保存缩略图路径
	private String timeStr;// 获取录制时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_shoot_video);
		myAutoFocusCallback = new Camera.AutoFocusCallback() {
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					camera.setOneShotPreviewCallback(null);
				} else {

				}
			}
		};
		// 初始化控件
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		videoPhoto = (ImageView) findViewById(R.id.shoot_video_thumbnail_iv);
		layout = (FrameLayout) findViewById(R.id.shoot_video_framelay);
		videoPlay = (ImageView) findViewById(R.id.pouse_or_stop_iv);
		timer = (TextView) findViewById(R.id.shoot_timer_tv);
		back = (Button) findViewById(R.id.shoot_video_cancel_btn);
		exchangeIV = (ImageView) findViewById(R.id.shoot_video_self_iv);
		loadBtn = (Button) findViewById(R.id.shoot_video_comfir_btn);

		exchangeIV.setOnClickListener(this);
		back.setOnClickListener(this);
		videoPlay.setOnClickListener(this);
		// PackageManager pm = getPackageManager();
		// boolean permission = (PackageManager.PERMISSION_GRANTED ==
		// pm.checkPermission("android.hardware.camera",
		// ShootVideoActivity.this.getPackageName()));
		// if (!permission) {
		// ToastUtil.showShortToast("请打开摄像头权限");
		// return;
		// }
		initCamera();// 初始化照相机
		path = SdcardUtil.getPath() + FileConstant.UPLOAD_VIDEO_PATH;
		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(System
				.currentTimeMillis()) + ".mp4";
	}

	/**
	 * 初始化照相机
	 */
	@SuppressWarnings("deprecation")
	private void initCamera() {
		try {
			camera = Camera.open();
			if (camera == null) {
				ToastUtil.showShortToast("请打开摄像头权限");
				finish();
				return;
			}
			preview = new CameraPreview(this, camera);
			preview.setFocusable(false);
			preview.setEnabled(false);

			cameraParams = camera.getParameters();
			if (Build.VERSION.SDK_INT >= 14) {
				cameraParams.setFocusMode("auto");

				// camera.autoFocus(myAutoFocusCallback);
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (!Tool.isEmpty(camera)) {
							camera.autoFocus(myAutoFocusCallback);
						}
					}
				}, 200);

			} else {
				cameraParams
						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}

			// cameraParams.setRotation(90);
			camera.setDisplayOrientation(90);
			camera.setParameters(cameraParams);
			camera.setPreviewDisplay(preview.getHolder());
		} catch (Exception e) {
			ToastUtil.showShortToast("请打开摄像头权限");
			finish();
			return;
		}
		layout.addView(preview);

	}

	/**
	 * 开始
	 */
	private void startCamera() {

		second = 0;
		minute = 0;
		hour = 0;
		bool = true;
		camera.startPreview();
		camera.unlock();// 解锁后才能调用，否则报错
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setOrientationHint(90);
		mediaRecorder.setCamera(camera);// 将camera添加到视频录制端口
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音麦
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源

		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 输出格式
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 声音源码
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 视频源码
		mediaRecorder.setVideoSize(640, 480);
		// mediaRecorder.setVideoFrameRate(15);
		// mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mediaRecorder.setVideoEncodingBitRate(500 * 1024);

		mediaRecorder.setPreviewDisplay(preview.getHolder().getSurface());
		videoPath = path + fileName;// 视频本地路径
		mediaRecorder.setOutputFile(videoPath);
		try {
			mediaRecorder.prepare();
			timer.setVisibility(View.VISIBLE);
			handler.postDelayed(task, 1000);// 执行计时,每一秒进行一次
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mediaRecorder.start();
			Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
		} catch (IllegalStateException e) {
			this.finish();
			Toast.makeText(this, "不能录制视频!", Toast.LENGTH_SHORT).show();
		}

	}

	/*
	 * 定时器设置，实现计时
	 */
	public Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			isControlEnable = true;
			return false;
		}
	});

	public Runnable task = new Runnable() {
		public void run() {
			if (bool) {
				handler.postDelayed(this, 1000);
				allTime++;
				second++;
				if (second >= 60) {
					minute++;
					second = second % 60;
					videoPlay.performClick();
				}
				if (minute >= 60) {
					hour++;
					minute = minute % 60;
				}
				timer.setText(format(hour) + ":" + format(minute) + ":"
						+ format(second));
				// timeStr = format(hour) + ":" + format(minute) + ":" +
				// format(second);
			}
		}
	};

	/*
	 * 格式化时间
	 */
	public String format(int i) {
		String s = i + "";
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		try {
			if (mediaRecorder != null) {
				mediaRecorder.release();

			}
			if (camera != null) {
				camera.lock();
				camera.stopPreview();
				camera.release();
			}
			// camera.stopPreview();
			// camera.release();
			// camera=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始或结束录制
	 */
	private void startOrStopVideo() {
		if (isTaking) {
			mediaRecorder.stop();
			Toast.makeText(this, "录制完成，已保存  ", Toast.LENGTH_SHORT).show();

			finish();
		} else {
			// videoSizeLayout.setVisibility(View.GONE);
			startCamera();
			isTaking = true;
		}
	}

	@Override
	public void onClick(View v) {
		final Intent intent;
		switch (v.getId()) {

		case R.id.shoot_video_self_iv:// 切换拍摄视角
			// 摄像头计数
			int cameraCount = 0;
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();// 得到摄像头数量

			for (int i = 0; i < cameraCount; i++) {
				Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
				if (cameraPosition == 1) {
					// 现在是后置，变更为前置
					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																						// CAMERA_FACING_BACK后置
						camera.stopPreview();// 停掉原来摄像头的预览
						camera.release();// 释放资源
						camera = null;// 取消原来摄像头
						camera = Camera.open(i);// 打开当前选中的摄像头
						camera.setDisplayOrientation(90);// 摄像头必须是90度,否则永远是横屏拍摄
						try {
							camera.setPreviewDisplay(preview.getHolder());// 通过surfaceview显示取景画
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						camera.startPreview();// 开始预览
						cameraPosition = 0;
						break;
					}
				} else {
					// 现在是前置， 变更为后置
					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 表摄像头的方位，CAMERA_FACING_FRONT前置
																					// CAMERA_FACING_BACK后置
						camera.stopPreview();// 停掉原来摄像头的预览
						camera.release();// 释放资源
						camera = null;// 取消原来摄像头
						camera = Camera.open(i);// 打开当前选中的摄像头ͷ
						camera.setDisplayOrientation(90);
						try {
							camera.setPreviewDisplay(preview.getHolder());// 通过surfaceview显示取景画
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						camera.startPreview();// 开始预览
						cameraPosition = 1;
						break;
					}
				}

			}
			break;

		case R.id.shoot_video_cancel_btn:// 取消
			finish();
			break;

		case R.id.pouse_or_stop_iv:// 暂停或者停止播放
			if (flag == 0) {// 第一次点击状态
				exchangeIV.setVisibility(View.GONE);
				loadBtn.setVisibility(View.GONE);
				if (isControlEnable) {
					if (videoPath != null) {
						mediaRecorder.release();// 释放资源
					}
					startCamera();
					// startOrStopVideo();
					handler.sendEmptyMessageDelayed(0, 2000);
					videoPlay.setImageResource(R.drawable.onlease);
				}
				flag = 1;
			} else if (flag == 1) {// 第二次点击状态
				mediaRecorder.stop();// 停止记录
				handler.removeCallbacks(task);// 关闭计时器
				timer.setText(format(0) + ":" + format(0) + ":" + format(0));
				videoPlay.setImageResource(R.drawable.no_shoot);
				Log.d("ygy_path", videoPath);
				// 确定结束本页面,并将视频上传到服务器
				exchangeIV.setVisibility(View.VISIBLE);
				loadBtn.setVisibility(View.VISIBLE);
				loadBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 接口
						// 结束本页面
						Intent intent1 = new Intent();
						intent1.putExtra("videoPath", videoPath);// 录制路径

						intent1.putExtra("videoSeconds", allTime + "");// 录制时间
						try {
							String bitMapPath = BitmapUtil
									.saveVideoBitmap(BitmapUtil
											.getVideoImg(videoPath));// 获取bitmap的路径
							intent1.putExtra("bitMapPath", bitMapPath);// 获取bitmap的路径
						} catch (IOException e) {
							e.printStackTrace();
						}
						setResult(RESULT_OK, intent1);
						finish();
					}
				});

				videoPhoto.setVisibility(View.VISIBLE);
				videoPhoto.setImageBitmap(BitmapUtil.getVideoImg(videoPath));// 得到保存后的视频缩略图
				videoPhoto.setOnClickListener(new View.OnClickListener() {// 录像截图
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.putExtra("mrrck_videoPath", videoPath);
								intent.setClass(ShootVideoActivity.this,
										TestVideoActivity.class);
								startActivity(intent);
							}
						});

				flag = 0;
			}
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
