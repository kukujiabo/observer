package com.peichong.observer.slidingcurve;

import java.util.ArrayList;

import com.peichong.observer.R;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.warning.WarningActivity;
import com.readystatesoftware.viewbadger.BadgeView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO: 滑动曲线图
 * 
 * @author: wy
 * @version: V1.0
 */
public class ControlActivity extends Activity implements OnClickListener {
	private MyHorizontalScrollView studyGraphLayout;
	private StudyGraphView studyGraph;
	private ArrayList<StudyGraphItem> studyGraphItems;
	private ArrayList<PointF> pointList;
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

	/** 时间设置按钮 */
	private ImageButton time;

	/** 设置温度 */
	private TextView tv_temperature;

	/** 设置湿度 */
	private TextView tv_humidity;

	/** 设置时间 */
	private TextView tv_time;

	/** 温度 */
	private String temperature_string = "";

	/** 湿度 */
	private String humidity_string = "";

	/** 时间 */
	private String time_string = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		initUi();
	}

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	@SuppressLint("CutPasteId")
	private void initUi() {
		
		studyGraphLayout = (MyHorizontalScrollView) findViewById(R.id.study_graph_layout);
		studyGraph = (StudyGraphView) findViewById(R.id.study_graph);
		menu = (ImageButton) findViewById(R.id.menu);
		warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);

		temperature = (ImageButton) findViewById(R.id.temperature);
		humidity = (ImageButton) findViewById(R.id.humidity);
		time = (ImageButton) findViewById(R.id.time);

		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		tv_time = (TextView) findViewById(R.id.tv_time);

		menu.setOnClickListener(this);
		warning.setOnClickListener(this);
		information.setOnClickListener(this);

		temperature.setOnClickListener(this);
		humidity.setOnClickListener(this);
		time.setOnClickListener(this);

		// 拿到温度
		temperature_string = tv_temperature.getText().toString().trim();

		// 拿到湿度
		humidity_string = tv_humidity.getText().toString().trim();

		// 拿到时间
		time_string = tv_time.getText().toString().trim();

		//温度曲线图按钮加提示
		View target = findViewById(R.id.temperature);
		BadgeView badge = new BadgeView(this, target);
		badge.setText("!");
		badge.show();
		
		//温度曲线图
		TemperatureCurve();
		
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub

	}

	public interface OnScrollListerner {
		void optionScrollEvent(int currentIndex);
	}

	/** 温度曲线图 */
	public void TemperatureCurve() {
		studyGraphItems = new ArrayList<StudyGraphItem>();
		studyGraphItems.add(new StudyGraphItem("00:00", 5));
		studyGraphItems.add(new StudyGraphItem("01:00", (float) 7.5));
		studyGraphItems.add(new StudyGraphItem("02:00", 10));
		studyGraphItems.add(new StudyGraphItem("03:00", (float) 15.5));
		studyGraphItems.add(new StudyGraphItem("04:00", 20));
		studyGraphItems.add(new StudyGraphItem("05:00", 17));
		studyGraphItems.add(new StudyGraphItem("06:00", 15));
		studyGraphItems.add(new StudyGraphItem("07:00", 13));
		studyGraphItems.add(new StudyGraphItem("08:00", 18));
		studyGraphItems.add(new StudyGraphItem("09:00", 11));
		studyGraphItems.add(new StudyGraphItem("10:00", 8));
		studyGraphItems.add(new StudyGraphItem("11:00", 5));
		studyGraphItems.add(new StudyGraphItem("12:00", 10));
		studyGraphItems.add(new StudyGraphItem("13:00", (float) 15.5));
		studyGraphItems.add(new StudyGraphItem("14:00", 8));
		studyGraphItems.add(new StudyGraphItem("15:00", 11));
		studyGraphItems.add(new StudyGraphItem("16:00", 10));
		studyGraphItems.add(new StudyGraphItem("17:00", 5));
		studyGraphItems.add(new StudyGraphItem("18:00", 8));
		studyGraphItems.add(new StudyGraphItem("19:00", 7));
		studyGraphItems.add(new StudyGraphItem("20:00", (float) 10.5));
		studyGraphItems.add(new StudyGraphItem("21:00", 5));
		studyGraphItems.add(new StudyGraphItem("22:00", 4));
		studyGraphItems.add(new StudyGraphItem("23:00", 17));
		studyGraphItems.add(new StudyGraphItem("24:00", 2));
		
		studyGraph.setData(studyGraphItems);

		pointList = studyGraph.getPoints();
		
	}

	/** 湿度曲线图 */
	public void HumidityDiagram(){
		studyGraphItems = new ArrayList<StudyGraphItem>();
		
		StudyGraphItem stu1= new StudyGraphItem();
		stu1.setDate("00:00");
		stu1.setHumidity(5);
		studyGraphItems.add(stu1);
		
		studyGraph.setData(studyGraphItems);

		pointList = studyGraph.getPoints();
	}
	
	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		if (v == menu) {
			// 侧滑菜单
			Toast.makeText(ControlActivity.this, "侧滑菜单:", Toast.LENGTH_LONG)
					.show();
		} else if (v == warning) {
			// 警告页面
			startActivity(new Intent(ControlActivity.this,
					WarningActivity.class));
			finish();
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(ControlActivity.this,
					InformationActivity.class));
			finish();
		}else if(v == temperature){
			//温度曲线图
			Toast.makeText(ControlActivity.this, "温度曲线图:", Toast.LENGTH_LONG)
			.show();
		}else if(v == humidity){
			//湿度曲线图
			Toast.makeText(ControlActivity.this, "湿度曲线图:", Toast.LENGTH_LONG)
			.show();
		}else if(v==time){
			//设置时间界面
			startActivity(new Intent(ControlActivity.this,
					TimeActivity.class));
		}
	}
}
