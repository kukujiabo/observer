package com.peichong.observer.equipmentmgm;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.set.SetActivity;
import com.peichong.observer.slidingcurve.ControlActivity;

/**
 * TODO: 设备管理
 * 
 * @author: wy
 * @version: V1.0
 */
public class EquipmentMgmActivity extends BaseActivity implements
		OnClickListener{

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
	
	/**返回*/
	private ImageButton ib_return;

/*	*//******* 侧滑菜单 *******//*
	private ListView lv_set;// 设置菜单

	private MenuAdapter Menuadapter;
	
	private SlidingMenu menus;*/
	
	/**设置温度图标*/
	private ImageView set_icon;
	
	/**设置温度传感器*/
	private TextView set_model;
	
	/**设置温度传感器状态*/
	private TextView set_state;
	
	/**设置温度传感器状态图标*/
	private ImageView set_switch;
	
	
	/**设置湿度图标*/
	private ImageView set_icon_two;
	
	/**设置湿度传感器*/
	private TextView set_model_two;
	
	/**设置湿度传感器状态*/
	private TextView set_state_two;
	
	/**设置湿度传感器状态图标*/
	private ImageView set_switch_two;
	
	/**进入设置界面*/
	private ImageButton set_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_equipmentmgm);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		
		if (app.gettActive().equals("1")) {
			set_state.setText("正在运行");
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.zhengchang);
			set_switch.setImageBitmap(b);
		}else{
			set_state.setText("机器故障");
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.guzhang);
			set_switch.setImageBitmap(b);
		}
		if (app.gethActive().equals("1")) {
			set_state_two.setText("正在运行");
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.zhengchang);
			set_switch_two.setImageBitmap(b);
		}else{
			set_state_two.setText("机器故障");
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.guzhang);
			set_switch_two.setImageBitmap(b);
		}
	}

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initUi() {
		
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(this);
		
		
		set_icon=(ImageView) findViewById(R.id.set_icon);
		set_model=(TextView) findViewById(R.id.set_model);
		set_state=(TextView) findViewById(R.id.set_state);
		set_switch=(ImageView) findViewById(R.id.set_switch);
		
		set_icon_two=(ImageView) findViewById(R.id.set_icon_two);
		set_model_two=(TextView) findViewById(R.id.set_model_two);
		set_state_two=(TextView) findViewById(R.id.set_state_two);
		set_switch_two=(ImageView) findViewById(R.id.set_switch_two);
		
		set_ok=(ImageButton) findViewById(R.id.set_ok);
		set_ok.setOnClickListener(this);
		
	}

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		
		if (v==ib_return) {
			//主界面控制台
			startActivity(new Intent(EquipmentMgmActivity.this, ControlActivity.class));
			finish();
		}
		//进入设置界面
		else if(v==set_ok){
			//进入设置界面
			startActivity(new Intent(EquipmentMgmActivity.this, SetActivity.class));
		}
	}
}
