package com.peichong.observer.versionupdate;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.activities.MainActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.slidingcurve.ControlActivity;


/** 
 * TODO:   	版本更新
 * @author:   wy 
 * @version:  V1.0 
 */
public class VersionUpdateActivity extends BaseActivity implements OnClickListener{

	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**返回*/
	private ImageButton ib_return;
	
	/**当前版本*/
	private TextView current;
	
	/**当前版本大小*/
	private TextView size;
	
	/**更新版本*/
	private TextView update;
	
	/**更新版本大小*/
	private TextView sizetwo;
	
	/**版本更新按钮*/
	private ImageButton download;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_versionupdate);
		initUi();
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
		
		current=(TextView) findViewById(R.id.current);
		
		size=(TextView) findViewById(R.id.size);
		
		update=(TextView) findViewById(R.id.update);
		
		sizetwo=(TextView) findViewById(R.id.sizetwo);
		
		download=(ImageButton) findViewById(R.id.download);
		
		String s=getVersionName();
		current.setText(s);
		
	}
	
/*	*//** 菜单抽屉实体类 *//*
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

	

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		
		if (v==ib_return) {
			//主界面控制台
			startActivity(new Intent(VersionUpdateActivity.this, ControlActivity.class));
			finish();
		}
		//版本更新
		else if(v==download){
			Toast.makeText(VersionUpdateActivity.this, "版本更新！",
					Toast.LENGTH_LONG).show();
		}
		
	}
	
	/**
	 * 获取本地版本号，版本号必须为两位
	 * 
	 * @return
	 */
	private String getVersionName() {
		String ver = "1.0";
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			ver = packInfo.versionName;
		} catch (NameNotFoundException e) {
			return ver;
		}
		return ver;
	}
}
