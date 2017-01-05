package com.meiku.dev.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;

public class DBManagerUtil {

	/**
	 * 检查DB版本，是否需要更新，更新后initDataBase
	 * 
	 * @param version
	 */
	public static boolean isNeedDownloadDb(String version) {
		// 当前版本号
		String cacheDbVersion = AppContext.getInstance().getAppDBVersion();
		if (!Tool.isEmpty(cacheDbVersion) && cacheDbVersion.equals(version)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检测是否导入过本地asset数据库，导入过的就不需要再次导入
	 */
	public static boolean checkAssetDBHasImported() {
		return (Boolean) PreferHelper.getSharedParam(ConstantKey.DB_ASSET_IMPORTED, false);
	}
	
	/**
	 * 标记本地asset数据库已经导入
	 */
	private static void setAssetDBImported(boolean hasImport) {
		 PreferHelper.setSharedParam(ConstantKey.DB_ASSET_IMPORTED, hasImport);
	}
	
	/**
	 * 下载DB,并导入
	 * 
	 * @param downLoadUrl
	 *            DB下载路径
	 */
	public static void downLoadDBAndImport(String downLoadUrl,
			final String newversion, final ImportDBListener listener) {
		HttpUtils httpUtil = new HttpUtils();
		final String fileDestPath = SdcardUtil.getPath() + FileConstant.DB_PATH
				+ FileConstant.APP_DB_NAME;
		HttpHandler handler = httpUtil.download(downLoadUrl, fileDestPath,
				false, true, new RequestCallBack<File>() {
					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						asyncImportWebNewDB(fileDestPath, newversion,listener);
					}

					@Override
					public void onFailure(HttpException e, String s) {
						listener.importResult(false);//返回失败,统一导入本地asset的DB
					}
				});
	}

	/**
	 * 把下载后的数据库导入给APP，已经initDataBase
	 * 
	 * @param httpFilePath
	 * @param newVersion
	 * @param listener 
	 */
	private static void asyncImportWebNewDB(String httpFilePath,
			final String newVersion, final ImportDBListener listener) {
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				String path = params[0];
				return imporDatabase(path);
			}

			@Override
			protected void onPostExecute(Boolean aBoolean) {
				super.onPostExecute(aBoolean);
				if (aBoolean) {
					AppContext.getInstance().setAppDBVersion(newVersion);
				}
				listener.importResult(aBoolean);
			}
		}.execute(httpFilePath);
	}

	/**
	 * 导入本地数据库
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean imporDatabase(String httpFilePath) {
		// 存放数据库的目录
		File dir = new File(FileConstant.LOCALDB_PATH);
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 数据库文件
		File file = new File(dir, FileConstant.DB_NAME);
		try {
			if (file.exists()) {
			 	file.delete();
			}
			file.createNewFile();
			// 加载需要导入的数据库
			InputStream is = new BufferedInputStream(new FileInputStream(
					new File(httpFilePath)));
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffere = new byte[is.available()];
			is.read(buffere);
			fos.write(buffere);
			is.close();
			fos.close();
			LogUtil.d("hl", "imporDatabase");
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 导入asset数据库
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean imporAssetDatabase() {
		// 存放数据库的目录
		File dir = new File(FileConstant.LOCALDB_PATH);
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 数据库文件
		File file = new File(dir, FileConstant.DB_NAME);
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			// 加载需要导入的数据库
			AssetManager am = MrrckApplication.getInstance().getAssets();
			// 得到数据库的输入流
			InputStream is = am.open(FileConstant.APP_DB_NAME);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffere = new byte[is.available()];
			is.read(buffere);
			fos.write(buffere);
			is.close();
			fos.close();
			LogUtil.d("hl", "imporAssetDatabase");
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 开线程从asset导入数据库，结束后走进入主页流程
	 * 
	 * @param httpFilePath
	 */
	public static void asyncImportAssetDatabase(final ImportDBListener listener) {
		new AsyncTask<String, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(String... params) {
				return imporAssetDatabase();
			}

			@Override
			protected void onPostExecute(Boolean aBoolean) {
				super.onPostExecute(aBoolean);
				setAssetDBImported(aBoolean);
				listener.importResult(aBoolean);
			}
		}.execute();
	}

	public interface ImportDBListener {
		void importResult(boolean isSuccess);
	}
}
