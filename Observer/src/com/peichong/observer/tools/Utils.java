package com.peichong.observer.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.LinearLayout;


/** 
 * TODO:   工具类
 * @author:   wy 
 * @version:  V1.0 
 */
public class Utils {
	
	/**根据手机的分辨率从 px(像素) 的单位 转成为 dp*/
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**将sp值转换为px值，保证文字大小不变*/
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	/**根据手机的分辨率从 dp 的单位 转成为 px(像素)*/
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**年月日 时分秒 日期格式 转变成时分*/
	@SuppressLint("SimpleDateFormat")
	public static String date(String s)throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d=formatter.parse(s);
		SimpleDateFormat format=new SimpleDateFormat("HH:mm");
		String dd = format.format(d);
		return dd;
	}
	
	/**年月日 时分秒 日期格式 转变成月日 时分*/
	@SuppressLint("SimpleDateFormat")
	public static String dateTwo(String s)throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d=formatter.parse(s);
		SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
		String dd = format.format(d);
		return dd;
	}
	
	/**缩小bitmap*/
	public static Bitmap CutPicture(Bitmap bitmap, int width, int height) {
		  if (bitmap != null) {
		   float scaleWidth = ((float) width) / bitmap.getWidth();
		   float scaleHeight = ((float) height) / bitmap.getHeight();
		   Matrix matrix = new Matrix();
		   matrix.postScale(scaleWidth, scaleHeight);
		   Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		   return resizedBitmap;
		  } else {
		   return null;
		  }
	}
}
