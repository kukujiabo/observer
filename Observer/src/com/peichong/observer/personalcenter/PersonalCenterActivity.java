package com.peichong.observer.personalcenter;


import org.fireking.app.imagelib.widget.PicSelectActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.peichong.observer.R;
import com.peichong.observer.activities.ActivityUtil;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.activities.MainActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.slidingcurve.ControlActivity;
import com.peichong.observer.tools.DownLoadImage;
import com.peichong.observer.tools.Utils;


/** 
 * TODO:   	个人中心页面
 * @author:   wy 
 * @version:  V1.0 
 */
public class PersonalCenterActivity extends BaseActivity implements OnClickListener{

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
	
	/**设置头像*/
	private ImageButton set_touxiang;
	
	/**设置昵称*/
	private ImageButton set_name;
	
	/**退出当前帐号*/
	private ImageButton tuichu;
	
	/**显示头像*/
	private ImageView show_touxiang;
	
	/**显示昵称*/
	private TextView show_name;
	
	/**显示手机号码*/
	private TextView show_phone;
	
	public  String newName;
	
	private Bitmap bit;
	
	/******* 侧滑菜单 *******//*
	private ListView lv_set;// 设置菜单

	private MenuAdapter Menuadapter;
	
	private SlidingMenu menus;*/
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_personalcenter);
		
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		
		//修改昵称
		Intent intent = this.getIntent(); 
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			show_name.setText(app.getName());
		}else{
			String name = bundle.getString("msg");
			show_name.setText(name);
		}
		
		//修改电话
		show_phone.setText(app.getPhone());
		
		//修改头像
		String s="http://218.244.135.148:8080"+app.getUrl().trim();
		if (app.getUrl().equals("") ||app.getUrl()==null) {
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.touxiangthree);
			show_touxiang.setImageBitmap(Utils.CutPicture(b, 65, 40));
		}else{
			new DownLoadImage(show_touxiang).execute(s);
		}
		
	}
	
	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initUi() {
		
		ib_return=(ImageButton) findViewById(R.id.fanhui);
		ib_return.setOnClickListener(this);
		
		set_touxiang=(ImageButton) findViewById(R.id.set_touxiang);
		set_touxiang.setOnClickListener(this);
		
		set_name=(ImageButton) findViewById(R.id.set_name);
		set_name.setOnClickListener(this);
		
		tuichu=(ImageButton) findViewById(R.id.tuichu);
		tuichu.setOnClickListener(this);
		
		show_name=(TextView) findViewById(R.id.show_name);
		show_phone=(TextView) findViewById(R.id.show_phone);
		show_touxiang=(ImageView) findViewById(R.id.show_touxiang);
		
		/*warning = (ImageButton) findViewById(R.id.warning);
		information = (ImageButton) findViewById(R.id.information);

		warning.setOnClickListener(this);
		information.setOnClickListener(this);
		
		menu = (ImageButton) findViewById(R.id.menu);
		menu.setOnClickListener(this);

		// configure the SlidingMenu
		menus = new SlidingMenu(this);
		menus.setMode(SlidingMenu.RIGHT);
		 //设置触摸屏幕的模式
		menus.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//menus.setShadowWidthRes(R.dimen.shadow_width);
		//menus.setShadowDrawable(R.drawable.shadow);

		 //设置滑动菜单视图的宽度
		menus.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menus.setFadeDegree(0.35f);
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
	}
	/** 菜单抽屉实体类 *//*
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
		//返回控制台主界面
		if (v==ib_return) {
			startActivity(new Intent(PersonalCenterActivity.this, ControlActivity.class));
			finish();
		}
		//设置头像
		else if(v==set_touxiang){
			Intent intent = new Intent(this,PicSelectActivity.class); 
			startActivityForResult(intent, 0x123);
		}
		//设置昵称
		else if(v==set_name){
			// rename();
			startActivity(new Intent(PersonalCenterActivity.this, SetNameActivity.class));
		}
		//退出当前帐号
		else if(v==tuichu){
			Esc();
		}
		/*if (v == menu) {
			// 侧滑菜单
			menus.toggle(true);
		}
		if (v == warning) {
			// 警告页面
			startActivity(new Intent(PersonalCenterActivity.this, WarningActivity.class));
		} else if (v == information) {
			// 资讯页面
			startActivity(new Intent(PersonalCenterActivity.this,
					InformationActivity.class));
		} else if (v == user_icon) {
			// 个人中心
			startActivity(new Intent(PersonalCenterActivity.this,
					PersonalCenterActivity.class));
		}*/
	}
	
	/**退出当前帐号*/
	public void Esc(){
		new AlertDialog.Builder(this).setTitle("退出当前帐号？")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 点击“确认”后的操作
				ActivityUtil.finishAll();
				finish();
				startActivity(new Intent(PersonalCenterActivity.this,MainActivity.class));
			}
		})
		.setNegativeButton("返回", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 点击“返回”后的操作,这里不设置没有任何操作
			}
		}).show();
      }  
	
	/**弹出对话框修改昵称*/
	public void rename() {
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.myedit, null);
        //内部局部类，只能访问方法的final类型的变量
        final EditText mname_edit = (EditText) textEntryView
                .findViewById(R.id.rename_edit);

        new AlertDialog.Builder(PersonalCenterActivity.this)
                .setView(textEntryView)
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {

                            }

                        })
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                if (!mname_edit.getText().toString().equals("")) {
                                    newName = mname_edit.getText().toString();
                                }
                                show_name.setText(newName);
                            }

                        }).show();
    }
    
}