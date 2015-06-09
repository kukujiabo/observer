package com.peichong.observer.personalcenter;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
	
	private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	
	private String imgPath  = "";
	private String imgName = "";
	
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
					doTakePhoto();
					break;
				case 1:  //从图库相册中选取
					doPickPhotoFromGallery();
					break;
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
		if(resultCode == RESULT_OK) {  //返回成功
			switch (requestCode) {
			case PHOTO_WITH_CAMERA:  {//拍照获取图片
				String status = Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)) { //是否有SD卡
					
					Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
					
					imgName = createPhotoFileName();
					//写一个方法将此文件保存到本应用下面啦
	            	savePicture(imgName,bitmap);

	            	if (bitmap != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = Utils.CutPicture(bitmap, 65, 40);
						
						show_touxiang.setImageBitmap(smallBitmap);
					}
	            	//Toast.makeText(PersonalCenterActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(PersonalCenterActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
				}
				break;
			}
				case PHOTO_WITH_DATA:  {//从图库中选择图片
					ContentResolver resolver = getContentResolver();
					//照片的原始资源地址
					Uri originalUri = data.getData();  

					try {
						 //使用ContentProvider通过URI获取原始图片
						Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
						
						imgName = createPhotoFileName();
		       			//写一个方法将此文件保存到本应用下面啦
		            	savePicture(imgName,photo);
		            	
		            	if (photo != null) {
							//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
							Bitmap smallBitmap = Utils.CutPicture(photo, 65, 40);
							
							show_touxiang.setImageBitmap(smallBitmap);
						}
		            	//Toast.makeText(PersonalCenterActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**从相册获取图片**/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");  // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action 
		startActivityForResult(intent, PHOTO_WITH_DATA); //取得相片后返回到本画面
	}
	
	/**拍照获取相片**/
	private void doTakePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
	   
	    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       
	    //直接使用，没有缩小  
        startActivityForResult(intent, PHOTO_WITH_CAMERA);  //用户点击了从相机获取
	}
	
	/**创建图片不同的文件名**/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());  //系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}
	
	 /**获取文件路径**/
	 public String uri2filePath(Uri uri)  
	    {  
	        String[] projection = {MediaStore.Images.Media.DATA };  
	        Cursor cursor = managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 
	 /**保存图片到本应用下**/
	 private void savePicture(String fileName,Bitmap bitmap) {
		   
			FileOutputStream fos =null;
			try {//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
				fos = PersonalCenterActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(null != fos) {
						fos.close();
						fos = null;
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
}