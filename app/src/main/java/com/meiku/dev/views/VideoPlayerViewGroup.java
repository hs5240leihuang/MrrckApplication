package com.meiku.dev.views;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.meiku.dev.R;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

/**
 * 
 * 视频播放view组件
 * 
 * @author huanglei
 * 
 */
public class VideoPlayerViewGroup implements OnErrorListener,
		OnPreparedListener {

	private Context context;
	private View view;
	private String TAG = getClass().getSimpleName();
	private ImageView stop,fullscreen;
	private TextView timeEnd, timeElapsed;
	private ProgressBar progressBar;
	private MyVideoView videoviewer;
	private CountDownTimer timer;
	private int mVideoWidth, mVideoHeight;

	private String videoPath;// 视频路径
	private SimpleDraweeView workImg;//缩略图
	private int flag = 0;// 记录按钮点击状态的
	private int time;

	public VideoPlayerViewGroup(Context context, String videoPath,int fileSeconds) {
		this.context = context;
		this.videoPath = videoPath;
		this.time = fileSeconds*1000;
		init();
	}

	public View getView() {
		return view;
	}

	public SimpleDraweeView getWorkImg() {
		return workImg;
	}

	private void init() {
		view = LayoutInflater.from(context).inflate(
				R.layout.view_myvideopalyer, null, false);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		stop = (ImageView) view
				.findViewById(R.id.video_play_back_pause_or_stop_iv);
		stop.setClickable(false);
		fullscreen=(ImageView) view.findViewById(R.id.fullscreen);
		fullscreen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it=new Intent(context,TestVideoActivity.class);
				it.putExtra("mrrck_videoPath", videoPath);
				context.startActivity(it);
			}

		});
		timeElapsed = (TextView) view.findViewById(R.id.timeElapsed);
		timeEnd = (TextView) view.findViewById(R.id.timeEnd);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		videoviewer = (MyVideoView) view.findViewById(R.id.videoviewer);
		workImg = (SimpleDraweeView) view.findViewById(R.id.workImg);
		LogUtil.d("hl", "##videoPath=" + videoPath);
		if (!Tool.isEmpty(videoPath)) {
			videoviewer.setVideoURI(Uri.parse(videoPath));
			videoviewer.requestFocus();
			videoviewer.setKeepScreenOn(true);
			videoviewer.setOnErrorListener(this);
			//videoviewer.setOnPreparedListener(this);
		}
		stop.setClickable(true);
		stop.setOnClickListener(playMediaListener);
		
		int time_elapsed = videoviewer.getCurrentPosition();
		progressBar.setProgress(time_elapsed);
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
							stop.setImageResource(R.drawable.video_pause_icon);
							stopMedia();
						}
					};
			
					timeEnd.setText(countTime(time));
					timeElapsed.setText(countTime(time_elapsed));
	}
 
	@Override
	public void onPrepared(MediaPlayer mp) {

		mp.setLooping(true);
		mVideoWidth = mp.getVideoWidth();
		mVideoHeight = mp.getVideoHeight();
//		stop.setClickable(true);
//		stop.setOnClickListener(playMediaListener);
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

		// onSeekCompletionListener declaration
		mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
			// show current frame after changing the playback position
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				if (!mp.isPlaying()) {
					// playMedia(true);
					System.out.println("inside the setOnSeekCompleteListener");
					playMedia(false);
				}
				System.out
						.println("inside------ the setOnSeekCompleteListener");
				timeElapsed.setText(countTime(videoviewer.getCurrentPosition()));
			}
		});

		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				stop.setImageResource(R.drawable.vedio_play_icon);
				flag = 0;
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

//		int time = videoviewer.getDuration();
//		int time_elapsed = videoviewer.getCurrentPosition();
//		progressBar.setProgress(time_elapsed);
//
//		// update current playback time every 500ms until stop
//		timer = new CountDownTimer(time, 500) {
//
//			@Override
//			public void onTick(long millisUntilFinished) {
//				timeElapsed
//						.setText(countTime(videoviewer.getCurrentPosition()));
//				float a = videoviewer.getCurrentPosition();
//				float b = videoviewer.getDuration();
//				progressBar.setProgress((int) (a / b * 100));
//			}
//
//			@Override
//			public void onFinish() {
//				stop.setImageResource(R.drawable.video_pause_icon);
//				stopMedia();
//			}
//		};
//
//		timeEnd.setText(countTime(time));
//		timeElapsed.setText(countTime(time_elapsed));
//		playMedia(true);
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
			if (flag == 0) {// 第一次点击
				videoviewer.setOnPreparedListener(VideoPlayerViewGroup.this);
				Handler handler= new Handler();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						playMedia(true);
					}
				}, 500);

				stop.setImageResource(R.drawable.video_pause_icon);
				workImg.setVisibility(View.GONE);
				ToastUtil.showShortToast("开始播放");
				flag = 1;
			} else if (flag == 1) {// 第二次点击
				playMedia(false);
				stop.setImageResource(R.drawable.vedio_play_icon);
				ToastUtil.showShortToast("暂停播放");
				flag = 0;
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

	public void onStop() {
		// TODO Auto-generated method stub
		if (videoviewer != null)
			videoviewer.stopPlayback();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

}
