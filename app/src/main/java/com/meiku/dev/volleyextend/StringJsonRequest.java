package com.meiku.dev.volleyextend;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class StringJsonRequest extends StringRequest{
	private String mRequestBody;
	
	//基于POST
	public StringJsonRequest( String url, Listener<String> listener,String mRequestBody,
			ErrorListener errorListener) {
		super(Method.POST, url, listener, errorListener);
		this.mRequestBody =mRequestBody;
		 
	}
//	//设置超时时间和重试次数
//	@Override
//	public RetryPolicy getRetryPolicy() { 
//	         RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,
//	        		 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//	        		 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT); 
//	         return retryPolicy; 
//	}
	
	 @Override  
     protected Map<String, String> getParams() throws AuthFailureError {  
         Map<String,String> map = new HashMap<String,String>();  
         map.put("params",mRequestBody);  
         return map;  
     }  
	 
	   @Override
	    protected Response<String> parseNetworkResponse(NetworkResponse response) {
	        // TODO Auto-generated method stub
	        String str = null;
	        try {
	            str = new String(response.data,"utf-8");
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
	    }

   

}
