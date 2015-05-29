package com.peichong.observer.analysislog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.handmark.pulltorefresh.library.ListViewUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.configure.Constants;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.BaseStringRequest;
import com.peichong.observer.tools.LogUtil;
import com.peichong.observer.warning.WarningActivity;
import com.peichong.observer.warning.WarningAdapter;

/**
 * TODO: 分析日志页面
 * 
 * @author: wy
 * @version: V1.0
 */
public class AnalysisLogActivity extends BaseActivity implements
		OnClickListener/*,OnRefreshListener2<ListView>*/{

	/** 菜单 *//*
	private ImageButton menu;
	*//** 警告 *//*
	private ImageButton warning;
	*//** 资讯 *//*
	private ImageButton information;

	*//** 个人中心 *//*
	private ImageButton user_icon;
*/
	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**返回*/
	private ImageButton ib_return;

	/******* 侧滑菜单 *******//*
	private ListView lv_set;// 设置菜单

	private MenuAdapter Menuadapter;
	
	private SlidingMenu menus;*/
	
	private ListView listView;
	
	
	private  AnalysisAdapter analysisAdapter;
	/*private ListView mListView;
	private PullToRefreshListView mPullListView;
	private int startNum = 1;
	private int endNum = 10;
	*//** 滑动获取数据判断 *//*
	public boolean loadData = false;
	private ArrayList<HashMap<String, Object>> mCpData = new ArrayList<HashMap<String, Object>>();
	*/
	
	/**标题*/
	private String  set_text;
	
	/**时间*/
	private String set_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysislog);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		//mPullListView.setRefreshing();
	}

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initUi() {
	/*	warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);

		warning.setOnClickListener(this);
		information.setOnClickListener(this);

		menu = (ImageButton) findViewById(R.id.menu);
		menu.setOnClickListener(this);

		// configure the SlidingMenu
		menus = new SlidingMenu(this);
		menus.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		menus.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// 设置滑动菜单视图的宽度
		menus.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
		menus.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 为侧滑菜单设置布局
		menus.setMenu(R.layout.activity_menu);

		user_icon = (ImageButton) findViewById(R.id.user_icon);
		user_icon.setOnClickListener(this);

		lv_set = (ListView) findViewById(R.id.lv_set);
		List<MenuInfo> list = initRightMenus();
		Menuadapter = new MenuAdapter(this, list);
		lv_set.setAdapter(Menuadapter);
		lv_set.setOnItemClickListener(this);*/
		
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(this);
		
		
		listView = (ListView) this.findViewById(R.id.list);
		listView.setDividerHeight(0);
		analysisAdapter = new AnalysisAdapter(this, getData());
		listView.setAdapter(analysisAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(AnalysisLogActivity.this, "点击:"+position,
						Toast.LENGTH_LONG).show();
			}
		});
		
		
		/*mPullListView = (PullToRefreshListView) findViewById(R.id.list);
		mPullListView.setOnRefreshListener(this);

		mListView = mPullListView.getRefreshableView();
		mListView.setScrollbarFadingEnabled(true);
		mListView.setSelector(android.R.color.transparent);
		mListView.setScrollBarStyle(AbsListView.SCROLLBARS_INSIDE_OVERLAY);
		
		analysisAdapter = new AnalysisAdapter(this, mCpData);
		mListView.setAdapter(analysisAdapter);

		ListViewUtil.initPullToRefreshLabel(this, mPullListView, true, true,
				true);*/
		
	}

