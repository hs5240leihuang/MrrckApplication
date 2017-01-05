package com.zxing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.im.GroupInfoActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

/**
 * 扫一扫页面
 * 
 */
public class CaptureActivity extends BaseActivity implements Callback,
		View.OnClickListener {

	private CaptureActivityHandler handler;

	private ViewfinderView viewfinderView;

	private boolean hasSurface;

	private Vector<BarcodeFormat> decodeFormats;

	private String characterSet;

	private InactivityTimer inactivityTimer;

	private MediaPlayer mediaPlayer;

	private boolean playBeep;

	private static final float BEEP_VOLUME = 0.10f;

	private boolean vibrate;

	private ImageView back;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_capture;
	}

	@Override
	public void initView() {
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		back = (ImageView) findViewById(R.id.left_res_title);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne: // 个人
			break;
		case reqCodeTwo: // 群
			break;
		case reqCodeThree:// 关注版块
			ToastUtil.showLongToast("关注成功");
			onBackPressed();
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		if (null != obj) {
			String value = obj.getText();
			LogUtil.d("hl", "qrcode:" + value);
			if (value.startsWith("http://")) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				if (barcode == null) {
					dialog.setIcon(null);
				} else {
					Drawable drawable = new BitmapDrawable(barcode);
					dialog.setIcon(drawable);
				}
				dialog.setTitle("扫描结果");
				dialog.setMessage(obj.getText());
				dialog.setNegativeButton("打开",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 用默认浏览器打开扫描得到的地址
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								Uri content_url = Uri.parse(obj.getText());
								intent.setData(content_url);
								startActivity(intent);
								finish();
							}
						});
				dialog.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				dialog.create().show();
			} else {
				if (value.startsWith(ConstantKey.QR_CODE_USER)) { // 加好友
					Intent intent = new Intent(CaptureActivity.this,
							PersonShowActivity.class);
					intent.putExtra(PersonShowActivity.TO_USERID_KEY,
							value.replace(ConstantKey.QR_CODE_USER, ""));
					startActivity(intent);
					finish();
				} else if (value.startsWith(ConstantKey.QR_CODE_GROUP)) { // 加入群
					Intent i = new Intent(CaptureActivity.this, GroupInfoActivity.class);
					i.putExtra(ConstantKey.KEY_IM_MULTI_CHATROOMID,
							value.replace(ConstantKey.QR_CODE_GROUP, ""));
					startActivity(i);
					finish();
				} else if (value.startsWith(ConstantKey.QR_CODE_BOARD)) {// 关注社区版块
					String[] ids = value.split("&");
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("menuId", ids[2]);
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					map.put("boardId", ids[1]);
					req.setBody(JsonUtil.Map2JsonObj(map));
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_COMM_BOARDBYADD));
					httpPost(reqCodeThree, AppConfig.PUBLICK_BOARD, req);
				}
			}
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public void onBack(View v) {
		onBackPressed();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// case R.id.mycard_btn:
		// startActivity(new Intent(this, MyBusinessCardActivity.class));
		// break;
		// case R.id.add_btn:
		// startActivity(new Intent(this, InputPhoneAddFriend.class));
		// break;
		}
	}

}