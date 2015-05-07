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

	public StudyGraphItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
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
	
}
