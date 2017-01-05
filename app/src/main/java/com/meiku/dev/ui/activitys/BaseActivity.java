package com.meiku.dev.ui.activitys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqBaseStr;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.utils.CompressUtil;
import com.meiku.dev.utils.Des3Util;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.LoadingDialog;
import com.meiku.dev.volleyextend.GsonRequest;
import com.meiku.dev.volleyextend.MultiFileRequest;
import com.meiku.dev.volleyextend.StringJsonRequest;

public abstract class BaseActivity extends Activity {
	protected LoadingDialog mProgressDialog;
	boolean isShowProgressDialog = true;
	public final static int reqCodeOne = 100;
	public final static int reqCodeTwo = 200;
	public final static int reqCodeThree = 300;
	public final static int reqCodeFour = 400;
	protected int page = 1;// 页数
	private Runnable proRunnable;
	private Handler proHandler = new Handler();
	private boolean activityRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initPageLayout();
		MrrckApplication.getInstance().addActivity(getClass().getName(), this);
	}

	private void initPageLayout() {
		setContentView(R.layout.activity_base);
		LinearLayout mClientLayout = (LinearLayout) findViewById(R.id.middle);
		if (mClientLayout != null && getCurrentLayoutID() != -1) {
			mClientLayout.addView(LayoutInflater.from(getBaseContext())
					.inflate(getCurrentLayoutID(), null),
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}
		initView();
		initValue();
		bindListener();
	}

	public <T> void httpPost(final int requestCode, String action, ReqBase req) {
		httpPost(requestCode, action, req, isShowProgressDialog);
	}

	public <T> void httpPost(final int requestCode, String action, ReqBase req,
			final boolean showdialog) {
		if (!NetworkTools.isNetworkAvailable(BaseActivity.this)) {
			ToastUtil.showShortToast("当前网络不可用，请检查你的网络设置");
			onFailed(ConstantKey.REQCODE_NONET, null);
			return;
		}
		Listener<String> successLis = new Listener<String>() {

			@Override
			public void onResponse(String resp) {
				dismissProgressDialog();
				try {
					ReqBase basejson = new ReqBase();
					if (AppConfig.IS_SECRET) {// APP端采用了加密
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						basejson.setHeader(reqStr.getHeader());
						basejson.setBody(JsonUtil.String2Object(Des3Util
								.decode(reqStr.getBody())));
					} else {
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						if (AppConfig.IS_COMPRESS
								&& !Tool.isEmpty(reqStr)
								&& !Tool.isEmpty(reqStr.getHeader())
								&& AppConfig.DATA_COMPRESS.equals(reqStr
										.getHeader().getZipFlag())) {
							basejson.setHeader(reqStr.getHeader());
							basejson.setBody(JsonUtil
									.String2Object(CompressUtil
											.DecompressBody(reqStr.getBody())));
							LogUtil.d("hlhl", "使用数据解压缩");
						} else {
							basejson = (ReqBase) JsonUtil.jsonToObj(
									ReqBase.class, resp);
						}
					}
					ReqHead head = basejson.getHeader();
					if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
						MrrckApplication.getInstance().checkDoubleLoginStatus(
								basejson.getHeader().getJsessionId());
						onSuccess(requestCode, basejson);
					} else {
						onFailed(requestCode, basejson);
					}
				} catch (Exception e) {
					ToastUtil.showShortToast(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ErrorListener errLis = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				dismissProgressDialog();
				onFailed(requestCode, null);
				BaseActivity.this.onErrorResponse(requestCode, arg0);
			}
		};
		String mreqBody;
		// 是否压缩
		if (AppConfig.IS_COMPRESS
				&& !Tool.isEmpty(req.getBody())
				&& req.getBody().toString().length() > CompressUtil.BYTE_MIN_LENGTH) {
			mreqBody = CompressUtil.CompressBody(req);
		} else {// 压缩暂不使用加密
			if (AppConfig.IS_SECRET) {
				mreqBody = Des3Util.reqToSecret(req);
			} else {
				mreqBody = JsonUtil.objToJson(req);
			}
		}
		StringJsonRequest request = new StringJsonRequest(AppConfig.DOMAIN
				+ action, successLis, mreqBody, errLis);

		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantKey.ReqNetTimeOut,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		MrrckApplication.getInstance().addToRequestQueue(request);

		if (showdialog)
			showProgressDialog();
	}

	/**
	 * 
	 * @param requestCode
	 * @param url
	 *            传递完整路径
	 * @param clazz
	 * @param showdialog
	 * @param params
	 */
	public <T> void httpGetWithUrl(final int requestCode, String url,
			Class<T> clazz, boolean showdialog, String... params) {
		if (!NetworkTools.isNetworkAvailable(BaseActivity.this)) {
			ToastUtil.showShortToast("当前网络不可用，请检查你的网络设置");
			return;
		}
		Listener<T> successLis = new Listener<T>() {

			@Override
			public void onResponse(T arg0) {
				dismissProgressDialog();
				onSuccess(requestCode, arg0);
			}
		};
		ErrorListener errLis = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				dismissProgressDialog();
				onFailed(requestCode, null);
				BaseActivity.this.onErrorResponse(requestCode, arg0);
			}
		};
		GsonRequest<T> request = new GsonRequest<T>(url, clazz, successLis,
				errLis, params);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantKey.ReqNetTimeOut,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MrrckApplication.getInstance().addToRequestQueue(request);
		if (showdialog)
			showProgressDialog();
	}

	/**
	 * @param fileUploads
	 *            = 要上传的文件
	 * @param paramsMap
	 *            = 要上传的参数
	 * @param show
	 */
	public <T> void uploadFiles(final int requestCode, String action,
			Map<String, List<FormFileBean>> mapFileBean, ReqBase req,
			boolean show) {
		if (!NetworkTools.isNetworkAvailable(BaseActivity.this)) {
			ToastUtil.showShortToast("当前网络不可用，请检查你的网络设置");
			return;
		}
		Listener<String> successLis = new Listener<String>() {

			@Override
			public void onResponse(String resp) {
				dismissProgressDialog();
				try {
					ReqBase basejson = new ReqBase();
					if (AppConfig.IS_SECRET) {// APP端采用了加密
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						basejson.setHeader(reqStr.getHeader());
						basejson.setBody(JsonUtil.String2Object(Des3Util
								.decode(reqStr.getBody())));
					} else {
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						if (AppConfig.IS_COMPRESS
								&& !Tool.isEmpty(reqStr)
								&& !Tool.isEmpty(reqStr.getHeader())
								&& AppConfig.DATA_COMPRESS.equals(reqStr
										.getHeader().getZipFlag())) {
							basejson.setHeader(reqStr.getHeader());
							basejson.setBody(JsonUtil
									.String2Object(CompressUtil
											.DecompressBody(reqStr.getBody())));
							LogUtil.d("hlhl", "使用数据解压缩");
						} else {
							basejson = (ReqBase) JsonUtil.jsonToObj(
									ReqBase.class, resp);
						}
					}
					ReqHead head = basejson.getHeader();
					if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
						MrrckApplication.getInstance().checkDoubleLoginStatus(
								basejson.getHeader().getJsessionId());
						onSuccess(requestCode, basejson);
					} else {
						onFailed(requestCode, basejson);
					}
				} catch (Exception e) {
					ToastUtil.showShortToast(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ErrorListener errLis = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				dismissProgressDialog();
				onFailed(requestCode, null);
				BaseActivity.this.onErrorResponse(requestCode, arg0);
			}
		};
		Map<String, String> paramMap = new HashMap<String, String>();
		String mreqBody;
		// 是否压缩
		if (AppConfig.IS_COMPRESS
				&& !Tool.isEmpty(req.getBody())
				&& req.getBody().toString().length() > CompressUtil.BYTE_MIN_LENGTH) {
			mreqBody = CompressUtil.CompressBody(req);
		} else {// 压缩暂不使用加密
			if (AppConfig.IS_SECRET) {
				mreqBody = Des3Util.reqToSecret(req);
			} else {
				mreqBody = JsonUtil.objToJson(req);
			}
		}
		paramMap.put("params", mreqBody);
		MultiFileRequest fileRequest = new MultiFileRequest(AppConfig.DOMAIN
				+ action, errLis, successLis, mapFileBean, paramMap);

		fileRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantKey.ReqNetTimeOut,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MrrckApplication.getInstance().addToRequestQueue(fileRequest);
		if (show)
			showProgressDialog();
	}

	public interface OnRespListener<T> {
		void onSuccess(T arg0);

		void onFailed(T arg0);
	}

	/**
	 * @param fileUploads
	 *            = 要上传的文件
	 * @param paramsMap
	 *            = 要上传的参数
	 * @param show
	 */
	public <T> void uploadResFiles(final int requestCode, String action,
			Map<String, List<FormFileBean>> mapFileBean, ReqBase req,
			boolean show) {
		if (!NetworkTools.isNetworkAvailable(BaseActivity.this)) {
			ToastUtil.showShortToast("当前网络不可用，请检查你的网络设置");
			return;
		}
		Listener<String> successLis = new Listener<String>() {

			@Override
			public void onResponse(String resp) {
				dismissProgressDialog();
				try {
					ReqBase basejson = new ReqBase();
					if (AppConfig.IS_SECRET) {// APP端采用了加密
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						basejson.setHeader(reqStr.getHeader());
						basejson.setBody(JsonUtil.String2Object(Des3Util
								.decode(reqStr.getBody())));
					} else {
						ReqBaseStr reqStr = (ReqBaseStr) JsonUtil.jsonToObj(
								ReqBaseStr.class, resp);
						if (AppConfig.IS_COMPRESS
								&& !Tool.isEmpty(reqStr)
								&& !Tool.isEmpty(reqStr.getHeader())
								&& AppConfig.DATA_COMPRESS.equals(reqStr
										.getHeader().getZipFlag())) {
							basejson.setHeader(reqStr.getHeader());
							basejson.setBody(JsonUtil
									.String2Object(CompressUtil
											.DecompressBody(reqStr.getBody())));
							LogUtil.d("hlhl", "使用数据解压缩");
						} else {
							basejson = (ReqBase) JsonUtil.jsonToObj(
									ReqBase.class, resp);
						}
					}
					ReqHead head = basejson.getHeader();
					if (ConstantKey.REQ_SUCCESS.equals(head.getRetStatus())) {
						MrrckApplication.getInstance().checkDoubleLoginStatus(
								basejson.getHeader().getJsessionId());
						onSuccess(requestCode, basejson);
					} else {
						onFailed(requestCode, basejson);
					}
				} catch (Exception e) {
					ToastUtil.showShortToast(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		ErrorListener errLis = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				dismissProgressDialog();
				onFailed(requestCode, null);
				BaseActivity.this.onErrorResponse(requestCode, arg0);
			}
		};
		Map<String, String> paramMap = new HashMap<String, String>();
		String mreqBody;
		// 是否压缩
		if (AppConfig.IS_COMPRESS
				&& !Tool.isEmpty(req.getBody())
				&& req.getBody().toString().length() > CompressUtil.BYTE_MIN_LENGTH) {
			mreqBody = CompressUtil.CompressBody(req);
		} else {// 压缩暂不使用加密
			if (AppConfig.IS_SECRET) {
				mreqBody = Des3Util.reqToSecret(req);
			} else {
				mreqBody = JsonUtil.objToJson(req);
			}
		}
		paramMap.put("params", mreqBody);
		MultiFileRequest fileRequest = new MultiFileRequest(
				AppConfig.DOMAIN_UPLOAD + action, errLis, successLis,
				mapFileBean, paramMap);
		fileRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantKey.ReqNetTimeOut,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MrrckApplication.getInstance().addToRequestQueue(fileRequest);
		if (show)
			showProgressDialog();
	}

	protected abstract int getCurrentLayoutID();

	public abstract void initView();

	public abstract void initValue();

	public abstract void bindListener();

	public abstract <T> void onSuccess(int requestCode, T arg0);

	public abstract <T> void onFailed(int requestCode, T arg0);

	public void onErrorResponse(int requestCode, VolleyError error) {
		LogUtil.e("volley-ERROR", error.getMessage());
		// if (arg0.networkResponse != null) {
		// byte[] htmlBodyBytes = arg0.networkResponse.data;
		// LogUtil.e("volley-ERROR", new String(htmlBodyBytes));
		// }
		//
		// ToastUtil.showShortToast(this,
		// getResources().getString(R.string.net_exception));
		if (error instanceof TimeoutError) {// 超时
			ToastUtil.showShortToast(getResources().getString(
					R.string.volley_error_Timeout));
		} else if ((error instanceof NetworkError)
				|| (error instanceof NoConnectionError)) {// 网络问题
			ToastUtil.showShortToast(getResources().getString(
					R.string.volley_error_Timeout));
		} else if ((error instanceof ServerError)
				|| (error instanceof AuthFailureError)) {
			ToastUtil.showShortToast(getResources().getString(
					R.string.volley_error_Timeout));
		} else {
			ToastUtil.showShortToast(getResources().getString(
					R.string.volley_error_Other));
		}
	}

	public void showProgressDialog() {
		showProgressDialog("请稍等...");
	}

	public void showProgressDialog(String message) {
		mProgressDialog = new LoadingDialog(this, message);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// 转圈已取消则移除延时
				if (proHandler != null && proRunnable != null) {
					proHandler.removeCallbacks(proRunnable);
				}
			}
		});
		if (!mProgressDialog.isShowing() && BaseActivity.this != null
				&& activityRunning) {
			try {
				mProgressDialog.show();
				proRunnable = new Runnable() {

					@Override
					public void run() {
						dismissProgressDialog();
					}
				};
				proHandler.postDelayed(proRunnable, 30 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityRunning = false;
		// JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityRunning = true;
		// JPushInterface.onResume(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissProgressDialog();
		MrrckApplication.getInstance().removeActivity(getClass().getName());
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		if (config.fontScale > 1.1f) {
			config.fontScale = 1.1f;
		}
		// config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}
}
