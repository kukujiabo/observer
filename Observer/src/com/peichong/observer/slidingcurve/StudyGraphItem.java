package com.peichong.observer.slidingcurve;



/** 
 * TODO:   记录曲线
 * @author:   wy 
 * @version:  V1.0 
 */
public class StudyGraphItem {
	/**时间*/
	public String date;	
	/**温度*/
	public float temperature;
	/**湿度*/
	public float humidity;

	public StudyGraphItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * @param date
	 * @param temperature
	 * @param humidity
	 */
	public StudyGraphItem(String date, float temperature) {
		super();
		this.date = date;
		this.temperature = temperature;
	}



	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the humidity
	 */
	public float getHumidity() {
		return humidity;
	}

	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	
}
