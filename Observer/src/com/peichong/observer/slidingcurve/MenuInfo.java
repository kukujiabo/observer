package com.peichong.observer.slidingcurve;

import android.graphics.drawable.Drawable;



/** 
 * TODO:   	 菜单抽屉实体类
 * @author:   wy 
 * @version:  V1.0 
 */
public class MenuInfo {
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
	public MenuInfo( Drawable drawable) {
		super();
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

	/**
	 * @return the function
	 */
}
