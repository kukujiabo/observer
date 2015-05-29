package com.peichong.observer.application;


import android.app.Application;

/**应用程序全局属性*/
public class ObserverApplication extends Application {
	/**用户ID*/
	private String uid;
	/**用户邮箱*/
	private String email;
	/**机器温度ID*/
	private String tid;
	
	private String mid;
	
	/**机器湿度ID*/
	private String hid;
	/**机器地址*/
	private String address;
	/**机器的类型（0温度 1湿度）*/
	private String type;
	
	
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
	 * @param uid
	 * @param email
	 * @param mid
	 * @param address
	 * @param type
	 */
	/**
	 * @return the mid
	 */
	/**
	 * @param mid the mid to set
	 */
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * @return the hid
	 */
	public String getHid() {
		return hid;
	}
	/**
	 * @param hid the hid to set
	 */
	public void setHid(String hid) {
		this.hid = hid;
	}
	/**
	 * @param uid
	 * @param email
	 * @param tid
	 * @param hid
	 * @param address
	 * @param type
	 */
	public ObserverApplication(String uid, String email, String tid,
			String hid, String address, String type) {
		super();
		this.uid = uid;
		this.email = email;
		this.tid = tid;
		this.hid = hid;
		this.address = address;
		this.type = type;
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
