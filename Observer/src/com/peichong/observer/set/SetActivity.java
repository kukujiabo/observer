package com.peichong.observer.set;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.peichong.observer.R;
import com.peichong.observer.about.AboutActivity;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.analysislog.AnalysisLogActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.equipmentmgm.EquipmentMgmActivity;
import com.peichong.observer.information.InformationActivity;
import com.peichong.observer.personalcenter.PersonalCenterActivity;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.slidingcurve.CustomMenu;
import com.peichong.observer.slidingcurve.MenuAdapter;
import com.peichong.observer.slidingcurve.MenuInfo;
import com.peichong.observer.slidingcurve.CustomMenu.OnScrollListener;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.LogUtil;
import com.peichong.observer.versionupdate.VersionUpdateActivity;
import com.peichong.observer.warning.WarningActivity;


/** 
 * TODO:   	设置页面
 * @author:   wy 
 * @version:  V1.0 
 */
public class SetActivity extends BaseActivity implements OnClickListener,
OnTouchListener, GestureDetector.OnGestureListener, OnItemClickListener{

	/** 菜单 */
	private ImageButton menu;
	/** 警告 */
	private ImageButton warning;
	/** 资讯 */
	private ImageButton information;
	
	/** 个人中心 */
	private ImageButton user_icon;
	
	/**应用程序全局属性*/
	private ObserverApplication app;
	
	/** 用户的ID 从用户登录数据中取的 */
	private String uid = "";
	
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
	
	
	/**温度警告*/
	private ImageButton sethyperthermia;
	
	/**高温警告*/
	private TextView gethtem;
	
	/**修改高温警告*/
	private EditText sethtem;
	
	/**低温警告*/
	private TextView getltem;
	
	/**修改低温警告*/
	private EditText setltem;
	
	/**湿度警告*/
	private ImageButton sethumidity;
	
	/**高湿警告*/
	private TextView gethhum;
	
	/**修改高湿警告*/
	private EditText sethhum;
	
	/**低湿警告*/
	private TextView getlhum;
	
	/**修改低湿警告*/
	private EditText setlhum;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		//拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		getUserConfigurationInformation();
	}
	
	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	@SuppressWarnings("deprecation")
	private void initUi() {
		menu = (ImageButton) findViewById(R.id.menu);
		warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);
		user_icon = (ImageButton) findViewById(R.id.user_icon);
	
		menu.setOnClickListener(this);
		warning.setOnClickListener(this);
		information.setOnClickListener(this);
		user_icon.setOnClickListener(this);
		
		sethyperthermia=(ImageButton) findViewById(R.id.sethyperthermia);
		gethtem=(TextView) findViewById(R.id.gethtem);
		sethtem=(EditText) findViewById(R.id.sethtem);
		getltem=(TextView) findViewById(R.id.getltem);
		setltem=(EditText) findViewById(R.id.setltem);
		sethumidity=(ImageButton) findViewById(R.id.sethumidity);
		gethhum=(TextView) findViewById(R.id.gethhum);
		sethhum=(EditText) findViewById(R.id.sethhum);
		getlhum=(TextView) findViewById(R.id.getlhum);
		setlhum=(EditText) findViewById(R.id.setlhum);
		
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
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
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub

	}

	public interface OnScrollListerner {
		void optionScrollEvent(int currentIndex);
	}

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

		layout_left.setLayoutParams(layoutParams);
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

	
	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		if (v == menu) {
			// 侧滑菜单
			Toast.makeText(SetActivity.this, "侧滑菜单:", Toast.LENGTH_LONG)
					.show();
		} else if (v == warning) {
			// 警告页面
			startActivity(new Intent(SetActivity.this,
					WarningActivity.class));
			finish();
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(SetActivity.this,
					InformationActivity.class));
			finish();
		}
		else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(SetActivity.this,
					PersonalCenterActivity.class));
			finish();
		}
	}
	
	/** 获取用户的配置信息 */
	public void getUserConfigurationInformation() {
		showProgressDialog();
		uid = app.getUid();
		String url = "uid=" + uid;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.USER_CONFIGURATION_INFROMATION + url,
				new Response.Listener<String>() {
					@SuppressWarnings("deprecation")
					@Override
					public void onResponse(String response) {

						LogUtil.showLog("========请求获取用户的配置信息数据========",
								"response:" + response);
						try {
							JSONObject comJson = new JSONObject(response);

							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									JSONObject jo = comJson
											.getJSONObject("data");
									
									jo.getJSONObject("t_interval").getString("setting_value");
									jo.getJSONObject("h_interval").getString("setting_value");
									
									String tType=jo.getJSONObject("t_warning").getString("setting_value");
									if (tType.equals("1")) {
										sethyperthermia.setBackgroundDrawable(getResources().getDrawable(R.drawable.shezhishuanganniu));
									}else{
										sethyperthermia.setBackgroundDrawable(getResources().getDrawable(R.drawable.shezhianniu));
									}
									
									String hType=jo.getJSONObject("h_warning").getString("setting_value");
									if (hType.equals("1")) {
										sethumidity.setBackgroundDrawable(getResources().getDrawable(R.drawable.shezhishuanganniu));
									}else{
										sethumidity.setBackgroundDrawable(getResources().getDrawable(R.drawable.shezhianniu));
									}
									
									String ht=jo.getJSONObject("t_warning_high").getString("setting_value");
									gethtem.setText(ht);
									gethtem.setVisibility(View.INVISIBLE);
									sethtem.setText(ht);
									
									String lt=jo.getJSONObject("t_warning_low").getString("setting_value");
									getltem.setText(lt);
									getltem.setVisibility(View.INVISIBLE);
									setltem.setText(lt);
									
									String hh=jo.getJSONObject("h_warning_high").getString("setting_value");
									gethhum.setText(hh);
									gethhum.setVisibility(View.INVISIBLE);
									sethhum.setText(hh);
									
									String lh=jo.getJSONObject("h_warning_low").getString("setting_value");
									getlhum.setText(lh);
									getlhum.setVisibility(View.INVISIBLE);
									setlhum.setText(lh);
									
									LogUtil.showLog(
											"==========请求获取用户的配置信息接口请求成功:====",
											"获取的数据：" + jo);
								}
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========请求获取用户的配置信息接口请求失败:====",
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

						LogUtil.showLog("==========获取用户的配置信息错误:====", "获取的数据："
								+ error.toString());
					}
				});
		mRequestQueue.add(mStringRequest);
	}
}