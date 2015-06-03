package com.peichong.observer.slidingcurve;

import android.graphics.drawable.Drawable;



/** 
 * TODO:   	 菜单抽屉实体类
 * @author:   wy 
 * @version:  V1.0 
 */
public class MenuInfo {
	private String function;
	private Drawable url;

	/**
	 * @return the url
	 */
	public Drawable getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(Drawable url) {
		this.url = url;
	}

	/**
	 * @param function
	 * @param drawable
	 */
	public MenuInfo(String function, Drawable drawable) {
		super();
		this.function = function;
		this.url = drawable;
	}

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
