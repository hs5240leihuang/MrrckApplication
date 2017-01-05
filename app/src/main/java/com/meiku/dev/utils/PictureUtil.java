package com.meiku.dev.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.meiku.dev.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck
 * 
 * 类描述：图片压缩工具 类名称：com.meiku.dev.utils.PictureUtil 创建人：admin 创建时间：2015-10-30
 * 上午10:22:27
 * 
 * @version V3.0
 */
public class PictureUtil {
	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public static File getAlbumDir() {
		File dir = new File(SdcardUtil.getPath()
				+ FileConstant.UPLOAD_PHOTO_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 把相册图片压缩另存为APP缓存路径
	 * 
	 * @param path
	 * @return
	 */
	public static String save(String path) {
		String newPath = "";
		try {
			// newPath = formatFilePath(path);

			File f = new File(path);
			newPath = PictureUtil.getAlbumDir() + "/" + f.getName();

			newPath = compressImage(path, newPath);
			// Bitmap bm = PictureUtil.getSmallBitmap(path);
			// // Bitmap bm;
			// // if (Build.MANUFACTURER.contains("samsung")) {
			// // Matrix matrix = new Matrix();
			// // matrix.setRotate(90);
			// // bm = Bitmap.createBitmap(tempBitMap, 0, 0,
			// // tempBitMap.getWidth(), tempBitMap.getHeight(), matrix,
			// // true);
			// // } else {
			// // bm = tempBitMap;
			// // }
			//
			// // Bitmap bm = PictureUtil.getSmallBitmap(path);
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			// FileOutputStream fos = new FileOutputStream(new File(newPath));
			// int options = 100;
			// // 如果大于80kb则再次压缩,最多压缩三次
			// while (baos.toByteArray().length / 1024 > 80 && options != 10) {
			// // 清空baos
			// baos.reset();
			// // 这里压缩options%，把压缩后的数据存放到baos中
			// bm.compress(Bitmap.CompressFormat.PNG, options, baos);
			// options -= 30;
			// }
			// fos.write(baos.toByteArray());
			// fos.close();
			// baos.close();
			// // bm.compress(Bitmap.CompressFormat.JPEG, 70, fos);
			// Log.e("PictureUtil", "Compress OK!");

		} catch (Exception e) {
			Log.e("PictureUtil", "error", e);
		}
		return newPath;
	}

	/**
	 * 将bitmap保存在本APP的固定路径
	 * 
	 * @param bitName
	 * @param mBitmap
	 * @throws IOException
	 */
	public static String saveMyBitmap(String bitName, Bitmap mBitmap) {
		File cutdir = new File(PictureUtil.getAlbumDir() + "/takephoto/");
		if (!cutdir.exists()) {
			cutdir.mkdirs();
		}

		String filePath = cutdir + (bitName + ".png");
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null; // 字节数组输出流
		try {
			baos = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组

			File file = new File(filePath);
			// 将字节数组写入到刚创建的图片文件中
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}

	// 临时方法
	// 保存新文件路径
	public static String formatFilePath(String filePath) {
		String newFilePath = null;
		String name = null;
		String endTag = null;
		int start = filePath.lastIndexOf("/");
		int end = filePath.lastIndexOf(".");
		if (start != -1 && end != -1) {
			name = filePath.substring(start + 1, end);
			endTag = filePath.substring(end);
		}
		newFilePath = PictureUtil.getAlbumDir() + "/" + name + "_1" + endTag;
		return newFilePath;
	}

	/**
	 * 获取指定路径下的图片的指定大小的缩略图 getImageThumbnail
	 * 
	 * @return Bitmap
	 * @throws
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (Exception e) {
		}
		return file;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;

	}

	/**
	 * 旋转图片一定角度 rotaingImageView
	 * 
	 * @return Bitmap
	 * @throws
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 将图片变为圆角
	 * 
	 * @param bitmap
	 *            原Bitmap图片
	 * @param pixels
	 *            图片圆角的弧度(单位:像素(px))
	 * @return 带有圆角的图片(Bitmap 类型)
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 将图片转化为圆形头像
	 * 
	 * @throws
	 * @Title: toRoundBitmap
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}

	public static String simpleCompressImage(String path, String newPath) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(newPath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(outputStream);
		}
		recycle(bitmap);
		return newPath;
	}

	public static void recycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		System.gc();
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
		}
	}

	public static DisplayImageOptions normalImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.sichat_placehold_icon)
			.showImageForEmptyUri(R.drawable.sichat_placehold_icon)
			.showImageOnFail(R.drawable.sichat_placehold_icon)
			.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)

			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true)
			.build();

//	public static void displayImageCacheElseNetwork(ImageView imageView,
//			String path, String url) {
//		ImageLoader imageLoader = ImageLoader.getInstance();
//		if (path != null) {
//			File file = new File(path);
//			if (file.exists()) {
//				imageLoader.displayImage("file://" + path, imageView,
//						normalImageOptions);
//				return;
//			}
//		}
//		imageLoader.displayImage(url, imageView, normalImageOptions);
//	}

	public static String compressImage(String path, String newPath) {
		int degree = readPictureDegree(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int inSampleSize = 1;
		int maxSize = 1500;
		if (options.outWidth > maxSize || options.outHeight > maxSize) {
			int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
			int heightScale = (int) Math
					.ceil(options.outHeight * 1.0 / maxSize);
			inSampleSize = Math.max(widthScale, heightScale);
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = inSampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		bitmap = rotaingImageView(degree, bitmap);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int newW = w;
		int newH = h;
		if (w > maxSize || h > maxSize) {
			if (w > h) {
				newW = maxSize;
				newH = (int) (newW * h * 1.0 / w);
			} else {
				newH = maxSize;
				newW = (int) (newH * w * 1.0 / h);
			}
		}
		Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
		// if (Build.MANUFACTURER.contains("samsung")) {
		// Matrix matrix = new Matrix();
		// matrix.setRotate(90);
		// newBitmap = Bitmap.createBitmap(newBitmap, 0, 0,
		// newBitmap.getWidth(), newBitmap.getHeight(), matrix, true);
		// }
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(newPath);
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
		} catch (FileNotFoundException e) {
		} finally {
			closeQuietly(outputStream);
		}
		recycle(newBitmap);
		recycle(bitmap);
		return newPath;
	}

	/**
	 * 
	 * 将图片按照某个角度进行旋转
	 * 
	 * 
	 * 
	 * @param bm
	 * 
	 *            需要旋转的图片
	 * 
	 * @param degree
	 * 
	 *            旋转角度
	 * 
	 * @return 旋转后的图片
	 */

	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {

		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;

	}

	/**
	 * 获取项目drawable路径下的图片
	 * 
	 * @param strPic
	 * @param context
	 * @return
	 */
	public static Bitmap getDrawablePic(String strPic, Context context) {
		int indentify = context.getResources().getIdentifier(
				context.getPackageName() + ":drawable/" + strPic, null, null);
		return BitmapFactory.decodeResource(context.getResources(), indentify);
	}

}
