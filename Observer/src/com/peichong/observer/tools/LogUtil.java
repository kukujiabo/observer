package com.peichong.observer.tools;

import com.peichong.observer.BuildConfig;

import android.util.Log;


/** 
 * TODO:   	日志打印工具
 * @author:   wy 
 * @version:  V1.0 
 */
public class LogUtil {
	public static void showLog(String tag, String msg) {
		if(BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}		
	}
	
	public static void showLog(String msg) {
		if(BuildConfig.DEBUG) {
			Log.d("com.peichong.observer", msg);
		}		
	}
}
