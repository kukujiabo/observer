package com.peichong.observer.slidingcurve;

import java.util.ArrayList;
import java.util.List;

import com.peichong.observer.R;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.slidingcurve.CustomMenu.OnScrollListener;
import com.peichong.observer.warning.WarningActivity;
import com.readystatesoftware.viewbadger.BadgeView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
public class ControlActivity extends Activity implements OnClickListener,OnTouchListener,
GestureDetector.OnGestureListener, OnItemClickListener{
	private MyHorizontalScrollView studyGraphLayout;
	private StudyGraphView studyGraph;
	private ArrayList<StudyGraphItem> studyGraphItems;
	private ArrayList<PointF> pointList;
	
	private boolean hasMeasured = false;// 是否Measured.
	private LinearLayout layout_left;// 左边布局
	private LinearLayout layout_right;// 右边布局
	private ListView lv_set;// 设置菜单
	private MenuAdapter adapter;
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

	private String title[] = { 
			"控制台", "资讯", "警告", "设置","分析日志","设备管理","版本更新","关于我们"};

	private CustomMenu custom;
	
	
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control);
		initUi();
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

		// 温度曲线图按钮加提示
		View target = findViewById(R.id.temperature);
		BadgeView badge = new BadgeView(this, target);
		badge.setText("!");
		badge.show();
             
		// 湿度曲线图按钮加提示
		View targetTwo = findViewById(R.id.humidity);
		BadgeView badgeTwo = new BadgeView(this, targetTwo);
		badgeTwo.setText("!");
		badgeTwo.show();

		// 温度曲线图
		TemperatureCurve();
		
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		lv_set = (ListView) findViewById(R.id.lv_set);
		custom = (CustomMenu) findViewById(R.id.mylaout);
		
		List<MenuInfo> list=initRightMenus();
		adapter=new MenuAdapter(this, list);
		lv_set.setAdapter(adapter);
		
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

	}

	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub

	}

	public interface OnScrollListerner {
		void optionScrollEvent(int currentIndex);
	}

	/**菜单抽屉实体类*/
	private List<MenuInfo> initRightMenus(){
		List<MenuInfo> templist=new ArrayList<MenuInfo>();
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
	void doScrolling(float distanceX) {
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

		layout_left.setLayoutParams(layoutParams);
		layout_right.setLayoutParams(layoutParams_1);
	}
	
	/***
	 * 获取移动距离 移动的距离其实就是layout_left的宽度
	 */
	void getMAX_WIDTH() {
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
		if (view != null && view == custom) {
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		// 只要没有滑动则都属于点击
		if (layoutParams.leftMargin == -MAX_WIDTH)
			Toast.makeText(ControlActivity.this, title[position], 1).show();
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
	public void HumidityDiagram() {
		studyGraphItems = new ArrayList<StudyGraphItem>();

		StudyGraphItem stu1 = new StudyGraphItem();
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
		} else if (v == temperature) {
			// 温度曲线图
			Toast.makeText(ControlActivity.this, "温度曲线图:", Toast.LENGTH_LONG)
					.show();
		} else if (v == humidity) {
			// 湿度曲线图
			Toast.makeText(ControlActivity.this, "湿度曲线图:", Toast.LENGTH_LONG)
					.show();
		} else if (v == time) {
			// 设置时间界面
			startActivity(new Intent(ControlActivity.this, TimeActivity.class));
		}
	}
}
