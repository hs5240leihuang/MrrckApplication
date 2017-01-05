package com.nereo.multi_image_selector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 文件夹Adapter Created by Nereo on 2015/4/7.
 */
public class FolderAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	private List<Folder> mFolders = new ArrayList<Folder>();

	int mImageSize;

	int lastSelected = 0;
	private int type;// 0图片，1视频

	public FolderAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageSize = mContext.getResources().getDimensionPixelOffset(
				R.dimen.folder_cover_size);
	}

	public FolderAdapter(Context context, int type) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageSize = mContext.getResources().getDimensionPixelOffset(
				R.dimen.folder_cover_size);
		this.type = type;
	}

	/**
	 * 设置数据集
	 * 
	 * @param folders
	 */
	public void setData(List<Folder> folders) {
		if (folders != null && folders.size() > 0) {
			mFolders = folders;
		} else {
			mFolders.clear();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFolders.size() + 1;
	}

	@Override
	public Folder getItem(int i) {
		if (i == 0)
			return null;
		return mFolders.get(i - 1);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.list_item_folder, viewGroup,
					false);
			holder = new ViewHolder(view);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (holder != null) {
			if (i == 0) {
				if (type == 1) {
					holder.name.setText("所有视频");
					holder.size.setText(getTotalImageSize() + "个");
					if (mFolders.size() > 0) {
						Folder f = mFolders.get(0);
						holder.cover.setImageBitmap(Util.getVideoThumbnail(
								f.cover.path, 96, 96,
								Images.Thumbnails.MICRO_KIND));
					}
				} else {
					holder.name.setText("所有图片");
					holder.size.setText(getTotalImageSize() + "张");
					if (mFolders.size() > 0) {
						Folder f = mFolders.get(0);
						BitmapUtils bitmapUtils = new BitmapUtils(mContext);
						bitmapUtils.display(holder.cover, f.cover.path);
					}
				}
			} else {
				holder.bindData(getItem(i));
			}
			if (lastSelected == i) {
				holder.indicator.setVisibility(View.VISIBLE);
			} else {
				holder.indicator.setVisibility(View.INVISIBLE);
			}
		}
		return view;
	}

	private int getTotalImageSize() {
		int result = 0;
		if (mFolders != null && mFolders.size() > 0) {
			for (Folder f : mFolders) {
				result += f.images.size();
			}
		}
		return result;
	}

	public void setSelectIndex(int i) {
		if (lastSelected == i)
			return;

		lastSelected = i;
		notifyDataSetChanged();
	}

	public int getSelectIndex() {
		return lastSelected;
	}

	class ViewHolder {
		ImageView cover;
		TextView name;
		TextView size;
		ImageView indicator;

		ViewHolder(View view) {
			cover = (ImageView) view.findViewById(R.id.cover);
			name = (TextView) view.findViewById(R.id.name);
			size = (TextView) view.findViewById(R.id.size);
			indicator = (ImageView) view.findViewById(R.id.indicator);
			view.setTag(this);
		}

		void bindData(Folder data) {
			name.setText(data.name);
			if (type == 1) {
				size.setText(data.images.size() + "个");
				cover.setImageBitmap(Util.getVideoThumbnail(data.cover.path,
						96, 96, Images.Thumbnails.MICRO_KIND));
			} else {
				size.setText(data.images.size() + "张");
				// 显示图片
				// BitmapUtils bitmapUtils = new BitmapUtils(mContext);
				// bitmapUtils.display(cover, data.cover.path);
				ImageLoader.getInstance().displayImage(
						"file://" + data.cover.path, cover,
						PictureUtil.normalImageOptions);
			}
		}
	}

}
