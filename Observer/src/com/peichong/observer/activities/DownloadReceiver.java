package com.peichong.observer.activities;

import java.io.File;

import com.peichong.observer.tools.SharedPreferencesUtils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;


/** 
 * TODO:   	监听下载完成
 * @author:   wy 
 * @version:  V1.0 
 */
public class DownloadReceiver extends BroadcastReceiver{
	public DownloadReceiver() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {  
			Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);   
			
			String downloadId = SharedPreferencesUtils.getData(context, "downloadId");
			
			if(downloadId.equals(id + "")) {
				//获取下载的安装包路径
				Query query = new Query();  
	            query.setFilterById(id);  
	            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);  
	            Cursor cursor = downloadManager.query(query);  
	            String  path = "";
	            int columnCount = cursor.getColumnCount();
	            while(cursor.moveToNext()) {  
	                for (int j = 0; j < columnCount; j++) {  
	                    String columnName = cursor.getColumnName(j);  
	                    String string = cursor.getString(j);  
	                    if(columnName.equals("local_filename")) {  
	                        path = string;  
	                    }
	                }  
	            }  
	            cursor.close();  
	            //下载文件后自动打开
	            
	            if(!path.equals("") && path.endsWith("apk")) {
	            	Intent intentOpen = new Intent(Intent.ACTION_VIEW);
		            intentOpen.addCategory(Intent.CATEGORY_DEFAULT);
					intentOpen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentOpen.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
					context.startActivity(intentOpen);
	            }
					
			}
			
		}
	}
}
