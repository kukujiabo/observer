package com.peichong.observer.slidingcurve;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.peichong.observer.R;
import com.peichong.observer.about.AboutActivity;
import com.peichong.observer.activities.ActivityUtil;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.capture.CaptureActivity;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.equipmentmgm.EquipmentMgmActivity;
import com.peichong.observer.personalcenter.PersonalCenterActivity;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.DownLoadImage;
import com.peichong.observer.tools.LogUtil;
import com.peichong.observer.tools.Utils;
import com.peichong.observer.warning.WarningActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * TODO: 滑动温湿度图
 * 
 * @author: wy
 * @version: V1.0
 */
@SuppressLint("NewApi")
public class ControlActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	/** 应用程序全局属性 */
	private ObserverApplication app;

	/*
	 * @SuppressWarnings("unused") private MyHorizontalScrollView
	 * studyGraphLayout; private StudyGraphView studyGraph; private
	 * ArrayList<StudyGraphItem> studyGraphItems;
	 * 
	 * @SuppressWarnings("unused") private ArrayList<PointF> pointList;
	 */

	/******* 温度湿度滚动栏 ******/
	private Context context;
	private List<TemperatureInfo> temperatureList;
	private List<HumidityInfo> humidityList;
	private TemperatureHorizontalScrollView mHorizontalScrollView;
	private TemperatureAdapter tAdapter;
	private HumidityAdapter hAdapter;

	/******* 侧滑菜单 *******/
	private ListView lv_set;// 设置菜单

	private MenuAdapter Menuadapter;

	/** 控制台文件保存的类型 */
	public static int chooseType = 1;
	
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
	//private ImageButton time;

	/** 设置温度 */
	private TextView tv_temperature;

	/** 设置湿度 */
	private TextView tv_humidity;

	/** 设置时间 */
	//private TextView tv_time;

	/** 个人中心 */
	private ImageView user_icon;
	
	/**设置名字*/
	private TextView uese_name;

	/** 用户的ID 从用户登录数据中取的 */
	private String uid = "";

	/** 仪器id */
	private String mid = "";
	
	/** 仪器温度id */
	private String mtid = "";
	
	/** 仪器湿度id */
	private String mhid = "";
	
	/** 机器地址 */
	private String address = "";
	
	/**机器运行状态*/
	private String active="";
	
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

	/** 温度图温度Y */
	private float data_t;

	/** 温度图时间X */
	private String created_at_t;

	/** 温度图转换后的时间Y */
	private String s_t;

	/** 湿度图温度Y */
	private float data_h;

	/** 湿度图时间X */
	private String created_at_h;

	/** 湿度图转换后的时间Y */
	private String s_h;

	private ArrayList<String> tlist;

	private ArrayList<String> hlist;
	
	private SlidingMenu menus;
	
	private Resources res;
	
	private ImageView yuan;
	
	@SuppressLint("HandlerLeak")
	private Handler _scrollHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

			// 获取旧数据
			case 0:
				addOldData();
				break;

			// 获取新数据
			case 1:
				addNewData();
				break;
			default:
				break;
			}

		}

	};
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_control);
		context = this;
		// 获取Resources对象  
	    res = this.getResources();  
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		
		initUi();
		
		// 获取用户仪器的信息
        getUserInstrumentInformation();
        
}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		menus.showContent();
		//修改头像
				String s="http://218.244.135.148:8080"+app.getUrl().trim();
				if (app.getUrl().equals("") ||app.getUrl()==null) {
					Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.touxianggo);
					user_icon.setImageBitmap(b);
				}else{
					new DownLoadImage(user_icon).execute(s);
				}
	}
	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	@SuppressLint("CutPasteId")
	private void initUi() {
		/*
		 * studyGraphLayout = (MyHorizontalScrollView)
		 * findViewById(R.id.study_graph_layout); studyGraph = (StudyGraphView)
		 * findViewById(R.id.study_graph);
		 */
		warning = (ImageButton) findViewById(R.id.warning);

		information = (ImageButton) findViewById(R.id.information);

		temperature = (ImageButton) findViewById(R.id.temperature);

		humidity = (ImageButton) findViewById(R.id.humidity);


		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);

		warning.setOnClickListener(this);
		information.setOnClickListener(this);

		temperature.setOnClickListener(this);
		humidity.setOnClickListener(this);

		// 温度曲线图按钮加提示
		// View target = findViewById(R.id.temperature);
		// BadgeView badge = new BadgeView(this, target);
		// badge.setText("!");
		// badge.show();

		// 湿度曲线图按钮加提示
		// View targetTwo = findViewById(R.id.humidity);
		// BadgeView badgeTwo = new BadgeView(this, targetTwo);
		// badgeTwo.setText("!");
		// badgeTwo.show();

		menu = (ImageButton) findViewById(R.id.menu);
		menu.setOnClickListener(this);

		// configure the SlidingMenu
		menus = new SlidingMenu(this);
		menus.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menus.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// 设置滑动菜单视图的宽度
		menus.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		// 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
		menus.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 为侧滑菜单设置布局
		menus.setMenu(R.layout.activity_menu);

		user_icon = (ImageView) findViewById(R.id.user_icon);
		user_icon.setOnClickListener(this);
		uese_name=(TextView) findViewById(R.id.uese_name);
		
		uese_name.setText(app.getName());
		//修改头像
		String s="http://218.244.135.148:8080"+app.getUrl().trim();
		if (app.getUrl().equals("") ||app.getUrl()==null) {
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.touxianggo);
			user_icon.setImageBitmap(b);
		}else{
			new DownLoadImage(user_icon).execute(s);
		}

		lv_set = (ListView) findViewById(R.id.lv_set);
		List<MenuInfo> list = initRightMenus();
		Menuadapter = new MenuAdapter(this, list);
		lv_set.setAdapter(Menuadapter);
		lv_set.setOnItemClickListener(this);
		
		// 获取控制台类型默认为温度显示图（1 温度显示图 ---- 2湿度显示图）
		if (chooseType == 1) {
		temperature.setBackgroundResource(R.drawable.wendunliang);
		humidity.setBackgroundResource(R.drawable.shiduhui);
		}else if (chooseType == 2){
			temperature.setBackgroundResource(R.drawable.wenduhui);
			humidity.setBackgroundResource(R.drawable.shiduliang);
		}else {
			
		}
		
		yuan=(ImageView) findViewById(R.id.yuan);
		
		mHorizontalScrollView = (TemperatureHorizontalScrollView) findViewById(R.id.horizontal_scrollview);
		mHorizontalScrollView._scrollHandler = _scrollHandler;
		mHorizontalScrollView.setFadingEdgeLength(0);

		//回调事件  滑动的每一个item
		mHorizontalScrollView.setOnItemResetListener(new TemperatureHorizontalScrollView.OnItemResetListener() {
			
			@Override
			public void onReset(int position) {
				// 获取控制台类型默认为温度显示图（1 温度显示图 ---- 2湿度显示图）
				if (chooseType == 1) {
					// 温度显示
					float f=temperatureList.get(position).getTemperature();
					if (f>=15 && f<=16) {
						yuan.setBackgroundResource(R.drawable.kongwenyuan);
					}else if(f>=17 && f<=18){
						yuan.setBackgroundResource(R.drawable.kongwenyuann);
					}else if(f>=19 && f<=24){
						yuan.setBackgroundResource(R.drawable.kongwenyuannn);
					}else if(f>=25 && f<=26){
						yuan.setBackgroundResource(R.drawable.kongwenyuannnn);
					}else if(f>=27 && f<=28){
						yuan.setBackgroundResource(R.drawable.kongwenyuannnnn);
					}
				} else if (chooseType == 2) {
					// 湿度显示
					float f=humidityList.get(position).getHumidity();
					if (f>=75 && f<=76) {
						yuan.setBackgroundResource(R.drawable.kongwenyuan);
					}else if(f>=77 && f<=78){
						yuan.setBackgroundResource(R.drawable.kongwenyuann);
					}else if(f>=79 && f<=80){
						yuan.setBackgroundResource(R.drawable.kongwenyuannn);
					}else if(f>=81 && f<=82){
						yuan.setBackgroundResource(R.drawable.kongwenyuannnn);
					}else if(f>=83 && f<=85){
						yuan.setBackgroundResource(R.drawable.kongwenyuannnnn);
					}
				} else {
					
				}
			}
		});
		
		//回调事件  点击的每一个item
		mHorizontalScrollView
				.setOnItemClickListener(new TemperatureHorizontalScrollView.OnItemClickListener() {

					@Override
					public void click(int position) {
						// 获取控制台类型默认为温度显示图（1 温度显示图 ---- 2湿度显示图）
						if (chooseType == 1) {
							// 温度显示
							tv_temperature.setText((int) temperatureList.get(
									position).getTemperature()
									+ "");
						} else if (chooseType == 2) {
							// 湿度显示
							tv_humidity.setText((int) humidityList
									.get(position).getHumidity() + "");
						} else {
							
						}
					}
				});
	}

	/** 菜单抽屉实体类 */
	private List<MenuInfo> initRightMenus() {
		List<MenuInfo> templist = new ArrayList<MenuInfo>();
		templist.add(new MenuInfo(res.getDrawable(R.drawable.touxiaangtubiaoff)));
		templist.add(new MenuInfo(res.getDrawable(R.drawable.jianggaoo)));
		templist.add(new MenuInfo(res.getDrawable(R.drawable.touxiaangtubiaoc)));
		templist.add(new MenuInfo(res.getDrawable(R.drawable.touxiaangtubiaob)));
		templist.add(new MenuInfo(res.getDrawable(R.drawable.touxiaangtubiaoa)));
		return templist;
	}

	/** 条目点击 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		// 只要没有滑动则都属于点击
		switch (position) {
		// 控制台
		case 0:
		intent = new Intent(view.getContext(), ControlActivity.class);
		menus.showContent();
		break;
			
	    // 警告
		case 1:
		intent = new Intent(view.getContext(), WarningActivity.class);
		menus.showContent();
		break;
						
		// 设备管理
		case 2:
			intent = new Intent(view.getContext(), EquipmentMgmActivity.class);
			menus.showContent();
			break;

		// 版本更新
		case 3:
			//intent = new Intent(viaew.getContext(), VersionUpdateActivity.class);
			menus.showContent();
			updated();
			break;

		// 关于我们
		case 4:
			intent = new Intent(view.getContext(), AboutActivity.class);
			menus.showContent();
			break;

		}

		// 页面跳转
		if (intent != null) {
			// intent.putExtra("lv_item_id", id);
			startActivity(intent);
		}
	}

	/** 温度曲线图 */
	/*
	 * public void TemperatureCurve() { studyGraphItems = new
	 * ArrayList<StudyGraphItem>(); //studyGraphItems.add(new
	 * StudyGraphItem(data_s, s_s)); studyGraphItems.add(new
	 * StudyGraphItem("00:00", (float) 5.5)); studyGraphItems.add(new
	 * StudyGraphItem("01:00", (float) 6.5)); studyGraphItems.add(new
	 * StudyGraphItem("02:00", (float) 5.9)); studyGraphItems.add(new
	 * StudyGraphItem("03:00", (float) 7.5)); studyGraphItems.add(new
	 * StudyGraphItem("04:00", 8)); studyGraphItems.add(new
	 * StudyGraphItem("05:00", 11)); studyGraphItems.add(new
	 * StudyGraphItem("06:00", 13)); studyGraphItems.add(new
	 * StudyGraphItem("07:00", (float) 13.5)); studyGraphItems.add(new
	 * StudyGraphItem("08:00", (float) 9.55)); studyGraphItems.add(new
	 * StudyGraphItem("09:00", 12)); studyGraphItems.add(new
	 * StudyGraphItem("10:00", (float) 7.66)); studyGraphItems.add(new
	 * StudyGraphItem("11:00", 8)); studyGraphItems.add(new
	 * StudyGraphItem("12:00", 5)); studyGraph.setData(studyGraphItems);
	 * pointList = studyGraph.getPoints();
	 * 
	 * }
	 */

	/** 按钮点击 */
	@Override
	public void onClick(View v) {

		if (v == menu) {
			// 侧滑菜单
			menus.toggle(true);
		} else if (v == warning) {
			// 扫一扫页面
			startActivity(new Intent(ControlActivity.this,
					CaptureActivity.class));
			// 侧滑菜单
			menus.showContent();
		} else if (v == information) {
			// 警告页面
			startActivity(new Intent(ControlActivity.this,
					WarningActivity.class));
			// 侧滑菜单
			menus.showContent();
		} else if (v == temperature) {
			// 温度曲线图
			chooseType = 1;
			//getUserInstrumentInformation();
			getConsoleGraphTemperature();
			temperature.setBackgroundResource(R.drawable.wendunliang);
			humidity.setBackgroundResource(R.drawable.shiduhui);
		} else if (v == humidity) {
			// 湿度曲线图
			chooseType = 2;
			//getUserInstrumentInformation();
			getConsoleGraphHumidity();
			temperature.setBackgroundResource(R.drawable.wenduhui);
			humidity.setBackgroundResource(R.drawable.shiduliang);
		} 
		else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(ControlActivity.this,
					PersonalCenterActivity.class));
			//startActivity(new Intent(ControlActivity.this,ResultStrDetailsActivity.class));
			// 侧滑菜单
			menus.showContent();
		}
	}

	/**
	 * 控制台曲线图温度获取接口
	 * 
	 * @return
	 */
	public void getConsoleGraphTemperature() {

		uid = app.getUid();
		mid = app.getTid();
		tid = "";
		tcomp = "";

		String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "tid=" + tid
				+ "&" + "comp=" + tcomp;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_CONSOLE_GRAPH_TEMPERATURE + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						tlist = new ArrayList<String>();
						if (temperatureList == null) {

							temperatureList = new ArrayList<TemperatureInfo>();

						} else {
							temperatureList.clear();
						}

						LogUtil.showLog("========请求控制台曲线图温度获取接口数据========",
								"response:" + response);
						try {
							// 接口返回的数据
							JSONObject comJson = new JSONObject(response);

							// JSONObject中的字段
							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									// JSONObject中的字段
									JSONArray array = comJson
											.getJSONArray("data");

									// studyGraphItems = new
									// ArrayList<StudyGraphItem>();
									int length = array.length();
									for (int i = 0; i < length; i++) {
										// JSONArray中的字段
										JSONObject jo = array.optJSONObject(i);

										data_t = (float) jo.getDouble("data");
										created_at_t = jo
												.getString("created_at");

										tid = jo.getString("id");

										// tcomp=jo.getString("comp");

										try {
											// 日期转换
											s_t = Utils.date(created_at_t);
											// 温度曲线图
											// studyGraphItems.add(new StudyGraphItem(s, data));

										} catch (Exception e) {
											e.printStackTrace();
										}

										tlist.add(tid);

										temperatureList
												.add(new TemperatureInfo(
														data_t, s_t));
									}
									LogUtil.showLog(tlist.get(0));
									LogUtil.showLog(tlist.get(49));
									if (tAdapter == null) {
										tAdapter = new TemperatureAdapter(
												ControlActivity.this,
												temperatureList);

										mHorizontalScrollView
												.setAdapter(tAdapter);

										tv_temperature
												.setText((int) temperatureList
														.get(49)
														.getTemperature()
														+ "");
									} else {
										
										mHorizontalScrollView
												.setAdapter(tAdapter);
										
										tAdapter.notifyDataSetChanged();

									}
									// studyGraph.setData(studyGraphItems);
									// pointList = studyGraph.getPoints();
									// navs.add(new TemperatureInfo(data_t,
									// s_t));
									
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
						
						//dismissProgressDialog();
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
		mid = app.getTid();

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
		mid = app.getHid();
		hid = "";
		hcomp = "";

		String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "hid=" + hid
				+ "&" + "comp=" + hcomp;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_CONSOLE_GRAPH_HUMIDITY + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						hlist = new ArrayList<String>();
						if (humidityList == null) {

							humidityList = new ArrayList<HumidityInfo>();

						} else {

							humidityList.clear();

						}
						LogUtil.showLog("========请求控制台湿度获取接口数据========",
								"response:" + response);
						try {
							// 接口返回的数据
							JSONObject comJson = new JSONObject(response);

							// JSONObject中的字段
							if (comJson.has("code")) {
								int code = comJson.getInt("code");

								// 请求成功
								if (code == 1) {
									// JSONObject中的字段
									JSONArray array = comJson
											.getJSONArray("data");

									int length = array.length();
									for (int i = 0; i < length; i++) {
										// JSONArray中的字段
										JSONObject jo = array.optJSONObject(i);
										data_h = (float) jo.getDouble("data");
										created_at_h = jo
												.getString("created_at");

										hid = jo.getString("id");
										// hcomp=jo.getString("comp");

										try {
											// 日期转换
											s_h = Utils.date(created_at_h);

										} catch (Exception e) {
											e.printStackTrace();
										}

										hlist.add(hid);
										humidityList.add(new HumidityInfo(
												data_h, s_h));
									}
									LogUtil.showLog(hlist.get(0));
									LogUtil.showLog(hlist.get(49));
									
									
									
									if (hAdapter == null) {

										hAdapter = new HumidityAdapter(
												ControlActivity.this,
												humidityList);
										mHorizontalScrollView
												.setAdapter(hAdapter);
										tv_humidity.setText((int) humidityList
												.get(49).getHumidity() + "");
									} else {

										mHorizontalScrollView
												.setAdapter(hAdapter);

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
						//dismissProgressDialog();
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
		//showProgressDialog();
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
										// JSONArray中的字段
										JSONObject jo = array.optJSONObject(i);
										type = jo.getString("type");
										mid = jo.getString("id");
										address = jo.getString("address");
										active=jo.getString("active");

										if (type.equals("0")) {
											app.setTid(mid);
											app.settActive(active);
										}else if(type.equals("1")){
											app.setHid(mid);
											app.sethActive(active);
										}
										
										// 把mid缓存到Application,可供所有activity使用
										app.setMid(mid);
										app.setType(type);

										LogUtil.showLog(
												"+++++++++++++mid+++++++++++++++++:",
												"mid：" + mid);
										LogUtil.showLog(
												"++++++++++++++address++++++++++++++++:",
												"address：" + address);
										LogUtil.showLog(
												"++++++++++++++type++++++++++++++++:",
												"type：" + type);

										// 如果机器类型为0，并且控制台类型为1，则为温度显示图
										if (type.equals("0") && chooseType == 1) {
											getConsoleGraphTemperature();
										} else if (type.equals("1")&& chooseType == 2) {
											getConsoleGraphHumidity();
										} else {
											//Toast.makeText(ControlActivity.this,"type:" + type+ "chooseType:"+ chooseType,Toast.LENGTH_LONG).show();
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
						//dismissProgressDialog();
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

	/** 获取新数据 */
	protected void addNewData() {
		// 温度
		if (chooseType == 1) {
			//showProgressDialog();
			uid = app.getUid();
			mid = app.getTid();
			tid = tlist.get(tlist.size() - 1);
			// 0 新数据 旧新数据
			tcomp = "0";

			String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "tid=" + tid
					+ "&" + "comp=" + tcomp;

			BaseStringRequest mStringRequest = new BaseStringRequest(
					Method.GET,
					Constants.RequestUrl.GET_CONSOLE_GRAPH_TEMPERATURE + url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							LogUtil.showLog(
									"========请求新数据控制台曲线图温度获取接口数据========",
									"response:" + response);
							try {
								// 接口返回的数据
								JSONObject comJson = new JSONObject(response);

								// JSONObject中的字段
								if (comJson.has("code")) {
									int code = comJson.getInt("code");

									// 请求成功
									if (code == 1) {
										// JSONObject中的字段
										JSONArray array = comJson
												.getJSONArray("data");

										int length = array.length();
										
										if (length==0) {
											Toast.makeText(ControlActivity.this, "没有最新温度！",
													Toast.LENGTH_LONG).show();
										}
										else if(length>0){
											
										for (int i = 0; i < length; i++) {
											// JSONArray中的字段
											JSONObject jo = array
													.optJSONObject(i);

											data_t = (float) jo
													.getDouble("data");
											created_at_t = jo
													.getString("created_at");
											tid=jo.getString("id");

											try {
												// 日期转换
												s_t = Utils.date(created_at_t);

											} catch (Exception e) {
												e.printStackTrace();
											}

											tlist.add(tid);
											
											temperatureList
													.add(new TemperatureInfo(
															data_t, s_t));
										}

										tv_temperature
												.setText((int) temperatureList
														.get(temperatureList.size() - 1)
														.getTemperature()
														+ "");

										mHorizontalScrollView
												.setAdapter(tAdapter);

										tAdapter.notifyDataSetChanged();
										
										}

										LogUtil.showLog(
												"==========控制台新数据曲线图温度获取接口请求成功:====",
												"获取的新数据：" + array);
									}

									// 请求失败
									else if (code == 0) {

										String msg = comJson.getString("msg");

										LogUtil.showLog(
												"==========控制台新数据曲线图温度获取接口请求失败:====",
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
							//dismissProgressDialog();
						}

					}, new Response.ErrorListener() {
						public void onErrorResponse(VolleyError error) {
							LogUtil.showLog("==========控制台新数据曲线图温度获取接口错误:====",
									"失败原因：" + error.toString());
						}
					});

			mRequestQueue.add(mStringRequest);
		}
		// 湿度
		else if (chooseType == 2) {
			//showProgressDialog();
			uid = app.getUid();
			mid = app.getHid();
			hid = hlist.get(hlist.size() - 1);
			// 0 新数据 1旧数据
			hcomp = "0";

			String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "hid=" + hid
					+ "&" + "comp=" + hcomp;

			BaseStringRequest mStringRequest = new BaseStringRequest(
					Method.GET, Constants.RequestUrl.GET_CONSOLE_GRAPH_HUMIDITY
							+ url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							LogUtil.showLog("========请求控制台新数据湿度获取接口数据========",
									"response:" + response);
							try {
								// 接口返回的数据
								JSONObject comJson = new JSONObject(response);

								// JSONObject中的字段
								if (comJson.has("code")) {
									int code = comJson.getInt("code");

									// 请求成功
									if (code == 1) {
										// JSONObject中的字段
										JSONArray array = comJson
												.getJSONArray("data");

										int length = array.length();
										
										if (length==0) {
											Toast.makeText(ControlActivity.this, "没有最新湿度！",
													Toast.LENGTH_LONG).show();
											
										}else if(length>0){
											
										for (int i = 0; i < length; i++) {
											// JSONArray中的字段
											JSONObject jo = array
													.optJSONObject(i);
											data_h = (float) jo
													.getDouble("data");
											created_at_h = jo
													.getString("created_at");
											hid=jo.getString("id");

											try {
												// 日期转换
												s_h = Utils.date(created_at_h);

											} catch (Exception e) {
												e.printStackTrace();
											}

											hlist.add(hid);
											
											humidityList.add(new HumidityInfo(
													data_h, s_h));
										}

										tv_humidity.setText((int) humidityList
												.get(humidityList.size() - 1).getHumidity() + "");

										mHorizontalScrollView
												.setAdapter(hAdapter);

										tAdapter.notifyDataSetChanged();
										}

										LogUtil.showLog(
												"==========控制台曲线图新数据湿度获取接口请求成功:====",
												"获取的新数据：" + array);
									}
									// 请求失败
									else if (code == 0) {

										String msg = comJson.getString("msg");

										LogUtil.showLog(
												"==========请求控制台新数据湿度接口请求失败:====",
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
							//dismissProgressDialog();
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {

							LogUtil.showLog("==========控制台新数据湿度获取接口错误:====",
									"获取的新数据：" + error.toString());
						}
					});
			mRequestQueue.add(mStringRequest);
		}

	}

	/** 获取旧数据 */
	protected void addOldData() {
		// 温度
		if (chooseType == 1) {
			//showProgressDialog();
			uid = app.getUid();
			mid = app.getTid();
			tid = tlist.get(0);
			// 0 新数据 1旧数据
			tcomp = "1";

			String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "tid=" + tid
					+ "&" + "comp=" + tcomp;

			BaseStringRequest mStringRequest = new BaseStringRequest(
					Method.GET,
					Constants.RequestUrl.GET_CONSOLE_GRAPH_TEMPERATURE + url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							LogUtil.showLog(
									"========请求旧数据控制台曲线图温度获取接口数据========",
									"response:" + response);
							ArrayList<TemperatureInfo> tempList =new ArrayList<TemperatureInfo>();
							
							ArrayList<String> telist = new ArrayList<String>();
							try {
								// 接口返回的数据
								JSONObject comJson = new JSONObject(response);

								// JSONObject中的字段
								if (comJson.has("code")) {
									int code = comJson.getInt("code");

									// 请求成功
									if (code == 1) {
										// JSONObject中的字段
										JSONArray array = comJson
												.getJSONArray("data");

										int length = array.length();
										
										if (length==0) {
											Toast.makeText(ControlActivity.this, "没有旧温度！",
													Toast.LENGTH_LONG).show();
										}
										else if(length>0){
											
										for (int i = 0; i < length; i++) {
											// JSONArray中的字段
											JSONObject jo = array
													.optJSONObject(i);

											data_t = (float) jo
													.getDouble("data");
											created_at_t = jo
													.getString("created_at");
											
											tid=jo.getString("id");

											try {
												// 日期转换
												s_t = Utils.date(created_at_t);

											} catch (Exception e) {
												e.printStackTrace();
											}
											
											telist.add(tid);
											
											//添加临时数组
											tempList.add(new TemperatureInfo(
															data_t, s_t));
											
										}
										
										ArrayList <String> ttemp = new ArrayList<String>();
										ttemp.addAll(telist);
										ttemp.addAll(tlist);
										tlist.clear();
										tlist.addAll(ttemp);
										
										ArrayList <TemperatureInfo> temp = new ArrayList<TemperatureInfo>();
										temp.addAll(tempList);
										temp.addAll(temperatureList);
										
										temperatureList.clear();
										
										temperatureList.addAll(temp);
										
										tv_temperature
										.setText((int) temperatureList
												.get(tlist.size() -1)
												.getTemperature()
												+ "");
										
										mHorizontalScrollView.setAdapter(tAdapter);

										tAdapter.notifyDataSetChanged();
										
										}
						
										LogUtil.showLog(
												"==========控制台旧数据曲线图温度获取接口请求成功:====",
												"获取的旧数据：" + array);
									}

									// 请求失败
									else if (code == 0) {

										String msg = comJson.getString("msg");

										LogUtil.showLog(
												"==========控制台旧数据曲线图温度获取接口请求失败:====",
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
							//dismissProgressDialog();
						}

					}, new Response.ErrorListener() {
						public void onErrorResponse(VolleyError error) {
							LogUtil.showLog("==========控制台旧数据曲线图温度获取接口错误:====",
									"失败原因：" + error.toString());
						}
					});

			mRequestQueue.add(mStringRequest);
		}
		// 湿度
		else if (chooseType == 2) {
			//showProgressDialog();
			uid = app.getUid();
			mid = app.getHid();
			hid = hlist.get(0);
			// 0 新数据 1旧数据
			hcomp = "1";

			String url = "uid=" + uid + "&" + "mid=" + mid + "&" + "hid=" + hid
					+ "&" + "comp=" + hcomp;

			BaseStringRequest mStringRequest = new BaseStringRequest(
					Method.GET, Constants.RequestUrl.GET_CONSOLE_GRAPH_HUMIDITY
							+ url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							
							LogUtil.showLog("========请求控制台旧数据湿度获取接口数据========",
									"response:" + response);
							ArrayList<HumidityInfo> hempList =new ArrayList<HumidityInfo>();
							
							ArrayList<String> helist = new ArrayList<String>();
							try {
								// 接口返回的数据
								JSONObject comJson = new JSONObject(response);

								// JSONObject中的字段
								if (comJson.has("code")) {
									int code = comJson.getInt("code");

									// 请求成功
									if (code == 1) {
										// JSONObject中的字段
										JSONArray array = comJson
												.getJSONArray("data");

										int length = array.length();
										
										if (length==0) {
											Toast.makeText(ControlActivity.this, "没有旧湿度！",
													Toast.LENGTH_LONG).show();
										}
										
										else if(length>0){
										for (int i = 0; i < length; i++) {
											// JSONArray中的字段
											JSONObject jo = array
													.optJSONObject(i);
											data_h = (float) jo
													.getDouble("data");
											created_at_h = jo
													.getString("created_at");
											
											hid=jo.getString("id");
											try {
												// 日期转换
												s_h = Utils.date(created_at_h);

											} catch (Exception e) {
												e.printStackTrace();
											}

											helist.add(hid);
											
											hempList.add(new HumidityInfo(
													data_h, s_h));
										}

										
										ArrayList <String> htemp = new ArrayList<String>();
										htemp.addAll(helist);
										htemp.addAll(hlist);
										hlist.clear();
										hlist.addAll(htemp);
										
										ArrayList <HumidityInfo> temp = new ArrayList<HumidityInfo>();
										
										temp.addAll(hempList);
										temp.addAll(humidityList);
										
										humidityList.clear();
										
										humidityList.addAll(temp);
										
										tv_humidity.setText((int) humidityList
												.get(humidityList.size() -1).getHumidity() + "");
										
										mHorizontalScrollView
										.setAdapter(hAdapter);
										
										hAdapter.notifyDataSetChanged();
										
										}

										LogUtil.showLog(
												"==========控制台曲线图旧数据湿度获取接口请求成功:====",
												"获取的旧数据：" + array);
									}
									// 请求失败
									else if (code == 0) {

										String msg = comJson.getString("msg");

										LogUtil.showLog(
												"==========请求控制台旧数据湿度接口请求失败:====",
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
							//dismissProgressDialog();
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {

							LogUtil.showLog("==========控制台旧数据湿度获取接口错误:====",
									"获取的旧数据：" + error.toString());
						}
					});
			mRequestQueue.add(mStringRequest);
		}

	}
	
	/**点击返回键退出*/
	@Override
	   public void onBackPressed() {  
		menus.showContent();
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
		}  
	
	   /**没有最新版本*/
	   public void updated() {  
		new AlertDialog.Builder(this).setTitle("当前已为最新版本！")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).show();
		}  
}
