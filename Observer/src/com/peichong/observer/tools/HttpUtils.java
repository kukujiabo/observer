package com.peichong.observer.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/*
 * edit by meroc 2015-03-14
 * provide network services.
 */
public class HttpUtils {
	
	public static String request (String requestMethod, String url, HashMap <String, String> params) throws Exception {
		
		URL uri = null;
		
		HttpURLConnection connection = null;
		
		InputStreamReader in = null;
		
		String result = null;
		
		String paramLine = null;
		
		uri = new URL(url);
		
		connection = (HttpURLConnection) uri.openConnection();
		
		connection.setDoInput(true);
		
		connection.setDoOutput(true);
		
		connection.setRequestMethod(requestMethod);
		
		connection.setReadTimeout(10000);
		
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		connection.setRequestProperty("Charset", "utf-8");
		
		OutputStreamWriter dop = new OutputStreamWriter(connection.getOutputStream());
		
		if (params != null && params.size() > 0) {
			
			Iterator iter = params.entrySet().iterator();
			
			while (iter.hasNext()) {
				
				Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
				
				String key = entry.getKey();
				
				String value = entry.getValue();
				
				paramLine += key + "=" + value + "&";
				
			}
			
			dop.write(paramLine);
			
		}
		
		dop.flush();
		
		in = new InputStreamReader(connection.getInputStream());
		
		BufferedReader bf = new BufferedReader(in);
		
		StringBuffer strBuffer = new StringBuffer();
		
		String line = null;
		
		while ((line = bf.readLine()) != null) {
			
			strBuffer.append(line);
			
		}
		
		result = strBuffer.toString();
		
		dop.close();
		
		return result;
		
	}

	public static HashMap <String, String> uploadFile (
			
			String method, 
		
			String filePath, 
			
			String apiUrl,
			
			int readTimeoutMillis,
			
			int connTimeoutMillis,
			
			HashMap <String, String> params,
			
			Handler handler			//发送上传进度的handler
		
		) throws Exception {
		
		File f = null;
		
        String end = "\r\n";
        
        String twoHyphens = "--";
        
        String boundary = "*****";

        f = new File(filePath);
        
        if (f == null || (!f.exists())) {
        	
        	throw new Exception ("file not exists.");
        	
        }
        
        long requestTime = System.currentTimeMillis();
        
        long responseTime = 0;
        
        String paramsLine = "";
        
        if (params != null && params.size() == 0) {
        	
        	Iterator<String> iter = params.keySet().iterator();
        	
        	while (iter.hasNext()) {
        	
        		String key = iter.next();
        		
        		String value = params.get(key);
        		
        		paramsLine += key + "=" + value + "&";
        		
        	}
        	
        }
        
        URL url = new URL(apiUrl + "?" + paramsLine);
        
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        
        /*
         * 设置读取流的最大等待时间
         */
        conn.setReadTimeout(readTimeoutMillis);
        
        /*
         * 设置链接等待最长时间
         */
        conn.setConnectTimeout(connTimeoutMillis);
        
        /*
         * 允许input, output 不适用 cache
         */
        conn.setDoInput(true);
        
        conn.setDoOutput(true);
        
        conn.setUseCaches(false);
        
        /*
         * 设置传送method
         */
        conn.setRequestMethod(method);
        
        /*
         * 设置property 
         */
        conn.setRequestProperty("charset", "utf-8");
        
        conn.setRequestProperty("connection", "keep-alive");
        
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        
        /*
         * 设置dataoutputstream
         */
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        
        dos.writeBytes(twoHyphens + boundary + end);
        
        String fileKey = "uploadedFile";

        StringBuffer sb = new StringBuffer();
        
        sb.append("Content-Disposition:form-data; name=\"" +
        		
        	fileKey + "\"; filename=\"" + f.getName() + "\"" + end);
        
        sb.append("Content-Type:image/*" + end );
        
        dos.writeBytes(sb.toString());
        
        /*
         * 取得要上传图片的FileInputStream
         */
        FileInputStream fis = new FileInputStream(f);
        
        byte [] bytes = new byte[1024];
        
        int len = 0;
        
        int curLen = 0;
        
        int fileSize = fis.available();
        
        /*
         * 文件写入dos
         */
        while ((len = fis.read(bytes)) != -1) {
        	
        	curLen += len;
        	
        	dos.write(bytes, 0, len);
        	
        	/*
        	 * 如果需要显示上传进度，传如非空handler
        	 */
        	if (handler != null) {
        	
	        	Message proMsg = new Message();
	        	
	        	proMsg.what = 11;
	        	
	        	proMsg.arg1 = curLen;
	        	
	        	proMsg.arg2 = fileSize;
	        	
	        	handler.sendMessage(proMsg);
        	
        	}
        	
        }
        
        dos.write(end.getBytes());
        
        dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
        
        fis.close();
        
        dos.flush();
        
        /*
         * 需要返回的hashMap
         */
        HashMap <String, String> resultMap = new HashMap <String, String> ();
        
        
        /*
         * 获取服务器返回http code
         */
        int res = conn.getResponseCode();
        
        resultMap.put("code", String.valueOf(res));
        
        responseTime = System.currentTimeMillis();
        
        int duration = (int)(responseTime - requestTime)/1000;
        
        /*
         * 获取上传所用时间 
         */
        resultMap.put("time", String.valueOf(duration));
        
        /*
         * 获取服务器返回信息
         */
        InputStream is = conn.getInputStream();
            
        int ch;
            
        StringBuffer b = new StringBuffer();
            
        while ((ch = is.read()) != -1) {
            	
           b.append((char)ch);
            	
        }
            
        /*
         * 取得response内容
         */
        resultMap.put("res", b.toString());
        
        return resultMap;
		
	}
	
	/*
	 * function: dowload
	 * params: String url, String fpath, String fname 
	 * return: 
	 * 下载文件
	 */
	public static int download (
			
			String apiUrl, 
			
			String fpath, 
			
			String fname, 
			
			String method,
			
			int timeoutMillis,
			
			Handler handler			//显示下载进度的handler
		
		) throws IOException {
		
		URL url = new URL(apiUrl);
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setConnectTimeout(timeoutMillis);
		
		conn.setRequestMethod(method);
		
		int code = conn.getResponseCode();
		
		if (code == 200) {
			
			InputStream is = conn.getInputStream();
			
			FileUtils.createNewFile(fname, fpath, is, handler);
			
		}
		
		return code;
		
	}
	
	/**通过url加载图片*/
	public static Bitmap getBitmap(String path) throws IOException{

	    URL url = new URL(path);
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setConnectTimeout(5000);
	    conn.setRequestMethod("GET");
	    if(conn.getResponseCode() == 200){
	    InputStream inputStream = conn.getInputStream();
	    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	    return bitmap;
	    }
	    return null;
	    }
	
	/**通过url加载图片*/
	public static Bitmap getHttpBitmap(String url)throws IOException{
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
         
        return bitmap;
         
	}
	
}