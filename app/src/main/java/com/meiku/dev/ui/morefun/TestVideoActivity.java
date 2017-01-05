package com.meiku.dev.ui.morefun;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.MyVideoView;
import com.meiku.dev.views.VerticalSeekBar;

public class TestVideoActivity extends Activity implements
		MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{
	private String TAG = getClass().getSimpleName();
	private ImageView stop;
	private TextView timeEnd, timeElapsed;
	private ProgressBar progressBar;
	private MyVideoView videoviewer;
	private CountDownTimer timer;
	private int mVideoWidth, mVideoHeight;

	private AudioManager audioManager;// 播放管理
	private VerticalSeekBar soundVolumeSBar;// 音量控制进度条
	private ImageView volumeImage;// 音量控制

	private String videoPath;// 视频路径
	private static int flag = 0;// 记录按钮点击状态的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_video_playback);
		// 初始化控件
		initView();
		// 初始化数据
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		videoPath = intent.getStringExtra("mrrck_videoPath");
		if (!TextUtils.isEmpty(videoPath)) {
			videoviewer.setVideoURI(Uri.parse(videoPath));
		} else {
			// 下载视频
			downLoadVideo();
			ToastUtil.showShortToast("视频路径错误");
			return;
		}
		videoviewer.requestFocus();
		videoviewer.setKeepScreenOn(true);
		videoviewer.setOnErrorListener(this);
		videoviewer.setOnPreparedListener(this);

		// 音量
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int MaxSound = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		soundVolumeSBar.setMax(MaxSound);
		int currentSount = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundVolumeSBar.setProgress(currentSount);

		volumeImage.setOnClickListener(new View.OnClickListener() {// 音量控制
					@Override
					public void onClick(View v) {
						if (flag == 0) {
							soundVolumeSBar.setVisibility(View.VISIBLE);
							soundVolumeSBar
									.setOnSeekBarChangeListener(new SeekBarListener());
							flag = 1;
						} else if (flag == 1) {
							soundVolumeSBar.setVisibility(View.GONE);
							flag = 0;
						}
					}
				});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		findViewById(R.id.play_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		stop = (ImageView) findViewById(R.id.video_play_back_pause_or_stop_iv);
		timeElapsed = (TextView) findViewById(R.id.timeElapsed);
		timeEnd = (TextView) findViewById(R.id.timeEnd);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		videoviewer = (MyVideoView) findViewById(R.id.videoviewer);
		soundVolumeSBar = (VerticalSeekBar) findViewById(R.id.volume_bar);
		volumeImage = (ImageView) findViewById(R.id.video_play_back_volume_iv);
	}

	/**
	 * 下载视频 接口实现
	 */
	private void downLoadVideo() {

	}

	/**
	 * 音量进度条
	 */
	class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				int SeekPosition = seekBar.getProgress();
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						SeekPosition, 0);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {

		mp.setLooping(false);
		mVideoWidth = mp.getVideoWidth();
		mVideoHeight = mp.getVideoHeight();
		stop.setOnClickListener(playMediaListener);

		mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onVideoSizeChanged called " + width + ":" + height);
				if (width == 0 || height == 0) {
					Log.e(TAG, "invalid video width(" + width + ") or height("
							+ height + ")");
					return;
				}
				mVideoWidth = width;
				mVideoHeight = height;
				playMedia(true);
			}
		});

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				stop.setImageResource(R.drawable.vedio_play_icon);
				stopMedia();
			}
		});
		// onBufferingUpdateListener declaration
		mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
			// show updated information about the buffering progress
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				Log.d(TAG, "percent: " + percent);
				progressBar.setSecondaryProgress(percent);
			}
		});

		int time = videoviewer.getDuration();
		int time_elapsed = videoviewer.getCurrentPosition();
		progressBar.setProgress(time_elapsed);

		// update current playback time every 500ms until stop
		timer = new CountDownTimer(time, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
				timeElapsed
						.setText(countTime(videoviewer.getCurrentPosition()));
				float a = videoviewer.getCurrentPosition();
				float b = videoviewer.getDuration();
				progressBar.setProgress((int) (a / b * 100));
			}

			@Override
			public void onFinish() {
				LogUtil.d("hl", "onFinish@@@@@");
				stop.setImageResource(R.drawable.vedio_play_icon);
				stopMedia();
			}
		};
		timeEnd.setText(countTime(time));
		timeElapsed.setText(countTime(time_elapsed));
		playMedia(true);
	}

	/**
	 * 停止播放
	 */
	private void stopMedia() {
		if (videoviewer.getCurrentPosition() != 0) {
			stop.setClickable(true);
			videoviewer.pause();
			videoviewer.seekTo(0);
			timer.cancel();
			timeElapsed.setText(countTime(videoviewer.getCurrentPosition()));
			progressBar.setProgress(0);
		}
	}

	/**
	 * 计算时间 *
	 * 
	 * @param miliseconds
	 * @return
	 */
	private String countTime(int miliseconds) {
		String timeInMinutes = new String();
		int minutes = miliseconds / 60000;
		int seconds = (miliseconds % 60000) / 1000;
		timeInMinutes = minutes + ":"
				+ (seconds < 10 ? "0" + seconds : seconds);
		return timeInMinutes;

	}

	private View.OnClickListener playMediaListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (videoviewer != null) {
				if (videoviewer.isPlaying()) {
					playMedia(false);
					stop.setImageResource(R.drawable.vedio_play_icon);
					ToastUtil.showShortToast("暂停播放");
				} else {
					playMedia(true);
					stop.setImageResource(R.drawable.video_pause_icon);
					ToastUtil.showShortToast("开始播放");
					flag = 1;
				}
			}
		}
	};

	/**
	 * @param isPlay
	 */
	private void playMedia(boolean isPlay) {
		System.err.println("height:- " + mVideoHeight);
		System.err.println("width:- " + mVideoWidth);
		if (isPlay) {
			videoviewer.changeVideoSize(mVideoWidth, mVideoHeight);
			videoviewer.start();
			timer.start();
		} else {
			videoviewer.pause();
			timer.cancel();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (videoviewer != null)
			videoviewer.stopPlayback();
		if (timer != null) {
			timer.cancel();
		}
		super.onStop();
	}
}