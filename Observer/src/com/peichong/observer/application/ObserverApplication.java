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
	
	/**昵称*/
	private String name;
	
	/**电话*/
	private String phone;
	
	/**图片url*/
	private String url;
	
	/**设置温度和湿度类型*/
	private int set_type;
	
	/**高温警告*/
	private String t_warning_high;
	
	/**低温警告*/
	private String t_warning_low;
	
	/**高湿警告*/
	private String h_warning_high;
	
	/**低湿警告*/
	private String h_warning_low;
	
	/**温度传感器运行状态*/
	private String tActive;
	
	/**湿度传感器运行状态*/
	private String hActive;
	
	
	

	/**
	 * @return the tActive
	 */
	public String gettActive() {
		return tActive;
	}
	/**
	 * @param tActive the tActive to set
	 */
	public void settActive(String tActive) {
		this.tActive = tActive;
	}
	/**
	 * @return the hActive
	 */
	public String gethActive() {
		return hActive;
	}
	/**
	 * @param hActive the hActive to set
	 */
	public void sethActive(String hActive) {
		this.hActive = hActive;
	}
	/**
	 * @return the t_warning_high
	 */
	public String getT_warning_high() {
		return t_warning_high;
	}
	/**
	 * @param t_warning_high the t_warning_high to set
	 */
	public void setT_warning_high(String t_warning_high) {
		this.t_warning_high = t_warning_high;
	}
	/**
	 * @return the t_warning_low
	 */
	public String getT_warning_low() {
		return t_warning_low;
	}
	/**
	 * @param t_warning_low the t_warning_low to set
	 */
	public void setT_warning_low(String t_warning_low) {
		this.t_warning_low = t_warning_low;
	}
	/**
	 * @return the h_warning_high
	 */
	public String getH_warning_high() {
		return h_warning_high;
	}
	/**
	 * @param h_warning_high the h_warning_high to set
	 */
	public void setH_warning_high(String h_warning_high) {
		this.h_warning_high = h_warning_high;
	}
	/**
	 * @return the h_warning_low
	 */
	public String getH_warning_low() {
		return h_warning_low;
	}
	/**
	 * @param h_warning_low the h_warning_low to set
	 */
	public void setH_warning_low(String h_warning_low) {
		this.h_warning_low = h_warning_low;
	}
	/**
	 * @return the set_type
	 */
	public int getSet_type() {
		return set_type;
	}
	/**
	 * @param set_type the set_type to set
	 */
	public void setSet_type(int set_type) {
		this.set_type = set_type;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
