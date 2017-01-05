package com.meiku.dev.volleyextend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.utils.Tool;

public class MultiFileRequest extends Request<String> {

	@SuppressWarnings("deprecation")
	private MultipartEntity entity = new MultipartEntity();
	// MultipartEntityBuilder builder = MultipartEntityBuilder.create();

	private final Response.Listener<String> mListener;

	// private List<File> mFileParts;
	// private String mFilePartName;
	private Map<String, List<FormFileBean>> mapFileBean;
	private Map<String, String> mParams;
	
 

	/**
	 * 多个文件，对应一个key
	 * 
	 * @param url
	 * @param errorListener
	 * @param listener
	 * @param filePartName
	 * @param files
	 * @param params
	 */
	public MultiFileRequest(String url, Response.ErrorListener errorListener,
			Response.Listener<String> listener,
			Map<String, List<FormFileBean>> mapFileBean,
			Map<String, String> params) {
		super(Method.POST, url, errorListener);
		this.mapFileBean = mapFileBean;
		mListener = listener;
		mParams = params;
		buildMultipartEntity();
	}

	// public MultiFileRequest(String url, Response.ErrorListener errorListener,
	// Response.Listener<String> listener, String filePartName,
	// List<File> files, Map<String, String> params) {
	// super(Method.POST, url, errorListener);
	// mFilePartName = filePartName;
	// mListener = listener;
	// mFileParts = files;
	// mParams = params;
	// buildMultipartEntity();
	// }

	@SuppressWarnings("deprecation")
	private void buildMultipartEntity() {

		for (Map.Entry<String, List<FormFileBean>> entry : mapFileBean
				.entrySet()) {
			List<FormFileBean> listBean = entry.getValue();
			if (!Tool.isEmpty(listBean)) {
				for (FormFileBean bean : listBean) {
					entity.addPart(
							entry.getKey(),
							new FileBody(bean.getFile(), ContentType.create(
									HTTP.OCTET_STREAM_TYPE, HTTP.UTF_8), bean
									.getFileName()));
				}

			}
		}

		try {
			if (mParams != null && mParams.size() > 0) {
				for (Map.Entry<String, String> entry : mParams.entrySet()) {
					entity.addPart(
							entry.getKey(),
							new StringBody(entry.getValue(), Charset
									.forName("UTF-8")));
				}
			}
		} catch (UnsupportedEncodingException e) {
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@SuppressWarnings("deprecation")
	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {

			entity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		if (VolleyLog.DEBUG) {
			if (response.headers != null) {
				for (Map.Entry<String, String> entry : response.headers
						.entrySet()) {
					VolleyLog.d(entry.getKey() + "=" + entry.getValue());
				}
			}
		}

		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#getHeaders()
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		VolleyLog.d("getHeaders");
		Map<String, String> headers = super.getHeaders();

		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}

		return headers;
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

	// /**
	// * 单个文件
	// * @param url
	// * @param errorListener
	// * @param listener
	// * @param filePartName
	// * @param file
	// * @param params
	// */
	// public MultiFileRequest(String url, Response.ErrorListener errorListener,
	// Response.Listener<String> listener, String filePartName, File file,
	// Map<String, String> params) {
	// super(Method.POST, url, errorListener);
	//
	// mFileParts = new ArrayList<File>();
	// if (file != null) {
	// mFileParts.add(file);
	// }
	// mFilePartName = filePartName;
	// mListener = listener;
	// mParams = params;
	// buildMultipartEntity();
	// }

}