/*	*//** 菜单抽屉实体类 *//*
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
	}*/
	
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "标题一");
		map.put("time", "2015-05-25");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "标题二");
		map.put("time", "2015-05-27");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "标题三");
		map.put("time", "2015-06-25");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "标题四");
		map.put("time", "2015-05-15");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "标题五");
		map.put("time", "2015-05-29");
		list.add(map);
		
		list.add(map);
		return list;
	}

	/** 条目点击 *//*
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {

		// 曲线图页面
		case 0:
			intent = new Intent(view.getContext(), ControlActivity.class);
			menus.toggle(true);
			break;

		// 资讯页面
		case 1:
			intent = new Intent(view.getContext(), InformationActivity.class);
			menus.toggle(true);
			break;

		// 警告页面
		case 2:
			intent = new Intent(view.getContext(), WarningActivity.class);
			menus.toggle(true);
			break;

		// 设置页面
		case 3:
			intent = new Intent(view.getContext(), SetActivity.class);
			menus.toggle(true);
			break;

		// 分析日志页面
		case 4:
			intent = new Intent(view.getContext(), AnalysisLogActivity.class);
			menus.toggle(true);
			break;

		// 设备管理页面
		case 5:
			intent = new Intent(view.getContext(), EquipmentMgmActivity.class);
			menus.toggle(true);
			break;

		// 版本更新页面
		case 6:
			intent = new Intent(view.getContext(), VersionUpdateActivity.class);
			menus.toggle(true);
			break;

		// 关于页面
		case 7:
			intent = new Intent(view.getContext(), AboutActivity.class);
			menus.toggle(true);
			break;
		}

		// 页面跳转
		if (intent != null) {
			// intent.putExtra("lv_item_id", id);
			startActivity(intent);
		}
	}*/

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		if (v==ib_return) {
			//主界面控制台
			startActivity(new Intent(AnalysisLogActivity.this, ControlActivity.class));
			finish();
		}
		/*if (v == menu) {
			// 侧滑菜单
			menus.toggle(true);
		}
		if (v == warning) {
			// 警告页面
			startActivity(new Intent(AnalysisLogActivity.this,
					WarningActivity.class));
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(AnalysisLogActivity.this,
					InformationActivity.class));
		} else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(AnalysisLogActivity.this,
					PersonalCenterActivity.class));
		}*/
	}
	
	
	/*  获取分享日志数据接口
	 * 
	 * @return*/
	 
	/*public void getDate() {

		uid = app.getUid();
		String start = "start=" + startNum;
		String end = "end=" + endNum;
		
		//type 0 温度 1 湿度
		//w_data: "75" 数据
		//up_down 0低 1高
		
		String url = "uid=" + uid + "&" + start + "&" + end;

		BaseStringRequest mStringRequest = new BaseStringRequest(Method.GET,
				Constants.RequestUrl.GET_WARNING + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						
						LogUtil.showLog("========请求分享日志接口数据========",
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

										HashMap<String, Object> item = new HashMap<String, Object>();
										set_text = jo.getString("type");
										set_time=jo.getString("w_data");
										
										item.put("type",set_text);
										item.put("w_data",set_time);
										mCpData.add(item);
									}
									
									LogUtil.showLog(	
											"==========分享日志接口请求成功:====",
											"获取的数据：" + array);
								}
								
								// 请求失败
								else if (code == 0) {

									String msg = comJson.getString("msg");

									LogUtil.showLog(
											"==========分享日志接口请求失败:====",
											"失败原因：" + msg);
								} 
							}
							
							analysisAdapter.notifyDataSetChanged();
							mPullListView.onRefreshComplete();
							if (comJson.getInt("totalCount") > mCpData.size()) {
								ListViewUtil.initPullToRefreshLabel(
										AnalysisLogActivity.this,
										mPullListView, false, true, true);
							} else {
								ListViewUtil.initPullToRefreshLabel(
										AnalysisLogActivity.this,
										mPullListView, false, true, false);
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					}
					
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						mPullListView.onRefreshComplete();
						ListViewUtil.initPullToRefreshLabel(AnalysisLogActivity.this,mPullListView, false, true, true);
						LogUtil.showLog("==========分享日志数据获取接口错误:====",
								"失败原因：" + error.toString());
					}
				});

		mRequestQueue.add(mStringRequest);
	}
	 */

	/**
	 * 
	 * 下拉刷新（重新加载第一页）
	 */
	/*@Override
	public void onPullDownToRefresh(
			PullToRefreshBase<ListView> pulltorefreshbase) {
		
		startNum = 1;
		endNum = 10;
		mCpData.clear();
		analysisAdapter.notifyDataSetChanged();
		getDate();
	}
	*/
	
	/**
	 * 
	 * 上拉刷新（加载更多）
	 */
	/*
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> pulltorefreshbase) {
		startNum += 10;
		endNum += 10;
		getDate();
			
	}*/
}
