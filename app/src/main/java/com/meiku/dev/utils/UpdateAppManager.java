package com.meiku.dev.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UpdateBean;
import com.meiku.dev.bean.UpdateBean.UpdateReq;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.volleyextend.StringJsonRequest;

/**
 * 版本升级
 * 
 */
public class UpdateAppManager {
	// 文件分隔符
	private static final String FILE_SEPARATOR = "/";
	// 外存sdcard存放路径
	private static final String FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ FILE_SEPARATOR
			+ "autoupdate"
			+ FILE_SEPARATOR;
	// 下载应用存放全路径
	private static final String FILE_NAME = FILE_PATH + "autoupdate.apk";
	// 更新应用版本标记
	private static final int UPDARE_TOKEN = 0x29;
	// 准备安装新版本应用标记
	private static final int INSTALL_TOKEN = 0x31;

	private Context context;
	private String message = "检测到有新版本发布，建议您更新！";

	// 下载应用的对话框
	// private Dialog dialog;
	// 下载应用的进度条
	// private ProgressBar progressBar;
	// 进度条的当前刻度值
	private int curProgress;
	// 用户是否取消下载
	private boolean isCancel;

	// 下载地址
	private String updateUrl = "";
	// 是否强制更新
	private boolean isForceUpdate = false;

	private OnUpdateResultListener mResultListener;
	private int fileLength;
	// private TextView pro;
	private long readedLength;

	public interface OnUpdateResultListener {
		public void onNotUpdate();
		public void onCancleClick();
	}

	public void setOnUpdateResultListener(OnUpdateResultListener listener) {
		this.mResultListener = listener;
	}

