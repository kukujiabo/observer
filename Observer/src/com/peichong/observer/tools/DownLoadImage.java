/**
 * 
 */
package com.peichong.observer.tools;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


/** 
 * TODO:   在4.0系统中显示网络图片 
 * @author:   wy 
 * @version:  V1.0 
 */
public class DownLoadImage extends AsyncTask<String, Void, Bitmap> {  
    ImageView imageView;  
  
    public DownLoadImage(ImageView imageView) {  
        // TODO Auto-generated constructor stub  
        this.imageView = imageView;  
    }  
  
    @Override
	public Bitmap doInBackground(String... urls) {  
        // TODO Auto-generated method stub  
        String url = urls[0];  
        Bitmap tmpBitmap = null;  
        try {  
            InputStream is = new java.net.URL(url).openStream();  
            tmpBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return Utils.CutPicture(tmpBitmap, 60, 60); 
    }  
  
    @Override  
    protected void onPostExecute(Bitmap result) {  
        // TODO Auto-generated method stub  
        imageView.setImageBitmap(result);  
    }  
}  