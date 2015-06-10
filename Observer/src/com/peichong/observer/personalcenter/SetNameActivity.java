/**
 * 
 */
package com.peichong.observer.personalcenter;


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
import com.peichong.observer.capture.CaptureActivity;
import com.peichong.observer.capture.WebActivity;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.set.SetActivity;
import com.peichong.observer.set.TemHumActivity;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.Base64Coder;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.LogUtil;
import com.peichong.observer.tools.SharedPreferencesUtils;


/** 
 * TODO:   	修改昵称
 * @author:   wy 
 * @version:  V1.0 
 */
public class SetNameActivity  extends BaseActivity implements OnClickListener{
	
	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**返回*/
	private ImageButton ib_return;
	
	/**保存*/
	private TextView ok;
	
	/**设置昵称*/
	private EditText set_name;
	
	/**用户ID*/
	private String uid;
	
	private BaseStringRequest mStringRequest;
	
	/**帐号姓名*/
	private String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.myedittext);
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
		//返回个人中心
		if (v==ib_return) {
			finish();
		}
		//保存成功返回个人中心
		else if(v==ok){
			name = set_name.getText().toString().trim();
			// 帐号不为空
			if (!name.equals("")) {
				// base64转码
				String dd = Base64Coder.encode(
						(name).getBytes()).toString();
				SharedPreferencesUtils.saveData(this, "Base64N&P", dd);
				SharedPreferencesUtils.saveUserName(this, name);
				// 修改请求
				setName();
			} else {
				// 帐号为空
				Toast.makeText(SetNameActivity.this, "请输入帐号！",
						Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
	/** 修改姓名 */
	public void setName() {
		showProgressDialog();
		uid=app.getUid();
		String url = "uid=" + uid + "&" + "uname=" + name;
		mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.SET_NAME + url,
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
									app.setName(name);
										// 修改成功
										startActivity(new Intent(
												SetNameActivity.this,
												PersonalCenterActivity.class));
										finish();
								}
								// 请求失败
								else if (code == 0) {
									// 修改失败
									Toast.makeText(SetNameActivity.this,
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
						Toast.makeText(SetNameActivity.this,
								"修改错误:" + error.toString(), Toast.LENGTH_SHORT)
								.show();
					}
				}) {

			@Override
			protected HashMap<String, String> getParams()
					throws AuthFailureError {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("name", name);
				return hashMap;
			}
		};

		mStringRequest.setTag("updateName");
		mRequestQueue.add(mStringRequest);
	}
}
