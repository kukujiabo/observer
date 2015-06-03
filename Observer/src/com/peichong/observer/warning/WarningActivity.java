package com.peichong.observer.warning;

import com.peichong.observer.R;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.slidingcurve.ControlActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


/** 
 * TODO:   	  警告页面
 * @author:   wy 
 * @version:  V1.0 
 */
public class WarningActivity extends Activity implements OnClickListener{
	
	/** 菜单 */
	private ImageButton menu;
	/** 资讯 */
	private ImageButton information;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning);
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
		information = (ImageButton) findViewById(R.id.information);
		menu.setOnClickListener(this);
		information.setOnClickListener(this);
	}

	/**按钮点击*/
	@Override
	public void onClick(View v) {
		if (v == menu) {
			// 侧滑菜单
						Toast.makeText(WarningActivity.this, "侧滑菜单:", Toast.LENGTH_LONG)
								.show();
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(WarningActivity.this, InformationActivity.class));
			finish();
		}
	}
}
