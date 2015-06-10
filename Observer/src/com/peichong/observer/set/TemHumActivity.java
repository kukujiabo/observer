/**
 * 
 */
package com.peichong.observer.set;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.activities.MainActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.LogUtil;


/** 
 * TODO:   	修改温度和湿度
 * @author:   wy 
 * @version:  V1.0 
 */
public class TemHumActivity extends BaseActivity implements OnClickListener{
	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**返回*/
	private ImageButton ib_return;
	
	/**保存*/
	private TextView ok;
	
	/**设置昵称*/
	private EditText set_names;
	
	/**修改类型*/
	private String setting_name;
	
	/**用户ID*/
	private String uid;
	
	private BaseStringRequest mStringRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.temhum);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		if (SetActivity.set_type==1) {
			set_names.setHint("修改高温警告");
		}else if(SetActivity.set_type==2){
			set_names.setHint("修改低温警告");
		}
		 else if(SetActivity.set_type==3){
			 set_names.setHint("修改高湿警告");
		}
		 else if(SetActivity.set_type==4){
			 set_names.setHint("修改低湿警告");
		}
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
		
		set_names=(EditText) findViewById(R.id.set_names);
		
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
			setTemHem();
		}
	}
	
	/** 修改温度和湿度 */
	public void setTemHem() {
		showProgressDialog();
		uid=app.getUid();
		
		if (SetActivity.set_type==1) {
			setting_name=app.getT_warning_high();
		}else if(SetActivity.set_type==2){
			setting_name=app.getT_warning_low();
			set_names.setHint("修改低温警告");
		}
		 else if(SetActivity.set_type==3){
			 setting_name=app.getH_warning_high();
			 set_names.setHint("修改高湿警告");
		}
		 else if(SetActivity.set_type==4){
			 setting_name=app.getH_warning_low();
			 set_names.setHint("修改低湿警告");
		}
		
		String url = "uid=" + uid + "&" + setting_name + "=" + set_names.getText();
		mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.SET_UPDATE + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						dismissProgressDialog();

						try {
							// 接口返回的数据
							JSONObject comJson = new JSONObject(response);

							// JSONObject中的字段
							if (comJson.has("code")) {
								int code = comJson.getInt("code");
								// 请求成功
								if (code == 1) {
										// 修改成功
										startActivity(new Intent(
												TemHumActivity.this,
												SetActivity.class));
										finish();
								}
								// 请求失败
								else if (code == 0) {
									// 修改失败
									Toast.makeText(TemHumActivity.this,
											"修改失败:" + response,
											Toast.LENGTH_LONG).show();
									String msg = comJson.getString("msg");

									LogUtil.showLog("==========修改失败:====",
											"失败原因：" + msg);
								} else {

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						dismissProgressDialog();
						Toast.makeText(TemHumActivity.this,
								"修改错误:" + error.toString(), Toast.LENGTH_SHORT)
								.show();
					}
				}) {

			@Override
			protected HashMap<String, String> getParams()
					throws AuthFailureError {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("setting_name", set_names.getText().toString());
				return hashMap;
			}
		};

		mStringRequest.setTag("update");
		mRequestQueue.add(mStringRequest);
	}
}
