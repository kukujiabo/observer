/**
 * 
 */
package com.peichong.observer.slidingcurve;


/** 
 * TODO:   	湿度滚动栏实体类
 * @author:   wy 
 * @version:  V1.0 
 */
public class HumidityInfo {
	private Float humidity;
	private String time;
	/**
	 * 
	 */
	public HumidityInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param humidity
	 * @param time
	 */
	public HumidityInfo(Float humidity, String time) {
		super();
		this.humidity = humidity;
		this.time = time;
	}
	/**
	 * @return the humidity
	 */
	public Float getHumidity() {
		return humidity;
	}
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
}
