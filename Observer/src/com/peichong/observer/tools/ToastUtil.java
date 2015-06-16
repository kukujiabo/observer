/**
 * 
 */
package com.peichong.observer.tools;

import android.content.Context;
import android.widget.Toast;


/** 
 * TODO:   	提示信息工具类
 * @author:   wy 
 * @version:  V1.0 
 */
public class ToastUtil {
	public static void showShortToast(Context con, String msg) {
		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context con, String msg) {
		Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
	}
	
}
