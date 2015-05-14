/**
 * 
 */
package com.peichong.observer.tools;


/** 
 * TODO:   	字符串处理	
 * @author:   wy 
 * @version:  V1.0 
 */
public class StringUtil {
	
	//字符串切割
		public static String cut(String str,String Xmlfirst,String Xmlend){
			if (str == null || Xmlfirst == null || Xmlend == null)  
			      return null;
			return str.substring(str.indexOf(Xmlfirst) + Xmlfirst.length(), str.indexOf(Xmlend));  
			
		}
}
