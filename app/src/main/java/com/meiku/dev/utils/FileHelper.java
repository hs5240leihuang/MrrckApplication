package com.meiku.dev.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Decoder.BASE64Decoder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.utils.SdcardUtil.SDNotEnouchSpaceException;
import com.meiku.dev.utils.SdcardUtil.SDUnavailableException;

/******************************************************************
 * 文件名称 : FileHelper.java 文件描述 : 主要是对文件系统的一些操作，包括文件的读写操作,主要是完成下载中的保存下载文件的功能
 ******************************************************************/
public class FileHelper {

	/**
	 * 当出错的时候返回的字节数为-1
	 */
	public final static int ERROR = -1;

	/**
	 * 当没有写入任何数据的时候返回0
	 */
	public final static int NONE = 0;

	/**
	 * 该变量是从sd卡读取文件时默认的字符缓冲区的大小
	 */
	private final static int MAX_LENTH = 1024 * 32;

	/**
	 * sd卡所在的区块位置
	 */
	final static int SDCARD_SYSTEM = 4;

	/**
	 * 在程序第一次安装的时候就在sd卡的主目录下新建一个系统所需的目录
	 * 
	 * @return 创建目录是否成功
	 */
	public static boolean makeDir() {
		// // 首次启动删除imohooebook文件夹
		// if (FileConstant.isFirstStartUp) {
		// FileHelper.delAllFile(FileConstant.SD_PATH + FileConstant.APP_PATH);
		// }

		boolean isComplete = false;
		// 创建DB路径
		File file = new File(FileConstant.SD_PATH + FileConstant.DB_PATH);
		if (!isFileExist(file)) {
			isComplete = file.mkdirs();
		}

		// 创建段子图片缓存目录
		file = new File(FileConstant.SD_PATH + FileConstant.UPLOAD_PHOTO_PATH);
		if (!isFileExist(file)) {
			isComplete = file.mkdirs();
		}

		// 创建视频缓存目录
		file = new File(FileConstant.SD_PATH + FileConstant.UPLOAD_VIDEO_PATH);
		if (!isFileExist(file)) {
			isComplete = file.mkdirs();
		}

		// 创建音频缓存目录
		file = new File(FileConstant.SD_PATH + FileConstant.UPLOAD_AUDIO_PATH);
		if (!isFileExist(file)) {
			isComplete = file.mkdirs();
		}

		// 创建日志目录
		file = new File(FileConstant.SD_PATH + FileConstant.LOG_PATH);
		if (!isFileExist(file)) {
			isComplete = file.mkdirs();
		}

		// 创建赛事广告图目录
		file = new File(FileConstant.SD_PATH + FileConstant.MATCHAD_PATH);
		if (!isFileExist(file)) {
			isComplete = file.mkdirs();
		}
		
		file = null;

		return isComplete;
	}

	/**
	 * 创建下载文件路径
	 * 
	 * @param filePath
	 *            文件名
	 * @return 生成的文件
	 */
	public static File createDownloadFile(String filePath) {
		return createFile(filePath);
	}

	/**
	 * 创建文件路径
	 * 
	 * @param filePath
	 * @return
	 */
	public static File createResFile(String filePath) {
		return createFile(filePath);
	}

