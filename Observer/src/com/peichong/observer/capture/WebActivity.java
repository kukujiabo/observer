/**
 * 
 */
package com.peichong.observer.capture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.personalcenter.PersonalCenterActivity;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.LogUtil;


/** 
 * TODO:   	内置浏览器
 * @author:   wy 
 * @version:  V1.0 
 */


public class WebActivity extends BaseActivity{
	
	
	private WebView webview;
	private String url;
	/**返回*/
	private ImageButton ib_return;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_web);
		initUi();
	}

	/**  
	 * TODO : 
	 * @throw 	初始化
	 * @return :void 
	 */ 
	@SuppressLint("SetJavaScriptEnabled")
	private void initUi() {
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v==ib_return) {
					//主界面控制台
					startActivity(new Intent(WebActivity.this, CaptureActivity.class));
					finish();
				}
			}
		});
		
		webview=(WebView) findViewById(R.id.web);
		Intent intent = this.getIntent(); 
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			Toast.makeText(getApplicationContext(), "找不到网页!", Toast.LENGTH_SHORT).show();
		}
		// 获取参数1
		String url = bundle.getString("msg");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url);   //在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
				return true;
			}
		});
		LogUtil.showLog("url:"+url);
		//访问地址
		webview.loadUrl(url);
		
		//客户端
		MyWebViewClient client=new MyWebViewClient();
		webview.setWebViewClient(client);
		
	}
	
	/**键盘按下*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果按的是返回键
		if (keyCode==KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			//回跳上一个界面
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//**内部类 客户端*//*
	public class MyWebViewClient extends WebViewClient{
		
		//**加载资源显示到webview*//*
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//在webView里执行请求url
			view.loadUrl(url);
			return true;
		}
	}
}