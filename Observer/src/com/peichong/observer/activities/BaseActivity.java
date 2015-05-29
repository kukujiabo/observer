package com.peichong.observer.activities;



import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;



/** 
 * TODO:   	activity基类,可滑动返回
 * @author:   wy 
 * @version:  V1.0 
 */

public class BaseActivity extends Activity{
	private ProgressDialog mProgressDialog;
	public RequestQueue mRequestQueue;
	
	/** 时间戳 */
	public String timestamp;
	private JSONObject jo=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequestQueue = Volley.newRequestQueue(this);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCanceledOnTouchOutside(false);
		//mProgressDialog.setMessage("正在加载...");
	}

	@Override
	protected void onStop() {
		mRequestQueue.cancelAll(this);
		super.onStop();
	}

	
	
	
	public void showProgressDialog() {
		if (mProgressDialog != null && !mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	public String getTimestamp() {
		Long tsLong = System.currentTimeMillis();
		return timestamp = tsLong.toString();
	}

}
