package com.peichong.observer.activities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.peichong.observer.R;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.Base64Coder;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.JsonUtil;
import com.peichong.observer.tools.LogUtil;
import com.peichong.observer.tools.SharedPreferencesUtils;
import com.peichong.observer.tools.UpdateUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * TODO: 登录
 * 
 * @author: wy
 * @version: V1.0
 */
@SuppressLint({ "NewApi", "InlinedApi" })
public class MainActivity extends BaseActivity implements OnClickListener {

	private String name = "";
	private String password = "";
	private EditText et_name;
	private EditText et_password;
	private ImageButton ib_register;

	private BaseStringRequest mStringRequest;

	/**应用程序全局属性*/
	private ObserverApplication app;
	
	/** 温度Y */
	private Float data;

	/** 时间X */
	private String created_at;

	/** 转换后的时间Y */
	private String s;

	/** 用户的ID 从用户登录数据中取的 */
	private String uid = "";

	/** 邮箱 */
	private String email = "";
	
	/** 电话 */
	private String phone = "";
	
	/** 头像url */
	private String imgUrl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_login);
		//拿到Application对象
		app= (ObserverApplication) getApplication();
		init();
		et_name.setText("testuser1");
		et_password.setText("123");
	}

	/**
	 * TODO : 初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void init() {
		et_name = (EditText) findViewById(R.id.name);
		et_password = (EditText) findViewById(R.id.password);
		ib_register = (ImageButton) findViewById(R.id.ib_register);
		ib_register.setOnClickListener(this);
	}

	/**
	 * 按钮点击事件
	 **/
	@Override
	public void onClick(View v) {
		if (v == ib_register) {
			name = et_name.getText().toString().trim();
			password = et_password.getText().toString().trim();
			// 帐号和密码不为空
			if (!name.equals("") && !password.equals("")) {
				// base64转码
				String dd = Base64Coder.encode(
						(name + ":" + password).getBytes()).toString();
				SharedPreferencesUtils.saveUserPasword(this, password);
				SharedPreferencesUtils.saveData(this, "Base64N&P", dd);
				SharedPreferencesUtils.saveUserName(this, name);
				SharedPreferencesUtils.saveUserPasword(this, password);
				// 登入请求
				login();
			} else {
				// 帐号和密码为空
				Toast.makeText(MainActivity.this, "请输入帐号和密码！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * TODO : 登录
	 * 
	 * @throw
	 * @return :void
	 */
	private void login() {

		 /* if (name.equals("testuser1") && password.equals("123")) {
		 
		 // 登录成功 
		Intent intent = new Intent();
		  intent.setClass(MainActivity.this, ControlActivity.class); 
		  //把温度Y传过去
		  
		  //intent.putExtra("data", data); 
		  //把转换后的时间X传过去 
		  //intent.putExtra("s", s); 
		  startActivity(intent); 
		  //获取温度曲线图接口 getConsoleGraphTemperature();
		  finish(); 
		  } else { 
			  // 登录失败
			  Toast.makeText(MainActivity.this, "登录失败:",Toast.LENGTH_LONG) .show();
		 
		  }*/
		 
		showProgressDialog();
		String url = "name=" + name + "&" + "password=" + password;
		mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.LOGIN + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("+++++++获取登录接口数据+++++++:", "登录接口数据："
								+ response);
						dismissProgressDialog();

						try {
							// 接口返回的数据
							JSONObject comJson = new JSONObject(response);

							// JSONObject中的字段
							if (comJson.has("code")) {
								int code = comJson.getInt("code");
								// 请求成功
								if (code == 1) {

									// JSONObject中的字段
									JSONObject jo = comJson
											.getJSONObject("info");
									if (comJson.has("info")) {
										// JSONObject中的字段
										uid = jo.getString("id");
										email = jo.getString("email");
										name = jo.getString("name");
										phone = jo.getString("phone");
										imgUrl = jo.getString("pic_url");
										
										//把id缓存到Application,可供所有activity使用
										app.setUid(uid);
										//把email缓存到Application,可供所有activity使用
										app.setEmail(email);

										//把昵称缓存到Application
										app.setName(name);
										
										//把电话缓存到Application
										app.setPhone(phone);
										
										//把图片url缓存到Application
										app.setUrl(imgUrl);
										
										LogUtil.showLog(
												"+++++++++++id+++++++++", "id:"
														+ app.getUid());
										LogUtil.showLog(
												"+++++++++++email+++++++++",
												"email:" + app.getEmail());

										LogUtil.showLog(
												"==========获取登录接口数据接口请求成功:====",
												"获取的数据：" + jo);
										// 登录成功
										startActivity(new Intent(
												MainActivity.this,
												ControlActivity.class));
										finish();
									}
								}
								// 请求失败
								else if (code == 0) {
									// 登录失败
									Toast.makeText(MainActivity.this,
											"登录失败:" + response,
											Toast.LENGTH_LONG).show();
									String msg = comJson.getString("msg");

									LogUtil.showLog("==========登录失败:====",
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
						Toast.makeText(MainActivity.this,
								"请求错误:" + error.toString(), Toast.LENGTH_LONG)
								.show();
					}
				}) {

			@Override
			protected HashMap<String, String> getParams()
					throws AuthFailureError {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("action", "checkflag");
				hashMap.put("username", name);
				hashMap.put("pwd", password); 
				return hashMap;
			}
		};

		mStringRequest.setTag("Login");
		mRequestQueue.add(mStringRequest);
	}

	public static void doCall(Context context, String number) {
		Uri uri = Uri.parse("tel:" + number);
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}

	/**
	 * 检查更新
	 * 
	 * @param url
	 * @param version
	 */
	@SuppressWarnings("unused")
	private void checkUpdate(final String url, String version) {
		if (UpdateUtil.checkUpdate(version, getVersionName())) {
			// 有新版本,需要加上弹框提示

			Builder myBuilder = new AlertDialog.Builder(this);
			myBuilder.setTitle(R.string.dialog_title);
			myBuilder.setMessage("有新版本，请更新。");
			myBuilder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

							Uri uri = Uri.parse(url);
							Request request = new Request(uri);
							request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
									| DownloadManager.Request.NETWORK_WIFI);
							request.setVisibleInDownloadsUi(false);
							request.setMimeType("application/vnd.android.package-archive");

							Toast.makeText(MainActivity.this, "开始下载",
									Toast.LENGTH_SHORT).show();
							long id = downloadManager.enqueue(request);
							SharedPreferencesUtils.saveData(MainActivity.this,
									"downloadId", id + "");
						}
					});

			myBuilder.setNegativeButton(R.string.cancel, null);
			myBuilder.create().show();

		}
	}

	/**
	 * 获取版本消息
	 */
	@SuppressWarnings("unused")
	private void getVersionData() {
		// mRequestQueue = Volley.newRequestQueue(this);
		// 2 创建StringRequest对象
		mStringRequest = new BaseStringRequest(Constants.RequestUrl.VERSION,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						JsonUtil.data = response;
						checkUpdate(JsonUtil.getJsonString("downloadURL"),
								JsonUtil.getJsonString("version"));
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// text.setText("get请求错误:" + error.toString());
					}
				}) {
		};
		mStringRequest.setTag("version");
		mRequestQueue.add(mStringRequest);
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

	
	@Override
	   public void onBackPressed() {  
		new AlertDialog.Builder(this).setTitle("确认退出吗？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						ActivityUtil.finishAll();
						finish();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
		// super.onBackPressed();
		      }  
	
}
