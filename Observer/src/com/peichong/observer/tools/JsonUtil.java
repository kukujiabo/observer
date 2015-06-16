/**
 * 
 */
package com.peichong.observer.tools;

import org.json.JSONException;
import org.json.JSONObject;


/** 	JSON解析工具
 * TODO:   
 * @author:   wy 
 * @version:  V1.0 
 */
public class JsonUtil {
	public static String data = "";

	public static String getJsonString(String key) {
		try {
			JSONObject jsonData = new JSONObject(data);
			
			if(jsonData.has(key)) {
				return jsonData.getString(key);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;		
	}
}