	public UpdateAppManager(Context context) {
		this.context = context;
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDARE_TOKEN:
				// progressDialog.getmLeafLoadingView().setProgress(curProgress);
				// progressDialog.getTvPro().setText(
				// "当前: " + curProgress + "% ( "
				// + Util.FormetFileSize(readedLength) + " / "
				// + Util.FormetFileSize(fileLength) + " )");
				downLoadDialog.getProgressBar().setProgress(curProgress);
				downLoadDialog.getTvPro().setText(curProgress + "%");
				break;

			case INSTALL_TOKEN:
				installApp();
				break;
			}
		}
	};
	private DownloadDialog downLoadDialog;

	// private ProgressDialog progressDialog;

	public void checkUpdateInfo(UpdateBean entity) {
		// UpdateBean.UpdateReq req = (UpdateReq) JsonUtil.jsonToObj(
		// UpdateBean.UpdateReq.class, basejson.getBody()
		// .toString());
		// UpdateBean entity = req.versionEntity;

		if (entity != null) {
			int forceUpdate = entity.forceUpdate;
			int currentVersion = entity.currentVersion;

			PackageManager pm = context.getPackageManager();
			try {
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				int versionCode = pi.versionCode;

				if (currentVersion != versionCode) {
					updateUrl = entity.url;

					LogUtil.d("hl", "updateUrl=" + updateUrl);
					if (forceUpdate == 1) { // 强制更新
						isForceUpdate = true;
						showDownloadDialog();
					} else { // 不强制更新
						String remark = entity.remark;
						if (Tool.isEmpty(remark)) {
							remark = message;
						}
						showNoticeDialog(remark);
					}
				} else {
					mResultListener.onNotUpdate();
				}
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
				mResultListener.onNotUpdate();
			}
		}
	}

	/**
	 * 检测应用更新信息
	 */
	public void checkUpdateInfo() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("devType", ConstantKey.DEV_TYPE);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_APP_VERSION));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		StringJsonRequest request = new StringJsonRequest(AppConfig.DOMAIN
				+ AppConfig.PUBLIC_REQUEST_MAPPING, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				ReqBase basejson = (ReqBase) JsonUtil.jsonToObj(ReqBase.class,
						arg0);
				ReqHead head = basejson.getHeader();
				if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
					UpdateBean.UpdateReq req = (UpdateReq) JsonUtil.jsonToObj(
							UpdateBean.UpdateReq.class, basejson.getBody()
									.toString());
					UpdateBean entity = req.versionEntity;

					LogUtil.d("hl", "版本信息：" + basejson.getBody().toString());
					if (entity != null) {
						int forceUpdate = entity.forceUpdate;
						int currentVersion = entity.currentVersion;

						PackageManager pm = context.getPackageManager();
						try {
							PackageInfo pi = pm.getPackageInfo(
									context.getPackageName(), 0);
							int versionCode = pi.versionCode;

							if (currentVersion != versionCode) {
								updateUrl = entity.url;

								LogUtil.d("hl", "updateUrl=" + updateUrl);
								if (forceUpdate == 1) { // 强制更新
									isForceUpdate = true;
									showDownloadDialog();
								} else { // 不强制更新
									String remark = entity.remark;
									if (Tool.isEmpty(remark)) {
										remark = message;
									}
									showNoticeDialog(remark);
								}
							} else {
								mResultListener.onNotUpdate();
							}
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
							mResultListener.onNotUpdate();
						}
					}

				} else {
					mResultListener.onNotUpdate();
				}
			}
		}, JsonUtil.objToJson(req), new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				mResultListener.onNotUpdate();
			}
		});
		Volley.newRequestQueue(context).add(request);
	}

	/**
	 * 显示提示更新对话框
	 * 
	 * @param remark
	 */
	private void showNoticeDialog(String remark) {
		final CommonDialog commonDialog = new CommonDialog(context, "版本更新",
				remark, "确定", "取消");
		commonDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void doConfirm() {
				commonDialog.dismiss();
				showDownloadDialog();
			}

			@Override
			public void doCancel() {
				commonDialog.dismiss();
				if (mResultListener != null) {
					mResultListener.onCancleClick();
				}
			}
		});
		commonDialog.setCancelable(false);
		commonDialog.show();
		commonDialog.setContentLeft();
	}

	/**
	 * 显示下载进度对话框
	 */
	private void showDownloadDialog() {
		// progressDialog = new ProgressDialog(context, isForceUpdate,
		// new ClickInterface() {
		//
		// @Override
		// public void doCancel() {
		// isCancel = true;
		// progressDialog.dismiss();
		// mResultListener.onNotUpdate();
		// }
		// });
		// progressDialog.show();
		downLoadDialog = new DownloadDialog(context, false);
		downLoadDialog.show();
		downloadApp();
	}

	/**
	 * 下载新版本应用
	 */
	private void downloadApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL url = null;
				InputStream in = null;
				FileOutputStream out = null;
				HttpURLConnection conn = null;
				try {
					url = new URL(updateUrl);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestProperty("Accept-Encoding", "identity");
					conn.connect();
					fileLength = conn.getContentLength();
					in = conn.getInputStream();
					File filePath = new File(FILE_PATH);
					if (!filePath.exists()) {
						filePath.mkdir();
					}
					out = new FileOutputStream(new File(FILE_NAME));
					byte[] buffer = new byte[1024];
					int len = 0;
					readedLength = 0l;
					while ((len = in.read(buffer)) != -1) {
						// 用户点击“取消”按钮，下载中断
						if (isCancel) {
							break;
						}
						out.write(buffer, 0, len);
						readedLength += len;
						curProgress = (int) (((float) readedLength / fileLength) * 100);
						handler.sendEmptyMessage(UPDARE_TOKEN);
						if (readedLength >= fileLength) {
							// progressDialog.dismiss();
							downLoadDialog.dismiss();
							// 下载完毕，通知安装
							handler.sendEmptyMessage(INSTALL_TOKEN);
							break;
						}
					}
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 安装新版本应用
	 */
	private void installApp() {
		File appFile = new File(FILE_NAME);
		if (!appFile.exists()) {
			return;
		}
		String command = "chmod " + "777" + " " + FILE_NAME;
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}