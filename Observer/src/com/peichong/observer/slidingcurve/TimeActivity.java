package com.peichong.observer.slidingcurve;

import com.peichong.observer.R;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.warning.WarningActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO: 设置时间
 * 
 * @author: wy
 * @version: V1.0
 */
public class TimeActivity extends Activity implements OnClickListener {

	/** 菜单 */
	private ImageButton menu;
	/** 警告 */
	private ImageButton warning;
	/** 资讯 */
	private ImageButton information;

	/** 温度曲线图按钮 */
	private ImageButton temperature;

	/** 湿度曲线图按钮 */
	private ImageButton humidity;

	/** 设置温度 */
	private TextView tv_temperature;

	/** 设置湿度 */
	private TextView tv_humidity;

	/** 设置时间 */
	private TextView time_show;
	
	/** 获取时间 */
	private TextView tv_time;

	/** 温度 */
	private String temperature_string = "";

	/** 湿度 */
	private String humidity_string = "";

	/** 时间 */
	private String time_string = "";
	
	/**时间设置条*/
	private SeekBar time_seekbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		initUi();
		
		//把滑动的时间设置进去
		time_string=(String) time_show.getText();
		tv_time.setText(time_string);
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

		temperature = (ImageButton) findViewById(R.id.temperature);
		humidity = (ImageButton) findViewById(R.id.humidity);

		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		time_show = (TextView) findViewById(R.id.time_show);
		tv_time=(TextView) findViewById(R.id.tv_time);
		
		time_seekbar=(SeekBar) findViewById(R.id.time_seekbar);
		
		 //初始化
		time_seekbar.setProgress(1);
		//最大值
		time_seekbar.setMax(61);
		time_seekbar.setOnSeekBarChangeListener(seekListener);
		//初始化为一分钟
		time_show.setText("1");

		menu.setOnClickListener(this);
		warning.setOnClickListener(this);
		information.setOnClickListener(this);

		temperature.setOnClickListener(this);
		humidity.setOnClickListener(this);

		// 拿到温度
		temperature_string = tv_temperature.getText().toString().trim();

		// 拿到湿度
		humidity_string = tv_humidity.getText().toString().trim();

		// 拿到时间
		time_string = time_show.getText().toString().trim();
	}
	
	
	/**时间设置拖动条*/
	 private OnSeekBarChangeListener seekListener = new OnSeekBarChangeListener() {
		
		 /**停止拖动*/
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.i("wy:","停止拖动...");
		}
		
		/**开始拖动*/
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.i("wy:","开始拖动...");
		}
		
		/**改变数值*/
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			Log.i("wy:","改变数值..."); 
			time_show.setText(progress+"");
			tv_time.setText(progress+"");
		}
	};

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		if (v == menu) {
			// 侧滑菜单
			Toast.makeText(TimeActivity.this, "侧滑菜单:", Toast.LENGTH_LONG)
					.show();
		} else if (v == warning) {
			// 警告页面
			startActivity(new Intent(TimeActivity.this, WarningActivity.class));
			finish();
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(TimeActivity.this,
					InformationActivity.class));
			finish();
		}else if (v == temperature) {
			// 温度曲线图
			Toast.makeText(TimeActivity.this, "温度曲线图:", Toast.LENGTH_LONG)
					.show();
		} else if (v == humidity) {
			// 湿度曲线图
			Toast.makeText(TimeActivity.this, "湿度曲线图:", Toast.LENGTH_LONG)
					.show();
		}
	}
	
}