	/**
	 * 通过提供的文件名在默认路径下生成文件
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 生成的文件
	 */
	public static File createFile(String fileName) {
		File file = new File(fileName);
		if (!isFileExist(file)) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// LogX.trace(e.getMessage(), " fail");
				if (fileName.lastIndexOf("/") > -1) {
					String fatherRoot = fileName.substring(0,
							fileName.lastIndexOf("/"));
					File filetemp = new File(fatherRoot);
					filetemp.mkdirs();
					try {
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return file;
	}

	/**
	 * 将从下载管理那里获取来的数据流写入文件中
	 * 
	 * @param io
	 *            从下载管理那里获取来的io流
	 * @param fileName
	 *            需要存储的文件的路径和名称
	 * @return 总共存储成功的字节数
	 * @throws SDNotEnouchSpaceException
	 * @throws SDUnavailableException
	 */
	public static int writeFile(byte[] io, String fileName)
			throws SDUnavailableException, SDNotEnouchSpaceException {
		int length = NONE;
		if (io != null) {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(createFile(fileName), "rw");
				file.seek(file.length());
				file.write(io);
			} catch (IOException e) {
				// LogX.trace(e.getMessage(), " fail");
				SdcardUtil.checkSD(io);
				// 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
				return ERROR;
			} finally {
				try {
					file.close();
					file = null;
				} catch (IOException e) {
					// LogX.trace(e.getMessage(), " fail");
				}
				length = io.length;
				io = null;
			}
		}
		return length;
	}

	/**
	 * 将从网络获取来的数据流写入文件中
	 * 
	 * @param file
	 *            从网络获取来的io流
	 * @param io
	 *            需要存储的文件的名称
	 * @return 总共存储成功的字节数
	 * @throws SDNotEnouchSpaceException
	 * @throws SDUnavailableException
	 */
	public static int writeFile(RandomAccessFile file, byte[] io)
			throws SDUnavailableException, SDNotEnouchSpaceException {
		int length = NONE;

		if (io != null) {
			if (file != null) {
				try {
					file.seek(file.length());
					file.write(io);
				} catch (IOException e) {
					// LogX.trace(e.getMessage(), " fail");
					SdcardUtil.checkSD(io);
					// 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
					return ERROR;
				}
				length = io.length;
				io = null;
			} else {
				SdcardUtil.checkSD(io);
				// 如果出现异常，返回的字节数为-1，表示出现了异常，没有写入成功
				return ERROR;
			}

		}

		return length;
	}

	public static void writeFile2(RandomAccessFile file, byte[] io)
			throws Exception {
		file.seek(file.length());
		file.write(io);
	}

	/**
	 * 从本地文件中读取文件信息
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 文件中的信息
	 */
	public static String readFile(String fileName) throws Error {
		RandomAccessFile file = null;
		byte[] buffer = new byte[MAX_LENTH];
		StringBuffer content = new StringBuffer();
		try {
			file = new RandomAccessFile(createFile(fileName), "rw");
			int length = 0;
			while ((length = file.read(buffer)) != -1) {
				if (length == MAX_LENTH)
					content.append(new String(buffer));
				else {
					byte[] tempByte = new byte[length];
					for (int i = 0; i < tempByte.length; i++) {
						tempByte[i] = buffer[i];
					}
					content.append(new String(tempByte));
					tempByte = null;
				}
			}
		} catch (IOException e) {
			// LogX.trace(e.getMessage(), " fail");
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				// LogX.trace(e.getMessage(), " fail");
			}
		}
		return content.toString();
	}

