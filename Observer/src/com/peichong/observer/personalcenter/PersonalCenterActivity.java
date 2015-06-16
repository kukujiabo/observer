package com.peichong.observer.personalcenter;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**点击保存 上传头像*//*
	private TextView ok;*/
	
	/**用户id*/
	private String uid;
	
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
	
	/**拍照*/
	private static final int PHOTO_REQUEST_CAMERA = 1;
	/**从相册中选择*/
	private static final int PHOTO_REQUEST_GALLERY = 2;
	/**结果*/
	private static final int PHOTO_REQUEST_CUT = 3;

	private Bitmap bitmap;
	
	private String photo;

	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.png";
	private File tempFile;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_personalcenter);
		
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		initUi();
		
		//修改电话
		String str=app.getPhone();
		show_phone.setText(str.substring(0, 7)+"****");
		
		//修改姓名
		show_name.setText(app.getName());
		
		//修改头像
		String s="http://218.244.135.148:8080"+app.getUrl().trim();
		if (app.getUrl().equals("") ||app.getUrl()==null) {
			Bitmap b=BitmapFactory.decodeResource(this.getResources(), R.drawable.touxiangthree);
			show_touxiang.setImageBitmap(Utils.CutPicture(b, 60, 60));
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
		/*ok=(TextView) findViewById(R.id.ok);
		ok.setOnClickListener(this);*/
		
	}

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
//			Intent intent = new Intent(this,PicSelectActivity.class); 
//			startActivityForResult(intent, 0x123);
			openPictureSelectDialog();
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
		/*//上传头像
		else if(v==ok){
			//upload();
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
	
	
    
	/**打开对话框**/
	private void openPictureSelectDialog() {
		//自定义Context,添加主题
		Context dialogContext = new ContextThemeWrapper(PersonalCenterActivity.this, android.R.style.Theme_Light);
		String[] choiceItems= new String[2];
		choiceItems[0] = "相机拍摄";  //拍照
		choiceItems[1] = "本地相册";  //从相册中选择
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1,choiceItems);
		//对话框建立在刚才定义好的上下文上
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("选择图片");
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:  //相机
					//doTakePhoto();
					camera();
					break;
				case 1:  //从图库相册中选取
					//doPickPhotoFromGallery();
					gallery();
					break;
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
	}	
	
	
	/**上传头像*/
	public void upload() {
		uid=app.getUid();
		//系统时间
		Date d = new Date();
		//文件名为uid+当前系统时间
		String filename = Base64.encodeToString((uid + d.toString()).getBytes(), Base64.DEFAULT).trim()+".png".trim();
		
		try {
			if (bitmap==null) {
			Toast.makeText(PersonalCenterActivity.this, "请选择图片！", Toast.LENGTH_SHORT).show();
		}else{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			byte[] buffer = out.toByteArray();

			// 将图片流以字符串形式存储下来  
			byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
			photo = new String(encode);
			RequestParams params = new RequestParams();
			params.put("uid", uid);
			params.put("filename", filename);
			params.put("photo", photo);
			
			String url = "http://218.244.135.148:8080/user/profileImageUpload";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,byte[] responseBody) {
					try {
						if (statusCode == 200) {

							Toast.makeText(PersonalCenterActivity.this, "头像上传成功!", Toast.LENGTH_SHORT)
									.show();
							//如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换  
							// Bitmap dBitmap = BitmapFactory.decodeFile(photo);  
					        //BitmapDrawable drawable = new BitmapDrawable(dBitmap);
					        //show_touxiang.setBackgroundDrawable(drawable);  
					        // show_touxiang.setImageBitmap(dBitmap); 
							// 接口返回的数据
							JSONObject comJson = new JSONObject(new String(responseBody));
							String s = comJson.getString("pic_url");
							app.setUrl(s);
							String url="http://218.244.135.148:8080"+app.getUrl().trim();
							new DownLoadImage(show_touxiang).execute(url);
					         
						} else {
							Toast.makeText(PersonalCenterActivity.this,
									"网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Toast.makeText(PersonalCenterActivity.this,
							"网络访问异常，错误码  > " + statusCode, Toast.LENGTH_SHORT).show();

				}
			});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**从相册获取*/
	public void gallery() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/**拍照获取*/
	public void camera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	/**传值*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//相册
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
			}

		} 
		//照相
		else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(PersonalCenterActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			
			try {
				bitmap = data.getParcelableExtra("data");
				//修改头像
				//show_touxiang.setImageBitmap(bitmap);
				//boolean delete = tempFile.delete();
				//System.out.println("delete = " + delete);
				upload();
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(PersonalCenterActivity.this, "设置失败！", Toast.LENGTH_SHORT).show();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**剪切图片*/
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 60);
		intent.putExtra("outputY", 60);
		// 图片格式
		intent.putExtra("outputFormat", "PNG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
	
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**弹出对话框修改昵称*//*
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
    }*/
}