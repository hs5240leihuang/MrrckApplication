package com.meiku.dev.ui.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

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
import com.meiku.dev.volleyextend.StringJsonRequest;

public abstract class BaseFragment extends Fragment {
	protected LoadingDialog mProgressDialog;
	public final static int reqCodeOne = 100;
	public final static int reqCodeTwo = 200;
	public final static int reqCodeThree = 300;
	public final static int reqCodeFour = 400;
	boolean isShowProgressDialog = true;
	private Runnable proRunnable;
	private Handler proHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initValue();
	}

	public abstract void initValue();

	public <T> void httpPost(final int requestCode, String relativeURL,
			ReqBase req) {
		httpPost(requestCode, relativeURL, req, isShowProgressDialog);
	}

	public <T> void httpPost(final int requestCode, String action, ReqBase req,
			boolean showdialog) {
		if (!NetworkTools.isNetworkAvailable(getActivity())) {
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
				BaseFragment.this.onErrorResponse(requestCode, arg0);
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

	public abstract <T> void onSuccess(int requestCode, T arg0);

	public abstract <T> void onFailed(int requestCode, T arg0);

	public void onErrorResponse(int requestCode, VolleyError error) {
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
		if (mProgressDialog == null) {
			mProgressDialog = new LoadingDialog(getActivity(), message);
		}
		mProgressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// 转圈已取消则移除延时
				if (proHandler != null && proRunnable != null) {
					proHandler.removeCallbacks(proRunnable);
				}
			}
		});
		if (!mProgressDialog.isShowing() && getActivity() != null) {
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

}
