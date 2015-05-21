package com.peichong.observer.application;

import android.app.Application;
import android.content.Context;

/**应用程序全局属性*/
public class ObserverApplication extends Application {
	/**用户ID*/
	private String uid;
	/**用户邮箱*/
	private String email;
	/**机器ID*/
	private String mid;
	/**机器地址*/
	private String address;
	/**机器的类型（0温度 1湿度）*/
	private String type;

	
	/**
	 * @param uid
	 * @param email
	 * @param mid
	 * @param address
	 * @param type
	 */
	public ObserverApplication(String uid, String email, String mid,
			String address, String type) {
		super();
		this.uid = uid;
		this.email = email;
		this.mid = mid;
		this.address = address;
		this.type = type;
	}
	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 
	 */
	public ObserverApplication() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
