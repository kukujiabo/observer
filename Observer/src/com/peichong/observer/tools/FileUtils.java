package com.peichong.observer.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class FileUtils {

	/*
	 * function: SDReady
	 * params: 
	 * return: boolean
	 * 判断SD卡是否插入
	 */
	public static boolean SDReady () {
		
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		
	}
	
	/*
	 * function: SDDirectory
	 * params:
	 * return: File
	 * 获取SD卡根目录
	 */
	public static File SDDirectory () {
		
		return Environment.getExternalStorageDirectory();
		
	}
	
	/*
	 * function: getAbsoluteDir
	 * params: f
	 * return: String
	 * 获取文件夹或文件的绝对路径
	 */
	public static String getFilePath (File f) {
		
		return f.getAbsolutePath();
		
	}
	
	/*
	 * function: fileSize
	 * params: f
	 * retrun: long
	 * 获取文件大小
	 */
	public static long fileSize(File f) throws IOException {
		
		long s = 0;
		
		if (f.exists()) {
			
			FileInputStream fis = new FileInputStream(f);
			
			s = fis.available();
			
		} else {
			
			s = 0;
			
		}
		
		return s;
		
	}
	
	/*
	 * function: dirSize
	 * params: d
	 * return long
	 * 获取文件夹大小
	 */
	public static long dirSize (File d) {
		
		long s = 0;
		
		File flist[] = d.listFiles();
		
		int len = flist.length;
		
		for (int i = 0; i < len; i++) {
			
			if (flist[i].isDirectory()) {
				
				s = s + dirSize(flist[i]);
				
			} else {
				
				s += flist[i].length();
				
			}
			
		}
		
		return s;
		
	}
	
	/*
	 * function formatFileSize
	 * params: size
	 * return: String
	 * 转换文件大小的输出格式
	 */
	public static String formatFileSize (long size) {
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		String formatedSize = "";
		
		if (size < 1024) {
			
			formatedSize = df.format((double) size) + "B";
			
		} else if (size < 1048576) {
			
			formatedSize = df.format((double) size/1024) + "K";
			
		} else if (size < 1073741824) {
			
			formatedSize = df.format((double) size/1048576) + "M";
			
		} else {
			
			formatedSize = df.format((double) size/1073741824) + "G";
			
		}
		
		return formatedSize;
		
	}
	
	/*
	 * function dirCount
	 * params: d
	 * return: long
	 * 获取一个目录下的文件个数
	 */
	public long dirCount(File d) {
		
		long s = 0, len = 0;
		
		File files[] = d.listFiles();
		
		s = len = files.length;
		
		for (int i = 0; i < len; i++) {
			
			if (files[i].isDirectory()) {
				
				s += dirCount(files[i]);
				
				s --;
				
			}
			
		}
		
		return s;
		
	}
	
	
	/*
	 * function: getRelatDir
	 * params: f
	 * return: String
	 * 获取文件或文件夹的相对路径
	 */
	public static String getRelatDir (File f) {
		
		return f.getPath();
		
	}
	
	/*
	 * function: getParent
	 * params: f
	 * return: String
	 * 获取文件或文件夹的父目录
	 */
	public static String getParent (File f) {
		
		return f.getParent();
		
	}
	
	/*
	 * function: save
	 * params: fname, fcontent, context
	 * return: void
	 * 保存文件
	 */
	public static void save (String fname, String fcontent, Context context) throws IOException {
		
		FileOutputStream fileOuputStream = context.openFileOutput(fname, Context.MODE_PRIVATE);
		
		fileOuputStream.write(fcontent.getBytes());
		
	}
	
	/*
	 * function: read
	 * params: fname, context
	 * return String
	 * 读取文件
	 */
	public static String read (String fname, Context context) throws IOException {
		
		FileInputStream fileInputStream = context.openFileInput(fname);
		
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		
		byte [] buffer = new byte[1024];
		
		int len = 0;
		
		while ((len = fileInputStream.read(buffer)) > 0) {
			
			byteArray.write(buffer);
			
		}
		
		return byteArray.toString();
		
	}
	
	/*
	 * function: createNewFile
	 * params: fname, fdir
	 * return: 
	 * 创建新文件
	 */
	public static void createNewFile (String fname, String fdir, InputStream is, Handler handler) throws IOException {
		
		File d = new File(fdir);
		
		/*
		 * 若目录不存在，则创建目录 
		 */
		if (!d.exists()) {
			
			d.mkdirs();
			
		}
		
		String newFile = fdir.lastIndexOf("/") == fdir.length() - 1 ? fdir + fname : fdir + "/" + fname;

		File f = new File(newFile);
		
		/*
		 * 若存在同名文件，则删除
		 */
		if (f.exists()) {
			
			f.delete();
			
		}
	
		OutputStream os = new FileOutputStream(newFile);
		
		byte [] bt = new byte[1024];
		
		int fileSize = is.available();
		
		int currentLength = 0;
		
		int len = 0;
		
		while ((len = is.read(bt)) > 0) {
			
			os.write(bt, 0, len);

			currentLength += len;
			
			if (handler != null) {
				
				Message proMsg = new Message();
				
				proMsg.what = 12;
				
				proMsg.arg1 = currentLength;
				
				proMsg.arg2 = fileSize;
				
				handler.sendMessage(proMsg);
				
			}
			
		}
		
		os.close();
		
		is.close();
		
	}

}
