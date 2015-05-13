/**
 * 
 */
package com.peichong.observer.configure;


/** 
 * TODO:   使用到的常量集合，包括接口名
 * @author:   wy 
 * @version:  V1.0 
 */
public class Constants {
	public static String BASE64NP = "";
	public static class RequestUrl {
		
		/** 控制台曲线图温度获取接口*/                                  
		public static final String GET_CONSOLE_GRAPH_TEMPERATURE="http://192.168.1.149/temperature/tpage?";
		
		/**获取最新温度的接口*/
		public static final String GET_NEWEST_TEMPERATURE="http://192.168.1.149/temperature/tnew?";
		
		
		/**控制台湿度获取接口*/
		public static final String GET_CONSOLE_GRAPH_HUMIDITY="http://192.168.1.149/humidity/hpage?";
		
		/**获取最新湿度的接口*/
		public static final String GET_NEWEST_HUMIDITY="http://192.168.1.149/humidity/hnew?";
	
		
		/**获取用户仪器的信息*/                                  
		public static final String USER_INSTRUMENT_INFROMATION="http://192.168.1.149/mechine/getInfoByUser?";
		
		/**根据仪器id获取仪器信息*/                                  
		public static final String IN_INSTRUMENT_INFROMATION="http://192.168.1.149/mechine/getInfoById?";
		
		
		/**获取用户的配置信息*/                                  
		public static final String USER_CONFIGURATION_INFROMATION="http://192.168.1.149/userconfig/getConfigByUser?";
	
		/**获取用户指定的配置信息*/                                  
		public static final String USER_SPECIFIED_CONFIGURATION_INFROMATION="http://192.168.1.149/userconfig/getConfigBySid?";
	
		
		/** 版本消息  */
		public static final String VERSION = "";
	}
}
