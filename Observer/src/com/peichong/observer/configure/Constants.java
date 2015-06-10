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
		
		/** 登录*/    
		public static final String LOGIN="http://218.244.135.148:8080/login?";
		
		/** 控制台曲线图温度获取接口*/                                  
		public static final String GET_CONSOLE_GRAPH_TEMPERATURE="http://218.244.135.148:8080/temperature/tpage?";
		
		/**获取最新温度的接口*/
		public static final String GET_NEWEST_TEMPERATURE="http://218.244.135.148:8080/temperature/tnew?";
		
		
		/**控制台湿度获取接口*/
		public static final String GET_CONSOLE_GRAPH_HUMIDITY="http://218.244.135.148:8080/humidity/hpage?";
		
		/**获取最新湿度的接口*/
		public static final String GET_NEWEST_HUMIDITY="http://218.244.135.148:8080/humidity/hnew?";
	
		
		/**获取用户仪器的信息*/                                  
		public static final String USER_INSTRUMENT_INFROMATION="http://218.244.135.148:8080/mechine/getInfoByUser?";
		
		/**根据仪器id获取仪器信息*/                                  
		public static final String IN_INSTRUMENT_INFROMATION="http://218.244.135.148:8080/mechine/getInfoById?";
		
		
		/**获取用户的配置信息*/                                  
		public static final String USER_CONFIGURATION_INFROMATION="http://218.244.135.148:8080/userconfig/getConfigByUser?";
	
		/**获取用户指定的配置信息*/                                  
		public static final String USER_SPECIFIED_CONFIGURATION_INFROMATION="http://218.244.135.148:8080/userconfig/getConfigBySid?";
	
		/** 控制台曲线图温度获取接口*/                                  
		public static final String GET_WARNING="http://218.244.135.148:8080/warning/list?";
		
		/** 修改湿度和温度*/                                  
		public static final String SET_UPDATE="http://218.244.135.148:8080/usersetting/update?";
		
		/** 修改姓名*/                                  
		public static final String SET_NAME="http://218.244.135.148:8080/user/edit?";
		
		/** 版本消息  */
		public static final String VERSION = "";
	}
}
