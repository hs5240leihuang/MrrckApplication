package com.meiku.dev.views;

import java.io.File;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;

public class RecordButton extends TextView {
	public static final int BACK_RECORDING = R.drawable.chat_voice_bg_pressed;
	public static final int BACK_IDLE = R.drawable.chat_voice_bg;
	public static final int SLIDE_UP_TO_CANCEL = 0;
	public static final int RELEASE_TO_CANCEL = 1;
	private static final int MIN_INTERVAL_TIME = 1000;// 2s
	private static int[] recordImageIds = { R.drawable.chat_icon_voice0,
			R.drawable.chat_icon_voice1, R.drawable.chat_icon_voice2,
			R.drawable.chat_icon_voice3, R.drawable.chat_icon_voice4,
			R.drawable.chat_icon_voice5 };
	private TextView textView;
	private String outputPath = null;
	private RecordEventListener recordEventListener;
	private long startTime;
	private Dialog recordIndicator;
	private View view;
	private MediaRecorder recorder;
	private ObtainDecibelThread thread;
	private Handler volumeHandler;
	private ImageView imageView;
	private int status;
	private Handler timeUpHandler = new Handler();
	private OnDismissListener onDismiss = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			stopRecording();
		}
	};
	private boolean isonLine = true;
	private Context context;

	public RecordButton(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setRecordEventListener(RecordEventListener listener) {
		recordEventListener = listener;
	}

	private void init() {
		volumeHandler = new ShowVolumeHandler();
		setBackgroundResource(BACK_IDLE);
		initRecordDialog();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			outputPath = FileHelper.getRecordPathByCurrentTime();
			if (!isonLine) {
				ToastUtil.showShortToast("暂不支持发送离线语音");
				return false;
			} else {
				startRecord();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (status == RELEASE_TO_CANCEL) {
				cancelRecord();
			} else {
				finishRecord();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getY() < 0) {
				status = RELEASE_TO_CANCEL;
			} else {
				status = SLIDE_UP_TO_CANCEL;
			}
			setTextViewByStatus();
			break;
		case MotionEvent.ACTION_CANCEL:
			cancelRecord();
			break;
		}
		return true;
	}

	public int getColor(int id) {
		return getContext().getResources().getColor(id);
	}

	private void setTextViewByStatus() {
		if (status == RELEASE_TO_CANCEL) {
			textView.setTextColor(getColor(R.color.mrrck_bg));
			textView.setText(R.string.chat_record_button_releaseToCancel);
		} else if (status == SLIDE_UP_TO_CANCEL) {
			textView.setTextColor(getColor(R.color.white));
			textView.setText(R.string.chat_record_button_slideUpToCancel);
		}
	}

	private void startRecord() {
		hasDone = false;
		timeUpHandler.postDelayed(timeUpRunable, 60 * 1000);
		startRecording();
	}

	private Runnable timeUpRunable = new Runnable() {

		@Override
		public void run() {
			setCanFocusable(false);
			finishRecord();
		}
	};
	private boolean hasDone;

	private void initRecordDialog() {
		recordIndicator = new Dialog(getContext(),
				R.style.chat_record_button_toast_dialog_style);

		view = inflate(getContext(), R.layout.chat_record_layout, null);
		imageView = (ImageView) view.findViewById(R.id.imageView);
		textView = (TextView) view.findViewById(R.id.textView);
		recordIndicator.setContentView(view, new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		recordIndicator.setOnDismissListener(onDismiss);

		LayoutParams lp = recordIndicator.getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
	}

	private void removeFile() {
		File file = new File(outputPath);
		if (file.exists()) {
			file.delete();
		}
	}

	private void finishRecord() {
		if (null != timeUpHandler && null != timeUpRunable) {
			timeUpHandler.removeCallbacks(timeUpRunable);
		}
		recordIndicator.dismiss();
		setBackgroundResource(BACK_IDLE);
		long intervalTime = System.currentTimeMillis() - startTime;
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				stopRecording();
			}
		}, 1000);
		if (intervalTime < MIN_INTERVAL_TIME) {
			ToastUtil.showShortToast(getContext().getString(
					R.string.chat_record_button_pleaseSayMore));
			removeFile();
			return;
		}
		int sec = Math.round(intervalTime * 1.0f / 1000);
		if (recordEventListener != null && !hasDone) {
			if (FileHelper.isFileExist(new File(outputPath))
					&& new File(outputPath).length() > 0) {
				recordEventListener.onFinishedRecord(outputPath, sec);
			} else {
				showTip();// 部分手机录音权限被禁用后不能生存录音文件，有的手机有录音文件但是大小为0
			}
			hasDone = true;
		}
		setCanFocusable(true);
	}

	private void setCanFocusable(boolean canFocus) {
		if (canFocus) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					requestFocus();
					setClickable(true);
					setFocusable(true);
					setFocusableInTouchMode(true);
				}
			}, 1000);
		} else {
			clearFocus();
			setClickable(false);
			setFocusable(false);
			setFocusableInTouchMode(false);
		}

	}

	private void cancelRecord() {
		if (null != timeUpHandler && null != timeUpRunable) {
			timeUpHandler.removeCallbacks(timeUpRunable);
		}
		stopRecording();
		setBackgroundResource(BACK_IDLE);
		recordIndicator.dismiss();
		// ToastUtil.showShortToast(getContext().getString(
		// R.string.chat_cancelRecord));
		removeFile();
		setCanFocusable(true);
	}

	// 第一种，就是start的时候会报异常，这种我们把它包在try catch中即可捕获到异常。
	// 第二种，就是不报异常，正常执行，这种情况我们没办法去判断系统是否禁止了我们的app的录音权限，所以我在此分析的是部分机型在被禁止后不报异常，我们可以去检测音频振幅大小，部分机型的音频振幅值在用MediaRecorder时是0，在用AudioRecord时值小于0，所以这种情况我们可以通过其振幅值判断，
	// 第三种，部分手机录音权限被禁用后不能生存录音文件
	// 第四种，有的手机有录音文件但是大小为0
	private void startRecording() {
		if (recorder == null) {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(outputPath);
			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			recorder.reset();
			recorder.setOutputFile(outputPath);
		}
		if (recorder == null) {// 部分手机录音权限被禁用初始化后任为空
			showTip();
			hasDone = true;
			return;
		}
		try {
			recorder.start();// 部分手机权限被禁用，不可调用start()方法
			thread = new ObtainDecibelThread();
			thread.start();
			recordEventListener.onStartRecord();
			startTime = System.currentTimeMillis();
			setBackgroundResource(BACK_RECORDING);
			recordIndicator.show();
		} catch (Exception e) {
			e.printStackTrace();
			showTip();
			hasDone = true;
		}

	}

	private void stopRecording() {
		if (thread != null) {
			thread.exit();
			thread = null;
		}
		if (recorder != null) {
			try {
				recorder.stop();
			} catch (Exception e) {
			} finally {
				recorder.release();
				recorder = null;
			}
		}
	}

	public interface RecordEventListener {
		public void onFinishedRecord(String audioPath, int secs);

		void onStartRecord();
	}

	private class ObtainDecibelThread extends Thread {
		private volatile boolean running = true;

		public void exit() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (recorder == null || !running) {
					break;
				}
				int x = recorder.getMaxAmplitude();
				if (x != 0) {
					int f = (int) (10 * Math.log(x) / Math.log(10));
					int index = (f - 18) / 5;
					if (index < 0)
						index = 0;
					if (index > 5)
						index = 5;
					volumeHandler.sendEmptyMessage(index);
				}
			}
		}

	}

	class ShowVolumeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			imageView.setImageResource(recordImageIds[msg.what]);
			// imageView.setImageResource(recordImageIds[5]);
		}
	}

	public boolean isIsonLine() {
		return isonLine;
	}

	public void setIsonLine(boolean isonLine) {
		this.isonLine = isonLine;
	}

	private void showTip() {
		final CommonDialog commonDialog = new CommonDialog(context, "提示",
				"录音异常，请尝试在权限管理中开启录音权限！", "确定", "取消");
		commonDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void doConfirm() {
				commonDialog.dismiss();
				context.startActivity(new Intent(Settings.ACTION_SETTINGS));
			}

			@Override
			public void doCancel() {
				commonDialog.dismiss();
			}
		});
		commonDialog.show();
	}
}
