package com.meiku.dev.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.Tool;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		mContext = context;
		mPosition = position;
		mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder getViewHolder(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mPosition = position;
			return viewHolder;
		}
	}

	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	public ViewHolder setText(int viewId, String text) {
		TextView textView = getView(viewId);
		if (textView != null) {
			if (TextUtils.isEmpty(text)) {
				textView.setText("");
			} else {
				textView.setText(text);
				textView.setVisibility(View.VISIBLE);
			}
		}
		return this;
	}

	public ViewHolder setImage(int viewId, int resId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resId);
		return this;
	}

	public ViewHolder setImage(int viewId, String imageUrl) {
		ImageView imageView = getView(viewId);
		if (imageView != null) {
			if (Tool.isEmpty(imageUrl)) {
				imageView.setImageResource(R.drawable.gg_icon);
				imageView.setTag(null);
			} else {
				if (imageView.getTag() != null
						&& imageUrl.equals((String) imageView.getTag())) {
					imageView.setImageDrawable(imageView.getDrawable());
				} else {
					BitmapUtils bitmapUtils = new BitmapUtils(mContext);
					bitmapUtils.configDefaultLoadFailedImage(R.drawable.gg_icon);
					bitmapUtils.display(imageView, imageUrl);
				}
				imageView.setTag(imageUrl);
			}
		}
		return this;
	}

	/**
	 * 制定图片大小，减少占用的缓存
	 * 
	 * @param viewId
	 * @param imageUrl
	 * @param imageWidth
	 * @param imageHeight
	 * @return
	 */
	// public ViewHolder setImageWithNewSize(int viewId, String imageUrl,
	// int imageWidth, int imageHeight) {
	// final ImageView imageView = getView(viewId);
	// if (imageView != null) {
	// if (Tool.isEmpty(imageUrl)) {
	// imageView.setImageResource(R.drawable.gg_icon);
	// imageView.setTag(null);
	// } else {
	// if (imageView.getTag() != null
	// && imageUrl.equals((String) imageView.getTag())) {
	// imageView.setImageDrawable(imageView.getDrawable());
	// } else {
	// ImageLoader.getInstance().loadImage(imageUrl,
	// new ImageSize(imageWidth, imageHeight),PictureUtil.normalImageOptions,
	// new SimpleImageLoadingListener() {
	//
	// @Override
	// public void onLoadingComplete(String imageUri,
	// View view, Bitmap loadedImage) {
	// super.onLoadingComplete(imageUri, view,
	// loadedImage);
	// imageView.setImageBitmap(loadedImage);
	// }
	//
	// });
	// }
	// imageView.setTag(imageUrl);
	// }
	// }
	// return this;
	// }

	// 本地网络都兼容
	public ViewHolder setImage(int viewId, String imageUrl, int flag) {
		ImageView imageView = getView(viewId);
		if (imageView != null) {
			if (Tool.isEmpty(imageUrl)) {
				if (imageView != null) {
					imageView.setImageResource(R.drawable.loaderror);
				}
			} else {
				BitmapUtils bitmapUtils = new BitmapUtils(mContext);
				bitmapUtils.configDefaultLoadFailedImage(R.drawable.gg_icon);
				bitmapUtils.display(imageView, imageUrl);
			}
		}
		return this;
	}
	
	public ViewHolder setImageWithNewSize(int viewId, String imageUrl,
			int imageWidth, int imageHeight) {
		final ImageView imageView = getView(viewId);
		if (imageView != null) {
			if (Tool.isEmpty(imageUrl)) {
				imageView.setImageResource(R.drawable.gg_icon);
				imageView.setTag(null);
			} else {
				if (imageView.getTag() != null
						&& imageUrl.equals((String) imageView.getTag())) {
					imageView.setImageDrawable(imageView.getDrawable());
				} else {
					ImageLoader.getInstance().loadImage(imageUrl,
							new ImageSize(imageWidth, imageHeight),PictureUtil.normalImageOptions,
							new SimpleImageLoadingListener() {//获取小尺寸图片显示

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view,
											loadedImage);
									imageView.setImageBitmap(loadedImage);
								}

							});
				}
				imageView.setTag(imageUrl);
			}
		}
		return this;
	}
}
