package com.peichong.observer.capture;

import java.io.IOException;
import java.lang.reflect.Field;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


import com.peichong.observer.R;
import com.peichong.observer.activities.BaseActivity;
import com.peichong.observer.application.ObserverApplication;
import com.peichong.observer.tools.StringUtil;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/** 	
 * TODO:   	扫码页面
 * @author:   wy 
 * @version:  V1.0 
 */
public class CaptureActivity  extends BaseActivity implements OnClickListener{

	/** 应用程序全局属性 */
	private ObserverApplication app;
	
	/**返回*/
	private ImageButton ib_return;
	
	
	/******************扫描二维码**************************/
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CameraManager mCameraManager;

	private TextView scanResult;
	private FrameLayout scanPreview;
	private Button scanRestart;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private ImageScanner mImageScanner = null;

	static {
		System.loadLibrary("iconv");
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		
		setContentView(R.layout.activity_information);
		// 拿到application对象
		app = (ObserverApplication) getApplication();
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initUi();
		addEvents();
		initViews();
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
		
		scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
		scanResult = (TextView) findViewById(R.id.capture_scan_result);
		scanRestart = (Button) findViewById(R.id.capture_restart_scan);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
	}
	
	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		
		if (v==ib_return) {
			//主界面控制台
			finish();
		}
	}
	
	/**点击重新扫描*/
	private void addEvents() {
		scanRestart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					scanResult.setText("扫描中...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});
	}

	private void initViews() {
		mImageScanner = new ImageScanner();
		mImageScanner.setConfig(0, Config.X_DENSITY, 3);
		mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

		autoFocusHandler = new Handler();
		mCameraManager = new CameraManager(this);
		try {
			mCameraManager.openDriver();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mCamera = mCameraManager.getCamera();
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		scanPreview.addView(mPreview);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.85f);
		animation.setDuration(3000);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.REVERSE);
		scanLine.startAnimation(animation);
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};
	
	/**处理扫描结果*/
	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Size size = camera.getParameters().getPreviewSize();

			// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < size.height; y++) {
				for (int x = 0; x < size.width; x++)
					rotatedData[x * size.height + size.height - y - 1] = data[x
							+ y * size.width];
			}

			// 宽高也要调整
			int tmp = size.width;
			size.width = size.height;
			size.height = tmp;

			initCrop();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(rotatedData);
			barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
					mCropRect.height());

			int result = mImageScanner.scanImage(barcode);
			String resultStr = null;

			if (result != 0) {
				SymbolSet syms = mImageScanner.getResults();
				for (Symbol sym : syms) {
					resultStr = sym.getData();
				}
			}

			//如果扫描结果不为空
			if (!TextUtils.isEmpty(resultStr)) {
				//判断是不是数字 如果是数字代表条形码
				if (StringUtil.isNum(resultStr)) {
					previewing = false;
					mCamera.setPreviewCallback(null);
					mCamera.stopPreview();

					scanResult.setText("扫描结果:" + resultStr);
					barcodeScanned = true;
				}
				else{
					Toast.makeText(CaptureActivity.this, "扫描的不是条形码！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = mCameraManager.getCameraResolution().y;
		int cameraHeight = mCameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
