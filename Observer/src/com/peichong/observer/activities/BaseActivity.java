package com.peichong.observer.activities;


import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;



/** 
 * TODO:   	activity基类,可滑动返回
 * @author:   wy 
 * @version:  V1.0 
 */
public class BaseActivity extends SwipeBackActivity{
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
		mProgressDialog.setMessage("正在加载...");
	}

	@Override
	protected void onStop() {
		mRequestQueue.cancelAll(this);
		super.onStop();
	}

	
	@Override
	   public void onBackPressed() {  
		new AlertDialog.Builder(this).setTitle("确认退出吗？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						ActivityUtil.finishAll();
						finish();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
		// super.onBackPressed();
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
