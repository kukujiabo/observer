package com.peichong.observer.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/** 
 * TODO:   本地数据处理类
 * @author:   wy 
 * @version:  V1.0 
 */
public class SharedPreferencesUtils {
	
	private static SharedPreferences mSharedPreferences;
	
	private static Editor mEditor;
	
	private final static String DATA_NAME = "com.peichong.observer";
	
	private final static String USER_NAME = "USER_NAME";
	
	private final static String USER_PASSWORD = "USER_PASSWORD";
	
	private final static String LIST_TEXT_SIZE = "LIST_TEXT_SIZE";
	
	@SuppressLint("CommitPrefEdits")
	private static void init(Context context) {
		
		mSharedPreferences = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		
	}
	
	
	/**
	 * 保存用户名
	 * @param context
	 * @param value
	 */
	public static void saveUserName(Context context, String value){
		init(context);
		mEditor.putString(USER_NAME, value);
		mEditor.commit();
	}
	
	/**
	 * 保存密码
	 * @param context
	 * @param value
	 */
	public static void saveUserPasword(Context context, String value){
		init(context);
		mEditor.putString(USER_PASSWORD, value);
		mEditor.commit();
	}
	
	
	
	/**
	 * 保存数据
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveData(Context context,String key, String value){
		init(context);
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
	/**
	 * 获取数据
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getData(Context context,String key){
		
		return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(key, "");
	}
	
	/**
	 * 保存列表字体大小
	 * @param context
	 * @param value
	 */
	public static void saveListTextSize(Context context, float value){
		init(context);
		mEditor.putFloat(LIST_TEXT_SIZE, value);
		mEditor.commit();
	}
	
	/**
	 * 获取列表字体大小
	 * @param context
	 * @return
	 */
	public static float getListTextSize(Context context){
		return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getFloat(LIST_TEXT_SIZE, 20);
	}
	
	/**
	 * 获取密码
	 * @param context
	 * @return
	 */
	public static String getUserPassword(Context context){
		
		return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(USER_PASSWORD, "");
		
	}
	
	/**
	 * 获取用户名
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context){
		
		return context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE).getString(USER_NAME, "");
		
	}
}
