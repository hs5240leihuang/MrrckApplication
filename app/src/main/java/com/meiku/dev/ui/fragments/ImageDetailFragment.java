package com.meiku.dev.ui.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.meiku.dev.R;
import com.meiku.dev.utils.BitmapUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.MessageDialog;
import com.meiku.dev.views.MessageDialog.messageListener;
import com.meiku.dev.views.circleimage.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 显示图片 Created by huanglei on 2015/10/2.
 */
public class ImageDetailFragment extends BaseFragment {
	private String mImageUrl = "";
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private Bitmap bitMap;
	public MediaScannerConnection msc;

	public static ImageDetailFragment newInstance(String imageUrl) {
		ImageDetailFragment f = new ImageDetailFragment();
		Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		//单击点击图片返回
		mAttacher
				.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

					@Override
					public void onPhotoTap(View arg0, float arg1, float arg2) {
						getActivity().finish();
					}
				});
		//长按图片弹出菜单
		mAttacher.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				showSavePicDialog();
				return true;
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	/**
	 * 把一个url的网络图片变成一个本地的BitMap
	 */
	class getBitMap extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			if (bitMap == null) {
				URL myFileUrl = null;
				InputStream is = null;
				try {
					myFileUrl = new URL(params[0]);
					HttpURLConnection conn = (HttpURLConnection) myFileUrl
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					is = conn.getInputStream();
					bitMap = Util.decodeSampledBitmapFromStream(is, 400, 300);
					// byte[] data = getBytes(is);
					// bitmap = BitmapFactory.decodeByteArray(data, 0,
					// data.length);
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
			}
			return bitMap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			progressBar.setVisibility(View.GONE);
			doSavePic(bitmap);
		}

	}

	private void doSavePic(Bitmap bitmap) {
		Bitmap bm = BitmapUtil.compBitmap(bitmap, 300, 400);
		if (bm == null || getActivity() == null
				|| getActivity().getContentResolver() == null) {
			ToastUtil.showShortToast("保存失败!");
			return;
		}
		final String savePath = MediaStore.Images.Media.insertImage(
				getActivity().getContentResolver(), bm, "title", null);
		if (!"".equals(savePath) && null != savePath) {
			ToastUtil.showShortToast("保存成功!");
			// 扫描到相册
			msc = new MediaScannerConnection(getActivity(),
					new MediaScannerConnectionClient() {
						public void onMediaScannerConnected() {
							msc.scanFile("/sdcard/image.jpg", "image/jpeg");
						}

						public void onScanCompleted(String path, Uri uri) {
							msc.disconnect();
						}
					});
		} else {
			ToastUtil.showShortToast("保存失败!");
		}
	}

	private void showSavePicDialog() {
		ArrayList<String> messageList = new ArrayList<String>();
		messageList.add(getString(R.string.save));
		messageList.add(getString(R.string.cancel));
		final MessageDialog messageDialog = new MessageDialog(getActivity(),
				messageList, new messageListener() {

					@Override
					public void positionchoose(int position) {
						if (position == 0) {
							if (Tool.isEmpty(mImageUrl)) {
								ToastUtil.showShortToast("图片路径为空，无法下载");
								return;
							} else if (mImageUrl.startsWith("file://")) {
								ToastUtil.showShortToast("图片已保存到SD卡");
							} else {
								new getBitMap().execute(mImageUrl);
							}
						}
					}
				});
		messageDialog.show();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			// 本地文件夹图片(com.meiku.dev/uploadFile我发送图片位置，com.meiku.dev/imgsCache
			// 来的消息图片缓存)
			progressBar.setVisibility(View.VISIBLE);
			if (!mImageUrl.startsWith("http")
					&& mImageUrl.contains("/com.meiku.dev/")) {
				mImageUrl = "file://" + mImageUrl;
			}
			ImageLoader.getInstance().displayImage(mImageUrl, mImageView,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// String message = null;
							// switch (failReason.getType()) {
							// case IO_ERROR:
							// message = "世界最遥远的距离就是没有网~";
							// break;
							// case DECODING_ERROR:
							// message = "图片无法显示~";
							// break;
							// case NETWORK_DENIED:
							// message = "网络好像有点问题，无法下载~";
							// break;
							// case OUT_OF_MEMORY:
							// message = "哎呀，图片太大了~";
							// break;
							// case UNKNOWN:
							// message = "出现了未知的错误~";
							// break;
							// }
							// Toast.makeText(getActivity(), message,
							// Toast.LENGTH_SHORT).show();
							if (mAttacher != null) {
								mAttacher.update();
							}
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressBar.setVisibility(View.GONE);
							if (mAttacher != null) {
								mAttacher.update();
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initValue() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (bitMap != null) {
			if (!bitMap.isRecycled()) {
				bitMap.recycle();
			}
			bitMap = null;
		}
		if (mAttacher != null) {
			mAttacher.cleanup();
			mAttacher = null;
		}
		System.gc();
	}

}
