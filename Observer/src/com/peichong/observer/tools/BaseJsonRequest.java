/**
 * 
 */
package com.peichong.observer.tools;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;


/** 
 * TODO:   	重写的请求类，解决编码问题
 * @author:   wy 
 * @version:  V1.0 
 */
public class BaseJsonRequest extends JsonObjectRequest{
private String url = "";
	
	public BaseJsonRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
		this.url = url;
		// TODO Auto-generated constructor stub
	}

	public BaseJsonRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		this.url = url;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
			headers.put("X-SUP-DOMAIN", "cre.com");
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
	protected Response<JSONObject> parseNetworkResponse(
			NetworkResponse response) {
		String ss = "";
		try {
			ss = new String(response.data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			return Response.success(new JSONObject(ss), HttpHeaderParser.parseCacheHeaders(response));
		} catch (JSONException e) {
			JSONObject js = new JSONObject();
			try {
				js.put("data", ss);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return Response.success(js, HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {

			return super.parseNetworkResponse(response);
		}
	}

}
