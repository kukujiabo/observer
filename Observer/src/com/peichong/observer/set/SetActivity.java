package com.peichong.observer.set;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
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
		/*warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);

		warning.setOnClickListener(this);
		information.setOnClickListener(this);*/
		
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
		
	/*	menu = (ImageButton) findViewById(R.id.menu);
		menu.setOnClickListener(this);

		// configure the SlidingMenu
		menus = new SlidingMenu(this);
		menus.setMode(SlidingMenu.RIGHT);
		 //设置触摸屏幕的模式
		menus.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//menus.setShadowWidthRes(R.dimen.shadow_width);
		//menus.setShadowDrawable(R.drawable.shadow);

		 //设置滑动菜单视图的宽度
		menus.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menus.setFadeDegree(0.35f);
		// 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
		menus.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 为侧滑菜单设置布局
		menus.setMenu(R.layout.activity_menu);

		user_icon = (ImageButton) findViewById(R.id.user_icon);
		user_icon.setOnClickListener(this);
		
		lv_set = (ListView) findViewById(R.id.lv_set);
		List<MenuInfo> list = initRightMenus();
		Menuadapter = new MenuAdapter(this, list);
		lv_set.setAdapter(Menuadapter);
		lv_set.setOnItemClickListener(this);*/
	}

	/** 菜单抽屉实体类 *//*
	private List<MenuInfo> initRightMenus() {
		List<MenuInfo> templist = new ArrayList<MenuInfo>();
		templist.add(new MenuInfo("控制台"));
		templist.add(new MenuInfo("资讯"));
		templist.add(new MenuInfo("警告"));
		templist.add(new MenuInfo("设置"));
		templist.add(new MenuInfo("分析日志"));
		templist.add(new MenuInfo("设备管理"));
		templist.add(new MenuInfo("版本更新"));
		templist.add(new MenuInfo("关于我们"));
		return templist;
	}
*/
	/** 条目点击 *//*
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {

		// 曲线图页面
		case 0:
			intent = new Intent(view.getContext(), ControlActivity.class);
			menus.toggle(true);
			break;

		// 资讯页面
		case 1:
			intent = new Intent(view.getContext(), InformationActivity.class);
			menus.toggle(true);
			break;

		// 警告页面
		case 2:
			intent = new Intent(view.getContext(), WarningActivity.class);
			menus.toggle(true);
			break;

		// 设置页面
		case 3:
			intent = new Intent(view.getContext(), SetActivity.class);
			menus.toggle(true);
			break;

		// 分析日志页面
		case 4:
			intent = new Intent(view.getContext(), AnalysisLogActivity.class);
			menus.toggle(true);
			break;

		// 设备管理页面
		case 5:
			intent = new Intent(view.getContext(), EquipmentMgmActivity.class);
			menus.toggle(true);
			break;

		// 版本更新页面
		case 6:
			intent = new Intent(view.getContext(), VersionUpdateActivity.class);
			menus.toggle(true);
			break;

		// 关于页面
		case 7:
			intent = new Intent(view.getContext(), AboutActivity.class);
			menus.toggle(true);
			break;
			
		}

		// 页面跳转
		if (intent != null) {
			// intent.putExtra("lv_item_id", id);
			startActivity(intent);
		}
	}*/

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		
		//返回至设备管理界面
		if (v==ib_return) {
			finish();
		}
		//设置温度高温
		else if(v==ib1){
			startActivity(new Intent(SetActivity.this, TemHighActivity.class));
		}
		//设置温度低温
		else if(v==ib2){
			startActivity(new Intent(SetActivity.this, TemLowhActivity.class));	
		}
		//设置湿度高温
		else if(v==ib3){
			startActivity(new Intent(SetActivity.this, HemHighActivity.class));	
		}
		//设置湿度低温
		else if(v==ib4){
			startActivity(new Intent(SetActivity.this, HemLowhActivity.class));	
		}
		/*if (v == menu) {
			// 侧滑菜单
			menus.toggle(true);
		}
		if (v == warning) {
			// 警告页面
			startActivity(new Intent(SetActivity.this, WarningActivity.class));
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(SetActivity.this,
					InformationActivity.class));
		} else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(SetActivity.this,
					PersonalCenterActivity.class));
		}*/
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

									jo.getJSONObject("t_interval").getString(
											"setting_value");
									jo.getJSONObject("h_interval").getString(
											"setting_value");

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

									String ht = jo.getJSONObject(
											"t_warning_high").getString(
											"setting_value");
									
									String lt = jo.getJSONObject(
											"t_warning_low").getString(
											"setting_value");
									

									String hh = jo.getJSONObject(
											"h_warning_high").getString(
											"setting_value");
									
									String lh = jo.getJSONObject(
											"h_warning_low").getString(
											"setting_value");
									
									//修改高温度
									Intent intent = SetActivity.this.getIntent(); 
									Bundle bundle = intent.getExtras();
									if (bundle == null) {
										gethtem.setText(ht);
										getltem.setText(lt);
										gethhum.setText(hh);
										getlhum.setText(lh);
									}else{
										String name = bundle.getString("msg1");
										gethtem.setText(name);
										getltem.setText(lt);
										gethhum.setText(hh);
										getlhum.setText(lh);
									}
									
									//修改低温度
									Intent intent2 = SetActivity.this.getIntent(); 
									Bundle bundle2 = intent2.getExtras();
									if (bundle2 == null) {
										gethtem.setText(ht);
										getltem.setText(lt);
										gethhum.setText(hh);
										getlhum.setText(lh);
									}else{
										String name = bundle2.getString("msg2");
										gethtem.setText(ht);
										getltem.setText(name);
										gethhum.setText(hh);
										getlhum.setText(lh);
									}
									
									//修改高湿度
									Intent intent3 = SetActivity.this.getIntent(); 
									Bundle bundle3 = intent3.getExtras();
									if (bundle3 == null) {
										gethtem.setText(ht);
										getltem.setText(lt);
										gethhum.setText(hh);
										getlhum.setText(lh);
									}else{
										String name = bundle3.getString("msg3");
										gethtem.setText(ht);
										getltem.setText(lt);
										gethhum.setText(name);
										getlhum.setText(lh);
									}
									
									//修改高湿度
									Intent intent4 = SetActivity.this.getIntent(); 
									Bundle bundle4 = intent4.getExtras();
									if (bundle4 == null) {
										gethtem.setText(ht);
										getltem.setText(lt);
										gethhum.setText(hh);
										getlhum.setText(lh);
									}else{
										String name = bundle4.getString("msg4");
										gethtem.setText(ht);
										getltem.setText(lt);
										gethhum.setText(hh);
										getlhum.setText(name);
									}
									
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