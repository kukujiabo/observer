/**
 * 
 */
package com.peichong.observer.capture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.tools.DownLoadImage;
import com.peichong.observer.tools.Utils;


/** 
 * TODO:   	 条形码详情
 * @author:   wy 
 * @version:  V1.0 
 */
public class ResultStrDetailsActivity extends BaseActivity implements OnClickListener{
	
	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**屏幕宽*/
	private int screenWidth;
	
	/**返回*/
	private ImageButton ib_return;
	
	/**收藏*/
	private ImageButton click;
	
	/**图片url*/
	private ImageView url;
	
	/**名称*/
	private TextView name;
	
	/**品牌*/
	private TextView brand;
	
	/**产地*/
	private TextView origin;
	
	/**类型*/
	private TextView type;
	
	/**类型内容*/
	private TextView typeContent;
	
	/**待酒温度*/
	private TextView temappropriate;
	
	/**待酒湿度*/
	private TextView hemappropriate;
	
	/**温馨提示图片*/
	private ImageView prompt;
	
	/**温馨提示*/
	private TextView prompt_text;
	
	/**温馨提示内容*/
	private TextView prompt_twotext;
	
	/**当前温度*/
	private TextView tem;
	
	/**当前湿度*/
	private TextView hum;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		
		setContentView(R.layout.activity_resultstrdetails);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
		initUi();
		
		//设置图片
				String s="http://218.244.135.148:8080"+app.getBarCodeIv().trim();
				if (app.getBarCodeIv().equals("") ||app.getBarCodeIv()==null) {
					Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.tupianp);
					url.setImageBitmap(Utils.CutPicture(b, url.getWidth(), 100));
				}else{
					new DownLoadImageTwo(url).execute(s);
				}
		
		//设置名字
		name.setText(app.getBarCodeName());
		
		//设置物品品牌
		brand.setText(app.getBarCodeBrand());
		
		//设置物品产地
		origin.setText(app.getBarCodeOrigin());
		
		//设置物品类型介绍
		type.setText(app.getBarCodeTv5()+" :");
				
		//设置物品类型详情
		typeContent.setText(app.getBarCodeType());
		
		//设置待酒温度
		temappropriate.setText(app.getBarCodeTemappropriate()+"°C");
				
		//设置待酒湿度
		hemappropriate.setText(app.getBarCodeHemappropriate()+"%");
		
		//设置当前温度
		tem.setText(app.getBarCodeTem()+"°C");
				
				
		//设置当前湿度
		hum.setText(app.getBarCodeHem()+"%");
		
		//设置温馨提示内容
		prompt_twotext.setText(app.getBarCodePrompt_twotext());
		
		prompt_text.setText("温馨提示");
		
		//当前温度
		String xts=app.getBarCodeTem();
		//待酒温度
		String dts=app.getBarCodeTemappropriate();
		//tresult大于0  xts>dts    tresult等于0  xts=dts   tresult小于0  xts<dts
		int tresult = xts.compareTo(dts);
		
		//当前湿度
		String xhs=app.getBarCodeHem();
		//待酒湿度
		String dhs=app.getBarCodeHemappropriate();
		//result大于0  xhs>dhs    result等于0  xhs=dhs   result小于0  xhs<dhs
		int hresult = xhs.compareTo(dhs);
		
		//当前温度和湿度小于待酒或者大于待酒
		if (tresult<0 || hresult<0 || tresult>0 || hresult>0) {
			//设置温馨提示图片 危险
			prompt.setBackgroundResource(R.drawable.wenxintishikuangaa);
			prompt_text.setTextColor(0xf86666);
			prompt_twotext.setTextColor(0xf86666);
		/*	//当前温度低于待酒  保温
			if (tresult<0) {
				
			}
			//当前湿度度低于待酒  保湿
			else{
				
			}*/
			
		}
		//当前温度和湿度等于待酒
		else if(tresult==0 || hresult==0){
			//设置温馨提示图片 正常
			prompt.setBackgroundResource(R.drawable.wenxintishikuanga);
			prompt_text.setTextColor(0xf2aa10);
			prompt_twotext.setTextColor(0xf2aa10);
			//当前温度等于待酒  正常
			/*if (tresult==0) {
				
			}
			//当前湿度等于待酒  正常
			else{
				
			}*/
			
		}
		/*//当前温度和湿度大于待酒
		else if (tresult>0 || hresult>0)
			//设置温馨提示图片 危险
			prompt.setBackgroundResource(R.drawable.wenxintishikuangaa);
			prompt_text.setTextColor(0xf86666);*/
			/*//当前温度大于待酒  降温
			if (tresult>0) {
				
			}
			//当前湿度大于待酒  降温
			else{
				
			}*/
		}
	
		
	/**初始化*/
	private void initUi(){
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(this);
		
		click=(ImageButton) findViewById(R.id.click);
		click.setOnClickListener(this);
		
		url=(ImageView) findViewById(R.id.iv);
		
		name=(TextView) findViewById(R.id.name);
		
		brand=(TextView) findViewById(R.id.brand);
		
		origin=(TextView) findViewById(R.id.origin);
		
		type=(TextView) findViewById(R.id.tv5);
		
		typeContent=(TextView) findViewById(R.id.type);
		
		temappropriate=(TextView) findViewById(R.id.temappropriate);
		
		hemappropriate=(TextView) findViewById(R.id.hemappropriate);
		
		prompt=(ImageView) findViewById(R.id.prompt);
		
		prompt_text=(TextView) findViewById(R.id.prompt_text);
		
		prompt_twotext=(TextView) findViewById(R.id.prompt_twotext);
		
		tem=(TextView) findViewById(R.id.tem);
		
		hum=(TextView) findViewById(R.id.hum);
		
	}
	
	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		
		if (v==ib_return) {
			//扫码页面
			startActivity(new Intent(ResultStrDetailsActivity.this, CaptureActivity.class));
			finish();
		}else if(v==click){
			Toast.makeText(ResultStrDetailsActivity.this, "收藏成功！",
					Toast.LENGTH_LONG).show();
		}
	}
}
