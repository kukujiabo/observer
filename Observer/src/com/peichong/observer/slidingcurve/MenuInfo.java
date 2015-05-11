package com.peichong.observer.slidingcurve;


/** 
 * TODO:   	 菜单抽屉实体类
 * @author:   wy 
 * @version:  V1.0 
 */
public class MenuInfo {
	private String function;

	/**
	 * 
	 */
	public MenuInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param function
	 */
	public MenuInfo(String function) {
		super();
		this.function = function;
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	
}
