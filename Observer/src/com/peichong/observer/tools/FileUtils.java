package com.peichong.observer.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class FileUtils {

	public static final String FILENAME = "filename";

	private static FileUtils fileUtil = null;

	// 手机存储机制
	private SharedPreferences sp = null;

	public static FileUtils getFileUtil() {

		if (fileUtil == null) {
			return new FileUtils();
		}
		return fileUtil;
	}
	
	/**
	 * 创建文件目录，并且返回该文件目录 如果该目录已经存在，则返回该目录
	 * 
	 * @param dirPath
	 * @return File
	 */
	public static File createDir(String dirPath) {
		File file = new File(dirPath);
		if (file.exists())
			return file;
		boolean b = file.mkdirs();
		if (b)
			return file;
		else
			return null;
	}

	/**
	 * 创建文件并且返回该新创建的文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() && file.isFile())
			return file;
		boolean b = file.createNewFile();
		if (b)
			return file;
		else
			return null;
	}

	/**
	 * 删除文件夹或者文件
	 * 
	 * @param filePath
	 * @return boolean
	 */
	public static boolean delete(String filePath) {
		Log.d("DeleteFile", filePath);
		File file = new File(filePath);
		boolean b = false;
		if (file.exists()) {
			b = true;
			deleteFileOrDir(filePath);
		}
		return b;
	}

	/**
	 * 递归删除文件夹和文件
	 * 
	 * @param path
	 */
	private static void deleteFileOrDir(String path) {
		File file = new File(path);
		if (file.isFile())
			file.delete();
		else {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFileOrDir(f.getAbsolutePath());
			}
			file.delete();
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isExist(String filePath) {
		return new File(filePath).exists();
	}

	/**
	 * 根据InputStream和编码，返回String类型的字符串
	 * 
	 * @param iStream
	 * @param encoding
	 * @return String
	 */
	public static String getStringFromInputStream(InputStream iStream,
			String encoding) throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		if (encoding == null)
			isr = new InputStreamReader(iStream);
		else
			isr = new InputStreamReader(iStream, encoding);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null)
			sb.append(line + "\r\n");
		br.close();
		isr.close();
		iStream.close();
		return sb.toString();
	}

	/**
	 * 根据InputStream和编码，返回String类型的字符串
	 * 
	 * @param iStream
	 * @return String
	 */
	public static String getStringFromInputStream(InputStream iStream)
			throws IOException {
		return getStringFromInputStream(iStream, null);
	}

	/**
	 * 文件检测, 没有就创建
	 * 
	 * @param filePath
	 *            文件的父路径
	 * @param fileName
	 *            文件的名称
	 * @return 检测的文件, 如果失败返回null.
	 * */
	public static File detectPath(String filePath, String fileName)
			throws IOException {

		if (null == filePath || null == fileName)
			return null;

		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();

		file = new File(filePath + File.separator + fileName);
		if (!file.exists())
			file.createNewFile();

		return file;
	}

	// /**
	// * 文件检测, 没有就创建
	// * @param context 环境
	// * @param filePath 文件的绝对路径
	// * @return 检测的文件, 如果失败返回null.
	// * */
	// public static File detectPath(Context context, String filePath) throws
	// IOException, RuntimeException {
	//
	// if(!DeviceUtils.checkSDCard())
	// throw new
	// RuntimeException(context.getString(R.string.exc_sdcard_unavailable));
	//
	// if(!isFilePath(filePath))
	// throw new
	// RuntimeException(context.getString(R.string.exc_error_filepath));
	//
	// int divide = filePath.lastIndexOf(File.separator);
	// String name = filePath.substring(divide + File.separator.length());
	// String path = filePath.substring(0, divide);
	//
	// return detectPath(path, name);
	// }

	/**
	 * 文件路径验证
	 * 
	 * @param filePath
	 *            需要验证的文件路径
	 * @return 验证成功返回true, 否则返回false.
	 * */
	public static boolean isFilePath(String filePath) {

		if (TextUtils.isEmpty(filePath))
			return false;

		filePath = filePath.replaceAll("//", "/").trim();
		Pattern pattern = Pattern.compile("(^//.|^/|^[a-zA-Z])?:?/.+(/$)?");
		Matcher matcher = pattern.matcher(filePath);

		return matcher.matches();
	}

	
	
	/**
	 * 获取路径
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getPath(Context context, Uri uri) {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
			}
		}
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;

	}
    
 // 保存数据
 	public void setSharedPreferencesData(String key, String value,
 			String fileName, Context context) {

 		// 当前应用可访问
 		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
 		// 获得编辑器
 		Editor edit = sp.edit();
 		// 存入数据
 		edit.putString(key, value);
 		// 提交数据
 		edit.commit();
 	}

 	// 获取数据
 	public String getSharedPreferencesData(String key, String fileName,
 			Context context) {

 		String value = null;

 		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
 		// 获得数据
 		value = sp.getString(key, null);

 		return value;
 	}

 	// 删除数据
 	public void deleteSharedPreferencesData(String fileName, Context context) {

 		// 当前应用可访问
 		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
 		// 获得编辑器
 		Editor edit = sp.edit();
 		// 存入数据
 		edit.clear();
 		// 提交数据
 		edit.commit();
 	}
	
	
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
