package com.peichong.observer.slidingcurve;


/** 
 * TODO:   	温度滚动栏实体类
 * @author:   wy 
 * @version:  V1.0 
 */

public class TemperatureInfo {
	private Float temperature;
	private String time;
	/**
	 * 
	 */
	public TemperatureInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param temperature
	 * @param time
	 */
	public TemperatureInfo(Float temperature, String time) {
		super();
		this.temperature = temperature;
		this.time = time;
	}
	/**
	 * @return the temperature
	 */
	public Float getTemperature() {
		return temperature;
	}
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(Float temperature) {
		this.temperature = temperature;
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
