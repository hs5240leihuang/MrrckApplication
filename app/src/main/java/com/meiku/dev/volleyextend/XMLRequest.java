package com.meiku.dev.volleyextend;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class XMLRequest extends Request<XmlPullParser>{
	final String TAG = "XMLRequest";
	private static final String PROTOCOL_CHARSET = "utf-8";
	private static final String GET_CHARSET = "GBK";
    private final Listener<XmlPullParser> mListener;  
    
    public XMLRequest(int method, String url, Listener<XmlPullParser> listener,  
            ErrorListener errorListener) {  
        super(method, url, errorListener);  
        mListener = listener;  
    }  
  
    public XMLRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener,String...params) {  
        
        this(Method.GET, String.format(url, encoder(params)), listener, errorListener);  
    }  
    private static String[] encoder(String[] strings){
        for(int i=0;i<strings.length;i++){
            try {
                strings[i]=URLEncoder.encode(strings[i], GET_CHARSET);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return strings;
    }
    @Override  
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {  
        try {  
            String xmlString = new String(response.data,  
                    HttpHeaderParser.parseCharset(response.headers));  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            XmlPullParser xmlPullParser = factory.newPullParser();  
            xmlPullParser.setInput(new StringReader(xmlString));  
            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));  
        } catch (UnsupportedEncodingException e) {  
            return Response.error(new ParseError(e));  
        } catch (XmlPullParserException e) {  
            return Response.error(new ParseError(e));  
        }  
    }  
  
    @Override  
    protected void deliverResponse(XmlPullParser response) {  
        mListener.onResponse(response);  
    }  
}
