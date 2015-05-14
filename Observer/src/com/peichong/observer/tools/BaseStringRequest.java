package com.peichong.observer.tools;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.peichong.observer.configure.Constants;


/** 
 * TODO:   	重写的请求类，解决编码问题
 * @author:   wy 
 * @version:  V1.0 
 */
public class BaseStringRequest extends StringRequest{
private String url = "";
	
	public BaseStringRequest(String url, Listener<String> listener,ErrorListener errorListener) {
		
		super(url, listener, errorListener);
		// TODO Auto-generated constructor stub
		this.url = url;
	}

	public BaseStringRequest(int method, String url, Listener<String> listener,ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
		this.url = url;
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		//headers.put("X-SUP-DOMAIN", "com");
		headers.put("X-SUP-SC", "nosec");
		/*if(!url.equals(Constants.RequestUrl.LOGIN)) {
			headers.put("Authorization", "Basic " + Constants.BASE64NP);
		}
		
		if(url.equals(Constants.RequestUrl.USER_INFO)) {
			headers.put("Content-Type", "application/json; charset=utf-8 ");
		}*/
		return headers;
	}

	
	
	@Override
	protected Response<String> parseNetworkResponse(
			NetworkResponse response) {
		// TODO Auto-generated method stub
		String str = null;
        try {
            str = new String(response.data,"utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
//		return super.parseNetworkResponse(response);
	}
}
