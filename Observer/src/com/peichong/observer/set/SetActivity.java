package com.peichong.observer.set;


import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.activities.MainActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.personalcenter.PersonalCenterActivity;
import com.peichong.observer.personalcenter.SetNameActivity;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.LogUtil;

/**
 * TODO: 设置页面
 * 
 * @author: wy
 * @version: V1.0
 */
public class SetActivity extends BaseActivity implements OnClickListener {

	/** 菜单 *//*
	private ImageButton menu;
	*//** 警告 *//*
	private ImageButton warning;
	*//** 资讯 *//*
	private ImageButton information;

	*//** 个人中心 *//*
	private ImageButton user_icon;*/

	/** 应用程序全局属性 */
	private ObserverApplication app;

	/** 用户的ID 从用户登录数据中取的 */
	private String uid = "";

/*	*//******* 侧滑菜单 *******//*
	private ListView lv_set;// 设置菜单

	/**侧滑菜单
	private SlidingMenu menus;
	private MenuAdapter Menuadapter;*/

	/** 温度警告 */
	private CheckBox sethyperthermia;

	/** 高温警告 */
	private TextView gethtem;

	/** 低温警告 */
	private TextView getltem;

	/** 湿度警告 */
	private CheckBox sethumidity;

	/** 高湿警告 */
	private TextView gethhum;

	/** 低湿警告 */
	private TextView getlhum;

	/**返回*/
	private ImageButton ib_return;
	
	/**温度高温*/
	private ImageButton ib1;
	
	/**温度低温*/
	private ImageButton ib2;
	
	/**湿度高温*/
	private ImageButton ib3;
	
	/**湿度低温*/
	private ImageButton ib4;
	
	/**修改类型*/
	public static int set_type=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_set);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		getUserConfigurationInformation();
	}

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initUi() {
		sethyperthermia = (CheckBox) findViewById(R.id.sethyperthermia);
		sethyperthermia.setOnClickListener(this);
		gethtem = (TextView) findViewById(R.id.gethtem);
		getltem = (TextView) findViewById(R.id.getltem);
		sethumidity = (CheckBox) findViewById(R.id.sethumidity);
		sethumidity.setOnClickListener(this);
		gethhum = (TextView) findViewById(R.id.gethhum);
		getlhum = (TextView) findViewById(R.id.getlhum);

		
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(this);
		
		ib1=(ImageButton) findViewById(R.id.ib2);
		ib1.setOnClickListener(this);
		
		ib2=(ImageButton) findViewById(R.id.ib3);
		ib2.setOnClickListener(this);
		
		ib3=(ImageButton) findViewById(R.id.ib5);
		ib3.setOnClickListener(this);
		
		ib4=(ImageButton) findViewById(R.id.ib6);
		ib4.setOnClickListener(this);
		
	}


	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		
		//返回至设备管理界面
		if (v==ib_return) {
			finish();
		}
		//设置温度高温
		else if(v==ib1){
			set_type=1;
			startActivity(new Intent(SetActivity.this, TemHumActivity.class));
			
		}
		//设置低温度
		else if(v==ib2){
			set_type=2;
			startActivity(new Intent(SetActivity.this, TemHumActivity.class));	
		}
		//设置湿度高温
		else if(v==ib3){
			set_type=3;
			startActivity(new Intent(SetActivity.this, TemHumActivity.class));	
		}
		//设置湿度低温
		else if(v==ib4){
			set_type=4;
			startActivity(new Intent(SetActivity.this, TemHumActivity.class));	
		}
	}

	
	
	/** 获取用户的配置信息 */
	public void getUserConfigurationInformation() {
		showProgressDialog();
		uid = app.getUid();
		String url = "uid=" + uid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.USER_CONFIGURATION_INFROMATION + url,
				new Response.Listener<String>() {
					@SuppressWarnings("deprecation")
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求获取用户的配置信息数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONObject jo = comJson
											.getJSONObject("data");

									jo.getJSONObject("t_interval").getString("setting_value");
									jo.getJSONObject("h_interval").getString("setting_value");

									String tType = jo
											.getJSONObject("t_warning")
											.getString("setting_value");
									if (tType.equals("1")) {
										sethyperthermia
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.shezhishuanganniu));
									} else {
										sethyperthermia
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.shezhianniu));
									}

									String hType = jo
											.getJSONObject("h_warning")
											.getString("setting_value");
									if (hType.equals("1")) {
										sethumidity
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.shezhishuanganniu));
									} else {
										sethumidity
												.setBackgroundDrawable(getResources()
														.getDrawable(
																R.drawable.shezhianniu));
									}

									String th = jo.getJSONObject(
											"t_warning_high").getString(
											"setting_value");
									
									String tl = jo.getJSONObject(
											"t_warning_low").getString(
											"setting_value");
									

									String hh = jo.getJSONObject(
											"h_warning_high").getString(
											"setting_value");
									
									String hl = jo.getJSONObject(
											"h_warning_low").getString(
											"setting_value");
									
									gethtem.setText(th);
									getltem.setText(tl);
									gethhum.setText(hh);
									getlhum.setText(hl);
									
									app.setT_warning_high(jo.getJSONObject(
											"t_warning_high").getString(
											"setting_name"));
									app.setT_warning_low(jo.getJSONObject(
											"t_warning_low").getString(
											"setting_name"));
									app.setH_warning_high(jo.getJSONObject(
											"h_warning_high").getString(
											"setting_name"));
									app.setH_warning_low(jo.getJSONObject(
											"h_warning_low").getString(
											"setting_name"));
									
									LogUtil.showLog(
											"==========请求获取用户的配置信息接口请求成功:====",
											"获取的数据：" + jo);
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求获取用户的配置信息接口请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						dismissProgressDialog();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						LogUtil.showLog("==========获取用户的配置信息错误:====", "获取的数据："
								+ error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}

}