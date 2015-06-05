package com.peichong.observer.equipmentmgm;


import android.content.Intent;
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
		
		/*warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);

		warning.setOnClickListener(this);
		information.setOnClickListener(this);

		menu = (ImageButton) findViewById(R.id.menu);
		menu.setOnClickListener(this);

		// configure the SlidingMenu
		menus = new SlidingMenu(this);
		menus.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		menus.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//menus.setShadowWidthRes(R.dimen.shadow_width);
		//menus.setShadowDrawable(R.drawable.shadow);

		// 设置滑动菜单视图的宽度
		menus.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		//menus.setFadeDegree(0.35f);
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
	}*/

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
	}
*/
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
		
		/*if (v == menu) {
			// 侧滑菜单
			menus.toggle(true);
		}
		if (v == warning) {
			// 警告页面
			startActivity(new Intent(EquipmentMgmActivity.this,
					WarningActivity.class));
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(EquipmentMgmActivity.this,
					InformationActivity.class));
		} else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(EquipmentMgmActivity.this,
					PersonalCenterActivity.class));
		}*/
	}
}
