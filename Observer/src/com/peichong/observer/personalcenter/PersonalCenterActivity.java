package com.peichong.observer.personalcenter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.peichong.observer.R;
import com.peichong.observer.about.AboutActivity;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.analysislog.AnalysisLogActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.equipmentmgm.EquipmentMgmActivity;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.set.SetActivity;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.slidingcurve.MenuAdapter;
import com.peichong.observer.slidingcurve.MenuInfo;
import com.peichong.observer.versionupdate.VersionUpdateActivity;
import com.peichong.observer.warning.WarningActivity;


/** 
 * TODO:   	个人中心页面
 * @author:   wy 
 * @version:  V1.0 
 */
public class PersonalCenterActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

	/** 菜单 */
	private ImageButton menu;
	/** 警告 */
	private ImageButton warning;
	/** 资讯 */
	private ImageButton information;
	
	/** 个人中心 */
	private ImageButton user_icon;

	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/******* 侧滑菜单 *******/
	private ListView lv_set;// 设置菜单

	private MenuAdapter Menuadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter);
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
		warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);

		warning.setOnClickListener(this);
		information.setOnClickListener(this);
		
		menu = (ImageButton) findViewById(R.id.menu);
		menu.setOnClickListener(this);

		// configure the SlidingMenu
		SlidingMenu menus = new SlidingMenu(this);
		menus.setMode(SlidingMenu.RIGHT);
		 //设置触摸屏幕的模式
		menus.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menus.setShadowWidthRes(R.dimen.shadow_width);
		menus.setShadowDrawable(R.drawable.shadow);

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
		lv_set.setOnItemClickListener(this);
	}
	/** 菜单抽屉实体类 */
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

	/** 条目点击 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {

		// 曲线图页面
		case 0:
			intent = new Intent(view.getContext(), ControlActivity.class);
			break;

		// 资讯页面
		case 1:
			intent = new Intent(view.getContext(), InformationActivity.class);
			break;

		// 警告页面
		case 2:
			intent = new Intent(view.getContext(), WarningActivity.class);
			break;

		// 设置页面
		case 3:
			intent = new Intent(view.getContext(), SetActivity.class);
			break;

		// 分析日志页面
		case 4:
			intent = new Intent(view.getContext(), AnalysisLogActivity.class);
			break;

		// 设备管理页面
		case 5:
			intent = new Intent(view.getContext(), EquipmentMgmActivity.class);
			break;

		// 版本更新页面
		case 6:
			intent = new Intent(view.getContext(), VersionUpdateActivity.class);
			break;

		// 关于页面
		case 7:
			intent = new Intent(view.getContext(), AboutActivity.class);
			break;
		}

		// 页面跳转
		if (intent != null) {
			// intent.putExtra("lv_item_id", id);
			startActivity(intent);
		}
	}

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		if (v == menu) {
			// 侧滑菜单
			Toast.makeText(PersonalCenterActivity.this, "侧滑菜单:", Toast.LENGTH_LONG).show();
		}
		if (v == warning) {
			// 警告页面
			startActivity(new Intent(PersonalCenterActivity.this, WarningActivity.class));
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(PersonalCenterActivity.this,
					InformationActivity.class));
		} else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(PersonalCenterActivity.this,
					PersonalCenterActivity.class));
		}
	}
}