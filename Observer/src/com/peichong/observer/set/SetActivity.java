package com.peichong.observer.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.peichong.observer.R;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.warning.WarningActivity;


/** 
 * TODO:   	设置页面
 * @author:   wy 
 * @version:  V1.0 
 */
public class SetActivity extends Activity implements OnClickListener{

	/** 菜单 */
	private ImageButton menu;
	/** 警告 */
	private ImageButton warning;
	/** 资讯 */
	private ImageButton information;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initUi();
	}
	
	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initUi() {
		menu = (ImageButton) findViewById(R.id.menu);
		warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);
	
		menu.setOnClickListener(this);
		warning.setOnClickListener(this);
		information.setOnClickListener(this);
	}
	
	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		if (v == menu) {
			// 侧滑菜单
			Toast.makeText(SetActivity.this, "侧滑菜单:", Toast.LENGTH_LONG)
					.show();
		} else if (v == warning) {
			// 警告页面
			startActivity(new Intent(SetActivity.this,
					WarningActivity.class));
			finish();
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(SetActivity.this,
					InformationActivity.class));
			finish();
		}
	}
}