	/**
	 * 从本地文件中读取文件信息
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 文件中的信息
	 */
	public static String readFileToByte(String fileName) {
		// String encodeType = EncodingDetect.getJavaEncode(fileName);
		RandomAccessFile file = null;
		byte[] buffer = new byte[MAX_LENTH];
		StringBuffer content = new StringBuffer();
		try {
			file = new RandomAccessFile(createFile(fileName), "rw");
			while (file.read(buffer) != -1) {
				content.append(new String(buffer));
			}
		} catch (IOException e) {
			// LogX.trace(e.getMessage(), " fail");
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				// LogX.trace(e.getMessage(), " fail");
			}
		}
		return content.toString();
	}

	/**
	 * @param filename1
	 * @param filename2
	 */
	public static void CopyFile(String filename1, String filename2) {
		try {
			FileOutputStream outStream;
			outStream = new FileOutputStream(filename2);
			// 源文件
			File file = new File(filename1);
			// 读入源文件
			FileInputStream inStream = new FileInputStream(file);
			// 文件头4096字节,每站120字节,必须固定空间,否则无效.
			byte[] buffer = new byte[(int) file.length()];
			inStream.read(buffer);
			outStream.write(buffer, 0, buffer.length);
			inStream.close();
			outStream.flush();
			outStream.close();
			outStream = null;
			// 新文件
			outStream = new FileOutputStream(filename2);
			outStream.write(buffer, 0, buffer.length);
			inStream.close();
			outStream.flush();
			outStream.close();
			outStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝地图资源到data
	 * 
	 * @param inputStream
	 * @param path
	 */
	public static void cpAssets(InputStream inputStream, String path) {
		try {
			RandomAccessFile ok = new RandomAccessFile(
					FileHelper.createFile(path), "rw");
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = inputStream.read(b)) != -1) {
				ok.write(b, 0, n);
			}
			inputStream.close();
			ok.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 是否存在此文件
	 * 
	 * @param file
	 *            判断是否存在的文件
	 * @return 存在返回true，否则返回false
	 */
	public static boolean isFileExist(final File file) {
		boolean isExist = false;
		// 在无SD卡时file会为空
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			isExist = true;
		} else {
			isExist = false;
		}
		return isExist;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            String 文件夹路径及名称 如c:/fqf
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 删除路径指向的文件
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return true删除成功，false删除失败
	 */
	public static boolean deleteFile(String fileName) {
		boolean isComplete = false;
		if (fileName == null) {
			return isComplete;
		}

		File file = new File(fileName);

		if (file != null && file.exists()) {
			isComplete = file.delete();
		} else {
			isComplete = true;
		}
		return isComplete;
	}

	/**
	 * 本地文件大小
	 * 
	 * @param fileName
	 *            文件的名称
	 * @return 返回文件的大小
	 */
	public static long getLocalFileSize(final String fileName) {
		File file = createFile(fileName);
		long length = 0;
		if (isFileExist(file)) {
			length = file.length();
		}
		return length;
	}

	/**
	 * 获取文件编码方式
	 * 
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			// bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					// 单独出现BF以下的，也算是GBK
					if (0x80 <= read && read <= 0xBF)
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF)// 双字节 (0xC0 - 0xDF)
							// (0x80 -
							// 0xBF),也可能在GB编码内
							continue;
						else
							break;
						// 也有可能出错，但是几率较小
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
			bis = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
	 * 
	 * @param res
	 *            原字符串
	 * @param filePath
	 *            文件路径
	 * @return 成功标记
	 */
	public static boolean string2File(String res, String filePath) {

		// try {
		// res = new String(res.getBytes("gbk"), "GBK");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// Log.e("1111111111111", filePath);
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[16384]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 写图片二级缓存 <功能详细描述>
	 * 
	 * @param data
	 * @param path
	 * @see [类、类#方法、类#成员]
	 */
	public static void writeImageCache(byte[] data, String path) {
		if (data != null) {
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(createFile(path), "rw");
				file.write(data);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				file = null;
			}

		}
	}

	/**
	 * 取得文件夹大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getDirSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getDirSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}

		return size;
	}

	/**
	 * 获取文件纯字节
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] getFileByte(String fileName) {
		RandomAccessFile file = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[MAX_LENTH];
		int len = 0;
		byte[] bytes;
		try {
			file = new RandomAccessFile(createFile(fileName), "rw");
			while ((len = file.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			// LogX.trace(e.getMessage(), " fail");
		} finally {
			bytes = baos.toByteArray();
			try {
				file.close();
			} catch (IOException e) {
				// LogX.trace(e.getMessage(), " fail");
			}
		}
		return bytes;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize2(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 把图片压缩到200K
	 * 
	 * @param oldpath
	 *            压缩前的图片路径
	 * @param newPath
	 *            压缩后的图片路径
	 * @return
	 */
	public static File compressFile(String oldpath, String newPath) {
		Bitmap compressBitmap = BitmapUtil.decodeFile(oldpath);
		Bitmap newBitmap = BitmapUtil.ratingImage(oldpath, compressBitmap);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		newBitmap.compress(CompressFormat.PNG, 100, os);
		byte[] bytes = os.toByteArray();

		File file = null;
		try {
			file = getFileFromBytes(bytes, newPath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (newBitmap != null) {
				if (!newBitmap.isRecycled()) {
					newBitmap.recycle();
				}
				newBitmap = null;
			}
			if (compressBitmap != null) {
				if (!compressBitmap.isRecycled()) {
					compressBitmap.recycle();
				}
				compressBitmap = null;
			}
		}
		return file;
	}

	/**
	 * 把字节数组保存为一个文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			// log.error("helper:get file from byte process error!");
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// log.error("helper:get file from byte process error!");
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	// 保存到sdcard
	public static void savePic(Bitmap b, String strFileName) {
		if (null == b)
			return;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	/**
	 * 语音聊天,录音保存的地址
	 * 
	 * @return
	 */
	public static String getRecordPathByCurrentTime() {
		return new File(FileConstant.SD_PATH + FileConstant.UPLOAD_AUDIO_PATH,
				XmppConstant.MSG_FILE_VOICE + "_" + System.currentTimeMillis())
				.getAbsolutePath()
				+ ".amr";
	}

	/**
	 * 图片聊天,图片保存的地址
	 * 
	 * @return
	 */
	public static String getImgPathByCurrentTime() {
		return new File(FileConstant.CacheFilePath, XmppConstant.MSG_FILE_IMG
				+ "_" + System.currentTimeMillis()).getAbsolutePath()
				+ ".png";
	}

	/**
	 * 图片聊天,图片保存的地址
	 * 
	 * @return
	 */
	public static String getVideoPathByCurrentTime() {
		return new File(FileConstant.CacheFilePath, XmppConstant.MSG_FILE_VIDEO
				+ "_" + System.currentTimeMillis()).getAbsolutePath()
				+ ".mp4";
	}

	public static String GetFileToStr(String imagDir) {// 将文件转化为字节数组字符串，并对其进行Base64编码处理
		if (FileHelper.isFileExist(new File(imagDir))) {

			InputStream in = null;
			byte[] data = null;
			// 读取图片字节数组
			try {
				in = new FileInputStream(imagDir);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(data);// 返回Base64编码过的字节数组字符串
		} else {
			return "";
		}
	}

	// base64字符串转化成音频
	public static String GenerateVoic(String voicStr, String realdir) { // 对字节数组字符串进行Base64解码并
		if (voicStr == null) // 图像数据为空
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(voicStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(realdir);
			out.write(b);
			out.flush();
			out.close();
			return realdir;
		} catch (Exception e) {
			return null;
		}
	}

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 public static File createTmpFile(Context context){

	        String state = Environment.getExternalStorageState();
	        if(state.equals(Environment.MEDIA_MOUNTED)){
	            // 已挂载
	            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	            String fileName = "multi_image_"+timeStamp+"";
	            File tmpFile = new File(pic, fileName+".jpg");
	            return tmpFile;
	        }else{
	            File cacheDir = context.getCacheDir();
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	            String fileName = "multi_image_"+timeStamp+"";
	            File tmpFile = new File(cacheDir, fileName+".jpg");
	            return tmpFile;
	        }

	    }
	 
	   private static final String TAG = "FileUtil";

	    public static boolean hasExtentsion(String filename) {
	        int dot = filename.lastIndexOf('.');
	        if ((dot > -1) && (dot < (filename.length() - 1))) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	    // 获取文件扩展名
	    public static String getExtensionName(String filename) {
	        if ((filename != null) && (filename.length() > 0)) {
	            int dot = filename.lastIndexOf('.');
	            if ((dot > -1) && (dot < (filename.length() - 1))) {
	                return filename.substring(dot + 1);
	            }
	        }
	        return "";
	    }

	    // 获取文件名
	    public static String getFileNameFromPath(String filepath) {
	        if ((filepath != null) && (filepath.length() > 0)) {
	            int sep = filepath.lastIndexOf('/');
	            if ((sep > -1) && (sep < filepath.length() - 1)) {
	                return filepath.substring(sep + 1);
	            }
	        }
	        return filepath;
	    }

	    // 获取不带扩展名的文件名
	    public static String getFileNameNoEx(String filename) {
	        if ((filename != null) && (filename.length() > 0)) {
	            int dot = filename.lastIndexOf('.');
	            if ((dot > -1) && (dot < (filename.length()))) {
	                return filename.substring(0, dot);
	            }
	        }
	        return filename;
	    }

	    public static String getMimeType(String filePath) {
	        if (TextUtils.isEmpty(filePath)) {
	            return "";
	        }
	        String type = null;
	        String extension = getExtensionName(filePath.toLowerCase());
	        if (!TextUtils.isEmpty(extension)) {
	            MimeTypeMap mime = MimeTypeMap.getSingleton();
	            type = mime.getMimeTypeFromExtension(extension);
	        }
	        Log.i(TAG, "url:" + filePath + " " + "type:" + type);

	        // FIXME
	        if (Tool.isEmpty(type) && filePath.endsWith("aac")) {
	            type = "audio/aac";
	        }

	        return type;
	    }

	    public enum SizeUnit {
	        Byte,
	        KB,
	        MB,
	        GB,
	        TB,
	        Auto,
	    }

	    public static String formatFileSize(long size) {
	        return formatFileSize(size, SizeUnit.Auto);
	    }

	    public static String formatFileSize(long size, SizeUnit unit) {
	        if (size < 0) {
	            return "--";
	        }

	        final double KB = 1024;
	        final double MB = KB * 1024;
	        final double GB = MB * 1024;
	        final double TB = GB * 1024;
	        if (unit == SizeUnit.Auto) {
	            if (size < KB) {
	                unit = SizeUnit.Byte;
	            } else if (size < MB) {
	                unit = SizeUnit.KB;
	            } else if (size < GB) {
	                unit = SizeUnit.MB;
	            } else if (size < TB) {
	                unit = SizeUnit.GB;
	            } else {
	                unit = SizeUnit.TB;
	            }
	        }

	        switch (unit) {
	            case Byte:
	                return size + "B";
	            case KB:
	                return String.format(Locale.US, "%.2fKB", size / KB);
	            case MB:
	                return String.format(Locale.US, "%.2fMB", size / MB);
	            case GB:
	                return String.format(Locale.US, "%.2fGB", size / GB);
	            case TB:
	                return String.format(Locale.US, "%.2fPB", size / TB);
	            default:
	                return size + "B";
	        }
	    }
}
