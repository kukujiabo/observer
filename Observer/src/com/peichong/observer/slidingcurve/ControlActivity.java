package com.peichong.observer.slidingcurve;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.peichong.observer.R;
import com.peichong.observer.about.AboutActivity;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.analysislog.AnalysisLogActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.equipmentmgm.EquipmentMgmActivity;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.personalcenter.PersonalCenterActivity;
import com.peichong.observer.set.SetActivity;
import com.peichong.observer.slidingcurve.CustomMenu.OnScrollListener;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.FileUtils;
import com.peichong.observer.tools.LogUtil;
import com.peichong.observer.tools.Utils;
import com.peichong.observer.versionupdate.VersionUpdateActivity;
import com.peichong.observer.warning.WarningActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * TODO: 滑动曲线图
 * 
 * @author: wy
 * @version: V1.0
 */
public class ControlActivity extends BaseActivity implements OnClickListener,
		OnTouchListener, GestureDetector.OnGestureListener, OnItemClickListener {
	
	/**应用程序全局属性*/
	private ObserverApplication app;
	
	/*@SuppressWarnings("unused")
	private MyHorizontalScrollView studyGraphLayout;
	private StudyGraphView studyGraph;
	private ArrayList<StudyGraphItem> studyGraphItems;
	@SuppressWarnings("unused")
	private ArrayList<PointF> pointList;*/

	/*******温度湿度滚动栏******/
	private Context context;
	private List<TemperatureInfo> temperatureList;
	private List<HumidityInfo> humidityList;
	private TemperatureHorizontalScrollView mHorizontalScrollView;
	private TemperatureAdapter tAdapter;
	private HumidityAdapter hAdapter;
	
	
	/*******侧滑菜单*******/
	private boolean hasMeasured = false;// 是否Measured.
	private LinearLayout layout_left;// 左边布局
	private LinearLayout layout_right;// 右边布局
	private ListView lv_set;// 设置菜单
	private MenuAdapter Menuadapter;
	/** 每次自动展开/收缩的范围 */
	private int MAX_WIDTH = 0;
	/** 每次自动展开/收缩的速度 */
	private final static int SPEED = 10;

	private final static int sleep_time = 5;

	private GestureDetector mGestureDetector;// 手势
	private boolean isScrolling = false;
	private float mScrollX; // 滑块滑动距离
	private int window_width;// 屏幕的宽度
	private View view = null;// 点击的view
	private CustomMenu custom;
	
	/**控制台文件保存的类型*/
	public static int chooseType=1;

	/**获取控制台文件保存的类型*/
	//private String getType;
	
	/** 菜单 */
	private ImageView menu;
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

	/** 个人中心 */
	private ImageButton user_icon;

	private BaseStringRequest mStringRequest;

	/** 用户的ID 从用户登录数据中取的 */
	private String uid = "";

	/** 仪器id */
	private String mid = "";
	
	/** 机器地址 */
	private String address = "";
	
	/** 机器类型 （0 温度 1湿度） */
	private String type = "";

	/**  */
	private String tid;
	
	/**  */
	private String tcomp;
	
	/**  */
	private String hid;
	
	/**  */
	private String hcomp;

	/** 设置id */
	private String sid = "";
	
	/**温度图温度Y*/
	private Float data_t;
	
	/**温度图时间X*/
	private String created_at_t;
	
	/**温度图转换后的时间Y*/
	private String s_t;
	
	/**湿度图温度Y*/
	private Float data_h;
	
	/**湿度图时间X*/
	private String created_at_h;
	
	/**湿度图转换后的时间Y*/
	private String s_h;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		context=this;
		//拿到application对象
		app = (ObserverApplication) getApplication();
		
		initUi();
		// 获取用户仪器的信息 
		getUserInstrumentInformation();
		
		//getNewestTemperature();

		//getConsoleGraphHumidity();

		//getNewestHumidity();

		//getUserInstrumentInformation();

		//getIdInstrumentInformation();

		//getUserConfigurationInformation();

		//getUserSpecifiedConfigurationInformation();
	}
	

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("CutPasteId")
	private void initUi() {

		/*studyGraphLayout = (MyHorizontalScrollView) findViewById(R.id.study_graph_layout);
		studyGraph = (StudyGraphView) findViewById(R.id.study_graph);*/
		
		warning = (ImageButton) findViewById(R.id.warning);
		
		information = (ImageButton) findViewById(R.id.information);

		temperature = (ImageButton) findViewById(R.id.temperature);
		
		humidity = (ImageButton) findViewById(R.id.humidity);
		
		time = (ImageButton) findViewById(R.id.time);

		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		tv_time = (TextView) findViewById(R.id.tv_time);

		user_icon = (ImageButton) findViewById(R.id.user_icon);
		

		warning.setOnClickListener(this);
		information.setOnClickListener(this);

		temperature.setOnClickListener(this);
		humidity.setOnClickListener(this);
		time.setOnClickListener(this);

		user_icon.setOnClickListener(this);
		
		// 拿到温度
		//temperature_string = tv_temperature.getText().toString().trim();

		// 拿到湿度
		//humidity_string = tv_humidity.getText().toString().trim();

		// 拿到时间
		//time_string = tv_time.getText().toString().trim();

		// 温度曲线图按钮加提示
		//View target = findViewById(R.id.temperature);
		//BadgeView badge = new BadgeView(this, target);
		//badge.setText("!");
		//badge.show();

		// 湿度曲线图按钮加提示
		//View targetTwo = findViewById(R.id.humidity);
		//BadgeView badgeTwo = new BadgeView(this, targetTwo);
		//badgeTwo.setText("!");
		//badgeTwo.show();

		
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		menu = (ImageView) findViewById(R.id.menu);
		lv_set = (ListView) findViewById(R.id.lv_set);
		custom = (CustomMenu) findViewById(R.id.mylaout);

		List<MenuInfo> list = initRightMenus();
		Menuadapter = new MenuAdapter(this, list);
		lv_set.setAdapter(Menuadapter);

		/***
		 * 实现该接口
		 */
		custom.setOnScrollListener(new OnScrollListener() {
			@Override
			public void doScroll(float distanceX) {
				doScrolling(distanceX);
			}

			@Override
			public void doLoosen() {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
						.getLayoutParams();
				Log.e("wy:", "layoutParams.leftMargin="
						+ layoutParams.leftMargin);
				// 缩回去
				if (layoutParams.leftMargin < -window_width / 2) {
					new AsynMove().execute(-SPEED);
				} else {
					new AsynMove().execute(SPEED);
				}
			}
		});

		lv_set.setOnItemClickListener(this);
		layout_right.setOnTouchListener(this);
		layout_left.setOnTouchListener(this);
		menu.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this);
		// 禁用长按监听
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
		
		
		mHorizontalScrollView=(TemperatureHorizontalScrollView)findViewById(R.id.horizontal_scrollview);
		mHorizontalScrollView.setImageView((ImageView) findViewById(R.id.iv_pre),(ImageView) findViewById(R.id.iv_next));
		
		
		/*mHorizontalScrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent e) {

				if(MotionEvent.ACTION_UP == e.getAction()){
					 if (v.getScrollX()==0 ) { 
						 LogUtil.showLog("111111");
					 }else if (v.getScrollX()==maxScrollX){
						
					 }
				}
				
				return false;
			}
		});*/
		
		mHorizontalScrollView.setOnItemClickListener(new TemperatureHorizontalScrollView.OnItemClickListener() {
			
			@Override
			public void click(int position) {
				//获取控制台类型默认为温度显示图（1 温度显示图 ---- 2湿度显示图）
				if (chooseType==1) {
					//温度显示
					tv_temperature.setText(temperatureList.get(position).getTemperature().toString());
				}
				else if(chooseType==2){
					//湿度显示
					tv_humidity.setText(humidityList.get(position).getHumidity().toString());
				}else{
					Toast.makeText(ControlActivity.this, "chooseType:"+chooseType,
							Toast.LENGTH_LONG).show();
				}
			}
		});		
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub

	}

	public interface OnScrollListerner {
		void optionScrollEvent(int currentIndex);
	}

	/**温度滚动栏*//*
	private List<TemperatureInfo> buildNavigation() {
		List<TemperatureInfo> navigations = new ArrayList<TemperatureInfo>();
		navigations.add(new TemperatureInfo((float) 15.55, "00:00"));
		navigations.add(new TemperatureInfo((float) 10.55, "01:00"));
		navigations.add(new TemperatureInfo((float) 8.55, "02:00"));
		navigations.add(new TemperatureInfo((float) 5.5, "03:00"));
		navigations.add(new TemperatureInfo((float) 7, "05:00"));
		navigations.add(new TemperatureInfo((float) 11.11, "06:00"));
		navigations.add(new TemperatureInfo((float) 22.22, "07:00"));
		navigations.add(new TemperatureInfo((float) 33.33, "08:00"));
		return navigations;
	}*/
	
	/** 菜单抽屉实体类 */
	private List<MenuInfo> initRightMenus() {
		List<MenuInfo> templist = new ArrayList<MenuInfo>();
		templist.add(new MenuInfo("控制台"));
		templist.add(new MenuInfo("资讯"));
		templist.add(new MenuInfo("警告"));
		templist.add(new MenuInfo("设置"));
		templist.add(new MenuInfo("分析日志"));
		templist.add(new MenuInfo("设备管理"));
		templist.add(new MenuInfo("版本更新"));
		templist.add(new MenuInfo("关于我们"));
		return templist;
	}

	/***
	 * listview 正在滑动时执行.
	 */
	public void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:向左为正，右为负

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		layoutParams.leftMargin -= mScrollX;
		layoutParams_1.leftMargin = window_width + layoutParams.leftMargin;
		if (layoutParams.leftMargin >= 0) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			layoutParams.leftMargin = 0;
			layoutParams_1.leftMargin = window_width;

		} else if (layoutParams.leftMargin <= -MAX_WIDTH) {
			// 拖过头了不需要再执行AsynMove了
			isScrolling = false;
			layoutParams.leftMargin = -MAX_WIDTH;
			layoutParams_1.leftMargin = window_width - MAX_WIDTH;
		}
		Log.v("wy:", "layoutParams.leftMargin=" + layoutParams.leftMargin
				+ ",layoutParams_1.leftMargin =" + layoutParams_1.leftMargin);

		//layout_left.setLayoutParams(layoutParams);
		layout_right.setLayoutParams(layoutParams_1);
	}

	/***
	 * 获取移动距离 移动的距离其实就是layout_left的宽度
	 */
	public void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = layout_right.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = custom
							.getLayoutParams();
					// 注意： 设置layout_left的宽度。防止被在移动的时候控件被挤压
					layoutParams.width = window_width;
					layout_left.setLayoutParams(layoutParams);

					// 设置layout_right的初始位置.
					layoutParams_1.leftMargin = window_width;
					layout_right.setLayoutParams(layoutParams_1);
					// 注意：设置lv_set的宽度防止被在移动的时候控件被挤压
					layoutParams_2.width = MAX_WIDTH;
					custom.setLayoutParams(layoutParams_2);

					Log.v("wy:", "MAX_WIDTH=" + MAX_WIDTH + "width="
							+ window_width);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				new AsynMove().execute(SPEED);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		view = v;// 记录点击的控件

		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// 缩回去
			if (layoutParams.leftMargin < -window_width / 2) {
				new AsynMove().execute(-SPEED);
			} else {
				new AsynMove().execute(SPEED);
			}
		}

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {

		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		// 将之改为true，才会传递给onSingleTapUp,不然事件不会向下传递.
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/***
	 * 点击松开执行
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 点击的不是layout_left
		if (view != null && view == menu) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// 左移动
			if (layoutParams.leftMargin >= 0) {
				new AsynMove().execute(-SPEED);
				lv_set.setSelection(0);// 设置为首位.
			} else {
				// 右移动
				new AsynMove().execute(SPEED);
			}
		} else if (view != null && view == layout_left) {
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				// 说明layout_left处于移动最左端状态，这个时候如果点击layout_left应该直接所以原有状态.(更人性化)
				// 右移动
				new AsynMove().execute(SPEED);
			}
		}

		return true;
	}

	/***
	 * 滑动监听 就是一个点移动到另外一个点. distanceX=后面点x-前面点x，如果大于0，说明后面点在前面点的右边及向右滑动
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], 0);
				layoutParams_1.leftMargin = Math.min(layoutParams_1.leftMargin
						+ values[0], window_width);
				Log.v("wy:", "layout_left右" + layoutParams.leftMargin
						+ ",layout_right右" + layoutParams_1.leftMargin);
			} else {
				// 左移动
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], -MAX_WIDTH);
				layoutParams_1.leftMargin = Math.max(layoutParams_1.leftMargin
						+ values[0], window_width - MAX_WIDTH);
				Log.v("wy:", "layout_left左" + layoutParams.leftMargin
						+ ",layout_right左" + layoutParams_1.leftMargin);
			}
			layout_right.setLayoutParams(layoutParams_1);
			layout_left.setLayoutParams(layoutParams);

		}

	}

	/** 条目点击 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		Intent intent = null;
		// 只要没有滑动则都属于点击
		if (layoutParams.leftMargin == -MAX_WIDTH)
			switch (position) {

			// 曲线图页面
			case 0:
				intent = new Intent(view.getContext(), ControlActivity.class);
				break;

			// 资讯页面
			case 1:
				intent = new Intent(view.getContext(),
						InformationActivity.class);
				break;

			// 警告页面
			case 2:
				intent = new Intent(view.getContext(), WarningActivity.class);
				break;

			// 设置页面
			case 3:
				intent = new Intent(view.getContext(), SetActivity.class);
				break;

			// 分析日志页面
			case 4:
				intent = new Intent(view.getContext(),
						AnalysisLogActivity.class);
				break;

			// 设备管理页面
			case 5:
				intent = new Intent(view.getContext(),
						EquipmentMgmActivity.class);
				break;

			// 版本更新页面
			case 6:
				intent = new Intent(view.getContext(),
						VersionUpdateActivity.class);
				break;

			// 关于页面
			case 7:
				intent = new Intent(view.getContext(), AboutActivity.class);
				break;
			}

		// 页面跳转
		if (intent != null) {
			// intent.putExtra("lv_item_id", id);
			startActivity(intent);
		}
	}

	/** 温度曲线图 *//*
	public void TemperatureCurve() {
		studyGraphItems = new ArrayList<StudyGraphItem>();
		//studyGraphItems.add(new StudyGraphItem(data_s, s_s));
		studyGraphItems.add(new StudyGraphItem("00:00", (float) 5.5));
		studyGraphItems.add(new StudyGraphItem("01:00", (float) 6.5));
		studyGraphItems.add(new StudyGraphItem("02:00", (float) 5.9));
		studyGraphItems.add(new StudyGraphItem("03:00", (float) 7.5));
		studyGraphItems.add(new StudyGraphItem("04:00", 8));
		studyGraphItems.add(new StudyGraphItem("05:00", 11));
		studyGraphItems.add(new StudyGraphItem("06:00", 13));
		studyGraphItems.add(new StudyGraphItem("07:00", (float) 13.5));
		studyGraphItems.add(new StudyGraphItem("08:00", (float) 9.55));
		studyGraphItems.add(new StudyGraphItem("09:00", 12));
		studyGraphItems.add(new StudyGraphItem("10:00", (float) 7.66));
		studyGraphItems.add(new StudyGraphItem("11:00", 8));
		studyGraphItems.add(new StudyGraphItem("12:00", 5));
		studyGraph.setData(studyGraphItems);
		pointList = studyGraph.getPoints();

	}*/

	/** 湿度曲线图 *//*
	public void HumidityDiagram() {
		studyGraphItems = new ArrayList<StudyGraphItem>();

		StudyGraphItem stu1 = new StudyGraphItem();
		stu1.setDate("00:00");
		stu1.setHumidity(5);
		studyGraphItems.add(stu1);

		studyGraph.setData(studyGraphItems);

		pointList = studyGraph.getPoints();
	}
*/
	/** 按钮点击 */
	@Override
	public void onClick(View v) {

		if (v == warning) {
			// 警告页面
			startActivity(new Intent(ControlActivity.this,
					WarningActivity.class));
			finish();
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(ControlActivity.this,
					InformationActivity.class));
		} else if (v == temperature) {
			// 温度曲线图
			chooseType=1;
			getUserInstrumentInformation();
			Toast.makeText(ControlActivity.this, "温度曲线图:"+chooseType, Toast.LENGTH_LONG)
					.show();
		} else if (v == humidity) {
			// 湿度曲线图
			chooseType=2;
			getUserInstrumentInformation();
			Toast.makeText(ControlActivity.this, "湿度曲线图:"+chooseType, Toast.LENGTH_LONG)
					.show();
		} else if (v == time) {
			// 设置时间界面
			startActivity(new Intent(ControlActivity.this, TimeActivity.class));
		} else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(ControlActivity.this,
					PersonalCenterActivity.class));
		}
	}

	/** 控制台曲线图温度获取接口 
	 * @return */
	public void getConsoleGraphTemperature() {

		uid = app.getUid();
		mid = app.getMid();
		tid ="";
		tcomp="";

		String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "tid=" + tid + "&" + "comp=" + tcomp;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_CONSOLE_GRAPH_TEMPERATURE + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						
						if (temperatureList == null) {
							
							temperatureList = new ArrayList<TemperatureInfo>();
							
						} else {
							
							temperatureList.clear();
							
							
						}
						

						
						LogUtil.showLog("========请求控制台曲线图温度获取接口数据========",
								"response:" + response);
						try {
							//接口返回的数据
							JSONObject comJson = new JSONObject(response);
							
							//JSONObject中的字段
							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									//JSONObject中的字段
									JSONArray array = comJson
						 					.getJSONArray("data"); 	
									
									//studyGraphItems = new ArrayList<StudyGraphItem>();
									int length = array.length();
									for (int i = 0; i < length; i++) {
										//JSONArray中的字段
										 	JSONObject jo = array.optJSONObject(i);
										    data_t = (float) jo.getDouble("data");
										    created_at_t = jo.getString("created_at");
										    
										    tid=jo.getString("id");
										    //tcomp=jo.getString("comp");
										    
										    try {
										    	//日期转换
												s_t=Utils.date(created_at_t);
												//温度曲线图
												//studyGraphItems.add(new StudyGraphItem(s, data));
												
											} catch (Exception e) {
												e.printStackTrace();
											}
										    
										    temperatureList.add(new TemperatureInfo(data_t, s_t));
									}	
									
									if (tAdapter == null) {
										tAdapter = new TemperatureAdapter(ControlActivity.this, temperatureList);
									
										mHorizontalScrollView.setAdapter(tAdapter);
									
										tv_temperature.setText(temperatureList.get(0).getTemperature().toString());
									
									} else {			
										
										mHorizontalScrollView.setAdapter(tAdapter);
										
										tAdapter.notifyDataSetChanged();
										
									}
									//studyGraph.setData(studyGraphItems);
									//pointList = studyGraph.getPoints();
									//navs.add(new TemperatureInfo(data_t, s_t));
									
									LogUtil.showLog(
											"==========控制台曲线图温度获取接口请求成功:====",
											"获取的数据：" + array);
									// todo: parse data.
								}
								
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========控制台曲线图温度获取接口请求失败:====",
											"失败原因：" + msg);
								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						dismissProgressDialog();
					}
					
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						LogUtil.showLog("==========控制台曲线图温度获取接口错误:====",
								"失败原因：" + error.toString());
					}
				});
		
			mRequestQueue.add(mStringRequest);
	}

	/** 获取最新温度的接口 */
	public void getNewestTemperature() {

		uid = app.getUid();
		mid = app.getMid();

		String url = "uid=" + uid + "&" + "mid=" + mid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_NEWEST_TEMPERATURE + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求获取最新温度的接口数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {

								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONArray array = comJson
											.getJSONArray("data");
									// JSONArray jarray = (JSONArray)
									// comJson.get("data");
									LogUtil.showLog(
											"==========请求获取最新温度接口请求成功:====",
											"获取的数据：" + array);
									// todo: parse data.
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求获取最新温度请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						LogUtil.showLog("==========请求获取最新温度的接口错误:====",
								"获取的数据：" + error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}

	/** 控制台湿度获取接口 */
	public void getConsoleGraphHumidity() {

		uid = app.getUid();
		mid = app.getMid();
		hid ="";
		hcomp="";

		String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "hid=" + hid + "&" + "comp=" + hcomp;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_CONSOLE_GRAPH_HUMIDITY + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						if (humidityList == null) {
							
							humidityList = new ArrayList<HumidityInfo>();
							
						} else {
							
							humidityList.clear();
							
						}
						LogUtil.showLog("========请求控制台湿度获取接口数据========",
								"response:" + response);
						try {
							//接口返回的数据
							JSONObject comJson = new JSONObject(response);
							
							//JSONObject中的字段
							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									//JSONObject中的字段
									JSONArray array = comJson
											.getJSONArray("data"); 	
									
									int length = array.length();
									for (int i = 0; i < length; i++) {
										//JSONArray中的字段
										 	JSONObject jo = array.optJSONObject(i);
										    data_h = (float) jo.getDouble("data");
										    created_at_h = jo.getString("created_at");
										    hid=jo.getString("id");
										   // hcomp=jo.getString("comp");
										    
										    try {
										    	//日期转换
												s_h=Utils.date(created_at_h);
												
											} catch (Exception e) {
												e.printStackTrace();
											}
										    
										    humidityList.add(new HumidityInfo(data_h, s_h));
									}	
									
									if (hAdapter == null) {
										
										hAdapter = new HumidityAdapter(ControlActivity.this, humidityList);
										mHorizontalScrollView.setAdapter(hAdapter);
										tv_humidity.setText(humidityList.get(0).getHumidity().toString());
									} else {
										
										mHorizontalScrollView.setAdapter(hAdapter);
									 	
										tAdapter.notifyDataSetChanged();
										
									}

									
									LogUtil.showLog(
											"==========控制台曲线图湿度获取接口请求成功:====",
											"获取的数据：" + array);
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求控制台湿度接口请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						dismissProgressDialog();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						LogUtil.showLog("==========控制台湿度获取接口错误:====", "获取的数据："
								+ error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}

	/** 获取最新湿度的接口 */
	public void getNewestHumidity() {

		uid = app.getUid();
		mid = app.getMid();

		String url = "uid=" + uid + "&" + "mid=" + mid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_NEWEST_HUMIDITY + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求获取最新湿度的接口数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONArray array = comJson
											.getJSONArray("data");
									// JSONArray jarray = (JSONArray)
									// comJson.get("data");
									LogUtil.showLog(
											"==========请求获取最新湿度接口请求成功:====",
											"获取的数据：" + array);
									// todo: parse data.
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求获取最新湿度接口请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						LogUtil.showLog("==========获取最新湿度的接口错误:====", "获取的数据："
								+ error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}

	/** 获取用户仪器的信息 */
	public void getUserInstrumentInformation() {
		showProgressDialog();
		uid = app.getUid();
		String url = "uid=" + uid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.USER_INSTRUMENT_INFROMATION + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求获取用户仪器的信息数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONArray array = comJson
											.getJSONArray("mechine_info");
									
									int length = array.length();
									for (int i = 0; i < length; i++) {
										//JSONArray中的字段
										 	JSONObject jo = array.optJSONObject(i);
										 	mid = jo.getString("id");
										 	address = jo.getString("address");
										 	type = jo.getString("type");
										   
										 	//把mid缓存到Application,可供所有activity使用
											app.setMid(mid);
											
										    LogUtil.showLog(
													"+++++++++++++mid+++++++++++++++++:",
													"mid：" + mid);
										    LogUtil.showLog(
													"++++++++++++++address++++++++++++++++:",
													"address：" + address);
										    LogUtil.showLog(
													"++++++++++++++type++++++++++++++++:",
													"type：" + type);
										    
										    //如果机器类型为0，并且控制台类型为1，则为温度显示图
										   if (type.equals("0") && chooseType==1) {
										    	getConsoleGraphTemperature();
											}
										   else if(type.equals("1") && chooseType==2){
											   getConsoleGraphHumidity();
										   }
											else{
												Toast.makeText(ControlActivity.this, "type:"+type +"chooseType:"+chooseType,
														Toast.LENGTH_LONG).show();
											}
									}
									LogUtil.showLog(
											"==========请求获取用户仪器的信息接口请求成功:====",
											"获取的数据：" + array);
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求获取用户仪器的信息接口请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
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
						LogUtil.showLog("==========获取用户仪器的信息错误:====", "获取的数据："
								+ error.toString());
					}
					
				});
		mRequestQueue.add(mStringRequest);
	}

	/** 根据仪器id获取仪器信息 */
	public void getIdInstrumentInformation() {

		uid = app.getUid();
		mid = app.getMid();

		String url = "uid=" + uid + "&" + "mid=" + mid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.IN_INSTRUMENT_INFROMATION + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求根据仪器id获取仪器信息数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONArray array = comJson
											.getJSONArray("data");
									// JSONArray jarray = (JSONArray)
									// comJson.get("data");
									LogUtil.showLog(
											"==========请求根据仪器id获取仪器信息接口请求成功:====",
											"获取的数据：" + array);
									// todo: parse data.
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求根据仪器id获取仪器信息接口请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						LogUtil.showLog("==========根据仪器id获取仪器信息错误:====",
								"获取的数据：" + error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}

	

	/** 获取用户指定的配置信息 */
	public void getUserSpecifiedConfigurationInformation() {

		uid = app.getUid();
		sid = "12";

		String url = "uid=" + uid + "&" + "sid=" + sid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.USER_SPECIFIED_CONFIGURATION_INFROMATION
						+ url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求获取用户指定的配置信息数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONArray array = comJson
											.getJSONArray("data");
									// JSONArray jarray = (JSONArray)
									// comJson.get("data");
									LogUtil.showLog(
											"==========请求获取用户指定的配置信息接口请求成功:====",
											"获取的数据：" + array);
									// todo: parse data.
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求获取用户指定的配置信息接口请求失败:====",
											"失败原因：" + msg);

								} else {

									try {
										throw new Exception();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								try {
									throw new Exception();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						LogUtil.showLog("==========获取用户指定的配置信息错误:====",
								"获取的数据：" + error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}
	/*public static void wy(){
		LogUtil.showLog("11111111");
	}
	public static void wy2(){
		LogUtil.showLog("22222222222");
	}*/
}
