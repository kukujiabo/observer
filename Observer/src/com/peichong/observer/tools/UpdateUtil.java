package com.peichong.observer.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** 
 * TODO:   更新工具
 * @author:   wy 
 * @version:  V1.0 
 */
public class UpdateUtil {
	private static boolean checkVersion(String version) {
        Pattern pattern = Pattern.compile("[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}");
        Matcher matcher = pattern.matcher(version);
        if (matcher.find())
            return true;
        return false;
    }
	
	/**
     * 根据服务器版本号和客户端版本号检查是否需要弹出更新提示框， 请在执行本类的update之前进行判断，方便后续进入的方法
     * 
     * @param serverVersion
     *            当前服务器版本号
     * @param clientVersion
     *            当前客户端版本号
     * @return boolean 是否需要提示升级
     */
    public static boolean checkUpdate(String serverVersion, String clientVersion) {
        if (checkVersion(serverVersion) && checkVersion(clientVersion))
            if (parseVersion(serverVersion) > parseVersion(clientVersion))
                return true;
        return false;
    }
    
    private static int parseVersion(String version) {
        int versionInteger = 0;
        String versionString = "";
        String[] arr = version.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            while (s.length() < 4)
                s = "0" + s;
            arr[i] = s;
        }
        for (int i = 0; i < arr.length; i++)
            versionString += arr[i];
        versionInteger = Integer.parseInt(versionString);
        return versionInteger;
    }
}
