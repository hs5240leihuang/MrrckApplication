package com.meiku.dev.volleyextend;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.meiku.dev.utils.LogUtil;
 

public class GsonRequest<T> extends Request<T> {
	final String TAG = "GsonRequest";
    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    
    private final Listener<T> mListener;
    private Gson mGson;
    private Class<T> mClass;
    private String mRequestBody;
    
    
    public GsonRequest(String url,Object param, Class<T> clazz, Listener<T> listener,
            ErrorListener errorListener) {

            this(Method.POST, url, clazz, listener, errorListener);
            mRequestBody = param==null?null:mGson.toJson(param);
            LogUtil.d(TAG, url+"  "+mRequestBody);
        
       }
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener,
        ErrorListener errorListener,String... strings) {
      this(Method.GET, String.format(url, encoder(strings)), clazz, listener, errorListener);
      LogUtil.d(TAG, String.format(url, strings));
      mRequestBody = null;
      
    }
    private static String[] encoder(String[] strings){
        for(int i=0;i<strings.length;i++){
            try {
                strings[i]=URLEncoder.encode(strings[i], PROTOCOL_CHARSET);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return strings;
    }
    public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener,
            ErrorListener errorListener) {
          super(method, url, errorListener);
          mGson = new Gson();
          mClass = clazz;
          mListener = listener;
        }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
      try {
        String jsonString = new String(response.data,PROTOCOL_CHARSET);
        LogUtil.d(TAG, jsonString);
        
        return Response.success(mGson.fromJson(jsonString, mClass),
            HttpHeaderParser.parseCacheHeaders(response));
      } catch (UnsupportedEncodingException e) {
        return Response.error(new ParseError(e));
      }
    }

    @Override
    protected void deliverResponse(T response) {
      mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
        	if(mRequestBody == null) {
        		return null;
        	} else {
        		return mRequestBody.getBytes(PROTOCOL_CHARSET);
        	}
//            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
  }
