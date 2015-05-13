package com.peichong.observer.activities;






import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.peichong.observer.R;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.Base64Coder;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.JsonUtil;
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
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



/** 
 * TODO:      登录
 * @author:   wy 
 * @version:  V1.0 
 */
@SuppressLint({ "NewApi", "InlinedApi" })
public class MainActivity extends BaseActivity implements OnClickListener{

	private String name = "";
	private String password = "";
	private EditText editText1;
	private EditText editText2;
	private ImageButton ib_register;
	
	private BaseStringRequest mStringRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		
	}

	/**  
	 * TODO : 初始化
	 * @throw 
	 * @return :void 
	 */ 
	private void init() {
		editText1 = (EditText) findViewById(R.id.name);
		editText2 = (EditText) findViewById(R.id.password);
		ib_register = (ImageButton) findViewById(R.id.ib_register);
		ib_register.setOnClickListener(this);
	}

	/**
	 *	按钮点击事件 
	 **/
	@Override
	public void onClick(View v) {
		if (v == ib_register){
			name=editText1.getText().toString().trim();
			password=editText2.getText().toString().trim();
			//帐号和密码不为空
			if (!name.equals("") && !password.equals("")){
				//base64转码
				String dd = Base64Coder.encode((name + ":" + password).getBytes()).toString();
				SharedPreferencesUtils.saveUserPasword(this, password);
				SharedPreferencesUtils.saveData(this, "Base64N&P", dd);
				SharedPreferencesUtils.saveUserName(this, name);
				SharedPreferencesUtils.saveUserPasword(this, password);
				//登入请求
				login();
			}
			else{
				//帐号和密码为空
				Toast.makeText(MainActivity.this,
						"请输入帐号和密码！", Toast.LENGTH_LONG)
						.show();			}
		}
	}
	
	/**  
	 * TODO : 登录
	 * @throw 
	 * @return :void 
	 */ 
	private void login() {
		if (name.equals("123") && password.equals("123")) {

			// 登录成功
			startActivity(new Intent(MainActivity.this,
					ControlActivity.class));
			finish();
		} else {
			// 登录失败
			Toast.makeText(MainActivity.this,
					"登录失败:", Toast.LENGTH_LONG)
					.show();
			
		}
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
	 * 获取本地版本号，版本号必须为三位
	 * 
	 * @return
	 */
	private String getVersionName() {
		String ver = "1.0.0";
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
