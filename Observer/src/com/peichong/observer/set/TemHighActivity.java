/**
 * 
 */
package com.peichong.observer.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.personalcenter.PersonalCenterActivity;
import com.peichong.observer.personalcenter.SetNameActivity;


/** 
 * TODO:   	设置温度高温
 * @author:   wy 
 * @version:  V1.0 
 */
public class TemHighActivity extends BaseActivity implements OnClickListener{
	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**返回*/
	private ImageButton ib_return;
	
	/**保存*/
	private TextView ok;
	
	/**设置昵称*/
	private EditText set_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.temhigh);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
	}


	/**  
	 * TODO : 初始化
	 * @throw 
	 * @return :void 
	 */ 
	private void initUi() {
		
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(this);
		
		ok=(TextView) findViewById(R.id.ok);
		ok.setOnClickListener(this);
		
		set_name=(EditText) findViewById(R.id.set_name);
		
	}
	
	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		//返回设置中心
		if (v==ib_return) {
			finish();
		}
		//保存成功返回设置中心
		else if(v==ok){
			Intent intent = new Intent(TemHighActivity.this, SetActivity.class);  
			Bundle bundle = new Bundle();
			bundle.putString("msg1", set_name.getText().toString());
			intent.putExtras(bundle); 
			startActivity(intent);
			finish();
		}
	}
}
