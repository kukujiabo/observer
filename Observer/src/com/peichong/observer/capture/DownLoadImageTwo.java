package com.peichong.observer.capture;

import java.io.InputStream;

import com.peichong.observer.tools.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


/** 
 * TODO:   在4.0系统中显示网络图片 
 * @author:   wy 
 * @version:  V1.0 
 */
public class DownLoadImageTwo extends AsyncTask<String, Void, Bitmap> {  
    ImageView imageView;  
  
    public DownLoadImageTwo(ImageView imageView) {  
        // TODO Auto-generated constructor stub  
        this.imageView = imageView;  
    }  
  
    @Override
	public Bitmap doInBackground(String... urls) {  
        String url = urls[0];  
        Bitmap tmpBitmap = null;  
        try {  
            InputStream is = new java.net.URL(url).openStream();  
            tmpBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return Utils.CutPicture(tmpBitmap, imageView.getWidth(), 100); 
    }  
  
    @Override  
    protected void onPostExecute(Bitmap result) {  
        // TODO Auto-generated method stub  
        imageView.setImageBitmap(result);  
    }  
}  